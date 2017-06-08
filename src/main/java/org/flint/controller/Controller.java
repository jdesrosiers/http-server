package org.flint.controller;

import java.io.InputStream;
import java.io.IOException;

import javaslang.Tuple;
import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.control.Option;
import javaslang.control.Try;

import org.apache.tika.Tika;

import org.flint.datastore.DataStore;
import org.flint.datastore.DataStoreException;
import org.flint.datastore.FileSystemDataStore;
import org.flint.exception.HttpException;
import org.flint.exception.NotFoundHttpException;
import org.flint.exception.PreconditionFailedHttpException;
import org.flint.exception.UnsupportedMediaTypeHttpException;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

public class Controller {
    private static final Tika mimeTypeDetector = new Tika();

    private DataStore dataStore;
    private Map<String, Patcher> patchers = HashMap.empty();

    public Controller(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void addPatcher(String contentType, Patcher patcher) {
        patchers = patchers.put(contentType, patcher);
    }

    public Response get(Request request) throws DataStoreException {
        ensureFileExists(request.getPath());

        String contentType = Try.of(() -> {
            InputStream body = dataStore.fetch(request.getPath());
            return mimeTypeDetector.detect(body, request.getPath());
        }).getOrElse("application/octet-stream");

        Response response = Response.create();
        response.setHeader("Content-Type", contentType);
        response.setBody(dataStore.fetch(request.getPath()));
        return response;
    }

    public Response delete(Request request) throws DataStoreException {
        dataStore.delete(request.getPath());
        return Response.create();
    }

    public Response put(Request request) throws DataStoreException {
        return write(request, request.getPath());
    }

    public Response write(Request request, String identifier) throws DataStoreException {
        int statusCode = dataStore.contains(identifier) ? StatusCode.OK : StatusCode.CREATED;
        dataStore.save(identifier, request.getBody());

        Response response = Response.create(statusCode);
        if (!request.getPath().equals(identifier)) {
            response.setHeader("Location", identifier);
        }

        return response;
    }

    public Response patch(Request request) throws DataStoreException {
        ensureFileExists(request.getPath());

        Patcher patcher = request.getHeader("Content-Type")
            .flatMap(patchers::get)
            .getOrElseThrow(() -> {
                HttpException he = new UnsupportedMediaTypeHttpException();
                he.setHeader("Accept-Patch", patchers.keySet().mkString(" "));

                return he;
            });

        if (isIfMatch(request.getHeader("If-Match"), dataStore.hashOf(request.getPath()))) {
            throw new PreconditionFailedHttpException();
        }

        PatchResult patchResult = patcher.patch(dataStore.fetch(request.getPath()), request.getBody());

        if (!patchResult.isSuccessful()) {
            Response response = Response.create(StatusCode.UNPROCESSABLE_ENTITY);
            response.setBody(patchResult.getResult());
            return response;
        } else {
            dataStore.save(request.getPath(), patchResult.getResult());
            return Response.create(StatusCode.NO_CONTENT);
        }
    }

    private boolean isIfMatch(Option<String> ifMatch, String eTag) {
        return ifMatch.isDefined() && !ifMatch.get().toUpperCase().equals(eTag);
    }

    private void ensureFileExists(String targetPath) {
        if (!dataStore.contains(targetPath)) {
            throw new NotFoundHttpException();
        }
    }
}
