package com.example.sato.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by tomoki on 2015/07/06.
 */
//カメラプレビューを表示するView
//2015/11/29現在ではandroid.hardware.cameraが非推奨になっているので
//android.hardware.camera2を使うべきかも
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraView(Context context, Camera camera) {
        super(context);
        Log.d("Surface", "Create Constructor");
        mCamera = camera;
        mCamera.setDisplayOrientation(90);
        mCamera = cameraParamSetting(mCamera);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("Surface", "surfaceCreated");
        try{
            if(mCamera == null) {
                Log.d("Surface", "mCamera==null");
                //戻るボタンで画面がCameraActivityに戻るときにコンストラクタが生成されないので
                //ここに同様の処理を記述
                mCamera = Camera.open();
                mCamera.setDisplayOrientation(90);
                mHolder = getHolder();
                mHolder.addCallback(this);
                mCamera = cameraParamSetting(mCamera);
            }
            //mCamera.stopPreview();
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            Log.d("ERROR", "Camera error on surfaceCreated " + e.getMessage());
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("Surface", "surfaceChanged");
        if(mHolder.getSurface() == null)
            return;
        try{
            mCamera.stopPreview();
        } catch (Exception e){

        }
        try{
            if (mCamera != null) {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            Log.d("ERROR", "Camera error on surfaceChanged " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("Surface", "surfaceDestroyed");
        mCamera.stopPreview();
    }

    //数式撮影するために最適な画面拡大サイズや手振れ補正などを設定
    private Camera cameraParamSetting(Camera camera){
        //Zoom処理 デフォのサイズでは教科書の小さい数字がうまく写せないので事前に拡大表示する
        Camera.Parameters prm = camera.getParameters();
        prm.setVideoStabilization(true);            //手振れ補正
        Log.d("Option", "getMaxZoom " + prm.getMaxZoom());
        prm.setZoom(17);
        camera.setParameters(prm);
        return camera;
    }
}
