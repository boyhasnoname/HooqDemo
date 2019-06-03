package com.main.moviedb.demo.movielist.model;

import java.util.List;

public class MovieListResponse{
	private Dates dates;
	private int page;
	private int totalPages;
	public List<ResultsItem> results;
	private int totalResults;
}