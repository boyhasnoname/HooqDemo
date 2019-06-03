package com.main.moviedb.demo.movielist.model;

public class MovieListServerResponse {
    private String movieListResponse;
    private int statusCode;

    public String getMovieListResponse() {
        return movieListResponse;
    }

    public void setMovieListResponse(String movieListResponse) {
        this.movieListResponse = movieListResponse;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
