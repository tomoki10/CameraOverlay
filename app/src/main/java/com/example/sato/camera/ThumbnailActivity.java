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

    public void imageUploadToServer(View view){
        //非同期処理のため作成したUploadAsyncTaskクラスのインスタンスを生成
        Log.d("OPEN_FILEname", open_filepath);
        UploadAsyncTask upAsync = new UploadAsyncTask(this,
                new UploadAsyncTask.AsyncTaskCallback() {
            public void preExecute() {
                //だいたいの場合ダイアログの表示などを行う
            }
            public void postExecute(String result) throws Exception {
                //AsyncTaskの結果を受け取って行う処理を記述する
                //resultにはサーバからの数式が返ってくる

                Intent intent = new Intent(getBaseContext(), GraphActivity.class);
                //ACTION_SENDでデータを受け渡しできるようにを設定
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra("MathML",result);
                startActivity(intent);

//
//                String divisionFunc = MathDivision.MathDivision(result);
//                //数式を空白で単位分割した文字列を取得
//                String formula = LexicalAnalysis.FormulaToInfix(divisionFunc);
//                //数式が1or2変数の式であるかの判定
//                String receiveStr = FunctionTypeCheck.FunctionTypeCheckM(formula);

//                //操車場アルゴリズムで数式を逆ポーランド記法に変換
//                List<String> formulaList = ShuntingYard.ListDivision(formula);
//                List<String> shuntingYardList = ShuntingYard.ShuntingYardAlg(formulaList);
//                //逆ポーランド記法の計算
//                Float[] resultNum = ReversePolishNotationOld.ReversePolishNotationOld(shuntingYardList, 1, 30);
//                Log.d("TAG","RESULT_OK");
//                Log.d("shuntingYardList",String.valueOf(shuntingYardList));
//                Log.d("ReversePolishResult",String.valueOf(resultNum[0]));
//
//                Toast.makeText(getBaseContext(), String.valueOf(resultNum[0]), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getBaseContext(), receiveStr, Toast.LENGTH_LONG).show();
            }
            public void progressUpdate(int progress) {
                //だいたいプログレスダイアログを進める
            }
            public void cancel() {
                //キャンセルされた時になんかする
                Toast.makeText(getBaseContext(), "AAA", Toast.LENGTH_LONG).show();
            }
        });
        //アクセス先のURL
        String param1 = "http://任意";


        String param2 = open_filepath;
        Log.d("TAG URI",open_filepath);

        //デバッグ用にpath変更
        //param2  = "/storage/emulated/0/DCIM/Camera/TestData22.jpg";


        //URLを受け渡し
        upAsync.execute(param1, param2);

    }
}
