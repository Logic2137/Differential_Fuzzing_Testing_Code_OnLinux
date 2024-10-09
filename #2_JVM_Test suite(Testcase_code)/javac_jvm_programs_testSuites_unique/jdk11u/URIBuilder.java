

package jdk.test.lib.net;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URIBuilder {

    public static URIBuilder newBuilder() {
        return new URIBuilder();
    }

    private String scheme;
    private String userInfo;
    private String host;
    private int port;
    private String path;
    private String query;
    private String fragment;

    private URIBuilder() {}

    public URIBuilder scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public URIBuilder userInfo(String userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public URIBuilder host(String host) {
        this.host = host;
        return this;
    }

    public URIBuilder loopback() {
        return host(InetAddress.getLoopbackAddress().getHostAddress());
    }

    public URIBuilder port(int port) {
        this.port = port;
        return this;
    }

    public URIBuilder path(String path) {
        this.path = path;
        return this;
    }

    public URIBuilder query(String query) {
        this.query = query;
        return this;
    }

    public URIBuilder fragment(String fragment) {
        this.fragment = fragment;
        return this;
    }

    public URI build() throws URISyntaxException {
        return new URI(scheme, userInfo, host, port, path, query, fragment);
    }

    public URI buildUnchecked() {
        try {
            return build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public URL toURL() throws URISyntaxException, MalformedURLException {
        return build().toURL();
    }

    public URL toURLUnchecked() {
        try {
            return toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
