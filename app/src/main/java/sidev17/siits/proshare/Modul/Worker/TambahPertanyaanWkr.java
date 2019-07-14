package sidev17.siits.proshare.Modul.Worker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sidev17.siits.proshare.Adapter.SpinnerAdp;
import sidev17.siits.proshare.Interface.PerubahanTerjemahListener;
import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Model.Bidang;
import sidev17.siits.proshare.Model.Pengguna;
import sidev17.siits.proshare.Model.Permasalahan;
import sidev17.siits.proshare.Model.Problem.Solusi;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Array;
import sidev17.siits.proshare.Utils.PackBahasa;
import sidev17.siits.proshare.Utils.Terjemahan;
import sidev17.siits.proshare.Utils.Ukuran;
import sidev17.siits.proshare.Utils.ViewTool.BitmapHandler;
import sidev17.siits.proshare.Utils.ViewTool.EditTextMod;
import sidev17.siits.proshare.Utils.ViewTool.GaleriLoader;
import sidev17.siits.proshare.Utils.Utilities;

import static sidev17.siits.proshare.Utils.Utilities.getSolusiImagesRef;
import static sidev17.siits.proshare.Utils.Utilities.getUserBahasa;


public class TambahPertanyaanWkr extends AppCompatActivity {
    public static final int JENIS_POST_SHARE= 10;
    public static final int JENIS_POST_TANYA= 11;
    public static final int JENIS_POST_JAWAB= 12;
    public static final int JENIS_POST_REVIEW= 13;
    public static final int JENIS_MUAT_PILIH_FOTO= 21;
    public static final int JENIS_MUAT_BATAL_PILIH_FOTO= 20;
    public static final int BIDANG_KOSONG= -1;
    public static final int BIDANG_TERISI= 0;

    protected int jenisPost= 0;

    private View parentUtama;
    private GridView wadahCell, liveQuestion;
    private TabBarIcon tabBarIcon;

    private EditText teksJudul;
    private TextView teksPjgJudul; //hanya muncul saat jenisPost == JENIS_POST_TANYA || jenisPost == JENIS_POST_SHARE
    private int pjgJudul= 0;
    private int maksEnter= 1;
    private Spinner pilihanMajority;
    private SpinnerAdp adpMajority;
    private int idBidang= BIDANG_KOSONG;
    private EditText teksDeskripsi;

    private ImageView tmbCentang;

    private int tabIcon[];
    private int tabIconInduk[];
    private final String WARNA_DITEKAN= "#FFFFFF"; //"#4972AD"; //biruLaut
    private final String WARNA_TAK_DITEKAN= "#C9C9C9"; //ambilWarna(R.color.abuTua)

    private String pathFoto[];
    private String pathVideo[];
    private String bidangPost = "", deskripsiPost = "", orangPost = "", waktuPost = "", idPost = "";
    private Array<Integer> posisiFotoDipilih= new Array<>();
    private Array<Integer> urutanFotoDipilih= new Array<>();
    private Array<Integer> posisiVideoDipilih= new Array<>();
    private Array<Integer> urutanVideoDipilih= new Array<>();

    private GaleriLoader loader;
    private int lebarCell= -1;

    //private String kategoriItem= "foto"; // "foto" atau "video" tergantung tab yang dipilih
    private int kategoriItem= GaleriLoader.JENIS_FOTO; // "foto" atau "video" tergantung tab yang dipilih
    private boolean transisiKategori= false;

    /*
    ================================
    untuk foto atau video yang habis didownload / dari server
    ================================
    */
    private int jmlLampiranDariServer = 0;
/*  private "FileGambar" gambarAwal[] //kalau bisa tipe Bitmap

================================
*/

    private Array<String> pathDipilih= new Array<>();
//    private Array<Integer> jenisDipilih= new Array<>(); //GaleriLoader.JENIS_FOTO; GaleriLoader.JENIS_VIDEO;
    private Array<BitmapHandler> bitmapDipilih= new Array<>();
    private Array<Integer> kategoriItemDipilih= new Array<>();
    private int urutanItemDipilih[]= {0,1,2,3,4,5,6,7,8,9}; //untuk urutan dipilih baik foto maupun video

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

    protected int idHalaman;
//    private boolean reviewShare= false;

/*
================================
Bisa-tidaknya pertanyaan/post dikirim
================================
*/
    private boolean bisaDikirim= false;
    private boolean bisaDikirimLangsungKeluar= false;
    private String pesanTakBisaDikirim= null;

    private String judulAwal= "";
    private String deskripsiAwal= "";
    private boolean samaDgAwal= true;
    private boolean lampiranSamaDgAwal= true; //@KOMEN_GAK_KEPAKE agar gampang pengecekan saat gakda lampiran
    private boolean teksSamaDgAwal= true;


/*
================================
Bagian EXPERT / TambahJawaban
================================
*/

    private boolean verifiedQuestion= false;

    private TextView vBidang;

    private LinearLayout vTindakan;
    private TextView vTolak;
    private TextView vLempar;
    private TextView vJawab;

    private LinearLayout vWadahPertanyaan;

//================================

    protected void aturIdHalaman(){
        idHalaman= R.layout.activity_tambah_pertanyaan_wkr;
    }
    protected void aturJenisPost(){}
/*
    protected void aturIdHalaman_Default(){
        aturIdHalaman(R.layout.activity_tambah_pertanyaan_wkr);
    }
*/
    void verifyQuestion(boolean verify){
        verifiedQuestion= verify;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        aturIdHalaman(getIntent().getIntExtra("idHalaman", R.layout.activity_tambah_pertanyaan_wkr));

        aturIdHalaman();
        aturJenisPost();

        initUrutanDipilih();
        parentUtama= getLayoutInflater().inflate(idHalaman, null);
        setContentView(parentUtama);

        tmbCentang= findViewById(R.id.tambah_ok);
        initTeksIsian();
        ambilData();
        wadahCell= findViewById(R.id.tambah_properti_cell_wadah);
        liveQuestion= findViewById(R.id.tambah_properti_cell_dipilih);
        tabBarIcon= new TabBarIcon((View) findViewById(R.id.tambah_properti_wadah),
                (View) findViewById(R.id.tambah_properti_icon));
        if(idHalaman == R.layout.activity_tambah_jawaban_exprt && jenisPost != JENIS_POST_REVIEW) {
            initTabIcon(new int[]{R.id.tambah_gambar_gmb/*, R.id.tambah_link_gmb*/});
            initTabIconInduk(new int[]{R.id.tambah_gambar/*, R.id.tambah_link*/});
            initTabBarIcon();
        } else if(idHalaman == R.layout.activity_tambah_pertanyaan_wkr){
            initTabIcon(new int[]{R.id.tambah_gambar_gmb, R.id.tambah_video_gmb/*, R.id.tambah_link_gmb*/});
            initTabIconInduk(new int[]{R.id.tambah_gambar, R.id.tambah_video/*, R.id.tambah_link*/});
            initTabBarIcon();
        }
        if(jenisPost == JENIS_POST_REVIEW) {
            findViewById(R.id.tambah_properti_icon).setVisibility(View.GONE);
            inisiasiOk();
        }
        else
            initAksiTekan();

        pathFoto= ambilPathGambar();
        pathVideo= ambilPathVideo();
//        isiFileFoto(pathFoto, 0, 12);
//        inisiasiFileFoto();

        if(idHalaman== R.layout.activity_tambah_jawaban_exprt) {
            vBidang= findViewById(R.id.tambah_bidang);
            vBidang.setVisibility(View.INVISIBLE);
            vTindakan= findViewById(R.id.tambah_tindakan);
            vTolak= findViewById(R.id.tambah_tindakan_tolak);
            vTolak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(jenisPost == JENIS_POST_JAWAB){
                        tandaiPertanyaanDitolak();
                        finish();
                    } else{
                        blokReview();
                    }
                }
            });
            vLempar= findViewById(R.id.tambah_tindakan_lempar);
            vLempar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(jenisPost == JENIS_POST_JAWAB){
                        lemparPertanyaan();
                        finish();
                    } else
                        abaikanReview();
                }
            });
            vLempar.setText("ABAIKAN");
            vJawab= findViewById(R.id.tambah_tindakan_jawab);
            vJawab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(jenisPost == JENIS_POST_JAWAB){
                        vTindakan.setVisibility(View.GONE);
                        tabBarIcon.aturTinggiTabDefault();
                        tabBarIcon.tampilkanIconTab();
                        EditTextMod.enableEditText(teksDeskripsi, InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, true);
                        inisiasiOk();
                    } else{
                        verifikasiReview();
                    }
                }
            });
            vJawab.setText("VALIDASI");

            translateAksi(vLempar, vTolak, vJawab);
            tabBarIcon.aturTinggiTab(tabBarIcon.tinggiTabDitekan());
            tabBarIcon.hilangkanIconTab();

            isiPertanyaan();
/*
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
*/
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
                    if(position <= 0)
                        idBidang= BIDANG_KOSONG;
                    else
                        idBidang = position + 1;
                    cekBidangKosong();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    idBidang = BIDANG_KOSONG;
                    cekBidangKosong();
                }
            });
            inisiasiOk();
        }
    }

    void translateAksi(TextView ... tv){
        for(int i = 0 ; i < tv.length; i ++)
            tv[i].setText(PackBahasa.tambahJawab[Terjemahan.indexBahasa(this)][i]);
        if(jenisPost == JENIS_POST_REVIEW)
            for(int i = 0; i < tv.length; i ++)
                tv[i].setText(PackBahasa.review[Terjemahan.indexBahasa(this)][i]);
    }

    private void initTabIcon(int tabIcon[]){
        this.tabIcon= tabIcon;
        for(int i= 0; i< tabIcon.length; i++) {
            findViewById(tabIcon[i]).getBackground().setAlpha(0);
        }
    }
    private void initTabIconInduk(int tabIconInduk[]){
        this.tabIconInduk= tabIconInduk;
    }

    /*
    ================================
    Bagian EXPERT / TambahJawaban
    ================================
    */

    private interface ReviewIf{
        void selesaiLoad();
    }

    private void verifikasiReview(){
        //SESUATU!!!
        reviewShare(1, new ReviewIf() {
            @Override
            public void selesaiLoad() {
                finish();
            }
        });
    }
    private void abaikanReview(){
        //SESUATU!!!
        reviewShare(2, new ReviewIf() {
            @Override
            public void selesaiLoad() {
                finish();
            }
        });
    }
    private void blokReview(){
        //SESUATU!!!
        reviewShare(3, new ReviewIf() {
            @Override
            public void selesaiLoad() {
                finish();
            }
        });
    }

    private void tandaiPertanyaanDitolak(){
        //////
        setProblemStatus("0", String.valueOf(Konstanta.PROBLEM_STATUS_REJECTED));
        finish();
    }
    private void lemparPertanyaan(){
        ///////
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.LEMPAR_PERTANYAAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> masalah = new HashMap<>();
                masalah.put("id_problem", idPost);
                masalah.put("id_orang", Utilities.getUserID(TambahPertanyaanWkr.this));
                return masalah;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private String ambilBidang(int idBidang){return null;}

    //hanya berisi deskripsi dan lampiran foto atau video pertanyaan
    //judul dan bidang pertanyaan sudah di-init di header
    private void isiPertanyaan(){
        new AsyncTask<Void, Void, String>(){

            @Override
            protected String doInBackground(Void... voids) {
                String output = "";
                output = Utilities.loadBidangKu(bidangPost, TambahPertanyaanWkr.this);
                return output;
            }

            @Override
            protected void onPostExecute(String s) {
                vBidang.setVisibility(View.VISIBLE);
                vBidang.setText(s);
                String[] akanDiterjemahkan = {s, waktuPost};
                Terjemahan.terjemahkanAsync(akanDiterjemahkan, "en", Utilities.getUserBahasa(TambahPertanyaanWkr.this), TambahPertanyaanWkr.this, new PerubahanTerjemahListener() {
                    @Override
                    public void dataBerubah(String[] kata) {
                        vBidang.setText(kata[0]);
                    }
                });
            }
        }.execute();

        View viewPertanyaan= getLayoutInflater().inflate(R.layout.model_timeline_pertanyaan, null);
        viewPertanyaan.findViewById(R.id.tl_vote).setVisibility(View.GONE);
        //Lakukan modifikasi data
        final TextView deskripsi = viewPertanyaan.findViewById(R.id.tl_deskripsi);
        final TextView waktu = viewPertanyaan.findViewById(R.id.tl_waktu);
        TextView nama = viewPertanyaan.findViewById(R.id.tl_nama_orang);
        TextView status = viewPertanyaan.findViewById(R.id.tl_status_orang);
        CircleImageView fotoProfil = viewPertanyaan.findViewById(R.id.tl_orang_gambar);
        LinearLayout lampiran = viewPertanyaan.findViewById(R.id.lampiran_pertanyaan);

        if(jenisPost == JENIS_POST_REVIEW) {
            deskripsi.setVisibility(View.GONE);
            waktu.setVisibility(View.GONE);
            nama.setVisibility(View.GONE);
            status.setVisibility(View.GONE);
            fotoProfil.setVisibility(View.GONE);
            viewPertanyaan.findViewById(R.id.tl_info_orang).setVisibility(View.GONE);
            viewPertanyaan.findViewById(R.id.tl_attribut_bawah).setVisibility(View.GONE);
            Utilities.loadFotoSolusi(new ArrayList<String>(), new ArrayList<String>(), lampiran, this, idPost);
            loadShare(new SolusiIf() {
                @Override
                public void loadDeskripsi(String deskripsi) {
                    teksDeskripsi.setText(deskripsi);
                    Terjemahan.terjemahkanAsync(new String[]{deskripsiPost, teksJudul.getText().toString()}, "en", Utilities.getUserBahasa(TambahPertanyaanWkr.this), TambahPertanyaanWkr.this, new PerubahanTerjemahListener() {
                        @Override
                        public void dataBerubah(String[] kata) {
                            teksDeskripsi.setText(kata[0]);
                            teksJudul.setText(kata[1]);
                        }
                    });
                }
            });
            vWadahPertanyaan= findViewById(R.id.tambah_wadah_pertanyaan);
            vWadahPertanyaan.addView(viewPertanyaan);
            return;
        }

        initOrang(nama, status, fotoProfil);
        Utilities.loadFotoLampiran(new ArrayList<String>(), new ArrayList<String>(), lampiran, this, idPost);
        deskripsi.setText(deskripsiPost);
        //waktu.setText(waktuPost);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("MMMM dd, yyyy");
        Date date = null;
        try {
            date = format1.parse(waktuPost);
            waktuPost = format2.format(date);
            waktu.setText(waktuPost);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Terjemahan.terjemahkanAsync(new String[]{deskripsiPost, teksJudul.getText().toString(), waktuPost}, "en", Utilities.getUserBahasa(this), this, new PerubahanTerjemahListener() {
            @Override
            public void dataBerubah(String[] kata) {
                deskripsi.setText(kata[0]);
                teksJudul.setText(kata[1]);
                waktu.setText(kata[2]);
            }
        });

        vWadahPertanyaan= findViewById(R.id.tambah_wadah_pertanyaan);
        vWadahPertanyaan.addView(viewPertanyaan);
    }

    private void initOrang(final TextView nama, final TextView status, final CircleImageView fotoProfil){
        Utilities.getUserRef(orangPost).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pengguna user = dataSnapshot.getValue(Pengguna.class);
                nama.setText(user.getNama());
                if(user.getPhotoProfile()!=null)
                    Glide.with(TambahPertanyaanWkr.this).load(user.getPhotoProfile()).into(fotoProfil);
                status.setText(PackBahasa.statusOrang[Terjemahan.indexBahasa(TambahPertanyaanWkr.this)][Pengguna.Status.statusStr(user.getStatus())]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//====================================

    private void cekBidangKosong(){
        cekBisaDikirim();
    }
    private void cekTeksSamaDgAwal(){
        if(judulAwal.compareTo(teksJudul.getText().toString()) == 0
        && deskripsiAwal.compareTo(teksDeskripsi.getText().toString()) == 0)
            teksSamaDgAwal= true;
        else teksSamaDgAwal= false;
        cekBisaDikirim();
    }
    private void cekLampiranSamaDgAwal(){
/*
        if(lampiranSamaDgAwal)
//        if(pathDipilih.ukuran() == 0 && jmlLampiranDariServer != liveQuestion.getCount())
            samaDgAwal= false;
        else
            samaDgAwal= true;
*/
        cekBisaDikirim();
    }
    private void cekBisaDikirim(){
        int warna;
        samaDgAwal= teksSamaDgAwal && lampiranSamaDgAwal;
/*
        if(!teksSamaDgAwal || !lampiranSamaDgAwal)
            samaDgAwal= false;
        else samaDgAwal= true;
*/
        if(idBidang == BIDANG_KOSONG)
            pesanTakBisaDikirim= "Isi bidang terlebih dahulu";
        else if(teksJudul.getText().length() <= 0)
            pesanTakBisaDikirim= "Isi judul terlebih dahulu";
        else {
            pesanTakBisaDikirim= null;
            if(samaDgAwal)
                bisaDikirimLangsungKeluar= true;
        }

        if(pesanTakBisaDikirim != null){
            warna= getResources().getColor(R.color.abuTua);
            bisaDikirim= false;
        }else{
            warna= getResources().getColor(R.color.biruLaut);
            bisaDikirim= true;
        }
//        if(tmbCentang != null)
        tmbCentang.getDrawable().setTint(warna);
    }

    private void kondisikanJenisPost(){
        if(jenisPost == JENIS_POST_TANYA){
            pjgJudul= 100;
            maksEnter= 6;
        } else if(jenisPost == JENIS_POST_SHARE){
            pjgJudul= 60;
            maksEnter= 4;
        }
        initTeksPjgJudul();
    }
    private void ambilData(){
        Intent intentSebelumnya= getIntent();

        if(jenisPost == 0)
            jenisPost= intentSebelumnya.getIntExtra("jenisPost", JENIS_POST_SHARE);

//        Toast.makeText(TambahPertanyaanWkr.this, "Jenis= " +jenisPost, Toast.LENGTH_LONG).show();
        kondisikanJenisPost();

        if(jenisPost == JENIS_POST_JAWAB || jenisPost == JENIS_POST_REVIEW){
            Bundle bundle = intentSebelumnya.getBundleExtra("paket_detail_pertanyaan");
            judulAwal= bundle.getString("judul_pertanyaan");
            deskripsiPost = bundle.getString("deskripsi_pertanyaan");
            bidangPost = bundle.getString("majority");
            idBidang= BIDANG_TERISI;
            orangPost = bundle.getString("owner");
            waktuPost = bundle.getString("waktu");
            idPost = bundle.getString("pid");
            teksJudul.setText(judulAwal);
        }else{
            judulAwal= intentSebelumnya.getStringExtra("judul");
            deskripsiAwal= intentSebelumnya.getStringExtra("deskripsi");
            ArrayList<String> urlFoto = intentSebelumnya.getStringArrayListExtra("urlFoto");
            String bidangStr= intentSebelumnya.getStringExtra("bidang");

            if(judulAwal == null)
                judulAwal= "";
            if(urlFoto != null){
                jmlLampiranDariServer= urlFoto.size();
                if(jmlLampiranDariServer > 0)
                    lampiranSamaDgAwal= true;
            }
            if(bidangStr != null)
                idBidang= BIDANG_TERISI;
            if(deskripsiAwal== null)
                deskripsiAwal= "";
/*
            if(judulAwal.length() > 0){
                teksJudul.setText(judulAwal);
                teksDeskripsi.setText(deskripsiAwal);
                idBidang= bidang;
                if(idHalaman == R.layout.activity_tambah_jawaban_exprt)
                    vBidang.setText(ambilBidang(idBidang));/*
                if(urlFoto!=null && urlFoto.size()>0){

                }* /
            }
*/
        }

        //file gambar yang udah diload dari server saat di activity DetailPertanyaan
        //gak harus array String
/*
        gambarAwal[]= intentSebelumnya.getStringArrayExtra("gambarAwal");
        if(gambarAwal != null && gambarAwal.length > 0){
            jmlLampiranDariServer= pathAwal.length;
            //Bitmap array=
        }
*/
    }
/*
    private void cekPanjangTeksIsian(){
        boolean isiaKosong= (teksJudul.getText().length() <= 0);// && teksDeskripsi.getText().length() <= 0);
        int warna= getResources().getColor(R.color.biruLaut);
        if(isiaKosong) warna= getResources().getColor(R.color.abuLebihTua);
        tmbCentang.getDrawable().setTint(warna);
        bisaDikirim= !isiaKosong;
    }
*/
    //dipanggil setelah initTeksIsian() dan kondisikanJenisPost() dipanggil karena pjgTeksJudul dan teksJudul harus diisi dulu
    private void initTeksPjgJudul(){
//        Toast.makeText(TambahPertanyaanWkr.this, "Jenis pjgTeks= " +jenisPost, Toast.LENGTH_LONG).show();
        if(jenisPost == JENIS_POST_TANYA || jenisPost == JENIS_POST_SHARE){
            teksPjgJudul= findViewById(R.id.pjg_judul);
            TextWatcher twPjgJudul= new TextWatcher() {
                private String teksSebelum= "";
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    teksSebelum= s.toString();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String pjg= s.length() +"/" +pjgJudul;
                    teksPjgJudul.setText(pjg);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    int jmlEnter= teksJudul.getLineCount();
                    int pjgTeks= s.length();
                    if(pjgTeks > pjgJudul || jmlEnter > maksEnter){
                        if(teksSebelum.length() > 0)
                            s.replace(0, s.length(), teksSebelum);
                        else
                            s.replace(pjgJudul, s.length(), "");
                        if(pjgTeks > pjgJudul)
                            Toast.makeText(TambahPertanyaanWkr.this, "Judul melebihi panjang yang diperbolehkan (" +pjgJudul +" huruf)", Toast.LENGTH_LONG).show();
                    }
                }
            };
            teksJudul.addTextChangedListener(twPjgJudul);
            String pjgJudulAwal= teksJudul.getText().length() +"/" +pjgJudul;
            teksPjgJudul.setText(pjgJudulAwal);
        }
    }
    private void initTeksIsian(){
        TextWatcher twIsian= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
/*
                if(s.length() > 0){
                    bisaDikirim= true;
                    tmbCentang.getDrawable().setTint(getResources().getColor(R.color.biruLaut));
                }else{
                    bisaDikirim= false;
                    tmbCentang.getDrawable().setTint(getResources().getColor(R.color.abuTua));
                }
*/
                cekTeksSamaDgAwal();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        teksJudul= findViewById(R.id.tambah_judul);

        teksJudul.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(tabBarIcon.trahirDitekan() > -1)
                    tabBarIcon.tekanItem(tabBarIcon.trahirDitekan());
                teksJudul.requestFocus();
                EditTextMod.tampilkanSoftKey(v, true);
/*
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
*/
                return false;
            }
        });

        // initTeksJudul();
        teksJudul.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        teksJudul.clearFocus();
        teksJudul.setMaxLines(4);
        teksJudul.setSingleLine(false);
        teksJudul.addTextChangedListener(twIsian);
        teksDeskripsi= findViewById(R.id.tambah_deskripsi);
        teksDeskripsi.setHint(PackBahasa.tambahKnowledge[Terjemahan.indexBahasa(this)][1]);
        teksDeskripsi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.isEnabled()){
                    if(tabBarIcon.trahirDitekan() > -1)
                        tabBarIcon.tekanItem(tabBarIcon.trahirDitekan());
                    teksDeskripsi.requestFocus();
                    EditTextMod.tampilkanSoftKey(teksDeskripsi, true);
/*
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
*/
                    return false;
                } else return true;
            }
        });
//        teksDeskripsi.setTextIsSelectable(true);
        teksDeskripsi.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        teksDeskripsi.setMaxLines(1000);
        teksDeskripsi.setSingleLine(false);
        teksDeskripsi.addTextChangedListener(twIsian);
//        teksDeskripsi.addTextChangedListener(twIsian);
        if(idHalaman == R.layout.activity_tambah_jawaban_exprt && jenisPost != JENIS_POST_REVIEW){
//            EditTextMod.enableEditText(teksDeskripsi, InputType.TYPE_NULL, false);
            EditTextMod.enableEditText(teksJudul, InputType.TYPE_NULL, false);
            EditTextMod.enableEditText(teksDeskripsi, InputType.TYPE_NULL, false);
        }
    }

    private int ambilWarna(int id){
        return getResources().getColor(id);
    }
    //Ganti setOnClickListener
    private void tekan(View v){
        tabBarIcon.tekanItem(v);
//        wadahCell.setY(tabBarIcon.ambilLetakY());
    }
    private void initTabBarIcon(){
        tabBarIcon.aturTabIcon(tabIcon);
        tabBarIcon.aturTabIconInduk(tabIconInduk);
        tabBarIcon.aturIdWarnaDitekan(WARNA_DITEKAN);
        tabBarIcon.aturIdWarnaTakDitekan(WARNA_TAK_DITEKAN);
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
        private int tabIconInduk[];

        private String idWarnaDitekan;
        private String idWarnaTakDitekan;

        private int trahirDitekan= -1;
        private boolean ditekanKah= false;

        View view;
        View viewIcon;

        private int tinggiDefault = -1;

        TabBarIcon(){}
        TabBarIcon(View view, View viewIcon){
            this.view= view;
            this.viewIcon= viewIcon;
            initTinggiDefault();
        }
        TabBarIcon(View view, int tabIcon[], int tabGaris[], String idWarnaDitekan, String idWarnaTakDitekan){
            this.view= view;
            this.tabIcon= tabIcon;
            this.tabGaris= tabGaris;
            this.idWarnaDitekan= idWarnaDitekan;
            this.idWarnaTakDitekan= idWarnaTakDitekan;
        }
        void aturTabIcon(int tabIcon[]){
            this.tabIcon= tabIcon;
        }
        void aturTabIconInduk(int tabIconInduk[]){
            this.tabIconInduk= tabIconInduk;
        }
        void aturTabGaris(int tabGaris[]){
            this.tabGaris= tabGaris;
        }
        void aturIdWarnaDitekan(String idWarnaDitekan){
            this.idWarnaDitekan= idWarnaDitekan;
        }
        void aturIdWarnaTakDitekan(String idWarnaTakDitekan){
            this.idWarnaTakDitekan= idWarnaTakDitekan;
        }

        int trahirDitekan(){
            return trahirDitekan;
        }

        void setClickListenerTab(View.OnClickListener l[]){
            for(int i= 0; i< tabIcon.length; i++) {
                View v= view.findViewById(tabIconInduk[i]);
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
                    aturTinggiTab(tinggiTabDitekan());
                }
                int jmlCell= 0;
                if(ind== 0) {
                    wadahCell.setNumColumns(3);
//                    kategoriItem= "video";
//                    simpanPosisiUrutan(posisiVideoDipilih, urutanVideoDipilih);
                    kategoriItem= GaleriLoader.JENIS_FOTO;
                    initLoader(pathFoto, GaleriLoader.JENIS_FOTO, posisiFotoDipilih, urutanFotoDipilih);
                    jmlCell= pathFoto.length;
                } else if(ind== 1){
                    if(idHalaman == R.layout.activity_tambah_pertanyaan_wkr){
                        wadahCell.setNumColumns(3);
//                    kategoriItem= "foto";
//                    simpanPosisiUrutan(posisiFotoDipilih, urutanFotoDipilih);
                        kategoriItem= GaleriLoader.JENIS_VIDEO;
                        initLoader(pathVideo, GaleriLoader.JENIS_VIDEO_THUMBNAIL, posisiVideoDipilih, urutanVideoDipilih);
                        jmlCell= pathVideo.length;
                        RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams(50, 50);
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        lp.setMargins(10, 10, 10, 10);
                        loader.tambahAksesoris(R.drawable.obj_indek_video_lingkaran, lp);
                    } else{
                        tekanItem(2);
                        return;
                    }
                } else if(ind== 2)
                    wadahCell.setNumColumns(1);

                int lebar = wadahCell.getWidth() / wadahCell.getNumColumns();
                AdapterPropertiCell adpCell = new AdapterPropertiCell(lebar, ind, jmlCell, view);
                wadahCell.setAdapter(adpCell);
            } else if(ditekanKah && ind == trahirDitekan){
                aturTinggiTabDefault();
            }
            tekanGantiWarna(ind);
        }
        void tekanGantiWarna(int ind){
            if(ind >= tabIcon.length)
                ind--;
            if(ind != trahirDitekan) {
                ImageView iconDitekanSkrg = view.findViewById(tabIcon[ind]);
                if (ditekanKah) {
                    ImageView iconDitekanTadi = view.findViewById(tabIcon[trahirDitekan]);
                    iconDitekanTadi.setColorFilter(Color.parseColor(idWarnaTakDitekan));
                    iconDitekanTadi.getBackground().setAlpha(0);
                }
                iconDitekanSkrg.setColorFilter(Color.parseColor(idWarnaDitekan));
                iconDitekanSkrg.getBackground().setAlpha(255);
                trahirDitekan= ind;
                aturDitekan(true);
            } else{
                ImageView iconDitekanTadi = view.findViewById(tabIcon[trahirDitekan]);
                iconDitekanTadi.setColorFilter(Color.parseColor(idWarnaTakDitekan));
                iconDitekanTadi.getBackground().setAlpha(0);
                trahirDitekan= -1;
                aturDitekan(false);
            }/*
            switch(ind){
                case 0:
                    kategoriItem= "foto";
                    break;
                case 1:
                    kategoriItem= "video";
                    break;
            }*/
        }
        void initTinggiDefault(){
            int ukuran[]= Ukuran.ukuranView(viewIcon);
            tinggiDefault= (int) (ukuran[1] /getResources().getDisplayMetrics().density);
        }
        void aturDitekan(boolean ditekan){
            ditekanKah= ditekan;
        }

        void aturTinggiTab(int tinggi){
            RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, tinggi);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            view.setLayoutParams(lp);
        }
        void aturTinggiTabDefault(){
            aturTinggiTab(tinggiDefault);
        }

        int tinggiTabDitekan(){
            return tinggiDefault +400;
        }

        void hilangkanIconTab(){
            viewIcon.setVisibility(View.GONE);
        }
        void tampilkanIconTab(){
            viewIcon.setVisibility(View.VISIBLE);
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
        adpMajority= new SpinnerAdp(majority, getBaseContext(), Color.parseColor("#000000"));
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
/*
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
*/

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

    private void initUrutanDipilih(){
        Array.PenungguTraverse<Integer, Void, Void> pngTraverse= new Array.PenungguTraverse<Integer, Void, Void>() {
            int urutanHilang;

            @Override
            public void awalTraverse(Integer... masukan) {
                urutanHilang= masukan[0];
            }

            @Override
            public Void traverse(Array array, int indek, Object isiArray) {
                int isi= (Integer) isiArray;
                if(isi >= urutanHilang)
                    array.ganti(indek, new Integer(--isi));
                return null;
            }
        };
        urutanFotoDipilih.aturPenungguTraverse(pngTraverse);
        urutanVideoDipilih.aturPenungguTraverse(pngTraverse);
    }
    private int ambilUrutan(int mulai){
        while(!kategoriItemDipilih.ambil(urutanItemDipilih[mulai]).equals(kategoriItem))
            mulai++;
        return mulai;
    }
    private void simpanPosisiUrutan(int posisi){
        if(kategoriItem == GaleriLoader.JENIS_FOTO){
            posisiFotoDipilih.tambah(posisi);
            urutanFotoDipilih.tambah(loader.ambilUrutanDipilih(posisi));
        } else if(kategoriItem == GaleriLoader.JENIS_VIDEO){
            posisiVideoDipilih.tambah(posisi);
            urutanVideoDipilih.tambah(loader.ambilUrutanDipilih(posisi));
        }
    }
/*
    private void simpanPosisiUrutan(Array<Integer> posisiDipilih, Array<Integer> urutanDipilih){
        if(kategoriItem.equals("foto")){/*
            if(posisiFotoDipilih == null){
                posisiFotoDipilih= new Array<>();
                urutanFotoDipilih= new Array<>();
            } else{* /
                posisiFotoDipilih.dariObjectArray(posisiDipilih);
                urutanFotoDipilih.dariObjectArray(urutanDipilih);
//            }
        } else if(kategoriItem.equals("video")){/*
            if(posisiVideoDipilih == null){
                posisiVideoDipilih= new Array<>();
                urutanVideoDipilih= new Array<>();
            } else{* /
                posisiVideoDipilih.dariObjectArray(posisiDipilih);
                urutanVideoDipilih.dariObjectArray(urutanDipilih);
//            }
        }
    }
*/

    private void hapusPosisiUrutan(int posisi){
        hapusPosisiUrutan(posisi, -1, kategoriItem);
    }
    private int hapusPosisiUrutan(int posisi, int urutan, int kategoriItem){
        int indekYgHilang= -1;
        if(kategoriItem == GaleriLoader.JENIS_FOTO){
//            int indek=;
//            Toast.makeText(this, "indek hapus= " +indek, Toast.LENGTH_SHORT).show();
            if(urutan == -1)
                urutan= posisiFotoDipilih.hapus(new Integer(posisi));
            else
                posisiFotoDipilih.hapus(urutan);
            indekYgHilang= urutanFotoDipilih.hapus(urutan);
        } else if(kategoriItem == GaleriLoader.JENIS_VIDEO){
            if(urutan == -1)
                urutan= posisiVideoDipilih.hapus(new Integer(posisi));
            else
                posisiVideoDipilih.hapus(urutan);
            indekYgHilang= urutanVideoDipilih.hapus(urutan);
        }
        urutanFotoDipilih.traverse(indekYgHilang);
        urutanVideoDipilih.traverse(indekYgHilang);
        return indekYgHilang;
    }


    void initLoader(String pathFile[], int jenisFoto, Array<Integer> posisiDipilih, final Array<Integer> urutanDipilih){
//        inisiasiArrayDipilih(pathFoto.length);
        loader= new GaleriLoader(this, pathFile, 18, jenisFoto,
                R.layout.model_tambah_properti_cell, R.id.tambah_cell_pratinjau);
        loader.aturBentukFoto(GaleriLoader.BENTUK_KOTAK);
        loader.aturUkuranPratinjau(250);
        sesuaikanLebarCell(250);
        loader.aturSumberBg(R.drawable.obj_gambar_kotak);
        loader.aturSumberBgTakBisa(R.drawable.obj_tanda_seru_lingkaran_garis);

        loader.aturModeBg(false);

        loader.aturAksiPilihFoto(new GaleriLoader.AksiPilihFoto() {
            @Override
            public void pilihFoto(View v, int posisi) {
//                cekLampiranSamaDgAwal();
                if(!transisiKategori){
                   // Toast.makeText(TambahPertanyaanWkr.this, "kategori= " +kategoriItem, Toast.LENGTH_SHORT).show();
                    int urutan= loader.ambilUrutanDipilih(posisi);
                    bitmapDipilih.tambah(loader.ambilBitmapHandler(urutan-1));
                    pathDipilih.tambah(loader.ambilPathFoto(posisi));
                    kategoriItemDipilih.tambah(kategoriItem);
                    simpanPosisiUrutan(posisi);
                }
                int indek= pathDipilih.indekAwal(loader.ambilPathFoto(posisi));
                TextView noUrut = v.findViewById(R.id.tambah_cell_centang_no);
                noUrut.setText(Integer.toString(ambilUrutan(indek)+1));
                noUrut.getBackground().setTint(getResources().getColor(R.color.biruLaut));
                noUrut.getBackground().setAlpha(255);
                noUrut.setSelected(true);

//                int lebar= lebarCell; //v.getLayoutParams().width;

                muatAdapterDipilih(JENIS_MUAT_PILIH_FOTO);
/*                try{
                } catch (Exception e){
//                    Toast.makeText(TambahPertanyaanWkr.this, "error= " +e.getMessage(), Toast.LENGTH_SHORT).show();
                    throw new RuntimeException("posisi= " +posisi +" urutan= " +loader.ambilUrutanDipilih(posisi) +" " +e.getMessage());
                }*/
            }
            @Override
            public void batalPilihFoto(View v, int posisi) {
                try{
                    int urutan= (loader.ambilUrutanDipilih(posisi)-1);
                  //  Toast.makeText(TambahPertanyaanWkr.this, "urutan hapus= " +urutan, Toast.LENGTH_SHORT).show();
                    batalPilihan(urutan, true);
//                    hapusPosisiUrutan(posisi, -1, kategoriItemDipilih.ambil(urutan));

                    Array<View> itemDipilih= loader.ambilViewDipilih();
                    TextView noUrut = v.findViewById(R.id.tambah_cell_centang_no);
                    noUrut.setText("");
                    noUrut.getBackground().setAlpha(0);
                    noUrut.setSelected(false);

                    for(int i= urutan+1; i< itemDipilih.ukuran(); i++) {
                        noUrut = itemDipilih.ambil(i).findViewById(R.id.tambah_cell_centang_no);
                        noUrut.setText(Integer.toString(ambilUrutan(i-1)+1));
                    }

                    if(posisi < jmlLampiranDariServer)
                        lampiranSamaDgAwal= false;
//                    muatAdapterDipilih();
//                    GridView liveQuestion= parentUtama.findViewById(R.id.tambah_properti_cell_dipilih);
//                    int lebar= lebarCell; //v.getLayoutParams().width;

                } catch (Exception e){
                    Toast.makeText(TambahPertanyaanWkr.this, "error= " +e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        loader.aturAksiBuffer(new GaleriLoader.AksiBuffer() {
            @Override
            public void bufferThumbnail(final int posisi, final int jmlBuffer) {
                View view= loader.ambilView(posisi);

                final TextView indDipilih = view.findViewById(R.id.tambah_cell_centang_no);
                indDipilih.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!indDipilih.isSelected()) {
                            loader.pilihFoto(posisi);
                        } else
                            loader.batalPilihFoto(posisi);
                    }
                });
            }

            @Override
            public void bufferUtama(int posisi, int jmlBuffer) {

            }
        });
        transisiKategori= true;
        if(posisiDipilih.ukuran() > 0 && urutanDipilih.ukuran() > 0){
            loader.aturIndDipilih(posisiDipilih, urutanDipilih);
       //     Toast.makeText(this, "posisi.length= " +posisiDipilih.ukuran(), Toast.LENGTH_SHORT).show();
        }
        transisiKategori= false;
    }

    private void batalPilihan(int urutan, boolean urutanSesuai){
        int kategoriSkrg= kategoriItem;
        int urutanMasingKategori= urutan;
        if(!urutanSesuai) {
            kategoriSkrg= kategoriItemDipilih.ambil(urutan);
            Array<Integer> indek= new Array<>(kategoriItemDipilih.indek(new Integer(kategoriSkrg)));
            urutanMasingKategori= indek.indekAwal(new Integer((urutan)));
        }
        urutan= hapusPosisiUrutan(-1, urutanMasingKategori, kategoriSkrg) -1;
        bitmapDipilih.hapus(urutan);
        pathDipilih.hapus(urutan);
        kategoriItemDipilih.hapus(urutan);
        muatAdapterDipilih(JENIS_MUAT_BATAL_PILIH_FOTO);
//        if(pathDipilih.ukuran() > 0)
//        else
//            aturTinggiGrid(liveQuestion, 0);
        Log.e("LOADER", "BATAL PILIH FOTO!!! ===== pathDipilih.ukuran()= " +pathDipilih.ukuran());
    }
    private void muatAdapterDipilih(int jenisMuat){
        AdapterPropertiDipilih adpDipilih= new AdapterPropertiDipilih(lebarCell);
        liveQuestion.setAdapter(adpDipilih);
        Log.e("LOADER", "BATAL PILIH FOTO!!! ===== loader.ambilBitmapDipilih().ukuran()= " +loader.ambilBitmapDipilih().ukuran());
        int ukuranBitmapDipilih= loader.ambilBitmapDipilih().ukuran();
        if(jenisMuat == JENIS_MUAT_BATAL_PILIH_FOTO)
            ukuranBitmapDipilih -= 1;

        if(ukuranBitmapDipilih == 0)
            aturTinggiGrid(liveQuestion, 0);
        else if(ukuranBitmapDipilih <= 3)
            aturTinggiGrid(liveQuestion, lebarCell);
        else
            aturTinggiGrid(liveQuestion, lebarCell+90);
    }
    void aturTinggiGrid(GridView grid, int tinggi){
        grid.getLayoutParams().height= tinggi;
    }

    public void inisiasiOk(){
        if(jenisPost == JENIS_POST_REVIEW)
            tmbCentang.setVisibility(View.GONE);
        else
            tmbCentang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(idHalaman == R.layout.activity_tambah_jawaban_exprt){
                        Dialog dialog= new Dialog(TambahPertanyaanWkr.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_kirim_jawaban);
                        TextView vKirim= dialog.findViewById(R.id.tindakan_kirim);
                        TextView txtDialogMessage = dialog.findViewById(R.id.tindakan_konfirmasi);
                        TextView txtLempar = dialog.findViewById(R.id.tindakan_lempar);
                        TextView txtKirim = dialog.findViewById(R.id.tindakan_kirim);

                        txtDialogMessage.setText(PackBahasa.tambahDialog[Terjemahan.indexBahasa(TambahPertanyaanWkr.this)][2]);
                        txtLempar.setText(PackBahasa.tambahDialog[Terjemahan.indexBahasa(TambahPertanyaanWkr.this)][0]);
                        txtKirim.setText(PackBahasa.tambahDialog[Terjemahan.indexBahasa(TambahPertanyaanWkr.this)][1]);

                        vKirim.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                kirimPertanyaan();
                            }
                        });
                        TextView VLempar= dialog.findViewById(R.id.tindakan_lempar);
                        vLempar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                lemparPertanyaan();
                                finish();
                            }
                        });
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                    else
                        kirimPertanyaan();
                }
            });
/*
        teksJudul.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(tabBarIcon.trahirDitekan() > -1)
                    tabBarIcon.tekanItem(tabBarIcon.trahirDitekan());
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
        teksDeskripsi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(tabBarIcon.trahirDitekan() > -1)
                    tabBarIcon.tekanItem(tabBarIcon.trahirDitekan());
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
*/
    }

    private void uploadKomentar(final Solusi solusi, final String id_problem, boolean fromShare){
        if(!fromShare) {
            final ProgressDialog uploading = new ProgressDialog(TambahPertanyaanWkr.this);
            uploading.setMessage("Sending solution...");
            if (TambahPertanyaanWkr.this != null)
                uploading.show();
        }
        Utilities.getUserRef(solusi.getOrang().replace(".", ",")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Pengguna user = dataSnapshot.getValue(Pengguna.class);
                StringRequest stringRequestSolusi = new StringRequest(Request.Method.POST, Konstanta.TAMBAH_SOLUSI_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                   // uploading.dismiss();
                                    finish();
                                    if(!obj.getBoolean("error"))
                                        setProblemStatus("1", String.valueOf(Konstanta.PROBLEM_STATUS_VERIFIED));
                                   // uploading.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    finish();
                                }

                                Log.d("kirim komentar :", response);
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // uploading.dismiss();
                        Toast.makeText(TambahPertanyaanWkr.this, "Failed to add comment!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> masalah = new HashMap<>();
                        masalah.put("id_problem", id_problem);
                        masalah.put("id_solusi", solusi.getId_solusi());
                        masalah.put("deskripsi", solusi.getDeskripsi());
                        masalah.put("orang", solusi.getOrang());
                        masalah.put("nama_orang", user.getNama());
                        masalah.put("status_orang", String.valueOf(user.getStatus()));
                        masalah.put("foto_orang", Utilities.cekNull(user.getPhotoProfile()));
                        return masalah;
                    }
                };
                Volley.newRequestQueue(TambahPertanyaanWkr.this).add(stringRequestSolusi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TambahPertanyaanWkr.this, "Failed to add comment!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProblemStatus(final String answered_status, final String status) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.SET_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("problem_status", response);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> masalah = new HashMap<>();
                masalah.put("id_problem", idPost);
                masalah.put("answered_status", answered_status);
                masalah.put("status", status);
                return masalah;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void kirimJawaban(String komentar, String idKomentator, String[] pathFoto, final String id_problem, final boolean fromShare){
        final Solusi sol = new Solusi();
        sol.setId_solusi(Utilities.getUid());
        sol.setDeskripsi(komentar);
        sol.setOrang(idKomentator);
         if(pathFoto.length>0){
            final ProgressDialog uploading = new ProgressDialog(this);
            String urlFotoTerupload[] = new String[1];
            uploadFotoKomentar(0, pathFoto, urlFotoTerupload, sol.getId_solusi(),this, uploading, new FotoKomentarListener() {
                @Override
                public void tambahkanKomentar(String[] url) {
                    tambahkanFotoKomentar(TambahPertanyaanWkr.this, sol.getId_solusi(), url);
                    String[] komen = new String[]{sol.getDeskripsi()};
                    Terjemahan.terjemahkanAsync(komen, getUserBahasa(TambahPertanyaanWkr.this), "en", TambahPertanyaanWkr.this, new PerubahanTerjemahListener() {
                        @Override
                        public void dataBerubah(String[] kata) {
                            sol.setDeskripsi(kata[0]);
                            uploadKomentar(sol, id_problem, fromShare);
                        }
                    });
                    setProblemStatus("1", String.valueOf(Konstanta.PROBLEM_STATUS_VERIFIED));
                }
            });
        }else{
             String[] komen = new String[]{sol.getDeskripsi()};
             Terjemahan.terjemahkanAsync(komen, getUserBahasa(TambahPertanyaanWkr.this), "en", TambahPertanyaanWkr.this, new PerubahanTerjemahListener() {
                 @Override
                 public void dataBerubah(String[] kata) {
                     sol.setDeskripsi(kata[0]);
                     uploadKomentar(sol, id_problem, fromShare);
                 }
             });
        }
    }

    public static void tambahkanFotoKomentar(final Context c, final String id_solusi, final String[] url_foto){
        Cache cache = new DiskBasedCache(c.getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue antrianRequest = new RequestQueue(cache, network);
        antrianRequest.start();
        for(int i = 0 ; i< url_foto.length; i++){
            Log.d("foto "+Integer.toString(i), url_foto[i]);
            //  Toast.makeText(c, "wes dijajal!", Toast.LENGTH_SHORT).show();
            final int posisi = i ;
            StringRequest sRequest = new StringRequest(Request.Method.POST, Konstanta.TAMBAH_SOLUSI_FOTO_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Respons foto", response);
                    // Toast.makeText(c, "Wes iso nambahno foto!\n"+response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Respons foto", error.toString());
                    //Toast.makeText(c, "Jek gak iso nambahno foto!/n"+error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> foto = new HashMap<>();
                    foto.put("id_solusi", id_solusi);
                    foto.put("url_foto", url_foto[posisi]);
                    return foto;
                }
            };
            Volley.newRequestQueue(c).add(sRequest);
        }
    }

    private interface FotoKomentarListener{
        void tambahkanKomentar(String[] url);
    }

    public static void uploadFotoKomentar(final int urutanFile, final String[] alamatFile, final String[] urlFile, final String id, final Activity c, final ProgressDialog uploading, final FotoKomentarListener ls){
        uploading.setMessage("uploading..."+" "+String.valueOf(urutanFile+1)+"/"+String.valueOf(alamatFile.length)+" 0%");
        uploading.show();
        Uri file = Uri.fromFile(new File(alamatFile[urutanFile]));
        final StorageReference filepath = getSolusiImagesRef(id, urutanFile);
        filepath.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String alamatUrl = uri.toString();
                        // Toast.makeText(c, "tes alamat : "+alamatUrl, Toast.LENGTH_SHORT).show();
                        int urutanSekarang = urutanFile+1;
                        String[] url = new String[urutanSekarang];
                        url[urutanFile]=alamatUrl;
                        if(urutanFile<alamatFile.length){
                            for(int i=0; i<urutanFile; i++){
                                url[i]=urlFile[i];
                            }
                            if(urutanFile!=alamatFile.length-1){
                                uploadFotoKomentar(urutanSekarang, alamatFile, url, id,  c, uploading, ls);
                            }else{
                                ls.tambahkanKomentar(url);
                            }
                        }else{
                            Toast.makeText(c, "Photos succesfully uploaded!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(c, "foto ke "+String.valueOf(urutanFile+1)+" gagal diupload!", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                uploading.setMessage("uploading..."+" "+String.valueOf(urutanFile+1)+"/"+String.valueOf(alamatFile.length)+" "+(int)progress+"%");
            }
        });
    }

    private interface SolusiIf{
        void loadDeskripsi(String deskripsi);
    }

    private void loadShare(final SolusiIf iface){
        StringRequest stringRequestShare = new StringRequest(Request.Method.POST, Konstanta.LOAD_SOLUSI_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject objSolusi = obj.getJSONObject("0");
                            String desc = objSolusi.getString("deskripsi");
                            Log.d("deskripsi : ", desc);
                            iface.loadDeskripsi(desc);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // uploading.dismiss();
                Toast.makeText(TambahPertanyaanWkr.this, "Failed to add comment!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> masalah = new HashMap<>();
                masalah.put("id_problem", idPost);
                return masalah;
            }
        };
        Volley.newRequestQueue(TambahPertanyaanWkr.this).add(stringRequestShare);
    }

    // ini buat mereview share
    private void reviewShare(final int tipe, final ReviewIf ls){
        final String update= (tipe == 1)?teksDeskripsi.getText().toString():"";
        final ProgressDialog uploading = new ProgressDialog(this);
        String pesan = PackBahasa.review[Terjemahan.indexBahasa(this)][2 + tipe];
        uploading.setMessage(pesan + "...");
        uploading.show();
        StringRequest stringRequestShare = new StringRequest(Request.Method.POST, Konstanta.REVIEW_SHARE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("review post :", response);
                        if(tipe == 1)
                            uploading.dismiss();
                        ls.selesaiLoad();
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // uploading.dismiss();
                Log.d("error review : ", error.toString());
                Toast.makeText(TambahPertanyaanWkr.this, "Failed to review!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> masalah = new HashMap<>();
                masalah.put("tipe", String.valueOf(tipe));
                masalah.put("update", update);
                masalah.put("id_expert", Utilities.getUserID(TambahPertanyaanWkr.this));
                masalah.put("id_problem", idPost);
                return masalah;
            }
        };
        Volley.newRequestQueue(TambahPertanyaanWkr.this).add(stringRequestShare);
    }

    public interface Uploading {
        void uploaded(ProgressDialog dialog);
    }

    //METHOD DUMMY!
    public void kirimPertanyaan(){
        if(bisaDikirimLangsungKeluar){
            finish();
            return;
        }else if(!bisaDikirim){
            Toast.makeText(this, pesanTakBisaDikirim, Toast.LENGTH_LONG).show();
            return;
        }
        //simpan pertanyaan.
        String pathFotoDipilih[] = new String[0];
        String pathVideoDipilih[] = new String[0];
        if(pathDipilih.ukuran()>0){
            int indekFoto[]= kategoriItemDipilih.indek(new Integer(GaleriLoader.JENIS_FOTO));
            int indekVideo[]= kategoriItemDipilih.indek(new Integer(GaleriLoader.JENIS_VIDEO));
            pathFotoDipilih = new String[indekFoto.length];
            pathDipilih.ambil(pathFotoDipilih, indekFoto);
            pathVideoDipilih = new String[indekVideo.length];
            pathDipilih.ambil(pathVideoDipilih, indekVideo);
        }
        if(jenisPost == JENIS_POST_JAWAB){
            kirimJawaban(teksDeskripsi.getText().toString(), Utilities.getUserID(this), pathFotoDipilih, idPost, false);
        } else {
            String judul= teksJudul.getText().toString();
            final String deskripsi= teksDeskripsi.getText().toString();
            boolean verified= verifiedQuestion;
            String PId = Utilities.getUid();
           // Toast.makeText(this, "judul : "+judul, Toast.LENGTH_SHORT).show();
            final Permasalahan problem = new Permasalahan();
            problem.setproblem_owner(Utilities.getUserID(this).replace(",","."));
            problem.setpid(PId);
            problem.setproblem_desc(deskripsi);
            problem.setproblem_title(judul);
            problem.setStatus(verified?1:0);
            problem.setpicture_id("");
            problem.setmajority_id(String.valueOf(idBidang));

            ProgressDialog uploading = new ProgressDialog(this);
            if(jenisPost == JENIS_POST_SHARE){
                final String[] pathPhoto = pathFotoDipilih;
                //problem.setproblem_desc(deskripsi);
                Utilities.tambahkanMasalah(this, problem, uploading, 1, jenisPost, new Uploading() {
                    @Override
                    public void uploaded(ProgressDialog dialog) {
                        dialog.dismiss();
                        kirimJawaban(deskripsi, Utilities.getUserID(TambahPertanyaanWkr.this),pathPhoto, problem.getpid(), true);
                    }
                });
            } else {
                if(pathFotoDipilih.length + pathVideoDipilih.length>0){
                    String urlLampiranTerUpload[] = new String[1];
                    if(pathFotoDipilih.length > 0)
                        Utilities.uploadLampiran(0, pathFotoDipilih, pathVideoDipilih, urlLampiranTerUpload, PId, problem, this, uploading, jenisPost, 1);
                    else
                        Utilities.uploadLampiran(0, pathFotoDipilih, pathVideoDipilih, urlLampiranTerUpload, PId, problem, this, uploading, jenisPost, 2);
                }else{
                    Utilities.tambahkanMasalah(this, problem, uploading, 1, jenisPost, new Uploading() {
                        @Override
                        public void uploaded(ProgressDialog dialog) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                }
            }
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

    private void sesuaikanLebarCell(int lebar){
        if(lebarCell == -1)
            lebarCell= lebar;
    }

    class AdapterPropertiDipilih extends BaseAdapter{
        private int lebar;

        AdapterPropertiDipilih(int lebar){
            this.lebar= lebar;
        }

        @Override
        public int getCount() {
            return bitmapDipilih.ukuran() + jmlLampiranDariServer;
        }

        @Override
        public Object getItem(int position) {
            return bitmapDipilih.ambil(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parentInn) {
            View view= getLayoutInflater().inflate(R.layout.model_tambah_properti_cell, null);
            ImageView bg= view.findViewById(R.id.tambah_cell_pratinjau);

            if(position >= jmlLampiranDariServer){
//                int urutan= kategoriItemDipilih.indekKe(position, kategoriItem);
//                bg.setImageBitmap(bitmapDipilih.ambil(position));
                bitmapDipilih.ambil(position).pasangKeImage(bg);
            }
            else{
/*
================
!!!!!!!!!!!
Bitmap dari gambar yang diload dari server
================
                bg.setImageBitmap(gambarAwal[position].ambilBitmap());
*/
                view.getLayoutParams().height= lebar;
                view.getLayoutParams().width= lebar;
            }
            //view.setLayoutParams(new ViewGroup.LayoutParams(lebar, lebar));


            bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent kePreview= new Intent(getBaseContext(), GaleriPreview.class);
                    String array[]= new String[pathDipilih.ukuran()];
                    kePreview.putExtra("path", pathDipilih.jadikanArray(array));
                    kePreview.putExtra("posisiFoto", position);
                    kePreview.putExtra("indikatorDitampilkan", false);
                    kePreview.putExtra("jenisFoto", GaleriLoader.JENIS_CAMPURAN);

                    startActivity(kePreview);
                }
            });

            ImageView centang = view.findViewById(R.id.tambah_cell_centang);
            centang.setImageResource(R.drawable.icon_silang);
            centang.getBackground().setAlpha(0);
            centang.setColorFilter(getResources().getColor(R.color.merah));
            centang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cekLampiranSamaDgAwal();
/*
================
!!!!!!!!!!!
Menghapus file gambar yg diload dari internet
================
*/
                    if(position < jmlLampiranDariServer);
/*
                        gambarAwal.hapus(position)
*/
                    else{
                        if(tabBarIcon.trahirDitekan() == 0)
                            loader.batalPilihFoto(posisiFotoDipilih.ambil(position));
                        else if(tabBarIcon.trahirDitekan() == 1)
                            loader.batalPilihFoto(posisiVideoDipilih.ambil(position));
                        else if(tabBarIcon.trahirDitekan() != 2)
                            batalPilihan(position -jmlLampiranDariServer, false);
                    }
                }
            });

            return view;
        }
    }

    @Override
    public void onBackPressed() {
        if(tabBarIcon.trahirDitekan() != -1)
            tabBarIcon.tekanItem(tabBarIcon.trahirDitekan());
        else{
            EditTextMod.tampilkanSoftKey(teksDeskripsi, false);
            super.onBackPressed();
        }
    }

    //    ==14 Jan 2019: udah diperbaiki error + re-struktur GaleriLoader;
//    !!! YG BELUM: masih ada error di bagian silang di item yg dipilih di TambahPertanyaan
}
