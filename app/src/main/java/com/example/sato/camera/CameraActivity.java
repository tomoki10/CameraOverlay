package com.example.sato.camera;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class CameraActivity extends ActionBarActivity {

    private Camera mCamera = null;
    private CameraView mCameraView = null;
    //private PaintView pv = null;
    private TrimView tv = null;
    Bitmap _bmOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        try {
            mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }


        if (mCamera != null) {

            //カメラの描画
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
            //camera_view.addView(pv);
        }


        //btn to close the application

        ImageButton imgClose = (ImageButton) findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //トリミング領域の描画
        LinearLayout trim_view = (LinearLayout)findViewById(R.id.trim_view);
        FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
        tv = new TrimView(getApplicationContext());
        trim_view.addView(tv);
        tv.sizeSet(trim_view.getWidth(),trim_view.getHeight());

        //同じサイズになるのを確認
        Log.d("trimView width",String.valueOf(trim_view.getWidth()));
        Log.d("trimView height", String.valueOf(trim_view.getHeight()));
        Log.d("cameraV width", String.valueOf(camera_view.getWidth()));
        Log.d("cameraV height", String.valueOf(camera_view.getHeight()));

        int _width = trim_view.getWidth();
        int _height = trim_view.getHeight();

        super.onWindowFocusChanged(hasFocus);
    }
}
