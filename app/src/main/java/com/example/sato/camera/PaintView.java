package com.example.sato.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by tomoki on 2015/07/07.
 */
public class PaintView extends View{

    private Paint paint;

    private ArrayList<Path> pathList = new ArrayList<Path>();
    private Path path;

    public PaintView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Path path : pathList) {
            canvas.drawPath(path, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                // タッチしたとき
                path = new Path();
                path.moveTo(event.getX(), event.getY());
                pathList.add(path);
                break;
            case MotionEvent.ACTION_MOVE:
                // タッチしたまま動かしたとき
                path.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                // 指を離したとき
                path.lineTo(event.getX(), event.getY());
                break;
            default:
        }
        invalidate();
        return true;
    }


}
