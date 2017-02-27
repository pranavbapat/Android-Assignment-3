package com.example.pranav.assignment3;

/**
 * Created by pranav on 2/27/17.
 */

public class DrawingActivity extends AppCompatActivity {

    private static final String DEFAULT_MODE = "draw";
    private static final String DEFAULT_COLOR = "black";

    private DrawingView circleDrawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.circleDrawingView = new DrawingView(this);
        this.circleDrawingView.setAppMode(DEFAULT_MODE);
        this.circleDrawingView.setColor(DEFAULT_COLOR);
        setContentView(this.circleDrawingView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mode_settings, menu);
        menu.findItem(R.id.app_mode).getSubMenu().findItem(R.id.drawing_mode).setChecked(true);
        menu.findItem(R.id.color_mode).getSubMenu().findItem(R.id.black_color).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean showMessage = true;
        String toastMessage = getResources().getString(R.string.color_update_toast);
        switch (item.getItemId()) {
            case R.id.drawing_mode:
                this.circleDrawingView.setAppMode("draw");
                item.setChecked(true);
                toastMessage = getResources().getString(R.string.draw_toast);
                break;
            case R.id.moving_mode:
                this.circleDrawingView.setAppMode("move");
                item.setChecked(true);
                toastMessage = getResources().getString(R.string.move_toast);
                break;
            case R.id.delete_mode:
                this.circleDrawingView.setAppMode("delete");
                item.setChecked(true);
                toastMessage = getResources().getString(R.string.delete_toast);
                break;
            case R.id.blue_color:
                Log.d("DrawAct", "blue");
                this.circleDrawingView.setColor("blue");
                item.setChecked(true);
                break;
            case R.id.green_color:
                Log.d("DrawAct", "green");
                this.circleDrawingView.setColor("green");
                item.setChecked(true);
                break;
            case R.id.red_color:
                Log.d("DrawAct", "red");
                this.circleDrawingView.setColor("red");
                item.setChecked(true);
                break;
            case R.id.black_color:
                Log.d("DrawAct", "black");
                this.circleDrawingView.setColor("black");
                item.setChecked(true);
                break;
            default:
                showMessage = false;
                break;
        }
        if(showMessage)
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();

        return true;
    }
}

res/menu/mode_settings.xml:

<menu xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="schemas.android.com/tools"
        tools:context="com.example.sagar.drawingapplication.DrawingActivity"
        xmlns:drawingapplication="http://schemas.android.com/apk/res-auto">
<item
android:id="@+id/app_mode"
        android:orderInCategory="100"
        drawingapplication:showAsAction="always|withText"
        android:title="@string/mode">
<menu>
<group android:checkableBehavior="single">
<item android:id="@+id/drawing_mode"
        android:title="@string/drawing_mode" />
<item android:id="@+id/moving_mode"
        android:title="@string/moving_mode" />
<item android:id="@+id/delete_mode"
        android:title="@string/delete_mode" />
</group>
</menu>
</item>
<item
android:id="@+id/color_mode"
        android:orderInCategory="200"
        drawingapplication:showAsAction="always|withText"
        android:title="@string/color">
<menu>
<group android:checkableBehavior="single">
<item android:id="@+id/blue_color"
        android:title="@string/blue_color" />
<item android:id="@+id/green_color"
        android:title="@string/green_color" />
<item android:id="@+id/red_color"
        android:title="@string/red_color" />
<item android:id="@+id/black_color"
        android:title="@string/black_color" />
</group>
</menu>
</item>
</menu>

        View class:
        package com.example.sagar.drawingapplication;

        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Paint;
        import android.graphics.PointF;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.VelocityTracker;
        import android.view.View;

        import java.util.ArrayList;
        import java.util.Iterator;
        import java.util.List;
        import java.util.Timer;
        import java.util.TimerTask;

/**
 * Created by Sagar on 2/18/2017.
 */

public class DrawingView extends View {
    private static final float DEFAULT_RADIUS = 100f;

    private Circle mCurrentCircle;
    private List<Circle> mCircleList = new ArrayList<>();
    private VelocityTracker velocityTracker;
    private Paint mPaintPreference;
    private PointF currentPoint;
    private PointF deleteModeInitialPoint;
    private PointF moveModeInitialPoint;
    private Timer moveTimer;

    private String appMode;
    private String touchAction;
    private float centerX = 0f;
    private float centerY = 0f;
    private float endPointX = 0f;
    private float endPointY = 0f;
    private float radius = 0f;
    private float xVelocity = 0f;
    private float yVelocity = 0f;

    public DrawingView(Context context){
        super(context);

        mPaintPreference = new Paint();
        //mPaintPreference.setStyle(Paint.Style.STROKE);
    }

    public DrawingView(Context context, AttributeSet attrSet){
        super(context, attrSet);
    }

    public void setColor(String color){
        if(color == "blue")
            mPaintPreference.setColor(getResources().getColor(R.color.colorBlue));
        else if(color == "green")
            mPaintPreference.setColor(getResources().getColor(R.color.colorGreen));
        else if(color == "red")
            mPaintPreference.setColor(getResources().getColor(R.color.colorRed));
        else if(color == "black")
            mPaintPreference.setColor(getResources().getColor(R.color.colorBlack));
    }

    public void setAppMode(String mode){
        appMode = mode;
        deleteModeInitialPoint = new PointF();
        moveModeInitialPoint = new PointF();
        if(appMode == "move") {
            velocityTracker = null;
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        currentPoint = new PointF(event.getX(), event.getY());
        String actionName = "";
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchAction = "DOWN";
                if(appMode == "draw") {
                    mCurrentCircle = new Circle(currentPoint);
                    Log.d("DrawAct1", String.valueOf(mPaintPreference.getColor()));
                    mCurrentCircle.setPaintColor(mPaintPreference.getColor());
                }else if(appMode == "move"){
                    velocityTracker = VelocityTracker.obtain();
                    velocityTracker.addMovement(event);
                    moveModeInitialPoint = currentPoint;
                    Log.d("DrawMove", "DOWN called");
                    invalidate();
                }else if(appMode == "delete"){
                    deleteModeInitialPoint = currentPoint;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                touchAction = "MOVE";
                if(appMode == "draw"){
                    if(mCurrentCircle != null) {
                        mCurrentCircle.setCurrent(currentPoint);
                        mCircleList.add(mCurrentCircle);
                        invalidate();
                    }
                }else if(appMode == "move"){
                    //invalidate();
                    velocityTracker.addMovement(event);
                    //velocityTracker.computeCurrentVelocity(1);
                    //xVelocity = velocityTracker.getXVelocity();
                    //yVelocity = velocityTracker.getYVelocity();
                }
                break;
            case MotionEvent.ACTION_UP:
                touchAction = "UP";
                if(appMode == "draw"){
                    if(mCurrentCircle != null) {
                        mCurrentCircle.setCurrent(currentPoint);
                        mCircleList.add(mCurrentCircle);
                        invalidate();
                    }
                }else if(appMode == "move"){
                    velocityTracker.addMovement(event);
                    velocityTracker.computeCurrentVelocity(1);
                    xVelocity = velocityTracker.getXVelocity();
                    yVelocity = velocityTracker.getYVelocity();
                    //Log.d("DrawActMove", "X-up: " + String.valueOf(xVelocity));
                    //Log.d("DrawActMove", "Y-up: " + String.valueOf(yVelocity));
                    /*moveTimer = new Timer();
                    moveTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                        }
                    }, 100, 100);*/
                    invalidate();
                } else if(appMode == "delete"){
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mCurrentCircle = null;
                velocityTracker.clear();
                break;
            default:
                //        actionName = "OTHER - DEFAULT";
                //        Log.d("DrawAct", actionName);
                mCurrentCircle = null;
        }
        //Log.d("DrawAct", actionName + " " + currentPoint.x + " " + currentPoint.y);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas){
        Iterator<Circle> mCircleListIterator = mCircleList.iterator();
        while (mCircleListIterator.hasNext()) {
            Circle circle = mCircleListIterator.next();
            centerX = circle.getOrigin().x;
            centerY = circle.getOrigin().y;

            if(appMode == "move"){
                Log.d("DrawMove", touchAction);
                if(touchAction == "UP"){
                    float distance = (float) (Math.sqrt(Math.pow((moveModeInitialPoint.x - centerX), 2) + Math.pow((moveModeInitialPoint.y - centerY), 2)));
                    if(distance <= circle.getRadius() && !circle.getIsMovable()){
                        circle.setIsMovable(true);
                        circle.setXVelocity(0);
                        circle.setYVelocity(0);
                        Log.d("DrawMove", "Set movable - with 0 velocity");
                    }else if(!circle.getIsMovable()){
                        circle.setIsMovable(false);
                        Log.d("DrawMove", "Steady item");
                    }
                    if(!circle.getIsMoving() && circle.getIsMovable()) {
                        circle.setXVelocity(xVelocity);
                        circle.setYVelocity(yVelocity);
                        Log.d("DrawMove", "Set velocity: " + String.valueOf(xVelocity) + " : " + String.valueOf(yVelocity));
                        circle.setIsMoving(true);
                        Log.d("DrawMove", "Set Is moving");
                    }
                }
            }else{
                circle.setIsMoving(false);
                circle.setIsMovable(false);
            }

            if(appMode == "delete"){
                float initialPointDistance = (float) (Math.sqrt(Math.pow((deleteModeInitialPoint.x - centerX), 2) + Math.pow((deleteModeInitialPoint.y - centerY), 2)));
                float endPointDistance = (float) (Math.sqrt(Math.pow((currentPoint.x - centerX), 2) + Math.pow((currentPoint.y - centerY), 2)));
                if(initialPointDistance <= circle.getRadius() && endPointDistance <= circle.getRadius()) {
                    mCircleListIterator.remove();
                    continue;
                }
            }

            if(appMode == "move"
                    && circle.getIsMoving()){
                float circleXVelocity = circle.getXVelocity();
                float circleYVelocity = circle.getYVelocity();
                //Log.d("DrawMoving", "Get velocity: " + String.valueOf(circleXVelocity) + " : " + String.valueOf(circleYVelocity));
                radius = circle.getRadius();
                if(centerX - radius + circleXVelocity <= 0 || centerX + radius + circleXVelocity >= canvas.getWidth()) {
                    circleXVelocity = -circleXVelocity;
                    circle.setXVelocity(circleXVelocity);
                }else if(centerY - radius + circleYVelocity <= 0 || centerY + radius + circleYVelocity >= canvas.getHeight()) {
                    circleYVelocity = -circleYVelocity;
                    circle.setYVelocity(circleYVelocity);
                }
                circle.setOrigin(new PointF(centerX + circleXVelocity, centerY + circleYVelocity));
                //Log.d("DrawMove", "Draw moving circle");
            }else{
                endPointX = circle.getCurrent().x;
                endPointY = circle.getCurrent().y;
                if (centerX == endPointX && centerY == endPointY) {
                    radius = DEFAULT_RADIUS;
                }
                else
                    radius = (float) (Math.sqrt(Math.pow((endPointX - centerX), 2) + Math.pow((endPointY - centerY), 2)));
                if(centerX - radius >= 0
                        && centerY - radius >= 0
                        && centerX + radius <= canvas.getWidth()
                        && centerY + radius <= canvas.getHeight())
                    circle.setRadius(radius);
                else
                    radius = circle.getRadius();
            }
            mPaintPreference.setColor(circle.getPaintColor());
            canvas.drawCircle(circle.getOrigin().x, circle.getOrigin().y, radius, mPaintPreference);
        }
        touchAction = "";
        if(appMode == "move")
            invalidate();
    }
}

Circle class:
        package com.example.sagar.drawingapplication;

        import android.graphics.PointF;

/**
 * Created by Sagar on 2/19/2017.
 */

public class Circle {
    protected PointF mOrigin;
    protected PointF mCurrent;
    protected float radius;
    protected int paintColor;
    protected float xVelocity;
    protected float yVelocity;
    protected boolean isMovable;
    protected boolean isMoving;

    public Circle() {}

    public Circle(PointF point){
        mOrigin = point;
        mCurrent = point;
        radius = 0f;
        isMovable = false;
        isMoving = false;
        xVelocity = 0f;
        yVelocity= 0f;
    }

    public PointF getOrigin(){
        return mOrigin;
    }

    public void setOrigin(PointF point){
        mOrigin = point;
    }

    public PointF getCurrent(){
        return mCurrent;
    }

    public void setCurrent(PointF point){
        mCurrent = point;
    }

    public float getRadius(){
        return radius;
    }

    public void setRadius(float circleRadius){
        radius = circleRadius;
    }

    public int getPaintColor(){
        return paintColor;
    }

    public void setPaintColor(int color){
        paintColor = color;
    }

    public boolean getIsMovable(){
        return isMovable;
    }

    public void setIsMovable(boolean isInMovableRegion){
        isMovable = isInMovableRegion;
    }

    public float getXVelocity(){
        return xVelocity;
    }

    public void setXVelocity(float xVelocityValue){
        xVelocity = xVelocityValue;
    }

    public float getYVelocity(){
        return yVelocity;
    }

    public void setYVelocity(float yVelocityValue){
        yVelocity = yVelocityValue;
    }

    public boolean getIsMoving(){
        return isMoving;
    }

    public void setIsMoving(boolean isAlreadyMoving){
        isMoving = isAlreadyMoving;
    }
}

    String resource:
<resources>
<string name="app_name">DrawingApp</string>
<string name="mode">Mode</string>
<string name="drawing_mode">Draw</string>
<string name="moving_mode">Move</string>
<string name="delete_mode">Delete</string>
<string name="color">Color</string>
<string name="black_color">Black</string>
<string name="blue_color">Blue</string>
<string name="green_color">Green</string>
<string name="red_color">Red</string>
<string name="draw_toast">Entering draw mode!</string>
<string name="move_toast">Entering moving mode!</string>
<string name="delete_toast">Entering delete mode!</string>
<string name="color_update_toast">Color choice updated!</string>
</resources>

        res/values/Colors xml:

<?xml version="1.0" encoding="utf-8"?>
<resources>
<color name="colorPrimary">#3F51B5</color>
<color name="colorPrimaryDark">#303F9F</color>
<color name="colorAccent">#FF4081</color>
<color name="colorBlack">#FF000000</color>
<color name="colorBlue">#FF0000FF</color>
<color name="colorGreen">#FF00FF00</color>
<color name="colorRed">#FFFF0000</color>
</resources>