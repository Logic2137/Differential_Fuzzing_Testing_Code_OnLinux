

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.util.Optional;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class FixedHttpResponse<T> implements HttpResponse<T> {

    private final int statusCode;
    private final HttpRequest request;
    private final HttpHeaders headers;
    private final T body;
    private final SSLSession sslSession;
    private final URI uri;
    private final HttpClient.Version version;

    public FixedHttpResponse(int statusCode,
                             HttpRequest request,
                             HttpHeaders headers,
                             T body,
                             SSLSession sslSession,
                             URI uri,
                             HttpClient.Version version) {
        this.statusCode = statusCode;
        this.request = request;
        this.headers = headers;
        this.body = body;
        this.sslSession = sslSession;
        this.uri = uri;
        this.version = version;
    }

    @Override
    public int statusCode() {
        return statusCode;
    }

    @Override
    public HttpRequest request() {
        return request;
    }

    @Override
    public Optional<HttpResponse<T>> previousResponse() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return headers;
    }

    @Override
    public T body() {
        return body;
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return Optional.ofNullable(sslSession);
    }

    @Override
    public URI uri() {
        return uri;
    }

    @Override
    public HttpClient.Version version() {
        return version;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append(super.toString()).append(" [ ")
                .append("status code: ").append(statusCode)
                .append(", request: ").append(request)
                .append(", headers: ").append(headers)
                .append(" ]")
                .toString();
    }
}
