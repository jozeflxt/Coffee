package com.lexot.cenicafe.Views;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lexot.cenicafe.BatchActivity;
import com.lexot.cenicafe.ListActivity;
import com.lexot.cenicafe.MapListActivity;
import com.lexot.cenicafe.MapsActivity;
import com.lexot.cenicafe.Models.BLL;
import com.lexot.cenicafe.OptionsActivity;
import com.lexot.cenicafe.R;

import java.util.ArrayList;

public class ButtonHome extends RelativeLayout implements View.OnClickListener {
    Integer id;
    Context context;
    private BLL bll;

    public ButtonHome(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.bll = new BLL(context);
        this.setOnClickListener(this);
        inflate(context, R.layout.view_button_home,this);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ButtonHome,
                0, 0);

        try {
            (findViewById(R.id.main)).setOnClickListener(this);
            (findViewById(R.id.main)).setBackgroundColor(a.getColor(R.styleable.ButtonHome_colorBH,getResources().getColor(android.R.color.white)));
            ((TextView)findViewById(R.id.title)).setText(a.getString(R.styleable.ButtonHome_titleBH));
            ((ImageView)findViewById(R.id.image)).setImageDrawable(a.getDrawable(R.styleable.ButtonHome_imageBH));
            id = a.getInt(R.styleable.ButtonHome_idBH,0);
        } finally {
            a.recycle();
        }
    }

    @Override
    public void onClick(View v) {
        switch (id)
        {
            case 1:
                Intent in1 = new Intent(context,BatchActivity.class);
                context.startActivity(in1);
                return;
            case 2:
                requestName(false);
                return;
            case 3:
                Intent in3 = new Intent(context,MapListActivity.class);
                context.startActivity(in3);
                return;
            case 4:
                return;
            case 5:
                Intent in5 = new Intent(context,ListActivity.class);
                context.startActivity(in5);
                return;
            case 6:
                Intent in6 = new Intent(context,OptionsActivity.class);
                context.startActivity(in6);
                return;
        }
    }

    public void requestName(final Boolean isVideo)
    {
        ArrayList listBatches = bll.getBatches(false);





    }

}
