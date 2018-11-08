package sidev17.siits.proshare.Utils;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class ScaleGesture implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {
    private View view;
    private ScaleGestureDetector gestureScale;
    private float skalaPerubah = 1;
    private boolean sedangDiskala;

    private boolean zoom = false;
    private boolean ukuranAsli= true;

    private float xPusat= -1;
    private float yPusat;
    private float xAsal;
    private float yAsal;

    public float xSentuhAwal;
    public float ySentuhAwal;
    public float xViewAwal;
    public float yViewAwal;
    public float xGeser;
    public float yGeser;
/*
    private Handler handlerZoomIn;
    private Handler handlerZoomOut;
*/

    public ScaleGesture(Context c){
        gestureScale = new ScaleGestureDetector(c, this);
//        handlerZoomIn= new Handler();
//        handlerZoomOut= new Handler();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        this.view = view;
        gestureScale.onTouchEvent(event);

        if(xPusat == -1)
            initTitikAsal(view);

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            xSentuhAwal= event.getRawX();
            ySentuhAwal = event.getRawY();
            xViewAwal= view.getX();
            yViewAwal= view.getY();

            if(ukuranAsli){
                if(zoom){
                    zoomIn(event.getX(), event.getY(), (float) 1.5);
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
                    zoomOut(xPusat, yPusat);
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
        } else if(!ukuranAsli && event.getAction() == MotionEvent.ACTION_MOVE){
            xGeser= event.getRawX() - xSentuhAwal;
            yGeser= event.getRawY() - ySentuhAwal;

            if(Math.abs(xGeser) >= 1 || Math.abs(yGeser) >= 1)
                zoom= false;

            view.setX(xViewAwal + xGeser);
            view.setY(yViewAwal + yGeser);

            if(pngAksiGeser != null)
                pngAksiGeser.geser(view, xGeser, yGeser);
        }
        return true;
    }

    private void initTitikAsal(View v){
        xAsal= v.getX();
        yAsal= v.getY();
        xPusat= xAsal +(float) v.getWidth() / 2;
        yPusat= yAsal +(float) v.getHeight() /2;
    }

    private AksiGeser pngAksiGeser;
    public interface AksiGeser{
        void geser(View v, float xGeser, float yGeser);
    }
    public void aturAksiGeser(AksiGeser png){
        pngAksiGeser= png;
    }

    private AksiZoom pngAksiZoom;
    public interface AksiZoom{
        void zoomIn(View v, float skala);
        void zoomOut(View v, float skala);
    }
    public void aturAksiZoom(AksiZoom png){
        pngAksiZoom= png;
    }

    public void zoomOut(float xPusat, float yPusat){
//        view.setX(xViewAwal);
//        view.setY(yViewAwal);
        view.setScaleX(1);
        view.setScaleY(1);

        view.setPivotX(xPusat);
        view.setPivotY(yPusat);

        view.setX(xAsal);
        view.setY(yAsal);
        if(pngAksiZoom != null)
            pngAksiZoom.zoomOut(view, 1);
    }
    public void zoomIn(float xPusat, float yPusat, float skala){
        view.setPivotX(xPusat);
        view.setPivotY(yPusat);
        view.setScaleX(skala);
        view.setScaleY(skala);
        if(pngAksiZoom != null)
            pngAksiZoom.zoomIn(view, skala);
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        skalaPerubah *= detector.getScaleFactor();
        skalaPerubah = (skalaPerubah < 1 ? 1 : skalaPerubah); // prevent our view from becoming too small //
        skalaPerubah = ((float)((int)(skalaPerubah * 100))) / 100; // Change precision to help with jitter when user just rests their fingers //
//        view.setPivotX();
        view.setPivotY(detector.getFocusX());
        view.setPivotY(detector.getFocusY());
        view.setScaleX(skalaPerubah);
        view.setScaleY(skalaPerubah);
        if(skalaPerubah > 1) {
            ukuranAsli= false;
            if(pngAksiZoom != null)
                pngAksiZoom.zoomIn(view, skalaPerubah);
        } else if(skalaPerubah == 1)
            ukuranAsli= true;
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        sedangDiskala = true;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        sedangDiskala = false;
    }
}

