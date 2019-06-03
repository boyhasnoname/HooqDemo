package com.main.moviedb.demo.moviedetails.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.main.moviedb.demo.R;
import com.main.moviedb.demo.adapter.MovieListAdapter;
import com.main.moviedb.demo.moviedetails.presenter.RelatedMoviesHandler;
import com.main.moviedb.demo.movielist.model.MovieListResponse;
import com.main.moviedb.demo.movielist.model.MovieListServerResponse;
import com.main.moviedb.demo.movielist.model.ResultsItem;
import com.main.moviedb.demo.movielist.presenter.MovieApiInterface;
import com.main.moviedb.demo.utils.Constants;
import com.main.moviedb.demo.utils.Utility;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpStatus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity implements MovieApiInterface, MovieListAdapter.OnItemClickListener {
    private ResultsItem resultsItem;

    @BindView(R.id.recyclerViewUserInfo)
    RecyclerView recyclerViewUserInfo;

    @BindView(R.id.txtViewDescription)
    TextView txtViewDescription;

    @BindView(R.id.txtViewReleaseDate)
    TextView txtViewReleaseDate;

    @BindView(R.id.txtViewErrorView)
    TextView txtViewErrorView;

    private MovieListAdapter.OnItemClickListener onItemClickListener;
    private MovieApiInterface movieApiInterface;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.CollapsingToolbarLayout1);
        onItemClickListener = this;
        movieApiInterface = this;
        resultsItem = getIntent().getParcelableExtra(Constants.RESULT_ITEM);
        if(resultsItem == null) {
            showErrorMessage(getString(R.string.error_hint));
            return;
        }
        collapsingToolbarLayout.setTitle(resultsItem.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(resultsItem.getTitle());
        ImageView userProfileThump = findViewById(R.id.backdrop);
        txtViewErrorView.setVisibility(View.GONE);
        recyclerViewUserInfo.setVisibility(View.VISIBLE);
        if(Utility.isNetworkAvailable(getApplicationContext())) {
            Picasso.with(getApplicationContext())
                    .load(Constants.MOVIE_POSTER_URL + resultsItem.getBackdrop_path())
                    .fit()
                    .into(userProfileThump);
            onLoadHttpData();

        } else {
            showErrorMessage(getString(R.string.no_network_error));
        }
        if(resultsItem.getRelease_date() != null && !resultsItem.getRelease_date().equalsIgnoreCase("")) {
            txtViewReleaseDate.setText(Utility.getDateFormat("yyyy-MM-dd", "MMM yyyy", resultsItem.getRelease_date(), null));
        } else {
            txtViewReleaseDate.setText(R.string.not_specified);
        }

        if(resultsItem.getOverview() != null && !resultsItem.getOverview().equalsIgnoreCase("")) {
            txtViewDescription.setText(resultsItem.getOverview());
        } else {
            txtViewDescription.setText(R.string.not_specified);
        }
    }

    private void showErrorMessage(String errorMessage) {
        txtViewErrorView.setVisibility(View.VISIBLE);
        recyclerViewUserInfo.setVisibility(View.GONE);
        txtViewErrorView.setText(errorMessage);
    }

    private void onLoadHttpData() {
        new RelatedMoviesHandler(movieApiInterface, currentPage, resultsItem.getId()).execute(Constants.MOVIE_LIST_API_URL);
    }

    @Override
    public void onSuccessCallBack(MovieListServerResponse response) {
        if(response.getStatusCode() == HttpStatus.SC_OK) {
            Gson gson = new Gson();
            MovieListResponse movieListResponse = gson.fromJson(response.getMovieListResponse(), MovieListResponse.class);
            if(movieListResponse.results == null) {
                return;
            } else if(movieListResponse.results != null && movieListResponse.results.size() == 0) {
                showErrorMessage("No more related movies found.");
                return;
            }
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewUserInfo.setLayoutManager(mLayoutManager);
            recyclerViewUserInfo.setAdapter(new MovieListAdapter(getApplicationContext(), movieListResponse.results, onItemClickListener, true, resultsItem.getId()));
        } else {
            showErrorMessage(response.getMovieListResponse());
        }
    }

    @Override
    public void onItemClick(ResultsItem item) {
        Intent movieDetailIntent = new Intent(this, MovieDetailsActivity.class);
        movieDetailIntent.putExtra(Constants.RESULT_ITEM, item);
        startActivity(movieDetailIntent);
        finish();
    }
}