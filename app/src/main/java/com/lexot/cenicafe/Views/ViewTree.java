package com.lexot.cenicafe.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.lexot.cenicafe.FrameListActivity;
import com.lexot.cenicafe.ListActivity;
import com.lexot.cenicafe.Listeners.TreeListener;
import com.lexot.cenicafe.MapsActivity;
import com.lexot.cenicafe.Models.CoffeeTree;
import com.lexot.cenicafe.R;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

public class ViewTree  extends LinearLayout implements View.OnClickListener {

    private TextView txtTreeId;
    private TextView txtLat;
    private TextView txtLng;
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
        this.txtLat = findViewById(R.id.txtLat);
        this.txtBranchesNoUsed = findViewById(R.id.txtBranchesNoUsed);
        this.txtLng = findViewById(R.id.txtLng);
        this.setOnClickListener(this);
    }
    public void bind(CoffeeTree item)
    {
        this.tree = item;
        txtTreeId.setText(item.Index.toString());
        txtLat.setText(df2.format(item.Lat));
        txtLng.setText(df2.format(item.Lng));
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
