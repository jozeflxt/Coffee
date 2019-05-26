package com.lexot.cenicafe.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lexot.cenicafe.Listeners.TreeListener;
import com.lexot.cenicafe.Models.CoffeeTree;
import com.lexot.cenicafe.R;

import java.text.DecimalFormat;

public class ViewTree  extends LinearLayout implements View.OnClickListener {

    private TextView txtTreeId;
    private TextView txtBranchesNoUsed;
    private TreeListener treeListener;
    private CoffeeTree tree;

    private static DecimalFormat df2 = new DecimalFormat(".##");

    public ViewTree(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ViewTree(Context context, TreeListener treeListener) {
        super(context);
        inflate(getContext(), R.layout.view_tree, this);
        this.treeListener = treeListener;
        this.txtTreeId = findViewById(R.id.txtTreeId);
        this.txtBranchesNoUsed = findViewById(R.id.txtBranchesNoUsed);
        this.setOnClickListener(this);
    }
    public void bind(CoffeeTree item)
    {
        this.tree = item;
        txtTreeId.setText(item.Index.toString());
        if (item.NoUsedBranchesCount > 0) {
            txtBranchesNoUsed.setText("Ramas sin usar: " + item.NoUsedBranchesCount.toString());
        } else {
            txtBranchesNoUsed.setText("Todas las ramas han sido usadas");
            txtBranchesNoUsed.setTextColor(getResources().getColor(R.color.darkBlue));
        }
    }


    @Override
    public void onClick(View view) {
        treeListener.clickDetail(tree);
    }
}
