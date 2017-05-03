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
import javaslang.collection.List;

import org.cobspec.html.Index;
import org.cobspec.html.Link;
import org.cobspec.template.IndexTemplate;
import org.flint.exception.HttpException;
import org.flint.exception.NotFoundHttpException;
import org.flint.exception.PreconditionFailedHttpException;
import org.flint.exception.UnsupportedMediaTypeHttpException;
import org.flint.MediaType;
import org.flint.Response;
import org.flint.Request;
import org.flint.UnixPatch;
import org.flint.StatusCode;
import org.util.FileSystem;

public class FileSystemController {
    private Path rootPath;

    public FileSystemController(Path rootPath) {
        this.rootPath = rootPath;
    }

    public Response get(Request request) throws IOException {
        Path targetPath = getTargetPath(request);

        if (!Files.exists(targetPath)) {
            throw new NotFoundHttpException();
        }

        Response response = Response.create();
        String extension = FileSystem.getExtension(request.getRequestTarget().getPath());
        String contentType = MediaType.fromExtension(extension).getOrElse("application/octet-stream");
        response.setHeader("Content-Type", contentType);
        response.setBody(Files.newInputStream(targetPath));

        return response;
    }

    public Response index(Request request) throws IOException {
        Path targetPath = getTargetPath(request);

        if (!Files.exists(targetPath)) {
            throw new NotFoundHttpException();
        }

        List<Link> links = List.ofAll(Files.walk(targetPath)
            .filter(Files::isRegularFile)
            .map((filePath) -> {
                String href = rootPath.relativize(filePath).toString();
                String display = targetPath.relativize(filePath).toString();
                return new Link(href, display);
            })
            .collect(Collectors.toList()));
        Index index = new Index(request.getRequestTarget().getPath(), links);

        Response response = Response.create();
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setBody(IndexTemplate.render(index));

        return response;
    }

    public Response delete(Request request) throws IOException {
        Files.delete(getTargetPath(request));
        return Response.create();
    }

    public Response write(Request request) throws IOException {
        ByteArrayInputStream body = new ByteArrayInputStream(request.getBody().getBytes());
        return _write(request.getRequestTarget().getPath(), body);
    }

    public Response write(Request request, String location) throws IOException {
        ByteArrayInputStream body = new ByteArrayInputStream(request.getBody().getBytes());
        Response response = _write(location, body);
        response.setHeader("Location", location);

        return response;
    }

    public Response patch(Request request) throws IOException, InterruptedException {
        Path targetPath = getTargetPath(request);

        if (!Files.exists(targetPath)) {
            throw new NotFoundHttpException();
        }

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

    private boolean isPatchTypeSupported(Option<String> contentType) {
        return contentType.isDefined() && contentType.get().equals("application/unix-diff");
    }

    public boolean isIfMatch(Option<String> ifMatch, String eTag) {
        return ifMatch.isDefined() && !ifMatch.get().toUpperCase().equals(eTag);
    }

    private Path getTargetPath(Request request) {
        return rootPath.resolve("." + request.getRequestTarget().getPath()).normalize();
    }

    private Response _write(String target, InputStream body) throws IOException {
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
}