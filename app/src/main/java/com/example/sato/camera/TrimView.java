package com.example.sato.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class TrimView extends View {

    private static TrimView instance = null;

	private float _x = 0, _y = 0;
    Paint paint1;
    Paint paint2;
    Paint paint3;
    int pointSize = 15;
        
    public TrimView(Context context) {
        super(context);
        Log.d("LifeCycle","Trimview Instance Created");
        paint1 = new Paint();
        paint1.setColor(0xcc000000);
        paint1.setAntiAlias(true);

        paint2 = new Paint();
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setColor(Color.RED);

        paint3 = new Paint();
        paint3.setAntiAlias(true);
        paint3.setColor(Color.RED);

        instance = null;
    }

    public static TrimView getInstance(Context context) {
        if(instance == null){
            instance = new TrimView(context);
        }
        return instance;
    }

    int _w;
    int _h;
    //widthとheightの長さ
    int sqWidth;
    int sqHeight;
    //中心
    int sqX;
    int sqY;
        
        
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /*
        _w = w;
        _h = h;
        sqWidth = w - 40;
        sqHeight = w - 40;
        sqX = w / 2;
        sqY = h / 2;
        */
    }
    
    public void sizeSet(int w, int h) {
        _w = w;
        _h = h;
        sqWidth = w - 40;
        sqHeight = w - 40;
        sqX = w / 2;
        sqY = h / 2;
    }
    
    public ArrayList<Integer> getTrimData(){
        ArrayList<Integer> _arl = new ArrayList<Integer>();
        _arl.add(sqX-sqWidth/2);
        _arl.add(sqY-sqHeight/2);
        _arl.add(sqWidth);
        _arl.add(sqHeight);
        
        return _arl;
    }

    //トリミングされる領域の描画
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, _w, sqY - sqHeight / 2, paint1);
        canvas.drawRect(0, sqY - sqHeight / 2, sqX - sqWidth / 2, sqY + sqHeight / 2, paint1);
        canvas.drawRect(sqX + sqWidth
                / 2, sqY - sqHeight / 2, _w, sqY + sqHeight / 2, paint1);
        canvas.drawRect(0, sqY + sqHeight / 2, _w, _h, paint1);
        
        canvas.drawRect(sqX - sqWidth / 2, sqY - sqHeight / 2, sqX + sqWidth
                / 2, sqY + sqHeight / 2, paint2);
        canvas.drawCircle(sqX, sqY - sqHeight / 2, pointSize, paint3);
        canvas.drawCircle(sqX, sqY + sqHeight / 2, pointSize, paint3);
        canvas.drawCircle(sqX - sqWidth / 2, sqY, pointSize, paint3);
        canvas.drawCircle(sqX + sqWidth / 2, sqY, pointSize, paint3);
    }

    String TouchMode = "NONE";
    float _distance = 0f;
    
    //トリミングビューの操作
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
        case MotionEvent.ACTION_DOWN:
            _x = e.getX();
            _y = e.getY();
            //タッチ位置によるモードの判別
            if (sqX - sqWidth / 2+20 < _x && sqX + sqWidth / 2-20 > _x) {
                // ｘ的にいうと触れている
                if (sqY - sqHeight / 2+20 < _y && sqY + sqHeight / 2-20 > _y) {
                    // y的にいうと触れている
                    TouchMode = "MOVE";
                }else if(sqY - sqHeight / 2-20 < _y && sqY + sqHeight / 2+20 > _y){
                    _distance = culcDistance(sqX,sqY,(int)e.getX(),(int)e.getY());
                    TouchMode = "SCALE";
                }
            }else if(sqX - sqWidth / 2-20 < _x && sqX + sqWidth / 2+20 > _x){
                if(sqY - sqHeight / 2-20 < _y && sqY + sqHeight / 2+20 > _y){
                    _distance = culcDistance(sqX,sqY,(int)e.getX(),(int)e.getY());
                    Log.e("log",Float.toString(_distance));
                    TouchMode = "SCALE";
                }
            }
            

            break;
        case MotionEvent.ACTION_MOVE:
            if (TouchMode == "MOVE") {
                float disX = e.getX() - _x;
                float disY = e.getY() - _y;

                sqX += disX;
                sqY += disY;

                
            }else if(TouchMode == "SCALE"){
                
                float _cdistance = culcDistance(sqX,sqY,(int)e.getX(),(int)e.getY());
                
                float _rate = _cdistance/_distance;
                Log.d("rate", String.valueOf(_rate));
                _rate = (_rate < 1.05)? _rate: 1.05f;
                _rate = (_rate > 0.95)? _rate: 0.95f;

                //アスペクト比を横、縦別で管理する場合
                if( _x < sqX -100 || _x >sqX + 100)
                    sqWidth *= _rate;
                if( _y < sqY - 50 || _y >sqY + 50)
                    sqHeight *= _rate;

                if(sqWidth > _w){
                    sqWidth = _w;
                }else if(sqWidth < 100){
                    sqWidth = 100;
                }

                if(sqHeight > _h){
                    sqHeight = _h;
                }else if(sqHeight < 100){
                    sqHeight = 100;
                }

                //アスペクト比を縦横同じ比率にする場合
                //sqHeight = sqWidth;

            }
            if (sqX - sqWidth / 2 < 0) {
                sqX = sqWidth / 2;
            } else if (sqX + sqWidth / 2 > _w) {
                sqX = _w - sqWidth / 2;
            }
            if (sqY - sqHeight / 2 < 0) {
                sqY = sqHeight / 2;
            } else if (sqY + sqHeight / 2 > _h) {
                sqY = _h - sqHeight / 2;
            }
            _distance = culcDistance(sqX,sqY,(int)e.getX(),(int)e.getY());
            _x = e.getX();
            _y = e.getY();
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
            TouchMode = "NONE";
            break;
        default:
            break;
        }
        return true;
    }
    
    private float culcDistance(int x1,int y1,int x2,int y2) {
        float x = x1 - x2;
        float y = y1 - y2;
        return FloatMath.sqrt(x * x + y * y);
    }
}
