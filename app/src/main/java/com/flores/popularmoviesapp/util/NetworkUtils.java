package com.flores.popularmoviesapp.util;

import android.net.Uri;
import android.util.Log;

import com.flores.popularmoviesapp.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_DATABASE_URL = "https://api.themoviedb.org";

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com";

    private static final String WATCH_PATH = "watch";
    private static final String VIDEO_PARAM = "v";

    private static final String API_KEY_PARAM = "api_key";

    private static final String POSTER_SIZE = "w185";
    private static final String AUTH_VERSION = "3";
    private static final String MOVIE_PATH = "movie";
    private static final String TRAILER_PATH = "videos";
    private static final String REVIEW_PATH = "reviews";

    private static final String POPULAR_PATH = "popular";
    private static final String TOP_RATED_PATH = "top_rated";

    /**
     * Builds the URL used to return the list of movies
     *
     * @return The URL to use to query the The Movie DB.
     */
    public static URL buildUrl(Sort sort) {
        String sortPath = POPULAR_PATH;
        switch (sort) {
            case POPULAR:
                sortPath = POPULAR_PATH;
                break;
            case TOP_RATED:
                sortPath = TOP_RATED_PATH;
                break;
        }

        Uri builtUri = Uri.parse(MOVIE_DATABASE_URL).buildUpon()
                .appendPath(AUTH_VERSION)
                .appendPath(MOVIE_PATH)
                .appendPath(sortPath)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * Builds the URL used to return the list of trailers for a movie
     *
     * @return The URL to use to query the The Movie DB.
     */
    public static URL buildTrailerUrl(String movieId) {
        Uri builtUri = Uri.parse(MOVIE_DATABASE_URL).buildUpon()
                .appendPath(AUTH_VERSION)
                .appendPath(MOVIE_PATH)
                .appendPath(movieId)
                .appendPath(TRAILER_PATH)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Trailer URI " + url);

        return url;
    }

    /**
     * Builds the URL reviews of a specific movie
     *
     * @return The URL to use to query the The Movie DB.
     */
    public static URL buildReviewUrl(String movieId) {
        Uri builtUri = Uri.parse(MOVIE_DATABASE_URL).buildUpon()
                .appendPath(AUTH_VERSION)
                .appendPath(MOVIE_PATH)
                .appendPath(movieId)
                .appendPath(REVIEW_PATH)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Review URI " + url);

        return url;
    }

    /**
     * Builds the Uri used to redirect and watch the movie trailer or clip
     *
     * @return The Uri to redirect to youtube or web browser
     */
    public static Uri buildWatchUrl(String movieKey) {
        Uri builtUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendPath(WATCH_PATH)
                .appendQueryParameter(VIDEO_PARAM, movieKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Youtube URI " + url);

        return builtUri;
    }

    /**
     * Builds the URL used to return the Movie poster
     *
     * @return The URL to of the image
     */
    static URL buildImageUrl(String imageFile) {
        Uri builtUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendPath(POSTER_SIZE)
                .appendPath(imageFile)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Image URI " + url);

        return url;
    }

    public enum Sort {
        POPULAR,
        TOP_RATED
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            int status = urlConnection.getResponseCode();
            Log.v(TAG, "Status code: " + status);

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}