package sidev17.siits.proshare.Modul.Worker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.GaleriLoader;
import sidev17.siits.proshare.Utils.ParcelHolder;
import sidev17.siits.proshare.Utils.UnserializableHolder;

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
    private String pathVideo[];
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
        pathVideo= ambilPathVideo();
//        isiFileFoto(pathFoto, 0, 12);
//        inisiasiFileFoto();
        inisiasiOk();
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
                if(ind== 0) {
                    wadahCell.setNumColumns(3);
                    initLoader(pathFoto, GaleriLoader.JENIS_FOTO);
                } else if(ind== 1){
                    wadahCell.setNumColumns(3);
                    initLoader(pathVideo, GaleriLoader.JENIS_VIDEO_THUMBNAIL);
                    RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams(50, 50);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    lp.setMargins(10, 10, 10, 10);
                    loader.tambahAksesoris(R.drawable.obj_indek_video_lingkaran, lp);
                } else if(ind== 2)
                    wadahCell.setNumColumns(1);

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
    public String[] ambilPathVideo(){
        ArrayList<String> arrayList= getVideoPath();
        String array[]= new String[arrayList.size()];
        int indJalan= 0;
        for(int i= array.length-1; i >= 0; i--)
            array[indJalan++]= arrayList.get(i);
        return array;
    } public ArrayList<String> getVideoPath() {
        Uri uri;
        ArrayList<String> listOfAllVideo = new ArrayList<String>();
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String PathOfVideo = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Bitmap bm= ThumbnailUtils.createVideoThumbnail("asas", MediaStore.Video.Thumbnails.MICRO_KIND);

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME };

        cursor = getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            PathOfVideo = cursor.getString(column_index_data);

            listOfAllVideo.add(PathOfVideo);
        }
        return listOfAllVideo;
    }

    void initLoader(String pathFile[], int jenisFoto){
//        inisiasiArrayDipilih(pathFoto.length);
        loader= new GaleriLoader(getBaseContext(), pathFile, 18, jenisFoto);
        loader.aturBentukFoto(GaleriLoader.BENTUK_KOTAK);
        loader.aturUkuranPratinjau(250);
        loader.aturSumberBg(R.drawable.obj_gambar_kotak);
        loader.aturSumberBgTakBisa(R.drawable.obj_tanda_seru_lingkaran_garis);
        loader.aturIdElemenImg(R.id.tambah_cell_pratinjau);

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
                int indDipilih[]= loader.ambilSemuaUrutanDipilih();
                ArrayList<View> viewDipilih= loader.ambilViewDipilih();
                int indYgDibatalkan= loader.ambilUrutanDipilih(posisi) -1;
                int indBaru= indYgDibatalkan +1;

                TextView noUrut = v.findViewById(R.id.tambah_cell_centang_no);
                noUrut.setText("");
                noUrut.getBackground().setAlpha(0);

                for(int i= 0; i< indDipilih.length; i++) {
                    if(i > indYgDibatalkan) {
                        noUrut = viewDipilih.get(i).findViewById(R.id.tambah_cell_centang_no);
                        noUrut.setText(Integer.toString(indBaru++));
                    }
                }
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
            }
            @Override
            public void bufferUtama(int posisi, int jmlBuffer) {

            }
        });
    }

    void aturTinggiGrid(GridView grid, int tinggi){
        grid.getLayoutParams().height= tinggi;
    }

    public void inisiasiOk(){
        tmbCentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimPertanyaan();
            }
        });
    }
    //METHOD DUMMY!
    public void kirimPertanyaan(){
        //simpan pertanyaan.
//        String pathFotoDipilih[]= loader.ambilPathDipilih();
        int jmlUdahDiload= loader.ambilJmlUdahDiload();
        int batas= loader.ambilJmlDipilih();
        String daftarInd= "";
        int dipilih[]= loader.ambilSemuaUrutanDipilih();
        for(int i= 0; i< batas; i++)
            daftarInd+= Integer.toString(dipilih[i]) +", ";
        Toast.makeText(getBaseContext(), "dipilih \"" +Integer.toString(jmlUdahDiload) +"\": " +daftarInd, Toast.LENGTH_LONG).show();
        String judul= teksJudul.getText().toString();
        String deskripsi= teksDeskripsi.getText().toString();
        boolean verified= verifiedQuestion;
//        buatPathFotoDipilih();

        //lakukan sesuatu...
    }

    class AdapterPropertiCell extends BaseAdapter{

        int lebar;
        int indPosisi;

        View parent;

        AdapterPropertiCell(int lebar, int indPosisi, View parent){
            this.lebar= lebar;
            this.indPosisi= indPosisi;
            this.parent= parent;
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
        private ArrayList<Bitmap> itemDipilih;

        AdapterPropertiDipilih(int lebar, ArrayList<Bitmap> itemDipilih){
            this.lebar= lebar;
            this.itemDipilih = itemDipilih;
        }

        @Override
        public int getCount() {
            return itemDipilih.size();
        }

        @Override
        public Object getItem(int position) {
            return itemDipilih.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parentInn) {
            View view= getLayoutInflater().inflate(R.layout.model_tambah_properti_cell, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(lebar, lebar));

            ImageView bg= view.findViewById(R.id.tambah_cell_pratinjau);
            bg.setImageBitmap(itemDipilih.get(position));
            bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent kePreview= new Intent(getBaseContext(), PhotoPreview.class);
/*
                    Bitmap arrayBm[]= new Bitmap[itemDipilih.size()];
                    arrayBm= itemDipilih.toArray(arrayBm);
                    kePreview.putExtra("foto", new ParcelHolder<Bitmap>(arrayBm));
*/
//                    kePreview.putParcelableArrayListExtra("foto", itemDipilih);
                    kePreview.putStringArrayListExtra("judul", loader.ambilJudulDipilih());
                    kePreview.putExtra("path", loader.ambilPathDipilih());
                    kePreview.putExtra("indDipilih", position);

                    startActivity(kePreview);
                }
            });

            ImageView centang = view.findViewById(R.id.tambah_cell_centang);

            ViewGroup vg= (ViewGroup) view;
            vg.removeView(centang);

            return view;
        }
    }
}
