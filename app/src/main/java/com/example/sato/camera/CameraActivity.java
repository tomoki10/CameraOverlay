package com.example.sato.camera;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;


public class CameraActivity extends ActionBarActivity {

    private Camera mCamera = null;
    private CameraView mCameraView = null;
    private PaintView pv=null;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("TAG","TAGAAA");
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        try{
            mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e){
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }


        if(mCamera != null) {
            pv = new PaintView(this);
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);


            camera_view.addView(mCameraView);//add the SurfaceView to the layout
            camera_view.addView(pv);
        }



        //btn to close the application

        ImageButton imgClose = (ImageButton)findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });


    }

}
