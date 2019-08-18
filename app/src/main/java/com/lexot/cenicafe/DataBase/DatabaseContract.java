package com.lexot.cenicafe.DataBase;

import android.provider.BaseColumns;

/**
 * Created by carlosmunoz on 14/03/17.
 */
public class DatabaseContract {
    public DatabaseContract() {
    }
    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String REAL_TYPE = " REAL";
    public static final String BLOB_TYPE = " BLOB";
    public static final String COMMA_SEP = ",";
    public static abstract class Batches implements BaseColumns {
        // BaseColumns nos entrega las constantes \_ID y \_COUNT

        public static final String TABLE_NAME = "batch";
        public static final String COLUMN_NAME_BATCH_NAME = "name";
        public static final String COLUMN_NAME_BATCH_AGE = "age";
        public static final String COLUMN_NAME_BATCH_TREES = "trees";
        public static final String COLUMN_NAME_BATCH_TOTAL_TREES = "totalTrees";
        public static final String COLUMN_NAME_BATCH_BRANCHES = "branches";
        public static final String COLUMN_NAME_BATCH_STEMS = "stems";
        public static final String COLUMN_NAME_BATCH_REAL_ID = "realid";
        public static final String COLUMN_NAME_SYNCED = "synced";

        public static final String SQL_CREATE_BATCHES_TABLE =
                "CREATE TABLE " + Batches.TABLE_NAME + " (" +
                        Batches._ID + " INTEGER PRIMARY KEY," +
                        Batches.COLUMN_NAME_BATCH_NAME + TEXT_TYPE + COMMA_SEP +
                        Batches.COLUMN_NAME_BATCH_AGE + INTEGER_TYPE + COMMA_SEP +
                        Batches.COLUMN_NAME_BATCH_TREES + INTEGER_TYPE + COMMA_SEP+
                        Batches.COLUMN_NAME_BATCH_TOTAL_TREES + INTEGER_TYPE + COMMA_SEP+
                        Batches.COLUMN_NAME_BATCH_BRANCHES + INTEGER_TYPE + COMMA_SEP+
                        Batches.COLUMN_NAME_BATCH_STEMS + INTEGER_TYPE + COMMA_SEP+
                        Batches.COLUMN_NAME_SYNCED + INTEGER_TYPE + COMMA_SEP+
                        Batches.COLUMN_NAME_BATCH_REAL_ID + INTEGER_TYPE +
                        " )";

        public static final String SQL_DELETE_BATCHES_TABLE =
                "DROP TABLE IF EXISTS " + Batches.TABLE_NAME;
    }
    public static abstract class Branches implements BaseColumns {
        // BaseColumns nos entrega las constantes \_ID y \_COUNT

        public static final String TABLE_NAME = "branch";
        public static final String COLUMN_NAME_BRANCH_DATE = "date";
        public static final String COLUMN_NAME_BRANCH_TREEID = "treeid";
        public static final String COLUMN_NAME_BRANCH_INDEX = "indexBranch";
        public static final String COLUMN_NAME_BRANCH_STEMID = "stemid";
        public static final String COLUMN_NAME_BRANCH_REALID = "realid";
        public static final String COLUMN_NAME_SYNCED = "synced";

        public static final String SQL_CREATE_BRANCHES_TABLE =
                "CREATE TABLE " + Branches.TABLE_NAME + " (" +
                        Branches._ID + " INTEGER PRIMARY KEY," +
                        Branches.COLUMN_NAME_BRANCH_DATE + TEXT_TYPE + COMMA_SEP +
                        Branches.COLUMN_NAME_BRANCH_TREEID + INTEGER_TYPE + COMMA_SEP+
                        Branches.COLUMN_NAME_BRANCH_INDEX + INTEGER_TYPE + COMMA_SEP+
                        Branches.COLUMN_NAME_BRANCH_STEMID + INTEGER_TYPE + COMMA_SEP+
                        Branches.COLUMN_NAME_SYNCED + INTEGER_TYPE + COMMA_SEP+
        Branches.COLUMN_NAME_BRANCH_REALID + INTEGER_TYPE +
                " )";

        public static final String SQL_DELETE_BRANCHES_TABLE =
                "DROP TABLE IF EXISTS " + Branches.TABLE_NAME;
    }
    public static abstract class Trees implements BaseColumns {
        // BaseColumns nos entrega las constantes \_ID y \_COUNT

        public static final String TABLE_NAME = "tree";
        public static final String COLUMN_NAME_TREE_REALID = "realid";
        public static final String COLUMN_NAME_TREE_BATCHID = "batchid";
        public static final String COLUMN_NAME_TREE_INDEX = "indexTree";
        public static final String COLUMN_NAME_SYNCED = "synced";

        public static final String SQL_CREATE_TREES_TABLE =
                "CREATE TABLE " + Trees.TABLE_NAME + " (" +
                        Trees._ID + " INTEGER PRIMARY KEY," +
                        Trees.COLUMN_NAME_TREE_BATCHID + INTEGER_TYPE + COMMA_SEP+
                        Trees.COLUMN_NAME_TREE_INDEX + INTEGER_TYPE + COMMA_SEP+
                        Trees.COLUMN_NAME_SYNCED + INTEGER_TYPE + COMMA_SEP+
                        Trees.COLUMN_NAME_TREE_REALID + INTEGER_TYPE +
                        " )";

        public static final String SQL_DELETE_TREES_TABLE =
                "DROP TABLE IF EXISTS " + Trees.TABLE_NAME;
    }
    public static abstract class Coordinates implements BaseColumns {
        // BaseColumns nos entrega las constantes \_ID y \_COUNT

        public static final String TABLE_NAME = "coordinate";
        public static final String COLUMN_NAME_COORDINATE_LAT = "lat";
        public static final String COLUMN_NAME_COORDINATE_LNG = "lng";
        public static final String COLUMN_NAME_COORDINATE_INDEX = "indexCoordinate";
        public static final String COLUMN_NAME_COORDINATE_BATCHID = "batchId";
        public static final String COLUMN_NAME_SYNCED = "synced";

        public static final String SQL_CREATE_COORDINATES_TABLE =
                "CREATE TABLE " + Coordinates.TABLE_NAME + " (" +
                        Coordinates._ID + " INTEGER PRIMARY KEY," +
                        Coordinates.COLUMN_NAME_COORDINATE_LAT + REAL_TYPE + COMMA_SEP +
                        Coordinates.COLUMN_NAME_COORDINATE_LNG + REAL_TYPE + COMMA_SEP+
                        Coordinates.COLUMN_NAME_COORDINATE_INDEX + INTEGER_TYPE + COMMA_SEP+
                        Coordinates.COLUMN_NAME_SYNCED + INTEGER_TYPE + COMMA_SEP+
                        Coordinates.COLUMN_NAME_COORDINATE_BATCHID + INTEGER_TYPE +
                        " )";

        public static final String SQL_DELETE_COORDINATES_TABLE =
                "DROP TABLE IF EXISTS " + Coordinates.TABLE_NAME;
    }
    public static abstract class Frames implements BaseColumns {
        // BaseColumns nos entrega las constantes \_ID y \_COUNT

        public static final String TABLE_NAME = "frame";
        public static final String COLUMN_NAME_FRAME_FACTOR = "factor";
        public static final String COLUMN_NAME_FRAME_TIME = "time";
        public static final String COLUMN_NAME_FRAME_BRANCHID = "branchid";
        public static final String COLUMN_NAME_FRAME_DATA = "data";
        public static final String COLUMN_NAME_SYNCED = "synced";


        public static final String SQL_CREATE_FRAMES_TABLE =
                "CREATE TABLE " + Frames.TABLE_NAME + " (" +
                        Branches._ID + " INTEGER PRIMARY KEY," +
        Frames.COLUMN_NAME_FRAME_FACTOR + REAL_TYPE + COMMA_SEP +
        Frames.COLUMN_NAME_FRAME_TIME + INTEGER_TYPE + COMMA_SEP +
        Frames.COLUMN_NAME_FRAME_BRANCHID + INTEGER_TYPE + COMMA_SEP+
                        Frames.COLUMN_NAME_SYNCED + INTEGER_TYPE + COMMA_SEP+
        Frames.COLUMN_NAME_FRAME_DATA + TEXT_TYPE +
                " )";

        public static final String SQL_DELETE_FRAMES_TABLE =
                "DROP TABLE IF EXISTS " + Frames.TABLE_NAME;
    }
}
