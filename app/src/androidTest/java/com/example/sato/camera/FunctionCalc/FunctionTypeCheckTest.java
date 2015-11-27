package com.example.sato.camera.FunctionCalc;

import junit.framework.TestCase;

/**
 * Created by tomoki on 2015/10/29.
 */

public class FunctionTypeCheckTest extends TestCase {
    public void test数字が正確にカウントされているかのテスト() throws Exception {
        FunctionTypeCheck ftc = new FunctionTypeCheck();
        assertEquals(ftc.countString("xxxyyy+123","x"),3);
        assertEquals(ftc.countString("xxx  yyy +123","x"),3);
        assertEquals(ftc.countString("xyx y y+x+123","x"),3);
    }

    //現在対応している数式の確認が可能
    public void test入力された数式が正しいか確認() throws Exception {
        FunctionTypeCheck ftc = new FunctionTypeCheck();

        //対応している数式のチェック
        assertEquals(ftc.FunctionTypeCheckM("2y=2x+4"), "2+x");
        assertEquals(ftc.FunctionTypeCheckM("y=2x+4"),"4+2*x");
        assertEquals(ftc.FunctionTypeCheckM("y=2x+3x+4+4"),"8+5*x");
        assertEquals(ftc.FunctionTypeCheckM("2y+x+4=2x+3x+4+4"),"2+2*x");
        assertEquals(ftc.FunctionTypeCheckM("2u+x+4=2x+3x+4+4"),"2+2*x");

        //入力数式がエラーの場合のチェック
        assertEquals(ftc.FunctionTypeCheckM("2+3+5"),"変数がありません");
        assertEquals(ftc.FunctionTypeCheckM("2x+a+b=0"),"変数が三つ以上あります");
    }

//    public void testEqualDelete() throws Exception {
//        FunctionTypeChecker ftc = new FunctionTypeChecker();
//        assertEquals(ftc.equalAdd("y=2x+4"), "y==2x+4");
//        assertEquals(ftc.equalAdd("2y=2x+4"), "2y==2x+4");
//    }

    public void testDependentVariableDelete() throws Exception{
        FunctionTypeCheck ftc = new FunctionTypeCheck();
        assertEquals(ftc.dependentVariableDelete("{{y->2+x}}"), "2+x");
        assertEquals(ftc.dependentVariableDelete("{{y->4+2*x}}"), "4+2*x");
    }

}