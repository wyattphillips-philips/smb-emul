package com.example.smb;

/**
 * For now, always allows anonymous read/write access.
 */
public class AccessManager {
    public boolean canRead(String user) {
        return true; // anonymous read allowed
    }

    public boolean canWrite(String user) {
        return true; // anonymous write allowed
    }
}
