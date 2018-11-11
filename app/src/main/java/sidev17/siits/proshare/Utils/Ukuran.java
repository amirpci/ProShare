package sidev17.siits.proshare.Utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;


public class Ukuran {
    public static int DpKePx(int dp, DisplayMetrics dm){
        int px= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
        return px;
    }
    public static int PxKeDp(int px, DisplayMetrics dm){
        int dp= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, dm);
        return dp;
    }

    public static int SpKePx(int sp, DisplayMetrics dm){
        int px= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, dm);
        return px;
    }
    public static int PxKeSp(int px, DisplayMetrics dm){
        int sp= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, dm);
        return sp;
    }
}
