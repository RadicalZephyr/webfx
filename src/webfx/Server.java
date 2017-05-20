package webfx;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.InetSocketAddress;
import java.net.URI;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting web server on port 3000...");
        HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
        server.createContext("/", new Handler(Paths.get(".")));
        server.start();
    }

    static class Handler implements HttpHandler {
        private static final String HEADER = "<!DOCTYPE html>\n<html><head></head><body>";
        private static final String FOOTER = "</body></html>";

        private final Path root;

        public Handler(Path root) {
            this.root = root;
        }

        private Path relativeToRoot(Path branch) {
            return this.root.relativize(branch);
        }

        public String getDirectoryContent(Path content) throws IOException {
            Stream<String> headings = Stream.of(String.format("<h1>Index of /%s</h1>",
                                                              relativeToRoot(content).getFileName()));

            Path parentPath = content.getParent();
            if (parentPath != null && parentPath.startsWith(this.root)) {
                headings = Stream.concat(headings,
                                         Stream.of(String.format("<a href=\"/%s\">..</a><br>",
                                                                 relativeToRoot(parentPath).toString())));
            }

            return
                Stream.concat(headings,
                              Files.list(content)
                              .map((Path entry) -> String.format("<a href=\"/%s\">%s</a><br>",
                                                                 relativeToRoot(entry).toString(),
                                                                 entry.getFileName())))
                .collect(Collectors.joining("", HEADER, FOOTER));
        }

        private void copy(InputStream is, OutputStream outStream) throws IOException {
            try (BufferedInputStream inStream  = new BufferedInputStream(is)) {
                int buffSize = 8 * 1024;
                byte[] buffer = new byte[buffSize];

                int bytesRead = inStream.read(buffer, 0, buffSize);
                while (bytesRead != -1) {
                    outStream.write(buffer, 0, bytesRead);
                    bytesRead = inStream.read(buffer, 0, buffSize);
                }

                outStream.flush();
            }
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            URI uri = t.getRequestURI();

            Path content = Paths.get(uri.toString());
            Path relativeRequestPath = Paths.get("/").relativize(content);
            Path absolutePath = this.root.resolve(relativeRequestPath);

            String contentType = "";
            InputStream response = null;
            long responseSize = 0;

            try {
                if (Files.isRegularFile(absolutePath)) {
                    contentType = "text/plain";
                    response = Files.newInputStream(absolutePath);
                    responseSize = Files.size(absolutePath);

                } else if (Files.isDirectory(absolutePath)) {
                    contentType = "text/html";
                    byte[] bytes = getDirectoryContent(absolutePath).getBytes();
                    response = new ByteArrayInputStream(bytes);
                    responseSize = bytes.length;

                } else {
                    contentType = "text/plain";
                    response = new ByteArrayInputStream(new byte[0]);
                    responseSize = 0;
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }

            Headers headers = t.getResponseHeaders();
            headers.put("Content-Type", Arrays.asList(contentType));

            t.sendResponseHeaders(200, responseSize);

            OutputStream os = t.getResponseBody();
            copy(response, os);
            os.close();
        }
    }
}
