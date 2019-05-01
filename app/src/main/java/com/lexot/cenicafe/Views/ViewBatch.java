package com.lexot.cenicafe.Views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lexot.cenicafe.ListActivity;
import com.lexot.cenicafe.Models.CoffeeBatch;
import com.lexot.cenicafe.Models.CoffeeBranch;
import com.lexot.cenicafe.R;
import com.lexot.cenicafe.TreeActivity;
import com.rilixtech.materialfancybutton.MaterialFancyButton;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;

public class ViewBatch  extends LinearLayout implements View.OnClickListener {

    private final Context context;

    TextView txtName;
    TextView txtAge;
    TextView txtBranches;
    TextView txtStems;
    TextView txtTrees;
    View moreDetailsView;
    Integer idBatch;
    public ViewBatch(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    public ViewBatch(Context context) {
        super(context);
        this.context = context;
        inflate(getContext(), R.layout.view_batch, this);
        this.txtName = findViewById(R.id.txtName);
        this.txtAge = findViewById(R.id.txtAge);
        this.txtBranches = findViewById(R.id.txtBranches);
        this.txtStems = findViewById(R.id.txtStems);
        this.txtTrees = findViewById(R.id.txtTrees);
        this.moreDetailsView = findViewById(R.id.moreDetailsView);
        this.setOnClickListener(this);
        MaterialFancyButton btnAddBatch = findViewById(R.id.btnDetail);
        btnAddBatch.setOnClickListener(this);
        PushDownAnim.setPushDownAnimTo(btnAddBatch);
    }

    public void bind(CoffeeBatch item)
    {
        idBatch = item.Id;
        txtName.setText(item.Name);
        txtAge.setText(item.Age.toString());
        txtBranches.setText(item.BranchesAmmount.toString());
        txtStems.setText(item.Stems.toString());
        txtTrees.setText(item.Trees.toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDetail:
                if(moreDetailsView.getVisibility() == GONE) {
                    moreDetailsView.setVisibility(VISIBLE);
                } else {
                    moreDetailsView.setVisibility(GONE);
                }
                break;
            default:
                Intent intent = new Intent(getContext(), TreeActivity.class);
                intent.putExtra(TreeActivity.BATCH_ID_PARAM, idBatch);
                getContext().startActivity(intent);
                break;
        }
    }
}
