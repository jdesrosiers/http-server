package org.flint.controller;

import java.io.InputStream;

public interface Patcher {
    public PatchResult patch(final InputStream subject, final String diff);
}
