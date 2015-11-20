package com.example.sato.camera.FunctionCalc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomoki on 2015/10/19.
 */

public class Utils {

    //数式文字列を空白ごとのリストに分割する
    public static List<String> ListDivision(String division_formula){
        List<String> objectFormula = new ArrayList<String>();
        String[] array;
        array = division_formula.split(" ");
        System.out.println(array[0]+"空白");
        for(int i=1; i<array.length;i++){
            objectFormula.add(array[i]);
        }
        return objectFormula;
    }
}
