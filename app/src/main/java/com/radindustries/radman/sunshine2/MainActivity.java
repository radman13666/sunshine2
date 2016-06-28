package com.radindustries.radman.sunshine2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private final String FORECASTFRAGMENT_TAG = "FFTAG";
    private String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        mLocation = Utility.getPreferredLocation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment, new ForecastFragment())
//                    .add(R.id.fragment, new ForecastFragment(), FORECASTFRAGMENT_TAG)
//                    .commit();
//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        String location = Utility.getPreferredLocation( this );
//        // update the location in our second pane using the fragment manager
//        if (location != null && !location.equals(mLocation)) {
//            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
//            if ( null != ff ) {
//                ff.onLocationChanged();
//            }
//            mLocation = location;
//        }
//    }

}
