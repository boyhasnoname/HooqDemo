package com.main.moviedb.demo.movielist.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ResultsItem implements Parcelable {
	private String overview;
	private String originalLanguage;
	private String original_title;
	private boolean video;
	private String title;
	private List<Integer> genreIds;
	private String poster_path;
	private String backdrop_path;
	private String release_date;
	private double vote_average;
	private double popularity;
	private int id;
	private boolean adult;
	private int voteCount;

	protected ResultsItem(Parcel in) {
		overview = in.readString();
		originalLanguage = in.readString();
		original_title = in.readString();
		video = in.readByte() != 0;
		title = in.readString();
		poster_path = in.readString();
		backdrop_path = in.readString();
		release_date = in.readString();
		vote_average = in.readDouble();
		popularity = in.readDouble();
		id = in.readInt();
		adult = in.readByte() != 0;
		voteCount = in.readInt();
	}

	public static final Creator<ResultsItem> CREATOR = new Creator<ResultsItem>() {
		@Override
		public ResultsItem createFromParcel(Parcel in) {
			return new ResultsItem(in);
		}

		@Override
		public ResultsItem[] newArray(int size) {
			return new ResultsItem[size];
		}
	};

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getOriginalLanguage() {
		return originalLanguage;
	}

	public void setOriginalLanguage(String originalLanguage) {
		this.originalLanguage = originalLanguage;
	}

	public String getOriginal_title() {
		return original_title;
	}

	public void setOriginal_title(String original_title) {
		this.original_title = original_title;
	}

	public boolean isVideo() {
		return video;
	}

	public void setVideo(boolean video) {
		this.video = video;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Integer> getGenreIds() {
		return genreIds;
	}

	public void setGenreIds(List<Integer> genreIds) {
		this.genreIds = genreIds;
	}

	public String getPoster_path() {
		return poster_path;
	}

	public void setPoster_path(String poster_path) {
		this.poster_path = poster_path;
	}

	public String getBackdrop_path() {
		return backdrop_path;
	}

	public void setBackdrop_path(String backdrop_path) {
		this.backdrop_path = backdrop_path;
	}

	public String getRelease_date() {
		return release_date;
	}

	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}

	public double getVote_average() {
		return vote_average;
	}

	public void setVote_average(double vote_average) {
		this.vote_average = vote_average;
	}

	public double getPopularity() {
		return popularity;
	}

	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isAdult() {
		return adult;
	}

	public void setAdult(boolean adult) {
		this.adult = adult;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public int getImageBitmap() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(overview);
		parcel.writeString(originalLanguage);
		parcel.writeString(original_title);
		parcel.writeByte((byte) (video ? 1 : 0));
		parcel.writeString(title);
		parcel.writeString(poster_path);
		parcel.writeString(backdrop_path);
		parcel.writeString(release_date);
		parcel.writeDouble(vote_average);
		parcel.writeDouble(popularity);
		parcel.writeInt(id);
		parcel.writeByte((byte) (adult ? 1 : 0));
		parcel.writeInt(voteCount);
	}
}