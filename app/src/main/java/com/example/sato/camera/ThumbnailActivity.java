package com.example.sato.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class ThumbnailActivity extends ActionBarActivity {

    private String open_filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnail);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);


        //Uriの取得
        Intent cameraIntent = getIntent();
        Uri uri = cameraIntent.getParcelableExtra(Intent.EXTRA_STREAM);
        open_filepath = uri.getPath();
        Log.d("OpenFilePath", open_filepath);

        //画像の読み込み
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        //画像用のメモリ領域を確保せず、画像の構造を読み取る
        Bitmap _bmp = BitmapFactory.decodeFile(open_filepath,options);

        //Viewへ画像をセット
        imageView.setImageBitmap(_bmp);
    }

    public void imageUploadOfServer(View view){
        //非同期処理のため作成したUploadAsyncTaskクラスのインスタンスを生成
        Log.d("OPEN_FILEname", open_filepath);
        UploadAsyncTask upAsync = new UploadAsyncTask(this,
                new UploadAsyncTask.AsyncTaskCallback() {
            public void preExecute() {
                //だいたいの場合ダイアログの表示などを行う
            }
            public void postExecute(String result) {
                //AsyncTaskの結果を受け取ってなんかする
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
            }
            public void progressUpdate(int progress) {
                //だいたいプログレスダイアログを進める
            }
            public void cancel() {
                //キャンセルされた時になんかする
            }
        });
        //アクセス先のURL
        String param1 = "http://任意";
        String param2 = open_filepath;
        //URLを受け渡し
        upAsync.execute(param1, param2);

    }
}
