package com.lexot.cenicafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.lexot.cenicafe.Adapters.TreeAdapter;
import com.lexot.cenicafe.Listeners.TreeListener;
import com.lexot.cenicafe.Models.BLL;
import com.lexot.cenicafe.Models.CoffeeBatch;
import com.lexot.cenicafe.Models.CoffeeTree;

import java.util.ArrayList;
import java.util.List;

public class TreeActivity extends AppCompatActivity implements TreeListener {


    public static String BATCH_ID_PARAM = "batchIdParam";

    private Integer batchId;
    TreeAdapter adapter;
    RecyclerView list;
    private List<CoffeeTree> listTreees = new ArrayList<>();
    private BLL bll;
    private CoffeeBatch batch;
    private Integer treeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);
        bll = new BLL(this);
        batchId = getIntent().getIntExtra(BATCH_ID_PARAM,0);
        batch = bll.getBatch(batchId);
        adapter = new TreeAdapter(this);
        list = findViewById(R.id.list);
        adapter.setContext(this);
        adapter.setList(listTreees);
        list.setAdapter(adapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listTreees = bll.getTrees(batchId);
        adapter.setList(listTreees);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clickDetail(final CoffeeTree tree) {
        if (batch.Stems > 1) {
            new AwesomeInfoDialog(this)
                    .setTitle("¿Cuál tallo deseas elegir?")
                    .setMessage("")
                    .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                    .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                    .setCancelable(true)
                    .setPositiveButtonText("Tallo 1")
                    .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                    .setPositiveButtonTextColor(R.color.white)
                    .setNegativeButtonText("Tallo 2")
                    .setNegativeButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                    .setNegativeButtonTextColor(R.color.white)
                    .setPositiveButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            goToBranchList(1, tree.Id);
                        }
                    })
                    .setNegativeButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            goToBranchList(2, tree.Id);
                        }
                    })
                    .show();
        } else {
            goToBranchList(1, tree.Id);
        }
    }

    void goToBranchList(Integer stemId, Integer treeId) {
        Intent i = new Intent(this, ListActivity.class);
        Bundle b = new Bundle();
        b.putInt(ListActivity.TREE_ID_PARAM, treeId);
        b.putInt(ListActivity.STEM_ID_PARAM, stemId);
        i.putExtras(b);
        startActivity(i);
    }


}
