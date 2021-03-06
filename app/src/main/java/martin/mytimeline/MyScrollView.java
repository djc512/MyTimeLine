package martin.mytimeline;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by admin on 2017/8/29.
 */

public class MyScrollView extends ScrollView {

    private Context ctx;
    private int currentPos;
    private int secondPos;
    private String moveTime;
    private int unit;
    private int startPos;
    private int endPos;
    private int disPos;
    private int ivWidth;
    private int dis;
    private String time; //当前的时间
    private FrameLayout fl;
    private int width;//屏幕的宽度

    private int leftMargin;
    private int scaleMargin;//刻度值之间的间距，默认60
    private int scaleValue;//刻度值的大小，默认20
    private int indicateMargin;//指示线距离顶部的值，默认60
    private Calendar calendar;

    private int hour;
    private int minute;
    private int second;
    private long downTime;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            int top = 0;
            switch (what) {
                case 0:
                    top = 60;
                    break;
                case 1:
                    top = 120;
                    break;
                case 2:
                    top = 30;
                    break;
            }

            removeAllViews();
            setState(top);
        }
    };

    public MyScrollView(Context context) {
        super(context);
        init();
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ctx = getContext();
        detector = new ScaleGestureDetector(ctx, new ScaleGestureListener());
        calendar = Calendar.getInstance();

        scaleValue = 20;
        indicateMargin = 60;

//        scaleMargin = 60;
//        unit = Utils.dip2px(ctx, scaleMargin); //每个时间刻度之间的Margin值

        dis = Utils.dip2px(ctx, indicateMargin);//指示线距离顶部的高度
        leftMargin = Utils.dip2px(ctx, 10);
    }

    /**
     * 设置初始化控件的状态
     *
     * @param top
     */
    private void setState(int top) {
        fl = new FrameLayout(ctx);

        scaleMargin = top;
        unit = Utils.dip2px(ctx, scaleMargin); //每个时间刻度之间的Margin值

        setScaleLine();
        setScale();
        setTimePisode();
        setIndicate();
        addView(fl);

        invalidate();

        //延迟执行，防止刻度未画完，就已经滚动
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //时间2017-08-29 15:00:00，默认的当前时间

                final int pos = moveByTime(calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        calendar.get(Calendar.SECOND));

                scrollTo(0, pos);
            }
        }, 200);
    }

    /**
     * 设置时间刻度线
     */
    private void setScaleLine() {
        LinearLayout llLine = new LinearLayout(ctx);
        llLine.setOrientation(LinearLayout.VERTICAL);
        for (int i = 27; i > -1; i--) {
            View view = new View(ctx);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.dip2px(ctx, 10), Utils.dip2px(ctx, 1));
            if (i == 27) {
                params.topMargin = Utils.dip2px(ctx, scaleMargin + (float) (scaleValue / 2 - 0.5));//第一个位置，要减去0.5个刻度线的高度
            } else {
                params.topMargin = Utils.dip2px(ctx, scaleMargin + scaleValue - 1);//其他的位置，减去1个刻度线的高度
            }
            view.setLayoutParams(params);
            view.setBackgroundColor(Color.RED);
            llLine.addView(view);
        }
        fl.addView(llLine);
    }

    /**
     * 设置竖直指示线
     */
    private void setIndicate() {

        int indicateWidth = Utils.dip2px(ctx, 10);
        //设置开始时间 14:00:00
        startPos = moveByTime(14, 20, 00);//设置开始时间
        endPos = moveByTime(15, 40, 0);//设置结束时间
        disPos = Math.abs(startPos - endPos);

        ImageView iv = new ImageView(ctx);
        iv.setBackgroundResource(R.drawable.shape_indicate_blue);
        LayoutParams ivParams = new LayoutParams(indicateWidth, disPos);//设置区段的宽高
        ivParams.leftMargin = ivWidth;//设置距离左边距的位置
        ivParams.topMargin = endPos + dis;//设置距离顶部的高低
        iv.setLayoutParams(ivParams);
        fl.addView(iv);

        int endPos1 = moveByTime(15, 20, 0);//设置结束时间
        int disPos1 = Math.abs(startPos - endPos1);

        ImageView iv1 = new ImageView(ctx);
        iv1.setBackgroundResource(R.drawable.shape_indicate_pink);
        LayoutParams ivParams1 = new LayoutParams(indicateWidth, disPos1);//设置区段的宽高
        ivParams1.leftMargin = ivWidth + Utils.dip2px(ctx, 20);//设置距离左边距的位置
        ivParams1.topMargin = endPos1 + dis;//设置距离顶部的高低(需要加上指示线距离顶部的高度)
        iv1.setLayoutParams(ivParams1);
        fl.addView(iv1);
    }

    /**
     * 设置时间段
     */
    private void setTimePisode() {

        ivWidth = width / 4;//设置片段的宽度

        //设置开始时间 14:00:00
        startPos = moveByTime(14, 20, 00);//设置开始时间
        endPos = moveByTime(15, 40, 0);//设置结束时间
        disPos = Math.abs(startPos - endPos);

        ImageView iv = new ImageView(ctx);
        iv.setBackgroundResource(R.color.colorAccent);
        LayoutParams ivParams = new LayoutParams(ivWidth, disPos);//设置区段的宽高
        ivParams.leftMargin = ivWidth * 2;//设置距离左边距的位置
        ivParams.topMargin = endPos + dis;//设置距离顶部的高低
        iv.setLayoutParams(ivParams);
        fl.addView(iv);

        //设置开始时间
        int startPos1 = moveByTime(17, 30, 00);
        int endPos1 = moveByTime(17, 50, 00);
        int disPos1 = Math.abs(startPos1 - endPos1);

        ImageView iv1 = new ImageView(ctx);
        iv1.setBackgroundResource(R.mipmap.android);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        LayoutParams ivParams1 = new LayoutParams(ivWidth, disPos1);
        ivParams1.leftMargin = ivWidth * 2;
        ivParams1.topMargin = endPos1 + dis;
        iv1.setLayoutParams(ivParams1);
        fl.addView(iv1);
    }

    /**
     * 设置刻度
     */
    private void setScale() {
        LinearLayout llTv = new LinearLayout(ctx);
        llTv.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i > -1; i--) {
            TextView tv = new TextView(ctx);
            tv.setText(i + "");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, Utils.dip2px(ctx, scaleValue));
            params.leftMargin = leftMargin;
            params.topMargin = unit;
            tv.setLayoutParams(params);
            llTv.addView(tv);
        }

        for (int i = 23; i > -1; i--) {
            TextView tv = new TextView(ctx);
            tv.setText(i + "");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, Utils.dip2px(ctx, scaleValue));
            params.leftMargin = leftMargin;
            params.topMargin = unit;
            tv.setLayoutParams(params);
            llTv.addView(tv);
        }

        for (int i = 23; i > 20; i--) {
            TextView tv = new TextView(ctx);
            tv.setText(i + "");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, Utils.dip2px(ctx, scaleValue));
            params.leftMargin = leftMargin;
            params.topMargin = unit;
            tv.setLayoutParams(params);
            llTv.addView(tv);
        }

        fl.addView(llTv);
    }

    /**
     * 根据时间计算要滚动的位置
     */
    private int moveByTime(int hour, int minute, int second) {
        //此时小时应该滚动的位置为
//        int hourPos = Utils.dip2px(ctx, 60 * (24 - hour) + 20 * (23 - hour) + 10);
        int hourPos = Utils.dip2px(ctx, scaleMargin * (25 - hour) + scaleValue * (24 - hour) + scaleValue / 2);
        int minutePos = Utils.dip2px(ctx, scaleMargin + scaleValue) / 60 * minute;//实际一个刻度为80
        secondPos = Utils.dip2px(ctx, scaleMargin + scaleValue) / 3600 * second;
        currentPos = hourPos - minutePos - secondPos - dis;//减去指示线的Magin,保证与当前的时间保持一致

        return currentPos;
    }


    @Override
    protected void onScrollChanged(int l, int scrollY, int oldl, int oScrollY) {
        setTimeByPosition();
        // 设置暂停时间，默认当前时间
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);

        int moveByTime = moveByTime(hour, minute, second);
        Log.i("MyScrollView", "moveByTime:" + moveByTime + "scrollY:" + scrollY);
        //暂停滚动
        if (moveByTime > scrollY) {
            scrollTo(0, moveByTime);
            return;
        }
    }

    /**
     * 根据距离计算时间
     */
    private void setTimeByPosition() {
        long milliTime = Utils.dateToStamp(time);//获取当前时间的时间戳

        int yscroll = getScrollY();//获取当前滚动的位置
        long disPos = yscroll - currentPos;
        long moveMillinTime = disPos * 3600 * 1000 / (Utils.dip2px(ctx, scaleMargin + scaleValue));//移动的毫秒数
        long disTime = milliTime - moveMillinTime;//移动后的毫秒数
        moveTime = Utils.stampToDate(disTime);

        if (onTimeChangeListener != null) {
            onTimeChangeListener.onTimeChange(moveTime);
        }
    }

    public void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
        this.onTimeChangeListener = onTimeChangeListener;
    }

    private OnTimeChangeListener onTimeChangeListener;

    public interface OnTimeChangeListener {
        void onTimeChange(String time);
    }

    /**
     * 初始化数据
     *
     * @param width
     * @param time
     */
    public void setData(int width, String time) {
        this.width = width;
        this.time = time;
        handler.sendEmptyMessage(0);
    }

    private ScaleGestureDetector detector;

    public class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {

            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            float currentSpan = scaleGestureDetector.getCurrentSpan();
            float previousSpan = scaleGestureDetector.getPreviousSpan();
            Log.d("ScaleGestureListener", "currentSpan:" + currentSpan + "previousSpan:" + previousSpan);
            if (currentSpan > previousSpan) {
                handler.sendEmptyMessage(1);
                ToastUtil.show(ctx, "放大手势");
            } else if (currentSpan < previousSpan) {
                handler.sendEmptyMessage(2);

                ToastUtil.show(ctx, "缩小手势");
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);

        int action = ev.getAction();
        int pointerCount = ev.getPointerCount();
        if (pointerCount == 2) {
            return detector.onTouchEvent(ev);
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                long upTime = System.currentTimeMillis();
                if (upTime - downTime > 60) {
                    break;
                } else {
                    handler.sendEmptyMessage(0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        super.dispatchTouchEvent(ev);
        return true;
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
    }
}
