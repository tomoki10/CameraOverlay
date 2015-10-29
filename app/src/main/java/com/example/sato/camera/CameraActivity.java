package com.example.sato.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class CameraActivity extends ActionBarActivity {

    private Camera mCamera = null;
    private CameraView mCameraView = null;
    private TrimView trimView = null;
    Bitmap _bmp;

    //Viewのサイズ
    int _width;
    int _height;

    boolean autoFocusOnOff=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.sato.camera.R.layout.activity_camera);

        try {
            mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if (mCamera != null) {
            //カメラの描画
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout) findViewById(com.example.sato.camera.R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
        }

        //デバッグ用
        final Handler handler = new Handler();
        Timer timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(autoFocusOnOff) {
                            mCamera.cancelAutoFocus();
                            mCamera.autoFocus(null);
                            autoFocusOnOff = false;
                        }
                    }
                });
            }
        }, 3000, 5000); //初回起動の遅延(3sec)と周期(3sec)指定

        //ここまで

        //閉じるボタン
        ImageButton imgClose = (ImageButton) findViewById(com.example.sato.camera.R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });


    }


    //現在は使っていない
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mCamera != null)
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    camera.cancelAutoFocus();
                    camera.autoFocus(null);
                    Toast.makeText(getBaseContext(), "AutoFocus", Toast.LENGTH_SHORT).show();
                }
            });
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //トリミング領域の描画
        LinearLayout trim_view = (LinearLayout)findViewById(com.example.sato.camera.R.id.trim_view);
        FrameLayout camera_view = (FrameLayout) findViewById(com.example.sato.camera.R.id.camera_view);
        trimView = new TrimView(getApplicationContext());
        trim_view.addView(trimView);
        trimView.sizeSet(trim_view.getWidth(), trim_view.getHeight());

        //同じサイズになるのを確認
        Log.d("trimView width",String.valueOf(trim_view.getWidth()));
        Log.d("trimView height", String.valueOf(trim_view.getHeight()));
        Log.d("cameraV width", String.valueOf(camera_view.getWidth()));
        Log.d("cameraV height", String.valueOf(camera_view.getHeight()));

        _width = trim_view.getWidth();
        _height = trim_view.getHeight();

        ((ImageButton)findViewById(R.id.imageButton)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCamera.autoFocus(mAutoFocusListener);
            }
        });

        super.onWindowFocusChanged(hasFocus);
    }

    private Camera.AutoFocusCallback mAutoFocusListener = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            if(autoFocusOnOff==false) {
                camera.cancelAutoFocus();
                camera.autoFocus(null);
            }
            // 撮影
            final ArrayList<Integer> _al = trimView.getTrimData();
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {

                    _bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    //-90度回転して保存されているので補正
                    Matrix mat = new Matrix();
                    mat.postRotate(90);
                    Log.d("Bmp width",String.valueOf(_bmp.getWidth()));
                    Log.d("Bmp height", String.valueOf(_bmp.getHeight()));
                    _bmp = Bitmap.createBitmap(_bmp, 0, 0, _bmp.getWidth(), _bmp.getHeight(), mat, true);

                    //trimViewのサイズと画像のサイズが違うので座標を合わせる
                    float _scaleW = (float) _width / (float) _bmp.getWidth();
                    float _scaleH = (float) _height / (float) _bmp.getHeight();
                    _scaleW = _scaleW > 0 ? _scaleW : 0.1f;
                    _scaleH = _scaleH > 0 ? _scaleH : 0.1f;

                    //変換座標(left,top,right,bottom)
                    int x1 = (int)(_al.get(0)/_scaleW);
                    int y1 = (int)(_al.get(1)/_scaleH);
                    int x2 = (int)(_al.get(2)/_scaleW);
                    int y2 = (int)(_al.get(3)/_scaleH);

                    //画像サイズの座標を超えないように補正
                    x1 = (x1>0) ? x1 : 0;
                    y1 = (y1>0) ? y1 : 0;
                    x2 = (x2 + x1 < _bmp.getWidth()) ? x2 : _bmp.getWidth() - x1;
                    y2 = (y2 + y1 < _bmp.getHeight()) ? y2 : _bmp.getHeight() - y1;

                    _bmp = Bitmap.createBitmap(_bmp, x1,y1,x2,y2);
                    //画像の保存
                    Calendar calendar = Calendar.getInstance();
                    File filePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/DCIM/Camera/" +
                            "TestData" + calendar.get(Calendar.SECOND) + ".jpg");
                    Log.d("FilePass", String.valueOf(filePath));
                    bmpSaved(_bmp, filePath);

                    //画面遷移
                    Intent intent = new Intent(getBaseContext(), ThumbnailActivity.class);
                    //ACTION_SENDでデータを受け渡しできるようにを設定
                    intent.setAction(Intent.ACTION_SEND);

                    Uri uri = Uri.fromFile(filePath);

                    //URIを送るためにEXTAR_STREAMを使用
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(intent);
                }
            });
        }
    };

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

//    @Override
//    protected void onPause() {
//        super.onPause();
//        mCamera.setPreviewCallback(null);
//        mCamera.release();
//    }
}
