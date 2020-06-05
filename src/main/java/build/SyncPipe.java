package build;

import java.io.InputStream;
import java.io.OutputStream;

class SyncPipe implements Runnable {

    public SyncPipe(InputStream inputStream, OutputStream outputStream) { this.inputStream = inputStream; this.outputStream = outputStream; }

    public void run() {
        try {
            final byte[] buffer = new byte[1024];
            for (int length = 0; (length = inputStream.read(buffer)) != -1; ) { outputStream.write(buffer, 0, length); }
        }
        catch (Exception e) { e.printStackTrace(); }
    }
    private final OutputStream outputStream;
    private final InputStream inputStream;
}
