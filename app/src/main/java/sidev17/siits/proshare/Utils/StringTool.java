package sidev17.siits.proshare.Utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

public class StringTool {
    public static Spannable aturWarnaSpan(String str, int warna, int dari, int sampe){
        Spannable spannable = new SpannableString(str);
        return aturWarnaSpan(spannable, warna, dari, sampe);
    }
    public static Spannable aturWarnaSpan(Spannable spannable, int warna, int dari, int sampe){
        spannable.setSpan(new ForegroundColorSpan(warna), dari, sampe, 0);// set color
        return spannable;
    }

    public static Spannable aturUkuranSpan(String str, float ukuranRelatif, int dari, int sampe){
        Spannable spannable = new SpannableString(str);
        return aturUkuranSpan(spannable, ukuranRelatif, dari, sampe);
    }
    public static Spannable aturUkuranSpan(Spannable spannable, float ukuranRelatif, int dari, int sampe){
        spannable.setSpan(new RelativeSizeSpan(ukuranRelatif), dari,sampe, 0); // set size
        return spannable;
    }
}
