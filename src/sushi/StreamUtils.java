package sushi;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class StreamUtils {
    public static <T> T uncheckCall(Callable<T> callable) {
        try { return callable.call(); }
        catch (RuntimeException e) { throw e; }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static String readFile(String path) throws IOException {
        return readFile(path, Charset.defaultCharset());
    }

    public static void uncheckRun(RunnableExc r) {
        try { r.run(); } catch (Exception e) { throw new RuntimeException(e); }
    }

    public interface RunnableExc { void run() throws Exception; }
}
