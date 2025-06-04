# smb-emul
Emulates a very small SMB-like server. It accepts a custom text protocol
for uploading and downloading files. The server now attempts to respond
to the initial SMB negotiation used by JCIFS clients, but after the
handshake clients must speak the simple text commands described below.

## Running

Compile the sources and run `com.example.smb.SmbServerMain` using your
preferred Java runtime. The server listens on port `4455` and stores
files under a `share/` directory.

## Simple Protocol

Commands are newline separated:

```
PUT <name> <size>\n<binary data>
GET <name>\n
QUIT\n
```

Anonymous read and write access is allowed.
