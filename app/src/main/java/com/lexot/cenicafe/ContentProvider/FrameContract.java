package com.lexot.cenicafe.ContentProvider;

/**
 * Created by Carlos  Muñoz on 14/3/2017.
 */
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import com.lexot.cenicafe.DataBase.DatabaseContract;

/**
 *  Esta clase provee las constantes y URIs necesarias para trabajar con el StudentsProvider
 */
public final class FrameContract {

    public static final String AUTHORITY = "com.lexot.cenicafe.provider";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri FRAME_URI = Uri.withAppendedPath(FrameContract.BASE_URI, "/frame");

    /*
        MIME Types
        Para listas se necesita  'vnd.android.cursor.dir/vnd.com.example.andres.provider.students
        Para items se necesita 'vnd.android.cursor.item/vnd.com.example.andres.provider.students'
        La primera parte viene esta definida en constantes de ContentResolver
     */
    public static final String URI_TYPE_COFFEE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE +
            "/vnd.com.lexot.cenicafe.frameprovider.coffee";

    public static final String URI_TYPE_COFFEE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            "/vnd.com.lexot.cenicafe.frameprovider.coffee";

    /*
        Tabla definida en provider. Aca podria ser una distinta a la de la base de datos,
        pero consideramos la misma.
     */
    public static final class FrameColumns implements BaseColumns {

        private FrameColumns(){}

        public static final String FACTOR = DatabaseContract.Frames.COLUMN_NAME_FRAME_FACTOR;
        public static final String TIME = DatabaseContract.Frames.COLUMN_NAME_FRAME_TIME;
        public static final String DATA = DatabaseContract.Frames.COLUMN_NAME_FRAME_DATA;
        public static final String BRANCH_ID = DatabaseContract.Frames.COLUMN_NAME_FRAME_BRANCHID;
        public static final String SYNCED = DatabaseContract.Frames.COLUMN_NAME_SYNCED;

        public static final String DEFAULT_SORT_ORDER = BRANCH_ID + " DESC";
    }
}