package com.lexot.cenicafe.ContentProvider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.lexot.cenicafe.DataBase.DatabaseContract;

public class TreeContract {

    public static final String AUTHORITY = "com.lexot.cenicafe.provider";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri TREE_URI = Uri.withAppendedPath(TreeContract.BASE_URI, "/tree");

    public static final String URI_TYPE_COFFEE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE +
            "/vnd.com.lexot.cenicafe.treeprovider.coffee";

    public static final String URI_TYPE_COFFEE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            "/vnd.com.lexot.cenicafe.treeprovider.coffee";

    public static final class TreeColumns implements BaseColumns {

        private TreeColumns(){}

        public static final String BATCH_ID = DatabaseContract.Trees.COLUMN_NAME_TREE_BATCHID;
        public static final String INDEX = DatabaseContract.Trees.COLUMN_NAME_TREE_INDEX;
        public static final String REAL_ID = DatabaseContract.Trees.COLUMN_NAME_TREE_REALID;
        public static final String SYNCED = DatabaseContract.Trees.COLUMN_NAME_SYNCED;
        public static final String DEFAULT_SORT_ORDER = REAL_ID + " DESC";
    }
}