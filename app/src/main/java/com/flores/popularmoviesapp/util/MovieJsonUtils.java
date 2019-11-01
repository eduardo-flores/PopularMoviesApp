package com.flores.popularmoviesapp.util;

import com.flores.popularmoviesapp.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
public final class MovieJsonUtils {

    public static String[] getListFromJson(String movieJsonStr)
            throws JSONException {

        final String RESULT_LIST = "results";
        final String STATUS_CODE = "status_code";
        final String STATUS_MESSAGE = "status_message";

        String[] parsedData;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        /* Is there an error? */
        if (movieJson.has(STATUS_CODE) && movieJson.has(STATUS_MESSAGE)) {
            String message = movieJson.getString(STATUS_MESSAGE);

            throw new JSONException(message);
        }

        JSONArray movieArray = movieJson.getJSONArray(RESULT_LIST);

        parsedData = new String[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            parsedData[i] = movieArray.getJSONObject(i).toString();
        }

        return parsedData;
    }

    public static Movie getMovieFromJson(String movieJsonStr)
            throws JSONException {

        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_TITLE = "title";
        final String MOVIE_VOTE_AVERAGE = "vote_average";
        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_SYNOPSIS = "overview";

        JSONObject movieJson = new JSONObject(movieJsonStr);

        /* Is there an error? */
        if (!movieJson.has(MOVIE_TITLE) && !movieJson.has(MOVIE_POSTER)) {
            String message = "Invalid JSON Object.";

            throw new JSONException(message);
        }

        Movie movie = new Movie();
        movie.setTitle(movieJson.getString(MOVIE_TITLE));
        movie.setVoteAverage(movieJson.getDouble(MOVIE_VOTE_AVERAGE));
        movie.setPoster(NetworkUtils.buildImageUrl(movieJson.getString(MOVIE_POSTER).replace("/", "")).toString());
        movie.setReleaseDate(movieJson.getString(MOVIE_RELEASE_DATE));
        movie.setSynopsis(movieJson.getString(MOVIE_SYNOPSIS));

        return movie;
    }


}