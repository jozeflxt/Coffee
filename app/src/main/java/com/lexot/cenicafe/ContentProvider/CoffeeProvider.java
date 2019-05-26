package com.lexot.cenicafe.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.lexot.cenicafe.DataBase.CoffeeDbHelper;
import com.lexot.cenicafe.DataBase.DatabaseContract;

/**
 * Created by Carlos  Muñoz on 14/3/2017.
 */
public class CoffeeProvider extends ContentProvider {
    public static final int BRANCH_LIST = 1;
    public static final int BRANCH_ID = 2;
    public static final int FRAME_LIST = 3;
    public static final int FRAME_ID = 4;
    public static final int BATCH_LIST = 5;
    public static final int BATCH_ID = 6;
    public static final int TREE_LIST = 7;
    public static final int TREE_ID = 8;
    public static final int COORDINATE_LIST = 9;
    private static final UriMatcher sUriMatcher;
    static{
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(BranchContract.AUTHORITY, "branch", BRANCH_LIST);
        sUriMatcher.addURI(BranchContract.AUTHORITY, "branch/#", BRANCH_ID);
        sUriMatcher.addURI(FrameContract.AUTHORITY, "frame", FRAME_LIST);
        sUriMatcher.addURI(FrameContract.AUTHORITY, "frame/#", FRAME_ID);
        sUriMatcher.addURI(BatchContract.AUTHORITY, "batch", BATCH_LIST);
        sUriMatcher.addURI(BatchContract.AUTHORITY, "batch/#", BATCH_ID);
        sUriMatcher.addURI(BatchContract.AUTHORITY, "tree", TREE_LIST);
        sUriMatcher.addURI(BatchContract.AUTHORITY, "tree/#", TREE_ID);
        sUriMatcher.addURI(BatchContract.AUTHORITY, "coordinate", COORDINATE_LIST);
    }
    /*
        Instancia de StudentsDbHelper para interactuar con la base de datos
     */
    private CoffeeDbHelper mDbHelper;

    public CoffeeProvider() { }

    @Override
    public boolean onCreate() {
        mDbHelper = CoffeeDbHelper.getInstance(getContext());
        return true;
    }

    /*
        Llamado para borrar una o mas filas de una tabla
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rows = 0;
        switch (sUriMatcher.match(uri)) {
            case BATCH_LIST:
                rows = db.delete(DatabaseContract.Batches.TABLE_NAME, null, null);
                break;
            case BATCH_ID:
                rows = db.delete(DatabaseContract.Batches.TABLE_NAME, selection, selectionArgs);
            case BRANCH_LIST:
                rows = db.delete(DatabaseContract.Branches.TABLE_NAME, null, null);
                break;
            case BRANCH_ID:
                rows = db.delete(DatabaseContract.Branches.TABLE_NAME, selection, selectionArgs);
            case FRAME_LIST:
                rows = db.delete(DatabaseContract.Frames.TABLE_NAME, null, null);
                break;
            case FRAME_ID:
                rows = db.delete(DatabaseContract.Frames.TABLE_NAME, selection, selectionArgs);
            case TREE_LIST:
                rows = db.delete(DatabaseContract.Trees.TABLE_NAME, null, null);
                break;
            case TREE_ID:
                rows = db.delete(DatabaseContract.Trees.TABLE_NAME, selection, selectionArgs);
                break;
            case COORDINATE_LIST:
                rows = db.delete(DatabaseContract.Coordinates.TABLE_NAME, null, null);
                break;
        }
        // Se retorna el numero de filas eliminadas
        return rows;
    }

    /*
        Se determina el MIME Type del dato o conjunto de datos al que apunta la URI
     */
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case BRANCH_LIST:
                return BranchContract.URI_TYPE_COFFEE_DIR;
            case BRANCH_ID:
                return BranchContract.URI_TYPE_COFFEE_ITEM;
            case BATCH_LIST:
                return BatchContract.URI_TYPE_COFFEE_DIR;
            case BATCH_ID:
                return BatchContract.URI_TYPE_COFFEE_ITEM;
            case FRAME_LIST:
                return FrameContract.URI_TYPE_COFFEE_DIR;
            case FRAME_ID:
                return FrameContract.URI_TYPE_COFFEE_ITEM;
            case TREE_LIST:
                return TreeContract.URI_TYPE_COFFEE_DIR;
            case TREE_ID:
                return TreeContract.URI_TYPE_COFFEE_ITEM;
            case COORDINATE_LIST:
                return CoordinateContract.URI_TYPE_COFFEE_DIR;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Long id = Long.valueOf(0);
        switch (sUriMatcher.match(uri)){
            case BRANCH_LIST:
            case BRANCH_ID:
                id = db.insert(DatabaseContract.Branches.TABLE_NAME, null, values);
                break;
            case BATCH_LIST:
            case BATCH_ID:
                id = db.insert(DatabaseContract.Batches.TABLE_NAME, null, values);
                break;
            case FRAME_LIST:
            case FRAME_ID:
                id = db.insert(DatabaseContract.Frames.TABLE_NAME, null, values);
                break;
            case TREE_LIST:
            case TREE_ID:
                id = db.insert(DatabaseContract.Trees.TABLE_NAME, null, values);
                break;
            case COORDINATE_LIST:
            default:
                id = db.insert(DatabaseContract.Coordinates.TABLE_NAME, null, values);
                break;
        }
        // Le avisa a los observadores
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(BranchContract.BASE_URI + "/" + id);

    }

    /*
        Retorna el o los datos que se le pida de acuerdo a la URI
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String tableName = "";
        switch (sUriMatcher.match(uri)){

            case BATCH_LIST:
                // Si no hay un orden especificado,
                // lo ordenamos de manera ascendente de acuerdo a lo que diga el contrato
                if (sortOrder == null || TextUtils.isEmpty(sortOrder))
                    sortOrder = BatchContract.BatchColumns.DEFAULT_SORT_ORDER;
                tableName = DatabaseContract.Batches.TABLE_NAME;
                break;

            // Se pide un estudiante en particular
            case BATCH_ID:
                // Se adjunta la ID del estudiante selecciondo en el filtro de la seleccion
                if (selection == null)
                    selection = "";
                selection = selection + "_ID = " + uri.getLastPathSegment();
                tableName = DatabaseContract.Batches.TABLE_NAME;
                break;
            // Se pide la lista completa de estudiantes
            case BRANCH_LIST:
                // Si no hay un orden especificado,
                // lo ordenamos de manera ascendente de acuerdo a lo que diga el contrato
                if (sortOrder == null || TextUtils.isEmpty(sortOrder))
                    sortOrder = BranchContract.BranchColumns.DEFAULT_SORT_ORDER;
                tableName = DatabaseContract.Branches.TABLE_NAME;
                break;

            // Se pide un estudiante en particular
            case BRANCH_ID:
                // Se adjunta la ID del estudiante selecciondo en el filtro de la seleccion
                if (selection == null)
                    selection = "";
                selection = selection + "_ID = " + uri.getLastPathSegment();
                tableName = DatabaseContract.Branches.TABLE_NAME;
                break;
            // Se pide la lista completa de estudiantes
            case FRAME_LIST:
                // Si no hay un orden especificado,
                // lo ordenamos de manera ascendente de acuerdo a lo que diga el contrato
                if (sortOrder == null || TextUtils.isEmpty(sortOrder))
                    sortOrder = FrameContract.FrameColumns.DEFAULT_SORT_ORDER;
                tableName = DatabaseContract.Frames.TABLE_NAME;
                break;

            // Se pide un estudiante en particular
            case FRAME_ID:
                // Se adjunta la ID del estudiante selecciondo en el filtro de la seleccion
                if (selection == null)
                    selection = "";
                selection = selection + "_ID = " + uri.getLastPathSegment();
                tableName = DatabaseContract.Frames.TABLE_NAME;
                break;
            // Se pide la lista completa de estudiantes
            case TREE_LIST:
                // Si no hay un orden especificado,
                // lo ordenamos de manera ascendente de acuerdo a lo que diga el contrato
                if (sortOrder == null || TextUtils.isEmpty(sortOrder))
                    sortOrder = TreeContract.TreeColumns.DEFAULT_SORT_ORDER;
                tableName = DatabaseContract.Trees.TABLE_NAME;
                break;

            // Se pide un árbol en particular
            case TREE_ID:
                // Se adjunta la ID del árbol selecciondo en el filtro de la seleccion
                if (selection == null)
                    selection = "";
                selection = selection + "_ID = " + uri.getLastPathSegment();
                tableName = DatabaseContract.Trees.TABLE_NAME;
                break;
            case COORDINATE_LIST:
                // Si no hay un orden especificado,
                // lo ordenamos de manera ascendente de acuerdo a lo que diga el contrato
                if (sortOrder == null || TextUtils.isEmpty(sortOrder))
                    sortOrder = CoordinateContract.CoordinateColumns.DEFAULT_SORT_ORDER;
                tableName = DatabaseContract.Coordinates.TABLE_NAME;
                break;
            // La URI que se recibe no esta definida
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
        Cursor cursor = db.query(tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        // Se retorna un cursor sobre el cual se debe iterar para obtener los datos
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String tableName = "";
        if (selection == null)
            selection = "";
        switch (sUriMatcher.match(uri)){
            case BATCH_LIST:
                tableName = DatabaseContract.Batches.TABLE_NAME;
                break;
            case BATCH_ID:
                tableName = DatabaseContract.Batches.TABLE_NAME;
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;
            case BRANCH_LIST:
                tableName = DatabaseContract.Branches.TABLE_NAME;
                break;
            case BRANCH_ID:
                tableName = DatabaseContract.Branches.TABLE_NAME;
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;
            case TREE_LIST:
                tableName = DatabaseContract.Trees.TABLE_NAME;
                break;
            case TREE_ID:
                tableName = DatabaseContract.Trees.TABLE_NAME;
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;
            case FRAME_LIST:
                tableName = DatabaseContract.Frames.TABLE_NAME;
                break;
            case FRAME_ID:
                tableName = DatabaseContract.Frames.TABLE_NAME;
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;
            case COORDINATE_LIST:
            default:
                tableName = DatabaseContract.Coordinates.TABLE_NAME;
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;
        }

        int changes = db.update(tableName, values, selection, selectionArgs);
        // Le avisa a los observadores
        getContext().getContentResolver().notifyChange(uri, null);

        return changes;
    }
}