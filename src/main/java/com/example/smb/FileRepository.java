package com.example.smb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Handles storage of files on disk.
 */
public class FileRepository {
    private final Path rootDir;

    public FileRepository(Path rootDir) throws IOException {
        this.rootDir = rootDir;
        Files.createDirectories(rootDir);
    }

    public void saveFile(String name, InputStream in) throws IOException {
        Path dest = rootDir.resolve(name).normalize();
        try (OutputStream out = Files.newOutputStream(dest, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            in.transferTo(out);
        }
    }

    public void saveFile(String name, InputStream in, long length) throws IOException {
        Path dest = rootDir.resolve(name).normalize();
        try (OutputStream out = Files.newOutputStream(dest, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            byte[] buffer = new byte[8192];
            long remaining = length;
            while (remaining > 0) {
                int read = in.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                if (read == -1) {
                    break;
                }
                out.write(buffer, 0, read);
                remaining -= read;
            }
        }
    }

    public InputStream openFile(String name) throws IOException {
        Path src = rootDir.resolve(name).normalize();
        return Files.newInputStream(src, StandardOpenOption.READ);
    }
}
