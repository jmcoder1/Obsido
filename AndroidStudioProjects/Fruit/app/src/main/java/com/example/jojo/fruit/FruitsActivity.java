package com.example.jojo.fruit;

import android.content.Context;
import android.content.Loader;

import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agrawalsuneet.loaderspack.loaders.CurvesLoader;

import java.util.ArrayList;
import java.util.List;

public class FruitsActivity extends AppCompatActivity implements LoaderCallbacks<List<Fruit>> {

    private static final String LOG_TAG = FruitsActivity.class.getName();

    // URL Fruit data
    private static final String DUMMY_REQUEST =
            "https://raw.githubusercontent.com/fmtvp/recruit-test-data/master/data.json";

    // Constant value for the fruit loader ID. Comes into play if you're using multiple loaders
    private static final int FRUIT_LOADER_ID = 1;

    private FruitAdapter mFruitAdapterAdapter;

    private RelativeLayout noConnectionView;
    private CurvesLoader mProgressLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruits);

        mProgressLoader = findViewById(R.id.loading_spinner);
        noConnectionView = findViewById(R.id.no_connection_view);
        noConnectionView.setVisibility(View.INVISIBLE);

        ListView fruitListView = (ListView) findViewById(R.id.fruit_list);
        // TODO: Change this to an actual empty layout
        fruitListView.setEmptyView(noConnectionView);

        mFruitAdapterAdapter = new FruitAdapter(this, new ArrayList<Fruit>());
        fruitListView.setAdapter(mFruitAdapterAdapter);

        fruitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO: Open item on click
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(0, null, this);
        } else {
            // Otherwise, display error and hide the loading indicator
            mProgressLoader.setVisibility(View.GONE);
            noConnectionView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<List<Fruit>> onCreateLoader(int i, Bundle bundle) {
        Log.e(LOG_TAG, "called: onCreateLoader");
        return new FruitLoader(this, DUMMY_REQUEST);
    }

    @Override
    public void onLoadFinished(Loader<List<Fruit>> loader, List<Fruit> fruits) {
        Log.e(LOG_TAG, "called: onLoadFinished");
        // Hide loading indicator because the data has been loaded
        mProgressLoader.setVisibility(View.GONE);
        mFruitAdapterAdapter.clear();

        // If there is a valid list of {@link Fruit}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (fruits != null && !fruits.isEmpty()) {
            mFruitAdapterAdapter.addAll(fruits);
            noConnectionView.setVisibility(View.GONE);
        }
        // TODO: Handle what would happen if there are no fruits shown, i.e. a link with no fruits in the JSON
    }

    @Override
    public void onLoaderReset(Loader<List<Fruit>> loader) {
        Log.e(LOG_TAG, "called: onLoaderReset");
        mFruitAdapterAdapter.clear();
    }
}
