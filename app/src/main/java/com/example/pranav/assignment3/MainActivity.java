package com.example.pranav.assignment3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private int currentMode;
    private int currentDrawingColor;
    CircleView shapes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentMode = 1;
        currentDrawingColor = 1;
        shapes = (CircleView)findViewById(R.id.circle_view);
        shapes.setValues(currentMode, currentDrawingColor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuModeInflater = getMenuInflater();
        menuModeInflater.inflate(R.menu.mode_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem selectedItem){
        switch (selectedItem.getItemId()){
            case R.id.mode_draw_black:
                selectedItem.setChecked(true);
                currentMode = 1;
                currentDrawingColor = 1;
                shapes.setValues(currentMode, currentDrawingColor);
                return true;
            case R.id.mode_draw_blue:
                selectedItem.setChecked(true);
                currentMode = 1;
                currentDrawingColor = 2;
                shapes.setValues(currentMode, currentDrawingColor);
                return true;
            case R.id.mode_draw_green:
                selectedItem.setChecked(true);
                currentMode = 1;
                currentDrawingColor = 3;
                shapes.setValues(currentMode, currentDrawingColor);
                return true;
            case R.id.mode_draw_red:
                currentMode = 1;
                currentDrawingColor = 4;
                shapes.setValues(currentMode, currentDrawingColor);
                return true;
            case R.id.mode_delete:
                currentMode = 2;
                shapes.setValues(currentMode, currentDrawingColor);
                return true;
            case R.id.mode_move:
                currentMode = 3;
                shapes.setValues(currentMode, currentDrawingColor);
                return true;
        }
        return true;
    }
}
