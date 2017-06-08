package org.cobspec.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import javaslang.collection.List;
import javaslang.control.Try;

import org.cobspec.html.Link;
import org.cobspec.html.Index;
import org.cobspec.template.IndexTemplate;
import org.flint.controller.Controller;
import org.flint.controller.PatchResult;
import org.flint.datastore.FileSystemDataStore;
import org.flint.datastore.DataStoreException;
import org.unixdiff.UnixPatch;

public class FileSystemController extends Controller {
    public FileSystemController(Path rootPath) {
        super(new FileSystemDataStore(rootPath, indexPath -> FileSystemController.directoryListing(rootPath, indexPath)));

        addPatcher("application/unix-diff", FileSystemController::unixPatch);
    }

    private static InputStream directoryListing(final Path rootPath, final Path indexPath) throws DataStoreException {
        try {
            final List<Link> links = List.ofAll(Files.walk(indexPath, 1)
                    .filter(path -> Try.of(() -> !Files.isHidden(path) && !Files.isSameFile(path, indexPath)).get())
                    .map((filePath) -> {
                        final String href = rootPath.relativize(filePath).toString();
                        final String display = indexPath.relativize(filePath).toString();
                        return new Link(href, display);
                    })
                    .collect(Collectors.toList()));
            final Index index = new Index("/" + rootPath.relativize(indexPath).toString(), links);

            return new ByteArrayInputStream(IndexTemplate.render(index).getBytes());
        } catch (IOException ioe) {
            throw new DataStoreException(ioe);
        }
    }

    private static PatchResult unixPatch(InputStream subject, String diff) {
        try {
            UnixPatch patch = UnixPatch.patch(subject, diff);
            String result = patch.getStatus() == 0 ? patch.getPatchedContent() : patch.getError();
            return new PatchResult(patch.getStatus() == 0, result);
        } catch (Exception e) {
            return new PatchResult(false, e.getMessage());
        }
    }
}
