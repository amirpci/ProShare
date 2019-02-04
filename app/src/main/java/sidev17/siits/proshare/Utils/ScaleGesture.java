package sidev17.siits.proshare.Utils;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

public class ScaleGesture implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {
    public static final int MODE_GESER_HORIZONTAL= 10;
    public static final int MODE_GESER_VERTIKAL= 11;
    public static final int MODE_GESER_DEFAULT= 12; //geser ke segala arah

    private View view;
    private ScaleGestureDetector gestureScale;
    private float skalaPerubah = 1;
    private boolean sedangDiskala= false;
    private boolean sedangDigeser= false;

    private Context konteks;

    private boolean zoom = false;
    private boolean ukuranAsli= true;

    private boolean bisaZoom= true;
    private boolean bisaGeser= true;
    private boolean bisaDiskala= false; //hanya untuk internal. Tidak bisa dimodifikasi dari luar

    private boolean bisaGeserX= true;
    private boolean bisaGeserY= true;
    private float xSentuhAwal;
    private float ySentuhAwal;
    private float xViewAwal;
    private float yViewAwal;
    private float xGeser;
    private float yGeser;
    private float xPusatSkala;
    private float yPusatSkala;

    private float xAsli= -1;
    private float yAsli;
/*
    private Handler handlerZoomIn;
    private Handler handlerZoomOut;
*/

    public ScaleGesture(View v, Context c){
        if(v == null)
            throw new RuntimeException("Parameter #View v gak boleh null!");
        view= v;
        konteks= c;
        gestureScale = new ScaleGestureDetector(c, this);

        view.setOnTouchListener(this);
//        handlerZoomIn= new Handler();
//        handlerZoomOut= new Handler();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        gestureScale.onTouchEvent(event);

        Rect r= new Rect();
        view.getDrawingRect(r);

        if(xAsli == -1)
            initTitikAwal();
/*
        if(pngAksiSentuh != null)
            pngAksiSentuh.sentuh(view, event);
*/
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            xSentuhAwal= event.getRawX();
            ySentuhAwal = event.getRawY();
            xViewAwal= view.getX();
            yViewAwal= view.getY();

            if(pngAksiGeser != null)
                pngAksiGeser.awalGeser(view, xSentuhAwal, ySentuhAwal);
        } else if((!ukuranAsli || !bisaZoom) && bisaGeser && event.getAction() == MotionEvent.ACTION_MOVE){
            xGeser= event.getRawX() - xSentuhAwal;
            yGeser= event.getRawY() - ySentuhAwal;

            if(Math.abs(xGeser) >= 1 || Math.abs(yGeser) >= 1)
                zoom= false;

            float xBaru= xViewAwal + xGeser;
            float yBaru= yViewAwal + yGeser;

            if(bisaGeserX)
                view.setX(xBaru);
            if(bisaGeserY)
                view.setY(yBaru);

            sedangDigeser= true;

            if(pngAksiGeser != null)
                pngAksiGeser.geser(view, xGeser, yGeser);
        } else if(event.getAction() == MotionEvent.ACTION_UP){
            if(sedangDiskala){
                sedangDiskala= false;
                bisaGeser= true;
            }
            if(bisaZoom){
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
                        zoomOut(event.getX(), event.getY());
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
            sedangDigeser= false;
            if(pngAksiGeser != null)
                pngAksiGeser.akhirGeser(view, (xViewAwal + xGeser), (yViewAwal + yGeser));
        }
/*
            else if(event.getAction() == MotionEvent.ACTION_UP) {
               Cek.toastRect(konteks, r);
            }
*/
        return true;
    }
/*
    private AksiSentuh pngAksiSentuh;
    public interface AksiSentuh{
        void sentuh(View v, MotionEvent event);
    }
    public void aturAksiSentuh(AksiSentuh png){
        pngAksiSentuh= png;
    }
*/
    private AksiGeser pngAksiGeser;
    public static abstract class AksiGeser{
        public void awalGeser(View v, float xAwal, float yAwal){}
        public abstract void geser(View v, float xGeser, float yGeser);
        //saat MotionEvent.ACTION_UP
        public void akhirGeser(View v, float xAkhir, float yAkhir){}
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
        view.setPivotX(xPusat);
        view.setPivotY(yPusat);
//        view.setX(xViewAwal);
//        view.setY(yViewAwal);
        view.setScaleX(1);
        view.setScaleY(1);
        posisiAwal();
        bisaDiskala(false, 0, 0);
        if(pngAksiZoom != null)
            pngAksiZoom.zoomOut(view, 1);
    }
    public void zoomIn(float xPusat, float yPusat, float skala){
        view.setPivotX(xPusat);
        view.setPivotY(yPusat);
        view.setScaleX(skala);
        view.setScaleY(skala);
        bisaDiskala(true, xPusat, yPusat);
        if(pngAksiZoom != null)
            pngAksiZoom.zoomIn(view, skala);
    }

    private void bisaDiskala(boolean bisa, float xPusat, float yPusat){
        bisaDiskala= bisa;
        if(bisaDiskala){
            xPusatSkala= xPusat;
            yPusatSkala= yPusat;
        }
    }

    public void posisiAwal(){
        view.setX(xAsli);
        view.setY(yAsli);
    }
    private void initTitikAwal(){
        xAsli= view.getX();
        yAsli= view.getY();
    }

    public void bisaZoom(boolean bisa){
        bisaZoom= bisa;
    }
    public boolean bisaZoom(){
        return bisaZoom;
    }

    public void bisaGeser(boolean bisa){
        bisaGeser= bisa;
    }
    public boolean bisaGeser(){
        return bisaGeser;
    }

    public void aturModeGeser(int mode){
        bisaGeserX = bisaGeserY = true;
        if(mode == MODE_GESER_HORIZONTAL)
            bisaGeserY= false;
        else if(mode == MODE_GESER_VERTIKAL)
            bisaGeserX= false;
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if(bisaZoom && bisaDiskala){
            skalaPerubah *= detector.getScaleFactor();
            skalaPerubah = (skalaPerubah < 1 ? 1 : skalaPerubah); // prevent our view from becoming too small //
            skalaPerubah = ((float)((int)(skalaPerubah * 100))) / 100; // Change precision to help with jitter when user just rests their fingers //
//        view.setPivotX();
            view.setPivotX(xPusatSkala);
            view.setPivotY(yPusatSkala);
            view.setScaleX(skalaPerubah);
            view.setScaleY(skalaPerubah);
            Toast.makeText(konteks, "skala= " +skalaPerubah, Toast.LENGTH_SHORT).show();
            if(skalaPerubah > 1) {
                ukuranAsli= false;
                if(pngAksiZoom != null)
                    pngAksiZoom.zoomIn(view, skalaPerubah);
            } else if(skalaPerubah == 1)
                ukuranAsli= true;
            return true;
        }
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        sedangDiskala = true;
        bisaGeser= false;
        skalaPerubah= view.getScaleX();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
//        sedangDiskala = false;
//        bisaGeser= true;
    }

    public boolean sedangDiskala(){
        return sedangDiskala;
    }
    public boolean sedangDigeser(){
        return sedangDigeser;
    }
}

