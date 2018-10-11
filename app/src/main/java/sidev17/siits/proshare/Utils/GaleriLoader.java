package sidev17.siits.proshare.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;

public class GaleriLoader {
    public static final int JENIS_FOTO= 10;
    public static final int JENIS_VIDEO_THUMBNAIL= 11;
    public static final int JENIS_VIDEO= 12;

    private int jenisFoto;

    private Context konteks;

    private AksiBuffer aksiBuffer;
    private AksiPilihFoto aksiPilihFoto;

    private String pathFoto[];
    private Bitmap bufferFoto[];
    private View bufferView[];
    private int batasBuffer;
    private int batasMaksDipilih= 10;
    private int indKelihatan[];
    private int ukuranThumbnail;
    private int ukuranPratinjau;

    private boolean modeBg= true;
    private int sumberBg= -1;
    private String warnaTintBg= "#A3A3A3";
    private String warnaBg= "#ABABAB";
    private int sumberBgTakBisa= -1;
    private String warnaTintBgTakBisa= "#A3A3A3";

    private ArrayList<Integer> sumberAksesoris;
    private ArrayList<RelativeLayout.LayoutParams> lpAksesoris;

    private int idElemenImg= -1;
    private boolean elemenImg= true;

    private AsyncTask<Integer, Integer, Bitmap> loaderThumbnail[];
    private AsyncTask<Integer, Integer, Bitmap> loader[];

    private int dipilih[];
    private ArrayList<View> viewDipilih = new ArrayList<View>();
    private ArrayList<Bitmap> bitmapDipilih= new ArrayList<Bitmap>();
    private int cursorDipilih= 0;
    private int jmlUdahDiload= -1;


    public GaleriLoader(Context k, String pathFoto[], int jmlBuffer, int jenisFoto){
        konteks= k;
        this.jenisFoto= jenisFoto;
        this.pathFoto= pathFoto;
        batasBuffer= jmlBuffer;
        inisiasiBuffer(jmlBuffer);
        ukuranPratinjau = 400;
        ukuranThumbnail = 100;
        sumberAksesoris= new ArrayList<Integer>();
        lpAksesoris= new ArrayList<RelativeLayout.LayoutParams>();
    } public GaleriLoader(Context k, String pathFoto[], int jmlBuffer, int ukuranPratinjau, int jenisFoto){
        konteks= k;
        this.jenisFoto= jenisFoto;
        this.pathFoto= pathFoto;
        batasBuffer= jmlBuffer;
        inisiasiBuffer(jmlBuffer);
        this.ukuranPratinjau = ukuranPratinjau;
        ukuranThumbnail = 100;
        sumberAksesoris= new ArrayList<Integer>();
        lpAksesoris= new ArrayList<RelativeLayout.LayoutParams>();
    }

    public interface AksiBuffer{
        void bufferThumbnail(int posisi, int jmlBuffer);
        void bufferUtama(int posisi, int jmlBuffer);
    }
    public void aturAksiBuffer(AksiBuffer a){
        aksiBuffer= a;
    }

    public interface AksiPilihFoto{
        void pilihFoto(View v, int posisi);
        void batalPilihFoto(View v, int posisi);
    }
    public void aturAksiPilihFoto(AksiPilihFoto a){
        aksiPilihFoto= a;
    }

    private void inisiasiBuffer(int jmlBuffer){
        bufferFoto= new Bitmap[jmlBuffer];
        bufferView = new View[jmlBuffer];
        loaderThumbnail = new AsyncTask[jmlBuffer];
        loader = new AsyncTask[jmlBuffer];
        dipilih= new int[pathFoto.length];
        isiViewFoto(0);
        inisiasiIndKelihatan();
    }
    private void inisiasiIndKelihatan(){
        indKelihatan= new int[batasBuffer];
        for(int i= 0; i<batasBuffer; i++)
            indKelihatan[i]= -1;
    }

    public void aturJmlBuffer(int jmlBuffer){
        batasBuffer= jmlBuffer;
    }
    public int ambilJmlBuffer(){
        return batasBuffer;
    }
    public void aturJmlBatasDipilih(int jmlDipilih){
        batasMaksDipilih= jmlDipilih;
    }
    public int ambilJmlBatasDipilih(){
        return batasMaksDipilih;
    }

    public void tambahAksesoris(int sumber, RelativeLayout.LayoutParams lp){
        sumberAksesoris.add(sumber);
        lpAksesoris.add(lp);
    }
    public void tambahAksesoris(int sumber){
        tambahAksesoris(sumber, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
    public void kurangiAksesoris(int sumberYgDihilangkan){
        int indDihilangkan= sumberAksesoris.size()-1;
        for(int i= 0; i<sumberAksesoris.size(); i++)
            if(sumberAksesoris.get(i) == sumberYgDihilangkan){
                indDihilangkan= i;
                break;
            }
        sumberAksesoris.remove(indDihilangkan);
    }
    public int ambilAksesoris(int ind){
        return sumberAksesoris.get(ind);
    }

    public void aturModeBg(boolean mode){
        modeBg= mode;
    }
    public boolean modeBgKah(){
        return modeBg;
    }

    public void aturSumberBg(int id){
        sumberBg= id;
        aturBg();
    }
    public int ambilSumberBg(){
        return sumberBg;
    }
    public void aturWarnaTintBg(String warna){
        if(sumberBg > -1)
            warnaTintBg= warna;
    }
    public String ambilWarnaTintBg(){
        return warnaTintBg;
    }

    public void aturSumberBgTakBisa(int id){
        sumberBgTakBisa= id;
    }
    public int ambilSumberBgTakBisa(){
        return sumberBgTakBisa;
    }
    public void aturWarnaTintBgTakBisa(String warna){
        warnaTintBgTakBisa= warna;
    }
    public String ambilWarnaTintBgTakBisa(){
        return warnaTintBgTakBisa;
    }

    public void aturWarnaBg(String warna){
        warnaBg= warna;
        aturBg();
    }
    public String ambilWarnaBg(){
        return warnaBg;
    }

    private void aturBg(){
        if(modeBg) {
            for (int i = 0; i < batasBuffer; i++)
                aturBg(i);
        }
    }
    private void aturBg(int ind){
        if(modeBg) {
            if (sumberBg == -1 && warnaBg.startsWith("#"))
                bufferView[ind].setBackgroundColor(Color.parseColor(warnaBg));
            else {
                bufferView[ind].setBackgroundResource(sumberBg);
                bufferView[ind].getBackground().setTint(Color.parseColor(warnaTintBg));
            }
        }
    }

    private void aturBgTakBisa(int ind){
        ImageView img;
        if(elemenImg)
            img= (ImageView) bufferView[ind];
        else
            img= bufferView[ind].findViewById(idElemenImg);

        if(sumberBgTakBisa== -1 && warnaBg.startsWith("#"))
            img.setColorFilter(Color.parseColor(warnaBg));
        else {
            img.setImageResource(sumberBgTakBisa);
            img.setColorFilter(Color.parseColor(warnaTintBgTakBisa));
        }
    }

    private void isiFileFoto(int mulai, int sebanyak, int ukuranPokok){
        File file;
        for(int i= mulai; i< mulai +sebanyak && i<pathFoto.length; i++){
            file= new File(pathFoto[pathFoto.length-1-i]);
            Bitmap bm= null;
            if(jenisFoto == JENIS_FOTO) {
                if(file.length() /1024 < (10*1024))
                    bm = BitmapFactory.decodeFile(pathFoto[i]);
            }
            else if(jenisFoto == JENIS_VIDEO_THUMBNAIL)
                bm= ThumbnailUtils.createVideoThumbnail(pathFoto[i], MediaStore.Video.Thumbnails.MICRO_KIND);

            if(bm != null) {
                bm = skalaFoto(bm, ukuranPokok);
                bm = kropFotoKotak(bm);
                bufferFoto[i % batasBuffer] = bm;
            } else
                aturBgTakBisa(i % batasBuffer);
        }
    } private void isiFileFoto(int posisi, int ukuranPokok){
        File file= new File(pathFoto[pathFoto.length-1-posisi]);
        Bitmap bm= null;
        if(jenisFoto == JENIS_FOTO) {
            if(file.length() /1024 < (10*1024))
                bm = BitmapFactory.decodeFile(pathFoto[posisi]);
        }
        else if(jenisFoto == JENIS_VIDEO_THUMBNAIL)
            bm= ThumbnailUtils.createVideoThumbnail(pathFoto[posisi], MediaStore.Video.Thumbnails.MINI_KIND);

        if(bm != null) {
            bm = skalaFoto(bm, ukuranPokok);
            bm = kropFotoKotak(bm);
            bufferFoto[posisi % batasBuffer] = bm;
        } else
            aturBgTakBisa(posisi % batasBuffer);
    }

    public void aturIdElemenImg(int id){
        idElemenImg= id;
        elemenImg= false;
    }
    public void resetIdElemenImg(){
        idElemenImg= -1;
        elemenImg= true;
    }
    public int ambilIdElemenImg(){
        return idElemenImg;
    }
    public boolean elemenImgKah(){
        return elemenImg;
    }

    private void isiViewFoto(int mulai){
        int jalan= 0;
        for(int i= mulai; i< mulai +batasBuffer; i++){
            ImageView img= new ImageView(konteks);
            bufferView[jalan]= img;
            aturBg(jalan++);
        }
    } private void isiViewFoto(View v, int posisi){
        bufferView[posisi % batasBuffer]= v;
        aturBg(posisi % batasBuffer);
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

    public String[] ambilDaftarPathFoto(){
        return pathFoto;
    }
    public String ambilPathFoto(int ind){
        return pathFoto[ind];
    } public String ambilPathFoto(Bitmap bm){
        return pathFoto[cariIndBitmap(bm)];
    }

    public int cariIndBitmap(Bitmap bm){
        Bitmap bmCek;
        for(int i= 0; i<pathFoto.length; i++){
            bmCek= BitmapFactory.decodeFile(pathFoto[i]);
            if(bmCek.sameAs(bm))
                return i;
        }
        return -1;
    }

    public Bitmap[] ambilBufferFoto(){
        return bufferFoto;
    } public Bitmap ambilBitmap(int posisi){
        return bufferFoto[posisi % batasBuffer];
    }
    public View ambilView(int posisi){
        return pilahView(posisi);
    } public View[] ambilView(){
        return bufferView;
    }

    public void aturUkuranPratinjau(int ukuranPokok){
        ukuranPratinjau = ukuranPokok;
    }
    public float ambilUkuranPratinjau(){
        return ukuranPratinjau;
    }

    public void pasangAksesoris(View v){
        if(sumberAksesoris.size() > 0) {
            ViewGroup vg = (ViewGroup) v;
            for(int i= 0; i<sumberAksesoris.size(); i++) {
                ImageView imgAksesoris = new ImageView(konteks);
                imgAksesoris.setLayoutParams(lpAksesoris.get(i));
                imgAksesoris.setImageResource(sumberAksesoris.get(i));
                vg.addView(imgAksesoris);
            }
        }
    }
    public View buatFoto(int posisi){
        return buatFoto(new ImageView(konteks), posisi);
    }
    public View buatFoto(View v, int posisi){
        int ind= posisi % batasBuffer;
        if(posisi /batasBuffer != indKelihatan[ind]){
            isiViewFoto(v, posisi);
            indKelihatan[ind]= posisi /batasBuffer;
            ImageView img;
            if(elemenImg)
                img= (ImageView) v;
            else {
                img= v.findViewById(idElemenImg);
                pasangAksesoris(v);
            }
            pasangThumbnail(img, posisi);
        }
        return pilahView(posisi);
    }
    private View pilahView(int posisi){
        if(dipilih[posisi] == 0)
            return bufferView[posisi % batasBuffer];
        else
            return viewDipilih.get(dipilih[posisi] -1);
    }
    private void pasangThumbnail(final ImageView img, final int posisi){
        final int ind= posisi % batasBuffer;
        if(loaderThumbnail[ind] != null) {
            loaderThumbnail[ind].cancel(true);
        }
        loaderThumbnail[ind]= new AsyncTask<Integer, Integer, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Integer... integers) {
                int posisi= integers[0].intValue();

                isiFileFoto(posisi, ukuranThumbnail);
                if(aksiBuffer != null)
                    aksiBuffer.bufferThumbnail(posisi, batasBuffer);
                return bufferFoto[ind];
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                img.setImageBitmap(bitmap);
                pasangFoto(img, posisi);
                super.onPostExecute(bitmap);
            }
        };
        loaderThumbnail[ind].execute(posisi);
    }
    private void pasangFoto(final ImageView img, final int posisi){
        final int ind= posisi % batasBuffer;
        if(loader[ind] != null) {
            loader[ind].cancel(true);
        }
        loader[ind]= new AsyncTask<Integer, Integer, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Integer... integers) {
                int posisi= integers[0].intValue();

                isiFileFoto(posisi, ukuranPratinjau);
                if(aksiBuffer != null)
                    aksiBuffer.bufferUtama(posisi, batasBuffer);
                updateJmlFotoDiload(posisi);
                return bufferFoto[ind];
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                img.setImageBitmap(bitmap);
                super.onPostExecute(bitmap);
            }
        };
        loader[ind].execute(posisi);
    }

    //posisi = index pathFoto
    public void pilihFoto(int posisi){
        dipilih[posisi]= ++cursorDipilih;
        viewDipilih.add(bufferView[posisi % batasBuffer]);
        bitmapDipilih.add(bufferFoto[posisi % batasBuffer]);
        if(aksiPilihFoto != null)
            aksiPilihFoto.pilihFoto(bufferView[posisi % batasBuffer], posisi);
    }
    public void batalPilihFoto(int posisi){
        if(aksiPilihFoto != null) {
            View v= viewDipilih.get(dipilih[posisi]-1);
            aksiPilihFoto.batalPilihFoto(v, posisi);
        }
        viewDipilih.remove(dipilih[posisi]-1);
        bitmapDipilih.remove(dipilih[posisi]-1);
        updateFotoDipilih(dipilih[posisi]);
        dipilih[posisi]= 0;
        cursorDipilih--;
    }
    //update ind foto dipilih saat yg dipilih berkurang
    private void updateFotoDipilih(int cursor){
        int hitungan= cursor;
        for(int i= 0; i< jmlUdahDiload; i++)
            if(hitungan == cursorDipilih)
                return;
            else if(dipilih[i] > cursor){
                dipilih[i]--;
                hitungan++;
            }
    }
    public int ambilUrutanDipilih(int posisi){
        return dipilih[posisi];
    }
    public int[] ambilSemuaUrutanDipilih(){
        int batas= viewDipilih.size();
        int hitung= 0;
        int indDipilih[]= new int[batas];
        for(int i= 0; i< dipilih.length; i++)
            if(dipilih[i] > 0)
                indDipilih[hitung++] = dipilih[i];
            else if(hitung == batas)
                break;
        return indDipilih;
    }

    private void updateJmlFotoDiload(int posisi){
        if(posisi > jmlUdahDiload -1)
            jmlUdahDiload = posisi+1;
    }

    public ArrayList<View> ambilViewDipilih(){
        return viewDipilih;
    }
    public View ambilViewDipilih(int indKe){
        return viewDipilih.get(indKe);
    }

    public ArrayList<Bitmap> ambilFotoDipilih(){
        return bitmapDipilih;
    }
    public Bitmap ambilFotoDipilih(int indKe){
        return bitmapDipilih.get(indKe);
    }

    public String[] ambilPathDipilih(){
        int batas= viewDipilih.size();
        int hitung= 0;
        String pathDipilih[]= new String[batas];
        for(int i= 0; i< pathFoto.length; i++)
            if(dipilih[i] > 0) {
                pathDipilih[dipilih[i] - 1] = pathFoto[i];
                hitung++;
            } else if(hitung == batas)
                break;
        return pathDipilih;
    }

    public int ambilJmlDipilih(){
        return cursorDipilih;
    }
    public int ambilJmlUdahDiload(){
        return jmlUdahDiload;
    }
}
