package sidev17.siits.proshare.Utils.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ImgViewTouch extends ImageView {

    private PenungguSentuhan_Internal pngSentuhanInt;
    private PenungguSentuhan pngSentuhan;
    private PenungguKlik_Internal pngKlikInt;
    private PenungguKlik pngKlik;

    public ImgViewTouch(Context konteks){
        this(konteks, null);
    }
    public ImgViewTouch(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImgViewTouch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ImgViewTouch(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        throw new UnsupportedOperationException("setOnClickListener(OnClickListener l) gak diperbolehkan di kelas ImgViewTouch." +
                "\n Gunakan \"aturPenungguKlik(PenungguKlik png)\" !");
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        throw new UnsupportedOperationException("setOnTouchListener(OnTouchListener l) gak diperbolehkan di kelas ImgViewTouch." +
                "\n Gunakan \"aturPenungguSentuhan(PenungguSentuhan png)\" !");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean diKlik= super.onTouchEvent(event);
        if(pngSentuhan != null) {
            pngSentuhan.sentuh(this, event);
            diKlik |= true;
        }
        if(pngSentuhanInt != null) {
            pngSentuhanInt.sentuh_Int(this, event);
            diKlik |= true;
        }
        return diKlik;
    }
    @Override
    public boolean performClick() {
        boolean diKlik= super.performClick();
        if(pngKlik != null) {
            pngKlik.klik(this);
            diKlik |= true;
        }
        if(pngKlikInt != null) {
            pngKlikInt.klik_Int(this);
            diKlik |= true;
        }
        return diKlik;
    }

    protected interface PenungguSentuhan_Internal{
        void sentuh_Int(View v, MotionEvent event);
    }
    public interface PenungguSentuhan{
        void sentuh(View v, MotionEvent event);
    }
    protected interface PenungguKlik_Internal{
        void klik_Int(View v);
    }
    public interface PenungguKlik{
        void klik(View v);
    }

    protected void aturPenungguSentuhan_Internal(PenungguSentuhan_Internal png){
        pngSentuhanInt= png;
    }
    public void aturPenungguSentuhan(PenungguSentuhan png){
        pngSentuhan= png;
    }
    protected void aturPenungguKlik_Internal(PenungguKlik_Internal png){
        pngKlikInt= png;
    }
    public void aturPenungguKlik(PenungguKlik png){
        pngKlik= png;
    }
}
