
package com.sun.swingset3.demos.table;

import java.net.URI;

public class Link {

    protected String displayText;

    private URI uri;

    private String description;

    private boolean visited;

    public Link(String text) {
        setDisplayText(text);
    }

    public Link(String text, URI uri) {
        this(text);
        setUri(uri);
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String text) {
        this.displayText = text;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description != null ? description : uri != null ? uri.getPath() : null;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
