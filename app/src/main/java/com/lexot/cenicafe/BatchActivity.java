package com.lexot.cenicafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lexot.cenicafe.Adapters.BatchAdapter;
import com.lexot.cenicafe.Models.BLL;
import com.lexot.cenicafe.Models.CoffeeBatch;
import com.lexot.cenicafe.Utils.Helpers;
import com.rilixtech.materialfancybutton.MaterialFancyButton;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class BatchActivity extends AppCompatActivity implements View.OnClickListener {

    private BatchAdapter adapter;
    private RecyclerView list;
    private ArrayList<CoffeeBatch> listBatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch);
        adapter = new BatchAdapter();
        list = findViewById(R.id.list);
        updateList();
        adapter.setContext(this);
        list.setAdapter(adapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(layoutManager);
        MaterialFancyButton btnAddBatch = findViewById(R.id.btnAddBatch);
        btnAddBatch.setOnClickListener(this);
        PushDownAnim.setPushDownAnimTo(btnAddBatch);
    }

    public void updateList() {
        Helpers.showLoading(this, "Cargando", "Cargando Lotes");
        listBatches = new BLL(this).getBatches(false);
        adapter.setList(listBatches);
        Helpers.hideLoading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            updateList();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        Intent in1 = new Intent(this,NewBatchActivity.class);
        this.startActivityForResult(in1, 1);
    }
}
