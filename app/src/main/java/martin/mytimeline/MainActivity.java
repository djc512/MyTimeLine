package martin.mytimeline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private View line;
    private TextView Tvtime;
    private MyScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        params.topMargin = Utils.dip2px(this, 90);
        line.setLayoutParams(params);

    }
}
