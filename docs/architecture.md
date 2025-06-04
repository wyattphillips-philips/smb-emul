# SMB Emulator Architecture

The implementation provides a small set of classes that simulate a very
basic SMB-like server. Anonymous users have full read and write access.

## Components

### `SmbServer`
Listens for incoming socket connections and spawns a `ClientHandler` for
each client.

### `ClientHandler`
Processes commands from a single client. Supports uploading files using
`PUT` and downloading files using `GET`. File access is checked using an
`AccessManager` instance and data is stored via `FileRepository`.

### `FileRepository`
Stores files on the host filesystem under a configured directory. Exposes
methods to save new files and open existing files.

### `AccessManager`
Currently allows all operations. It exposes `canRead` and `canWrite`
checks which always return `true`.

## Relationships
- `SmbServer` maintains multiple `ClientHandler` instances to handle
  concurrent clients.
- `ClientHandler` depends on `AccessManager` for access control and
  `FileRepository` for file operations.

See `design.puml` for a class diagram.
