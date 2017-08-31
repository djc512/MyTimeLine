package martin.mytimeline;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private View line;
    private TextView Tvtime;
    private MyScrollView sv;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        initView();
        setListener();
    }

    private void setListener() {
        sv.setOnTimeChangeListener(new MyScrollView.OnTimeChangeListener() {
            @Override
            public void onTimeChange(String time) {
                Tvtime.setText(time);
            }
        });
    }

    private void initView() {
        line = findViewById(R.id.line);
        Tvtime = (TextView) findViewById(R.id.time);
        sv = (MyScrollView) findViewById(R.id.sv);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) line.getLayoutParams();
        params.height = Utils.dip2px(this, 1);
        params.topMargin = Utils.dip2px(this, 60);
        line.setLayoutParams(params);

        sv.setData(width);
    }
}
