package sidev17.siits.proshare.Utils;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class ScaleGesture implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {
    private View view;
    private ScaleGestureDetector gestureScale;
    private float scaleFactor = 1;
    private boolean inScale;

    private boolean zoom = false;
    private boolean ukuranAsli= true;

    private float coorX= -1;
    private float coorY= -1;


    public ScaleGesture(Context c){
        gestureScale = new ScaleGestureDetector(c, this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        this.view = view;
        if(coorX == -1)
            coorX= view.getX();
        if(coorY == -1)
            coorY= view.getY();
        gestureScale.onTouchEvent(event);

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(ukuranAsli){
                if(zoom){
                    zoomIn();
                    ukuranAsli= false;
                    zoom = false;
                } else {
                    zoom = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            zoom = false;
                        }
                    }, 1000);
                }
            } else{
                if(zoom){
                    zoomOut();
                    ukuranAsli= true;
                    zoom = false;
                } else {
                    zoom = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            zoom = false;
                        }
                    }, 1000);
                }
            }
        }
        else if(!ukuranAsli && event.getAction() == MotionEvent.ACTION_MOVE){
            view.setTranslationX(event.getRawX());
            view.setTranslationY(event.getRawY());
        }
        return true;
    }

    private AksiZoom pngAksiZoom;
    public interface AksiZoom{
        void zoomIn(View v, float scale);
        void zoomOut(View v, float scale);
    }
    public void aturAksiZoom(AksiZoom png){
        pngAksiZoom= png;
    }

    public void zoomOut(){
        view.setX(coorX);
        view.setY(coorY);
        view.setScaleX(1);
        view.setScaleY(1);
        if(pngAksiZoom != null)
            pngAksiZoom.zoomOut(view, 1);
    }
    public void zoomIn(){
        view.setScaleX(5);
        view.setScaleY(5);
        if(pngAksiZoom != null)
            pngAksiZoom.zoomIn(view, 20);
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();
        scaleFactor = (scaleFactor < 1 ? 1 : scaleFactor); // prevent our view from becoming too small //
        scaleFactor = ((float)((int)(scaleFactor * 100))) / 100; // Change precision to help with jitter when user just rests their fingers //
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
        if(pngAksiZoom != null && scaleFactor > 1)
            pngAksiZoom.zoomIn(view, scaleFactor);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        inScale = true;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        inScale = false;
    }
}

