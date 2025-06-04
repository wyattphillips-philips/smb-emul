package com.example.smb;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Barebones SMB-like server that accepts file uploads from clients.
 */
public class SmbServer {
    private final int port;
    private final FileRepository repository;
    private final AccessManager accessManager;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;

    public SmbServer(int port, FileRepository repository, AccessManager accessManager) {
        this.port = port;
        this.repository = repository;
        this.accessManager = accessManager;
    }

    /**
     * Start listening for client connections.
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("SMB server started on port " + port);
        while (!serverSocket.isClosed()) {
            Socket client = serverSocket.accept();
            executor.submit(new ClientHandler(client, repository, accessManager));
        }
    }

    /**
     * Stop the server.
     */
    public void stop() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        executor.shutdownNow();
    }
}
