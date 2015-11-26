package com.example.sato.camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sato.camera.FunctionCalc.FunctionTypeCheck;
import com.example.sato.camera.FunctionCalc.LexicalAnalysis;
import com.example.sato.camera.FunctionCalc.MathDivision;


public class GraphActivity extends ActionBarActivity {

    private String functionString ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        // テキストビューのテキストを設定します
        TextView textView = (TextView)findViewById(R.id.textView);

        Intent i = getIntent();
        String getFormula = i.getStringExtra("MathML");
        //Toast.makeText(getBaseContext(), getFormula, Toast.LENGTH_LONG).show();

        //MathMLから数式のみを抽出
        String divisionFunc = MathDivision.MathDivision(getFormula);
        //数式を空白で単位分割した文字列を取得
        //String formula = LexicalAnalysis.FormulaToInfix(divisionFunc);


        //数式が1or2変数の式であるかの判定
        try {
            String typeCheckedFunction = FunctionTypeCheck.FunctionTypeCheckM(divisionFunc);
            functionString = LexicalAnalysis.FormulaToInfix(typeCheckedFunction);
            textView.setText(typeCheckedFunction);
            Log.d("FTCReturn", functionString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BaseLineView blv = (BaseLineView)findViewById(R.id.canvasBasicView1);
        blv.setFunction(functionString);
    }

//
    //ここに関数がテキストボックスに入力された際のデータ受け渡しを記述する
    public void doAction(View view){
        Log.d("RECEIVE_STR", functionString);
        Intent intent = new Intent(this, FunctionInput.class);
        intent.putExtra("Input Func", functionString);
        startActivityForResult(intent, 1);
    }

    public String getFunctionString(){
        return functionString;
    }

//
//
//    //関数入力画面から関数値を受け取る
//    @Override
//    protected void onActivityResult(int requestCode, int resCode, Intent backIntent) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resCode, backIntent);
//        Log.d("TAG", "Activity Result");
//        switch(resCode){
//            case Activity.RESULT_OK:
//                functionChar = backIntent.getCharSequenceExtra("function").toString();
//                BaseLineView.setFunction(functionChar);
//
//                //数式を空白で単位分割した文字列を取得
//                String formula = LexicalAnalysis.FormulaToInfix(functionChar);
//                //操車場アルゴリズムで数式を逆ポーランド記法に変換
//                List<String> formulaList = Utils.ListDivision(formula);
//                List<String> shuntingYardList = ShuntingYard.ShuntingYardAlg(formulaList);
//                //逆ポーランド記法の計算
//                Float[] resultNum = ReversePolishNotationOld.ReversePolishNotationOld(shuntingYardList, 1, 30);
//                Log.d("TAG","RESULT_OK");
//                Log.d("shuntingYardList",String.valueOf(shuntingYardList));
//                Log.d("ReversePolishResult",String.valueOf(resultNum[0]));
//                break;
//            case Activity.RESULT_CANCELED:
//                Log.d("TAG","RESULT_CANCELED");
//                break;
//        }
//    }



}
