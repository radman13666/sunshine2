package com.radindustries.radman.sunshine2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.radindustries.radman.sunshine2.data.WeatherContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private static final String SUNSHINE_HASH_TAG = "#SunShineApp";
    private String mForecastStr;
    private ShareActionProvider mShareActionProvider;
    private static final int DETAIL_LOADER = 0;

    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP
    };

    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //opens the activity and displays the given data
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if (intent != null) {
            mForecastStr = intent.getDataString();
            ((TextView) rootView.findViewById(R.id.detail_text)).setText(mForecastStr);
        }
        return rootView;
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET); //returns u to the app after sharing
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + SUNSHINE_HASH_TAG);
        return Intent.createChooser(shareIntent, getResources().getText(R.string.send_to));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu); //puts action buttons if present
        MenuItem menuItem = menu.findItem(R.id.action_share); //creates the share action button
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        //creates the action provider and attaches it to the menuItem

        if (mShareActionProvider != null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null!");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.detail_action_settings){
            //code to enter the SETTINGS activity
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        return new CursorLoader(getActivity(),
                intent.getData(),
                FORECAST_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");

        if (!data.moveToFirst()) { return; }

        String dateString = Utility.formatDate(data.getLong(COL_WEATHER_DATE));
        String weatherDescription = data.getString(COL_WEATHER_DESC);
        boolean isMetric = Utility.isMetric(getActivity());
        String high = Utility.formatTemperature(data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
        String low = Utility.formatTemperature(data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

        mForecastStr = String.format("%s - %s - %s / %s", dateString, weatherDescription, high, low);

        TextView detailTextView = (TextView) getView().findViewById(R.id.detail_text);
        detailTextView.setText(mForecastStr);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //initialise the cursor loader
        //getLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }
}
