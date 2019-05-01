package com.lexot.cenicafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lexot.cenicafe.Adapters.BranchAdapter;
import com.lexot.cenicafe.Listeners.BranchListener;
import com.lexot.cenicafe.Models.BLL;
import com.lexot.cenicafe.Models.CoffeeBatch;
import com.lexot.cenicafe.Models.CoffeeBranch;
import com.rilixtech.materialfancybutton.MaterialFancyButton;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;

public class ListActivity extends AppCompatActivity implements BranchListener {


    public static String TREE_ID_PARAM = "treeIdParam";
    public static String STEM_ID_PARAM = "stemIdParam";
    public static Integer NEW_FRAME_REQUEST_CODE = 125;

    private Integer treeId;
    private Integer stemId;
    BranchAdapter adapter;
    RecyclerView list;
    private List<CoffeeBranch> listBranches;
    private BLL bll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        bll = new BLL(this);
        treeId = getIntent().getIntExtra(TREE_ID_PARAM,0);
        stemId = getIntent().getIntExtra(STEM_ID_PARAM,0);
        adapter = new BranchAdapter(this);
        list = findViewById(R.id.list);
        listBranches = bll.getBranches(treeId, stemId);
        adapter.setContext(this);
        adapter.setList(listBranches);
        list.setAdapter(adapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(layoutManager);
    }
    @Override
    public void captureFrames(Integer type, Integer branchId) {
        Intent i = new Intent(this,MainActivity.class);
        bll.updateBranch(branchId, type);
        Bundle b = new Bundle();
        b.putInt(MainActivity.BRANCH_ID_PARAM, branchId);
        i.putExtras(b);
        startActivityForResult(i, NEW_FRAME_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_FRAME_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                listBranches = bll.getBranches(treeId, stemId);
                adapter.setList(listBranches);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
