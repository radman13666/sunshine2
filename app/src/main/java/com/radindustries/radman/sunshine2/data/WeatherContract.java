/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.radindustries.radman.sunshine2.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the weather database.
 */
public class WeatherContract {

    //uri authority for the contract
    public static final String CONTENT_AUTHORITY = "com.radindustries.radman.sunshine2.app";

    //the base uri for the contract
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //directories for the weather and location
    public static final String PATH_WEATHER = "weather";
    public static final String PATH_LOCATION = "location";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        //returns the time in your time-zone
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /*
        Inner class that defines the table contents of the location table
        Students: This is where you will add the strings.  (Similar to what has been
        done for WeatherEntry)
     */
    public static final class LocationEntry implements BaseColumns {

        //uris for location
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        public static final String CONTENT_TYPE = //specifies a directory of items
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        public static final String CONTENT_ITEM_TYPE = //specifies a particular item in a database
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        //location table name
        public static final String TABLE_NAME = "location";
        
        //column for the location setting
        public static final String COLUMN_LOCATION_SETTING = "location_setting";
        
        //column for the city name
        public static final String COLUMN_CITY_NAME = "city_name";
        
        //columns for the location's latitide and longitude
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";

        //URI functions that will help
        public static Uri buildLocationUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    /* Inner class that defines the table contents of the weather table */
    public static final class WeatherEntry implements BaseColumns {

        //uri constants for the weather
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;


        public static final String TABLE_NAME = "weather";

        // Column with the foreign key into the location table.
        public static final String COLUMN_LOC_KEY = "location_id";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_DATE = "date";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_WEATHER_ID = "weather_id";

        // Short description and long description of the weather, as provided by API.
        // e.g "clear" vs "sky is clear".
        public static final String COLUMN_SHORT_DESC = "short_desc";

        // Min and max temperatures for the day (stored as floats)
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_HUMIDITY = "humidity";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_PRESSURE = "pressure";

        // Windspeed is stored as a float representing windspeed  mph
        public static final String COLUMN_WIND_SPEED = "wind";

        // Degrees are meteorological degrees (e.g, 0 is north, 180 is south).  Stored as floats.
        public static final String COLUMN_DEGREES = "degrees";

        public static Uri buildWeatherUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildWeatherLocation(String locationSetting) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting).build();
        }

        public static Uri buildWeatherUriWithStartDate(
                String locationSetting, long startDate
        ) {
            long normalizedStartDate = normalizeDate(startDate);
            return CONTENT_URI.buildUpon().appendPath(locationSetting)
                    .appendQueryParameter(COLUMN_DATE, Long.toString(normalizedStartDate)).build();
        }

        public static Uri buildWeatherLocationWithStartDate(String locationSetting, long date) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting)
                    .appendPath(Long.toString(normalizeDate(date))).build();
        }

        public static String getLocationSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1); //gets the location string from the uri
        }

        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2)); //gets the date integer form the uri
        }

        public static long getStartDateFromUri(Uri uri) { //gets the start date integer from the uri
            String dateString = uri.getQueryParameter(COLUMN_DATE);
            if (null != dateString && dateString.length() > 0) {
                return Long.parseLong(dateString);
            }
            else
                return 0;
        }

    }
}
