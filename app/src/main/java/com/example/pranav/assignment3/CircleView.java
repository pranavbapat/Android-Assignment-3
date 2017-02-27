package com.example.pranav.assignment3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by pranav on 2/26/17.
 */

public class CircleView extends View {

    ArrayList<Circle> circleList;
    Circle currentCircle;
    float downx, downy;
    float movex, movey;
    float radius;
    int currentMode;
    int currentDrawingColor;

    public CircleView(Context context){
        super(context);
        circleList = new ArrayList<Circle>();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        circleList = new ArrayList<Circle>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        Paint blackFill = new Paint();
        blackFill.setColor(Color.BLACK);
        if(currentCircle != null)
            currentCircle.drawOn(canvas);

        for(Circle oneCircle : circleList)
            oneCircle.drawOn(canvas);
        invalidate();
    }

    public void setValues(int currentMode, int currentDrawingColor){
        this.currentMode = currentMode;
        this.currentDrawingColor = currentDrawingColor;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){

        switch (motionEvent.getAction()){

            case MotionEvent.ACTION_DOWN:
                System.out.println("Action DOWN (" + motionEvent.getX() + ", " + motionEvent.getY() + ")");
                downx = motionEvent.getX();
                downy = motionEvent.getY();

                if(currentMode == 2)
                    deleteCircleIfTouched(downx, downy);
                break;

            case MotionEvent.ACTION_MOVE:
                if(currentMode == 1) {
                    System.out.println("ACTION MOVE (" + motionEvent.getX() + ", " + motionEvent.getY() + ")");
                    movex = motionEvent.getX();
                    movey = motionEvent.getY();
                    radius = (float) Math.sqrt(Math.pow(downx - movex, 2) + Math.pow(downy - movey, 2));
                    currentCircle = new Circle(downx, downy, radius, Color.BLUE);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(currentMode == 1) {
                    circleList.add(currentCircle);
                }
                break;
        }
        return true;
    }

    public void deleteCircleIfTouched(float centreX, float centreY){
        float distanceFromCentre=0;
        Circle currentCircle;

        for(int i=0;i<circleList.size();i++){
            currentCircle = circleList.get(i);
            distanceFromCentre = (float) Math.sqrt(Math.pow(centreX - currentCircle.centreX, 2) + Math.pow(centreY - currentCircle.centreY, 2));
            System.out.println("DISTANCE RADIUS: " + distanceFromCentre + " " + currentCircle.radius);
            if(distanceFromCentre <= currentCircle.radius) {
                System.out.println("CIRCLE DELETED");
                circleList.remove(i);
            }
        }
        invalidate();
    }
}
