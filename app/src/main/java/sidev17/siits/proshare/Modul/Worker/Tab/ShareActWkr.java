package sidev17.siits.proshare.Modul.Worker.Tab;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.service.carrier.CarrierIdentifier;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.eyalbira.loadingdots.LoadingDots;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.StringValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sidev17.siits.proshare.Adapter.SpinnerAdp;
import sidev17.siits.proshare.Interface.PencarianListener;
import sidev17.siits.proshare.Interface.PerubahanTerjemahListener;
import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Model.Bidang;
import sidev17.siits.proshare.Model.Permasalahan;
import sidev17.siits.proshare.Model.Problem.Solusi;
import sidev17.siits.proshare.Modul.Expert.MainActivityExprt;
import sidev17.siits.proshare.Modul.Worker.MainActivityWkr;
import sidev17.siits.proshare.Utils.ViewTool.Aktifitas;
import sidev17.siits.proshare.Utils.ViewTool.Aktifitas_Slider;
import sidev17.siits.proshare.Utils.ViewTool.Fragment_Header;
import sidev17.siits.proshare.Utils.ViewTool.MainAct_Header;
import sidev17.siits.proshare.Modul.Worker.DetailPertanyaanActivityWkr;
import sidev17.siits.proshare.Modul.Worker.TambahPertanyaanWkr;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.AlgoritmaKesamaan;
import sidev17.siits.proshare.Utils.Array;
import sidev17.siits.proshare.Utils.Terjemahan;
import sidev17.siits.proshare.Utils.Utilities;

import static android.app.Activity.RESULT_OK;
import static sidev17.siits.proshare.Utils.Utilities.initViewSolusiLampiran;

/**
 * Created by USER on 02/05/2018.
 */

public class ShareActWkr extends Fragment_Header {
    public final int PENGGUNA_EXPERT_TERVERIFIKASI= 402;
    public final int PENGGUNA_EXPERT= 401;
    public final int PENGGUNA_BIASA= 400;
    private LinearLayout relatedQuestion, addQuestion, loading;
    private EditText search_input,question_input;
    private TextView fileName, tmbSearch;
    private ImageView search_btn, uploadPhoto, uploadedPhoto;
//    private ImageView vTambahTimeline;
    private RecyclerView rcTimeline;
    private ProgressBar uploadPhotoProgress;
    private ProgressDialog addQuestionLoading;
    private SwipeRefreshLayout refresh;
    private LoadingDots loadingDitemukan;
    private DatabaseReference dataRef;
    private StorageReference storageRef;
    private RC_Masalah adapter;
    private int ketemu= 0;
    //private ArrayList<Permasalahan> Masalah;
   //private ArrayList<Solusi> Solusi;
    private View layoutTidakDitemukan;
    private RelativeLayout jumlahDitemukan;
    private static final int UpPhotoID = 2;
    private boolean photoDiambil = false, videoDiambil = false;
    private int QuestionID=0;
    private Uri uriPhoto;
    private String Photo_url;

    private boolean mulaiMencari= false;

  //  private Spinner pilihanMajority;
  //  private int idBidang = 0;
    //private SpinnerAdp adpMajority;
   // private boolean useMajority = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tanya_wkr, container, false);
        storageRef = FirebaseStorage.getInstance().getReference();
        dataRef = FirebaseDatabase.getInstance().getReference();
        refresh = (SwipeRefreshLayout)v.findViewById(R.id.refresh_timeline);
        relatedQuestion = (LinearLayout)v.findViewById(R.id.tanya_related_q);
        addQuestion = (LinearLayout)v.findViewById(R.id.tanya_add_question);
        question_input = (EditText)v.findViewById(R.id.tanya_desc);
        fileName = (TextView)v.findViewById(R.id.tanya_file_name);
        search_btn = (ImageView)v.findViewById(R.id.tanya_cari_icon);
        uploadPhoto = (ImageView)v.findViewById(R.id.tanya_add_photo);
        uploadedPhoto = (ImageView)v.findViewById(R.id.tanya_uploaded_photo);
/*
        vTambahTimeline= v.findViewById(R.id.timeline_tambah);
        vTambahTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keTambahTimeline= new Intent(getActivity(), TambahPertanyaanWkr.class);
                keTambahTimeline.putExtra("jenisPost", TambahPertanyaanWkr.JENIS_POST_SHARE);
                startActivity(keTambahTimeline);
            }
        });
*/

        search_input = (EditText)v.findViewById(R.id.et_search_question);
        search_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
//                    mulaiMencari= true;
//                    actInduk.tampilkanTombolUtama(false);
//                    vTambahTimeline.setVisibility(View.GONE);
                    ubahWarnaTmbCari(getResources().getColor(R.color.biruLaut));
                }
                else{
//                    mulaiMencari= false;
//                    actInduk.tampilkanTombolUtama(true);
//                    vTambahTimeline.setVisibility(View.VISIBLE);
                    ubahWarnaTmbCari(getResources().getColor(R.color.abuTua));
                }
                actInduk.gantiIconTombolUtama();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        uploadPhotoProgress = (ProgressBar) v.findViewById(R.id.tanya_upPhoto_loading);
        loading = (LinearLayout)v.findViewById(R.id.tanya_progress);
        rcTimeline = (RecyclerView)v.findViewById(R.id.list_timeline);
        loadingDitemukan = (LoadingDots)v.findViewById(R.id.loading_ditemukan);
        layoutTidakDitemukan = v.findViewById(R.id.layout_tambah_pertanyaan);
        jumlahDitemukan = v.findViewById(R.id.jumlah_ditemukan);
        refresh.setColorSchemeColors(getResources().getColor(R.color.abuTua), getResources().getColor(R.color.abuLebihTua),
                getResources().getColor(R.color.abuSangatTua));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter = new RC_Masalah(new ArrayList<Solusi>(), getActivity(), RC_Masalah.TIPE_TIMELINE_DEFAULT);
                rcTimeline.setAdapter(adapter);
                loadData(new PencarianListener() {
                    @Override
                    public void ketemu(ArrayList<Solusi> solusi) {
                        Log.d("k", "io");
                        adapter = new RC_Masalah(solusi, getActivity(), RC_Masalah.TIPE_TIMELINE_DEFAULT);
                        rcTimeline.setAdapter(adapter);
                    }
                });
            }
        });
        search_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    cariArtikel(true);
                    return true;
                }
                return false;
            }
        });
        tmbSearch= v.findViewById(R.id.ic_search);
        tmbSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cariArtikel(false);
            }
        });
       /*
        ArrayList<Bidang> bdg = new ArrayList<>();
        initPilihanMajority(bdg, v);
        loadPilihanMajorityServer(v);
        pilihanMajority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idBidang = position + 1;
                if(position == 0){
                    useMajority = false;
                }else{
                    useMajority = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */
        loadDaftarPertanyaan();
//        initHeader();
        judulHeader= "Pustaka";
        keHalamanAwal();
        daftarkanAksiBackPress(new Aktifitas.AksiBackPress() {
            @Override
            public boolean backPress() {
                boolean aksiBackPress= false;
                if(mulaiMencari){
                    mulaiMencari= false;
                    adapter = new RC_Masalah(new ArrayList<Solusi>(), getActivity(), RC_Masalah.TIPE_TIMELINE_DEFAULT);
                    rcTimeline.setAdapter(adapter);
                    loadData(new PencarianListener() {
                        @Override
                        public void ketemu(ArrayList<Solusi> solusi) {
                            Log.d("k", "io");
                            adapter = new RC_Masalah(solusi, getActivity(), RC_Masalah.TIPE_TIMELINE_DEFAULT);
                            rcTimeline.setAdapter(adapter);
                        }
                    });
                    aksiBackPress |= true;
                    actInduk.tampilkanTombolUtama(true);
                }
                if(search_input.getText().length() > 0){
                    search_input.setText("");
                    aksiBackPress |= true;
                }
                return aksiBackPress;
            }
        });
        daftarkanAksiTombolUtama(new MainAct_Header.AksiTombolUtama() {
            @Override
            public void tekan(View v, int halaman) {
                Intent keTambahTimeline= new Intent(getActivity(), TambahPertanyaanWkr.class);
                keTambahTimeline.putExtra("jenisPost", TambahPertanyaanWkr.JENIS_POST_SHARE);
                startActivity(keTambahTimeline);
            }
        });
        daftarkanPenampilTombolUtama(new MainAct_Header.PenampilTombolUtama() {
            @Override
            public boolean tampilkan(View tombolUtama) {
                return !mulaiMencari && search_input.getText().length() <= 0;
            }
        });
        ubahWarnaTmbCari(getResources().getColor(R.color.abuTua));
        return v;
    }

    @Override
    public void initHeader() {
//        Toast.makeText(actInduk, "INIT!!!", Toast.LENGTH_SHORT).show();
//        final MainAct_Header mainAct= (MainAct_Header) actInduk;
//        actInduk.aturJudulHeader("Pustaka");
        actInduk.aturGambarOpsiHeader(0, R.drawable.icon_daftar);
        actInduk.aturKlikOpsiHeader(0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keDaftarPertanyaan= new Intent(actInduk, DaftarTanyaActWkr.class);
                startActivity(keDaftarPertanyaan);
            }
        });
    }
    protected void keHalamanAwal(){
        Aktifitas_Slider mainAct= (Aktifitas_Slider) getActivity();
        mainAct.keHalaman(1);
        String ada= "ada";
        if(mainAct == null)
            ada= "null";
//        Toast.makeText(getContext(), "masukCOY!!! " +ada, Toast.LENGTH_SHORT).show();
    }
/*
    @Override
    public void onStop() {
        MainAct_Header mainAct= (MainAct_Header) getActivity();
        mainAct.bolehInitHeader= false;
        super.onStop();
    }
*/
    private void ubahWarnaTmbCari(int warna){
        tmbSearch.getBackground().setTint(warna);
    }
    private void cariArtikel(boolean ngeToast){
        if(search_input.getText().length() > 0){
            mulaiMencari = true;
            adapter = new RC_Masalah(new ArrayList<Solusi>(), getActivity(), RC_Masalah.TIPE_TIMELINE_DEFAULT);
            rcTimeline.setAdapter(adapter);
            loadingDitemukan.setVisibility(View.VISIBLE);
            Terjemahan.terjemahkanAsync(new String[]{search_input.getText().toString()}, Utilities.getUserBahasa(getActivity()), "en", getActivity(), new PerubahanTerjemahListener() {
                @Override
                public void dataBerubah(String[] kata) {
                    cariMasalahan(kata[0], new PencarianListener() {
                        @Override
                        public void ketemu(ArrayList<Solusi> solusi) {
                            adapter = new RC_Masalah(solusi, getActivity(), RC_Masalah.TIPE_TIMELINE_CARI);
                            rcTimeline.setAdapter(adapter);
                        }
                    });
                    }
            });
        } else if(ngeToast){
            Toast.makeText(actInduk, "Type your question first", Toast.LENGTH_SHORT).show();
        }
    }
    /*
    void initPilihanMajority(ArrayList<Bidang> majority, View v){

        pilihanMajority= v.findViewById(R.id.timeline_pilihan_majority);
        adpMajority= new SpinnerAdp(majority, getContext());
        pilihanMajority.setAdapter(adpMajority);
    } */

    void loadPilihanMajorityServer(final View v){
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
                Bidang all = new Bidang();
                all.setId(String.valueOf(0));
                all.setBidang("All");
                bdg.set(0, all);
             //   initPilihanMajority(bdg, v);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getActivity()).add(request);
    }

    private void bersihkanList(){
        adapter.notifyDataSetChanged();
    }

    private void hilangkanTandaKetemu(){
        jumlahDitemukan.setVisibility(View.GONE);
    }
    private void tetapkanJumlahKetemu(int ketemu){
        this.ketemu= ketemu;
        jumlahDitemukan.setVisibility(View.VISIBLE);
    }

    private void initTambahPertanyaan(){
        layoutTidakDitemukan.setVisibility(View.VISIBLE);
        layoutTidakDitemukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utilities.isStoragePermissionGranted(getActivity())) {
                    Intent keTambahPertanyaan= new Intent(getActivity(), TambahPertanyaanWkr.class);
                    keTambahPertanyaan.putExtra("jenisPost", TambahPertanyaanWkr.JENIS_POST_TANYA);
                    startActivity(keTambahPertanyaan);
                }
            }
        });
    }
    private void cariMasalahan(final String cari, final PencarianListener ls) {
        hilangkanTandaKetemu();
        //hkanList();
        layoutTidakDitemukan.setVisibility(View.GONE);
        final ArrayList<Permasalahan> Masalah = new ArrayList<>();
       // if(!useMajority){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.SEARCH_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                ArrayList<Solusi> Solusi = new ArrayList<>();
                                JSONArray jsonArr = new JSONArray(response);
                                for(int i=0; i<jsonArr.length(); i++){
                                    try {
                                        JSONObject jsonObject = jsonArr.getJSONObject(i);
                                        Solusi sol = new Solusi();
                                        Permasalahan masalah = new Permasalahan();
                                        masalah.setproblem_desc(jsonObject.getString("problem_desc"));
                                        masalah.setproblem_title(jsonObject.getString("problem_title"));
                                        masalah.setproblem_owner(jsonObject.getString("problem_owner"));
                                        masalah.setStatus(jsonObject.getInt("status"));
                                        masalah.setTimestamp(jsonObject.getString("timestamp"));
                                        masalah.setpid(jsonObject.getString("pid"));
                                        masalah.setmajority_id(jsonObject.getString("majority_id"));
                                        masalah.setStatuspost(jsonObject.getInt("status_post"));
                                        sol.setProblem(masalah);
                                        sol.setId_solusi(jsonObject.getString("id_solusi"));
                                        sol.setDeskripsi(jsonObject.getString("deskripsi"));
                                        sol.setJumlahKomentar(jsonObject.getInt("more"));
                                        Solusi.add(sol);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        loadingDitemukan.setVisibility(View.GONE);
                                    }
                                }
                                AlgoritmaKesamaan algoSama = new AlgoritmaKesamaan(Solusi, cari);
                                ArrayList<Solusi> ketemu = algoSama.listKetemu();
                                ls.ketemu(ketemu);

                                if(ketemu.size()==0){
                                    initTambahPertanyaan();
                                }else {
                                    tetapkanJumlahKetemu(ketemu.size());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Might error occured!", Toast.LENGTH_SHORT).show();
                            }
                            loadingDitemukan.setVisibility(View.GONE);
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingDitemukan.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Terjadi kesalahan jaringan!", Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(getActivity()).add(stringRequest);
       // }
        /*else{
            StringRequest stringRequestBidang = new StringRequest(Request.Method.POST, Konstanta.CARI_BIDANG,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                ArrayList<Solusi> Solusi = new ArrayList<>();
                                JSONArray jsonArr = new JSONArray(response);
                                Toast.makeText(getActivity(), "Berhasil loading! dengan panjang "+String.valueOf(jsonArr.length())+" bidang "+String.valueOf(idBidang), Toast.LENGTH_SHORT).show();
                                for(int i=0; i<jsonArr.length(); i++){
                                    try {
                                        JSONObject jsonObject = jsonArr.getJSONObject(i);
                                        Permasalahan masalah = new Permasalahan();
                                        Solusi sol = new Solusi();
                                        masalah.setproblem_desc(jsonObject.getString("problem_desc"));
                                        masalah.setproblem_title(jsonObject.getString("problem_title"));
                                        masalah.setproblem_owner(jsonObject.getString("problem_owner"));
                                        masalah.setStatus(jsonObject.getInt("status"));
                                        masalah.setTimestamp(jsonObject.getString("timestamp"));
                                        masalah.setpid(jsonObject.getString("pid"));
                                        masalah.setmajority_id(jsonObject.getString("majority_id"));
                                        sol.setProblem(masalah);
                                        sol.setId_solusi(jsonObject.getString("id_solusi"));
                                        sol.setDeskripsi(jsonObject.getString("deskripsi"));
                                        sol.setJumlahKomentar(jsonObject.getInt("more"));
                                        Solusi.add(sol);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        loadingDitemukan.setVisibility(View.GONE);
                                    }
                                }
                                AlgoritmaKesamaan algoSama = new AlgoritmaKesamaan(Solusi, cari);
                                ls.ketemu(algoSama.listKetemu());
                                if(Solusi.size()==0){
                                    initTambahPertanyaan();
                                }else {
                                    tetapkanJumlahKetemu(Masalah.size());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            loadingDitemukan.setVisibility(View.GONE);
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingDitemukan.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Terjadi kesalahan jaringan!", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> cari = new HashMap<>();
                    cari.put("majority", String.valueOf(idBidang));
                    return cari;
                }
            };
            Volley.newRequestQueue(getActivity()).add(stringRequestBidang);
        }
        */
    }

    void loadDaftarPertanyaan(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcTimeline.setLayoutManager(linearLayoutManager);
        rcTimeline.setLayoutManager(new LinearLayoutManager(getActivity()));
       // Solusi = new ArrayList<>();
        loadData(new PencarianListener() {
            @Override
            public void ketemu(ArrayList<sidev17.siits.proshare.Model.Problem.Solusi> solusi) {
                adapter = new RC_Masalah(solusi, getActivity(), RC_Masalah.TIPE_TIMELINE_DEFAULT);
                rcTimeline.setAdapter(adapter);
               // adapter.notifyDataSetChanged();
            }
        });
    }
    private void loadData(final PencarianListener ls) {
        hilangkanTandaKetemu();
       // bersihkanList();
        layoutTidakDitemukan.setVisibility(View.GONE);
        refresh.setRefreshing(false);
        loadingDitemukan.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.TIMELINE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ArrayList<Solusi> Solusi = new ArrayList<>();
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            //Log.d("loaddata", "ok"+String.valueOf(jsonArr.length()));
                            for(int i=0; i<jsonArr.length(); i++){
                                try {
                                    JSONObject jsonObject = jsonArr.getJSONObject(i);
                                    Solusi sol = new Solusi();
                                    Permasalahan masalah = new Permasalahan();
                                    Log.d("loadData ", jsonObject.getString("problem_title")+"\n");
                                    masalah.setproblem_desc(jsonObject.getString("problem_desc"));
                                    masalah.setproblem_title(jsonObject.getString("problem_title"));
                                    masalah.setproblem_owner(jsonObject.getString("problem_owner"));
                                    masalah.setStatus(jsonObject.getInt("status"));
                                    masalah.setTimestamp(jsonObject.getString("timestamp"));
                                    masalah.setpid(jsonObject.getString("pid"));
                                    masalah.setmajority_id(jsonObject.getString("majority_id"));
                                    masalah.setTotalVote(jsonObject.getInt("vote"));
                                    masalah.setStatuspost(jsonObject.getInt("status_post"));
                                    sol.setProblem(masalah);
                                    sol.setId_solusi(jsonObject.getString("id_solusi"));
                                    sol.setDeskripsi(jsonObject.getString("deskripsi"));
                                    sol.setJumlahKomentar(jsonObject.getInt("more"));
                                    Solusi.add(sol);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    loadingDitemukan.setVisibility(View.GONE);
                                }
                            }
                            ls.ketemu(Solusi);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadingDitemukan.setVisibility(View.GONE);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDitemukan.setVisibility(View.GONE);
                if(getActivity()!=null)
                Toast.makeText(getActivity(), "Network problem occured!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
    //cuma method percobaan
    void searchQuestion(final String input){
        dataRef.child("search").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(input.equals(dataSnapshot.getValue(String.class))){
                    transisiFadeIn(relatedQuestion);
                    transisiFadeOutBawah(addQuestion);
                }else{
                    transisiFadeIn(addQuestion);
                    transisiFadeOutAtas(relatedQuestion);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    boolean tambahPertanyaan(){
        getQuestionID();
        if(photoDiambil){
            dataRef.child("Problems").child(String.valueOf(QuestionID)).child("photo").setValue(Photo_url);
        }else{
            dataRef.child("Problems").child(String.valueOf(QuestionID)).child("photo").setValue("-");
        }
        if(!videoDiambil){
            dataRef.child("Problems").child(String.valueOf(QuestionID)).child("video").setValue("-");
        }
        dataRef.child("Problems").child(String.valueOf(QuestionID)).child("text").setValue(question_input.getText().toString());
        return true;
    }

    void transisiFadeOutBawah(final View v){
        v.animate()
                .translationY(v.getHeight())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.clearAnimation();
                        v.setVisibility(View.GONE);
                    }
                });
    }

    void transisiFadeOutAtas(final View v){
        v.animate()
                .translationY(-v.getHeight())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.clearAnimation();
                        v.setVisibility(View.GONE);
                    }
                });
    }

    void transisiFadeIn(final View v){
        v.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.setVisibility(View.VISIBLE);
                    }
                });
    }
    //untuk generate ID pertanyaan
    void getQuestionID(){
        Query lastQuery = dataRef.child("Problems").orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot lastChild=null;
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    lastChild=child;
                }
                QuestionID=Integer.parseInt(lastChild.getKey())+1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==UpPhotoID && resultCode==RESULT_OK){
            uploadPhoto.setVisibility(View.INVISIBLE);
            uploadPhotoProgress.setVisibility(View.VISIBLE);
            String Photo_name;
            uriPhoto = data.getData();
            if(uriPhoto.getLastPathSegment().length()>=10){
                Photo_name="..."+uriPhoto.getLastPathSegment().substring(uriPhoto.getLastPathSegment().length()-10);
            }else{
                Photo_name="..."+uriPhoto.getLastPathSegment();
            }
            fileName.setText(Photo_name);
            getQuestionID();
            StorageReference filepath = storageRef.child("Photos").child(String.valueOf(QuestionID)).child(uriPhoto.getLastPathSegment());
            filepath.putFile(uriPhoto).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadPhoto.setVisibility(View.GONE);
                    uploadPhotoProgress.setVisibility(View.GONE);
                    photoDiambil=true;
   //                 Photo_url = taskSnapshot.getDownloadUrl().toString();
                    Glide.with(getActivity()).load(Photo_url).into(uploadedPhoto);
                    uploadedPhoto.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), "Question Added", Toast.LENGTH_LONG).show();
                   // dataRef.child("Problems").child(String.valueOf(QuestionID)).child("photos").setValue(Photo_url);
                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    public class RC_Masalah extends RecyclerView.Adapter<RC_Masalah.vH>{
        private List<Solusi> solusi;
        protected static final int TIPE_TIMELINE_DEFAULT = 2398;
        protected static final int TIPE_TIMELINE_CARI = 8923;
        protected static final int TIPE_TIMELINE_KNOWLEDGE =  9282;
        protected static final int TIPE_LIHAT_LAINNYA=  123;
        private int jmlDitampilkan;
        private int tipeSekarang;
        private int tipeView= 0;
        Activity act;
        public RC_Masalah(List<Solusi> solusi, Activity act, int tipe) {
            jmlDitampilkan= (tipe == TIPE_TIMELINE_CARI && solusi.size() > 0) ? 2 : solusi.size();
            this.solusi = solusi;
            this.act = act;
            tipeSekarang = tipe;
        }

        @NonNull
        @Override
        public vH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(tipeSekarang == TIPE_TIMELINE_CARI) {
                if(viewType == TIPE_LIHAT_LAINNYA) {
                    tipeView= TIPE_LIHAT_LAINNYA;
                    return new vH(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_timeline_lihat_lainnya, parent, false));
                }
                return new vH(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_daftar_pertanyaan, parent, false));
            } else{
                if(viewType == TIPE_TIMELINE_KNOWLEDGE)
                    return new vH(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_daftar_share_timeline, parent, false));
                return new vH(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_daftar_pertanyaan_timeline, parent, false));
            }
        }

        @Override
        public int getItemViewType(int position) {
            if(position == jmlDitampilkan-1 && tipeSekarang == TIPE_TIMELINE_CARI)
                return TIPE_LIHAT_LAINNYA;
            if (solusi.get(position).getProblem().getStatuspost() == TambahPertanyaanWkr.JENIS_POST_SHARE )
                return TIPE_TIMELINE_KNOWLEDGE;
            return TIPE_TIMELINE_DEFAULT;
        }

        @Override
        public void onBindViewHolder(@NonNull final vH holder, final int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return jmlDitampilkan;
        }

        public class vH extends RecyclerView.ViewHolder{
            private TextView judulProblem, tanggalProblem, deskripsiSolusi, jumlahKomentar, totalVote, isi, totalLainnya;
            private LinearLayout lampiranSolusi;
            private View view;
            public vH(View itemView) {
                super(itemView);
                view = itemView;
//                if(tipeView == TIPE_LIHAT_LAINNYA)
                totalLainnya= itemView.findViewById(R.id.teks_hasil_lainnya);
                judulProblem = itemView.findViewById(R.id.tl_problem_judul);
                tanggalProblem = itemView.findViewById(R.id.tl_problem_tanggal);
                totalVote = itemView.findViewById(R.id.tl_problem_total_vote);
                deskripsiSolusi = itemView.findViewById(R.id.tl_solusi_deskripsi);
                jumlahKomentar = itemView.findViewById(R.id.tl_komentar_jumlah);
                lampiranSolusi = itemView.findViewById(R.id.lampiran_solusi);
            }
            public void bind(final int posisi){
                if(tipeView == TIPE_LIHAT_LAINNYA) {
                    try {
                        totalLainnya.setText("Lihat " + String.valueOf(ketemu - 1) + " solusi lainnya...");
                    }catch (Exception e){
//                        throw new RuntimeException("posisi= " +posisi +" jmlDitampilkan= " +jmlDitampilkan +" " +e.getMessage());
                    }
                    return;
                }
                /*
                if(masalah.get(posisi).getStatus()==PENGGUNA_EXPERT){
                    centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);
                }else if(masalah.get(posisi).getStatus()==PENGGUNA_BIASA){
                    centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
                } */

                if (solusi.get(posisi).getProblem().getStatuspost() == TambahPertanyaanWkr.JENIS_POST_SHARE) {
                    if(judulProblem != null)
                        judulProblem.setText(solusi.get(posisi).getProblem().getproblem_title());
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat format2 = new SimpleDateFormat("MMMM dd, yyyy");
                    String waktu = solusi.get(posisi).getProblem().getTimestamp();
                    Date date = null;
                    try {
                        date = format1.parse(waktu);
                        waktu = format2.format(date);
                        if(tanggalProblem != null)
                            tanggalProblem.setText(waktu);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // tanggalProblem.setText(solusi.get(posisi).getProblem().getTimestamp());
                    totalVote.setText(String.valueOf(solusi.get(posisi).getProblem().getTotalVote()));
                } else {
                    if(tipeSekarang == TIPE_TIMELINE_DEFAULT){
                        judulProblem.setText(solusi.get(posisi).getProblem().getproblem_title());
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat format2 = new SimpleDateFormat("MMMM dd, yyyy");
                        String waktu = solusi.get(posisi).getProblem().getTimestamp();
                        Date date = null;
                        try {
                            date = format1.parse(waktu);
                            waktu = format2.format(date);
                            tanggalProblem.setText(waktu);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        // tanggalProblem.setText(solusi.get(posisi).getProblem().getTimestamp());
                        totalVote.setText(String.valueOf(solusi.get(posisi).getProblem().getTotalVote()));
                        deskripsiSolusi.setText(solusi.get(posisi).getDeskripsi());
                        jumlahKomentar.setText("and "+String.valueOf(solusi.get(posisi).getJumlahKomentar())+" more comments...");
                        gantiBahasa(TIPE_TIMELINE_DEFAULT);
                    }else{
                        deskripsiSolusi.setText(solusi.get(posisi).getDeskripsi());
                        jumlahKomentar.setText("and "+String.valueOf(solusi.get(posisi).getJumlahKomentar())+" more comments...");
                        gantiBahasa(TIPE_TIMELINE_CARI);
                    }
                }

                loadSolusiLampiran(lampiranSolusi, solusi.get(posisi).getId_solusi(), getActivity());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle paketDetailPetanyaan= new Bundle();
                        paketDetailPetanyaan.putString("judul_pertanyaan", solusi.get(posisi).getProblem().getproblem_title());
                        paketDetailPetanyaan.putString("deskripsi_pertanyaan", solusi.get(posisi).getProblem().getproblem_desc());
                        paketDetailPetanyaan.putString("owner", solusi.get(posisi).getProblem().getproblem_owner());
                        paketDetailPetanyaan.putString("waktu", solusi.get(posisi).getProblem().getTimestamp());
                        paketDetailPetanyaan.putString("pid", solusi.get(posisi).getProblem().getpid());
                        paketDetailPetanyaan.putString("majority", solusi.get(posisi).getProblem().getmajority_id());
                        paketDetailPetanyaan.putString("status_post", Integer.toString(solusi.get(posisi).getProblem().getStatuspost()));
                        Intent inten= new Intent(getContext(), DetailPertanyaanActivityWkr.class);
                        inten.putExtra("paket_detail_pertanyaan", paketDetailPetanyaan);
                        startActivity(inten);
                    }
                });
                //  Toast.makeText(act, masalah.get(posisi).getpid(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(act, masalah.get(posisi).getTimestamp(), Toast.LENGTH_SHORT).show();
            }

            private void gantiBahasa(int tipe){
                if(!Utilities.getUserBahasa(getActivity()).equalsIgnoreCase("en")){
                    if(tipe==TIPE_TIMELINE_DEFAULT){
                        String akanTranslate[] = {judulProblem.getText().toString(), tanggalProblem.getText().toString(), deskripsiSolusi.getText().toString(), jumlahKomentar.getText().toString()};
                        Terjemahan.terjemahkanAsync(akanTranslate, "en", Utilities.getUserBahasa(getActivity()), getActivity(), new PerubahanTerjemahListener() {
                            @Override
                            public void dataBerubah(String[] kata) {
                                judulProblem.setText(kata[0]);
                                tanggalProblem.setText(kata[1]);
                                deskripsiSolusi.setText(kata[2]);
                                jumlahKomentar.setText(kata[3]);
                            }
                        });
                    }else if(tipe == TIPE_TIMELINE_KNOWLEDGE){
                        String akanTranslate[] = {judulProblem.getText().toString(), tanggalProblem.getText().toString(), deskripsiSolusi.getText().toString()};
                        Terjemahan.terjemahkanAsync(akanTranslate, "en", Utilities.getUserBahasa(getActivity()), getActivity(), new PerubahanTerjemahListener() {
                            @Override
                            public void dataBerubah(String[] kata) {
                                judulProblem.setText(kata[0]);
                                tanggalProblem.setText(kata[1]);
                                deskripsiSolusi.setText(kata[2]);
                            }
                        });
                    } else {
                        String akanTranslate[] = {deskripsiSolusi.getText().toString(), jumlahKomentar.getText().toString()};
                        Terjemahan.terjemahkanAsync(akanTranslate, "en", Utilities.getUserBahasa(getActivity()), getActivity(), new PerubahanTerjemahListener() {
                            @Override
                            public void dataBerubah(String[] kata) {
                                deskripsiSolusi.setText(kata[0]);
                                jumlahKomentar.setText(kata[1]);
                            }
                        });
                    }
                }
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

        }
    }
}

