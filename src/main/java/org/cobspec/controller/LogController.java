package org.cobspec.controller;

import java.nio.file.Path;

import org.flint.controller.Controller;
import org.flint.datastore.FileSystemDataStore;

public class LogController extends Controller {
    public LogController(Path logPath) {
        super(new FileSystemDataStore(logPath));
    }
}
