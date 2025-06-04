package com.example.smb;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Utility methods for responding to minimal SMB handshake requests.
 */
public final class SmbHandshakeUtil {
    private SmbHandshakeUtil() {}

    /**
     * Send a very small SMB NEGOTIATE response so clients like JCIFS
     * recognize that the server speaks SMB. This does not implement the
     * full protocol.
     */
    public static void sendNegotiateResponse(OutputStream out) throws IOException {
        // Minimal SMB1 negotiate protocol response with STATUS_SUCCESS
        byte[] resp = new byte[] {
            (byte)0xFF, 'S', 'M', 'B',
            (byte)0x72, // SMB_COM_NEGOTIATE
            0, 0, 0, 0, // Status
            (byte)0x98, // Flags (minimal)
            0x00, 0x00, // Flags2
            0, 0, // PID high
            0,0,0,0,0,0,8,0, // Signature + reserved
            0,0, // Tree ID
            0,0, // PID low
            0,0 // User ID
        };
        out.write(resp);
        out.flush();
    }
}
