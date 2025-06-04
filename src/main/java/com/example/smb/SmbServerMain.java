package com.example.smb;

import java.nio.file.Path;

/**
 * Simple entry point to run the SMB server.
 */
public class SmbServerMain {
    public static void main(String[] args) throws Exception {
        int port = 4455;
        Path root = Path.of("share");
        FileRepository repository = new FileRepository(root);
        AccessManager accessManager = new AccessManager();
        SmbServer server = new SmbServer(port, repository, accessManager);
        server.start();
    }
}
