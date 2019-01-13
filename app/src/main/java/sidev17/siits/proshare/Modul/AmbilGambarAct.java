package sidev17.siits.proshare.Modul;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sidev17.siits.proshare.Modul.Worker.PhotoPreview;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Array;
import sidev17.siits.proshare.Utils.ArrayMod;
import sidev17.siits.proshare.Utils.GaleriLoader;

public class AmbilGambarAct extends AppCompatActivity {

    private GridView wadahGambar;
    private GaleriLoader loader;
    private ImageView tmbKirim;

    private DisplayMetrics display;

    private String pathFoto[];
    private int posisiDipilih[]= new int[0];
    private int urutanDipilih[]= new int[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambil_gambar);

        display= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);

        wadahGambar= findViewById(R.id.wadah_gambar);
        pathFoto= ambilPathGambar();
        initLoader(pathFoto, GaleriLoader.JENIS_FOTO);
        isiIndDipilih();
        tmbKirim= findViewById(R.id.tambah_ok);
        tmbKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indDipilih[][]= loader.ambilUrutanDipilih();
                Intent hasil= new Intent();
                hasil.putExtra("pathFoto", loader.ambilPathDipilih());
                hasil.putExtra("posisiDipilih", indDipilih[0]);
                hasil.putExtra("urutanDipilih", indDipilih[1]);
                if(loader.ambilPathDipilih().length > 0)
                    setResult(RESULT_OK, hasil);
                finish();
            }
        });
        wadahGambar.setAdapter(new AdapterPropertiCell(display.widthPixels / wadahGambar.getNumColumns(), pathFoto.length));
    }

    private void isiIndDipilih(){
        int posisiDipilih[] = getIntent().getIntArrayExtra("posisiDipilih");
        int urutanDipilih[] = getIntent().getIntArrayExtra("urutanDipilih");

        if(posisiDipilih != null)
            this.posisiDipilih= posisiDipilih;
        if(urutanDipilih != null) {
            this.urutanDipilih= urutanDipilih;
            loader.aturIndDipilih(posisiDipilih, urutanDipilih);
        }
//        for(int i= 0; i< posisiDipilih.length; i++)
//            loader.pilihFoto(posisiDipilih[i]);
    }

    class AdapterPropertiCell extends BaseAdapter {

        int penghitung= 0;
        int batas= 0;
        int lebar;
        int jml;

        AdapterPropertiCell(int lebar, int jml){
            this.lebar= lebar;
            this.jml= jml;
        }

        @Override
        public int getCount() {
            return jml;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parentInn) {
            View view= loader.buatFoto(position); //getLayoutInflater().inflate(R.layout.model_tambah_properti_cell, null);
//            view.setLayoutParams(new ViewGroup.LayoutParams(lebar, lebar));
/*
            if(penghitung < posisiDipilih.length && posisiDipilih[penghitung]== position) {
                int batasDalam= urutanDipilih[penghitung];
                if(batasDalam > batas){
                    for(int i= penghitung; i< urutanDipilih.length; i++){
                        if();
                    }
                }

                loader.pilihFoto(posisiDipilih[penghitung++]);
            }
*/
            ImageView bg= view.findViewById(R.id.tambah_cell_pratinjau);
            bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent kePreview= new Intent(getBaseContext(), PhotoPreview.class);
                    kePreview.putStringArrayListExtra("judul", loader.ambilDaftarJudul());
                    kePreview.putExtra("path", loader.ambilDaftarPathFoto());
                    kePreview.putExtra("indDipilih", position);

                    startActivity(kePreview);
                }
            });
            return view;
        }
    }

    private void initLoader(String pathFile[], int jenisFoto){
//        inisiasiArrayDipilih(pathFoto.length);
        loader= new GaleriLoader(this, pathFile, 18, jenisFoto,
                R.layout.model_tambah_properti_cell, R.id.tambah_cell_pratinjau);
        loader.aturIndDipilih(posisiDipilih, urutanDipilih);
        loader.aturBentukFoto(GaleriLoader.BENTUK_KOTAK);
        loader.aturJmlItemPerGaris(3);
        loader.aturOffsetItem(20);
        loader.aturUkuranPratinjau(loader.UKURAN_MENYESUAIKAN_LBR_INDUK);
        loader.aturSumberBg(R.drawable.obj_gambar_kotak);
        loader.aturSumberBgTakBisa(R.drawable.obj_tanda_seru_lingkaran_garis);

        loader.aturModeBg(false);

        loader.aturAksiPilihFoto(new GaleriLoader.AksiPilihFoto() {
            @Override
            public void pilihFoto(View v, int posisi) {
                TextView noUrut = v.findViewById(R.id.tambah_cell_centang_no);
                noUrut.setText(Integer.toString(loader.ambilUrutanDipilih(posisi)));
                noUrut.getBackground().setTint(getResources().getColor(R.color.biruLaut));
                noUrut.getBackground().setAlpha(255);
            }
            @Override
            public void batalPilihFoto(View v, int posisi) {
                int indDipilih[][]= loader.ambilUrutanDipilih();
                Array<View> viewDipilih= loader.ambilViewDipilih();
//                int indYgDibatalkan= loader.ambilUrutanDipilih(posisi) -1;
                int mulai= ArrayMod.cariIndDlmArray(indDipilih[0], posisi);
                int indBaru= indDipilih[1][mulai];
                int batas= indDipilih[0].length;

                TextView noUrut = v.findViewById(R.id.tambah_cell_centang_no);
                noUrut.setText("");
                noUrut.getBackground().setAlpha(0);

                for(int i= indBaru; i< batas; i++) {
                    noUrut = viewDipilih.ambil(i).findViewById(R.id.tambah_cell_centang_no);
                    noUrut.setText(Integer.toString(indBaru++));
                }
            }
        });

        loader.aturAksiBuffer(new GaleriLoader.AksiBuffer() {
            @Override
            public void bufferThumbnail(final int posisi, final int jmlBuffer) {
                View view= loader.ambilView(posisi);
                final int lebar= view.getLayoutParams().width;

                final TextView indDipilih = view.findViewById(R.id.tambah_cell_centang_no);
//                final GridView liveQuestion= parentUtama.findViewById(R.id.tambah_properti_cell_dipilih);
                indDipilih.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!indDipilih.isSelected()) {
                            loader.pilihFoto(posisi);
                            indDipilih.setSelected(true);
/*
                            TambahPertanyaanWkr.AdapterPropertiDipilih adpDipilih= new TambahPertanyaanWkr.AdapterPropertiDipilih(lebar, loader.ambilFotoDipilih());
                            liveQuestion.setAdapter(adpDipilih);
                            if(loader.ambilFotoDipilih().size() <= 3)
                                aturTinggiGrid(liveQuestion, lebar);
                            else
                                aturTinggiGrid(liveQuestion, lebar+90);
*/
                        } else {
                            loader.batalPilihFoto(posisi);
                            indDipilih.setSelected(false);
/*
                            TambahPertanyaanWkr.AdapterPropertiDipilih adpDipilih= new TambahPertanyaanWkr.AdapterPropertiDipilih(lebar, loader.ambilFotoDipilih());
                            liveQuestion.setAdapter(adpDipilih);
                            if(loader.ambilFotoDipilih().size() == 0)
                                aturTinggiGrid(liveQuestion, 0);
                            else if(loader.ambilFotoDipilih().size() <= 3)
                                aturTinggiGrid(liveQuestion, lebar);
                            else
                                aturTinggiGrid(liveQuestion, lebar+90);
*/
                        }
                    }
                });

            }

            @Override
            public void bufferUtama(int posisi, int jmlBuffer) {

            }
        });
    }


    public String[] ambilPathGambar(){
        ArrayList<String> arrayList= getImagesPath();
        String array[]= new String[arrayList.size()];
        int indJalan= 0;
        for(int i= array.length-1; i >= 0; i--)
            array[indJalan++]= arrayList.get(i);
        return array;
    } public ArrayList<String> getImagesPath() {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String PathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ThumbnailUtils aka= new ThumbnailUtils();
        Bitmap bm= ThumbnailUtils.createVideoThumbnail("asas", MediaStore.Video.Thumbnails.MICRO_KIND);

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(PathOfImage);
        }
        return listOfAllImages;
    }
}
