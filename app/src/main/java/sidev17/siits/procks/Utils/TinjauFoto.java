package sidev17.siits.procks.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TinjauFoto extends ViewPager {
    private Context konteks;
    private boolean bisaDidrag= true;

    public TinjauFoto(Context k){
        super(k);
        konteks= k;
    }
    public TinjauFoto(@NonNull Context k, @Nullable AttributeSet attrs){
        super(k, attrs);
    }

    public void bisaDidrag(boolean bisa){
        bisaDidrag= bisa;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) & bisaDidrag;
    }
}
