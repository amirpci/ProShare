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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sidev17.siits.proshare.Modul.Worker.GaleriPreview;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Array;
import sidev17.siits.proshare.Utils.ArrayMod;
import sidev17.siits.proshare.Utils.ViewTool.GaleriLoader;

public class AmbilGambarAct extends AppCompatActivity {
    public static final int JENIS_AMBIL_BANYAK= 31;
    public static final int JENIS_AMBIL_SATU= 30;

    private GridView wadahGambar;
    private GaleriLoader loader;
    private ImageView tmbKirim;

    private ImageView indikatorDipilihSatu;
    private int posisiTrahir= -1;

    private DisplayMetrics display;

    private String pathFoto[];
    private int posisiDipilih[]= new int[0];
    private int urutanDipilih[]= new int[0];

    private int jenisPengambilan= JENIS_AMBIL_BANYAK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambil_gambar);

        display= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);

        wadahGambar= findViewById(R.id.wadah_gambar);
        pathFoto= ambilPathGambar();
        initLoader(pathFoto, GaleriLoader.JENIS_FOTO);
        ambilData();
        tmbKirim= findViewById(R.id.tambah_ok);
        tmbKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indDipilih[][]= loader.ambilUrutanDipilih();
                Intent hasil= new Intent();
                hasil.putExtra("pathFoto", loader.ambilPathDipilih());
                hasil.putExtra("posisiDipilih", indDipilih[0]);
                hasil.putExtra("urutanDipilih", indDipilih[1]);
//                hasil.putExtra("jenisPengambilan", jenisPengambilan);
                if(loader.ambilPathDipilih().length > 0)
                    setResult(RESULT_OK, hasil);
                finish();
            }
        });
/*
        wadahGambar.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Toast.makeText(AmbilGambarAct.this, "firstVisible= " +firstVisibleItem, Toast.LENGTH_SHORT).show();
            }
        });
*/
        wadahGambar.setAdapter(new AdapterPropertiCell(/*display.widthPixels / wadahGambar.getNumColumns(), pathFoto.length*/));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            int posisiTrahir= 0;
            posisiDipilih= data.getIntArrayExtra("posisiDipilih");
            urutanDipilih= data.getIntArrayExtra("urutanDipilih");
            if(posisiDipilih != null && posisiDipilih.length > 0){
                wadahGambar.setAdapter(null);
                if(jenisPengambilan == JENIS_AMBIL_SATU)
                    ((ViewGroup)indikatorDipilihSatu.getParent()).removeView(indikatorDipilihSatu);
                loader.aturIndDipilih(posisiDipilih, urutanDipilih);
                posisiTrahir= posisiDipilih[posisiDipilih.length-1];
                wadahGambar.setAdapter(new AdapterPropertiCell());
//            wadahGambar.invalidateViews();
            }

            int posisiFoto= data.getIntExtra("posisiFoto", wadahGambar.getFirstVisiblePosition());
//        int posisiGaleri= (posisiFoto /wadahGambar.getNumColumns());
            posisiTrahir= (posisiTrahir == 0) ? posisiFoto : posisiTrahir;
            posisiTrahir += (2 *wadahGambar.getNumColumns());

            if(posisiTrahir > pathFoto.length)
                posisiTrahir= pathFoto.length -1;

            wadahGambar.smoothScrollToPosition(posisiTrahir);
//        wadahGambar.setSelection(posisiTrahir);
//        wadahGambar.requestLayout();

//        Toast.makeText(this, "posisiTrahir= " +posisiTrahir, Toast.LENGTH_SHORT).show();

//        super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void ambilData(){
        Intent dataSebelumnya= getIntent();

        int posisiDipilih[] = dataSebelumnya.getIntArrayExtra("posisiDipilih");
        int urutanDipilih[] = dataSebelumnya.getIntArrayExtra("urutanDipilih");

        jenisPengambilan= dataSebelumnya.getIntExtra("jenisPengambilan", JENIS_AMBIL_BANYAK);
      //  Toast.makeText(this, "jenisPengambilan= " +jenisPengambilan, Toast.LENGTH_SHORT).show();

        if(posisiDipilih != null && posisiDipilih.length > 0) {
            jenisPengambilan= JENIS_AMBIL_BANYAK;
            this.posisiDipilih= posisiDipilih;
            this.urutanDipilih= urutanDipilih;
            loader.aturIndDipilih(posisiDipilih, urutanDipilih);
        }
    }
    private void initIndikatorDipilihKotak(View vInduk){
        if(jenisPengambilan == JENIS_AMBIL_SATU){
            RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams(vInduk.getWidth(), vInduk.getHeight());
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            indikatorDipilihSatu= new ImageView(this);
            indikatorDipilihSatu.setImageResource(R.drawable.obj_kotak_dipilih);
            indikatorDipilihSatu.setLayoutParams(lp);
        }
    }

    class AdapterPropertiCell extends BaseAdapter {
        int dipilih= 0;

        int penghitung= 0;
        int batas= 0;
//        int lebar;

        AdapterPropertiCell(){
        }

        @Override
        public int getCount() {
            return pathFoto.length;
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
            TextView noUrut = view.findViewById(R.id.tambah_cell_centang_no);
            if(noUrut.isSelected())
                Toast.makeText(AmbilGambarAct.this, "dipilih= " +noUrut.isSelected() +" posisi= " +position +" urutan= " +loader.ambilUrutanDipilih(view), Toast.LENGTH_LONG).show();

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
/*
            try{
                view.findViewById(R.id.tambah_cell_pratinjau);
            } catch (Exception e){
                throw new RuntimeException(e.getMessage() +" posisi= " +position +" ukuran= " +loader.ambilUrutanDipilih()[0].length);
            }
*/
            ImageView bg= view.findViewById(R.id.tambah_cell_pratinjau);
            bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent kePreview= new Intent(getBaseContext(), GaleriPreview.class);
//                    kePreview.putStringArrayListExtra("judul", loader.ambilDaftarJudul());
//                    kePreview.putExtra("path", loader.ambilDaftarPathFoto());
//                    kePreview.putExtra("jenisPath", GaleriPreview.PATH_DEFAULT);
                    kePreview.putExtra("posisiFoto", position);

                    int indDipilih[][]= loader.ambilUrutanDipilih();
                    kePreview.putExtra("posisiDipilih", indDipilih[0]);
                    kePreview.putExtra("urutanDipilih", indDipilih[1]);
                    kePreview.putExtra("jenisPengambilan", jenisPengambilan);

                    startActivityForResult(kePreview, 0);
                }
            });

            return view;
        }
    }

    private void initLoader(String pathFile[], int jenisFoto){
//        inisiasiArrayDipilih(pathFoto.length);
        loader= new GaleriLoader(this, pathFile, 18, jenisFoto,
                R.layout.model_tambah_properti_cell, R.id.tambah_cell_pratinjau);
//        loader.aturIndDipilih(posisiDipilih, urutanDipilih);
        loader.aturBentukFoto(GaleriLoader.BENTUK_KOTAK);
        loader.aturJmlItemPerGaris(3);
        loader.aturOffsetItem(20);
        loader.aturUkuranPratinjau(loader.UKURAN_MENYESUAIKAN_LBR_INDUK);
        loader.aturSumberBg(R.drawable.obj_gambar_kotak);
        loader.aturSumberBgTakBisa(R.drawable.obj_tanda_seru_lingkaran_garis);

        loader.aturModeBg(false);

        loader.aturAksiPilihFoto(new GaleriLoader.AksiPilihFoto() {
            @Override
            public void pilihFoto(View v, final int posisi) {
                if(jenisPengambilan == JENIS_AMBIL_BANYAK){
                    TextView noUrut = v.findViewById(R.id.tambah_cell_centang_no);
                    noUrut.setText(Integer.toString(loader.ambilUrutanDipilih(posisi)));
                    noUrut.getBackground().setTint(getResources().getColor(R.color.biruLaut));
                    noUrut.getBackground().setAlpha(255);
                    noUrut.setSelected(true);
                } else if(jenisPengambilan == JENIS_AMBIL_SATU){
                    if(loader.ambilUrutanDipilih(posisi) > 1)
                        loader.batalPilihFoto(posisiTrahir);
                    posisiTrahir= posisi;
                    if(indikatorDipilihSatu == null)
                        initIndikatorDipilihKotak(v);
                    ViewGroup vg= v.findViewById(R.id.tambah_cell_kotak_dipilih);
                    vg.addView(indikatorDipilihSatu);
                }
/*
                ImageView bg= v.findViewById(R.id.tambah_cell_pratinjau);
                bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent kePreview= new Intent(getBaseContext(), GaleriPreview.class);
                        kePreview.putStringArrayListExtra("judul", loader.ambilJudulDipilih());
                        kePreview.putExtra("path", loader.ambilPathDipilih());
                        kePreview.putExtra("indDipilih", loader.ambilUrutanDipilih(posisi)-1);

                        startActivity(kePreview);
                    }
                });
*/
            }
            @Override
            public void batalPilihFoto(View v, int posisi) {
                if(jenisPengambilan == JENIS_AMBIL_BANYAK){
                    int indDipilih[][]= loader.ambilUrutanDipilih();
                    Array<View> viewDipilih= loader.ambilViewDipilih();
//                int indYgDibatalkan= loader.ambilUrutanDipilih(posisi) -1;
                    int mulai= ArrayMod.cariIndDlmArray(indDipilih[0], posisi);
                    int indBaru= indDipilih[1][mulai];
                    int batas= indDipilih[0].length;

                    TextView noUrut = v.findViewById(R.id.tambah_cell_centang_no);
                    noUrut.setText("");
                    noUrut.getBackground().setAlpha(0);
                    noUrut.setSelected(false);

                    for(int i= indBaru; i< batas; i++) {
                        noUrut = viewDipilih.ambil(i).findViewById(R.id.tambah_cell_centang_no);
                        noUrut.setText(Integer.toString(indBaru++));
                    }
                } else if(jenisPengambilan == JENIS_AMBIL_SATU){
                    ViewGroup vg= v.findViewById(R.id.tambah_cell_kotak_dipilih);
                    vg.removeView(indikatorDipilihSatu);
                }
            }
        });

        loader.aturAksiBuffer(new GaleriLoader.AksiBuffer() {
            @Override
            public void bufferThumbnail(final int posisi, final int jmlBuffer) {
                View view= loader.ambilView(posisi);
//                final int lebar= view.getLayoutParams().width;

                final TextView indDipilih = view.findViewById(R.id.tambah_cell_centang_no);
//                final GridView liveQuestion= parentUtama.findViewById(R.id.tambah_properti_cell_dipilih);
                indDipilih.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!indDipilih.isSelected()) {
                            loader.pilihFoto(posisi);
/*
                            TambahPertanyaanWkr.AdapterPropertiDipilih adpDipilih= new TambahPertanyaanWkr.AdapterPropertiDipilih(lebar, loader.ambilBitmapDipilih());
                            liveQuestion.setAdapter(adpDipilih);
                            if(loader.ambilBitmapDipilih().size() <= 3)
                                aturTinggiGrid(liveQuestion, lebar);
                            else
                                aturTinggiGrid(liveQuestion, lebar+90);
*/
                        } else {
                            loader.batalPilihFoto(posisi);
/*
                            TambahPertanyaanWkr.AdapterPropertiDipilih adpDipilih= new TambahPertanyaanWkr.AdapterPropertiDipilih(lebar, loader.ambilBitmapDipilih());
                            liveQuestion.setAdapter(adpDipilih);
                            if(loader.ambilBitmapDipilih().size() == 0)
                                aturTinggiGrid(liveQuestion, 0);
                            else if(loader.ambilBitmapDipilih().size() <= 3)
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


    private String[] ambilPathGambar(){
        ArrayList<String> arrayList= getImagesPath();
        String array[]= new String[arrayList.size()];
        int indJalan= 0;
        for(int i= array.length-1; i >= 0; i--)
            array[indJalan++]= arrayList.get(i);
        return array;
    } private ArrayList<String> getImagesPath() {
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
