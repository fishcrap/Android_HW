package com.bytedance.camp.chapter4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.nfc.Tag;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClockView extends View {

    private static final int FULL_CIRCLE_DEGREE = 360;
    private static final int UNIT_DEGREE = 6;

    private static final float UNIT_LINE_WIDTH = 8; // 刻度线的宽度
    private static final int HIGHLIGHT_UNIT_ALPHA = 0xFF;
    private static final int NORMAL_UNIT_ALPHA = 0x80;

    private static final float HOUR_NEEDLE_LENGTH_RATIO = 0.4f; // 时针长度相对表盘半径的比例
    private static final float MINUTE_NEEDLE_LENGTH_RATIO = 0.6f; // 分针长度相对表盘半径的比例
    private static final float SECOND_NEEDLE_LENGTH_RATIO = 0.8f; // 秒针长度相对表盘半径的比例
    private static final float HOUR_NEEDLE_WIDTH = 12; // 时针的宽度
    private static final float MINUTE_NEEDLE_WIDTH = 8; // 分针的宽度
    private static final float SECOND_NEEDLE_WIDTH = 4; // 秒针的宽度

    private Calendar calendar = Calendar.getInstance();

    private float radius = 0; // 表盘半径
    private float centerX = 0; // 表盘圆心X坐标
    private float centerY = 0; // 表盘圆心Y坐标

    private List<RectF> unitLinePositions = new ArrayList<>();
    private Paint unitPaint = new Paint();
    private Paint needlePaint = new Paint();
    private Paint numberPaint = new Paint();
    private Paint circlePaint = new Paint();

    private Handler handler = new Handler();

    private final String TAG = "tag";

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        unitPaint.setAntiAlias(true);
        unitPaint.setColor(Color.WHITE);
        unitPaint.setStrokeWidth(UNIT_LINE_WIDTH);
        unitPaint.setStrokeCap(Paint.Cap.ROUND);
        unitPaint.setStyle(Paint.Style.STROKE);

        // TODO 设置绘制时、分、秒针的画笔: needlePaint
        needlePaint.setAntiAlias(true);
        needlePaint.setColor(Color.WHITE);
        needlePaint.setStyle(Paint.Style.FILL);
        needlePaint.setStrokeWidth(10f);

        // TODO 设置绘制时间数字的画笔: numberPaint
        numberPaint.setAntiAlias(true);
        numberPaint.setColor(Color.WHITE);
        numberPaint.setStyle(Paint.Style.FILL);
        numberPaint.setStrokeWidth(10f);
        numberPaint.setTextAlign(Paint.Align.CENTER);
        numberPaint.setTextSize(90f);

        //paint circle in the center of the clock
        circlePaint.setAntiAlias(true);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        configWhenLayoutChanged();
    }

    private void configWhenLayoutChanged() {
        float newRadius = Math.min(getWidth(), getHeight()) / 2f;
        if (newRadius == radius) {
            return;
        }
        radius = newRadius;
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;

        // 当视图的宽高确定后就可以提前计算表盘的刻度线的起止坐标了
        for (int degree = 0; degree < FULL_CIRCLE_DEGREE; degree += UNIT_DEGREE) {
            double radians = Math.toRadians(degree);
            float startX = (float) (centerX + (radius * (1 - 0.05f)) * Math.cos(radians));
            float startY = (float) (centerX + (radius * (1 - 0.05f)) * Math.sin(radians));
            float stopX = (float) (centerX + radius * Math.cos(radians));
            float stopY = (float) (centerY + radius * Math.sin(radians));
            unitLinePositions.add(new RectF(startX, startY, stopX, stopY));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawUnit(canvas);
        drawTimeNeedles(canvas);
        drawTimeNumbers(canvas);
        drawCircle(canvas);
        // TODO 实现时间的转动，每一秒刷新一次
        handler.postAtTime(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 1000L);
    }

    private void drawCircle(Canvas canvas) {
        float circle_radius = radius * 0.05f;
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX,centerY,circle_radius,circlePaint);

        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(radius * 0.03f);
        canvas.drawCircle(centerX,centerY,circle_radius,circlePaint);
    }

    // 绘制表盘上的刻度
    private void drawUnit(Canvas canvas) {
        for (int i = 0; i < unitLinePositions.size(); i++) {
            if (i % 5 == 0) {
                unitPaint.setAlpha(HIGHLIGHT_UNIT_ALPHA);
            } else {
                unitPaint.setAlpha(NORMAL_UNIT_ALPHA);
            }
            RectF linePosition = unitLinePositions.get(i);
            //canvas.drawLine(linePosition.left, linePosition.top, linePosition.right, linePosition.bottom, unitPaint);
            //paint unit in circle-shape
            unitPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle((linePosition.left + linePosition.right) / 2, (linePosition.top + linePosition.bottom) / 2, radius * 0.02f ,unitPaint);
        }
    }

    private void drawTimeNeedles(Canvas canvas) {
        Time time = getCurrentTime();
        int hour = time.getHours();
        int minute = time.getMinutes();
        int second = time.getSeconds();
        // TODO 根据当前时间，绘制时针、分针、秒针
        /**
         * 思路：
         * 1、以时针为例，计算从0点（12点）到当前时间，时针需要转动的角度
         * 2、根据转动角度、时针长度和圆心坐标计算出时针终点坐标（起始点为圆心）
         * 3、从圆心到终点画一条线，此为时针
         * 注1：计算时针转动角度时要把时和分都得考虑进去
         * 注2：计算坐标时需要用到正余弦计算，请用Math.sin()和Math.cos()方法
         * 注3：Math.sin()和Math.cos()方法计算时使用不是角度而是弧度，所以需要先把角度转换成弧度，
         *     可以使用Math.toRadians()方法转换，例如Math.toRadians(180) = 3.1415926...(PI)
         * 注4：Android视图坐标系的0度方向是从圆心指向表盘3点方向，指向表盘的0点时是-90度或270度方向，要注意角度的转换
         */
        // int hourDegree = 180;
        // float endX = (float) (centerX + radius * HOUR_NEEDLE_LENGTH_RATIO * Math.cos(Math.toRadians(hourDegree)))
        final int hour_base = 12;
        final int minute_base = 60;
        final int second_base = 60;

        double secondDegree = second * 1.0 / second_base * FULL_CIRCLE_DEGREE - 90;
        double minuteDegree = (minute + second * 1.0 / second_base) * 1.0 / minute_base * FULL_CIRCLE_DEGREE - 90;
        double hourDegree = (hour + minute * 1.0 / minute_base + second * 1.0 / minute_base / second_base ) * 1.0 / hour_base * FULL_CIRCLE_DEGREE - 90;

        float startX = centerX;
        float startY = centerY;

        float stopX = (float) (centerX + radius * HOUR_NEEDLE_LENGTH_RATIO * Math.cos(Math.toRadians(hourDegree)));
        float stopY = (float) (centerY + radius * HOUR_NEEDLE_LENGTH_RATIO * Math.sin(Math.toRadians(hourDegree)));
        needlePaint.setColor(Color.WHITE);
        needlePaint.setStrokeWidth(HOUR_NEEDLE_WIDTH);
        canvas.drawLine(startX,startY,stopX,stopY,needlePaint);

        stopX = (float) (centerX + radius * MINUTE_NEEDLE_LENGTH_RATIO * Math.cos(Math.toRadians(minuteDegree)));
        stopY = (float) (centerY + radius * MINUTE_NEEDLE_LENGTH_RATIO * Math.sin(Math.toRadians(minuteDegree)));
        needlePaint.setColor(Color.WHITE);
        needlePaint.setStrokeWidth(MINUTE_NEEDLE_WIDTH);
        canvas.drawLine(startX,startY,stopX,stopY,needlePaint);

        stopX = (float) (centerX + radius * SECOND_NEEDLE_LENGTH_RATIO * Math.cos(Math.toRadians(secondDegree)));
        stopY = (float) (centerY + radius * SECOND_NEEDLE_LENGTH_RATIO * Math.sin(Math.toRadians(secondDegree)));
        needlePaint.setColor(Color.GRAY);
        needlePaint.setStrokeWidth(SECOND_NEEDLE_WIDTH);
        canvas.drawLine(startX,startY,stopX,stopY,needlePaint);
        Log.i(TAG,"Current_Hour:" + hour);
        Log.i(TAG,"Current_Minute:" + minute);
        Log.i(TAG,"Current_Second:" + second);
    }

    private void drawTimeNumbers(Canvas canvas) {
        // TODO 绘制表盘时间数字（可选）
        final String[] number = new String[]{"12", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"};
        Paint.FontMetrics fontMetrics = numberPaint.getFontMetrics();
        final float halfHeight = (fontMetrics.bottom - fontMetrics.top) / 2;
        final float halfWidth = numberPaint.measureText(number[0]) / 2;
        final float baseline_center_distance = (fontMetrics.bottom + fontMetrics.top) / 2;
        final float number_center_distance_ratio = 0.90f - Math.max(halfHeight, halfWidth) / radius; // from cycle center to number center
        final float number_radius = radius * number_center_distance_ratio;
        float x = centerX;
        float y = centerY;
        for(int i = 0; i < 12; ++i) {
            canvas.drawText(
                    number[i],
                    x + (float) (number_radius * Math.cos(Math.PI * i / 6 - Math.PI / 2)),
                    y + (float) (number_radius * Math.sin(Math.PI * i / 6 - Math.PI / 2) - baseline_center_distance),
                    numberPaint);
        }
//        canvas.drawText("03",x + number_radius + fontMetrics.bottom,y,numberPaint);

    }

    // 获取当前的时间：时、分、秒
    private Time getCurrentTime() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        return new Time(
                calendar.get(Calendar.HOUR),    //12 hour
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
    }
}
