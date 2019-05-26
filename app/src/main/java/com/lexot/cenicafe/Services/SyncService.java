package com.lexot.cenicafe.Services;

import android.Manifest;
import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lexot.cenicafe.ContentProvider.BranchContract;
import com.lexot.cenicafe.ContentProvider.FrameContract;
import com.lexot.cenicafe.Data.ApiResponses.DefaultResponse;
import com.lexot.cenicafe.Data.Repositories.CoffeeRepository;
import com.lexot.cenicafe.Models.BLL;
import com.lexot.cenicafe.Models.CoffeeBatch;
import com.lexot.cenicafe.Models.CoffeeBranch;
import com.lexot.cenicafe.Models.CoffeeFrame;
import com.lexot.cenicafe.Models.CoffeeTree;
import com.lexot.cenicafe.Utils.Utilities;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SyncService extends IntentService {
    private BLL bll;
    private CoffeeRepository repo;
    private String deviceId;
    private ArrayList<CoffeeBatch> batches;
    private ArrayList<CoffeeTree> trees;
    private ArrayList<CoffeeBranch> branches;
    private ArrayList<CoffeeFrame> frames;
    public SyncService(String name) {
        super(name);
    }

    public SyncService() {
        super("SyncService");
    }

    void nextSync() {
        if(Utilities.isConnected(this)) {
            if (batches.size() > 0) {
                syncBatch(batches.get(0));
            } else {
                if (trees == null) {
                    trees = bll.getNoSyncedTrees();
                }
                if (trees.size() > 0) {
                    syncTree(trees.get(0));
                } else {
                    if (branches == null) {
                        branches = bll.getNoSyncedBranches();
                    }
                    if (branches.size() > 0) {
                        syncBranch(branches.get(0));
                    } else {
                        if (frames == null) {
                            frames = bll.getNoSyncedFrames();
                        }
                        if (frames.size() > 0) {
                            syncFrame(frames.get(0));
                        } else {
                            Log.v("LXT", "Todo sincronizado");
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent workIntent) {

        android.os.Debug.waitForDebugger();
        bll = new BLL(this);
        repo = new CoffeeRepository(this);
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getSystemService(Context.
                TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        deviceId = telephonyManager.getSubscriberId();
        batches = bll.getBatches(true);
        Call<DefaultResponse> call = repo.CreateDevice(deviceId);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Response<DefaultResponse> response, Retrofit retrofit) {
                nextSync();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

/*
        try {
            Long idBranch = ContentUris.parseId(workIntent.getData());
            Cursor c = mContentResolver.query(Uri.withAppendedPath(BranchContract.BRANCH_URI, idBranch.toString()), null, null, null, null);
            c.moveToFirst();
            //syncBranch(c);
            c.close();
        }
        catch (Exception e) {
            //Sincronizar rama por rama
            Cursor c = mContentResolver.query(BranchContract.BRANCH_URI, null,BranchContract.BranchColumns.SYNCED+"=0",null, null);
            while (c.moveToNext()) {
                syncBranch(c);
            }
            c.close();
            //Sincronizar Frames que hayan quedado sin sincronizar
            syncFrames(0,0);
        }*/
    }

    public void syncBatch(final CoffeeBatch coffeeBatch) {
        Call<DefaultResponse> call = repo.PostCoffeeBatch(coffeeBatch, deviceId);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Response<DefaultResponse> response, Retrofit retrofit) {
                if (response != null && response.isSuccess()) {
                    if (response.body().id != null) {
                        bll.updateSyncBatch(coffeeBatch.Id, response.body().id);
                        Intent i = new Intent("SYNC_FINISHED_BATCH_" + coffeeBatch.Id.toString());
                        sendBroadcast(i);
                        batches.remove(0);
                        nextSync();
                    }
                } else {
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void syncTree(final CoffeeTree coffeeTree) {
        Call<DefaultResponse> call = repo.PostCoffeeTree(coffeeTree, coffeeTree.BatchBackendId);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Response<DefaultResponse> response, Retrofit retrofit) {
                if (response != null && response.isSuccess()) {
                    if (response.body().id != null) {
                        bll.updateSyncTree(coffeeTree.Id, response.body().id);
                        Intent i = new Intent("SYNC_FINISHED_TREE_" + coffeeTree.Id.toString());
                        sendBroadcast(i);
                        trees.remove(0);
                        nextSync();
                    }
                } else {
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void syncBranch(final CoffeeBranch coffeeBranch) {
        Call<DefaultResponse> call = repo.PostCoffeeBranch(coffeeBranch,coffeeBranch.TreeBackendId);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Response<DefaultResponse> response, Retrofit retrofit) {
                if (response != null && response.isSuccess()) {
                    if (response.body().id != null) {
                        bll.updateSyncBranch(coffeeBranch.Id, response.body().id);
                        Intent i = new Intent("SYNC_FINISHED_BRANCH_" + coffeeBranch.Id.toString());
                        sendBroadcast(i);
                        branches.remove(0);
                        nextSync();
                    }
                } else {
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void syncFrame(final CoffeeFrame coffeeFrame) {
        String path = coffeeFrame.Data;
        //Logica para mandar el archivo
        File imgFile = new  File(path);
        byte[] bytes = new byte[0];
        if(imgFile.exists()){
            int size = (int) imgFile.length();
            bytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(imgFile));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        RequestBody file = RequestBody.create(MediaType.parse("multipart/form-data"),bytes);
        Call<DefaultResponse> call = repo.PostCoffeeFrame(coffeeFrame,file,coffeeFrame.BranchBackendId);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Response<DefaultResponse> response, Retrofit retrofit) {
                if (response != null && response.isSuccess()) {
                    if (response.body().id != null) {
                        bll.updateSyncFrame(coffeeFrame.Id, response.body().id);
                        Intent i = new Intent("SYNC_FINISHED_FRAME_" + coffeeFrame.Id.toString());
                        sendBroadcast(i);
                        frames.remove(0);
                        nextSync();
                    }
                } else {
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    public void syncFrames(Integer idBranch, Integer realIdBranch) {
        /*if(idBranch > 0)
        {
            Cursor c = mContentResolver.query(FrameContract.FRAME_URI, null,FrameContract.FrameColumns.BRANCHID+"="+idBranch.toString()+" AND "+FrameContract.FrameColumns.SYNCED+"=0",null, null);
            while (c.moveToNext()) {
                Log.d("DEBUG -- ", "void syncFrames(Integer idBranch, Integer realIdBranch) WHILE");

                Call<DefaultResponse> call = repo.PostCoffeeFrame(realIdBranch,factor,time, file);
                call.enqueue(new Callback<DefaultResponse>() {

                    // Retorno del servicio
                    // Sincronizando la base de datos.

                    //Viewing
                    @Override
                    public void onResponse(Response<DefaultResponse> response, Retrofit retrofit) {
                        Log.d("DEBUG -- ", " public void onResponse(Response<DefaultResponse> response, Retrofit retrofit)");
                        if (response != null && response.isSuccess()) {
                            if (response.body().id != null) {
                                ContentValues mNewValues = new ContentValues();
                                mNewValues.put(FrameContract.FrameColumns.SYNCED, "1");
                                int changes = mContentResolver.update(Uri.withAppendedPath(FrameContract.FRAME_URI, id.toString()), mNewValues, null, null);
                                changes = changes + 1;
                                Intent i = new Intent("SYNC_FINISHED_FRAME_"+id.toString());
                                sendBroadcast(i);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });


            }
            c.close();
        }
        else
        {
            Cursor c = mContentResolver.query(FrameContract.FRAME_URI, null,FrameContract.FrameColumns.SYNCED+"=0",null, null);
            while (c.moveToNext()) {
                Integer branchId = c.getInt(c.getColumnIndexOrThrow(FrameContract.FrameColumns.BRANCHID));
                Cursor c2 = mContentResolver.query(BranchContract.BRANCH_URI, null,BranchContract.BranchColumns._ID+"="+branchId.toString(),null, null);
                c2.moveToFirst();
                realIdBranch = c2.getInt(c2.getColumnIndexOrThrow(BranchContract.BranchColumns.REAL_ID));
                c2.close();
                if(realIdBranch > 0) {
                    byte[] frame = c.getBlob(c.getColumnIndexOrThrow(FrameContract.FrameColumns.DATA));
                    Integer time = c.getInt(c.getColumnIndexOrThrow(FrameContract.FrameColumns.TIME));
                    Integer factor = c.getInt(c.getColumnIndexOrThrow(FrameContract.FrameColumns.FACTOR));
                    final Integer id = c.getInt(c.getColumnIndexOrThrow("_id"));
                    Log.i("LXT", "LXT");
                    repo = new CoffeeRepository(this);
                    TelephonyManager mngr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                    RequestBody file = RequestBody.create(MediaType.parse("multipart/form-data"), frame);
                    Call<DefaultResponse> call = repo.PostCoffeeFrame(realIdBranch, factor, time, file);
                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Response<DefaultResponse> response, Retrofit retrofit) {
                            if (response != null && response.isSuccess()) {
                                if (response.body().id != null) {
                                    ContentValues mNewValues = new ContentValues();
                                    mNewValues.put(FrameContract.FrameColumns.SYNCED, "1");
                                    int changes = mContentResolver.update(Uri.withAppendedPath(FrameContract.FRAME_URI, id.toString()), mNewValues, null, null);
                                    changes = changes + 1;
                                    Intent i = new Intent("SYNC_FINISHED_FRAME_" + id.toString());
                                    sendBroadcast(i);
                                }
                            }

                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                }

            }
            c.close();
        }*/
    }

}