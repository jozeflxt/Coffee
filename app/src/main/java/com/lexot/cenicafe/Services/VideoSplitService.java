package com.lexot.cenicafe.Services;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.lexot.cenicafe.ContentProvider.BranchContract;
import com.lexot.cenicafe.ContentProvider.FrameContract;
import com.lexot.cenicafe.Models.BLL;
import com.lexot.cenicafe.Models.CoffeeBranch;
import com.lexot.cenicafe.Models.CoffeeFrame;
import com.lexot.cenicafe.Utils.Utilities;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class VideoSplitService extends IntentService {
    private Integer idBatch;
    private BLL bll;

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public VideoSplitService(String name) {
        super(name);
    }
    public VideoSplitService() {
        super("VideoSplitService");
    }
    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        double fps = (double) workIntent.getIntExtra("fps", 15);
        Long branchId = ContentUris.parseId(workIntent.getData());
        bll = new BLL(this);

        CoffeeBranch coffeeBranch = bll.getBranch(branchId.intValue());


        FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();

        //retriever.setDataSource(realPath);
        retriever.setDataSource(coffeeBranch.VideoUrl);
        String stringDuration = retriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
        long duration = (long) Double.parseDouble(stringDuration) * 1000;
        Mat imageColor = new Mat();
        Long tsLong1 = 0l;
        while (tsLong1 < duration) {
            Bitmap bitmap=retriever.getFrameAtTime(tsLong1,FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Utils.bitmapToMat(bitmap, imageColor);

            Mat imageRed, imageFilt = new Mat(), sobel1 = new Mat(), sobel2 = new Mat();
            List<Mat> rgb = new ArrayList<Mat>();

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

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
            double sumFilt = 0;
            MatOfDouble mean = new MatOfDouble(), stdDev = new MatOfDouble();
            Core.meanStdDev(imageFilt, mean, stdDev);
            sumFilt = mean.get(0,0)[0];

            CoffeeFrame coffeeFrame = new CoffeeFrame();
            coffeeFrame.Factor = sumFilt;
            coffeeFrame.BranchId = branchId.intValue();

            String nameFolder = coffeeBranch.getPath();
            File myDirectory = new File(nameFolder);
            Utilities.writeToFile(myDirectory,tsLong1 + ".jpg",byteArray);

            coffeeFrame.Data = myDirectory+"/"+tsLong1 + ".jpg";
            coffeeFrame.Time = tsLong1.intValue();
            bll.createFrame(coffeeFrame);
            tsLong1 += (long)(1000000 / fps);
        }
    }
}
