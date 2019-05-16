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
public final class BranchContract {

    public static final String AUTHORITY = "com.lexot.cenicafe.provider";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri BRANCH_URI = Uri.withAppendedPath(BranchContract.BASE_URI, "/branch");

    /*
        MIME Types
        Para listas se necesita  'vnd.android.cursor.dir/vnd.com.example.andres.provider.students
        Para items se necesita 'vnd.android.cursor.item/vnd.com.example.andres.provider.students'
        La primera parte viene esta definida en constantes de ContentResolver
     */
    public static final String URI_TYPE_COFFEE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE +
            "/vnd.com.lexot.cenicafe.branchprovider.coffee";

    public static final String URI_TYPE_COFFEE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            "/vnd.com.lexot.cenicafe.branchprovider.coffee";

    /*
        Tabla definida en provider. Aca podria ser una distinta a la de la base de datos,
        pero consideramos la misma.
     */
    public static final class BranchColumns implements BaseColumns {

        private BranchColumns(){}

        public static final String TYPE = DatabaseContract.Branches.COLUMN_NAME_BRANCH_TYPE;
        public static final String VIDEO = DatabaseContract.Branches.COLUMN_NAME_BRANCH_VIDEO;
        public static final String REAL_ID = DatabaseContract.Branches.COLUMN_NAME_BRANCH_REALID;
        public static final String TREE_ID = DatabaseContract.Branches.COLUMN_NAME_BRANCH_TREEID;
        public static final String INDEX = DatabaseContract.Branches.COLUMN_NAME_BRANCH_INDEX;
        public static final String STEM_ID = DatabaseContract.Branches.COLUMN_NAME_BRANCH_STEMID;
        public static final String DATE = DatabaseContract.Branches.COLUMN_NAME_BRANCH_DATE;
        public static final String SYNCED = DatabaseContract.Branches.COLUMN_NAME_SYNCED;
        public static final String DEFAULT_SORT_ORDER = REAL_ID + " DESC";
    }
}