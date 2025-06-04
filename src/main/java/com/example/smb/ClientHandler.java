package com.example.smb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Handles a single client connection.
 */
public class ClientHandler implements Runnable {
    private final Socket client;
    private final FileRepository repository;
    private final AccessManager accessManager;

    public ClientHandler(Socket client, FileRepository repository, AccessManager accessManager) {
        this.client = client;
        this.repository = repository;
        this.accessManager = accessManager;
    }

    @Override
    public void run() {
        try (InputStream in = new BufferedInputStream(client.getInputStream());
             OutputStream out = new BufferedOutputStream(client.getOutputStream())) {
            handleClient(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException ignore) {
            }
        }
    }

    private void handleClient(InputStream in, OutputStream out) throws IOException {
        String user = "anonymous"; // placeholder user
        byte[] buffer = new byte[8192];
        while (true) {
            String line = readLine(in);
            if (line == null) {
                break;
            }
            if (line.startsWith("PUT ")) {
                String[] parts = line.split(" ", 3);
                if (parts.length != 3) {
                    out.write("ERROR\n".getBytes(StandardCharsets.UTF_8));
                    out.flush();
                    continue;
                }
                String name = parts[1];
                long size = Long.parseLong(parts[2]);
                if (!accessManager.canWrite(user)) {
                    out.write("DENIED\n".getBytes(StandardCharsets.UTF_8));
                    out.flush();
                    continue;
                }
                repository.saveFile(name, in, size);
                out.write("OK\n".getBytes(StandardCharsets.UTF_8));
                out.flush();
            } else if (line.startsWith("GET ")) {
                String[] parts = line.split(" ", 2);
                if (parts.length != 2) {
                    out.write("ERROR\n".getBytes(StandardCharsets.UTF_8));
                    out.flush();
                    continue;
                }
                String name = parts[1];
                if (!accessManager.canRead(user)) {
                    out.write("DENIED\n".getBytes(StandardCharsets.UTF_8));
                    out.flush();
                    continue;
                }
                try (InputStream fileIn = repository.openFile(name)) {
                    long size = fileIn.available();
                    out.write(("OK " + size + "\n").getBytes(StandardCharsets.UTF_8));
                    int read;
                    while ((read = fileIn.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                    out.flush();
                } catch (IOException e) {
                    out.write("NOTFOUND\n".getBytes(StandardCharsets.UTF_8));
                    out.flush();
                }
            } else if (line.equals("QUIT")) {
                break;
            } else {
                out.write("UNKNOWN\n".getBytes(StandardCharsets.UTF_8));
                out.flush();
            }
        }
    }

    private String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            if (c == '\n') {
                break;
            }
            sb.append((char) c);
        }
        if (sb.length() == 0 && c == -1) {
            return null;
        }
        return sb.toString().trim();
    }
}
