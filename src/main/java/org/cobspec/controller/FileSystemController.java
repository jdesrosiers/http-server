package org.cobspec.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

import javaslang.collection.List;

import org.cobspec.html.Index;
import org.cobspec.html.Link;
import org.cobspec.template.IndexTemplate;
import org.httpserver.MediaType;
import org.httpserver.Response;
import org.httpserver.Request;
import org.httpserver.StatusCode;
import org.util.FileSystem;

public class FileSystemController {
    private Path rootPath;

    public FileSystemController(Path rootPath) {
        this.rootPath = rootPath;
    }

    public Response get(Request request) {
        Path targetPath = rootPath.resolve("." + request.getRequestTarget()).normalize();

        try {
            if (Files.exists(targetPath)) {
                Response response = Response.create();
                String extension = FileSystem.getExtension(request.getRequestTarget());
                String contentType = MediaType.fromExtension(extension).getOrElse("application/octet-stream");
                response.setHeader("Content-Type", contentType);
                response.setBody(Files.newInputStream(targetPath));

                return response;
            } else {
                return Response.create(StatusCode.NOT_FOUND);
            }
        } catch (IOException ioe) {
            return Response.create(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Response index(Request request) {
        try {
            Path targetPath = rootPath.resolve("." + request.getRequestTarget()).normalize();
            List<Link> links = List.ofAll(Files.walk(targetPath)
                .filter(Files::isRegularFile)
                .map((filePath) -> {
                    String href = rootPath.relativize(filePath).toString();
                    String display = targetPath.relativize(filePath).toString();
                    return new Link(href, display);
                })
                .collect(Collectors.toList()));
            Index index = new Index(request.getRequestTarget(), links);

            Response response = Response.create();
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            response.setBody(IndexTemplate.render(index));

            return response;
        } catch (IOException ioe) {
            return Response.create(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Response delete(Request request) {
        Path targetPath = rootPath.resolve("." + request.getRequestTarget()).normalize();

        try {
            Files.delete(targetPath);
            return Response.create();
        } catch (IOException ioe) {
            return Response.create(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Response write(Request request) {
        ByteArrayInputStream body = new ByteArrayInputStream(request.getBody().getBytes());
        return _write(request.getRequestTarget(), body);
    }

    public Response write(Request request, String location) {
        ByteArrayInputStream body = new ByteArrayInputStream(request.getBody().getBytes());
        Response response = _write(location, body);
        response.setHeader("Location", location);

        return response;
    }

    private Response _write(String target, InputStream body) {
        Path targetPath = rootPath.resolve("." + target).normalize();

        try {
            if (Files.exists(targetPath)) {
                Files.copy(body, targetPath, StandardCopyOption.REPLACE_EXISTING);
                return Response.create();
            } else {
                Files.createDirectories(targetPath.getParent());
                Files.copy(body, targetPath);
                return Response.create(StatusCode.CREATED);
            }
        } catch (IOException ioe) {
            return Response.create(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }
}
