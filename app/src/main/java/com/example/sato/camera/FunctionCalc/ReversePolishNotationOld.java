package com.example.sato.camera.FunctionCalc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Stack;

/**
 * Created by tomoki on 2015/07/12.
 */

public class ReversePolishNotationOld {
    /**
     * @param stringList the command line arguments
     */
    //3 1 2 / ^ x 2 ^ * -6 x * 3 1 2 / ^ + +


    //float型での計算はバグが出るので書き換え
    //引数(リスト、値域の最初、値域の最後)
    public static float[] ReversePolishNotationOld(List<String> stringList,float start,float end, float stepSize){
        long startTime = System.currentTimeMillis();
        long stopTime;
        float hensuu;     //数式内の変数
        String[] stringArray = (String[])stringList.toArray(new String[0]);;
        // LIFO
        Stack<Float> que = new Stack<Float>();
        Stack<BigDecimal> que2 = new Stack<BigDecimal>();
        float a = 0;
        float b = 0;
        BigDecimal a2;
        BigDecimal b2;

        //数式格納用
        int tiiki = tiikiSize(start,end,stepSize);
        float resultArr[] = new float[tiiki+1];  //+1
        //出力する配列番号
        int count=-1;

        BigDecimal bd;

        //切り捨てよう変数、現在なし
        BigDecimal resultBigDecimal[] = new BigDecimal[tiiki+1];


        for(hensuu=start;hensuu<end;hensuu=hensuu+stepSize){
            count++;
            for (int i = 0; i < stringArray.length; i++) {
                //if(!que.isEmpty())
                if (stringArray[i].equals("x")) {
                    que.push((float)hensuu);
                    que2.push(BigDecimal.valueOf(hensuu));
                    continue;
                }
                if (stringArray[i].equals("+")) {
                    a = que.pop();
                    b = que.pop();
                    que.add(b + a);
                    //
                    a2 = que2.pop();
                    b2 = que2.pop();
                    que2.add(b2.add(a2));

                    //System.out.println(b+" + "+a+"="+(b+a));
                    continue;
                }
                if (stringArray[i].equals("-")) {
                    a = que.pop();
                    b = que.pop();
                    que.add(b - a);
                    //
                    a2 = que2.pop();
                    b2 = que2.pop();
                    que2.add(b2.subtract(a2));

                    //System.out.println(b+" - "+a+"="+(b-a));
                    continue;
                }
                if (stringArray[i].equals("*")) {
                    a = que.pop();
                    b = que.pop();
                    que.add(b * a);
                    //
                    a2 = que2.pop();
                    b2 = que2.pop();
                    que2.add(b2.multiply(a2));

                    //System.out.println(b+" * "+a+"="+(b*a));
                    continue;
                }
                if (stringArray[i].equals("/")) {
                    a = que.pop();
                    b = que.pop();
                    que.add(b / a);
                    //
                    a2 = que2.pop();
                    b2 = que2.pop();
                    que2.add(b2.divide(a2, 2, BigDecimal.ROUND_HALF_UP));

                    //System.out.println(b+" / "+a+"="+(b/a));
                    continue;
                }
                if (stringArray[i].equals("^")) {
                    a = que.pop();
                    b = que.pop();
                    que.add((float)Math.pow(b,a));
                    //
                    a2 = que2.pop();
                    b2 = que2.pop();
                    que2.add(new BigDecimal(Math.pow(b2.floatValue(), a2.floatValue())));

                    //System.out.println(b+" ^ "+a+"="+Math.pow(b,a));
                    continue;
                }
                // 演算子以外はstackに登録する
                System.out.println("stack push decision" + stringArray[i]);
                if(isNumber(stringArray[i])) {
                    que.push(Float.parseFloat(stringArray[i]));
                    que2.push(new BigDecimal(stringArray[i]));
                }
            }
            if(!que.isEmpty()){
//                bd = new BigDecimal(que.pop());
//                bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);  //小数第２位
                //resultArr[count] = que.pop();//float)Math.round(que.pop());
                resultArr[count] = que.pop();//bd.floatValue();
                //
                resultBigDecimal[count] = que2.pop();
                System.out.println("最後:"+resultArr[count]);
                System.out.println("最後:"+resultBigDecimal[count]);
            }else{
                break;
            }
        }

        stopTime = System.currentTimeMillis();
        System.out.print((double)(stopTime - startTime)+"ms");

        //return Float.valueOf(stringArray[0]);
        return resultArr;
    }

    private static boolean isNumber(String str){
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int tiikiSize(float start, float end, float stepSize){
        int range=0;
        if(start>=0 && end>0){
            range = (int)Math.ceil(end-start);
        }else if(start<0 && end>=0){
            range = (int)Math.ceil(Math.abs(start)+end);
        }else if(start<0 && end<=0){
            range = (int)Math.ceil((Math.abs(start) - Math.abs(end)));
        }
        range = (int)Math.ceil(range/stepSize);
        return range;
    }

}
