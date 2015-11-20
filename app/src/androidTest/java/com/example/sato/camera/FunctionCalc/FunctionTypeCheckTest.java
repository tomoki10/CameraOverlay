package com.example.sato.camera.FunctionCalc;

import junit.framework.TestCase;

/**
 * Created by tomoki on 2015/10/29.
 */

public class FunctionTypeCheckTest extends TestCase {
    //数字が正確にカウントされているかのテスト
    public void testCountString() throws Exception {
        FunctionTypeCheck ftc = new FunctionTypeCheck();
        //成功
        assertEquals(ftc.countString("xxxyyy+123","x"),3);
        assertEquals(ftc.countString("xxx  yyy +123","x"),3);
        assertEquals(ftc.countString("xyx y y+x+123","x"),3);
    }

    //現在対応している数式の確認が可能
    public void test入力された数式が正しいか確認() throws Exception {
        FunctionTypeCheck ftc = new FunctionTypeCheck();

        assertEquals(ftc.FunctionTypeCheckM("2x+4"), "2x+4");
        assertEquals(ftc.FunctionTypeCheckM("y=2x+4"), "2x+4");
        assertEquals(ftc.FunctionTypeCheckM("2y=2x+4"),"2x+4");
        assertEquals(ftc.FunctionTypeCheckM("2y+4=2x+4"),"2x+4");
        assertEquals(ftc.FunctionTypeCheckM("2y+4=-2x+4"),"-2x+4");

        assertEquals(ftc.FunctionTypeCheckM("y=1/2x+4"),"1/2x+4");

        //現在は関数を返すように変更しているのでチェックできない
//        assertEquals(ftc.FunctionTypeCheckM("2x+4"), "{{x->-2}}");
//        assertEquals(ftc.FunctionTypeCheckM("y=2x+4"),"{{x->-2}}");
//        assertEquals(ftc.FunctionTypeCheckM("y=2x+3x+4"),"{{x->-4/5}}");
//        assertEquals(ftc.FunctionTypeCheckM("y=2x+3x+4+4"),"{{x->-8/5}}");
        //うまく計算できない例(左辺が考慮されない)
        //assertEquals(ftc.FunctionTypeCheckM("2y=2x+3x+4+4"),"{{x->-8/5}}");
        //assertEquals(ftc.FunctionTypeCheckM("2y+4=2x+3x+4+4"),"{{x->-8/5}}");

        //assertEquals(ftc.FunctionTypeCheckM("2x+4=0"),"{{x->-2}}");
    }

    public void testEqualDelete() throws Exception {
        FunctionTypeCheck ftc = new FunctionTypeCheck();
        assertEquals(ftc.equalDelete("y=2x+4"), "2x+4");
        assertEquals(ftc.equalDelete("2y=2x+4"), "2x+4");
    }

}