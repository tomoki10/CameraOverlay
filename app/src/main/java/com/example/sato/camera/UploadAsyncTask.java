package com.example.sato.camera;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class UploadAsyncTask extends AsyncTask<String, Integer, String> {

    //Activityへのコールバック用インタフェース
    //AsyncTaskは基本的にActivityへTask終了の状態を渡せないので使用する
    public interface AsyncTaskCallback {
        void preExecute();
        void postExecute(String result);
        void progressUpdate(int progress);
        void cancel();
    }

    private AsyncTaskCallback callback = null;

    private ProgressDialog dialog;	//進行状況の表示
    private Context context;
    private long start;		//処理開始時間計測
    private long end;			//処理終了時間計測

    public UploadAsyncTask(Context context){
        start = System.currentTimeMillis();
        this.context = context;
    }

    public UploadAsyncTask(Context context, AsyncTaskCallback _callback){
        start = System.currentTimeMillis();
        this.context = context;
        this.callback = _callback;
    }

    //非同期で実行したい処理の記述
  /*
   * (non-Javadoc)
   * @see android.os.AsyncTask#doInBackground(Params[])
   * Params[]に格納されたデータを使用できる
   */
    @Override
    protected String doInBackground(String... params) {

        String str = "";		//Webサーバからのレスポンス表示格納用の変数

        //クライアントオブジェクトの生成
        DefaultHttpClient client = new DefaultHttpClient();
        //POSTリクエストインスタンスの生成
        HttpPost post = new HttpPost(params[0]);
        //レスポンスの格納
        HttpResponse response = null;

        //MultipartEntityは廃止されているのでMultipartEntityBuilderを使用
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        //
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entity.setCharset(Charset.forName("UTF-8"));

        //名前はサーバーに送るname（＊webサーバーcakephpだと
        //data[A][B]で、連想配列で取得できる）

        String filename = params[1]; //"/storage/emulated/0/Pictures/Write On Picture/photo2.jpg";
        //画像の設定
        //画像のファイルパス
        File file = new File(filename);
        //データ順：PHPで受け取る際のname,stream,contentType,filename
        //Log.d("NAME", file.toString());
        entity.addBinaryBody("upfile",file,ContentType.create("image/jpeg"),filename);

        //テキストの設定
        ContentType textContentType = ContentType.create("text/plain","UTF-8");
        //データ順：PHPで受け取る際のname,送信したいtext,contentType
        entity.addTextBody("upfile","YahooBB",textContentType);

        post.setEntity(entity.build());

        try {
            //webサーバへファイルをPOST、responseを格納
            response = client.execute(post);
            str = EntityUtils.toString(response.getEntity(), "UTF-8");
            //LogCatにレスポンスを表示
            Log.i("HTTP status Line", response.getStatusLine().toString());
            Log.i("HTTP response", new String(str));
            end = System.currentTimeMillis();
            Log.d(getClass().getName(), "Measure: " + (end - start));
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    //非同期処理完了時に実行したい処理
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        callback.postExecute(result);

        if(dialog != null){
            dialog.dismiss();

        }
    }

    //非同期処理前に実行したい処理
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.preExecute();
        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait");
        dialog.setMessage("Uploading...");
        dialog.show();
    }


    //非同期処理を途中で終了する場合の処理
    @Override
    protected void onCancelled() {
        super.onCancelled();
        callback.cancel();
    }

    //進捗状況をUIスレッドで表示する記述が行える
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        callback.progressUpdate(values[0]);
    }
}
