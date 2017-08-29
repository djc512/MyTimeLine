package martin.mytimeline;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin on 2017/8/29.
 */

public class ToastUtil {
    public static Toast toast;

    public static void show(Context ctx, String msg) {
        if (toast == null) {
            toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
