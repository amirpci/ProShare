package sidev17.siits.proshare.Modul.Worker;

import android.content.Intent;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sidev17.siits.proshare.Adapter.SpinnerAdp;
import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Model.Bidang;
import sidev17.siits.proshare.Model.Permasalahan;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Array;
import sidev17.siits.proshare.Utils.ArrayMod;
import sidev17.siits.proshare.Utils.ViewTool.GaleriLoader;
import sidev17.siits.proshare.Utils.Utilities;

public class TambahPertanyaanWkr extends AppCompatActivity {

    private View parentUtama;
    private GridView wadahCell;
    private TabBarIcon tabBarIcon;

    private EditText teksJudul;
    private Spinner pilihanMajority;
    private SpinnerAdp adpMajority;
    private int idBidang=0;
    private EditText teksDeskripsi;

    private ImageView tmbCentang;

    private int tabIcon[]= {R.id.tambah_gambar, R.id.tambah_video, R.id.tambah_link};
    private final String WARNA_DITEKAN= "#4972AD"; //biruLaut
    private final String WARNA_TAK_DITEKAN= "#ADADAD"; //ambilWarna(R.color.abuLebihTua)

    private String pathFoto[];
    private String pathVideo[];
    private int posisiDipilih[];
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
        else{
            //==============================================
            //inisialkan Spinner Minggu, 11 Nov 2018, 21:05
            //==============================================
            ArrayList<Bidang> bdg = new ArrayList<>();
            ArrayList<String> bidang = DUMY_initArrayListMajority();
            for(int i = 0 ; i<bidang.size(); i++){
                Bidang bdang = new Bidang();
                bdang.setBidang(bidang.get(i));
                bdg.add(bdang);
            }
            initPilihanMajority(bdg);
            loadPilihanMajorityServer();
            pilihanMajority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    idBidang = position + 1;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

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
       // initTeksJudul();
        teksJudul.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
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
//        teksDeskripsi.setTextIsSelectable(true);
        teksDeskripsi.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        teksDeskripsi.setMaxLines(1000);
        teksDeskripsi.setSingleLine(false);
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
                int jmlCell= 0;
                if(ind== 0) {
                    wadahCell.setNumColumns(3);
                    initLoader(pathFoto, GaleriLoader.JENIS_FOTO);
                    jmlCell= pathFoto.length;
                } else if(ind== 1){
                    wadahCell.setNumColumns(3);
                    initLoader(pathVideo, GaleriLoader.JENIS_VIDEO_THUMBNAIL);
                    jmlCell= pathVideo.length;
                    RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams(50, 50);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    lp.setMargins(10, 10, 10, 10);
                    loader.tambahAksesoris(R.drawable.obj_indek_video_lingkaran, lp);
                } else if(ind== 2)
                    wadahCell.setNumColumns(1);

                int lebar = wadahCell.getWidth() / wadahCell.getNumColumns();
                AdapterPropertiCell adpCell = new AdapterPropertiCell(lebar, ind, jmlCell, view);
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

    ArrayList<String> DUMY_initArrayListMajority(){
        ArrayList<String> majority= new ArrayList<>();
        majority.add("Set majority");
       // majority.add("Beton");
        //majority.add("Teknik Lingkungan");
       // majority.add("Psychiatrist");
       // majority.add("Artist");
        return majority;
    }
    void initPilihanMajority(ArrayList<Bidang> majority){
        pilihanMajority= findViewById(R.id.tambah_majority);
        adpMajority= new SpinnerAdp(majority, getBaseContext());
        pilihanMajority.setAdapter(adpMajority);
    }

    void loadPilihanMajorityServer(){
        final ArrayList<Bidang> bdg = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Konstanta.DAFTAR_BIDANG, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                bdg.clear();
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Bidang bidang = new Bidang();
                        bidang.setId(obj.getString("id"));
                        bidang.setBidang(obj.getString("bidang"));
                        bdg.add(bidang);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                initPilihanMajority(bdg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(request);
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
        teksJudul.setTextIsSelectable(true);
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
        loader= new GaleriLoader(this, pathFile, 18, jenisFoto,
                R.layout.model_tambah_properti_cell, R.id.tambah_cell_pratinjau);
        loader.aturBentukFoto(GaleriLoader.BENTUK_KOTAK);
        loader.aturUkuranPratinjau(250);
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
                posisiDipilih= loader.ambilUrutanDipilih()[0];
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
                posisiDipilih= ArrayMod.kurangiArray(posisiDipilih, posisi);


                final TextView vIndDipilih = v.findViewById(R.id.tambah_cell_centang_no);
                final GridView liveQuestion= parentUtama.findViewById(R.id.tambah_properti_cell_dipilih);
                final int lebar= v.getLayoutParams().width;

                vIndDipilih.setSelected(false);
                AdapterPropertiDipilih adpDipilih= new AdapterPropertiDipilih(lebar, loader.ambilFotoDipilih());
                liveQuestion.setAdapter(adpDipilih);
                if(loader.ambilFotoDipilih().ukuran() == 0)
                    aturTinggiGrid(liveQuestion, 0);
                else if(loader.ambilFotoDipilih().ukuran() <= 3)
                    aturTinggiGrid(liveQuestion, lebar);
                else
                    aturTinggiGrid(liveQuestion, lebar+90);

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
                            if(loader.ambilFotoDipilih().ukuran() <= 3)
                                aturTinggiGrid(liveQuestion, lebar);
                            else
                                aturTinggiGrid(liveQuestion, lebar+90);
                        } else
                            loader.batalPilihFoto(posisi);
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
        String pathFotoDipilih[] = new String[0];
        if(loader != null)
            pathFotoDipilih= loader.ambilPathDipilih();
      //  int jmlUdahDiload= loader.ambilJmlUdahDiload();
      //  int batas= loader.ambilJmlDipilih();
        String daftarInd= "";
        for(int i=0;i<pathFotoDipilih.length;i++){
            Toast.makeText(this, pathFotoDipilih[i], Toast.LENGTH_SHORT).show();
        }
       // int dipilih[]= loader.ambilUrutanDipilih();
       // for(int i= 0; i< batas; i++)
       ////     daftarInd+= Integer.toString(dipilih[i]) +", ";
       // Toast.makeText(getBaseContext(), "dipilih \"" +Integer.toString(jmlUdahDiload) +"\": " +daftarInd, Toast.LENGTH_LONG).show();
        String judul= teksJudul.getText().toString();
        String deskripsi= teksDeskripsi.getText().toString();
        boolean verified= verifiedQuestion;
        String PId = Utilities.getUid();
        Toast.makeText(this, "judul : "+judul, Toast.LENGTH_SHORT).show();
        Permasalahan problem = new Permasalahan();
        problem.setproblem_owner(Utilities.getUserID(this).replace(",","."));
        problem.setpid(PId);
        problem.setproblem_desc(deskripsi);
        problem.setproblem_title(judul);
        problem.setStatus(verified?1:0);
        problem.setpicture_id("");
        problem.setmajority_id(String.valueOf(idBidang));
        for(int i =0 ; i<pathFotoDipilih.length;i++){
            Toast.makeText(this, pathFotoDipilih[i], Toast.LENGTH_SHORT).show();
        }
        ProgressDialog uploading = new ProgressDialog(this);
        if(pathFotoDipilih.length>0){
            String urlFotoTerupload[] = new String[1];
            Utilities.uploadFoto(0, pathFotoDipilih, urlFotoTerupload, PId, problem, this, uploading);
        }else{
            Utilities.tambahkanMasalah(this, PId, problem, uploading, 1);
        }
    }

    class AdapterPropertiCell extends BaseAdapter{

        int lebar;
        int indPosisi;
        int jml;

        View parent;

        AdapterPropertiCell(int lebar, int indPosisi, int jml, View parent){
            this.lebar= lebar;
            loader.aturLpWadahImg(new ViewGroup.LayoutParams(lebar, lebar));
            this.indPosisi= indPosisi;
            this.parent= parent;
            this.jml= jml;
        }

        @Override
        public int getCount() {
            if(indPosisi== 2)
                return 1;
            else
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

            if(indPosisi!= 2) {
                View view= loader.buatFoto(position); //getLayoutInflater().inflate(R.layout.model_tambah_properti_cell, null);
//                view.setLayoutParams(new ViewGroup.LayoutParams(lebar, lebar));

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
        private Array<Bitmap> itemDipilih;

        AdapterPropertiDipilih(int lebar, Array<Bitmap> itemDipilih){
            this.lebar= lebar;
            this.itemDipilih = itemDipilih;
        }

        @Override
        public int getCount() {
            return itemDipilih.ukuran();
        }

        @Override
        public Object getItem(int position) {
            return itemDipilih.ambil(position);
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
            bg.setImageBitmap(itemDipilih.ambil(position));
            bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent kePreview= new Intent(getBaseContext(), GaleriPreview.class);
/*
                    Bitmap arrayBm[]= new Bitmap[itemDipilih.size()];
                    arrayBm= itemDipilih.toArray(arrayBm);
                    kePreview.putExtra("foto", new ParcelHolder<Bitmap>(arrayBm));
*/
//                    kePreview.putParcelableArrayListExtra("foto", itemDipilih);
//                    kePreview.putStringArrayListExtra("judul", loader.ambilJudulDipilih());
//                    kePreview.putExtra("path", loader.ambilPathDipilih());
                    kePreview.putExtra("jenisPath", GaleriPreview.PATH_DEFAULT);
                    kePreview.putExtra("posisiFoto", position);

                    startActivity(kePreview);
                }
            });

            ImageView centang = view.findViewById(R.id.tambah_cell_centang);
            centang.setImageResource(R.drawable.icon_silang);
            centang.setBackgroundTintList(null);
            centang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
/*
                    Array<Bitmap> itemDipilihBaru= new Array<>();
                    for(int i= 0; i< itemDipilih.ukuran(); i++)
                        if(i != position)
                            itemDipilihBaru.tambah(itemDipilih.ambil(i));
*/
//                    GridView liveQuestion= parentUtama.findViewById(R.id.tambah_properti_cell_dipilih);
//                    liveQuestion.setAdapter(new AdapterPropertiDipilih(lebar, itemDipilihBaru));

                    loader.batalPilihFoto(posisiDipilih[position]);
                }
            });
//            ViewGroup vg= (ViewGroup) view;
//            vg.removeView(centang);

            return view;
        }
    }

//    ==14 Jan 2019: udah diperbaiki error + re-struktur GaleriLoader;
//    !!! YG BELUM: masih ada error di bagian silang di item yg dipilih di TambahPertanyaan
}
