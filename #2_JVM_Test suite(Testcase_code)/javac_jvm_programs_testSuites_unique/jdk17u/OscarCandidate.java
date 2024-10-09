
package com.sun.swingset3.demos.table;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class OscarCandidate {

    private String category;

    private Integer year;

    private boolean winner = false;

    private String movie;

    private URI imdbURI;

    private final ArrayList<String> persons = new ArrayList<String>();

    public OscarCandidate(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public String getMovieTitle() {
        return movie;
    }

    public void setMovieTitle(String movie) {
        this.movie = movie;
    }

    public URI getIMDBMovieURI() {
        return imdbURI;
    }

    public void setIMDBMovieURI(URI uri) {
        this.imdbURI = uri;
    }

    public List<String> getPersons() {
        return persons;
    }
}
