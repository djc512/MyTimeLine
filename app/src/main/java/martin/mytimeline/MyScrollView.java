package martin.mytimeline;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by admin on 2017/8/29.
 */

public class MyScrollView extends ScrollView {

    //当前的时间
    private int hour = 12;
    private int minute;//
    private int second;
    private int currentPos;
    private Context ctx;
    private int secondPos;

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
        int unit = Utils.dip2px(ctx, 60);//每个时间段的高度
        int minuteUnit = Utils.dip2px(ctx, 1);
        int secondUnit = Utils.dip2px(ctx, (float) 0.017);

        LinearLayout ll = new LinearLayout(ctx);
        ll.setOrientation(LinearLayout.VERTICAL);
        for (int i = 23; i > -1; i--) {
            TextView tv = new TextView(ctx);
            tv.setText(i + "");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, Utils.dip2px(ctx, 20));
            params.topMargin = unit;
            tv.setLayoutParams(params);
            ll.addView(tv);
        }
        addView(ll);

        //时间2017-08-29 15:10:30
        final int pos = moveByTime(15, 10, 30);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollTo(0, pos);
            }
        }, 200);
    }

    /**
     * 根据时间计算要滚动的位置
     */
    private int moveByTime(int hour, int minute, int second) {
        //此时小时应该滚动的位置为
        int hourPos = Utils.dip2px(ctx, 60 * (24 - hour) + 20 * (23 - hour) + 10);
        int minutePos = Utils.dip2px(ctx, 80) / 60 * minute;
        secondPos = Utils.dip2px(ctx, 80) / 3600 * second;
        currentPos = hourPos - minutePos - secondPos - Utils.dip2px(ctx, 110);
        return currentPos;
    }

    @Override
    protected void onScrollChanged(int l, int scrollY, int oldl, int oScrollY) {
        setTimeByPosition(scrollY);
    }

    /**
     * 根据距离计算时间
     */
    private void setTimeByPosition(int scrollY) {
        long milliTime = Utils.dateToStamp("2017-08-29 15:10:30");

        int yscroll = getScrollY();
        int disPos = yscroll - currentPos;

        int moveMillinTime = disPos * 3600 * 1000 / (Utils.dip2px(ctx, 80));//移动的毫秒数
        long disTime = milliTime - moveMillinTime;
        String moveTime = Utils.stampToDate(disTime);

        ToastUtil.show(ctx, "currentPos:" + currentPos + "yscroll:" + yscroll + "moveTime:" + moveTime);
    }
}
