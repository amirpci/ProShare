package sidev17.siits.proshare.Modul.Worker.Tab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.GetChars;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Model.Permasalahan;
import sidev17.siits.proshare.Modul.Worker.DetailPertanyaanActivityWkr;

import sidev17.siits.proshare.Modul.Worker.MainActivityWkr;
import sidev17.siits.proshare.Modul.Worker.TambahPertanyaanWkr;

import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Utilities;

import static com.android.volley.Request.*;


/**
 * Created by USER on 02/05/2018.
 */

public class DaftarTanyaActWkr extends Fragment {
    public final int PENGGUNA_EXPERT_TERVERIFIKASI= 402;
    public final int PENGGUNA_EXPERT= 401;
    public final int PENGGUNA_BIASA= 400;

    List<Permasalahan> Masalah;
    private RecyclerView daftarTanya;
    private ImageView tmbTambah;
    RecyclerView.Adapter adapter;

    private String Judul[]= {"What should I do whe this happen?", "How to gain inspiration?", "How to else?"};
    private String deskripsi[]= {"I do this everyday, but somehow...", "When it happens, I don't know what to do. I need inspiration.", "bla bla bla..."};

    private Image gambar[][];
    private int kategoriSoal[]= {1, 2, 1, 0};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_daftar_tanya_wkr, container, false);

        daftarTanya= view.findViewById(R.id.daftar_pertanyaan_wadah);
        //AdapterDaftarPertanyaan adpTanya= new AdapterDaftarPertanyaan();
        loadDaftarPertanyaan();
        /*daftarTanya.setAdapter(adpTanya);

        daftarTanya.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle paketDetailPetanyaan= new Bundle();
                paketDetailPetanyaan.putString("judul_pertanyaan", judul[position]);
                paketDetailPetanyaan.putString("deskripsi_pertanyaan", deskripsi[position]);
                Intent inten= new Intent(getContext(), DetailPertanyaanActivityWkr.class);
                inten.putExtra("paket_detail_pertanyaan", paketDetailPetanyaan);
                startActivity(inten);
            }
        }); */
        tmbTambah= view.findViewById(R.id.daftar_pertanyaan_tambah);
        tmbTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utilities.isStoragePermissionGranted(getActivity())){
                    Toast.makeText(getActivity(), "sudah", Toast.LENGTH_SHORT).show();
                    Intent inten= new Intent(getContext(), TambahPertanyaanWkr.class);
                    inten.putExtra("idHalaman", R.layout.activity_tambah_pertanyaan_wkr);
                    startActivity(inten);
                }else {
                    Toast.makeText(getActivity(), "belum", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    private void loadData() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Memuat Data");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Method.POST, Konstanta.PERTANYAAN_SAYA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Masalah.clear();
                            JSONArray jsonArr = new JSONArray(response);
                            Toast.makeText(getActivity(), "Berhasil loading!", Toast.LENGTH_SHORT).show();
                            for(int i=0; i<jsonArr.length(); i++){
                                try {
                                    JSONObject jsonObject = jsonArr.getJSONObject(i);
                                    Permasalahan masalah = new Permasalahan();
                                    masalah.setproblem_desc(jsonObject.getString("problem_desc"));
                                    masalah.setproblem_title(jsonObject.getString("problem_title"));
                                    masalah.setStatus(jsonObject.getInt("status"));
                                    Masalah.add(masalah);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    dialog.dismiss();
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Terjadi kesalahan jaringan!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> masalah = new HashMap<>();
                masalah.put("problem_owner", Utilities.getUserID(getActivity()).replace(",","."));
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
            private ImageView centang;
            private View view;
            public vH(View itemView) {
                super(itemView);
                view = itemView;
                centang = (ImageView) itemView.findViewById(R.id.daftar_pertanyaan_centang);
                judul = (TextView)itemView.findViewById(R.id.daftar_pertanyaan_judul);
                isi = (TextView)itemView.findViewById(R.id.daftar_pertanyaan_deskripsi);
            }
            public void bind(final int posisi){
                if(masalah.get(posisi).getStatus()==PENGGUNA_EXPERT){
                    centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);
                }else if(masalah.get(posisi).getStatus()==PENGGUNA_BIASA){
                    centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
                }
                judul.setText(masalah.get(posisi).getproblem_title());
                isi.setText(masalah.get(posisi).getproblem_desc());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle paketDetailPetanyaan= new Bundle();
                        paketDetailPetanyaan.putString("judul_pertanyaan", masalah.get(posisi).getproblem_title());
                        paketDetailPetanyaan.putString("deskripsi_pertanyaan", masalah.get(posisi).getproblem_desc());
                        Intent inten= new Intent(getContext(), DetailPertanyaanActivityWkr.class);
                        inten.putExtra("paket_detail_pertanyaan", paketDetailPetanyaan);
                        startActivity(inten);
                    }
                });
            }
        }
    }
   /* private class AdapterDaftarPertanyaan extends BaseAdapter {

        AdapterDaftarPertanyaan(){
        }

        @Override
        public int getCount() {
            return judul.length;
        }

        @Override
        public Object getItem(int position) {
            return judul[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view= getLayoutInflater().inflate(R.layout.model_daftar_pertanyaan, parent, false);

            TextView teksJudul= view.findViewById( R.id.daftar_pertanyaan_judul);
            TextView teksDesc= view.findViewById(R.id.daftar_pertanyaan_deskripsi);
            ImageView centang= view.findViewById(R.id.daftar_pertanyaan_centang);

            teksJudul.setText(judul[position]);
            teksDesc.setText(deskripsi[position]);

            if(kategoriSoal[position]== PENGGUNA_EXPERT)
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
            else if(kategoriSoal[position]== PENGGUNA_EXPERT_TERVERIFIKASI)
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);

            return view;
        }
    }
*/
}
