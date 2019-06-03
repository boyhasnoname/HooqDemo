package com.main.moviedb.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.main.moviedb.demo.R;
import com.main.moviedb.demo.movielist.model.ResultsItem;
import com.main.moviedb.demo.movielist.view.MovieListActivity;
import com.main.moviedb.demo.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.UserHolder> {

    private int mMovieId;
    private OnItemClickListener mOnItemClickListener;
    private boolean mIsMovieDetails = false;
    private Context mContext;
    private List<ResultsItem> mItems;

    public MovieListAdapter(Context context, List<ResultsItem> items, OnItemClickListener onItemClickListener, boolean isMovieDetails, int movieId) {
        mItems = items;
        mContext = context;
        mOnItemClickListener = onItemClickListener;
        mIsMovieDetails = isMovieDetails;
        mMovieId = movieId;
    }

    @NonNull
    @Override
    public MovieListAdapter.UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movielist_row, viewGroup, false);
        UserHolder userHolder = new UserHolder(myView);

        return userHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.UserHolder viewHolder, final int i) {
        if(i >= mItems.size()) {
            if(mIsMovieDetails)
                viewHolder.btnViewMore.setVisibility(View.VISIBLE);
            return;
        }
        viewHolder.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movieListIntent = new Intent(mContext, MovieListActivity.class);
                movieListIntent.putExtra(Constants.TAG_TITLE, "Recommended Movies");
                movieListIntent.putExtra(Constants.TAG_MOVIE_ID, mMovieId);
                movieListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(movieListIntent);
            }
        });
        viewHolder.txtViewMovieName.setText(mItems.get(i).getTitle());
        double rating = mItems.get(i).getVote_average()/2;
        viewHolder.ratingBar.setRating((float) rating);
        viewHolder.btnViewMore.setVisibility(View.GONE);
        Picasso.with(mContext)
                .load(Constants.MOVIE_POSTER_URL + mItems.get(i).getPoster_path())
                .placeholder(R.drawable.loader)
                .fit()
                .centerCrop().into(viewHolder.imgViewUserProfilePic);
        viewHolder.layoutMovieRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(mItems.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIsMovieDetails ? mItems.size() + 1 : mItems.size();
    }

    public void clear() {
        mItems.clear();
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        private final TextView txtViewMovieName;
        private final RatingBar ratingBar;
        private final ImageView imgViewUserProfilePic;
        private final FrameLayout layoutMovieRow;
        private final Button btnViewMore;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            layoutMovieRow = itemView.findViewById(R.id.layoutMovieRow);
            imgViewUserProfilePic = itemView.findViewById(R.id.imgViewMovieThump);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            txtViewMovieName = itemView.findViewById(R.id.txtViewMovieName);
            btnViewMore = itemView.findViewById(R.id.btnViewMore);
        }
    }

    public void add(ResultsItem response) {
        mItems.add(response);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultsItem> postItems) {
        for (ResultsItem response : postItems) {
            add(response);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ResultsItem item);
    }
}
