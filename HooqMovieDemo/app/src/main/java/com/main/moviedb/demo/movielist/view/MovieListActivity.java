package com.main.moviedb.demo.movielist.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.main.moviedb.demo.R;
import com.main.moviedb.demo.adapter.MovieListAdapter;
import com.main.moviedb.demo.moviedetails.view.MovieDetailsActivity;
import com.main.moviedb.demo.movielist.model.MovieListResponse;
import com.main.moviedb.demo.movielist.model.MovieListServerResponse;
import com.main.moviedb.demo.movielist.model.ResultsItem;
import com.main.moviedb.demo.movielist.presenter.MovieApiHandler;
import com.main.moviedb.demo.movielist.presenter.MovieApiInterface;
import com.main.moviedb.demo.movielist.presenter.MovieListPaginationListener;
import com.main.moviedb.demo.utils.Constants;
import com.main.moviedb.demo.utils.Utility;

import org.apache.http.HttpStatus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListActivity extends AppCompatActivity implements MovieApiInterface, SwipeRefreshLayout.OnRefreshListener, MovieListAdapter.OnItemClickListener {

    @BindView(R.id.recylerViewLayout)
    RecyclerView recyclerViewLayout;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.mTopToolbar)
    Toolbar mTopToolbar;

    @BindView(R.id.txtViewEmpty)
    TextView txtViewEmpty;

    public final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    int itemCount = 0;
    int movieId = -1;
    private MovieApiInterface movieApiInterface;
    private GridLayoutManager mLayoutManager;
    private MovieListAdapter movieListAdapter;
    private MovieListAdapter.OnItemClickListener onItemClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);
        ButterKnife.bind(this);
        setSupportActionBar(mTopToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(getIntent() != null && getIntent().getStringExtra(Constants.TAG_TITLE) != null && !getIntent().getStringExtra(Constants.TAG_TITLE).equalsIgnoreCase("")) {
            actionBar.setTitle(getIntent().getStringExtra(Constants.TAG_TITLE));
            movieId = getIntent().getIntExtra(Constants.TAG_MOVIE_ID, -1);
        } else {
            actionBar.setTitle("Latest Movies");
        }

        movieApiInterface = this;
        onItemClickListener = this;
        mLayoutManager = new GridLayoutManager(this, 3);
        recyclerViewLayout.setLayoutManager(mLayoutManager);
        swipeRefresh.setOnRefreshListener(this);

        if(!Utility.isNetworkAvailable(getApplicationContext())) {
            showErrorMessage(true, getString(R.string.no_network_error));
            return;
        } else {
            showErrorMessage(false, null);
        }

        recyclerViewLayout.addOnScrollListener(new MovieListPaginationListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                if(!Utility.isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "No network connection. Please switch on your wifi/mobile network and refresh the page", Toast.LENGTH_LONG).show();
                    return;
                }
                isLoading = true;
                currentPage++;
                onLoadMoreMovies();

            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        //recyclerViewLayout.setLayoutManager(mLayoutManager);
        onLoadHttpData();
    }

    private void showErrorMessage(boolean isError, String errorMsg) {
        if(isError) {
            recyclerViewLayout.setVisibility(View.GONE);
            txtViewEmpty.setVisibility(View.VISIBLE);

            txtViewEmpty.setText(errorMsg);
        } else {
            recyclerViewLayout.setVisibility(View.VISIBLE);
            txtViewEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void onLoadMoreMovies() {
        onLoadHttpData();
    }

    private void onLoadHttpData() {
        new MovieApiHandler(movieApiInterface, currentPage, movieId).execute(Constants.MOVIE_LIST_API_URL);
    }

    @Override
    public void onSuccessCallBack(MovieListServerResponse response) {
        if(response.getStatusCode() == HttpStatus.SC_OK) {
            Gson gson = new Gson();
            MovieListResponse movieListResponse = gson.fromJson(response.getMovieListResponse(), MovieListResponse.class);
            if(movieListAdapter == null) {
                movieListAdapter = new MovieListAdapter(getApplicationContext(), movieListResponse.results, onItemClickListener, false, movieId);
                recyclerViewLayout.setAdapter(movieListAdapter);
                return;
            }
            movieListAdapter.addAll(movieListResponse.results);
            swipeRefresh.setRefreshing(false);
        /*if (currentPage < totalPage)
            movieListAdapter.addLoading();
        else
            isLastPage = true;*/
            isLoading = false;
        } else {
            showErrorMessage(true, response.getMovieListResponse());
        }

    }

    @Override
    public void onRefresh() {
        if(!Utility.isNetworkAvailable(getApplicationContext())) {
            swipeRefresh.setRefreshing(false);
            showErrorMessage(true, "No network connection. Please switch on your wifi/mobile network and refresh the page");
            return;
        } else {
            showErrorMessage(false, null);
        }
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        if(movieListAdapter != null)
            movieListAdapter.clear();
        swipeRefresh.setRefreshing(false);
        onLoadMoreMovies();
    }

    @Override
    public void onItemClick(ResultsItem item) {
        Intent movieDetailIntent = new Intent(MovieListActivity.this, MovieDetailsActivity.class);
        movieDetailIntent.putExtra(Constants.RESULT_ITEM, item);
        startActivity(movieDetailIntent);
    }
}
