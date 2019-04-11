package sidev17.siits.proshare.Modul.Worker;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sidev17.siits.proshare.Interface.PerubahanTerjemahListener;
import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Model.Pengguna;
import sidev17.siits.proshare.Model.Problem.Solusi;
import sidev17.siits.proshare.Modul.AmbilGambarAct;
import sidev17.siits.proshare.Modul.ChatActivity;
import sidev17.siits.proshare.Modul.ProfileUserLainAct;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Array;
import sidev17.siits.proshare.Utils.PackBahasa;
import sidev17.siits.proshare.Utils.Terjemahan;
import sidev17.siits.proshare.Utils.View.ImgViewTouch;
import sidev17.siits.proshare.Utils.View.MenuBarView;
import sidev17.siits.proshare.Utils.ViewTool.Aktifitas;
import sidev17.siits.proshare.Utils.ViewTool.GaleriLoader;
import sidev17.siits.proshare.Utils.Utilities;
import sidev17.siits.proshare.Utils.Warna;

import static sidev17.siits.proshare.Utils.Utilities.getSolusiImagesRef;
import static sidev17.siits.proshare.Utils.Utilities.initViewSolusiLampiran;

public class DetailPertanyaanActivityWkr extends Aktifitas {
    public final int PENGGUNA_EXPERT_TERVERIFIKASI= 2;
    public final int PENGGUNA_EXPERT= 1;
    public final int PENGGUNA_BIASA= 0;

    private static final int AMBIL_GAMBAR= 11;

    private String emailPengguna= FirebaseAuth.getInstance().getCurrentUser().getEmail();

    private String namaPemilik;
    private String majorPemilik;
    private int statusPemilik;
    private String statusPost;

    private int totalVote = 0;
    private int voteStatus = 0;
    private String judulPertanyaan;
    private String majorityPertanyaan;
    private String deskripsiPertanyaan;
    private String emailOrang;
    private String urlFotoOrang;
    private String waktu;
    private String id_masalah;
    private int statusPertanyaan;
    private ArrayList<String> gambar;
    private ArrayList<String> fotoLampiran;
    private ArrayList<String> videoLampiran;
    private boolean adaSolusi;
    private boolean urlFotoOrangLoaded = false;

    private LinearLayout lampiran;
    private ImageView pertanyaanGambar1, pertanyaanGambar2, pertanyaanGambar3;
    private ImageView tmbBatal;
    private GridView gambarDipilih;

    private String orang[]= {"Mr. A", "Mr. B", "Mrs. C", "Will Smith"};
    private String job[]= {"Psychiatrist", "Motivator", "Engineer", "Actor"};
    private int kategoriExpert[]= {2, 1, 0, 2};

    private ArrayList<String> judulSolusi;
    private ArrayList<String> descSolusi;
    private ArrayList<Solusi> solusi;

    private ViewGroup viewLatar;
    private ListView detailPertanyaan;
    private DetailPertanyaanAdapter adpSolusi;
//    private GridView gambarPertanyaan;
    private View detailProfil;
    private AlertDialog dialog;
    private boolean profilDitampilkan= false;

    private int indViewKomentar[];
    private View viewPertanyaan;
    private View viewSolusi;
    private TextView teksJudul, teksMajority;
    private ArrayList<View> viewKomentar;

    private View viewBarKomen;
    private GaleriLoader loader;
    private Array<Integer> urutanDipilih; //urutan item yg dipilih
    private Array<Integer> posisiDipilih; //posisi item yg dipilih
    private int lbrBarKomen= -3;

    private MenuBarView menuBar;
    private LinearLayout header;

    private String majorStr = null;
    private String judulStr = null;
    private String descStr = null;

    private final int BATAS_BUFFER_VIEW= 7;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pertanyaan);

        teksJudul = findViewById(R.id.tl_judul);
        teksMajority = findViewById(R.id.tl_majority);
        isiDataPertanyaan();

        detailPertanyaan = (ListView) findViewById(R.id.detail_pertanyaan);
        adpSolusi= new DetailPertanyaanAdapter();
        detailPertanyaan.setAdapter(adpSolusi);

        viewLatar= (ViewGroup) detailPertanyaan.getRootView();
/*
        gambarPertanyaan= (GridView) findViewById(R.id.tl_gambar);
        int lebar= gambarPertanyaan.getWidth() /gambarPertanyaan.getNumColumns();
        AdapterGambar adpGambar= new AdapterGambar(lebar);
        gambarPertanyaan.setAdapter(adpGambar);

        if(gambar.size() <= 3)
            aturTinggiGrid(gambarPertanyaan, lebar);
        else
            aturTinggiGrid(gambarPertanyaan, lebar+90);
*/
        int jmlBuffer= (orang.length > BATAS_BUFFER_VIEW) ? BATAS_BUFFER_VIEW : orang.length;
        //initViewKomentar(jmlBuffer);
       // initIndViewKomentar(jmlBuffer);
        if(statusPost!=null)
            if(!statusPost.equalsIgnoreCase("10"))
                isiViewPertanyaan();
            else
                loadJudulBidang(teksJudul, teksMajority);
        initBarKomen();
        ((ViewGroup) findViewById(R.id.detail_bar_komen)).addView(viewBarKomen);
        if(emailOrang.equalsIgnoreCase(Utilities.getUserID(this))) {
            initMenuBar();
        }

        addHit();
    }

    private void addHit() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.ADD_HIT,
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
                masalah.put("id_problem", id_masalah);
                return masalah;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void initMenuBar(){
        menuBar= findViewById(R.id.tl_opsi);
        header = findViewById(R.id.tl_header);
        menuBar.setVisibility(View.VISIBLE);
        header.post(new Runnable() {
            @Override
            public void run() {
                menuBar.getLayoutParams().height = header.getHeight();
            }
        });
        int gmbOpsi[]= {R.drawable.icon_edit,
                R.drawable.icon_hapus};
        int tersedia[]= {menuBar.ITEM_TERSEDIA, menuBar.ITEM_TERSEDIA};
        menuBar.aturGmbItem(gmbOpsi);
        menuBar.aturItemTersedia(tersedia);
//        menuBar.aturArahBar(menuBar.ARAH_VERTIKAL);
        menuBar.aturLetakBarRelatif(menuBar.BAR_DI_BAWAH);
        menuBar.aturWarnaTersedia("#FFFFFF");
//        menuBar.aturWarnaTakTersedia();
        menuBar.aturWarnaKuat(Warna.ambilStringWarna(getResources().getColor(R.color.biruLaut)));
        menuBar.aturLatarInduk_Akhir(R.drawable.latar_kotak_tumpul_atas);
        menuBar.aturPenungguKlikBar(new MenuBarView.PenungguKlik_BarView() {
            @Override
            public void klik(MenuBarView v, boolean menuDitampilkan) {
                if(!menuDitampilkan)
                    v.aturWarnaInduk("#FFFFFF");
                else
                    v.aturWarnaInduk(Warna.ambilStringWarna(getResources().getColor(R.color.biruLaut)));
            }
        });
        menuBar.aturAksiKlikItem(0, new ImgViewTouch.PenungguKlik() {
            @Override
            public void klik(View v) {
                if(Utilities.isStoragePermissionGranted(DetailPertanyaanActivityWkr.this)){
                    if(majorStr == null || judulStr==null || descStr==null)
                        Toast.makeText(DetailPertanyaanActivityWkr.this, "Content has not fully loaded!", Toast.LENGTH_SHORT).show();
                    else{
                        Intent keEdit= new Intent(DetailPertanyaanActivityWkr.this, TambahPertanyaanWkr.class);

                        keEdit.putExtra("judul", judulPertanyaan);
                        keEdit.putExtra("deskripsi", deskripsiPertanyaan);
                        keEdit.putExtra("bidang", majorStr);
                        keEdit.putExtra("urlFoto", fotoLampiran);
//                keEdit.putExtra("gambarAwal", gambarAwal);

                        startActivity(keEdit);
                    }
                }else {
                    Toast.makeText(DetailPertanyaanActivityWkr.this, "Failed to get storage permission!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        menuBar.aturAksiKlikItem(1, new ImgViewTouch.PenungguKlik() {
            @Override
            public void klik(View v) {
                hapusPertanyaan();
            }
        });

        daftarkanBatas(menuBar.ambilBatas());
        daftarkanAksiBackPress(new Aktifitas.AksiBackPress() {
            @Override
            public boolean backPress() {
                if(menuBar.menuDitampilkan()){
                    menuBar.klik();
                    return true;
                }
                return false;
            }
        });
    }

    private void hapusPertanyaan() {
        //lakukan sesuatu...
        //kalau bisa pake konfirmasi dulu
        final Dialog dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_kirim_jawaban);
        TextView vHapus= dialog.findViewById(R.id.tindakan_kirim);
        TextView vBatal = dialog.findViewById(R.id.tindakan_lempar);
        TextView vKonfirmasi = dialog.findViewById(R.id.tindakan_konfirmasi);
        terjemahkanMenuBar(vKonfirmasi, vBatal, vHapus);
        vHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailPertanyaanActivityWkr.this, PackBahasa.toastDetail[Terjemahan.indexBahasa(getApplicationContext())][0], Toast.LENGTH_SHORT).show();
                Utilities.setHapusPertanyaaanStatus(DetailPertanyaanActivityWkr.this, "hapus");
                hapusDariServer();
                finish();
            }
        });
        vBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void hapusDariServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.DELETE_PROBLEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("delete oke", response);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailPertanyaanActivityWkr.this, "Something wrong must happen!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> masalah = new HashMap<>();
                masalah.put("id_problem", id_masalah);
                return masalah;
            }
        };
        Volley.newRequestQueue(DetailPertanyaanActivityWkr.this).add(stringRequest);
    }

    private void terjemahkanMenuBar(TextView ... txt){
        for(int i = 0; i < 3; i++)
            txt[i].setText(PackBahasa.menuBarDetail[Terjemahan.indexBahasa(this)][i]);
    }

    public void setTotalVote(int totalVote){
        this.totalVote = totalVote;
    }

    public int getTotalVote(){
        return totalVote;
    }

    public int getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(int voteStatus) {
        this.voteStatus = voteStatus;

    }

    private void isiDataPertanyaan(){
        Bundle paketDetailPertanyaan= getIntent().getBundleExtra("paket_detail_pertanyaan");

        judulSolusi = new ArrayList<>();
        descSolusi = new ArrayList<>();
        solusi = new ArrayList<>();
        viewKomentar = new ArrayList<>();
        fotoLampiran = new ArrayList<>();
        videoLampiran = new ArrayList<>();
        adaSolusi = true;

        judulSolusi.add("What should I do whe this happen?");
        judulSolusi.add("How to gain inspiration?");
        judulSolusi.add("How to else?");
        judulSolusi.add("Stay cool");

        descSolusi.add("I do this everyday, but somehow...");
        descSolusi.add("When it happens, I don't know what to do. I need inspiration.");
        descSolusi.add("bla bla bla...");
        descSolusi.add("Just bel cool bro!");
        judulPertanyaan= paketDetailPertanyaan.getString("judul_pertanyaan");
        deskripsiPertanyaan= paketDetailPertanyaan.getString("deskripsi_pertanyaan");
        emailOrang = paketDetailPertanyaan.getString("owner");
        majorityPertanyaan = paketDetailPertanyaan.getString("majority");
        waktu = paketDetailPertanyaan.getString("waktu");
        id_masalah = paketDetailPertanyaan.getString("pid");
        statusPost = paketDetailPertanyaan.getString("status_post");
        statusPertanyaan = paketDetailPertanyaan.getInt("status", 0);
        gambar= ambilGambarDariServer();
        Toast.makeText(this, id_masalah, Toast.LENGTH_SHORT).show();
        cekSolusi();
    }
    ArrayList<String> ambilGambarDariServer(){
        //lakukan sesuatu...
        return new ArrayList<String>();
    }
    private void initIndViewKomentar(int init){
        for(int i= 0; i<indViewKomentar.length; i++)
            indViewKomentar[i]= init;
    }

    private void initViewKnowledge(){

    }

    private void isiViewKomentar(final int ind){
        View view= getLayoutInflater().inflate(R.layout.model_kolom_komentar, null, false);

        TextView teksOrang= view.findViewById(R.id.komentar_orang_nama);
        final TextView teksJob= view.findViewById(R.id.komentar_orang_job);
        final TextView teksSolusi= view.findViewById(R.id.komentar_deskripsi);
        final TextView teksTanggal = view.findViewById(R.id.komentar_waktu);
        ImageView centang= view.findViewById(R.id.komentar_orang_centang);
        CircleImageView fotoProfil= view.findViewById(R.id.komentar_orang_gambar);
        LinearLayout lampiran = view.findViewById(R.id.lampiran_solusi);

        loadSolusiLampiran(lampiran, solusi.get(ind).getId_solusi(), this);


        final TextView jmlSuka= view.findViewById(R.id.komentar_vote_posisi);
        //Belum selesai
//        jmlSuka.setText(Integer.toString(ambilDaftarVoter("id").length));

        View profil= view.findViewById(R.id.komentar_orang_dp);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perbesar FotoProfl
                munculkanDetailProfil(ind);
            }
        });
        final ImageView tmbVoteUp= view.findViewById(R.id.komentar_vote_up);
        final ImageView tmbVoteDown= view.findViewById(R.id.komentar_vote_down);
        final TextView voteJumlah = view.findViewById(R.id.komentar_vote_posisi);
        Utilities.loadVoteCountSolusi(tmbVoteUp, tmbVoteDown, voteJumlah, this, solusi.get(ind).getId_solusi(), solusi.get(ind));
        tmbVoteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (solusi.get(ind).getVoteStatus() == 1) {
                    solusi.get(ind).setVoteStatus(0);
                    solusi.get(ind).setTotalVote(solusi.get(ind).getTotalVote()-1);
                    voteJumlah.setText(String.valueOf(solusi.get(ind).getTotalVote()));
                    tampilanToastDiterjemahkan("You Unvoted");
                    tmbVoteUp.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    if(solusi.get(ind).getVoteStatus() == 0) {
                        solusi.get(ind).setTotalVote(solusi.get(ind).getTotalVote() + 1);
                        voteJumlah.setText(String.valueOf(solusi.get(ind).getTotalVote()));
                    } else{
                        solusi.get(ind).setTotalVote(solusi.get(ind).getTotalVote() + 2);
                        voteJumlah.setText(String.valueOf(solusi.get(ind).getTotalVote()));
                    }
                    solusi.get(ind).setVoteStatus(1);
                    tampilanToastDiterjemahkan("You Voted Up");
                    tmbVoteUp.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.biruLaut), android.graphics.PorterDuff.Mode.SRC_IN);
                    tmbVoteDown.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                Utilities.voteSolusi("1", DetailPertanyaanActivityWkr.this, solusi.get(ind).getId_solusi());
            }
        });
        tmbVoteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (solusi.get(ind).getVoteStatus() == -1) {
                    solusi.get(ind).setVoteStatus(0);
                    solusi.get(ind).setTotalVote(solusi.get(ind).getTotalVote()+1);
                    voteJumlah.setText(String.valueOf(solusi.get(ind).getTotalVote()));
                    tampilanToastDiterjemahkan("You Unvoted");
                    tmbVoteDown.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    if(solusi.get(ind).getVoteStatus() == 0) {
                        solusi.get(ind).setTotalVote(solusi.get(ind).getTotalVote() - 1);
                        voteJumlah.setText(String.valueOf(solusi.get(ind).getTotalVote()));
                    } else{
                        solusi.get(ind).setTotalVote(solusi.get(ind).getTotalVote() - 2);
                        voteJumlah.setText(String.valueOf(solusi.get(ind).getTotalVote()));
                    }
                    solusi.get(ind).setVoteStatus(-1);
                    tampilanToastDiterjemahkan("You Voted Down");
                    tmbVoteDown.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.biruLaut), android.graphics.PorterDuff.Mode.SRC_IN);
                    tmbVoteUp.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                Utilities.voteSolusi("0", DetailPertanyaanActivityWkr.this, solusi.get(ind).getId_solusi());
            }
        });
        teksOrang.setText(solusi.get(ind).getNamaOrang());
        //teksJob.setText("wkwkwk");
        teksSolusi.setText(solusi.get(ind).getDeskripsi());
        if(!solusi.get(ind).getFotoOrang().equals("null"))
            Glide.with(this).load(solusi.get(ind).getFotoOrang()).into(fotoProfil);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("MMMM dd, yyyy");
        Date date = null;
        String waktu = "";
        try {
            date = format1.parse(solusi.get(ind).getTimestamp());
            waktu = format2.format(date);
            teksTanggal.setText(waktu);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String status = "";
        switch (solusi.get(ind).getStatus()){
            case "200" :
                status = "Worker";
                teksJob.setText(status);
                break;
            case "201" :
                status = "Expert";
                teksJob.setText(status);
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
                break;
            case "202" :
                status = "Verified Expert";
                teksJob.setText(status);
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);
                break;
        }

        String diterjemahkanLagi[] = {solusi.get(ind).getDeskripsi(), status, waktu};
        Terjemahan.terjemahkanAsync(diterjemahkanLagi, "en", Utilities.getUserBahasa(this), this, new PerubahanTerjemahListener() {
            @Override
            public void dataBerubah(String[] kata) {
                teksSolusi.setText(kata[0]);
                teksJob.setText(kata[1]);
                teksTanggal.setText(kata[2]);
            }
        });

       viewKomentar.add(view);
       // indViewKomentar[indView]= ind;
    }
    private void isiViewSolusi(){
        Log.d("solusi : ", "init view solusi");
        viewSolusi= getLayoutInflater().inflate(R.layout.model_solusi_expert, null, false);
        if(viewPertanyaan == null)
            viewSolusi.findViewById(R.id.komentar_header).setVisibility(View.GONE);
        TextView teksOrang= viewSolusi.findViewById(R.id.komentar_orang_nama);
        final TextView teksJob= viewSolusi.findViewById(R.id.komentar_orang_job);
        final TextView teksSolusi= viewSolusi.findViewById(R.id.komentar_deskripsi);
        ImageView centang= viewSolusi.findViewById(R.id.komentar_orang_centang);
        CircleImageView fotoProfil= viewSolusi.findViewById(R.id.komentar_orang_gambar);
        final TextView teksTanggal = viewSolusi.findViewById(R.id.komentar_waktu);
        LinearLayout lampiran = viewSolusi.findViewById(R.id.lampiran_solusi);

        loadSolusiLampiran(lampiran, solusi.get(0).getId_solusi(), this);

        final TextView jmlSuka= viewSolusi.findViewById(R.id.komentar_vote_posisi);
        View profil= viewSolusi.findViewById(R.id.komentar_orang_dp);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perbesar FotoProfl
                munculkanDetailProfil(0);
            }
        });
        final ImageView tmbVoteDown= viewSolusi.findViewById(R.id.komentar_vote_down);
        final ImageView tmbVoteUp= viewSolusi.findViewById(R.id.komentar_vote_up);
        final TextView voteJumlah = viewSolusi.findViewById(R.id.komentar_vote_posisi);
        Utilities.loadVoteCountSolusi(tmbVoteUp, tmbVoteDown, voteJumlah, this, solusi.get(0).getId_solusi(), solusi.get(0));
        tmbVoteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (solusi.get(0).getVoteStatus() == 1) {
                    solusi.get(0).setVoteStatus(0);
                    solusi.get(0).setTotalVote(solusi.get(0).getTotalVote()-1);
                    voteJumlah.setText(String.valueOf(solusi.get(0).getTotalVote()));
                    tampilanToastDiterjemahkan("You Unvoted");
                    tmbVoteUp.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    if(solusi.get(0).getVoteStatus() == 0) {
                        solusi.get(0).setTotalVote(solusi.get(0).getTotalVote() + 1);
                        voteJumlah.setText(String.valueOf(solusi.get(0).getTotalVote()));
                    } else{
                        solusi.get(0).setTotalVote(solusi.get(0).getTotalVote() + 2);
                        voteJumlah.setText(String.valueOf(solusi.get(0).getTotalVote()));
                    }
                    solusi.get(0).setVoteStatus(1);
                    tampilanToastDiterjemahkan("You Voted Up");
                    tmbVoteUp.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.biruLaut), android.graphics.PorterDuff.Mode.SRC_IN);
                    tmbVoteDown.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                Utilities.voteSolusi("1", DetailPertanyaanActivityWkr.this, solusi.get(0).getId_solusi());
            }
        });
        tmbVoteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (solusi.get(0).getVoteStatus() == -1) {
                    solusi.get(0).setVoteStatus(0);
                    solusi.get(0).setTotalVote(solusi.get(0).getTotalVote()+1);
                    voteJumlah.setText(String.valueOf(solusi.get(0).getTotalVote()));
                    tampilanToastDiterjemahkan("You Unvoted");
                    tmbVoteDown.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    if(solusi.get(0).getVoteStatus() == 0) {
                        solusi.get(0).setTotalVote(solusi.get(0).getTotalVote() - 1);
                        voteJumlah.setText(String.valueOf(solusi.get(0).getTotalVote()));
                    } else{
                        solusi.get(0).setTotalVote(solusi.get(0).getTotalVote() - 2);
                        voteJumlah.setText(String.valueOf(solusi.get(0).getTotalVote()));
                    }
                    solusi.get(0).setVoteStatus(-1);
                    tampilanToastDiterjemahkan("You Voted Down");
                    tmbVoteDown.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.biruLaut), android.graphics.PorterDuff.Mode.SRC_IN);
                    tmbVoteUp.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                Utilities.voteSolusi("0", DetailPertanyaanActivityWkr.this, solusi.get(0).getId_solusi());
            }
        });

        teksOrang.setText(solusi.get(0).getNamaOrang());
       // teksJob.setText("wkwkwkwkwkwk");
        teksSolusi.setText(solusi.get(0).getDeskripsi());
        if(!solusi.get(0).getFotoOrang().equals("null")){
            fotoProfil.setPadding(0, 0, 0, 0);
            Glide.with(this).load(solusi.get(0).getFotoOrang()).into(fotoProfil);
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("MMMM dd, yyyy");
        Date date = null;
        String waktu = "";
        try {
            date = format1.parse(solusi.get(0).getTimestamp());
            waktu = format2.format(date);
            teksTanggal.setText(waktu);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       // Log.d("solusi status", solusi.get(0).getStatus());
        String status ="";
        switch (solusi.get(0).getStatus()){
            case "200" :
                status = "Worker";
                teksJob.setText(status);
                break;
            case "201" :
                status = "Expert";
                teksJob.setText(status);
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
                break;
            case "202" :
                status = "Verfied Expert";
                teksJob.setText(status);
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);
                break;
        }
        String diterjemahkanLagi[] = {solusi.get(0).getDeskripsi(), status, waktu};
        Terjemahan.terjemahkanAsync(diterjemahkanLagi, "en", Utilities.getUserBahasa(this), this, new PerubahanTerjemahListener() {
            @Override
            public void dataBerubah(String[] kata) {
                teksSolusi.setText(kata[0]);
                teksJob.setText(kata[1]);
                teksTanggal.setText(kata[2]);
            }
        });
    }
    private void isiViewPertanyaan(){
        viewPertanyaan= getLayoutInflater().inflate(R.layout.model_timeline_pertanyaan, null, false);
        final TextView teksDeskripsi= viewPertanyaan.findViewById(R.id.tl_deskripsi);
        TextView teksNama = viewPertanyaan.findViewById(R.id.tl_nama_orang);
        TextView teksStatusOrang = viewPertanyaan.findViewById(R.id.tl_status_orang);
        TextView teksWaktu = viewPertanyaan.findViewById(R.id.tl_waktu);
        CircleImageView fotoProfil = viewPertanyaan.findViewById(R.id.tl_orang_gambar);
        lampiran = viewPertanyaan.findViewById(R.id.lampiran_pertanyaan);
        final LinearLayout statusPertanyaan = viewPertanyaan.findViewById(R.id.tl_status);
        final TextView voteJumlah = viewPertanyaan.findViewById(R.id.tl_vote_posisi);
        initOrang(teksNama, teksStatusOrang, fotoProfil);

        final TextView jmlSuka= viewPertanyaan.findViewById(R.id.komentar_vote_posisi);
        //Belum selesai
//        jmlSuka.setText(Integer.toString(ambilDaftarVoter("id").length));

        View profil= viewPertanyaan.findViewById(R.id.tl_orang_gambar);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perbesar FotoProfl
                munculkanDetailProfil(-1);
            }
        });
        final ImageView tmbVoteDown= viewPertanyaan.findViewById(R.id.tl_vote_down);
        final ImageView tmbVoteUp= viewPertanyaan.findViewById(R.id.tl_vote_up);
        Utilities.loadVoteCountProblem(tmbVoteUp, tmbVoteDown, voteJumlah, this, id_masalah);
        tmbVoteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voteStatus == 1) {
                    voteStatus = 0;
                    voteJumlah.setText(String.valueOf(--totalVote));
                    tampilanToastDiterjemahkan("You Unvoted");
                    tmbVoteUp.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    if(voteStatus == 0)
                        voteJumlah.setText(String.valueOf(++totalVote));
                    else{
                        totalVote += 2;
                        voteJumlah.setText(String.valueOf(totalVote));
                    }
                    voteStatus = 1;
                    tampilanToastDiterjemahkan("You Voted Up");
                    tmbVoteUp.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.biruLaut), android.graphics.PorterDuff.Mode.SRC_IN);
                    tmbVoteDown.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                Utilities.voteProblem("1", tmbVoteUp, tmbVoteDown, voteJumlah, DetailPertanyaanActivityWkr.this, id_masalah);
            }
        });
        tmbVoteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voteStatus == -1) {
                    voteStatus = 0;
                    voteJumlah.setText(String.valueOf(++totalVote));
                    tampilanToastDiterjemahkan("You Unvoted");
                    tmbVoteDown.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    if(voteStatus == 0)
                        voteJumlah.setText(String.valueOf(--totalVote));
                    else{
                        totalVote -= 2;
                        voteJumlah.setText(String.valueOf(totalVote));
                    }
                    voteStatus = -1;
                    tampilanToastDiterjemahkan("You Voted Down");
                    tmbVoteDown.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.biruLaut), android.graphics.PorterDuff.Mode.SRC_IN);
                    tmbVoteUp.setColorFilter(ContextCompat.getColor(DetailPertanyaanActivityWkr.this, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                Utilities.voteProblem("0", tmbVoteUp, tmbVoteDown, voteJumlah, DetailPertanyaanActivityWkr.this, id_masalah);
            }
        });

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("MMMM dd, yyyy");
        Date date = null;
        try {
            date = format1.parse(waktu);
            waktu = format2.format(date);
            teksWaktu.setText(waktu);
        } catch (ParseException e) {
            e.printStackTrace();
        }
      //  System.out.println();

        String judulYgDituliskan= judulPertanyaan;
        if(judulPertanyaan.length() > 30)
            judulYgDituliskan= judulPertanyaan.substring(0, 30) +"...";
        teksJudul.setText(judulYgDituliskan);
        teksMajority.setVisibility(View.GONE);
        teksDeskripsi.setText(deskripsiPertanyaan);
        loadPertanyaanLagi(teksJudul, teksMajority, teksDeskripsi, teksWaktu, waktu);
        Utilities.loadFotoLampiran(fotoLampiran, videoLampiran, lampiran, this, id_masalah);
        switch (this.statusPertanyaan){
            case Konstanta.PROBLEM_STATUS_UNVERIFIED:
                View v = getLayoutInflater().inflate(R.layout.pertanyaan_status_unverfied, null, false);
                Button verify = v.findViewById(R.id.tl_verify);
                Button reject = v.findViewById(R.id.tl_reject);
                verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        verifikasiPertanyaan(statusPertanyaan);
                    }
                });
                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tolakPertanyaan(statusPertanyaan);
                    }
                });
                statusPertanyaan.addView(v);
                break;
            case Konstanta.PROBLEM_STATUS_VERIFIED:
                View v2 = getLayoutInflater().inflate(R.layout.pertanyaan_status_verfied, null, false);
                statusPertanyaan.addView(v2);
                break;
            case Konstanta.PROBLEM_STATUS_REJECTED:
                View v3 = getLayoutInflater().inflate(R.layout.pertanyaan_status_rejected, null, false);
                statusPertanyaan.addView(v3);
                break;
        }
     //   Toast.makeText(this, waktu, Toast.LENGTH_SHORT).show();
    }

    private void loadSolusiLampiran(final LinearLayout lampiran, final String id_solusi, final Activity c) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.DAFTAR_FOTO_SOLUSI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<String> fotoLampiran = new ArrayList<>();
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            //  Toast.makeText(getActivity(), "Berhasil loading!", Toast.LENGTH_SHORT).show();
                            Solusi sol = new Solusi();
                            if(jsonArr.length()!=0){
                                for(int i = 0; i < jsonArr.length(); i++){
                                    org.json.JSONObject jsonObject = jsonArr.getJSONObject(i);
                                    fotoLampiran.add(jsonObject.getString("url_foto"));
                                }
                            }
                            initViewSolusiLampiran(fotoLampiran, lampiran, c);
                          //  loadVideoLampiran(fotoLampiran, videoLampiran, lampiran, act, id_masalah);
                        } catch (JSONException e) {
                            e.printStackTrace();
                          //  loadVideoLampiran(fotoLampiran, videoLampiran, lampiran, act, id_masalah);
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loadVideoLampiran(fotoLampiran, videoLampiran, lampiran, act, id_masalah);
                // Toast.makeText(act, Utilities.ubahBahasa("error cek solusi!", Utilities.getUserNegara(getApplicationContext()), getApplicationContext()), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> vote = new HashMap<>();
                vote.put("id_solusi", id_solusi);
                return vote;
            }
        };
        Volley.newRequestQueue(c).add(stringRequest);
    }


    private void loadPertanyaanLagi(final TextView teksJudul, final TextView teksMajority, final TextView teksDeskripsi, final TextView teksTanggal, String tanggal){
        String akanDiterjemahkan[] = {judulPertanyaan, deskripsiPertanyaan, tanggal};
        Terjemahan.terjemahkanAsync(akanDiterjemahkan, "en", Utilities.getUserBahasa(DetailPertanyaanActivityWkr.this),DetailPertanyaanActivityWkr.this, new PerubahanTerjemahListener() {
            @Override
            public void dataBerubah(String[] kata) {

                String judulYgDituliskan= kata[0];
                if(judulYgDituliskan.length() > 30)
                    judulYgDituliskan= judulYgDituliskan.substring(0, 30) +"...";
                teksJudul.setText(judulYgDituliskan);
                teksDeskripsi.setText(kata[1]);
                teksTanggal.setText(kata[2]);
                judulStr = kata[0];
                descStr = kata[1];
            }
        });
            new AsyncTask<Void, Void, String>(){

                @Override
                protected String doInBackground(Void... voids) {
                    String output = "";
                    output = Utilities.loadBidangKu(majorityPertanyaan, DetailPertanyaanActivityWkr.this);
                    return output;
                }

                @Override
                protected void onPostExecute(String s) {
                    String[] akanDiterjemahkan = {s};
                    Terjemahan.terjemahkanAsync(akanDiterjemahkan, "en", Utilities.getUserBahasa(DetailPertanyaanActivityWkr.this), DetailPertanyaanActivityWkr.this, new PerubahanTerjemahListener() {
                        @Override
                        public void dataBerubah(String[] kata) {
                            teksMajority.setVisibility(View.VISIBLE);
                            teksMajority.setText(kata[0]);
                            majorStr = kata[0];
                            if(id_masalah.equalsIgnoreCase(Utilities.getUserID(DetailPertanyaanActivityWkr.this)))
                            header.post(new Runnable() {
                                @Override
                                public void run() {
                                    menuBar.getLayoutParams().height = header.getHeight();
                                }
                            });
                        }
                    });
                }
            }.execute();
    }

    private void loadJudulBidang(final TextView teksJudul, final TextView teksMajority){
        teksJudul.setText(judulPertanyaan);
        String akanDiterjemahkan[] = {judulPertanyaan};
        Terjemahan.terjemahkanAsync(akanDiterjemahkan, "en", Utilities.getUserBahasa(DetailPertanyaanActivityWkr.this),DetailPertanyaanActivityWkr.this, new PerubahanTerjemahListener() {
            @Override
            public void dataBerubah(String[] kata) {

                String judulYgDituliskan= kata[0];
                if(judulYgDituliskan.length() > 30)
                    judulYgDituliskan= judulYgDituliskan.substring(0, 30) +"...";
                teksJudul.setText(judulYgDituliskan);
                judulStr = kata[0];
            }
        });
        new AsyncTask<Void, Void, String>(){

            @Override
            protected String doInBackground(Void... voids) {
                String output = "";
                output = Utilities.loadBidangKu(majorityPertanyaan, DetailPertanyaanActivityWkr.this);
                return output;
            }

            @Override
            protected void onPostExecute(String s) {
                String[] akanDiterjemahkan = {s};
                Terjemahan.terjemahkanAsync(akanDiterjemahkan, "en", Utilities.getUserBahasa(DetailPertanyaanActivityWkr.this), DetailPertanyaanActivityWkr.this, new PerubahanTerjemahListener() {
                    @Override
                    public void dataBerubah(String[] kata) {
                        teksMajority.setVisibility(View.VISIBLE);
                        teksMajority.setText(kata[0]);
                        majorStr = kata[0];
                        if(id_masalah.equalsIgnoreCase(Utilities.getUserID(DetailPertanyaanActivityWkr.this)))
                            header.post(new Runnable() {
                                @Override
                                public void run() {
                                    menuBar.getLayoutParams().height = header.getHeight();
                                }
                            });
                    }
                });
            }
        }.execute();
    }

    private void tampilanToastDiterjemahkan(String pesan){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String output = strings[0];
                if(getApplicationContext()!=null)
                    Utilities.ubahBahasa(output, Utilities.getUserNegara(getApplicationContext()), getApplicationContext());
                return output;
            }

            @Override
            protected void onPostExecute(String s) {
                if(DetailPertanyaanActivityWkr.this!=null)
                    Toast.makeText(DetailPertanyaanActivityWkr.this, s, Toast.LENGTH_SHORT).show();
            }
        }.execute(pesan);
    }

    private void tolakPertanyaan(LinearLayout statusPertanyaan) {
        statusPertanyaan.removeAllViews();
        View v3 = getLayoutInflater().inflate(R.layout.pertanyaan_status_rejected, null, false);
        statusPertanyaan.addView(v3);
    }

    private void verifikasiPertanyaan(LinearLayout statusPertanyaan) {
        statusPertanyaan.removeAllViews();
        View v2 = getLayoutInflater().inflate(R.layout.pertanyaan_status_verfied, null, false);
        statusPertanyaan.addView(v2);
    }


    //buat download full image dari server
   private class GetXMLTask extends AsyncTask<String, Void, ArrayList<Bitmap>> {
        @Override
        protected ArrayList<Bitmap> doInBackground(String... urls) {
            ArrayList<Bitmap> map = new ArrayList<>();
            for (String url : urls) {
                map.add(downloadImage(url));
            }
            return map;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //   pd = ProgressDialog.show(Info.this, "Please wait", "Downloading content", false, true);
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(ArrayList<Bitmap> result) {
            switch (result.size()){
                case 1:{
                    pertanyaanGambar1.setImageBitmap(result.get(0));
                    break;
                }
                case 2:{
                    pertanyaanGambar1.setImageBitmap(result.get(0));
                    pertanyaanGambar2.setImageBitmap(result.get(1));
                    break;
                }
                case 3:{
                    pertanyaanGambar1.setImageBitmap(result.get(0));
                    pertanyaanGambar2.setImageBitmap(result.get(1));
                    pertanyaanGambar3.setImageBitmap(result.get(2));
                    break;
                }
            }
          //  pd.dismiss();
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }

    private void cekSolusi(){
        solusi.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.SOLUTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            //  Toast.makeText(getActivity(), "Berhasil loading!", Toast.LENGTH_SHORT).show();
                            Solusi sol = new Solusi();
                            if(jsonArr.length()!=0){
                                org.json.JSONObject jsonObject = jsonArr.getJSONObject(0);
                                sol.setDeskripsi(jsonObject.getString("deskripsi"));
                                sol.setOrang(jsonObject.getString("orang"));
                                sol.setTimestamp(jsonObject.getString("timestamp"));
                                sol.setId_solusi(jsonObject.getString("id_solusi"));
                                sol.setStatus(jsonObject.getString("status_orang"));
                                sol.setFotoOrang(jsonObject.getString("foto_orang"));
                                sol.setNamaOrang(jsonObject.getString("nama_orang"));
                                sol.setStatusOrang("1");
                                solusi.add(sol);
                                isiViewSolusi();
                               // Toast.makeText(DetailPertanyaanActivityWkr.this, "solusi ok", Toast.LENGTH_SHORT).show();
                            }else{
                                sol.setStatusOrang("0");
                                sol.setOrang("0");
                                solusi.add(sol);
                            }
                            adpSolusi.notifyDataSetChanged();
                            cekKomentar();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("opo", error.toString());
                Toast.makeText(getApplicationContext(), Utilities.ubahBahasa("error cek solusi!", Utilities.getUserNegara(getApplicationContext()), getApplicationContext()), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> vote = new HashMap<>();
                vote.put("id_problem", id_masalah);
                return vote;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void cekKomentar() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.COMMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            for(int i=0; i<jsonArr.length(); i++){
                                try {
                                    org.json.JSONObject jsonObject = jsonArr.getJSONObject(i);
                                    Solusi sol = new Solusi();
                                    sol.setDeskripsi(jsonObject.getString("deskripsi"));
                                    sol.setOrang(jsonObject.getString("orang"));
                                    sol.setTimestamp(jsonObject.getString("timestamp"));
                                    sol.setId_solusi(jsonObject.getString("id_solusi"));
                                    sol.setStatus(jsonObject.getString("status_orang"));
                                    sol.setFotoOrang(jsonObject.getString("foto_orang"));
                                    sol.setNamaOrang(jsonObject.getString("nama_orang"));
                                    solusi.add(sol);
                                    isiViewKomentar(i+1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adpSolusi.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), Utilities.ubahBahasa("Failed to vote!", Utilities.getUserNegara(getApplicationContext()), getApplicationContext()), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> vote = new HashMap<>();
                vote.put("id_problem", id_masalah);
                return vote;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void initBarKomen(){
//        RelativeLayout.LayoutParams lpBar= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lpBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        lpBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        viewBarKomen= getLayoutInflater().inflate(R.layout.model_tab_edit_text, null, false);
//        viewBarKomen.setLayoutParams(lpBar);
//        viewBarKomen.setMinimumHeight(60);

        RelativeLayout.LayoutParams lpSilang= new RelativeLayout.LayoutParams(30, 30);
        lpSilang.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpSilang.setMargins(0, 20, 20, 0);

        int aksesoris[]= {R.drawable.icon_silang};
        RelativeLayout.LayoutParams lpAksesoris[]= {lpSilang};

        loader= initLoader(new String[0], aksesoris, lpAksesoris);

        urutanDipilih= new Array<>();
        posisiDipilih= new Array<>();

        final View klip= viewBarKomen.findViewById(R.id.tab_text_indikator);
        final View kirim= viewBarKomen.findViewById(R.id.tab_text_tindakan);
        final TextView teksKomentar= viewBarKomen.findViewById(R.id.tab_text_hint);
        teksKomentar.addTextChangedListener(new TextWatcher() {
            int jmlBaris= 1;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(teksKomentar.getLineCount() != jmlBaris && teksKomentar.getLineCount() <= 4){
                    jmlBaris= teksKomentar.getLineCount();
                    kirim.getLayoutParams().height= teksKomentar.getLineHeight() *(jmlBaris+1);
                    klip.getLayoutParams().height= teksKomentar.getLineHeight() *(jmlBaris+1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((ImageView) klip.findViewById(R.id.tab_text_indikator_gambar)).setImageResource(R.drawable.icon_klip);
        teksKomentar.setHint("Masukan komentar Anda");
        teksKomentar.setMaxLines(4);
        ((ImageView) kirim.findViewById(R.id.tab_text_tindakan_gambar)).setImageResource(R.drawable.icon_kirim);

        klip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utilities.isStoragePermissionGranted(DetailPertanyaanActivityWkr.this)){
                    Intent keAmbilGambar= new Intent(DetailPertanyaanActivityWkr.this, AmbilGambarAct.class);
                    keAmbilGambar.putExtra("urutanDipilih", urutanDipilih.PRIMITIF.arrayInt());
                    keAmbilGambar.putExtra("posisiDipilih", posisiDipilih.PRIMITIF.arrayInt());
                    startActivityForResult(keAmbilGambar, AMBIL_GAMBAR);
                }else {
                    Toast.makeText(DetailPertanyaanActivityWkr.this, "Failed to get storage permission!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String komentar= teksKomentar.getText().toString();
                String pathFoto[]= loader.ambilDaftarPathFoto();
                String id= Utilities.getUserID(getApplicationContext());
                String id_solusi = Utilities.getUid();
                kirimKomentar(id, id_solusi, komentar, pathFoto);
                teksKomentar.setText("");
            }
        });
    }

    private boolean sudahKomen(){
        if(solusi!=null){
            for(int i = 0; i<solusi.size(); i++){
                if(solusi.get(i).getOrang().equals(Utilities.getUserID(this)))
                    return true;
            }
        }
        return false;
    }

    private void kirimKomentar(final String idKomentator, final String id_solusi, final String komentar, String pathFoto[]){
        //lakukan sesuatu
        if(sudahKomen())
            Toast.makeText(this, "You already answered!", Toast.LENGTH_SHORT).show();
        else{
            final Solusi sol = new Solusi();
            sol.setId_solusi(id_solusi);
            sol.setDeskripsi(komentar);
            sol.setOrang(idKomentator);
            if(pathFoto.length>0){
                final ProgressDialog uploading = new ProgressDialog(this);
                String urlFotoTerupload[] = new String[1];
                uploadFotoKomentar(0, pathFoto, urlFotoTerupload, id_solusi,this, uploading, new FotoKomentarListener() {
                    @Override
                    public void tambahkanKomentar(String[] url) {
                        for(int i = 0; i < url.length; i ++)
                            Log.d("url "+String.valueOf(i), url[i]);
                        tambahkanFotoKomentar(DetailPertanyaanActivityWkr.this, id_solusi, url);
                        uploadKomentar(sol);
                        initFotoBatal();
                        uploading.dismiss();
                        setProblemStatus();
                    }
                });
            }else{
                uploadKomentar(sol);
                setProblemStatus();
            }

        }
    }
    private void setProblemStatus() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.SET_STATUS,
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
                masalah.put("id_problem", id_masalah);
                return masalah;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private interface FotoKomentarListener{
        void tambahkanKomentar(String[] url);
    }

    private void uploadKomentar(final Solusi solusi){
        final ProgressDialog uploading = new ProgressDialog(this);
        uploading.setMessage("Sending solution...");
        uploading.show();
        Utilities.getUserRef(solusi.getOrang().replace(".", ",")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Pengguna user = dataSnapshot.getValue(Pengguna.class);
                StringRequest stringRequestSolusi = new StringRequest(Request.Method.POST, Konstanta.TAMBAH_SOLUSI_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                uploading.dismiss();
                                Log.d("kirim komentar :", response);
                                cekSolusi();
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        uploading.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to add comment!", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> masalah = new HashMap<>();
                        masalah.put("id_problem", id_masalah);
                        masalah.put("id_solusi", solusi.getId_solusi());
                        masalah.put("deskripsi", solusi.getDeskripsi());
                        masalah.put("orang", solusi.getOrang());
                        masalah.put("nama_orang", user.getNama());
                        masalah.put("status_orang", String.valueOf(user.getStatus()));
                        masalah.put("foto_orang", Utilities.cekNull(user.getPhotoProfile()));
                        return masalah;
                    }
                };
                StringRequest stringRequestKomentar = new StringRequest(Request.Method.POST, Konstanta.TAMBAH_COMMENT_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                uploading.dismiss();
                                Log.i("kirim komentar :", response);
                                cekSolusi();
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        uploading.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to add comment!", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> masalah = new HashMap<>();
                        masalah.put("id_problem", id_masalah);
                        masalah.put("id_solusi", solusi.getId_solusi());
                        masalah.put("deskripsi", solusi.getDeskripsi());
                        masalah.put("orang", solusi.getOrang());
                        masalah.put("nama_orang", user.getNama());
                        masalah.put("status_orang", String.valueOf(user.getStatus()));
                        masalah.put("foto_orang", Utilities.cekNull(user.getPhotoProfile()));
                        return masalah;
                    }
                };
                if(!adaSolusi)
                    Volley.newRequestQueue(getApplicationContext()).add(stringRequestSolusi);
                else
                    Volley.newRequestQueue(getApplicationContext()).add(stringRequestKomentar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to add comment!", Toast.LENGTH_SHORT).show();
            }
        });
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

    private GaleriLoader initLoader(String pathFile[], int aksesoris[], RelativeLayout.LayoutParams lpAksesoris[]){
        final GaleriLoader loader= new GaleriLoader(this, pathFile, 18, GaleriLoader.JENIS_FOTO,
                R.layout.model_tambah_properti_cell, R.id.tambah_cell_pratinjau);
        loader.aturBentukFoto(GaleriLoader.BENTUK_KOTAK);
        loader.aturAksesoris(aksesoris, lpAksesoris);
        loader.aturOffsetItem(20);
        loader.aturJmlItemPerGaris(3);
        loader.aturUkuranPratinjau(loader.UKURAN_MENYESUAIKAN_LBR_INDUK);
        loader.aturSumberBg(R.drawable.obj_gambar_kotak);
        loader.aturSumberBgTakBisa(R.drawable.obj_tanda_seru_lingkaran_garis);

        loader.aturModeBg(false);
/*
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
/*
                final TextView urutanDipilih = view.findViewById(R.id.tambah_cell_centang_no);
                final GridView liveQuestion= parentUtama.findViewById(R.id.tambah_properti_cell_dipilih);
                urutanDipilih.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!urutanDipilih.isSelected()) {
                            loader.pilihFoto(posisi);
                            urutanDipilih.setSelected(true);
                            TambahPertanyaanWkr.AdapterPropertiDipilih adpDipilih= new TambahPertanyaanWkr.AdapterPropertiDipilih(lebar, loader.ambilBitmapDipilih());
                            liveQuestion.setAdapter(adpDipilih);
                            if(loader.ambilBitmapDipilih().size() <= 3)
                                aturTinggiGrid(liveQuestion, lebar);
                            else
                                aturTinggiGrid(liveQuestion, lebar+90);
                        } else {
                            loader.batalPilihFoto(posisi);
                            urutanDipilih.setSelected(false);
                            TambahPertanyaanWkr.AdapterPropertiDipilih adpDipilih= new TambahPertanyaanWkr.AdapterPropertiDipilih(lebar, loader.ambilBitmapDipilih());
                            liveQuestion.setAdapter(adpDipilih);
                            if(loader.ambilBitmapDipilih().size() == 0)
                                aturTinggiGrid(liveQuestion, 0);
                            else if(loader.ambilBitmapDipilih().size() <= 3)
                                aturTinggiGrid(liveQuestion, lebar);
                            else
                                aturTinggiGrid(liveQuestion, lebar+90);
                        }
                    }
                });
* /
            }

            @Override
            public void bufferUtama(int posisi, int jmlBuffer) {

            }
        });
*/
        return loader;
    }

    class AdapterPropertiCell extends BaseAdapter {

        GaleriLoader loader;
        int lebar;
        int jml;

        AdapterPropertiCell(GaleriLoader loader, int lebar, int jml){
            this.loader= loader;
            loader.aturLpWadahImg(new ViewGroup.LayoutParams(lebar, lebar));
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

//            view= loader.buatFoto(view, position);

            ImageView indUrutan= view.findViewById(R.id.tambah_cell_centang);
            indUrutan.setVisibility(View.GONE);

            ImageView bg= view.findViewById(R.id.tambah_cell_pratinjau);
            bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent kePreview= new Intent(getBaseContext(), GaleriPreview.class);
                    kePreview.putStringArrayListExtra("judul", loader.ambilDaftarJudul());
                    kePreview.putExtra("path", loader.ambilDaftarPathFoto());
                    kePreview.putExtra("posisiFoto", position);
                    kePreview.putExtra("indikatorDitampilkan", false);

                    startActivity(kePreview);
                }
            });

            ImageView batalPilih= view.findViewById(loader.idAksesoris(0));
            batalPilih.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GridView gambarDipilih= viewBarKomen.findViewById(R.id.wadah_gambar);
                    String pathFoto[]= loader.ambilDaftarPathFoto();
                    String pathFotoBaru[]= new String[pathFoto.length-1]; //data.getStringArrayExtra("pathFoto");
                    int jalan= 0;
                    for(int i= 0; i< pathFoto.length; i++)
                        if(i != position)
                            pathFotoBaru[jalan++]= pathFoto[i];

//                    int urutanHilang= urutanDipilih.ambil(position);
                    int indek= urutanDipilih.hapus(new Integer(position+1));
                    posisiDipilih.hapus(indek);
                    Toast.makeText(DetailPertanyaanActivityWkr.this, "indekHilang= " +position, Toast.LENGTH_SHORT).show();
                    perbaruiUrutanDipilih(position+1);
                    gambarDipilih.setAdapter(new AdapterPropertiCell(loader, gambarDipilih.getWidth() / loader.ambilJmlItemPerGaris(), pathFotoBaru.length));


                    float batasTinggi= lbrBarKomen +400;
                    if(pathFotoBaru.length <= 3) {
                        batasTinggi = lbrBarKomen + (loader.ambilUkuranPratinjau() + loader.ambilOffsetItem())
                                + gambarDipilih.getPaddingBottom() + gambarDipilih.getPaddingTop();
                        RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams) gambarDipilih.getLayoutParams();
                        lp.height= ViewGroup.LayoutParams.WRAP_CONTENT;
                    } else{
                        RelativeLayout.LayoutParams lpDipilih= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (batasTinggi- lbrBarKomen));
                        gambarDipilih.setLayoutParams(lpDipilih);
                    }
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) batasTinggi);
                    viewBarKomen.setLayoutParams(lp);

//                    loader.batalPilihFoto(posisiDipilih[position]);
//                    Toast.makeText(DetailPertanyaanActivityWkr.this, "posisi batal= " +posisiDipilih[position], Toast.LENGTH_LONG).show();
                }
            });

            return view;
        }
    }

    private void perbaruiUrutanDipilih(int urutanHilang){
        for(int i= 0; i< urutanDipilih.ukuran(); i++)
            if(urutanDipilih.ambil(i) > urutanHilang)
                urutanDipilih.ganti(i, new Integer(urutanDipilih.ambil(i)-1));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AMBIL_GAMBAR && resultCode == RESULT_OK){
            if(lbrBarKomen == -3)
                lbrBarKomen= viewBarKomen.getHeight();

            String pathFoto[]= data.getStringArrayExtra("pathFoto");
            urutanDipilih.dariArrayPrimitif(data.getIntArrayExtra("urutanDipilih"));
            posisiDipilih.dariArrayPrimitif(data.getIntArrayExtra("posisiDipilih"));

           gambarDipilih= viewBarKomen.findViewById(R.id.wadah_gambar);
            float batasTinggi= lbrBarKomen +400;
            loader.aturPathItem(pathFoto);
/*
            int posisiDipilihDalam[]= new int[urutanDipilih.length];
            for(int i= 0; i< urutanDipilih.length; i++)
                posisiDipilih[i]= i;

            loader.aturIndDipilih(posisiDipilihDalam, urutanDipilih);
*/
            gambarDipilih.setPadding(0, 50, 0, 20);

            if(pathFoto.length <= 3) {
                batasTinggi = lbrBarKomen + (loader.ambilUkuranPratinjau() + loader.ambilOffsetItem())
                        + gambarDipilih.getPaddingBottom() + gambarDipilih.getPaddingTop();
                RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams) gambarDipilih.getLayoutParams();
                lp.height= ViewGroup.LayoutParams.WRAP_CONTENT;
            } else{
                RelativeLayout.LayoutParams lpDipilih= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (batasTinggi- lbrBarKomen));
                gambarDipilih.setLayoutParams(lpDipilih);
            }

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) batasTinggi);
//            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            viewBarKomen.setLayoutParams(lp);
            gambarDipilih.setAdapter(new AdapterPropertiCell(loader, gambarDipilih.getWidth() / loader.ambilJmlItemPerGaris(), pathFoto.length));

            ((EditText) viewBarKomen.findViewById(R.id.tab_text_hint)).setMaxLines(1);
            tmbBatal= viewBarKomen.findViewById(R.id.batal);
            tmbBatal.setVisibility(View.VISIBLE);
            tmbBatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initFotoBatal();
                }
            });
        }
    }

    private  void initFotoBatal(){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, lbrBarKomen);
        RelativeLayout.LayoutParams lpDipilih= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        viewBarKomen.setLayoutParams(lp);
        gambarDipilih.setAdapter(null);
        gambarDipilih.setLayoutParams(lpDipilih);
        gambarDipilih.setPadding(0,0,0,0);
        ((EditText) viewBarKomen.findViewById(R.id.tab_text_hint)).setMaxLines(4);
        tmbBatal.setVisibility(View.GONE);

        urutanDipilih.hapusSemua();
        posisiDipilih.hapusSemua();
    }

    private void initOrang(final TextView nama, final TextView status, final CircleImageView fotoProfil){
        DatabaseReference ref = Utilities.getUserRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(emailOrang.replace(".", ","))){
                    Utilities.getUserRef(emailOrang).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Pengguna user = dataSnapshot.getValue(Pengguna.class);
                            namaPemilik= user.getNama();
                            majorPemilik = String.valueOf(user.getStatus());
                            urlFotoOrang = Utilities.cekNull(user.getPhotoProfile()); urlFotoOrangLoaded = true;
                            nama.setText(namaPemilik);
                            if(user.getPhotoProfile()!=null)
                                Glide.with(DetailPertanyaanActivityWkr.this).load(user.getPhotoProfile()).into(fotoProfil);
                            status.setText(PackBahasa.statusOrang[Terjemahan.indexBahasa(DetailPertanyaanActivityWkr.this)][Pengguna.Status.statusStr(user.getStatus())]);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String[] ambilDaftarVoter(String idPostingan){
        //ambil daftar voter tiap postingan (pertanyaan, solusi, atau komentar).
        return null;
    }

    private void munculkanDetailProfil(final int indUser){
            if(profilDitampilkan){
                viewLatar.removeView(detailProfil);
                profilDitampilkan= false;
            } else {
                final String email = (indUser==-1)? emailPengguna/*emailOrang*/ : (solusi.get(indUser).getOrang());
                Toast.makeText(this, "EMAIL ORANG: " +emailOrang, Toast.LENGTH_LONG).show();
                if(email.equalsIgnoreCase(emailPengguna)) {
                    Toast.makeText(this, "Orang itu adalah diri Anda sendiri", Toast.LENGTH_LONG).show();
                    return;
                }
                // if(detailProfil == null){
                detailProfil = getLayoutInflater().inflate(R.layout.model_detail_user, null, false);

                View latarBel = detailProfil.findViewById(R.id.user_latar);
                latarBel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewLatar.removeView(detailProfil);
                        profilDitampilkan = false;
                    }
                });
                ImageView aksiChat = detailProfil.findViewById(R.id.user_aksi_chat);
//            Toast.makeText(this, "ind "+String.valueOf(indUser), Toast.LENGTH_SHORT).show();

                aksiChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utilities.getUserRef().child(email.replace(".", ",")).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Pengguna orang = dataSnapshot.getValue(Pengguna.class);
                                Intent i = new Intent(DetailPertanyaanActivityWkr.this, ChatActivity.class);
                                //i.putExtra("idPesan", listPesan.getIdPesan());
                                i.putExtra("pengguna", orang);
                                //Toast.makeText(getActivity(), orang.getNama(), Toast.LENGTH_SHORT).show();
                                startActivity(i);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                ImageView aksiProfil = detailProfil.findViewById(R.id.user_aksi_lihat);
                aksiProfil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //inten ke profil orang itu
                        DatabaseReference ref = Utilities.getUserRef();
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(email.replace(".", ","))){
                                    Utilities.getUserRef().child(email.replace(".", ",")).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Pengguna orang = dataSnapshot.getValue(Pengguna.class);
                                            Intent i = new Intent(DetailPertanyaanActivityWkr.this, ProfileUserLainAct.class);
                                            //i.putExtra("idPesan", listPesan.getIdPesan());
                                            i.putExtra("pengguna", orang);
                                            //Toast.makeText(getActivity(), orang.getNama(), Toast.LENGTH_SHORT).show();
                                            startActivity(i);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(DetailPertanyaanActivityWkr.this, "User not found!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
                CircleImageView fotoOrang = detailProfil.findViewById(R.id.user_profil);
                // }
//                if(!profilDitampilkan) {
                viewLatar.addView(detailProfil);
                CircleImageView viewFoto= detailProfil.findViewById(R.id.user_profil);
                TextView viewNama= detailProfil.findViewById(R.id.user_nama);
                TextView viewStatus= detailProfil.findViewById(R.id.user_status);

                if(indUser >= 0) {
                    viewNama.setText(solusi.get(indUser).getNamaOrang());
                    viewStatus.setText(strStatus(solusi.get(indUser).getStatus()));
                    if(solusi.get(indUser).getFotoOrang()!=null && !solusi.get(indUser).getFotoOrang().equals("null")){
                        viewFoto.setPadding(0,0,0,0);
                        Utilities.updateFotoProfile(solusi.get(indUser).getFotoOrang(), viewFoto);
                    }
//                Toast.makeText(getBaseContext(), orang[indUser], Toast.LENGTH_LONG).show();
                } else if(indUser == -1){
                    if (urlFotoOrangLoaded) {
                        viewNama.setText(namaPemilik);
                        viewStatus.setText((majorPemilik != null)?strStatus(majorPemilik):"");
                        if (!urlFotoOrang.equals("null")) {
                            viewFoto.setPadding(0, 0, 0, 0);
                            Utilities.updateFotoProfile(urlFotoOrang, viewFoto);
                        }
                    }
                    Toast.makeText(getBaseContext(), namaPemilik, Toast.LENGTH_LONG).show();
                }
                profilDitampilkan= true;
//                }
            }
    }

    public String strStatus(String status){
        switch (Integer.parseInt(status)){
            case 200 :
                return PackBahasa.bahasaStatusAkun[Terjemahan.indexBahasa(this)][0];
            case 201 :
                return PackBahasa.bahasaStatusAkun[Terjemahan.indexBahasa(this)][1];
            case 202 :
                return PackBahasa.bahasaStatusAkun[Terjemahan.indexBahasa(this)][2];
        }
        return "";
    }


    void aturTinggiGrid(GridView grid, int tinggi){
        grid.getLayoutParams().height= tinggi;
    }


    class AdapterGambar extends BaseAdapter{
        private int lebar;

        AdapterGambar(int lebar){
            this.lebar= lebar;
        }

        @Override
        public int getCount() {
            return gambar.size();
        }

        @Override
        public Object getItem(int position) {
            return gambar.get(position);
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
            //bg.setImageBitmap(gambar.get(position));

            ImageView centang = view.findViewById(R.id.tambah_cell_centang);

            ViewGroup vg= (ViewGroup) view;
            vg.removeView(centang);

            return view;
        }
    }

    @Override
    public void onBackPressed() {
        if(!profilDitampilkan)
            super.onBackPressed();
        else
            munculkanDetailProfil(-1);
    }

    class DetailPertanyaanAdapter extends BaseAdapter{
        DetailPertanyaanAdapter (){
        }

        @Override
        public int getCount() {
            if(viewPertanyaan != null)
                return solusi.size() +1;
            else
                return solusi.size();
        }

        @Override
        public Object getItem(int position) {
            return orang[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if(viewPertanyaan != null){
                if(position > 1){
                    //
                    // if(indViewKomentar[position % indViewKomentar.length] != position -1)
                    //  isiViewKomentar(position-1);
                    //view= viewKomentar[position-1];
                    view = viewKomentar.get(position-2);
                }
                else if(position == 0)
                    view= viewPertanyaan;
                else{
                    if(solusi.get(position-1).getStatusOrang().equals("1")){
                        adaSolusi = true;
                        view = viewSolusi;
                    }else{
                        adaSolusi = false;
                        view = getLayoutInflater().inflate(R.layout.model_timeline_dumb_solusi, null, false);
                    }
                }
            } else {
                if(position > 0)
                    view = viewKomentar.get(position-1);
                else{
                    if(solusi.get(position).getStatusOrang().equals("1")){
                        adaSolusi = true;
                        view = viewSolusi;
                    }else{
                        adaSolusi = false;
                        view = getLayoutInflater().inflate(R.layout.model_timeline_dumb_solusi, null, false);
                    }
                }
            }
            return view;
        }
    }

    private class GridAdp extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
