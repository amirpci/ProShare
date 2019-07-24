package sidev17.siits.procks.Utils.ViewTool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;

public class BitmapHandler {
    private Bitmap bm;
    private PenungguProses pngProses;
    private AsyncTask<ImageView, Void, ImageView> penugas;
    private ImageView img;
    private boolean terpasang= false;

    public BitmapHandler(){
        this(null);
    }
    public BitmapHandler(Bitmap sumber){
        bm= sumber;
        buatProses();
    }

    public interface PenungguProses{
        void proses(BitmapHandler handler);
        void selesai(BitmapHandler handler, Bitmap bm, boolean tuntas);
    }
    public void aturPenungguProses(PenungguProses png){
        pngProses= png;
    }

    public Bitmap bitmap(){
        return bm;
    }
    public void isi(Bitmap sumber){
        bm= sumber;
    }

    public void pasangKeImage(){
        if(img == null)
            return;
        if(!terpasang){
            if(penugas.getStatus() == AsyncTask.Status.RUNNING
                    || penugas.getStatus() == AsyncTask.Status.FINISHED)
                hentikanProses();
            penugas.execute(img);
        }
    }
    public boolean pasangKeImage(ImageView img){
        if(img == null || this.img == img)
            return false;
        this.img= img;

        if(penugas.getStatus() == AsyncTask.Status.RUNNING
                || penugas.getStatus() == AsyncTask.Status.FINISHED){
            hentikanProses();
            buatProses();
        }
        penugas.execute(img);

        return bm != null;
    }

    //proses pemasangan bitmap
    public void hentikanProses(){
        penugas.cancel(true);
    }
    public void buatProses(){
        penugas= new AsyncTask<ImageView, Void, ImageView>(){
            @Override
            protected ImageView doInBackground(ImageView... imgs) {
                while(bm == null);
                return imgs[0];
            }

            @Override
            protected void onPostExecute(ImageView imgDalam) {
                img.setImageBitmap(bm);
                terpasang= true;
            }
        };
    }

    public Bitmap isiBitmap(String pathFoto, int ukuranPokok, int jenisFoto, int bentukFoto){
        File file= new File(pathFoto);
        Bitmap bm= null;
        boolean tuntas= false;
        if(pngProses != null)
            pngProses.proses(this);

        if(jenisFoto == GaleriLoader.JENIS_FOTO) {
            if(file.length() /1024 < (8*1024) && file.length() < Runtime.getRuntime().totalMemory()/*batasMemori[posisi % batasBuffer]*/)
                bm = BitmapFactory.decodeFile(pathFoto);
        }
        else if(jenisFoto == GaleriLoader.JENIS_VIDEO_THUMBNAIL)
            bm= ThumbnailUtils.createVideoThumbnail(pathFoto, MediaStore.Video.Thumbnails.MINI_KIND);


        if(bm != null) {
            bm = skalaFoto(bm, ukuranPokok);
            if (bentukFoto == GaleriLoader.BENTUK_KOTAK)
                bm = kropFotoKotak(bm);
            tuntas= true;
        }
        this.bm= bm;
        if(pngProses != null)
            pngProses.selesai(this, this.bm, tuntas);

        return bm;
    }

    private Bitmap skalaFoto(Bitmap bm, int ukuranPokok){
        int pjg= bm.getWidth();
        int lbr= bm.getHeight();

        int ukuranTerpanjang= (pjg < lbr) ? pjg : lbr;
        if(ukuranTerpanjang < ukuranPokok)
            ukuranPokok = ukuranTerpanjang;
        float skala= (float) ukuranPokok /ukuranTerpanjang;

        int pjgBaru= Math.round(pjg *skala);
        int lbrBaru= Math.round(lbr *skala);

        Bitmap fotoBaru= Bitmap.createScaledBitmap(bm, pjgBaru, lbrBaru, false);

        return fotoBaru;
    }

    private Bitmap kropFotoKotak(Bitmap bm){
        int pjg= bm.getWidth();
        int lbr= bm.getHeight();

        if(lbr < pjg)
            pjg= lbr;

        int mulaiX= bm.getWidth()/2 - pjg/2;
        int mulaiY= bm.getHeight()/2 - lbr/2;

        return Bitmap.createBitmap(bm, mulaiX, mulaiY, pjg, pjg);
    }
}
