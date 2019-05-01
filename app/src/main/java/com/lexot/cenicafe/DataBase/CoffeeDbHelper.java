package com.lexot.cenicafe.DataBase;

/**
 * Created by carlosmunoz on 14/03/17.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CoffeeDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Coffee.db";

    private static CoffeeDbHelper sInstance;

    private CoffeeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Nos aseguramos de que solo haya una instancia para evitar errores.
    // Mas detalles:
    // http://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html
    public static synchronized CoffeeDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CoffeeDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.Batches.SQL_CREATE_BATCHES_TABLE);
        db.execSQL(DatabaseContract.Trees.SQL_CREATE_TREES_TABLE);
        db.execSQL(DatabaseContract.Branches.SQL_CREATE_BRANCHES_TABLE);
        db.execSQL(DatabaseContract.Frames.SQL_CREATE_FRAMES_TABLE);
    }

    // Cambia la version del esquema en caso de haber modificaciones.
    // Por simplicidad asumimos que esto no va a pasar y tan solo se resetea la db.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.Batches.SQL_DELETE_BATCHES_TABLE);
        db.execSQL(DatabaseContract.Trees.SQL_DELETE_TREES_TABLE);
        db.execSQL(DatabaseContract.Branches.SQL_DELETE_BRANCHES_TABLE);
        db.execSQL(DatabaseContract.Frames.SQL_DELETE_FRAMES_TABLE);
        onCreate(db);
    }
}