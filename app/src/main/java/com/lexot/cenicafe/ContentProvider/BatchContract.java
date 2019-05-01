package com.lexot.cenicafe.ContentProvider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.lexot.cenicafe.DataBase.DatabaseContract;

public final class BatchContract {

    public static final String AUTHORITY = "com.lexot.cenicafe.provider";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri BATCH_URI = Uri.withAppendedPath(BatchContract.BASE_URI, "/batch");

    public static final String URI_TYPE_COFFEE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE +
            "/vnd.com.lexot.cenicafe.batchprovider.coffee";

    public static final String URI_TYPE_COFFEE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            "/vnd.com.lexot.cenicafe.batchprovider.coffee";

    public static final class BatchColumns implements BaseColumns {

        private BatchColumns(){}

        public static final String NAME = DatabaseContract.Batches.COLUMN_NAME_BATCH_NAME;
        public static final String AGE = DatabaseContract.Batches.COLUMN_NAME_BATCH_AGE;
        public static final String BRANCHES = DatabaseContract.Batches.COLUMN_NAME_BATCH_BRANCHES;
        public static final String TREES = DatabaseContract.Batches.COLUMN_NAME_BATCH_TREES;
        public static final String STEMS = DatabaseContract.Batches.COLUMN_NAME_BATCH_STEMS;
        public static final String REAL_ID = DatabaseContract.Batches.COLUMN_NAME_BATCH_REAL_ID;
        public static final String SYNCED = DatabaseContract.Branches.COLUMN_NAME_SYNCED;
        public static final String DEFAULT_SORT_ORDER = REAL_ID + " DESC";
    }
}