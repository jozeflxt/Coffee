package com.lexot.cenicafe.Views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.lexot.cenicafe.BatchActivity;
import com.lexot.cenicafe.BatchMapActivity;
import com.lexot.cenicafe.Models.CoffeeBatch;
import com.lexot.cenicafe.R;
import com.lexot.cenicafe.TreeActivity;
import com.rilixtech.materialfancybutton.MaterialFancyButton;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class ViewBatch  extends LinearLayout implements View.OnClickListener {

    private final Context context;

    TextView txtName;
    TextView txtAge;
    TextView txtBranches;
    TextView txtStems;
    TextView txtTrees;
    TextView txtTotalTrees;
    View moreDetailsView;
    Integer idBatch;
    Integer synced;
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
        this.txtTotalTrees = findViewById(R.id.txtTotalTrees);
        this.moreDetailsView = findViewById(R.id.moreDetailsView);
        this.setOnClickListener(this);
        MaterialFancyButton btnAddBatch = findViewById(R.id.btnDetail);
        btnAddBatch.setOnClickListener(this);
        PushDownAnim.setPushDownAnimTo(btnAddBatch);
        MaterialFancyButton btnMap = findViewById(R.id.btnMap);
        btnMap.setOnClickListener(this);
        PushDownAnim.setPushDownAnimTo(btnMap);
    }

    public void bind(CoffeeBatch item)
    {
        idBatch = item.Id;
        synced = item.Synced;
        txtName.setText(item.Name);
        txtAge.setText(item.Age.toString());
        txtBranches.setText(item.BranchesAmmount.toString());
        txtStems.setText(item.Stems.toString());
        txtTrees.setText(item.Trees.toString());
        txtTotalTrees.setText(item.TotalTrees.toString());
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
            case R.id.btnMap:
                startDraw(synced == 0);
                break;
            default:
                if (synced == 0) {
                   new AwesomeInfoDialog(getContext())
                            .setTitle("Geolocalización")
                            .setMessage("¿Cómo deseas tomar la información?")
                            .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                            .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                            .setCancelable(true)
                            .setPositiveButtonText("Dibujar en mapa")
                            .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                            .setPositiveButtonTextColor(R.color.white)
                            .setNegativeButtonText("Usar GPS")
                            .setNegativeButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                            .setNegativeButtonTextColor(R.color.white)
                            .setPositiveButtonClick(new Closure() {
                                @Override
                                public void exec() {
                                    startDraw(true);
                                }
                            })
                            .setNegativeButtonClick(new Closure() {
                                @Override
                                public void exec() {
                                    //startGPS();
                                }
                            })
                            .show();
                   break;
                } else {
                    Intent intent = new Intent(getContext(), TreeActivity.class);
                    intent.putExtra(TreeActivity.BATCH_ID_PARAM, idBatch);
                    getContext().startActivity(intent);
                }
        }
    }

    public void startDraw(Boolean isCreating) {
        Intent intent = new Intent(getContext(), BatchMapActivity.class);
        intent.putExtra(BatchMapActivity.BATCH_ID_PARAM, idBatch);
        intent.putExtra(BatchMapActivity.CREATING_PARAM, isCreating);
        ((BatchActivity)getContext()).startActivityForResult(intent, 0);
    }


}
