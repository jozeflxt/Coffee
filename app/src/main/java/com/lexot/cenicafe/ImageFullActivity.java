package com.lexot.cenicafe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageFullActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full);
        ImageView image = findViewById(R.id.image);
        byte[] imageData;
        Bundle b = getIntent().getExtras();
        if(b != null) {
            imageData = b.getByteArray("Image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

            Matrix matrix = new Matrix();

            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap .getWidth(), bitmap.getHeight(), matrix, true);
            image.setImageBitmap(rotatedBitmap);

        }
    }
}
