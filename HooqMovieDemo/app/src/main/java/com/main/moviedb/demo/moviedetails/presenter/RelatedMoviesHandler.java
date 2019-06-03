package com.main.moviedb.demo.moviedetails.presenter;

import android.net.Uri;
import android.os.AsyncTask;

import com.main.moviedb.demo.movielist.model.MovieListServerResponse;
import com.main.moviedb.demo.movielist.presenter.MovieApiInterface;
import com.main.moviedb.demo.utils.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class RelatedMoviesHandler extends AsyncTask<String, String, MovieListServerResponse> {

    private int pageNo;
    private MovieApiInterface mUserDetailInterface;
    private int movieId;

    public RelatedMoviesHandler(MovieApiInterface userDetailInterface, int pageNo, int movieId) {
        mUserDetailInterface = userDetailInterface;
        this.pageNo = pageNo;
        this.movieId = movieId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected MovieListServerResponse doInBackground(String... strings) {
        String response = "";
        HttpResponse httpResponse = null;
        MovieListServerResponse movieListServerResponse = new MovieListServerResponse();
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .authority(Constants.BASE_URL)
                    .path("3/movie/" + movieId +"/similar")
                    .appendQueryParameter("api_key", Constants.API_KEY)
                    .appendQueryParameter("page", String.valueOf(pageNo))
                    .build();
            request.setURI(new URI(uri.toString()));
            httpResponse = client.execute(request);
            StatusLine statusLine = httpResponse.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                httpResponse.getEntity().writeTo(byteArrayOutputStream);
                response = byteArrayOutputStream.toString();
                byteArrayOutputStream.close();
                movieListServerResponse.setMovieListResponse(response);
                movieListServerResponse.setStatusCode(statusLine.getStatusCode());
            } else {
                movieListServerResponse.setMovieListResponse(httpResponse.getStatusLine().getReasonPhrase());
                movieListServerResponse.setStatusCode(statusLine.getStatusCode());
            }
        } catch (URISyntaxException e) {
            movieListServerResponse.setMovieListResponse(e.getMessage());
            movieListServerResponse.setStatusCode(-100);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            movieListServerResponse.setMovieListResponse(e.getMessage());
            movieListServerResponse.setStatusCode(-100);
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            movieListServerResponse.setMovieListResponse(e.getMessage());
            movieListServerResponse.setStatusCode(-100);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return movieListServerResponse;
    }

    @Override
    protected void onPostExecute(MovieListServerResponse s) {
        super.onPostExecute(s);
        mUserDetailInterface.onSuccessCallBack(s);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(MovieListServerResponse s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
