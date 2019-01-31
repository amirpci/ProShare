package sidev17.siits.proshare.Modul.Worker.Tab;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Model.Bidang;
import sidev17.siits.proshare.Model.Permasalahan;
import sidev17.siits.proshare.Model.Problem.Solusi;
import sidev17.siits.proshare.Modul.Worker.DetailPertanyaanActivityWkr;
import sidev17.siits.proshare.Modul.Worker.TambahPertanyaanWkr;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.AlgoritmaKesamaan;
import sidev17.siits.proshare.Utils.Array;
import sidev17.siits.proshare.Utils.Utilities;
import java.sql.Timestamp;

import static android.app.Activity.RESULT_OK;

/**
 * Created by USER on 02/05/2018.
 */

public class ShareActWkr extends Fragment {
    public final int PENGGUNA_EXPERT_TERVERIFIKASI= 402;
    public final int PENGGUNA_EXPERT= 401;
    public final int PENGGUNA_BIASA= 400;
    private LinearLayout relatedQuestion, addQuestion, loading;
    private EditText search_input,question_input;
    private TextView fileName;
    private ImageView search_btn, uploadPhoto, uploadedPhoto;
    private Button add_question;
    private RecyclerView rcTimeline;
    private ProgressBar uploadPhotoProgress;
    private ProgressDialog addQuestionLoading;
    private SwipeRefreshLayout refresh;
    private LoadingDots loadingDitemukan;
    private DatabaseReference dataRef;
    private StorageReference storageRef;
    private RC_Masalah adapter;
    //private ArrayList<Permasalahan> Masalah;
   //private ArrayList<Solusi> Solusi;
    private RelativeLayout layoutTidakDitemukan;
    private RelativeLayout jumlahDitemukan;
    private static final int UpPhotoID = 2;
    private boolean photoDiambil = false, videoDiambil = false;
    private int QuestionID=0;
    private Uri uriPhoto;
    private String Photo_url;

    private Spinner pilihanMajority;
    private int idBidang = 0;
    private SpinnerAdp adpMajority;
    private boolean useMajority = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tanya_wkr, container, false);
        storageRef = FirebaseStorage.getInstance().getReference();
        dataRef = FirebaseDatabase.getInstance().getReference();
        refresh = (SwipeRefreshLayout)v.findViewById(R.id.refresh_timeline);
        relatedQuestion = (LinearLayout)v.findViewById(R.id.tanya_related_q);
        addQuestion = (LinearLayout)v.findViewById(R.id.tanya_add_question);
        search_input = (EditText)v.findViewById(R.id.et_search_question);
        question_input = (EditText)v.findViewById(R.id.tanya_desc);
        fileName = (TextView)v.findViewById(R.id.tanya_file_name);
        search_btn = (ImageView)v.findViewById(R.id.tanya_cari_icon);
        uploadPhoto = (ImageView)v.findViewById(R.id.tanya_add_photo);
        uploadedPhoto = (ImageView)v.findViewById(R.id.tanya_uploaded_photo);
        add_question = (Button)v.findViewById(R.id.tanya_addQuestion_btn);
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
                loadData(new PencarianListener() {
                    @Override
                    public void ketemu(ArrayList<Solusi> solusi) {
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
                    cariMasalahan(search_input.getText().toString(), new PencarianListener() {
                        @Override
                        public void ketemu(ArrayList<Solusi> solusi) {
                            adapter = new RC_Masalah(solusi, getActivity(), RC_Masalah.TIPE_TIMELINE_CARI);
                            rcTimeline.setAdapter(adapter);
                        }
                    });
                    return true;
                }
                return false;
            }
        });
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

        loadDaftarPertanyaan();
        return v;
    }

    void initPilihanMajority(ArrayList<Bidang> majority, View v){

        pilihanMajority= v.findViewById(R.id.timeline_pilihan_majority);
        adpMajority= new SpinnerAdp(majority, getContext());
        pilihanMajority.setAdapter(adpMajority);
    }

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
                initPilihanMajority(bdg, v);
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

    private void tetapkanJumlahKetemu(int ketemu){
        jumlahDitemukan.setVisibility(View.VISIBLE);
        TextView total = (TextView)jumlahDitemukan.findViewById(R.id.txt_total_ditemukan);
        total.setText(String.valueOf(ketemu)+" found");
    }

    private void initTambahPertanyaan(){
        layoutTidakDitemukan.setVisibility(View.VISIBLE);
        layoutTidakDitemukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utilities.isStoragePermissionGranted(getActivity()))
                   startActivity(new Intent(getActivity(), TambahPertanyaanWkr.class));
            }
        });
    }
    private void cariMasalahan(final String cari, final PencarianListener ls) {
        jumlahDitemukan.setVisibility(View.GONE);
        //hkanList();
        layoutTidakDitemukan.setVisibility(View.GONE);
        loadingDitemukan.setVisibility(View.VISIBLE);
        final ArrayList<Permasalahan> Masalah = new ArrayList<>();
        if(!useMajority){
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
                               // Masalah.addAll(algoSama.listKetemu());
                               // adapter.notifyDataSetChanged();
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
            });
            Volley.newRequestQueue(getActivity()).add(stringRequest);
        }else{
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
        jumlahDitemukan.setVisibility(View.GONE);
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
        private int tipeSekarang;
        Activity act;
        public RC_Masalah(List<Solusi> solusi, Activity act, int tipe) {
            this.solusi = solusi;
            this.act = act;
            tipeSekarang = tipe;
        }

        @NonNull
        @Override
        public vH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(tipeSekarang == TIPE_TIMELINE_CARI)
                return new vH(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_daftar_pertanyaan, parent, false));
            else
                return new vH(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_daftar_pertanyaan_timeline, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull vH holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return solusi.size();
        }

        public class vH extends RecyclerView.ViewHolder{
            private TextView judulProblem, tanggalProblem, deskripsiSolusi, jumlahKomentar, totalVote, isi;
            private ImageView centang, foto;
            private LinearLayout lampiranSolusi;
            private View view;
            public vH(View itemView) {
                super(itemView);
                view = itemView;
                judulProblem = itemView.findViewById(R.id.tl_problem_judul);
                tanggalProblem = itemView.findViewById(R.id.tl_problem_tanggal);
                totalVote = itemView.findViewById(R.id.tl_problem_total_vote);
                deskripsiSolusi = itemView.findViewById(R.id.tl_solusi_deskripsi);
                jumlahKomentar = itemView.findViewById(R.id.tl_komentar_jumlah);
                lampiranSolusi = itemView.findViewById(R.id.lampiran_solusi);
            }
            public void bind(final int posisi){
                /*
                if(masalah.get(posisi).getStatus()==PENGGUNA_EXPERT){
                    centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);
                }else if(masalah.get(posisi).getStatus()==PENGGUNA_BIASA){
                    centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
                } */

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
                    deskripsiSolusi.setText(solusi.get(posisi).getDeskripsi());
                    jumlahKomentar.setText("dan "+String.valueOf(solusi.get(posisi).getJumlahKomentar())+" komentar lainnya...");
                } else if(tipeSekarang == TIPE_TIMELINE_CARI){
                    deskripsiSolusi.setText(solusi.get(posisi).getDeskripsi());
                    jumlahKomentar.setText("dan "+String.valueOf(solusi.get(posisi).getJumlahKomentar())+" komentar lainnya...");
                }
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
                        Intent inten= new Intent(getContext(), DetailPertanyaanActivityWkr.class);
                        inten.putExtra("paket_detail_pertanyaan", paketDetailPetanyaan);
                        startActivity(inten);
                    }
                });
                //  Toast.makeText(act, masalah.get(posisi).getpid(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(act, masalah.get(posisi).getTimestamp(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}

