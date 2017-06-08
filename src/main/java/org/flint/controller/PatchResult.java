package org.flint.controller;

import java.io.InputStream;

public class PatchResult {
    private boolean isSuccessful;
    private String result;

    public PatchResult(final boolean isSuccessful, final String result) {
        this.isSuccessful = isSuccessful;
        this.result = result;
    }

    public boolean isSuccessful() {
        return this.isSuccessful;
    }

    public String getResult() {
        return this.result;
    }
}
