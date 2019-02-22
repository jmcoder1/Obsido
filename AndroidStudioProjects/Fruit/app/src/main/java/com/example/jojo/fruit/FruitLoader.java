package com.example.jojo.fruit;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

public class FruitLoader extends AsyncTaskLoader<List<Fruit>> {

    // Query URL for the JSON data
    private String mUrl;

    /**
     * This constructor handles the creation of an FruitLoader object.
     *
     * @param context The context the class is initialised in.
     * @param url The website query data for the JSON data.
     */
    public FruitLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    /**
     * Runs on the main thread after the background work has been completed. This method receives
     * as input, the return value from doInBackground(). We clear out the adapter, to get rid of
     * fruit data from a previous query. Then we update the adapter with the new list of fruits,
     * which will trigger the ListView to re-populate its list items.
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This method runs on a background thread and performs the network request.
     * We should not update the UI from a background thread, so we return a list of
     * {@link Fruit}s as the result.
     *
     * @return List<Fruit> The List containing the data about the fruits.
     */
    @Override
    public List<Fruit> loadInBackground() {
        if (mUrl == null) return null;

        return QueryUtils.fetchFruitData(mUrl);
    }

}
