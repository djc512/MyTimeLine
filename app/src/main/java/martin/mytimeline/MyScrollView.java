package martin.mytimeline;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by admin on 2017/8/29.
 */

public class MyScrollView extends ScrollView {

    //当前的时间

    private int currentPos;
    private Context ctx;
    private int secondPos;
    private String moveTime;
    private int unit;
    private String TAG = "MyScrollView";
    private int startPos;
    private int endPos;
    private int disPos;
    private int ivWidth;
    private int dis;
    private String time = "2017-08-29 15:00:00";

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
        //每个时间段的高度
        unit = Utils.dip2px(ctx, 60);
        dis = Utils.dip2px(ctx, 90);//指示线距离顶部的高度

        final FrameLayout fl = new FrameLayout(ctx);
        LinearLayout llTv = new LinearLayout(ctx);
        llTv.setOrientation(LinearLayout.VERTICAL);
        for (int i = 23; i > -1; i--) {
            TextView tv = new TextView(ctx);
            tv.setText(i + "");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, Utils.dip2px(ctx, 20));
            params.topMargin = unit;
            tv.setLayoutParams(params);
            llTv.addView(tv);
        }
        fl.addView(llTv);


        //设置开始时间 14:00:00
        startPos = moveByTime(14, 20, 00);//设置开始时间
        endPos = moveByTime(14, 40, 0);//设置结束时间
        disPos = Math.abs(startPos - endPos);

        ivWidth = Utils.dip2px(ctx, 120);//设置片段的宽度

        ImageView iv = new ImageView(ctx);
        iv.setBackgroundResource(R.color.colorAccent);
        LayoutParams ivParams = new LayoutParams(ivWidth, disPos);//设置区段的宽高
        ivParams.leftMargin = ivWidth;//设置距离左边距的位置
        ivParams.topMargin = endPos + dis;//设置距离顶部的高低
        iv.setLayoutParams(ivParams);
        fl.addView(iv);


        int startPos1 = moveByTime(17, 30, 00);
        int endPos1 = moveByTime(17, 50, 00);
        int disPos1 = Math.abs(startPos1 - endPos1);

        Log.i(TAG, "startPos1:" + startPos1);
        ImageView iv1 = new ImageView(ctx);
        iv1.setBackgroundResource(R.mipmap.android);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        LayoutParams ivParams1 = new LayoutParams(ivWidth, disPos1);
        ivParams1.leftMargin = ivWidth;
        ivParams1.topMargin = endPos1 + dis;
        iv1.setLayoutParams(ivParams1);
        fl.addView(iv1);

        addView(fl);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //时间2017-08-29 15:00:00，默认的当前时间
                final int pos = moveByTime(15, 0, 0);
                scrollTo(0, pos);
            }
        }, 200);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 根据时间计算要滚动的位置
     */
    private int moveByTime(int hour, int minute, int second) {
        //此时小时应该滚动的位置为
        int hourPos = Utils.dip2px(ctx, 60 * (24 - hour) + 20 * (23 - hour) + 10);
        int minutePos = Utils.dip2px(ctx, 80) / 60 * minute;
        secondPos = Utils.dip2px(ctx, 80) / 3600 * second;
        currentPos = hourPos - minutePos - secondPos - dis;


        return currentPos;
    }

    @Override
    protected void onScrollChanged(int l, int scrollY, int oldl, int oScrollY) {
        setTimeByPosition();
    }

    /**
     * 根据距离计算时间
     */
    private void setTimeByPosition() {
        long milliTime = Utils.dateToStamp(time);//获取当前时间的时间戳

        int yscroll = getScrollY();//获取当前滚动的位置
        long disPos = yscroll - currentPos;
        long moveMillinTime = disPos * 3600 * 1000 / (Utils.dip2px(ctx, 80));//移动的毫秒数
        long disTime = milliTime - moveMillinTime;
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

    private int width;

    public void setWidth(int width) {

    }
}
