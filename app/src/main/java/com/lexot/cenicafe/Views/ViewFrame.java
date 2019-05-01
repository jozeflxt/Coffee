package com.lexot.cenicafe.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lexot.cenicafe.ImageFullActivity;
import com.lexot.cenicafe.Models.CoffeeFrame;
import com.lexot.cenicafe.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ViewFrame extends LinearLayout {
    private final Context context;
    ImageView icon;
    TextView txtTime;
    TextView txtFactor;
    TextView txtProgress;
    private Bitmap myBitmap;

    public ViewFrame(Context context, AttributeSet attrs) {
        super(context);
        this.context = context;
    }

    public ViewFrame(Context context) {
        super(context);
        this.context = context;
        inflate(getContext(), R.layout.view_frame, this);
        this.icon = findViewById(R.id.icon);
        this.txtTime = findViewById(R.id.txtTime);
        this.txtFactor = findViewById(R.id.txtFactor);
        this.txtProgress = findViewById(R.id.txtProgress);
    }

    void clickImage()
    {
        Intent i = new Intent(context,ImageFullActivity.class);
        Bundle b = new Bundle();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        b.putByteArray("Image", stream.toByteArray()); //Your id
        i.putExtras(b);
        context.startActivity(i);

    }
    public void bind(final CoffeeFrame item)
    {
        txtTime.setText(item.Time.toString());
        txtFactor.setText(item.Factor.toString());
        File imgFile = new  File(item.Data);
        if(imgFile.exists()){
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        icon.setImageBitmap(myBitmap);
        if(item.Synced) {
            txtProgress.setText("Sincronizado");
            txtProgress.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        else {
            txtProgress.setText("Sincronizando ...");
            txtProgress.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
        icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickImage();
            }
        });
    }
}