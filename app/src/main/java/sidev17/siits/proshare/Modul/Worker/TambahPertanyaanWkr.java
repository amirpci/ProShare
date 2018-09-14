package sidev17.siits.proshare.Modul.Worker;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import sidev17.siits.proshare.R;

public class TambahPertanyaanWkr extends AppCompatActivity {

    private View parentUtama;
    private GridView wadahCell;
    private TabBarIcon tabBarIcon;

    private EditText teksJudul;
    private EditText teksDeskripsi;

    private ImageView tmbCentang;

    private int tabIcon[]= {R.id.tambah_gambar, R.id.tambah_video, R.id.tambah_link};
    private final String WARNA_DITEKAN= "#4972AD"; //biruLaut
    private final String WARNA_TAK_DITEKAN= "#ADADAD"; //ambilWarna(R.color.abuLebihTua)

    private String pathFoto[];
    private ArrayList<Bitmap> filePhoto = new ArrayList<Bitmap>();
    private int dipilih[];
    private ArrayList<String> pathFotoDipilih = new ArrayList<String>();
    private ArrayList<Bitmap> filePhotoDipilih = new ArrayList<Bitmap>();

    private int idHalaman;

    protected void setIdHalaman(int idHalaman){
        this.idHalaman= idHalaman;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIdHalaman(getIntent().getIntExtra("idHalaman", R.layout.activity_tambah_pertanyaan_wkr));

        parentUtama= getLayoutInflater().inflate(idHalaman, null);
        setContentView(parentUtama);

        wadahCell= findViewById(R.id.tambah_properti_cell_wadah);
        tabBarIcon= new TabBarIcon((View) findViewById(R.id.tambah_properti_wadah),
                (View) findViewById(R.id.tambah_properti_icon));
        tabBarIcon.setTabIcon(tabIcon);
        tabBarIcon.setIdWarnaDitekan(WARNA_DITEKAN);
        tabBarIcon.setIdWarnaTakDitekan(WARNA_TAK_DITEKAN);

        teksJudul= findViewById(R.id.tambah_judul);
        teksDeskripsi= findViewById(R.id.tambah_deskripsi);
        tmbCentang= findViewById(R.id.tambah_ok);

        pathFoto= ambilPathGambar();
        isiFileFoto(pathFoto);
        inisiasiArrayDipilih();
    }
    int ambilWarna(int id){
        return getResources().getColor(id);
    }
//Ganti setOnClickListener
    void tekan(View v){
        tabBarIcon.tekanItem(v);
//        wadahCell.setY(tabBarIcon.ambilLetakY());
    }

    class TabBarIcon {
        private int tabIcon[];
        private int tabGaris[];

        private String idWarnaDitekan;
        private String idWarnaTakDitekan;

        private int trahirDitekan= -1;
        private boolean ditekanKah= false;

        View view;
        View viewIcon;

        TabBarIcon(){}
        TabBarIcon(View view, View viewIcon){
            this.view= view;
            this.viewIcon= viewIcon;
        }
        TabBarIcon(View view, int tabIcon[], int tabGaris[], String idWarnaDitekan, String idWarnaTakDitekan){
            this.view= view;
            this.tabIcon= tabIcon;
            this.tabGaris= tabGaris;
            this.idWarnaDitekan= idWarnaDitekan;
            this.idWarnaTakDitekan= idWarnaTakDitekan;
        }
        void setTabIcon(int tabIcon[]){
            this.tabIcon= tabIcon;
        }
        void setTabGaris(int tabGaris[]){
            this.tabGaris= tabGaris;
        }
        void setIdWarnaDitekan(String idWarnaDitekan){
            this.idWarnaDitekan= idWarnaDitekan;
        }
        void setIdWarnaTakDitekan(String idWarnaTakDitekan){
            this.idWarnaTakDitekan= idWarnaTakDitekan;
        }

        int getTrahirDitekan(){
            return trahirDitekan;
        }

        public void tekanItem(View v){
            View icon;
            for(int i= 0; i< tabIcon.length; i++) {
                icon = view.findViewById(tabIcon[i]);
                if (icon == v) {
                    tekanItem(i);
                    break;
                }
            }
        }
        void tekanItem(int ind){
            if(ind != trahirDitekan) {
                if (!ditekanKah) {
                    final float batasTinggi = view.getHeight() + 400;

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) batasTinggi);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    view.setLayoutParams(lp);
                }
                if(ind== 2)
                    wadahCell.setNumColumns(1);
                else
                    wadahCell.setNumColumns(3);

                int lebar = wadahCell.getWidth() / wadahCell.getNumColumns();
                AdapterPropertiCell adpCell = new AdapterPropertiCell(lebar, ind, view, filePhoto);
                wadahCell.setAdapter(adpCell);
            } else if(ditekanKah && ind == trahirDitekan){
                int tinggiAwal= viewIcon.getHeight();

                RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, tinggiAwal);
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                view.setLayoutParams(lp);
            }
            tekanGantiWarna(ind);
/*
            if(tinggiAwal <batasTinggi) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        float tinggiUbah= tinggiAwal;
                        float lebar = view.getWidth();
                        while (tinggiUbah <= batasTinggi) {
                            try {
                                sleep(5);
                            } catch (Exception e) {
                            } finally {
                                view.setLayoutParams(new RelativeLayout.LayoutParams((int) lebar, (int) tinggiUbah));
                            }
                            tinggiUbah++;
                        }
                        super.run();
                    }
                };
                thread.run();
            }
*/
        }
        void tekanGantiWarna(int ind){
            if(ind != trahirDitekan) {
                ImageView iconDitekanSkrg = view.findViewById(tabIcon[ind]);
                if (ditekanKah) {
                    ImageView iconDitekanTadi = view.findViewById(tabIcon[trahirDitekan]);
                    iconDitekanTadi.setColorFilter(Color.parseColor(idWarnaTakDitekan));
                }
                iconDitekanSkrg.setColorFilter(Color.parseColor(idWarnaDitekan));
                trahirDitekan= ind;
                ditekanKah= true;
            } else{
                ImageView iconDitekanTadi = view.findViewById(tabIcon[trahirDitekan]);
                iconDitekanTadi.setColorFilter(Color.parseColor(idWarnaTakDitekan));
                trahirDitekan= -1;
                ditekanKah= false;
            }
        }
    }


    public String[] ambilPathGambar(){
        ArrayList<String> arrayList= getImagesPath();
        String array[]= new String[arrayList.size()];
        for(int i=0; i< array.length; i++)
            array[i]= arrayList.get(i);
        return array;
    } public ArrayList<String> getImagesPath() {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String PathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

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

    void isiFileFoto(String photoDir[]){
        for(int i= 0; i<20; i++) {
            Bitmap rawBitmap= BitmapFactory.decodeFile(photoDir[i]);
            rawBitmap= skalaFoto(rawBitmap, (float) 0.2);
            rawBitmap= kropFoto(rawBitmap);
//            rawBitmap= skalaFoto(rawBitmap, 10);
            filePhoto.add(rawBitmap);
        }
    }

    public Bitmap skalaFoto(Bitmap bm, float skala){
        int pjg= bm.getWidth();
        int lbr= bm.getHeight();

        int pjgBaru= Math.round(pjg *skala);
        int lbrBaru= Math.round(lbr *skala);

        Bitmap fotoBaru= Bitmap.createScaledBitmap(bm, pjgBaru, lbrBaru, false);

        return fotoBaru;
    }
    public Bitmap kropFoto(Bitmap bm){
        int pjg= bm.getWidth();
        int lbr= bm.getHeight();

        if(lbr < pjg)
            pjg= lbr;

        int mulaiX= bm.getWidth()/2 - pjg/2;
        int mulaiY= bm.getHeight()/2 - lbr/2;

        return Bitmap.createBitmap(bm, mulaiX, mulaiY, pjg, pjg);
    }

    public void inisiasiArrayDipilih(){
        dipilih= new int[filePhoto.size()];
    }
    public void pilihGambar(int ind, ImageView centang) {
        centang.setImageResource(R.drawable.obj_centang_lingkaran_bolong);
        centang.setColorFilter(Color.parseColor(WARNA_DITEKAN));
        centang.setSelected(true);
        dipilih[ind] = 1;
    } public void batalPilihGambar(int ind, ImageView centang){
        centang.setImageResource(R.drawable.latar_lingkaran_bolong);
        centang.setColorFilter(Color.parseColor("#EEFFFFFF"));
        centang.setSelected(false);
        dipilih[ind]= 0;
    }

    public void buatPathFotoDipilih(){
        pathFotoDipilih= new ArrayList<String>();
        for(int i= 0; i<dipilih.length; i++)
            if(dipilih[i] == 1)
                pathFotoDipilih.add(pathFoto[i]);
    }
    public void buatFotoDipilih(){
        filePhotoDipilih= new ArrayList<Bitmap>();
        for(int i= 0; i<dipilih.length; i++)
            if(dipilih[i] == 1)
                filePhotoDipilih.add(filePhoto.get(i));
    }

    public void inisiasiOk(){
        tmbCentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void pertanyaanOk(){
        //simpan pertanyaan.
        buatPathFotoDipilih();

        //lakukan sesuatu...
    }

    class AdapterPropertiCell extends BaseAdapter{

        int lebar;
        int indPosisi;
        private ArrayList<Bitmap> filePhoto = new ArrayList<Bitmap>();

        View parent;

        AdapterPropertiCell(int lebar, int indPosisi, View parent, ArrayList<Bitmap> filePhoto){
            this.lebar= lebar;
            this.indPosisi= indPosisi;
            this.parent= parent;
            this.filePhoto= filePhoto;
        }

        @Override
        public int getCount() {
            if(indPosisi== 2)
                return 1;
            else
                return 20;
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

            if(indPosisi!= 2) {
                final View view= getLayoutInflater().inflate(R.layout.model_tambah_properti_cell, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(lebar, lebar));

                ImageView bg= view.findViewById(R.id.tambah_cell_pratinjau);
                bg.setImageBitmap(filePhoto.get(position));

                final ImageView centang = view.findViewById(R.id.tambah_cell_centang);
                final GridView liveQuestion= parentUtama.findViewById(R.id.tambah_properti_cell_dipilih);
                centang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!centang.isSelected()) {
                            pilihGambar(position, centang);

                            buatFotoDipilih();
                            AdapterPropertiDipilih adpDipilih= new AdapterPropertiDipilih(lebar, indPosisi, parentUtama, filePhotoDipilih);
                            liveQuestion.setAdapter(adpDipilih);
                        } else {
                            batalPilihGambar(position, centang);

                            buatFotoDipilih();
                            AdapterPropertiDipilih adpDipilih= new AdapterPropertiDipilih(lebar, indPosisi, parentUtama, filePhotoDipilih);
                            liveQuestion.setAdapter(adpDipilih);
                        }
                    }
                });
                if (indPosisi == 1) {
                    ImageView indiVideo = view.findViewById(R.id.tambah_cell_indek_video);
                    indiVideo.setImageResource(R.drawable.obj_indek_video_lingkaran);
                }

                if(dipilih[position]== 1)
                    pilihGambar(position, centang);
                else
                    batalPilihGambar(position, centang);


                return  view;
            } else{

                EditText teksLink= new EditText(getBaseContext());
                RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(40, 40, 40, 40);

                SpannableString string= new SpannableString("insert your link here...");
                string.setSpan(new UnderlineSpan(), 0, string.length(), 0);

                teksLink.setLayoutParams(lp);
                teksLink.setHint(string);
                teksLink.setHintTextColor(Color.parseColor(WARNA_DITEKAN));

                teksLink.setTextColor(Color.parseColor(WARNA_DITEKAN));
                teksLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                return teksLink;
            }
        }
    }

    class AdapterPropertiDipilih extends BaseAdapter{

        int lebar;
        int indPosisi;
        private ArrayList<Bitmap> filePhoto = new ArrayList<Bitmap>();

        View parent;

        AdapterPropertiDipilih(int lebar, int indPosisi, View parent, ArrayList<Bitmap> filePhoto){
            this.lebar= lebar;
            this.indPosisi= indPosisi;
            this.parent= parent;
            this.filePhoto= filePhoto;
        }

        @Override
        public int getCount() {
            return filePhoto.size();
        }

        @Override
        public Object getItem(int position) {
            return filePhoto.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parentInn) {
            final View view= getLayoutInflater().inflate(R.layout.model_tambah_properti_cell, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(lebar, lebar));

            ImageView bg= view.findViewById(R.id.tambah_cell_pratinjau);
            bg.setImageBitmap(filePhoto.get(position));

            ImageView centang = view.findViewById(R.id.tambah_cell_centang);
            centang.setImageDrawable(null);

            return view;
        }
    }
}
