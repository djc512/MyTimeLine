package martin.mytimeline;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by admin on 2017/8/29.
 */

public class MyScrollView extends ScrollView {
    private int unit = Utils.dip2px(getContext(), 60);//每个时间段的高度
    private int minuteUnit = Utils.dip2px(getContext(), 1);
    private int secondUnit = Utils.dip2px(getContext(), (float) 0.017);
    //当前的时间
    private int hour = 12;
    private int minute;//
    private int second;

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
        Log.i("MyScrollView", "unit:" + unit + "minuteUnit:" + minuteUnit + "secondUnit:" + secondUnit);

        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        for (int i = 23; i > -1; i--) {
            TextView tv = new TextView(getContext());
            tv.setText(i + "");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, Utils.dip2px(getContext(), 20));
            params.topMargin = unit;
            tv.setLayoutParams(params);
            ll.addView(tv);
        }
        addView(ll);


//        Calendar calendar = Calendar.getInstance();
//        hour = calendar.get(Calendar.HOUR_OF_DAY);
//        minute = calendar.get(Calendar.MINUTE);
//        second = calendar.get(Calendar.SECOND);
//        moveByTime(hour, minute, second);
        moveByTime(22, 15, second);
    }

    /**
     * 根据时间滑动到指定位置
     *
     * @param hour
     * @param minute
     * @param second 默认为30s,便于计算
     */
    private void moveByTime(final int hour, final int minute, int second) {
        //延迟一点滚动，防止界面未绘画完成就已经滚动了
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //此时小时应该滚动的位置为
                int hourPos = Utils.dip2px(getContext(), 60 * (24 - hour) + 10 * (23 - hour) + 10);
//                int minutePos = Utils.dip2px(getContext(), minute);

                currentPos = hourPos - Utils.dip2px(getContext(), 110);//当前的位置
                scrollTo(0, currentPos);
            }
        }, 200);
    }

    private int currentHour;
    private int currentMinute;
    private int currentPos;

    /**
     * @param l
     * @param scrollYPos  当前在y轴上的位置
     * @param oldl
     * @param oscrollYPos 移动前在y轴上的距离
     */
    @Override
    protected void onScrollChanged(int l, int scrollYPos, int oldl, int oscrollYPos) {
        super.onScrollChanged(l, scrollYPos, oldl, oscrollYPos);

        int pxDistancePos = scrollYPos - currentPos;
        int scrollMinute = pxDistancePos / 3;//滚动的分钟
        int startMinute;
        if (pxDistancePos > 0) {//向上滚动，时间变小
            if (pxDistancePos <= unit) {//说明滑动不到一个小时,即为当前的分钟数
                currentMinute = 60 - scrollMinute;//当前的分钟

                if (currentMinute == 60) {
                    currentHour = hour - 1;
                } else if (currentMinute == 0) {//说明当前时间已经划到下一个时间点,重置当前位置
                    hour = currentHour;
                    currentMinute = 60;
                    currentPos = scrollYPos;

                }
                ToastUtil.show(getContext(), hour + ":" + currentMinute);

            } else {


            }
        } else {//向下滚动，时间变大

        }
    }
}
