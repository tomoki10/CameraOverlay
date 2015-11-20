package com.example.sato.camera.FunctionCalc;

import junit.framework.TestCase;

/**
 * Created by tomoki on 2015/11/10.
 */

public class LexicalAnalysisTest extends TestCase {

    public void test数式分割の確認(){
        LexicalAnalysis la = new LexicalAnalysis();
        //基本構造テスト
        assertEquals(la.FormulaToInfix("1+2-3*4/5")," 1 + 2 - 3 * 4 / 5");
        assertEquals(la.FormulaToInfix("-2*(x+3)^2")," -2 * ( x + 3 ) ^ 2");
        assertEquals(la.FormulaToInfix("-2*(3+4)-2")," -2 * ( 3 + 4 ) - 2");

        //特殊テスト
        assertEquals(la.FormulaToInfix("v=-4*x"),"v = -4 * x");
        assertEquals(la.FormulaToInfix("-2+3")," -2 + 3");
        assertEquals(la.FormulaToInfix("-2+(3+4)")," -2 + ( 3 + 4 )");
        assertEquals(la.FormulaToInfix("-2*(3+4)")," -2 * ( 3 + 4 )");
    }

}