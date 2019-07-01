package com.lexot.cenicafe;


import android.content.SharedPreferences;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.lexot.cenicafe.Models.BLL;
import com.lexot.cenicafe.Models.CoffeeBranch;
import com.lexot.cenicafe.Models.CoffeeFrame;
import com.lexot.cenicafe.Utils.DrawingView;
import com.lexot.cenicafe.Utils.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements
        SurfaceHolder.Callback {

    public static String BRANCH_ID_PARAM = "branchIdParam";

    SharedPreferences pref;
    private DrawingView drawingView;
    private Camera mCamera;
    private boolean recording;
    private Long startRecording;
    private int widthCamera;
    private int heightCamera;

    private List<String> focusModes;
    private MainActivity context;
    private File myDirectory;
    private String nameFolder;
    private CoffeeBranch coffeeBranch = new CoffeeBranch();
    private BLL bll;

    private Boolean cameraStopped = false;
    private byte[] dataFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bll = new BLL(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            coffeeBranch = bll.getBranch(b.getInt(MainActivity.BRANCH_ID_PARAM));
        } else {
            finish();
        }
        createFolder();
        setContentView(R.layout.activity_main);
        setupSurface();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCameraAndPreview();
        recording = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        releaseCameraAndPreview();
        mCamera = Camera.open();
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters parameters = this.mCamera.getParameters();
        // If null, likely called after camera has been released.
        if (parameters == null) {
            return;
        }
        Camera.Size size = getBestPreviewSize(width, height, parameters);
        int previewWidth = size.width;
        int previewHeight = size.height;
        parameters.setPreviewSize(previewWidth, previewHeight);
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.setParameters(parameters);
        mCamera.startPreview();
        mCamera.setDisplayOrientation(90);
        widthCamera = parameters.getPreviewSize().width;
        heightCamera = parameters.getPreviewSize().height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onBackPressed() {
        // Dialogo seguridad vuelta atras
        new AwesomeInfoDialog(this)
                .setTitle("¿Realmente deseas salir?")
                .setMessage("Los datos serán guardados")
                .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                .setCancelable(true)
                .setPositiveButtonText(getString(R.string.dialog_yes_button))
                .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                .setPositiveButtonTextColor(R.color.white)
                .setNegativeButtonText(getString(R.string.dialog_no_button))
                .setNegativeButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                .setNegativeButtonTextColor(R.color.white)
                .setPositiveButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        saveData();
                    }
                })
                .setNegativeButtonClick(new Closure() {
                    @Override
                    public void exec() {
                    }
                })
                .show();
    }

    public void saveData() {
        //Iniciar sincronización
        //Intent syncService = new Intent(context, SyncService.class);
        //syncService.setData(Uri.withAppendedPath(BranchContract.BRANCH_URI, idBranch.toString()));
        //context.startService(syncService);
        setResult(RESULT_OK);
        context.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        super.onKeyDown(keyCode, event);
        if (cameraStopped) {
            mCamera.startPreview();
            cameraStopped = false;
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                saveFrame();
            }
        } else {
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
                if (recording) {
                    mCamera.lock();
                }
                touchFocus();
            }
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
                startRecording = System.currentTimeMillis();
                mCamera.setOneShotPreviewCallback(new Camera.PreviewCallback() {

                    public void onPreviewFrame(byte[] _data, final Camera _camera) {
                        dataFrame = _data;
                        cameraStopped = true;
                        _camera.stopPreview();
                    }
                });
            }
        }
        return true;
    }

    public void saveFrame() {
        Long timeFrame = System.currentTimeMillis() - startRecording;
        ByteArrayOutputStream out_str = new ByteArrayOutputStream();
        Rect rect = new Rect(0, 0, widthCamera, heightCamera);
        YuvImage yuvimage = new YuvImage(dataFrame,
                ImageFormat.NV21, widthCamera, heightCamera, null);
        yuvimage.compressToJpeg(rect, 100, out_str);

        CoffeeFrame coffeeFrame = new CoffeeFrame();
        coffeeFrame.Time = timeFrame.intValue();
        coffeeFrame.Factor = 0d;


        Utilities.writeToFile(myDirectory, timeFrame + ".jpg", out_str.toByteArray());

        coffeeFrame.Data = myDirectory.getPath() + "/" + timeFrame + ".jpg";
        coffeeFrame.BranchId = coffeeBranch.Id;
        bll.createFrame(coffeeFrame);
    }

    public void touchFocus() {
        Integer square = pref.getInt("focusSquare", 75);
        Rect targetFocusRect = new Rect(-10 * square, -1000, 10 * square, 1000);
        int w = drawingView.getWidth();
        int h = drawingView.getHeight();
        Rect tFocusRect = new Rect((targetFocusRect.left + 1000) * w / 2000, (targetFocusRect.top + 1000) * h / 2000, (targetFocusRect.right + 1000) * w / 2000, (targetFocusRect.bottom + 1000) * h / 2000);
        final List<Camera.Area> focusList = new ArrayList<Camera.Area>();
        mCamera.cancelAutoFocus();
        Camera.Area focusArea = new Camera.Area(targetFocusRect, 1000);
        focusList.add(focusArea);
        Camera.Parameters para = mCamera.getParameters();
        focusModes = para.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
            para.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        if (para.getMaxNumFocusAreas() > 0) {
            para.setFocusAreas(focusList);
        }
        if (para.getMaxNumMeteringAreas() > 0) {
            para.setMeteringAreas(focusList);
        }
        TextView tvRecording = findViewById(R.id.tvRecording);
        tvRecording.setVisibility(View.INVISIBLE);
        mCamera.setParameters(para);
        mCamera.autoFocus(myAutoFocusCallback);
        drawingView.setHaveTouch(true, tFocusRect);
        drawingView.invalidate();
    }

    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            // TODO Auto-generated method stub
            if (!pref.getBoolean("autoFocus",false)) {
                mCamera.cancelAutoFocus();
            }
        }
    };

    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size bestSize = null;
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
        bestSize = sizeList.get(0);
        for (int i = 1; i < sizeList.size(); i++) {
            if ((sizeList.get(i).width * sizeList.get(i).height) >
                    (bestSize.width * bestSize.height)) {
                bestSize = sizeList.get(i);
            }
        }
        return bestSize;
    }

    private File getOutputMediaFile() {
        return new File(myDirectory.getPath() + "/video.mp4");
    }
    //Configurar superficie
    public void createFolder() {
        context = this;
        nameFolder = coffeeBranch.getPath();
        //Crear directorio
        myDirectory = new File(nameFolder);

        if (!myDirectory.mkdirs() && !myDirectory.exists()) {
            finish();
        };
    }

    //Configurar superficie
    public void setupSurface() {
        drawingView = new DrawingView(this);
        ViewGroup.LayoutParams layoutParamsDrawing
                = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        this.addContentView(drawingView, layoutParamsDrawing);
        SurfaceView cameraView = (SurfaceView) findViewById(R.id.surfaceview);
        cameraView.getHolder().addCallback(this);
    }


    private void releaseCameraAndPreview() {
        try {
            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }
        catch(Exception e) {

        }
    }
}
