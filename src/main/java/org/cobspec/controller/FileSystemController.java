package org.cobspec.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

import javaslang.control.Option;
import javaslang.control.Try;
import javaslang.collection.List;

import org.apache.tika.Tika;

import org.cobspec.html.Index;
import org.cobspec.html.Link;
import org.cobspec.template.IndexTemplate;
import org.flint.exception.HttpException;
import org.flint.exception.NotFoundHttpException;
import org.flint.exception.PreconditionFailedHttpException;
import org.flint.exception.UnsupportedMediaTypeHttpException;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;
import org.unixdiff.UnixPatch;
import org.util.FileSystem;

public class FileSystemController {
    private static final Tika contentType = new Tika();

    private Path rootPath;

    public FileSystemController(Path rootPath) {
        this.rootPath = rootPath;
    }

    public Response get(Request request) throws IOException {
        Path targetPath = getTargetPath(request);
        if (Files.isDirectory(targetPath)) {
            Path customIndexPath = targetPath.resolve("index");
            if (Files.exists(customIndexPath)) {
                return retrieve(customIndexPath);
            } else {
                return index(targetPath);
            }
        } else {
            return retrieve(targetPath);
        }
    }

    private Response retrieve(Path targetPath) throws IOException {
        ensureFileExists(targetPath);

        Response response = Response.create();
        response.setHeader("Content-Type", contentType.detect(targetPath));
        response.setBody(Files.newInputStream(targetPath));

        return response;
    }

    private Response index(Path targetPath) throws IOException {
        ensureFileExists(targetPath);

        List<Link> links = List.ofAll(Files.walk(targetPath, 1)
            .filter(path -> Try.of(() -> !Files.isHidden(path) && !Files.isSameFile(path, targetPath)).get())
            .map((filePath) -> {
                String href = rootPath.relativize(filePath).toString();
                String display = targetPath.relativize(filePath).toString();
                return new Link(href, display);
            })
            .collect(Collectors.toList()));
        Index index = new Index("/" + rootPath.relativize(targetPath).toString(), links);

        Response response = Response.create();
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setBody(IndexTemplate.render(index));

        return response;
    }

    public Response delete(Request request) throws IOException {
        Files.delete(getTargetPath(request));
        return Response.create();
    }

    public Response write(Request request, String body) throws IOException {
        return _write(Paths.get(request.getPath()), body);
    }

    public Response write(Request request) throws IOException {
        return write(request, request.getBody());
    }

    public Response write(Request request, Path location, String body) throws IOException {
        Response response = _write(location, body);
        response.setHeader("Location", location.toString());

        return response;
    }

    public Response write(Request request, Path location) throws IOException {
        return write(request, location, request.getBody());
    }

    public Response patch(Request request) throws IOException, InterruptedException {
        Path targetPath = getTargetPath(request);

        ensureFileExists(targetPath);

        if (!isPatchTypeSupported(request.getHeader("Content-Type"))) {
            HttpException he = new UnsupportedMediaTypeHttpException();
            he.setHeader("Accept-Patch", "application/unix-diff");
            throw he;
        }

        if (isIfMatch(request.getHeader("If-Match"), FileSystem.eTagFor(targetPath))) {
            throw new PreconditionFailedHttpException();
        }

        UnixPatch patch = UnixPatch.patch(targetPath, request.getBody());

        if (patch.getStatus() != 0) {
            Response response = Response.create(StatusCode.UNPROCESSABLE_ENTITY);
            response.setBody(patch.getError());
            return response;
        } else {
            return Response.create(StatusCode.NO_CONTENT);
        }
    }

    private void ensureFileExists(Path targetPath) {
        if (!Files.exists(targetPath)) {
            throw new NotFoundHttpException();
        }
    }

    private boolean isPatchTypeSupported(Option<String> contentType) {
        return contentType.isDefined() && contentType.get().equals("application/unix-diff");
    }

    public boolean isIfMatch(Option<String> ifMatch, String eTag) {
        return ifMatch.isDefined() && !ifMatch.get().toUpperCase().equals(eTag);
    }

    public Path getTargetPath(Request request) {
        Path path = rootPath.resolve("." + request.getPath()).normalize();
        return path.toString().equals("") ? Paths.get(".") : path;
    }

    private Response _write(Path target, InputStream body) throws IOException {
        Path targetPath = rootPath.resolve("." + target).normalize();

        if (Files.exists(targetPath)) {
            Files.copy(body, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return Response.create();
        } else {
            Files.createDirectories(targetPath.getParent());
            Files.copy(body, targetPath);
            return Response.create(StatusCode.CREATED);
        }
    }

    private Response _write(Path target, String body) throws IOException {
        return _write(target, new ByteArrayInputStream(body.getBytes()));
    }
}
