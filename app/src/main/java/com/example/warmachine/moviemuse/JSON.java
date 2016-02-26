package com.example.warmachine.moviemuse;


import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;

public class JSON implements Serializable {

    private String mOriginalTitle;
    private String mPlotSynopsis;
    private double mUserRating;
    private String mReleaseDate;

    private String mImageUrl;

    public String getImageUrl() {
        return mImageUrl;

    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    public double getUserRating() {
        return mUserRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    private JSON(String originalTitle, String plotSynopsis, double userRating, String releaseDate, String imageUrl) {
        this.mOriginalTitle = originalTitle;
        this.mPlotSynopsis = plotSynopsis;
        this.mUserRating = userRating;
        this.mReleaseDate = releaseDate;
        this.mImageUrl = imageUrl;
    }

    public static ArrayList<JSON> createMovieListFromJson(String moviesJsonString) throws JSONException {

        JSONObject moviesJson = new JSONObject(moviesJsonString);

        final String MOVIEDB_RESULTS = "results";
        JSONArray moviesArray = moviesJson.getJSONArray(MOVIEDB_RESULTS);

        ArrayList<JSON> JSONList = new ArrayList<>();
        for(int movieNumber = 0; movieNumber < moviesArray.length(); ++movieNumber)
        {
            JSON JSON = parseJsonMovieObject(moviesArray.getJSONObject(movieNumber));
            JSONList.add(JSON);
        }


        return JSONList;
    }

    private static JSON parseJsonMovieObject(JSONObject movieJsonObject) throws JSONException {

        final String MOVIEDB_ORIGINAL_TITLE = "original_title";
        final String MOVIEDB_POSTER_PATH = "poster_path";
        final String MOVIEDB_PLOT_SYNOPSIS = "overview";
        final String MOVIEDB_USER_RATING = "vote_average";
        final String MOVIEDB_RELEASE_DATE = "release_date";
        double userRating = movieJsonObject.getDouble(MOVIEDB_USER_RATING);

        String originalTitle = movieJsonObject.getString( MOVIEDB_ORIGINAL_TITLE );
        String plotSynopsis = movieJsonObject.getString(MOVIEDB_PLOT_SYNOPSIS);
        String releaseDate = movieJsonObject.getString(MOVIEDB_RELEASE_DATE);
        String posterPath = movieJsonObject.getString(MOVIEDB_POSTER_PATH);

        final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
        Uri.Builder posterUrl = Uri.parse(POSTER_BASE_URL).buildUpon();
        String posterSize = "w185";
        posterUrl.appendEncodedPath( posterSize );
        posterUrl.appendEncodedPath(posterPath);
        String imageUrl = posterUrl.toString();

        return new JSON(originalTitle, plotSynopsis, userRating, releaseDate, imageUrl);
    }

    @Override
    public String toString() {
        return "\n"
                + "Original title: " + mOriginalTitle
                + "\n" + "Plot synopsis: " + mPlotSynopsis
                + "\n" + "User rating: " + String.valueOf(mUserRating)
                + "\n" + "Release date: " + mReleaseDate
                + "\n" + "Image URL: " + mImageUrl
                + "\n";
    }
}
