package com.example.sato.camera.FunctionCalc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tomoki on 2015/07/29.
 */

public class MathDivision {
    public static String MathDivision(String functionStr){
        // TODO code application logic here
        long start = System.currentTimeMillis();
        long stop;
        String mathFolmula = "";
        String tmp;
        Boolean flag = false;
        String buffer = "";
        int fracCounter = 0;
        int rootCounter = 0;
        int count=0;        //２乗がきたときに使うカウント

        try{
            //File file = new File("/Users/sato/Documents/TextDivision/testRoot.txt");
            BufferedReader br = new BufferedReader(new StringReader(functionStr));

            String str = br.readLine();
            while(str != null){


                //分数があるなら
                if(str.contains("<mfrac>")){
                    flag = true;
                }

                if(str.contains("<mrow>") && flag == true){
                    fracCounter++;
                }else if(str.contains("</mrow>") && flag == true){
                    fracCounter--;
                    if(fracCounter == 0){
                        mathFolmula = mathFolmula+"/";
                        flag = false;
                    }
                }

                //根号があるなら
                if(str.contains("<mroot>")){
                    mathFolmula = mathFolmula+"(";
                    while(!str.contains("</mroot>")){

                        //根号の指数が存在するなら
                        if(rootCounter > 0 && (str.contains("<mi>") || str.contains("<mn>"))){
                            str = rawStringCut(str);
                            mathFolmula = mathFolmula+"^("+"1/"+str+")";
                            rootCounter++;
                        }

                        if(str.contains("<mi>") || str.contains("<mn>")){

                            str = rawStringCut(str);
                            mathFolmula = mathFolmula+str;
                            rootCounter++;
                        }
                        str = br.readLine();
                    }
                    //根号の指数がないなら
                    if(rootCounter == 1){
                        mathFolmula = mathFolmula+"^(1/2)";
                    }
                    rootCounter = 0;
                    mathFolmula = mathFolmula+")";
                }


                //累乗があるなら
                if(str.contains("<msubsup>")){
                    mathFolmula = mathFolmula+"(";
                    while(!str.contains("</msubsup>")){
                        if(str.contains("<mi>") || str.contains("<mo>") || str.contains("<mn>")){
                            str = rawStringCut(str);
                            mathFolmula = mathFolmula+str;
                        }
                        if(str.contains("<mrow>")) count++;
                        if(count == 2){
                            mathFolmula = mathFolmula+"^";
                            count=0;
                        }

                        str = br.readLine();
                    }
                    count=0;
                    mathFolmula = mathFolmula+")";
                }
                System.out.println("現在生成した数式:"+str);

                //タグの削除
                if(str.contains("<mi>") || str.contains("<mo>") || str.contains("<mn>")){
                    str = rawStringCut(str);
                    mathFolmula = mathFolmula+str;
                }
                str = br.readLine();
            }

            br.close();
        }catch(FileNotFoundException e){
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mathFolmula;
    }


    //数値やオペランド,変数のみを抽出する
    private static String rawStringCut(String item){
        //前方タグ削除
        Pattern pattern = Pattern.compile("<..>");
        Matcher matcher = pattern.matcher(item);
        item = matcher.replaceAll("");
        //後方タグ削除
        pattern = Pattern.compile("<...>");
        matcher = pattern.matcher(item);
        item = matcher.replaceAll("");
        //空白の削除
        pattern = Pattern.compile(" ");
        matcher = pattern.matcher(item);
        item = matcher.replaceAll("");
        //if(item.contains("(")) item = "*" + item; //もしも()付けるなら
        return item;
    }
}
