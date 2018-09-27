package sidev17.siits.proshare.Modul.Worker;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.method.KeyListener;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.GaleriLoader;

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
    private GaleriLoader loader;

//    private ArrayList<Bitmap> filePhoto = new ArrayList<Bitmap>();
//    private ArrayList<String> pathFotoDipilih = new ArrayList<String>();

/*
    private int dipilih[];
    private ArrayList<Bitmap> filePhotoDipilih = new ArrayList<Bitmap>();
    private ArrayList<Integer> intDipilih= new ArrayList<Integer>();
*/
    private ArrayList<View> viewDipilih = new ArrayList<View>();

    private final int JML_BUFFER_FOTO= 12;
    private final int JML_MAKS_FOTO= JML_BUFFER_FOTO *2;
    private int cursorGaleri= 1;

    private int batasDipilih= 20;
    private int batasBufferUdahDiload = 0;

    private int idHalaman;

    private TextView tmbVerify;
    private boolean verifiedQuestion= false;

    protected void setIdHalaman(int idHalaman){
        this.idHalaman= idHalaman;
    }

    void verifyQuestion(boolean verify){
        verifiedQuestion= verify;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIdHalaman(getIntent().getIntExtra("idHalaman", R.layout.activity_tambah_pertanyaan_wkr));

        parentUtama= getLayoutInflater().inflate(idHalaman, null);
        setContentView(parentUtama);

        if(idHalaman== R.layout.activity_tambah_jawaban_exprt) {
            tmbVerify = findViewById(R.id.tambah_verify);
            tmbVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!tmbVerify.isSelected()) {
                        tmbVerify.setSelected(true);
                        tmbVerify.setBackgroundColor(getResources().getColor(R.color.biruLaut));
                        tmbVerify.setTextColor(Color.parseColor("#FFFFFF"));
                        verifyQuestion(true);
                    } else {
                        tmbVerify.setSelected(false);
//                        tmbVerify.getBackground().setTint(getResources().getColor(R.color.abuSangatTua));
                        tmbVerify.setBackgroundResource(R.drawable.latar_bingkai_kotak);
                        tmbVerify.setTextColor(getResources().getColor(R.color.biruLaut));
                        verifyQuestion(false);
                    }
                }
            });
        }
        wadahCell= findViewById(R.id.tambah_properti_cell_wadah);
        tabBarIcon= new TabBarIcon((View) findViewById(R.id.tambah_properti_wadah),
                (View) findViewById(R.id.tambah_properti_icon));
        tabBarIcon.setTabIcon(tabIcon);
        tabBarIcon.setIdWarnaDitekan(WARNA_DITEKAN);
        tabBarIcon.setIdWarnaTakDitekan(WARNA_TAK_DITEKAN);
        initAksiTekan();

        teksJudul= findViewById(R.id.tambah_judul);
        teksJudul.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(tabBarIcon.getTrahirDitekan() > -1)
                    tabBarIcon.tekanItem(tabBarIcon.getTrahirDitekan());
                teksJudul.requestFocus();
                final InputMethodManager imm= (InputMethodManager) getBaseContext().getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(teksJudul, InputMethodManager.SHOW_IMPLICIT);
                tmbCentang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imm.hideSoftInputFromWindow(teksJudul.getWindowToken(), 0);
                        teksJudul.clearFocus();
                        tmbCentang.setImageResource(R.drawable.icon_kirim);
                        tmbCentang.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                kirimPertanyaan();
                            }
                        });
                    }
                });
                tmbCentang.setImageResource(R.drawable.obj_centang);
                return true;
            }
        });
        initTeksJudul();
        teksJudul.clearFocus();
        teksDeskripsi= findViewById(R.id.tambah_deskripsi);
        teksDeskripsi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(tabBarIcon.getTrahirDitekan() > -1)
                    tabBarIcon.tekanItem(tabBarIcon.getTrahirDitekan());
                teksDeskripsi.requestFocus();
                final InputMethodManager imm= (InputMethodManager) getBaseContext().getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(teksDeskripsi, InputMethodManager.SHOW_IMPLICIT);
                tmbCentang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imm.hideSoftInputFromWindow(teksDeskripsi.getWindowToken(), 0);
                        teksDeskripsi.clearFocus();
                        tmbCentang.setImageResource(R.drawable.icon_kirim);
                        tmbCentang.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                kirimPertanyaan();
                            }
                        });
                    }
                });
                tmbCentang.setImageResource(R.drawable.obj_centang);
                return true;
            }
        });
        tmbCentang= findViewById(R.id.tambah_ok);

        pathFoto= ambilPathGambar();
//        isiFileFoto(pathFoto, 0, 12);
//        inisiasiFileFoto();
        inisiasiOk();
        initLoader();
    }
    int ambilWarna(int id){
        return getResources().getColor(id);
    }
//Ganti setOnClickListener
    void tekan(View v){
        tabBarIcon.tekanItem(v);
//        wadahCell.setY(tabBarIcon.ambilLetakY());
    }
    void initAksiTekan(){
        View.OnClickListener l[]= {
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tabBarIcon.tekanItem(0);
                        InputMethodManager imm= (InputMethodManager) getBaseContext().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tabBarIcon.tekanItem(1);
                        InputMethodManager imm= (InputMethodManager) getBaseContext().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tabBarIcon.tekanItem(2);
                        InputMethodManager imm= (InputMethodManager) getBaseContext().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
        };
        tabBarIcon.setClickListenerTab(l);
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

        void setClickListenerTab(View.OnClickListener l[]){
            for(int i= 0; i< tabIcon.length; i++) {
                View v= view.findViewById(tabIcon[i]);
                v.setOnClickListener(l[i]);
            }
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
                AdapterPropertiCell adpCell = new AdapterPropertiCell(lebar, ind, view);
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
                aturDitekan(true);
            } else{
                ImageView iconDitekanTadi = view.findViewById(tabIcon[trahirDitekan]);
                iconDitekanTadi.setColorFilter(Color.parseColor(idWarnaTakDitekan));
                trahirDitekan= -1;
                aturDitekan(false);
            }
        }
        void aturDitekan(boolean ditekan){
            ditekanKah= ditekan;
        }
    }

    void initTeksJudul(){
        teksJudul.setKeyListener(new KeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_FLAG_CAP_WORDS;
            }

            @Override
            public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
                return true;
            }

            @Override
            public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
                return true;
            }

            @Override
            public boolean onKeyOther(View view, Editable text, KeyEvent event) {
                return true;
            }

            @Override
            public void clearMetaKeyState(View view, Editable content, int states) {

            }
        });
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

    void initLoader(){
//        inisiasiArrayDipilih(pathFoto.length);
        loader= new GaleriLoader(getBaseContext(), pathFoto, 18);
        loader.aturUkuranPratinjau(250);
        loader.aturSumberBg(R.drawable.obj_gambar_kotak);
        loader.aturSumberBgTakBisa(R.drawable.obj_tanda_seru_lingkaran_garis);
        loader.aturIdElemenImg(R.id.tambah_cell_pratinjau);

        loader.aturModeBg(false);

        loader.aturAksiPilihFoto(new GaleriLoader.AksiPilihFoto() {
            @Override
            public void pilihFoto(View v, int posisi) {
                TextView indDipilih = v.findViewById(R.id.tambah_cell_centang_no);
                indDipilih.setText(Integer.toString(loader.ambilUrutanDipilih(posisi)));
                indDipilih.getBackground().setTint(getResources().getColor(R.color.biruLaut));
                indDipilih.getBackground().setAlpha(255);
            }
            @Override
            public void batalPilihFoto(View v, int posisi) {
                TextView indDipilih = v.findViewById(R.id.tambah_cell_centang_no);
                indDipilih.setText("");
                indDipilih.getBackground().setAlpha(0);
            }
        });

        loader.aturAksiBuffer(new GaleriLoader.AksiBuffer() {
            @Override
            public void bufferThumbnail(final int posisi, final int jmlBuffer) {
                View view= loader.ambilView(posisi);
                final int lebar= view.getLayoutParams().width;

                final TextView indDipilih = view.findViewById(R.id.tambah_cell_centang_no);
                final GridView liveQuestion= parentUtama.findViewById(R.id.tambah_properti_cell_dipilih);
                indDipilih.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!indDipilih.isSelected()) {
                            loader.pilihFoto(posisi);
                            indDipilih.setSelected(true);
                            AdapterPropertiDipilih adpDipilih= new AdapterPropertiDipilih(lebar, loader.ambilFotoDipilih());
                            liveQuestion.setAdapter(adpDipilih);
                            if(loader.ambilFotoDipilih().size() <= 3)
                                aturTinggiGrid(liveQuestion, lebar);
                            else
                                aturTinggiGrid(liveQuestion, lebar+90);
                        } else {
                            loader.batalPilihFoto(posisi);
                            indDipilih.setSelected(false);
                            AdapterPropertiDipilih adpDipilih= new AdapterPropertiDipilih(lebar, loader.ambilFotoDipilih());
                            liveQuestion.setAdapter(adpDipilih);
                            if(loader.ambilFotoDipilih().size() == 0)
                                aturTinggiGrid(liveQuestion, 0);
                            else if(loader.ambilFotoDipilih().size() <= 3)
                                aturTinggiGrid(liveQuestion, lebar);
                            else
                                aturTinggiGrid(liveQuestion, lebar+90);
                        }
                    }
                });
/*
                if(dipilih[posisi]== 1)
                    pilihGambar(posisi, indDipilih);
                else
                    batalPilihGambar(posisi, indDipilih);
*/
            }
            @Override
            public void bufferUtama(int posisi, int jmlBuffer) {

            }
        });
    }

    void aturTinggiGrid(GridView grid, int tinggi){
//        HorizontalScrollView.LayoutParams lpGrid= new HorizontalScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, tinggi);
//        grid.setLayoutParams(lpGrid);
        grid.getLayoutParams().height= tinggi;
    }
/*
    void inisiasiFileFoto(){
        filePhoto= new ArrayList<Bitmap>();
    }
    void cekIsiFileFoto(int position){
        if(position % JML_BUFFER_FOTO == 0){
            Bitmap bitmapSmt[]= new Bitmap[JML_BUFFER_FOTO];
            if(position > cursorGaleri){
                if(filePhoto.size() == JML_MAKS_FOTO){
                    for(int i= JML_BUFFER_FOTO-1; i<JML_MAKS_FOTO; i++)
                        bitmapSmt[i]= filePhoto.get(i);
                    inisiasiFileFoto();
                    for(int i= 0; i<JML_BUFFER_FOTO; i++)
                        filePhoto.add(bitmapSmt[i]);
                }
                isiFileFoto(pathFoto, position, JML_BUFFER_FOTO);
                cursorGaleri= position;
            }
            else if(position < cursorGaleri){
                if(filePhoto.size() == JML_MAKS_FOTO){
                    for(int i= 0; i<JML_BUFFER_FOTO; i++)
                        bitmapSmt[i]= filePhoto.get(i);
                    inisiasiFileFoto();
                    isiFileFoto(pathFoto, position - JML_BUFFER_FOTO, JML_BUFFER_FOTO);
                    for(int i= 0; i<JML_BUFFER_FOTO; i++)
                        filePhoto.add(bitmapSmt[i]);
                }
                cursorGaleri= position;
            }
            else if(position == 0)
                isiFileFoto(pathFoto, position, JML_BUFFER_FOTO);
        }
    }
    void isiFileFoto(String photoDir[], int mulai, int sebanyak){
        for(int i= mulai; i<mulai +sebanyak; i++) {
            Bitmap rawBitmap= BitmapFactory.decodeFile(photoDir[i]);
            rawBitmap= skalaFoto(rawBitmap, (float) 0.2);
            rawBitmap= kropFoto(rawBitmap);
//            rawBitmap= skalaFoto(rawBitmap, 10);
            filePhoto.add(rawBitmap);
        }
//        inisiasiArrayDipilih();
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

    //untuk menambahkan img
    void updateImgDipilih(ImageView img){
        viewDipilih.add(img);
    }
    void updateBufferDiload(int batasBaru){
        if(batasBufferUdahDiload < batasBaru)
            batasBufferUdahDiload = batasBaru;
    }

    public void inisiasiArrayDipilih(int jml){
        dipilih= new int[jml];
    }
    //saat dipilih[] berkurang
    void updateDipilih(int cursor){
        for (int i = 0; i < batasBufferUdahDiload; i++){
            if(dipilih[i]== cursor++)
                dipilih[i]--;
            if(cursor== batasDipilih)
                return;
        }
    }
    public void pilihGambar(int ind, TextView teks) {
        teks.getBackground().setTint(getResources().getColor(R.color.biruLaut));
        teks.setText(Integer.toString(dipilih[ind]));
//        centang.setImageResource(R.drawable.obj_centang_lingkaran_bolong);
//        centang.setColorFilter(Color.parseColor(WARNA_DITEKAN));
//        centang.setSelected(true);
        dipilih[ind] = cursorGaleri++;

    } public void batalPilihGambar(int ind, ImageView centang){
        centang.setImageResource(R.drawable.latar_lingkaran_bolong);
        centang.setColorFilter(Color.parseColor("#EEFFFFFF"));
        centang.setSelected(false);
        updateDipilih(dipilih[ind]);
        dipilih[ind]= 0;
        cursorGaleri--;
    }
/*
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
*/
    public void inisiasiOk(){
        tmbCentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimPertanyaan();
            }
        });
    }
    public void kirimPertanyaan(){
        //simpan pertanyaan.
        String pathFotoDipilih[]= loader.ambilPathDipilih();
        Toast.makeText(getBaseContext(), "dir 0: " +pathFotoDipilih[0], Toast.LENGTH_LONG).show();
        String judul= teksJudul.getText().toString();
        String deskripsi= teksDeskripsi.getText().toString();
        boolean verified= verifiedQuestion;
//        buatPathFotoDipilih();

        //lakukan sesuatu...
    }

    class AdapterPropertiCell extends BaseAdapter{

        int lebar;
        int indPosisi;
//        private ArrayList<Bitmap> filePhoto = new ArrayList<Bitmap>();

        View parent;

        AdapterPropertiCell(int lebar, int indPosisi, View parent){
            this.lebar= lebar;
            this.indPosisi= indPosisi;
            this.parent= parent;
//            this.filePhoto= filePhoto;
        }

        @Override
        public int getCount() {
            if(indPosisi== 2)
                return 1;
            else
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

            if(indPosisi!= 2) {
                View view= getLayoutInflater().inflate(R.layout.model_tambah_properti_cell, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(lebar, lebar));

                view= loader.buatFoto(view, position);

//                updateBufferDiload(position);

//                cekIsiFileFoto(position);
/*
                ViewGroup vg= (ViewGroup) view.getParent();
                vg.addView(loader.buatFoto(position));

                final ImageView centang = view.findViewById(R.id.tambah_cell_centang);
                final GridView liveQuestion= parentUtama.findViewById(R.id.tambah_properti_cell_dipilih);
                centang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!centang.isSelected()) {
                            pilihGambar(position, centang);

                            buatFotoDipilih();
//                            AdapterPropertiDipilih adpDipilih= new AdapterPropertiDipilih(lebar, indPosisi, parentUtama, filePhotoDipilih);
//                            liveQuestion.setAdapter(adpDipilih);
                        } else {
                            batalPilihGambar(position, centang);

                            buatFotoDipilih();
//                            AdapterPropertiDipilih adpDipilih= new AdapterPropertiDipilih(lebar, indPosisi, parentUtama, filePhotoDipilih);
//                            liveQuestion.setAdapter(adpDipilih);
                        }
                    }
                });
                if (indPosisi == 1) {
                    ImageView indiVideo = view.findViewById(R.id.tambah_cell_indek_video);
                    indiVideo.setImageResource(R.drawable.obj_indek_video_lingkaran);
                }

                ImageView img= loader.buatFoto(position);
                img.setLayoutParams(new ViewGroup.LayoutParams(lebar, lebar));
/*
                if(dipilih[position]== 1)
                    pilihGambar(position, centang);
                else
                    batalPilihGambar(position, centang);
*/
                return view;
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
        private int lebar;
//        private int indPosisi;
        private ArrayList<Bitmap> fotoDipilih;

        AdapterPropertiDipilih(int lebar, ArrayList<Bitmap> fotoDipilih){
            this.lebar= lebar;
            this.fotoDipilih= fotoDipilih;
//            this.indPosisi= indPosisi;
//            this.parent= parent;
//            this.filePhoto= filePhoto;
        }

        @Override
        public int getCount() {
            return fotoDipilih.size();
        }

        @Override
        public Object getItem(int position) {
            return fotoDipilih.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parentInn) {
            View view= getLayoutInflater().inflate(R.layout.model_tambah_properti_cell, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(lebar, lebar));

            ImageView bg= view.findViewById(R.id.tambah_cell_pratinjau);
            bg.setImageBitmap(fotoDipilih.get(position));

            ImageView centang = view.findViewById(R.id.tambah_cell_centang);

            ViewGroup vg= (ViewGroup) view;
            vg.removeView(centang);

            return view;
        }
    }
}
