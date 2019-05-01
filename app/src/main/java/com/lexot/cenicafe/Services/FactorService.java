package com.lexot.cenicafe.Services;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import com.lexot.cenicafe.ContentProvider.FrameContract;
import com.lexot.cenicafe.Models.BLL;
import com.lexot.cenicafe.Models.CoffeeFrame;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FactorService extends IntentService {
    private Bitmap myBitmap;
    private BLL bll;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FactorService(String name) {
        super(name);
    }
    public FactorService() {
        super("FactorService");
    }
    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        Long id = ContentUris.parseId(workIntent.getData());
        bll = new BLL(this);
        CoffeeFrame coffeeFrame = bll.getFrame(id.intValue());
        String path = coffeeFrame.Data;
        File imgFile = new  File(path);

        if(imgFile.exists()){
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        Mat imageColor = new Mat();
        Mat imageRed, imageFilt  = new Mat(), sobel1 = new Mat(), sobel2 = new Mat();;
        List<Mat> rgb = new ArrayList<Mat>();

        Utils.bitmapToMat(myBitmap, imageColor);
        Core.split(imageColor, rgb);

        imageRed = rgb.get(0);
        switch (pref.getString("filter","1")) {
            case "1":
                Imgproc.Laplacian(imageRed, imageFilt, CvType.CV_32F);
                Core.pow(imageFilt, 2, imageFilt);
                Core.sqrt(imageFilt, imageFilt);
                break;
            case "2":
                Imgproc.Sobel(imageRed, sobel1, CvType.CV_32F, 1, 0);
                Imgproc.Sobel(imageRed, sobel2, CvType.CV_32F, 0, 1);
                imageFilt = new Mat(sobel1.rows(), sobel1.cols(), CvType.CV_32F);
                Core.pow(sobel2, 2, sobel2);
                Core.pow(sobel1, 2, sobel1);
                Core.addWeighted(sobel1, 1, sobel2, 1, 1, imageFilt);
                Core.sqrt(imageFilt, imageFilt);
                break;
            default:
                MatOfDouble mean = new MatOfDouble(), stdDev = new MatOfDouble();
                Core.meanStdDev(imageRed, mean, stdDev);
                Imgproc.Canny(imageRed, imageFilt, stdDev.get(0, 0)[0], stdDev.get(0, 0)[0] * 3);
                break;
        }
        int rCircles = imageRed.cols() / 10;
        int rCirclesPow = (rCircles * rCircles);
        int centerXPoint = imageRed.cols() / 2;
        int centerYPoint = imageRed.rows() / 2;
        int size = 0;
        double sumFilt = 0;
        for (int i = centerYPoint - rCircles; i < centerYPoint + rCircles; i++) {

            int xBand = (int) Math.floor(Math.sqrt(rCirclesPow - (int) Math.pow(centerYPoint - i, 2)));
            size = size + 2 * xBand;
            for (int j = centerXPoint - xBand; j < centerXPoint + xBand; j++) {
                sumFilt = imageFilt.get(i, j)[0] + sumFilt;
            }
        }
        bll.updateFrameFactor(id.intValue(), sumFilt);
    }

}
