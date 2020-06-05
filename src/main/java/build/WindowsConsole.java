package build;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class WindowsConsole {

    private static Process proc;

    public static void main(String[] args) { new WindowsConsole(); }

    public WindowsConsole(){

        try {
            proc = Runtime.getRuntime().exec(new String[]{ "cmd" });
            new Thread(new SyncPipe(proc.getErrorStream())).start();
            new Thread(new SyncPipe(proc.getInputStream())).start();

            PrintWriter stdin = new PrintWriter(proc.getOutputStream());
            stdin.println("ipconfig /all");
            stdin.close();

        } catch (IOException e) { e.printStackTrace(); }

    }

    static class SyncPipe implements Runnable {
        String str="";
        private final InputStream inputStream;
        public SyncPipe(InputStream inputStream) { this.inputStream = inputStream; }
        public void run() {
            try {
                final byte[] buffer = new byte[1024];
                while(inputStream.read(buffer) != -1){ str = str + IOUtils.toString(inputStream, StandardCharsets.UTF_8); }
//                System.out.println(str);
            }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

}
