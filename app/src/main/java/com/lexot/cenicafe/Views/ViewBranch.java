package com.lexot.cenicafe.Views;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.lexot.cenicafe.FrameListActivity;
import com.lexot.cenicafe.Listeners.BranchListener;
import com.lexot.cenicafe.MainActivity;
import com.lexot.cenicafe.Models.BLL;
import com.lexot.cenicafe.Models.CoffeeBranch;
import com.lexot.cenicafe.R;
import com.rilixtech.materialfancybutton.MaterialFancyButton;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class ViewBranch  extends LinearLayout implements View.OnClickListener {

    private final Context context;
    private BranchListener listener;
    TextView txtBranch;
    TextView txtProgress;
    TextView txtFrames;
    View syncContainer;
    private Integer Id;
    MaterialFancyButton btnRegisterPhoto;
    MaterialFancyButton btnClose;

    public ViewBranch(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    public ViewBranch(Context context, BranchListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        inflate(getContext(), R.layout.view_branch, this);
        this.txtBranch = findViewById(R.id.txtBranch);
        this.txtFrames = findViewById(R.id.txtFrames);
        this.syncContainer = findViewById(R.id.syncContainer);
        this.txtProgress = findViewById(R.id.txtProgress);
        this.setOnClickListener(this);
        btnRegisterPhoto = findViewById(R.id.btnRegisterPhoto);
        btnRegisterPhoto.setOnClickListener(this);
        PushDownAnim.setPushDownAnimTo(btnRegisterPhoto);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);
        PushDownAnim.setPushDownAnimTo(btnClose);
    }

    @Override
    protected void onDetachedFromWindow() {
        context.unregisterReceiver(syncFinishedReceiver);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        context.registerReceiver(syncFinishedReceiver, new IntentFilter("SYNC_FINISHED_BRANCH_"+Id.toString()));

    }
    private BroadcastReceiver syncFinishedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            txtProgress.setText("Sincronizado");
            txtProgress.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    };
    public void bind(CoffeeBranch item)
    {
        Id = item.Id;
        txtBranch.setText("Rama " + item.Index);
        txtFrames.setText(item.FramesCount.toString() + " Frames");
        checkStatus(item.Synced);

    }

    void checkStatus(Integer synced) {
        if(synced != 0) {
            syncContainer.setVisibility(VISIBLE);
            btnRegisterPhoto.setVisibility(GONE);
            btnClose.setVisibility(GONE);
            if(synced == 2) {
                txtProgress.setText("Sincronizado");
                txtProgress.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            else {
                txtProgress.setText("Sincronizando ...");
                txtProgress.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        }
    }

    void clickDetail()
    {
        Intent i = new Intent(context,FrameListActivity.class);
        Bundle b = new Bundle();
        b.putInt(FrameListActivity.BRANCH_ID_PARAM, Id); //Your id
        i.putExtras(b);
        context.startActivity(i);
        return;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegisterPhoto:
                listener.captureFrames(Id);
                break;
            case R.id.btnClose:
                new AwesomeInfoDialog(context)
                        .setTitle("Cerrar rama")
                        .setMessage("Los datos serán sincronizados y no se podrán agregar más imágenes")
                        .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                        .setCancelable(true)
                        .setPositiveButtonText(context.getString(R.string.dialog_yes_button))
                        .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                        .setPositiveButtonTextColor(R.color.white)
                        .setNegativeButtonText(context.getString(R.string.dialog_no_button))
                        .setNegativeButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                        .setNegativeButtonTextColor(R.color.white)
                        .setPositiveButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                (new BLL(context)).updateBranch(Id);
                            }
                        })
                        .setNegativeButtonClick(new Closure() {
                            @Override
                            public void exec() {
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }
}
