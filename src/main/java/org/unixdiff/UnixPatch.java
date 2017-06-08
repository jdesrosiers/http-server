package org.unixdiff;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

import org.util.FileSystem;

public class UnixPatch {
    private Process process;
    private Path targetPath;
    private Path errPath;
    private boolean complete = false;
    private int status;
    private String error;
    private String patched;

    private UnixPatch() {}

    public static UnixPatch patch(InputStream is, String diff) throws InterruptedException, IOException {
        UnixPatch patch = new UnixPatch();
        patch.targetPath = Files.createTempFile(null, null);
        patch.errPath = Files.createTempFile(null, null);

        Files.copy(is, patch.targetPath, StandardCopyOption.REPLACE_EXISTING);

        String command = String.format("patch %s -r %s", patch.targetPath, patch.errPath);
        patch.process = Runtime.getRuntime().exec(command);

        OutputStream stdIn = patch.process.getOutputStream();
        stdIn.write(diff.getBytes());
        stdIn.close();

        return patch;
    }

    public int getStatus() throws InterruptedException, IOException {
        waitAndClean();
        return status;
    }

    public String getError() throws InterruptedException, IOException {
        waitAndClean();
        return error;
    }

    public String getPatchedContent() throws InterruptedException, IOException {
        waitAndClean();
        return patched;
    }

    private void waitAndClean() throws InterruptedException, IOException {
        if (!complete) {
            process.waitFor(1L, TimeUnit.SECONDS);
            status = process.exitValue();
            error = Files.exists(errPath) ? FileSystem.fileToString(errPath) : "";

            if (status == 0) {
                patched = FileSystem.fileToString(targetPath);
            }

            Files.deleteIfExists(targetPath);
            Files.deleteIfExists(errPath);

            complete = true;
        }
    }
}
