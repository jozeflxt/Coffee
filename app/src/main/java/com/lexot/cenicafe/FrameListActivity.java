package com.lexot.cenicafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lexot.cenicafe.Adapters.FrameAdapter;
import com.lexot.cenicafe.ContentProvider.FrameContract;
import com.lexot.cenicafe.Models.BLL;
import com.lexot.cenicafe.Models.CoffeeFrame;

import java.util.ArrayList;
import java.util.List;

public class FrameListActivity extends AppCompatActivity {


    public static String BRANCH_ID_PARAM = "branchIdParam";

    FrameAdapter adapter;
    public Integer branchID = 0;
    RecyclerView list;
    private List<CoffeeFrame> listFrames;
    private FrameListActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_list);
        adapter = new FrameAdapter();
        list = findViewById(R.id.list);
        Bundle b = getIntent().getExtras();
        if(b != null)
            branchID = b.getInt(BRANCH_ID_PARAM);
        listFrames = new BLL(this).getFrames(branchID);
        adapter.setContext(context);
        adapter.setList(listFrames);
        list.setAdapter(adapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(layoutManager);
    }
}
