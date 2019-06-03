package com.main.moviedb.demo.movielist.presenter;

import com.main.moviedb.demo.movielist.model.MovieListServerResponse;

public interface MovieApiInterface {
    void onSuccessCallBack(MovieListServerResponse response);
}
