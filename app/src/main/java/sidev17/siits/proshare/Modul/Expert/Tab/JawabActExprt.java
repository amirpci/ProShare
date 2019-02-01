package sidev17.siits.proshare.Modul.Expert.Tab;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eyalbira.loadingdots.LoadingDots;
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
import sidev17.siits.proshare.Modul.Expert.TambahJawabanExprt;
import sidev17.siits.proshare.Modul.Worker.DetailPertanyaanActivityWkr;
import sidev17.siits.proshare.Modul.Worker.TambahPertanyaanWkr;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Utilities;

public class JawabActExprt extends Fragment {
    public final int PENGGUNA_EXPERT_TERVERIFIKASI= 2;
    public final int PENGGUNA_EXPERT= 1;
    public final int PENGGUNA_BIASA= 0;

    List<Permasalahan> Masalah;
    private RecyclerView daftarTanya;
    private ImageView tmbTambah;
    private SwipeRefreshLayout refreshLayout;
    RecyclerView.Adapter adapter;
    private LoadingDots loadingPertanyaan;

    private String judul[] = {"What should I do whe this happen?", "How to gain inspiration?", "How to else?"};
    private String deskripsi[] = {"I do this everyday, but somehow...", "When it happens, I don't know what to do. I need inspiration.", "bla bla bla..."};

    private Image gambar[][];
    private int kategoriSoal[]= {1, 2, 0, 1};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_harus_dijawab, container, false);
        loadingPertanyaan = (LoadingDots)view.findViewById(R.id.loading_pertanyaan);
        daftarTanya= view.findViewById(R.id.daftar_pertanyaan_wadah_expert);
        refreshLayout = view.findViewById(R.id.refresh_pertanyaan);
        loadDaftarPertanyaan();
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.abuTua), getResources().getColor(R.color.abuLebihTua),
                getResources().getColor(R.color.abuSangatTua));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
       // tmbTambah= view.findViewById(R.id.daftar_pertanyaan_tambah);
       /* tmbTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isStoragePermissionGranted(getActivity())) {
                    Toast.makeText(getActivity(), "sudah", Toast.LENGTH_SHORT).show();
                    Intent inten = new Intent(getContext(), TambahPertanyaanWkr.class);
                    inten.putExtra("idHalaman", R.layout.activity_tambah_pertanyaan_wkr);
                    startActivity(inten);
                } else {
                    Toast.makeText(getActivity(), "belum", Toast.LENGTH_SHORT).show();
                }
            }}); */

/*
        tmbTambah = view.findViewById(R.id.daftar_pertanyaan_tambah);
        tmbTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(getContext(), TambahJawabanExprt.class);
                inten.putExtra("idHalaman", R.layout.activity_tambah_jawaban_exprt);
                startActivity(inten);
            }
        });

*/
        return view;
    }

    void loadDaftarPertanyaan(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        daftarTanya.setLayoutManager(linearLayoutManager);
        daftarTanya.setLayoutManager(new LinearLayoutManager(getActivity()));
        Masalah = new ArrayList<>();
        adapter = new RC_Masalah(Masalah, getActivity());
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.CARI_BIDANG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Masalah.clear();
                            JSONArray jsonArr = new JSONArray(response);
                            //  Toast.makeText(getActivity(), "Berhasil loading!", Toast.LENGTH_SHORT).show();
                            for(int i=0; i<jsonArr.length(); i++){
                                try {
                                    JSONObject jsonObject = jsonArr.getJSONObject(i);
                                    if(jsonObject.getInt("answered_status")==1)
                                        continue;
                                    else{
                                        Permasalahan masalah = new Permasalahan();
                                        masalah.setproblem_desc(jsonObject.getString("problem_desc"));
                                        masalah.setproblem_title(jsonObject.getString("problem_title"));
                                        masalah.setproblem_owner(jsonObject.getString("problem_owner"));
                                        masalah.setStatus(jsonObject.getInt("status"));
                                        masalah.setpid(jsonObject.getString("pid"));
                                        masalah.setmajority_id(jsonObject.getString("majority_id"));
                                        masalah.setTimestamp(jsonObject.getString("timestamp"));
                                        Masalah.add(masalah);
                                    }
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
               // if(getActivity()!=null)
              //  Toast.makeText(getActivity(), "Terjadi kesalahan jaringan!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> masalah = new HashMap<>();
                masalah.put("majority", Utilities.getUserMajor(getActivity()));
                return masalah;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDaftarPertanyaan();
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
            return new vH(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_daftar_ditanyakan, parent, false));
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
            public vH(View itemView) {
                super(itemView);
                view = itemView;
                centang = (ImageView) itemView.findViewById(R.id.daftar_pertanyaan_centang);
                judul = (TextView)itemView.findViewById(R.id.daftar_pertanyaan_judul);
                isi = (TextView)itemView.findViewById(R.id.daftar_pertanyaan_deskripsi);
                foto = (ImageView)itemView.findViewById(R.id.daftar_pertanyaan_gambar);
            }
            public void bind(final int posisi){
                if(masalah.get(posisi).getStatus()==PENGGUNA_EXPERT){
                    centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);
                }else if(masalah.get(posisi).getStatus()==PENGGUNA_BIASA){
                    centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
                }
                judul.setText(masalah.get(posisi).getproblem_title());
                isi.setText(masalah.get(posisi).getproblem_desc());
                String[] judulIsi = {masalah.get(posisi).getproblem_title(), masalah.get(posisi).getproblem_desc()};
                new AsyncTask<String[], Void, String[]>(){

                    @Override
                    protected String[] doInBackground(String[]... strings) {
                        String[] output = strings[0];
                        for(int i=0;i<output.length;i++){
                            if(getActivity()!=null)
                                output[i] = Utilities.ubahBahasaDariId(output[i], Utilities.getUserBahasa(getActivity()), getActivity());
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
               // Toast.makeText(act, masalah.get(posisi).getpid(), Toast.LENGTH_SHORT).show();
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
                        Intent inten= new Intent(getContext(), DetailPertanyaanActivityWkr.class);
                        inten.putExtra("paket_detail_pertanyaan", paketDetailPetanyaan);
                        startActivity(inten);
                    }
                });
            }
        }
    }
}
