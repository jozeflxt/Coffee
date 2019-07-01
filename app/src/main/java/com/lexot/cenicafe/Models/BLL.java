package com.lexot.cenicafe.Models;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.lexot.cenicafe.ContentProvider.BatchContract;
import com.lexot.cenicafe.ContentProvider.BranchContract;
import com.lexot.cenicafe.ContentProvider.CoordinateContract;
import com.lexot.cenicafe.ContentProvider.FrameContract;
import com.lexot.cenicafe.ContentProvider.TreeContract;
import com.lexot.cenicafe.DataBase.CoffeeDbHelper;
import com.lexot.cenicafe.DataBase.DatabaseContract;
import com.lexot.cenicafe.Listeners.TreeListener;
import com.lexot.cenicafe.Services.FactorService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BLL {
    private ContentResolver mContentResolver;
    private Context context;

    public BLL(Context context) {
        this.mContentResolver = context.getContentResolver();
        this.context = context;
    }

    public ArrayList<CoffeeBatch> getBatches(Boolean onlyToSynced) {
        ArrayList<CoffeeBatch> batches = new ArrayList<CoffeeBatch>();
        Cursor cursor = mContentResolver.query(BatchContract.BATCH_URI, null, null, null, null);
        if (onlyToSynced) {
            cursor = mContentResolver.query(BatchContract.BATCH_URI, null, BatchContract.BatchColumns.SYNCED + "=1", null, null);
        }
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            CoffeeBatch coffeeBatch = new CoffeeBatch();
            coffeeBatch.Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            coffeeBatch.Name = cursor.getString(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.NAME));
            coffeeBatch.Age = cursor.getInt(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.AGE));
            coffeeBatch.BranchesAmmount = cursor.getInt(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.BRANCHES));
            coffeeBatch.Stems = cursor.getInt(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.STEMS));
            coffeeBatch.Trees = cursor.getInt(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.TREES));
            coffeeBatch.TotalTrees = cursor.getInt(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.TOTAL_TREES));
            coffeeBatch.Synced = cursor.getInt(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.SYNCED));
            coffeeBatch.BackendId = cursor.getInt(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.REAL_ID));
            batches.add(coffeeBatch);
            cursor.moveToNext();
        }
        cursor.close();
        return batches;
    }

    public CoffeeBatch getBatch(Integer batchId) {
        CoffeeBatch coffeeBatch = new CoffeeBatch();
        Cursor cursor = mContentResolver.query(BatchContract.BATCH_URI, null, BranchContract.BranchColumns._ID + "="+ batchId.toString(), null, null);
        if (cursor.moveToFirst()) {
            coffeeBatch.Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            coffeeBatch.Name = cursor.getString(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.NAME));
            coffeeBatch.Age = cursor.getInt(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.AGE));
            coffeeBatch.BranchesAmmount = cursor.getInt(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.BRANCHES));
            coffeeBatch.Stems = cursor.getInt(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.STEMS));
            coffeeBatch.Trees = cursor.getInt(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.TREES));
            coffeeBatch.Synced = cursor.getInt(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.SYNCED));
            coffeeBatch.BackendId = cursor.getInt(cursor.getColumnIndexOrThrow(BatchContract.BatchColumns.REAL_ID));
        }
        cursor.close();
        return coffeeBatch;
    }

    public ArrayList<CoffeeTree> getTrees(Integer batchId) {
        ArrayList<CoffeeTree> trees = new ArrayList<CoffeeTree>();
        String whereClause = " WHERE t." + DatabaseContract.Trees.COLUMN_NAME_TREE_BATCHID + "=" + batchId.toString();
        SQLiteDatabase db = CoffeeDbHelper.getInstance(context).getReadableDatabase();
        String query = "SELECT t.*, COUNT(b2."+DatabaseContract.Branches._ID+") as BranchesNoUsedCount " +
                " FROM " + DatabaseContract.Trees.TABLE_NAME + " AS t " +
                " LEFT JOIN " + DatabaseContract.Branches.TABLE_NAME + " AS b2 ON b2."+DatabaseContract.Branches.COLUMN_NAME_BRANCH_TREEID+"=t." + DatabaseContract.Trees._ID +
                " AND b2." + DatabaseContract.Branches.COLUMN_NAME_SYNCED + "=0" +
                whereClause +
                " GROUP BY t." + DatabaseContract.Trees._ID +
                " ORDER BY t." + DatabaseContract.Trees._ID + " ASC";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            CoffeeTree coffeeTree = new CoffeeTree();
            coffeeTree.Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            coffeeTree.Synced = cursor.getInt(cursor.getColumnIndexOrThrow(TreeContract.TreeColumns.SYNCED)) == 2;
            coffeeTree.BackendId = cursor.getInt(cursor.getColumnIndexOrThrow(TreeContract.TreeColumns.REAL_ID));
            coffeeTree.BatchId = cursor.getInt(cursor.getColumnIndexOrThrow(TreeContract.TreeColumns.BATCH_ID));
            coffeeTree.Index = cursor.getInt(cursor.getColumnIndexOrThrow(TreeContract.TreeColumns.INDEX));
            coffeeTree.NoUsedBranchesCount = cursor.getInt(cursor.getColumnIndexOrThrow("BranchesNoUsedCount"));
            trees.add(coffeeTree);
            cursor.moveToNext();
        }
        cursor.close();
        return trees;
    }

    public ArrayList<CoffeeTree> getNoSyncedTrees() {
        ArrayList<CoffeeTree> trees = new ArrayList<CoffeeTree>();
        SQLiteDatabase db = CoffeeDbHelper.getInstance(context).getReadableDatabase();
        String query = "SELECT t.*, b." + DatabaseContract.Batches.COLUMN_NAME_BATCH_REAL_ID  +  " as BatchBackendId " +
                " FROM " + DatabaseContract.Trees.TABLE_NAME + " AS t " +
                " JOIN " + DatabaseContract.Batches.TABLE_NAME + " AS b ON t."+DatabaseContract.Trees.COLUMN_NAME_TREE_BATCHID+"=b." + DatabaseContract.Batches._ID +
                " WHERE t." + DatabaseContract.Trees.COLUMN_NAME_SYNCED + "=1" +
                " AND b." + DatabaseContract.Batches.COLUMN_NAME_BATCH_REAL_ID + ">0" +
                " GROUP BY t." + DatabaseContract.Trees._ID +
                " ORDER BY t." + DatabaseContract.Trees._ID + " ASC";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            CoffeeTree coffeeTree = new CoffeeTree();
            coffeeTree.Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            coffeeTree.Synced = cursor.getInt(cursor.getColumnIndexOrThrow(TreeContract.TreeColumns.SYNCED)) == 2;
            coffeeTree.BackendId = cursor.getInt(cursor.getColumnIndexOrThrow(TreeContract.TreeColumns.REAL_ID));
            coffeeTree.BatchId = cursor.getInt(cursor.getColumnIndexOrThrow(TreeContract.TreeColumns.BATCH_ID));
            coffeeTree.Index = cursor.getInt(cursor.getColumnIndexOrThrow(TreeContract.TreeColumns.INDEX));
            coffeeTree.BatchBackendId = cursor.getInt(cursor.getColumnIndexOrThrow("BatchBackendId"));
            trees.add(coffeeTree);
            cursor.moveToNext();
        }
        cursor.close();
        return trees;
    }

    public ArrayList<CoffeeBranch> getBranches(Integer treeId, Integer stemId) {
        ArrayList<CoffeeBranch> branches = new ArrayList<CoffeeBranch>();
        SQLiteDatabase db = CoffeeDbHelper.getInstance(context).getReadableDatabase();
        String query = "SELECT b.*, COUNT(f."+DatabaseContract.Frames._ID+") as FramesCount " +
                " FROM " + DatabaseContract.Branches.TABLE_NAME + " AS b " +
                " LEFT JOIN " + DatabaseContract.Frames.TABLE_NAME + " AS f ON b."+DatabaseContract.Branches._ID+"=f." + DatabaseContract.Frames.COLUMN_NAME_FRAME_BRANCHID +
                " WHERE b." + DatabaseContract.Branches.COLUMN_NAME_BRANCH_TREEID + "=" + treeId.toString() +
                " AND b." + DatabaseContract.Branches.COLUMN_NAME_BRANCH_STEMID + "=" + stemId.toString() +
                " GROUP BY b." + DatabaseContract.Branches._ID +
                " ORDER BY b." + DatabaseContract.Branches._ID + " ASC";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            CoffeeBranch coffeeBranch = new CoffeeBranch();
            coffeeBranch.Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            coffeeBranch.Index = cursor.getInt(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.INDEX));
            coffeeBranch.Synced = cursor.getInt(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.SYNCED));
            coffeeBranch.BackendId = cursor.getInt(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.REAL_ID));
            coffeeBranch.TreeId = cursor.getInt(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.TREE_ID));
            coffeeBranch.StemId = cursor.getInt(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.STEM_ID));
            coffeeBranch.Date = cursor.getString(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.DATE));
            coffeeBranch.FramesCount = cursor.getInt(cursor.getColumnIndexOrThrow("FramesCount"));
            branches.add(coffeeBranch);
            cursor.moveToNext();
        }
        cursor.close();
        return branches;
    }

    public ArrayList<CoffeeBranch> getNoSyncedBranches() {
        ArrayList<CoffeeBranch> branches = new ArrayList<CoffeeBranch>();
        SQLiteDatabase db = CoffeeDbHelper.getInstance(context).getReadableDatabase();
        String query = "SELECT b.*, t." + DatabaseContract.Trees.COLUMN_NAME_TREE_REALID  +  " as TreeBackendId " +
                " FROM " + DatabaseContract.Branches.TABLE_NAME + " AS b " +
                " JOIN " + DatabaseContract.Trees.TABLE_NAME + " AS t ON b."+DatabaseContract.Branches.COLUMN_NAME_BRANCH_TREEID+"=t." + DatabaseContract.Trees._ID +
                " WHERE b." + DatabaseContract.Branches.COLUMN_NAME_SYNCED + "=1" +
                " AND t." + DatabaseContract.Trees.COLUMN_NAME_TREE_REALID + ">0" +
                " GROUP BY b." + DatabaseContract.Branches._ID +
                " ORDER BY b." + DatabaseContract.Branches._ID + " ASC";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            CoffeeBranch coffeeBranch = new CoffeeBranch();
            coffeeBranch.Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            coffeeBranch.Index = cursor.getInt(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.INDEX));
            coffeeBranch.Synced = cursor.getInt(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.SYNCED));
            coffeeBranch.BackendId = cursor.getInt(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.REAL_ID));
            coffeeBranch.TreeId = cursor.getInt(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.TREE_ID));
            coffeeBranch.StemId = cursor.getInt(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.STEM_ID));
            coffeeBranch.Date = cursor.getString(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.DATE));
            coffeeBranch.TreeBackendId = cursor.getInt(cursor.getColumnIndexOrThrow("TreeBackendId"));
            branches.add(coffeeBranch);
            cursor.moveToNext();
        }
        cursor.close();
        return branches;
    }

    public CoffeeBranch getBranch(Integer branchId) {
        Cursor cursor = mContentResolver.query(Uri.withAppendedPath(BranchContract.BRANCH_URI,branchId.toString()), null, null, null, null);
        CoffeeBranch coffeeBranch = new CoffeeBranch();
        if (cursor.moveToFirst()) {
            coffeeBranch.Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            coffeeBranch.Synced = cursor.getInt(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.SYNCED));
            coffeeBranch.StemId = cursor.getInt(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.STEM_ID));
            coffeeBranch.TreeId = cursor.getInt(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.TREE_ID));
            coffeeBranch.BackendId = cursor.getInt(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.REAL_ID));
            coffeeBranch.Date = cursor.getString(cursor.getColumnIndexOrThrow(BranchContract.BranchColumns.DATE));

            //coffeeBranch.Path =
        }
        return coffeeBranch;
    }

    public ArrayList<CoffeeFrame> getFrames(Integer branchId) {
        ArrayList<CoffeeFrame> frames = new ArrayList<CoffeeFrame>();
        Cursor cursor = mContentResolver.query(TreeContract.TREE_URI, null, FrameContract.FrameColumns.BRANCH_ID + "="+branchId.toString(), null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            CoffeeFrame coffeeFrame = new CoffeeFrame();
            coffeeFrame.Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            coffeeFrame.Synced = cursor.getInt(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.SYNCED)) == 2;
            coffeeFrame.BranchId = cursor.getInt(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.BRANCH_ID));
            coffeeFrame.Factor = cursor.getDouble(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.FACTOR));
            coffeeFrame.Time = cursor.getInt(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.TIME));
            coffeeFrame.Data = cursor.getString(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.DATA));
            frames.add(coffeeFrame);
            cursor.moveToNext();
        }
        cursor.close();
        return frames;
    }

    public ArrayList<CoffeeFrame> getNoSyncedFrames() {
        ArrayList<CoffeeFrame> frames = new ArrayList<CoffeeFrame>();
        SQLiteDatabase db = CoffeeDbHelper.getInstance(context).getReadableDatabase();
        String query = "SELECT f.*, b." + DatabaseContract.Branches.COLUMN_NAME_BRANCH_REALID  +  " as BranchBackendId " +
                " FROM " + DatabaseContract.Frames.TABLE_NAME + " AS f " +
                " JOIN " + DatabaseContract.Branches.TABLE_NAME + " AS b ON f."+DatabaseContract.Frames.COLUMN_NAME_FRAME_BRANCHID+"=b." + DatabaseContract.Branches._ID +
                " WHERE f." + DatabaseContract.Frames.COLUMN_NAME_SYNCED + "=1" +
                " AND b." + DatabaseContract.Branches.COLUMN_NAME_BRANCH_REALID + ">0" +
                " GROUP BY f." + DatabaseContract.Frames._ID +
                " ORDER BY f." + DatabaseContract.Frames._ID + " ASC";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {

            CoffeeFrame coffeeFrame = new CoffeeFrame();
            coffeeFrame.Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            coffeeFrame.Synced = cursor.getInt(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.SYNCED)) == 2;
            coffeeFrame.BranchId = cursor.getInt(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.BRANCH_ID));
            coffeeFrame.Factor = cursor.getDouble(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.FACTOR));
            coffeeFrame.Time = cursor.getInt(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.TIME));
            coffeeFrame.Data = cursor.getString(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.DATA));
            coffeeFrame.BranchBackendId = cursor.getInt(cursor.getColumnIndexOrThrow("BranchBackendId"));
            frames.add(coffeeFrame);
            cursor.moveToNext();
        }
        cursor.close();
        return frames;
    }

    public CoffeeFrame getFrame(Integer frameId) {
        Cursor cursor = mContentResolver.query(Uri.withAppendedPath(FrameContract.FRAME_URI,frameId.toString()), null, null, null, null);
        CoffeeFrame coffeeFrame = new CoffeeFrame();
        if (cursor.moveToFirst()) {
            coffeeFrame.Id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            coffeeFrame.Synced = cursor.getInt(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.SYNCED)) == 2;
            coffeeFrame.BranchId = cursor.getInt(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.BRANCH_ID));
            coffeeFrame.Factor = cursor.getDouble(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.FACTOR));
            coffeeFrame.Time = cursor.getInt(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.TIME));
            coffeeFrame.Data = cursor.getString(cursor.getColumnIndexOrThrow(FrameContract.FrameColumns.DATA));
        }
        return coffeeFrame;
    }

    public void updateBranch(Integer brachId) {
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(BranchContract.BranchColumns.SYNCED,1);
        mContentResolver.update(Uri.withAppendedPath(BranchContract.BRANCH_URI,brachId.toString()), mNewValues,null,null);
    }

    public void updateFrameFactor(Integer frameId, double factor) {
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(FrameContract.FrameColumns.FACTOR,factor);
        mNewValues.put(FrameContract.FrameColumns.SYNCED,1);
        mContentResolver.update(Uri.withAppendedPath(FrameContract.FRAME_URI,frameId.toString()), mNewValues,null,null);
    }

    public int createBatch(CoffeeBatch coffeeBatch) {
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(BatchContract.BatchColumns.AGE, coffeeBatch.Age);
        mNewValues.put(BatchContract.BatchColumns.BRANCHES, coffeeBatch.BranchesAmmount);
        mNewValues.put(BatchContract.BatchColumns.NAME, coffeeBatch.Name);
        mNewValues.put(BatchContract.BatchColumns.STEMS, coffeeBatch.Stems);
        mNewValues.put(BatchContract.BatchColumns.TREES, coffeeBatch.Trees);
        mNewValues.put(BatchContract.BatchColumns.TOTAL_TREES, coffeeBatch.TotalTrees);
        mNewValues.put(BatchContract.BatchColumns.SYNCED, 0);
        Uri uriInsertBatch = mContentResolver.insert(BatchContract.BATCH_URI, mNewValues);
        Long batchId = ContentUris.parseId(uriInsertBatch);
        if (batchId > 0) {
            for (int i=1; i<=coffeeBatch.Trees; i++) {
                ContentValues mNewTreeValues = new ContentValues();
                mNewTreeValues.put(TreeContract.TreeColumns.BATCH_ID, batchId);
                mNewTreeValues.put(TreeContract.TreeColumns.INDEX, i);
                mNewTreeValues.put(TreeContract.TreeColumns.SYNCED, 1);
                Long treeId = ContentUris.parseId(mContentResolver.insert(TreeContract.TREE_URI, mNewTreeValues));
                for (int j=1; j<=coffeeBatch.BranchesAmmount; j++) {
                    for (int k=1; k<=coffeeBatch.Stems; k++) {
                        ContentValues mNewBatchValues = new ContentValues();
                        mNewBatchValues.put(BranchContract.BranchColumns.TREE_ID, treeId);
                        mNewBatchValues.put(BranchContract.BranchColumns.INDEX, j);
                        mNewBatchValues.put(BranchContract.BranchColumns.DATE, (new SimpleDateFormat("ddMyyyy-HHmmss")).format(new Date()));
                        mNewBatchValues.put(BranchContract.BranchColumns.STEM_ID, k);
                        mNewBatchValues.put(BranchContract.BranchColumns.SYNCED, 0);
                        mContentResolver.insert(BranchContract.BRANCH_URI, mNewBatchValues);
                    }
                }
            }
            return batchId.intValue();
        } else {
            return 0;
        }
    }

    public int createCoordinates(List<LatLng> coordinates, Integer batchId) {
        for (int i=0; i<=coordinates.size() - 1; i++) {
            ContentValues mNewValues = new ContentValues();
            mNewValues.put(CoordinateContract.CoordinateColumns.LAT, coordinates.get(i).latitude);
            mNewValues.put(CoordinateContract.CoordinateColumns.LNG, coordinates.get(i).longitude);
            mNewValues.put(CoordinateContract.CoordinateColumns.INDEX, i);
            mNewValues.put(CoordinateContract.CoordinateColumns.BATCH_ID, batchId);
            Uri uriInsertFrame = mContentResolver.insert(CoordinateContract.COORDINATE_URI, mNewValues);
        }
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(BatchContract.BatchColumns.SYNCED,1);
        //Guardar en Base de datos
        return mContentResolver.update(Uri.withAppendedPath(BatchContract.BATCH_URI,batchId.toString()), mNewValues,null,null);
    }

    public ArrayList<CoffeeLatLng> getCoordinates(Integer batchId) {
        ArrayList<CoffeeLatLng> coordinates = new ArrayList<CoffeeLatLng>();
        Cursor cursor = mContentResolver.query(CoordinateContract.COORDINATE_URI, null, CoordinateContract.CoordinateColumns.BATCH_ID + "="+batchId.toString(), null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            CoffeeLatLng coordinate = new CoffeeLatLng();
            coordinate.BatchId = cursor.getInt(cursor.getColumnIndexOrThrow(CoordinateContract.CoordinateColumns.BATCH_ID));
            coordinate.Lat = cursor.getDouble(cursor.getColumnIndexOrThrow(CoordinateContract.CoordinateColumns.LAT));
            coordinate.Lng = cursor.getDouble(cursor.getColumnIndexOrThrow(CoordinateContract.CoordinateColumns.LNG));
            coordinate.Index = cursor.getInt(cursor.getColumnIndexOrThrow(CoordinateContract.CoordinateColumns.INDEX));
            coordinates.add(coordinate);
            cursor.moveToNext();
        }
        cursor.close();
        return coordinates;
    }

    public int createFrame(CoffeeFrame coffeeFrame) {
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(FrameContract.FrameColumns.FACTOR, coffeeFrame.Factor);
        mNewValues.put(FrameContract.FrameColumns.BRANCH_ID, coffeeFrame.BranchId);
        mNewValues.put(FrameContract.FrameColumns.DATA, coffeeFrame.Data);
        mNewValues.put(FrameContract.FrameColumns.TIME, coffeeFrame.Time);
        mNewValues.put(FrameContract.FrameColumns.SYNCED, 0);
        Uri uriInsertFrame = mContentResolver.insert(FrameContract.FRAME_URI, mNewValues);

        Intent factorServiceIntent = new Intent(context, FactorService.class);
        factorServiceIntent.setData(uriInsertFrame);
        context.startService(factorServiceIntent);

        return ((Long)ContentUris.parseId(uriInsertFrame)).intValue();
    }

    //UPDATE AFTER SYNC

    public int updateSyncBatch(Integer batchId, Integer backendId) {
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(BatchContract.BatchColumns.SYNCED,2);
        mNewValues.put(BatchContract.BatchColumns.REAL_ID,backendId);
        //Guardar en Base de datos
        return mContentResolver.update(Uri.withAppendedPath(BatchContract.BATCH_URI,batchId.toString()), mNewValues,null,null);
    }

    public int updateSyncTree(Integer treeId, Integer backendId) {
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(TreeContract.TreeColumns.SYNCED,2);
        mNewValues.put(TreeContract.TreeColumns.REAL_ID,backendId);
        //Guardar en Base de datos
        return mContentResolver.update(Uri.withAppendedPath(TreeContract.TREE_URI,treeId.toString()), mNewValues,null,null);
    }

    public int updateSyncBranch(Integer branchId, Integer backendId) {
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(BranchContract.BranchColumns.SYNCED,2);
        mNewValues.put(BranchContract.BranchColumns.REAL_ID,backendId);
        //Guardar en Base de datos
        return mContentResolver.update(Uri.withAppendedPath(BranchContract.BRANCH_URI,branchId.toString()), mNewValues,null,null);
    }

    public int updateSyncFrame(Integer frameId, Integer backendId) {
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(FrameContract.FrameColumns.SYNCED,2);
        //Guardar en Base de datos
        return mContentResolver.update(Uri.withAppendedPath(FrameContract.FRAME_URI,frameId.toString()), mNewValues,null,null);
    }
}
