package com.example.pranav.assignment3;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by pranav on 2/26/17.
 */

public class Circle {
    float centreX;
    float centreY;
    float radius;
    int color;

    public Circle(float centreX, float centreY, float radius, int color){
        this.centreX = centreX;
        this.centreY = centreY;
        this.radius = radius;
        this.color = color;
    }

    public void drawOn(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle(centreX, centreY, radius, paint);
    }
}
