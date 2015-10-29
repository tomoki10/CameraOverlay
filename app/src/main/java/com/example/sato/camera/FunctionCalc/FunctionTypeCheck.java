package com.example.sato.camera.FunctionCalc;

//import org.matheclipse.core.basic.Config;
//import org.matheclipse.core.eval.ExprEvaluator;
//import org.matheclipse.core.interfaces.IExpr;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.form.output.StringBufferWriter;
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
            //return resultErrorStr;
        }

        //変数となる文字が2文字以上含まれているか
        String alfabet = "abcdefghijklmnopqrstuvwxyz";
        int count = 0;
        int charCount = 0;
        //文字数の計測
        while(alfabet.length() > count){
            int i = countString(func, String.valueOf(alfabet.charAt(count)));
            //System.out.print(alfabet.charAt(count) + " = ");
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
            //return resultErrorStr;
        }

        //従属変数となる変数が文字列に含まれているならば処理
        if(charCount==2){
            //イコールが含まれない数式は計算不可
//            System.out.println(func.contains("="));
//            if(!(func.contains("="))){
//                //エラー処理
//            }

            //従属変数と独立変数が右左辺のどちらかだけに存在する場合
            //従属変数に係数がついているか
            String leftFunc = "";   //従属変数用
            String rightFunc = "";  //独立変数用

            //従属変数に係数がつく場合や従属変数と
            //独立変数が片方の辺に両方含まれている場合は今後実装

        }

        //古い方法
        //Symjaライブラリを利用し、式の簡単化
        F.initSymbols(null);
        EvalUtilities util = new EvalUtilities();
        IExpr mathResult;

        //数式の簡単化とy=の除去
        StringBufferWriter buf = new StringBufferWriter();
        String s = "Solve[x+2==0,x]";
        mathResult = util.evaluate("Solve["+func+"==0,x]");

        OutputFormFactory.get().convert(buf, mathResult);
        String output = buf.toString();
        System.out.println("Solve for x : " + func + " is " + output);

//        //新Symja symja-lib-yyyy-mm-dd.jarを使う方法
          //現在はライブラリの導入でエラーが起きるのとコンパイルまでに4~5分かかるので凍結
//        Config.PARSER_USE_LOWERCASE_SYMBOLS = true;
//
//        ExprEvaluator util = new ExprEvaluator(false, 100);
//
//        // Show an expression in the Java form:
//        // Note: single character identifiers are case sensistive
//        // (the "D()" function input must be written as upper case character)
//        IExpr javaForm = util.evaluate("Solve["+func+",a]");
//        // prints: D(Times(Sin(x),Cos(x)),x)
//        String output=javaForm.toString();
//        System.out.println(output);

        return output;
    }

    //文字列内の文字数を数えて返す
    private static int countString(String target, String searchWord){
        return target.length() - target.replaceAll(searchWord, "").length();
    }

}
