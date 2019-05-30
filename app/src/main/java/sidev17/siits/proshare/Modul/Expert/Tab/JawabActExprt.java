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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eyalbira.loadingdots.LoadingDots;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Model.Permasalahan;
import sidev17.siits.proshare.Modul.Expert.MainActivityExprt;
import sidev17.siits.proshare.Modul.Expert.TambahJawabanExprt;
import sidev17.siits.proshare.Modul.Expert.TambahReviewExprt;
import sidev17.siits.proshare.Modul.Worker.MainActivityWkr;
import sidev17.siits.proshare.Modul.Worker.TambahPertanyaanWkr;
import sidev17.siits.proshare.Utils.PackBahasa;
import sidev17.siits.proshare.Utils.Terjemahan;
import sidev17.siits.proshare.Utils.ViewTool.Fragment_Header;
import sidev17.siits.proshare.Utils.ViewTool.MainAct_Header;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Utilities;

import static sidev17.siits.proshare.Modul.Worker.TambahPertanyaanWkr.JENIS_POST_JAWAB;
import static sidev17.siits.proshare.Modul.Worker.TambahPertanyaanWkr.JENIS_POST_REVIEW;

public class JawabActExprt extends Fragment_Header {
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

    private View halamanKosong;

    // ini untuk batas antara share dan pertanyaan
    private int batas = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_harus_dijawab, container, false);
        loadingPertanyaan = (LoadingDots)view.findViewById(R.id.loading_pertanyaan);
        daftarTanya= view.findViewById(R.id.daftar_pertanyaan_wadah_expert);
        refreshLayout = view.findViewById(R.id.refresh_pertanyaan);
        initHalamanKosong(view);
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
//        initHeader();
        keHalamanAwal();
        judulHeader= "Pertnyaan yang Harus Dijawab";
        return view;
    }

    private void initHalamanKosong(View v) {
        halamanKosong = v.findViewById(R.id.daftar_pertanyaan_kosong);
        halamanKosong.setVisibility(View.GONE);
    }

    @Override
    public void initHeader() {
//        MainAct_Header mainAct= (MainAct_Header) actInduk;
//        actInduk.aturJudulHeader("Pertnyaan yang Harus Dijawab");
        actInduk.daftarkanAksiGantiHalaman(new MainAct_Header.AksiGantiHalaman() {
                    @Override
                    public void gantiHalaman(int posisi) {
                        if(posisi != 1)
                            actInduk.aturTambahanHeader("");
                        else
                            actInduk.aturTambahanHeader("(" +adapter.getItemCount() +")");
                    }
                });
        actInduk.aturGambarOpsiHeader_Null(0);
//        int resId[]= {};
    }
    private void keHalamanAwal(){
        MainActivityExprt mainAct= (MainActivityExprt) getActivity();
        mainAct.keHalaman(1);
    }
/*
    @Override
    public void onStop() {
        MainAct_Header mainAct= (MainAct_Header) getActivity();
        mainAct.bolehInitHeader= false;
        super.onStop();
    }
*/
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
        RC_Masalah rc =  ((RC_Masalah)adapter);
        rc.gantiJenisState();
        rc.resetTerbuka();
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
                                    Permasalahan masalah = new Permasalahan();
                                    if(jsonObject.getInt("status_post") != 12) {
                                        masalah.setproblem_desc(jsonObject.getString("problem_desc"));
                                        masalah.setproblem_title(jsonObject.getString("problem_title"));
                                        masalah.setproblem_owner(jsonObject.getString("problem_owner"));
                                        masalah.setStatus(jsonObject.getInt("status"));
                                        masalah.setStatuspost(jsonObject.getInt("status_post"));
                                        masalah.setpid(jsonObject.getString("pid"));
                                        masalah.setmajority_id(jsonObject.getString("majority_id"));
                                        masalah.setTimestamp(jsonObject.getString("timestamp"));
                                    } else{
                                        batas = i;
                                        masalah.setStatuspost(jsonObject.getInt("status_post"));
                                    }

                                    Masalah.add(masalah);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.d("jumalh masalah", String.valueOf(Masalah.size()));
                            if(adapter instanceof RC_Masalah){
                                ((RC_Masalah)adapter).gantiJenisState();
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
                masalah.put("id_orang", Utilities.getUserID(getActivity()));
                return masalah;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    public class RC_Masalah extends RecyclerView.Adapter<RC_Masalah.vH>{
        private List<Permasalahan> masalah;
        private final int VIEW_BATAS_JAWAB = 111;
        private final int VIEW_BATAS_SHARE = 222;
        private final int VIEW_NORMAL = 333;
        private final int VIEW_TIDAK_ADA = 444;
        private final int JENIS_TIDAK_ADA = 239;
        private final int JENIS_HANYA_JAWAB = 392;
        private final int JENIS_HANYA_SHARE = 932;
        private final int JENIS_NORMAL = 923;

        private int jenisState;
        private int jumlahPertanyaan = 0;
        private int jumlahShare = 0;

        private boolean shareTebuka = false;
        private boolean pertanyaanTerbuka = false;

        Activity act;

        public RC_Masalah(List<Permasalahan> masalah, Activity act) {
            this.masalah = masalah;
            this.act = act;
            Log.d("masalah size ", String.valueOf(masalah.size()));
            if(masalah.size() <= 1)
                jenisState = JENIS_TIDAK_ADA;
            else if (batas == 0)
                jenisState = JENIS_HANYA_SHARE;
            else if (batas == masalah.size() - 1)
                jenisState = JENIS_HANYA_JAWAB;
            else
                jenisState = JENIS_NORMAL;
        }

        public void resetTerbuka(){
            pertanyaanTerbuka = false;
            shareTebuka = false;
        }

        public void gantiJenisState(){
            Log.d("batas sekarang", String.valueOf(batas));
            if(Masalah.size() <= 1)
                jenisState = JENIS_TIDAK_ADA;
            else if (batas == 0)
                jenisState = JENIS_HANYA_SHARE;
            else if (batas == Masalah.size() - 1)
                jenisState = JENIS_HANYA_JAWAB;
            else
                jenisState = JENIS_NORMAL;
        }

        @NonNull
        @Override
        public vH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType == VIEW_NORMAL) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_daftar_pertanyaan_pribadi, parent, false);
                return new vH(v);
            }else if(viewType == VIEW_BATAS_JAWAB){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_pembatas_list, parent, false);
                TextView txt = v.findViewById(R.id.pembatas_teks);
                txt.setText(PackBahasa.harusJawab[Terjemahan.indexBahasa(act)][0] + " ("+ String.valueOf(jumlahPertanyaan)+")");
                return new vH(v);
            } else if(viewType == VIEW_BATAS_SHARE) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_pembatas_list, parent, false);
                TextView txt = v.findViewById(R.id.pembatas_teks);
                txt.setText(PackBahasa.harusJawab[Terjemahan.indexBahasa(act)][1] + " ("+ String.valueOf(jumlahShare)+")");
                return new vH(v);
            }
            return new vH(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_empty, parent, false));
        }

        @Override
        public int getItemViewType(int position) {
            Log.d("jenis View get", String.valueOf(jenisState));
            if(jenisState == JENIS_NORMAL){
                if(pertanyaanTerbuka){
                    if(position == 0)
                        return VIEW_BATAS_JAWAB;
                    else if (position == batas + 1)
                        return VIEW_BATAS_SHARE;
                    else return VIEW_NORMAL;
                }else if(shareTebuka) {
                    if (position == 0)
                        return VIEW_BATAS_JAWAB;
                    else if (position == 1)
                        return VIEW_BATAS_SHARE;
                    else return VIEW_NORMAL;
                } else{
                    if (position == 0)
                        return VIEW_BATAS_JAWAB;
                    else if (position == 1)
                        return VIEW_BATAS_SHARE;
                }
            } else if (jenisState == JENIS_HANYA_JAWAB){
                if(position == 0)
                    return VIEW_BATAS_JAWAB;
                else
                    return VIEW_NORMAL;
            } else if (jenisState == JENIS_HANYA_SHARE){
                if(position == 0)
                    return VIEW_BATAS_SHARE;
                else
                    return VIEW_NORMAL;
            } else if (jenisState == JENIS_TIDAK_ADA){
                return  VIEW_TIDAK_ADA;
            }
            return VIEW_NORMAL;
        }

        @Override
        public void onBindViewHolder(@NonNull vH holder, int position) {
            if(jenisState == JENIS_NORMAL){
                if(pertanyaanTerbuka && shareTebuka) {
                    if (position > 0 && position < batas + 1)
                        holder.bind(position - 1);
                    else if (position > batas + 1)
                        holder.bind(position - 1);
                    else if (position == 0)
                        holder.bindPembatas(0);
                    else if (position == batas + 1)
                        holder.bindPembatas(1);
                } else if(!pertanyaanTerbuka && shareTebuka){
                    if (position > 1)
                        holder.bind(batas + position - 1);
                    else if (position == 0)
                        holder.bindPembatas(0);
                    else if (position == 1)
                        holder.bindPembatas(1);
                } else if(pertanyaanTerbuka && !shareTebuka){
                    if (position > 0 && position < batas + 1)
                        holder.bind(position - 1);
                    else if (position == 0)
                        holder.bindPembatas(0);
                    else if (position == batas + 1)
                        holder.bindPembatas(1);
                } else{
                    if (position == 0)
                        holder.bindPembatas(0);
                    else if (position == 1)
                        holder.bindPembatas(1);
                }
            } else if (jenisState == JENIS_HANYA_SHARE || jenisState == JENIS_HANYA_JAWAB){
              //  Toast.makeText(act, "udah masuk", Toast.LENGTH_SHORT).show();
                if(position > 0) {
                    boolean terbuka = (jenisState == JENIS_HANYA_SHARE)?shareTebuka:pertanyaanTerbuka;
                    int pengurang = (jenisState == JENIS_HANYA_SHARE)?0:1;
                    holder.bind(position - pengurang);
                  //  Toast.makeText(act, "lebih dari 1", Toast.LENGTH_SHORT).show();
                } else {
                   // Toast.makeText(act, "udah masuk 2", Toast.LENGTH_SHORT).show();
                    holder.bindPembatas((jenisState == JENIS_HANYA_SHARE) ? 1 : 0);
                }
            }
        }

        @Override
        public int getItemCount() {
            int jumlahitem = (masalah.size()>0)?masalah.size()-1:0;
            if (actInduk.halamanSekarang() == 1)
                actInduk.aturTambahanHeader("(" + jumlahitem + ")");
            Log.d("jenis State", Integer.toString(jenisState));
            if (jenisState == JENIS_TIDAK_ADA) {
              //  Log.d("masalah jumlah", String.valueOf(masalah.size()));
              //  Log.d("jenis view", "tidak ada");
                halamanKosong.setVisibility(View.VISIBLE);
                return 0;
            } else if (jenisState == JENIS_HANYA_SHARE || jenisState == JENIS_HANYA_JAWAB) {
             //   Log.d("jenis view", "jenis hanya jawab");
                jumlahPertanyaan = masalah.size() - 1;
                jumlahShare = masalah.size() - 1;
                halamanKosong.setVisibility(View.GONE);
                if(jenisState == JENIS_HANYA_SHARE){
                    if(!shareTebuka)
                        return 1;
                    else
                        return masalah.size();
                } else {
                   // Log.d("pertanyaan oke", "ok");
                    if(!pertanyaanTerbuka)
                        return 1;
                    else
                        return masalah.size();
                }
            } else if (jenisState == JENIS_NORMAL){
                halamanKosong.setVisibility(View.GONE);
                jumlahPertanyaan = batas;
                jumlahShare = masalah.size() - jumlahPertanyaan - 1;
                if(pertanyaanTerbuka && shareTebuka)
                    return masalah.size() + 1;
                else if(!pertanyaanTerbuka && shareTebuka)
                    return masalah.size() + 1 - jumlahPertanyaan;
                else if(pertanyaanTerbuka && !shareTebuka)
                    return masalah.size() + 1 - jumlahShare;
                else
                    return 2;
             }
            return masalah.size();
        }

        public class vH extends RecyclerView.ViewHolder{
            private TextView judul, isi;
            private ImageView centang, foto;
            private View view;
            public vH(View itemView) {
                super(itemView);
                view = itemView;
                //view.findViewById(R.id.opsi_pertanyaan).setVisibility(View.GONE);
                centang = (ImageView) itemView.findViewById(R.id.daftar_pertanyaan_centang);
                judul = (TextView)itemView.findViewById(R.id.daftar_pertanyaan_judul);
                isi = (TextView)itemView.findViewById(R.id.daftar_pertanyaan_deskripsi);
                foto = (ImageView)itemView.findViewById(R.id.daftar_pertanyaan_gambar);
            }

            public void openClosePertanyaan(){
               // Toast.makeText(act, pertanyaanTerbuka?"Pertanyaan sedang terbuka":"Pertanyaan sedang tertutup", Toast.LENGTH_SHORT).show();
                pertanyaanTerbuka = !pertanyaanTerbuka;
                notifyDataSetChanged();
            }

            public void openCloseShare(){
                shareTebuka = !shareTebuka;
                notifyDataSetChanged();
            }

            public void bindPembatas(int posisi){
                if(posisi == 0)
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openClosePertanyaan();
                        }
                    });
                else
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openCloseShare();
                        }
                    });
            }

            public void bind(final int posisi){
                view.setVisibility(View.VISIBLE);
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
                        if(Utilities.isStoragePermissionGranted(getActivity())) {
                            Bundle paketDetailPetanyaan = new Bundle();
                            paketDetailPetanyaan.putString("judul_pertanyaan", masalah.get(posisi).getproblem_title());
                            paketDetailPetanyaan.putString("deskripsi_pertanyaan", masalah.get(posisi).getproblem_desc());
                            paketDetailPetanyaan.putString("owner", masalah.get(posisi).getproblem_owner());
                            paketDetailPetanyaan.putString("waktu", masalah.get(posisi).getTimestamp());
                            paketDetailPetanyaan.putString("pid", masalah.get(posisi).getpid());
                            paketDetailPetanyaan.putString("majority", masalah.get(posisi).getmajority_id());
                            int jenis = (posisi<=batas) ? JENIS_POST_JAWAB:JENIS_POST_REVIEW;
                            Intent inten = new Intent(getContext(), (masalah.get(posisi).getStatuspost() == TambahPertanyaanWkr.JENIS_POST_SHARE)?TambahReviewExprt.class:TambahJawabanExprt.class);
//                            Intent inten = new Intent(getContext(), TambahReviewExprt.class);
                            inten.putExtra("paket_detail_pertanyaan", paketDetailPetanyaan);
                            inten.putExtra("jenisPost", 12);
                            startActivity(inten);
                        }
                    }
                });
            }
        }
    }


}
