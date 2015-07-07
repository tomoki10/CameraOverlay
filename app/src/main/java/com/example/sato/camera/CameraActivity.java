package com.example.sato.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class CameraActivity extends ActionBarActivity {

    private Camera mCamera = null;
    private CameraView mCameraView = null;
    //private PaintView pv = null;
    private TrimView tv = null;
    Bitmap _bmp;

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
        if(mCamera != null)
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    camera.cancelAutoFocus();
                    camera.autoFocus(null);
                }
            });
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

        ((Button)findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ArrayList<Integer> _al = tv.getTrimData();

                Log.d("x1",String.valueOf(_al.get(0)));
                Log.d("y1", String.valueOf(_al.get(1)));
                Log.d("x2", String.valueOf(_al.get(2)));
                Log.d("y2", String.valueOf(_al.get(3)));

                mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {

                        //data型の画像がrotate90しているので一度保存してから切り取るか検討
                        Rect rect = new Rect(_al.get(0), _al.get(1), _al.get(2), _al.get(3));

                        //BitmapFactory.Options options = new BitmapFactory.Options();
                        //BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(data, 0, data.length, false);
                        //_bmp = decoder.decodeRegion(rect, options);

                        _bmp = BitmapFactory.decodeByteArray(data,0,data.length);
                        Matrix mat = new Matrix();
                        mat.postRotate(90);
                        _bmp = Bitmap.createBitmap(_bmp, 0, 0, _bmp.getWidth(), _bmp.getHeight(), mat, true);

                        //画像の保存
                        Calendar calendar = Calendar.getInstance();
                        File filePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/DCIM/Camera/" +
                                "TestData" + calendar.get(Calendar.SECOND) + ".jpg");
                        Log.d("FilePass", String.valueOf(filePath));
                        bmpSaved(_bmp, filePath);

                    }
                });

                //setResult(RESULT_OK);
                //finish();

            }
        });

        super.onWindowFocusChanged(hasFocus);
    }

    public void onFocusAction(View view){
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                camera.autoFocus(null);
            }
        });
    }

    private void bmpSaved(Bitmap bmp, File filePath){
        OutputStream out = null;
        try{
            //BitmapRegionDecoder reBmp = BitmapRegionDecoder.newInstance(data, 0, data.length, false);
            //画像ファイルが生成できるならば
            out = new FileOutputStream(filePath);
            //bmp = reBmp.decodeRegion(rect, null);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        }catch(FileNotFoundException e){
        }catch(IOException e){
        }finally{
            try{
                if(out != null)
                    out.close();
            }catch(IOException ex){}
        }
    }

}
