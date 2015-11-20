package com.example.sato.camera.FunctionCalc;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomoki on 2015/11/09.
 */

public class ReversePolishNotationOldTest extends TestCase {

    public void test数式表示確認(){
        ReversePolishNotationOld rpn = new ReversePolishNotationOld();

        float[] f = {2.0f,3.0f,4.0f,5.0f,6.0f};
        List<String> ls = new ArrayList<String>(){{
            add("3");add("x");add("1");add("-");add("+");}};

        float[] rpnResult = rpn.ReversePolishNotationOld(ls,0,30,1.0f);
        assertEquals(rpnResult[0],f[0]);
        assertEquals(rpnResult[1],f[1]);
        assertEquals(rpnResult[4],f[4]);

        //刻み幅の変更
        Float[] f2 = {2.0f,2.5f,3.0f,3.5f,4.0f};
        float[] rpnResult2 = rpn.ReversePolishNotationOld(ls,0,5,0.5f);
        assertEquals(rpnResult2[4],f2[4]);

        //刻み幅の変更(2)
        //0.1fを数十回足すと、小数点第一位で誤差が起きる（許容範囲内）
        Float[] f3 = {2.0f,2.1f,2.2f,2.3f,2.4f};
        float[] rpnResult3 = rpn.ReversePolishNotationOld(ls,0,5,0.1f);
        assertEquals(rpnResult3[4],f3[4]);
        assertEquals(Math.ceil(rpnResult3[49]),7.0d);

        //刻み幅の変更(3)
        Float[] f4 = {2.0f,2.01f,2.02f,2.03f,2.04f};
        float[] rpnResult4 = rpn.ReversePolishNotationOld(ls,0,5,0.01f);
        assertEquals(rpnResult4[4],f4[4]);

        //変数確認
        List<String> ls2 = new ArrayList<String>(){{
            add("-4");add("x");add("*");}};
        Float[] f5 = {-4.0f,-4.4f,-4.8f,-5.2f,-5.6f};
        float[] rpnResult5 = rpn.ReversePolishNotationOld(ls2,-20.0f,20.1f,0.1f);
        assertEquals(rpnResult5[0],80.f);
    }

    public void test値域の確認(){
        ReversePolishNotationOld rpn = new ReversePolishNotationOld();
        assertEquals(rpn.tiikiSize(20,30,0.1f),100);
        assertEquals(rpn.tiikiSize(-20,40,0.1f),600);
        assertEquals(rpn.tiikiSize(-40,-20,0.2f),100);
        assertEquals(rpn.tiikiSize(0,5,0.1f),50);
        assertEquals(rpn.tiikiSize(1,20,0.1f),190);

    }
}