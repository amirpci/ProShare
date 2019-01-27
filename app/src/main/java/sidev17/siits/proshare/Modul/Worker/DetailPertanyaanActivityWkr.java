package sidev17.siits.proshare.Modul.Worker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rmtheis.yandtran.language.Language;

import org.json.JSONArray;
import org.json.JSONException;

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
import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Model.Pengguna;
import sidev17.siits.proshare.Model.Problem.Solusi;
import sidev17.siits.proshare.Modul.AmbilGambarAct;
import sidev17.siits.proshare.Modul.SettingAct;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Array;
import sidev17.siits.proshare.Utils.View.ImgViewTouch;
import sidev17.siits.proshare.Utils.View.MenuBarView;
import sidev17.siits.proshare.Utils.ViewTool.Aktifitas;
import sidev17.siits.proshare.Utils.ViewTool.GaleriLoader;
import sidev17.siits.proshare.Utils.Utilities;
import sidev17.siits.proshare.Utils.Warna;

public class DetailPertanyaanActivityWkr extends Aktifitas {
    public final int PENGGUNA_EXPERT_TERVERIFIKASI= 2;
    public final int PENGGUNA_EXPERT= 1;
    public final int PENGGUNA_BIASA= 0;

    private static final int AMBIL_GAMBAR= 11;

    private String namaPemilik;
    private String majorPemilik;
    private int statusPemilik;

    private String judulPertanyaan;
    private String majorityPertanyaan;
    private String deskripsiPertanyaan;
    private String emailOrang;
    private String urlFotoOrang;
    private String waktu;
    private String id_masalah;
    private ArrayList<String> gambar;
    private ArrayList<String> fotoLampiran;
    private ArrayList<String> videoLampiran;
    private boolean adaSolusi;
    private boolean urlFotoOrangLoaded = false;

    private LinearLayout lampiran;
    private ImageView pertanyaanGambar1, pertanyaanGambar2, pertanyaanGambar3;

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
    private ArrayList<View> viewKomentar;

    private View viewBarKomen;
    private GaleriLoader loader;
    private Array<Integer> urutanDipilih; //urutan item yg dipilih
    private Array<Integer> posisiDipilih; //posisi item yg dipilih
    private int lbrBarKomen= -3;

    private MenuBarView menuBar;

    private final int BATAS_BUFFER_VIEW= 7;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pertanyaan);

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
        isiViewPertanyaan();
        initBarKomen();
        initMenuBar();
        ((ViewGroup) findViewById(R.id.detail_bar_komen)).addView(viewBarKomen);
    }

    private void initMenuBar(){
        menuBar= viewPertanyaan.findViewById(R.id.tl_opsi);
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
                Intent keEdit= new Intent(DetailPertanyaanActivityWkr.this, TambahPertanyaanWkr.class);

                keEdit.putExtra("judul", judulPertanyaan);
                keEdit.putExtra("deskripsi", deskripsiPertanyaan);
                TextView majority= viewPertanyaan.findViewById(R.id.tl_majority);
                keEdit.putExtra("bidang", majority.getText().toString());
//                keEdit.putExtra("gambarAwal", gambarAwal);

                startActivity(keEdit);
            }
        });
        menuBar.aturAksiKlikItem(1, new ImgViewTouch.PenungguKlik() {
            @Override
            public void klik(View v) {
                hapusPertanyaan();
            }
        });

        daftarkanBatas(menuBar.ambilBatas());
    }

    private void hapusPertanyaan(){
        //lakukan sesuatu...
        //kalau bisa pake konfirmasi dulu
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
    private void isiViewKomentar(final int ind){
        View view= getLayoutInflater().inflate(R.layout.model_kolom_komentar, null, false);

        TextView teksOrang= view.findViewById(R.id.komentar_orang_nama);
        TextView teksJob= view.findViewById(R.id.komentar_orang_job);
        TextView teksSolusi= view.findViewById(R.id.komentar_deskripsi);
        TextView teksTanggal = view.findViewById(R.id.komentar_waktu);
        ImageView centang= view.findViewById(R.id.komentar_orang_centang);
        CircleImageView fotoProfil= view.findViewById(R.id.komentar_orang_gambar);

        final TextView jmlSuka= view.findViewById(R.id.komentar_vote_posisi);
        //Belum selesai
//        jmlSuka.setText(Integer.toString(ambilDaftarVoter("id").length));

        View profil= view.findViewById(R.id.komentar_orang_dp);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perbesar FotoProfl
                munculkanAlertDialog(R.layout.model_detail_user, ind);
            }
        });
        final ImageView tmbVoteUp= view.findViewById(R.id.komentar_vote_up);
        final ImageView tmbVoteDown= view.findViewById(R.id.komentar_vote_down);
        final TextView voteJumlah = view.findViewById(R.id.komentar_vote_posisi);
        Utilities.loadVoteCountSolusi(tmbVoteUp, tmbVoteDown, voteJumlah, this, solusi.get(ind).getId_solusi());
        tmbVoteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.voteSolusi("1", tmbVoteUp, tmbVoteDown, voteJumlah, DetailPertanyaanActivityWkr.this, solusi.get(ind).getId_solusi());
            }
        });
        tmbVoteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.voteSolusi("0", tmbVoteUp, tmbVoteDown, voteJumlah, DetailPertanyaanActivityWkr.this, solusi.get(ind).getId_solusi());
            }
        });
        teksOrang.setText(Utilities.ubahBahasa(solusi.get(ind).getOrang(), Utilities.getUserNegara(this), this));
        //teksJob.setText("wkwkwk");
        teksSolusi.setText(Utilities.ubahBahasa(solusi.get(ind).getDeskripsi(), Utilities.getUserNegara(this), this));
        if(!solusi.get(ind).getFotoOrang().equals("null"))
            Glide.with(this).load(solusi.get(ind).getFotoOrang()).into(fotoProfil);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("MMMM dd, yyyy");
        Date date = null;
        String waktu;
        try {
            date = format1.parse(solusi.get(ind).getTimestamp());
            waktu = format2.format(date);
            teksTanggal.setText(Utilities.ubahBahasa(waktu, Utilities.getUserNegara(this), this));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (solusi.get(ind).getStatus()){
            case "200" :
                solusi.get(ind).setStatus(Utilities.ubahBahasa("Worker", Utilities.getUserNegara(getApplicationContext()), getApplicationContext()));
                teksJob.setText(solusi.get(ind).getStatus());
                break;
            case "201" :
                solusi.get(ind).setStatus(Utilities.ubahBahasa("Expert", Utilities.getUserNegara(getApplicationContext()), getApplicationContext()));
                teksJob.setText(solusi.get(ind).getStatus());
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
                break;
            case "202" :
                solusi.get(ind).setStatus(Utilities.ubahBahasa("Verified Expert", Utilities.getUserNegara(getApplicationContext()), getApplicationContext()));
                teksJob.setText(solusi.get(ind).getStatus());
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);
                break;
        }

       viewKomentar.add(view);
       // indViewKomentar[indView]= ind;
    }
    private void isiViewSolusi(){
        Log.d("solusi : ", "init view solusi");
        viewSolusi= getLayoutInflater().inflate(R.layout.model_solusi_expert, null, false);

        TextView teksOrang= viewSolusi.findViewById(R.id.komentar_orang_nama);
        TextView teksJob= viewSolusi.findViewById(R.id.komentar_orang_job);
        TextView teksSolusi= viewSolusi.findViewById(R.id.komentar_deskripsi);
        ImageView centang= viewSolusi.findViewById(R.id.komentar_orang_centang);
        CircleImageView fotoProfil= viewSolusi.findViewById(R.id.komentar_orang_gambar);
        TextView teksTanggal = viewSolusi.findViewById(R.id.komentar_waktu);

        final TextView jmlSuka= viewSolusi.findViewById(R.id.komentar_vote_posisi);
        View profil= viewSolusi.findViewById(R.id.komentar_orang_dp);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perbesar FotoProfl
                munculkanAlertDialog(R.layout.model_detail_user, 0);
            }
        });
        final ImageView tmbVoteDown= viewSolusi.findViewById(R.id.komentar_vote_down);
        final ImageView tmbVoteUp= viewSolusi.findViewById(R.id.komentar_vote_up);
        final TextView voteJumlah = viewSolusi.findViewById(R.id.komentar_vote_posisi);
        Utilities.loadVoteCountSolusi(tmbVoteUp, tmbVoteDown, voteJumlah, this, solusi.get(0).getId_solusi());
        tmbVoteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.voteSolusi("1", tmbVoteUp, tmbVoteDown, voteJumlah, DetailPertanyaanActivityWkr.this, solusi.get(0).getId_solusi());
            }
        });
        tmbVoteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.voteSolusi("0", tmbVoteUp, tmbVoteDown, voteJumlah, DetailPertanyaanActivityWkr.this, solusi.get(0).getId_solusi());
            }
        });

        teksOrang.setText(solusi.get(0).getNamaOrang());
       // teksJob.setText("wkwkwkwkwkwk");
        teksSolusi.setText(Utilities.ubahBahasa(solusi.get(0).getDeskripsi(), Utilities.getUserNegara(this), this));
        if(!solusi.get(0).getFotoOrang().equals("null"))
            Glide.with(this).load(solusi.get(0).getFotoOrang()).into(fotoProfil);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("MMMM dd, yyyy");
        Date date = null;
        String waktu;
        try {
            date = format1.parse(solusi.get(0).getTimestamp());
            waktu = format2.format(date);
            teksTanggal.setText(Utilities.ubahBahasa(waktu, Utilities.getUserNegara(this), this));
        } catch (ParseException e) {
            e.printStackTrace();
        }
       // Log.d("solusi status", solusi.get(0).getStatus());
        switch (solusi.get(0).getStatus()){
            case "200" :
                solusi.get(0).setStatus(Utilities.ubahBahasa("Worker", Utilities.getUserNegara(getApplicationContext()), getApplicationContext()));
                teksJob.setText(solusi.get(0).getStatus());
                break;
            case "201" :
                solusi.get(0).setStatus(Utilities.ubahBahasa("Expert", Utilities.getUserNegara(getApplicationContext()), getApplicationContext()));
                teksJob.setText(solusi.get(0).getStatus());
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
                break;
            case "202" :
                solusi.get(0).setStatus(Utilities.ubahBahasa("Verified Expert", Utilities.getUserNegara(getApplicationContext()), getApplicationContext()));
                teksJob.setText(solusi.get(0).getStatus());
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);
                break;
        }
    }
    private void isiViewPertanyaan(){
        viewPertanyaan= getLayoutInflater().inflate(R.layout.model_timeline_pertanyaan, null, false);
        TextView teksJudul= viewPertanyaan.findViewById(R.id.tl_judul);
        TextView teksMajority= viewPertanyaan.findViewById(R.id.tl_majority);
        TextView teksDeskripsi= viewPertanyaan.findViewById(R.id.tl_deskripsi);
        TextView teksNama = viewPertanyaan.findViewById(R.id.tl_nama_orang);
        TextView teksStatusOrang = viewPertanyaan.findViewById(R.id.tl_status_orang);
        TextView teksWaktu = viewPertanyaan.findViewById(R.id.tl_waktu);
        CircleImageView fotoProfil = viewPertanyaan.findViewById(R.id.tl_orang_gambar);
        lampiran = viewPertanyaan.findViewById(R.id.lampiran_pertanyaan);
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
                munculkanAlertDialog(R.layout.model_detail_user, -1);
            }
        });
        final ImageView tmbVoteDown= viewPertanyaan.findViewById(R.id.tl_vote_down);
        final ImageView tmbVoteUp= viewPertanyaan.findViewById(R.id.tl_vote_up);
        Utilities.loadVoteCountProblem(tmbVoteUp, tmbVoteDown, voteJumlah, this, id_masalah);
        tmbVoteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.voteProblem("1", tmbVoteUp, tmbVoteDown, voteJumlah, DetailPertanyaanActivityWkr.this, id_masalah);
            }
        });
        tmbVoteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.voteProblem("0", tmbVoteUp, tmbVoteDown, voteJumlah, DetailPertanyaanActivityWkr.this, id_masalah);
            }
        });

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("MMMM dd, yyyy");
        Date date = null;
        try {
            date = format1.parse(waktu);
            waktu = format2.format(date);
            teksWaktu.setText(Utilities.ubahBahasa(waktu, Utilities.getUserNegara(this), this));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println();

        teksJudul.setText(Utilities.ubahBahasa(judulPertanyaan, Utilities.getUserNegara(this), this));
        Utilities.loadBidang(majorityPertanyaan, Utilities.getUserNegara(this), teksMajority, this);
        teksDeskripsi.setText(Utilities.ubahBahasa(deskripsiPertanyaan, Utilities.getUserNegara(this), this));
        Utilities.loadFotoLampiran(fotoLampiran, videoLampiran, lampiran, this, id_masalah);
     //   Toast.makeText(this, waktu, Toast.LENGTH_SHORT).show();
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

        View klip= viewBarKomen.findViewById(R.id.tab_text_indikator);
        final TextView teksKomentar= viewBarKomen.findViewById(R.id.tab_text_hint);
        View kirim= viewBarKomen.findViewById(R.id.tab_text_tindakan);

        ((ImageView) klip.findViewById(R.id.tab_text_indikator_gambar)).setImageResource(R.drawable.icon_klip);
        teksKomentar.setHint("Masukan komentar Anda");
        teksKomentar.setMaxLines(4);
        ((ImageView) kirim.findViewById(R.id.tab_text_tindakan_gambar)).setImageResource(R.drawable.icon_kirim);

        klip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keAmbilGambar= new Intent(DetailPertanyaanActivityWkr.this, AmbilGambarAct.class);
                keAmbilGambar.putExtra("urutanDipilih", urutanDipilih.PRIMITIF.arrayInt());
                keAmbilGambar.putExtra("posisiDipilih", posisiDipilih.PRIMITIF.arrayInt());
                startActivityForResult(keAmbilGambar, AMBIL_GAMBAR);
            }
        });
        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String komentar= teksKomentar.getText().toString();
                String pathFoto[]= loader.ambilDaftarPathFoto();
                String id= Utilities.getUserID(getApplicationContext());

                kirimKomentar(id, komentar, pathFoto);
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

    private void kirimKomentar(final String idKomentator, final String komentar, String pathFoto[]){
        //lakukan sesuatu
        if(sudahKomen())
            Toast.makeText(this, "You already answered!", Toast.LENGTH_SHORT).show();
        else{
            final ProgressDialog uploading = new ProgressDialog(this);
            uploading.setMessage("Sending solution...");
            uploading.show();
            final String id_solusi = Utilities.getUid();
            Toast.makeText(this, id_solusi, Toast.LENGTH_SHORT).show();
            Utilities.getUserRef(idKomentator.replace(".", ",")).addValueEventListener(new ValueEventListener() {
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
                            masalah.put("id_solusi", id_solusi);
                            masalah.put("deskripsi", komentar);
                            masalah.put("orang", idKomentator);
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
                            masalah.put("id_solusi", id_solusi);
                            masalah.put("deskripsi", komentar);
                            masalah.put("orang", idKomentator);
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

            final GridView gambarDipilih= viewBarKomen.findViewById(R.id.wadah_gambar);
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
            final ImageView tmbBatal= viewBarKomen.findViewById(R.id.batal);
            tmbBatal.setVisibility(View.VISIBLE);
            tmbBatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            });
        }
    }

    private void initOrang(final TextView nama, final TextView status, final CircleImageView fotoProfil){
        Utilities.getUserRef(emailOrang).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pengguna user = dataSnapshot.getValue(Pengguna.class);
                namaPemilik= user.getNama();
                urlFotoOrang = Utilities.cekNull(user.getPhotoProfile()); urlFotoOrangLoaded = true;
                nama.setText(namaPemilik);
                Language languageID=null;
                switch (Integer.parseInt(user.getNegara())){
                    case 2 : languageID=Language.INDONESIAN; break;
                    case 3 : languageID=Language.ENGLISH; break;
                    case 4 : languageID=Language.ENGLISH; break;
                    case 5 : languageID=Language.JAPANESE; break;
                }
                com.rmtheis.yandtran.translate.Translate.setKey(getString(R.string.yandex_api_key));
                try {
                    switch ((int)user.getStatus()){
                        case 200 :
                            majorPemilik = Utilities.ubahBahasa("Worker", Utilities.getUserNegara(getApplicationContext()), getApplicationContext());
                            status.setText(majorPemilik);
                            statusPemilik= PENGGUNA_BIASA;
                            break;
                        case 201 :
                            majorPemilik = Utilities.ubahBahasa("Expert", Utilities.getUserNegara(getApplicationContext()), getApplicationContext());
                            status.setText(majorPemilik);
                            statusPemilik= PENGGUNA_EXPERT;
                            break;
                        case 202 :
                            majorPemilik = Utilities.ubahBahasa("Verified Expert", Utilities.getUserNegara(getApplicationContext()), getApplicationContext());
                            status.setText(majorPemilik);
                            statusPemilik= PENGGUNA_EXPERT_TERVERIFIKASI;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(user.getPhotoProfile()!=null)
                    Glide.with(DetailPertanyaanActivityWkr.this).load(user.getPhotoProfile()).into(fotoProfil);
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

    private void munculkanAlertDialog(int idHalaman, int indUser){
        if(detailProfil == null){
            detailProfil = getLayoutInflater().inflate(idHalaman, null, false);

            View latarBel = detailProfil.findViewById(R.id.user_latar);
            latarBel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewLatar.removeView(detailProfil);
                    profilDitampilkan = false;
                }
            });
            ImageView aksiChat = detailProfil.findViewById(R.id.user_aksi_chat);
            aksiChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //inten ke chat orang itu
                }
            });
            ImageView aksiProfil = detailProfil.findViewById(R.id.user_aksi_lihat);
            aksiProfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //inten ke profil orang itu
                }
            });
            CircleImageView fotoOrang = detailProfil.findViewById(R.id.user_profil);
        }
        if(!profilDitampilkan) {
            viewLatar.addView(detailProfil);
            CircleImageView viewFoto= detailProfil.findViewById(R.id.user_profil);
            TextView viewNama= detailProfil.findViewById(R.id.user_nama);
            TextView viewStatus= detailProfil.findViewById(R.id.user_status);

            if(indUser >= 0) {
                viewNama.setText(solusi.get(indUser).getNamaOrang());
                viewStatus.setText(solusi.get(indUser).getStatus());
                if(solusi.get(indUser).getFotoOrang()!=null && !solusi.get(indUser).getFotoOrang().equals("null")){
                    viewFoto.setPadding(0,0,0,0);
                    Utilities.updateFotoProfile(solusi.get(indUser).getFotoOrang(), viewFoto);
                }
                Toast.makeText(getBaseContext(), orang[indUser], Toast.LENGTH_LONG).show();
            } else if(indUser == -1){
                if (urlFotoOrangLoaded) {
                    viewNama.setText(namaPemilik);
                    viewStatus.setText(majorPemilik);
                    if (!urlFotoOrang.equals("null")) {
                        viewFoto.setPadding(0, 0, 0, 0);
                        Utilities.updateFotoProfile(urlFotoOrang, viewFoto);
                    }
                }
                Toast.makeText(getBaseContext(), namaPemilik, Toast.LENGTH_LONG).show();
            }
            profilDitampilkan= true;
        }
        else {
            viewLatar.removeView(detailProfil);
            profilDitampilkan= false;
        }

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


    class DetailPertanyaanAdapter extends BaseAdapter{
        DetailPertanyaanAdapter (){
        }

        @Override
        public int getCount() {
            return solusi.size() +1;
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
