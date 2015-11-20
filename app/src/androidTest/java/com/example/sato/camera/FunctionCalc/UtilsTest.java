package com.example.sato.camera.FunctionCalc;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomoki on 2015/11/10.
 */

public class UtilsTest extends TestCase {

    public void test分割テスト(){
        Utils utils = new Utils();
        String input = " -2 * ( x + 3 )";
        List<String> objectFormula = new ArrayList<String>(){{
            add("-2");add("*");add("(");add("x");add("+");add("3");add(")");}};

        assertEquals(utils.ListDivision(input), objectFormula);

        String input2 = "-4 * x";
        List<String> testFormula = new ArrayList<String>(){{
            add("-4");add("*");add("x");}};
        assertEquals(utils.ListDivision(input2), testFormula);

    }

}