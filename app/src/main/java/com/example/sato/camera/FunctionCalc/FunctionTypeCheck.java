package com.example.sato.camera.FunctionCalc;


import org.matheclipse.core.basic.Config;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tomoki on 2015/09/27.
 */

public class FunctionTypeCheck {
    public static String FunctionTypeCheckM(String func) throws Exception{

        System.out.print("FunctionTypeCheckに入力された数式："+func);

        //3変数の数式や変数のない数式が出力された場合にエラー文を返す
        String resultErrorStr;

        //独立変数となる変数が文字列に含まれているか
        Pattern p = Pattern.compile("[a-z]");
        Matcher m = p.matcher(func);

        if(!m.find()){
            //変数なしのエラー処理
            resultErrorStr="変数がありません";
            return resultErrorStr;
        }

        //変数となる文字が2文字以上含まれているか
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int count = 0;
        int charCount = 0;
        //文字数の計測
        while(alphabet.length() > count){
            int i = countString(func, String.valueOf(alphabet.charAt(count)));
            //System.out.print(alphabet.charAt(count) + " = ");
            //System.out.println(i);
            if(i>0){
                charCount++;
            }
            count++;
        }
        //文字数
        System.out.println("変数の数："+charCount);
        if(charCount>2){
            //エラー処理
            resultErrorStr="変数が三つ以上あります";
            return resultErrorStr;
        }

        //従属変数となる変数が文字列に含まれているならば処理

        //新Symja symja-lib-yyyy-mm-dd.jarを使う方法
        //現在はライブラリの導入でエラーが起きるのとコンパイルまでに4~5分かかるので凍結
        //実行可能になった！しかし、2~3分ほどコンパイルに時間がかかるので正式ビルドまでNetBeansで開発
        Config.PARSER_USE_LOWERCASE_SYMBOLS = true;

        ExprEvaluator util = new ExprEvaluator(false, 100);

        //SymJaに数式を読ませるため=を2個にする
        func = func.replace("=", "==");

        //yがご認識されやすいので補正（要修正）
        String regex = "u|v|p";
        func = func.replaceAll(regex, "y");

        IExpr javaForm = util.evaluate("Solve["+func+",y]");
        // prints: D(Times(Sin(x),Cos(x)),x)
        String output=javaForm.toString();
        System.out.println("SymJa出力結果"+output);

        //数式を整形
        func=dependentVariableDelete(output);
        return func;
    }

    //文字列内の文字数を数えて返す
    public static int countString(String target, String searchWord){
        return target.length() - target.replaceAll(searchWord, "").length();
    }

    //イコールの追加
    public static String equalAdd(String func){
        int start = func.indexOf("=");
        func = func.substring(start+1,func.length());
        return func;
    }

    //{{y->???}}のように来る式を???の形に整形する
    public static String dependentVariableDelete(String func){
        StringBuilder sb = new StringBuilder(func);
        sb.delete(0, 5);
        func = sb.substring(0, sb.length()-2);
        System.out.println("dependentValiableDelete Return = "+func);
        return func;
    }

}
