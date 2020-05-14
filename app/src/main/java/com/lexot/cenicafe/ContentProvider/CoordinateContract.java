package com.lexot.cenicafe.ContentProvider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.lexot.cenicafe.DataBase.DatabaseContract;

public class CoordinateContract {

    public static final String AUTHORITY = "com.lexot.cenicafe.provider";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri COORDINATE_URI = Uri.withAppendedPath(CoordinateContract.BASE_URI, "/coordinate");

    public static final String URI_TYPE_COFFEE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE +
            "/vnd.com.lexot.cenicafe.coordinateprovider.coffee";

    public static final String URI_TYPE_COFFEE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            "/vnd.com.lexot.cenicafe.coordinateprovider.coffee";

    public static final class CoordinateColumns implements BaseColumns {

        private CoordinateColumns(){}

        public static final String BATCH_ID = DatabaseContract.Coordinates.COLUMN_NAME_COORDINATE_BATCHID;
        public static final String LAT = DatabaseContract.Coordinates.COLUMN_NAME_COORDINATE_LAT;
        public static final String LNG = DatabaseContract.Coordinates.COLUMN_NAME_COORDINATE_LNG;
        public static final String SYNCED = DatabaseContract.Coordinates.COLUMN_NAME_SYNCED;
        public static final String INDEX = DatabaseContract.Coordinates.COLUMN_NAME_COORDINATE_INDEX;
        public static final String DEFAULT_SORT_ORDER = INDEX + " ASC";
    }
}