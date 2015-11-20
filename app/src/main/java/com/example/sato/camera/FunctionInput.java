package com.example.sato.camera;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;


public class FunctionInput extends Activity implements KeyboardView.OnKeyboardActionListener{


    private EditText editText;
    private String editString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams param  = new LinearLayout.LayoutParams(WC,WC);
        param.setMargins(0, 10, 0, 0);

        //配置をここで指定(xmlで書くべきだが現状ではKeyboardを描画する場合に
        //Activityから呼び出す方法しか分からないのでlayoutも此処に記述する)
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.WHITE);
        //setContentView(R.layout.activity_function_input);
        setContentView(linearLayout);
        editText = new EditText(this);
        editText.setTextSize(28.0f);
        editText.setWidth(1000);
        InputFilter[] _inputFilter = new InputFilter[1];
        _inputFilter[0] = new InputFilter.LengthFilter(15);
        editText.setFilters(_inputFilter);
        linearLayout.addView(editText,param);

        //前Activityから関数の受け取り
        Intent i = getIntent();
        String func = i.getStringExtra("Input Func");
        editString = func;
        editText.setText(func);
        //editText.setSelection(editString.length());

//        LinearLayout ll = new LinearLayout(this);
//        linearLayout.addView(ll);
//        ll.setOrientation(LinearLayout.HORIZONTAL);
//
//        //WYSIWYGに数式を表記する
//        EditText wysiwygText = new EditText(this);
//        wysiwygText.setTextSize(28.0f);
//        wysiwygText.setWidth(100);
//        ll.addView(wysiwygText, param);
//        //数式を分割して取得
//        String formula = LexicalAnalysis.FormulaToInfix("3^2+2");
//        List<String> strings = Utils.ListDivision(formula);
//        Log.d("TAG", String.valueOf(strings));
//        wysiwygText.setText(Html.fromHtml("<font color=\"red\">" + "<td>3/4</td>" + "</font>"));
//        wysiwygText.setLines(3);
//        EditText w = new EditText(this);
//        ll.addView(w);


        Keyboard keyboard = new Keyboard(this, R.xml.sample_keyboard);
        KeyboardView keyboardView = new KeyboardView(this, null);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener((KeyboardView.OnKeyboardActionListener) this);
        keyboardView.setBackgroundColor(Color.BLACK);
//		keyboardView.setBackgroundResource(R.drawable.ic_launcher);
        //keyboardView.setDrawingCacheBackgroundColor(Color.RED);
        linearLayout.addView(keyboardView,param);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.function_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onKey(int arg0, int[] arg1) {
        // TODO Auto-generated method stub

    }

    //キーボード入力の制御
    @Override
    public void onPress(int inputKey) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRelease(int inputKey) {
        //特殊文字の処理
        if(inputKey > 9999){
            String keyTemp;
            if(inputKey == 10000) {
                keyTemp = "sqrt(";
            }else if(inputKey == 10001){
                keyTemp = "(";
            }else if(inputKey == 10002){
                keyTemp = ")";
            }else{
                keyTemp ="";
            }
            try{
                editString =  editString + keyTemp;
                editTextSet();
            }catch (Exception e){
                e.printStackTrace();
                return;
            }
            return;
        }

        Log.d("KeyLoger", Integer.toString(inputKey));
        byte[] ascii = new byte[]{(byte) inputKey};
        //全削除キー処理
        if(inputKey== KeyEvent.KEYCODE_CLEAR ){			//Keyboard.KEYCODE_CLEAR==28　なぜか参照できない
            if(editString.length()>0){
                editString= "";
                editTextSet();
            }
            return;
        }
        //削除キー処理
        if(inputKey== Keyboard.KEYCODE_DELETE ){
            if(editString.length()>0){
                editString= editString.substring(0, editString.length()-1);
                editTextSet();
            }
            return;
        }else if(inputKey== KeyEvent.KEYCODE_ENTER){
            //グラフ描画のActivityへ処理を戻す
            Intent intent = new Intent();
            intent.putExtra("function", editString);
            this.setResult(Activity.RESULT_OK,intent);
            this.finish();
            return;
        }
        try {
            editString =  editString + new String(ascii , "US-ASCII");
            editTextSet();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void onText(CharSequence arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeDown() {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeLeft() {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeRight() {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeUp() {
        // TODO Auto-generated method stub

    }

    private void editTextSet(){
        editText.setText(editString);
        editText.setSelection(editString.length());
    }
}
