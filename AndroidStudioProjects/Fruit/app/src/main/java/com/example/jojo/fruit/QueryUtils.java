package com.example.jojo.fruit;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private QueryUtils() { }

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the fruit data set and return a list of {@link Fruit} objects.
     */
    public static List<Fruit> fetchFruitData(String requestUrl) {
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link fruit}s
        List<Fruit> fruits = extractFeatureFromJson(jsonResponse);
        // Return the list of {@link Fruit}s
        return fruits;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the fruit JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why the
                // makeHttpRequest method signature specifies than an IOException
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Fruit} objects that has been built up from
     * parsing a JSON response.
     * parsing the given JSON response.
     */
    private static List<Fruit> extractFeatureFromJson(String fruitJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(fruitJSON)) {
            return null;
        }
        List<Fruit> fruits = new ArrayList<>();

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(fruitJSON);

            JSONArray fruitArray = baseJsonResponse.getJSONArray("fruit");

            // For each fruit in the fruitArray, create an {@link Fruit} object
            for (int i = 0; i < fruitArray.length(); i++) {
                JSONObject currentFruit = fruitArray.getJSONObject(i);

                // Gets the values for fruit from the JSON
                String currentFruitName = currentFruit.getString("type");
                int currentFruitPrice = currentFruit.getInt("price");
                int currentFruitWeight = currentFruit.getInt("weight");

                Fruit fruit = new Fruit(currentFruitName, currentFruitPrice, currentFruitWeight);
                fruits.add(fruit);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the fruit JSON results", e);
        }

        return fruits;
    }
}
