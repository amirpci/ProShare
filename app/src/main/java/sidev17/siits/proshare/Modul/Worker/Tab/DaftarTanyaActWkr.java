package sidev17.siits.proshare.Modul.Worker.Tab;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eyalbira.loadingdots.LoadingDots;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Model.Permasalahan;
import sidev17.siits.proshare.Utils.ViewTool.MainAct_Header;
import sidev17.siits.proshare.Modul.Worker.DetailPertanyaanActivityWkr;

import sidev17.siits.proshare.Modul.Worker.TambahPertanyaanWkr;

import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Utilities;
import sidev17.siits.proshare.Utils.View.ImgViewTouch;
import sidev17.siits.proshare.Utils.View.MenuBarView;
import sidev17.siits.proshare.Utils.Warna;

import static com.android.volley.Request.*;


/**
 * Created by USER on 02/05/2018.
 */

public class DaftarTanyaActWkr extends MainAct_Header {
    public final int PENGGUNA_EXPERT_TERVERIFIKASI= 402;
    public final int PENGGUNA_EXPERT= 401;
    public final int PENGGUNA_BIASA= 400;

    List<Permasalahan> Masalah;
    private RecyclerView daftarTanya;
//    private ImageView tmbTambah;
    private SwipeRefreshLayout refreshLayout;
    RecyclerView.Adapter adapter;
    private LoadingDots loadingPertanyaan;
    private FirebaseUser user;

    private String Judul[]= {"What should I do whe this happen?", "How to gain inspiration?", "How to else?"};
    private String deskripsi[]= {"I do this everyday, but somehow...", "When it happens, I don't know what to do. I need inspiration.", "bla bla bla..."};

    private Image gambar[][];
    private int kategoriSoal[]= {1, 2, 1, 0};

    private View halamanKosong;


    @Nullable
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        View view= inflater.inflate(R.layout.activity_daftar_tanya_wkr, container, false);
        setContentView(R.layout.activity_daftar_tanya_wkr);
        loadingPertanyaan = /*(LoadingDots)view.*/findViewById(R.id.loading_pertanyaan);
        daftarTanya= /*view.*/findViewById(R.id.daftar_pertanyaan_wadah);
        refreshLayout = /*view.*/findViewById(R.id.refresh_pertanyaan);
        user = FirebaseAuth.getInstance().getCurrentUser();
        loadDaftarPertanyaan();
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.abuTua), getResources().getColor(R.color.abuLebihTua),
                getResources().getColor(R.color.abuSangatTua));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
/*
        tmbTambah= view.findViewById(R.id.daftar_pertanyaan_tambah);
        tmbTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utilities.isStoragePermissionGranted(DaftarTanyaActWkr.this)){
                   // Toast.makeText(DaftarTanyaActWkr.this, "sudah", Toast.LENGTH_SHORT).show();
                    Intent inten= new Intent(DaftarTanyaActWkr.this, TambahPertanyaanWkr.class);
                    inten.putExtra("idHalaman", R.layout.activity_tambah_pertanyaan_wkr);
                    startActivity(inten);
                }else {
                    Toast.makeText(DaftarTanyaActWkr.this, "Failed to get storage permission!", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/
        initHeaderDalam();
        initHalamanKosong();
    }
    private void initHeaderDalam(){
//        final MainActivityWkr mainAct= (MainActivityWkr) getActivity();
        initHeader();
        aturJudulHeader("Daftar Pertanyaan");
        aturTambahanHeader("(" +Integer.toString(adapter.getItemCount()) +")");
    }
    private void initHalamanKosong(){
        halamanKosong= findViewById(R.id.daftar_pertanyaan_kosong);
        int visibilitas= (adapter.getItemCount() == 0) ? View.VISIBLE : View.GONE;
        halamanKosong.setVisibility(visibilitas);
    }

    void loadDaftarPertanyaan(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DaftarTanyaActWkr.this);
        daftarTanya.setLayoutManager(linearLayoutManager);
        daftarTanya.setLayoutManager(new LinearLayoutManager(this));
        Masalah = new ArrayList<>();
        adapter = new RC_Masalah(Masalah, this);
        daftarTanya.setAdapter(adapter);
        loadData();
    }
    private void bersihkanList(){
        Masalah.clear();
        adapter.notifyDataSetChanged();
    }
    private void loadData() {
        bersihkanList();
        refreshLayout.setRefreshing(false);
        loadingPertanyaan.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Method.POST, Konstanta.PERTANYAAN_SAYA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Masalah.clear();
                            JSONArray jsonArr = new JSONArray(response);
                          //  Toast.makeText(DaftarTanyaActWkr.this, "Berhasil loading!", Toast.LENGTH_SHORT).show();
                            for(int i=0; i<jsonArr.length(); i++){
                                try {
                                    JSONObject jsonObject = jsonArr.getJSONObject(i);
                                    Permasalahan masalah = new Permasalahan();
                                    masalah.setproblem_desc(jsonObject.getString("problem_desc"));
                                    masalah.setproblem_title(jsonObject.getString("problem_title"));
                                    masalah.setproblem_owner(jsonObject.getString("problem_owner"));
                                    masalah.setStatus(jsonObject.getInt("status"));
                                    masalah.setTimestamp(jsonObject.getString("timestamp"));
                                    masalah.setpid(jsonObject.getString("pid"));
                                    masalah.setmajority_id(jsonObject.getString("majority_id"));
                                    Masalah.add(masalah);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadingPertanyaan.setVisibility(View.GONE);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPertanyaan.setVisibility(View.GONE);
                if(this != null)
                    Toast.makeText(DaftarTanyaActWkr.this, "Failed to load question!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> masalah = new HashMap<>();
                masalah.put("problem_owner", user.getEmail().replace(",","."));
                return masalah;
            }
        };
        Volley.newRequestQueue(DaftarTanyaActWkr.this).add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

//=========================SAMPAI SINI MIR!=======================
    protected void hapusPertanyaan(int idPertanyaan){
        //sesuatu
    }

    public class RC_Masalah extends RecyclerView.Adapter<RC_Masalah.vH>{
        private List<Permasalahan> masalah;
        Activity act;
        public RC_Masalah(List<Permasalahan> masalah, Activity act) {
            this.masalah = masalah;
            this.act = act;
        }

        @NonNull
        @Override
        public vH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new vH(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_daftar_pertanyaan, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull vH holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return masalah.size();
        }

        public class vH extends RecyclerView.ViewHolder{
            private TextView judul, isi;
            private ImageView centang, foto;
            private View view;
            private MenuBarView menuBar;
            public vH(View itemView) {
                super(itemView);
                view = itemView;
                centang = (ImageView) itemView.findViewById(R.id.daftar_pertanyaan_centang);
                judul = (TextView)itemView.findViewById(R.id.daftar_pertanyaan_judul);
                isi = (TextView)itemView.findViewById(R.id.daftar_pertanyaan_deskripsi);
                foto = (ImageView)itemView.findViewById(R.id.daftar_pertanyaan_gambar);
                initMenuBar();
            }
            private void initMenuBar(){
                menuBar= itemView.findViewById(R.id.opsi_pertanyaan);
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
                        if(Utilities.isStoragePermissionGranted(DaftarTanyaActWkr.this)){
                            Intent keEdit= new Intent(DaftarTanyaActWkr.this, TambahPertanyaanWkr.class);

                            keEdit.putExtra("judul", judul.getText().toString());
                            keEdit.putExtra("deskripsi", isi.getText().toString());
//                            TextView majority= viewPertanyaan.findViewById(R.id.tl_majority);
                            String bidang= null; //ambil bidang dari server
                            keEdit.putExtra("bidang", bidang);
                            ArrayList<String> fotoLampiran= null; //ambil foto dari server
                            keEdit.putExtra("urlFoto", fotoLampiran);
//                keEdit.putExtra("gambarAwal", gambarAwal);

                            startActivity(keEdit);
                        }else {
                            Toast.makeText(DaftarTanyaActWkr.this, "Failed to get storage permission!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                menuBar.aturAksiKlikItem(1, new ImgViewTouch.PenungguKlik() {
                    @Override
                    public void klik(View v) {
                        hapusPertanyaan(0);
                    }
                });

                daftarkanBatas(menuBar.ambilBatas());
            }

            public void bind(final int posisi){
                switch (masalah.get(posisi).getStatus()){
                    case Konstanta.PROBLEM_STATUS_UNVERIFIED:
                        centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
                        break;
                    case Konstanta.PROBLEM_STATUS_VERIFIED:
                        centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);
                        break;
                    case Konstanta.PROBLEM_STATUS_REJECTED:
                        centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_silang);
                }
                judul.setText(masalah.get(posisi).getproblem_title());
                isi.setText(masalah.get(posisi).getproblem_desc());
                String[] judulIsi = {masalah.get(posisi).getproblem_title(), masalah.get(posisi).getproblem_desc()};
                new AsyncTask<String[], Void, String[]>(){

                    @Override
                    protected String[] doInBackground(String[]... strings) {
                        String[] output = strings[0];
                        for(int i=0;i<output.length;i++){
                            if(DaftarTanyaActWkr.this!=null)
                                output[i] = Utilities.ubahBahasaDariId(output[i], Utilities.getUserBahasa(DaftarTanyaActWkr.this), DaftarTanyaActWkr.this);
                        }
                        return  output;
                    }

                    @Override
                    protected void onPostExecute(String[] strings) {
                        if(judul!=null && isi!=null){
                            judul.setText(strings[0]);
                            isi.setText(strings[1]);
                        }
                    }
                }.execute(judulIsi);
              //  Toast.makeText(act, masalah.get(posisi).getpid(), Toast.LENGTH_SHORT).show();
               // Toast.makeText(act, masalah.get(posisi).getTimestamp(), Toast.LENGTH_SHORT).show();
                Utilities.updateFoto(masalah.get(posisi).getpid(), foto, act);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle paketDetailPetanyaan= new Bundle();
                        paketDetailPetanyaan.putString("judul_pertanyaan", masalah.get(posisi).getproblem_title());
                        paketDetailPetanyaan.putString("deskripsi_pertanyaan", masalah.get(posisi).getproblem_desc());
                        paketDetailPetanyaan.putString("owner", masalah.get(posisi).getproblem_owner());
                        paketDetailPetanyaan.putString("waktu", masalah.get(posisi).getTimestamp());
                        paketDetailPetanyaan.putString("pid", masalah.get(posisi).getpid());
                        paketDetailPetanyaan.putString("majority", masalah.get(posisi).getmajority_id());
                        paketDetailPetanyaan.putInt("status", masalah.get(posisi).getStatus());
                        Intent inten= new Intent(DaftarTanyaActWkr.this, DetailPertanyaanActivityWkr.class);
                        inten.putExtra("paket_detail_pertanyaan", paketDetailPetanyaan);
                        startActivity(inten);
                    }
                });
            }
        }
    }

}
