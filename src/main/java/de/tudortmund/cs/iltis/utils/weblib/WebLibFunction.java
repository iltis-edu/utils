package de.tudortmund.cs.iltis.utils.weblib;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;

/**
 * A web lib function is a single, loosely coupled function or routine triggered via HTTP.
 *
 * <p>One web lib function has a dedicated "endpoint" associated with it and can be called with
 * arguments in different formats (String, raw bytes). Note, however, that the arguments are
 * <b>encoded as dictated by the content type</b>.
 */
public class WebLibFunction {

    private final String name;
    private final URI uri;
    private final String contentType;

    public WebLibFunction(String name, URI uri, String contentType) {
        this.name = name;
        this.uri = uri;
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public void call(String argument, long timeoutSeconds, WebLibFunctionCallHandler handler) {
        try {
            call(
                    HttpRequest.BodyPublishers.ofString(argument, StandardCharsets.UTF_8),
                    timeoutSeconds,
                    handler);
        } catch (Exception e) {
            handler.onError(e);
        }
    }

    /**
     * Call this web lib function with the given string as arguments
     *
     * @param argument the argument which will be encoded as UTF-8
     * @param handler the handler to deal with successful completions or failures
     */
    public void call(String argument, WebLibFunctionCallHandler handler) {
        try {
            call(HttpRequest.BodyPublishers.ofString(argument, StandardCharsets.UTF_8), handler);
        } catch (Exception e) {
            handler.onError(e);
        }
    }

    /**
     * Call this web lib function with the given raw bytes as arguments
     *
     * @param bytes the bytes which will send as they are
     * @param handler the handler to deal with successful completions or failures
     */
    public void call(byte[] bytes, WebLibFunctionCallHandler handler) {
        try {
            call(HttpRequest.BodyPublishers.ofByteArray(bytes), handler);
        } catch (Exception e) {
            handler.onError(e);
        }
    }

    /**
     * Call this web lib function with the content of the given file
     *
     * @param path the path to the file from which the content will be encoded as used as the
     *     argument
     * @param handler the handler to deal with successful completions or failures
     */
    public void call(Path path, WebLibFunctionCallHandler handler) {
        try {
            call(HttpRequest.BodyPublishers.ofFile(path), handler);
        } catch (Exception e) {
            handler.onError(e);
        }
    }

    private void call(
            HttpRequest.BodyPublisher publisher,
            long timeoutSeconds,
            WebLibFunctionCallHandler handler) {
        try {
            HttpRequest postRequest =
                    HttpRequest.newBuilder()
                            .uri(uri)
                            .timeout(Duration.ofSeconds(timeoutSeconds))
                            .setHeader("Content-Type", contentType)
                            .POST(publisher)
                            .build();
            HttpClient httpClient = HttpClient.newBuilder().build();
            HttpResponse<String> response =
                    httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException(
                        "Request error: response status code (= "
                                + response.statusCode()
                                + ") indicates a problem");
            }
            handler.onSuccess(response.body());
        } catch (Exception e) {
            handler.onError(e);
        }
    }

    private void call(HttpRequest.BodyPublisher publisher, WebLibFunctionCallHandler handler) {
        try {
            HttpRequest postRequest =
                    HttpRequest.newBuilder()
                            .uri(uri)
                            .setHeader("Content-Type", contentType)
                            .POST(publisher)
                            .build();
            HttpClient httpClient = HttpClient.newBuilder().build();
            HttpResponse<String> response =
                    httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException(
                        "Request error: response status code (= "
                                + response.statusCode()
                                + ") indicates a problem");
            }
            handler.onSuccess(response.body());
        } catch (Exception e) {
            handler.onError(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebLibFunction that = (WebLibFunction) o;
        return Objects.equals(name, that.name)
                && Objects.equals(uri, that.uri)
                && Objects.equals(contentType, that.contentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uri, contentType);
    }

    @Override
    public String toString() {
        return name + " @ " + uri + " [" + contentType + "]";
    }
}
