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
    private Path tempPath;
    private Path errPath;
    private boolean complete = false;
    private int status;
    private String error;

    private UnixPatch() {}

    public static UnixPatch patch(Path targetPath, String diff) throws InterruptedException, IOException {
        UnixPatch patch = new UnixPatch();
        patch.targetPath = targetPath;

        patch.tempPath = Paths.get(targetPath.toString() + ".tmp");
        patch.errPath = Paths.get(targetPath.toString() + ".tmp.rej");

        String command = String.format("patch %s -o %s", targetPath, patch.tempPath);
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

    private void waitAndClean() throws InterruptedException, IOException {
        if (!complete) {
            process.waitFor(1L, TimeUnit.SECONDS);
            status = process.exitValue();
            error = Files.exists(errPath) ? FileSystem.fileToString(errPath) : "";

            if (status == 0) {
                Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            Files.deleteIfExists(tempPath);
            Files.deleteIfExists(errPath);

            complete = true;
        }
    }
}
