package sidev17.siits.proshare.Modul.Expert.Tab;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import sidev17.siits.proshare.Modul.Expert.TambahJawabanExprt;
import sidev17.siits.proshare.Modul.Worker.DetailPertanyaanActivityWkr;
import sidev17.siits.proshare.Modul.Worker.Tab.DaftarTanyaActWkr;
import sidev17.siits.proshare.Modul.Worker.TambahPertanyaanWkr;
import sidev17.siits.proshare.R;

public class JawabActExprt extends Fragment {
    public final int PENGGUNA_EXPERT_TERVERIFIKASI= 2;
    public final int PENGGUNA_EXPERT= 1;
    public final int PENGGUNA_BIASA= 0;

    private ListView daftarTanya;
    private ImageView tmbTambah;

    private String judul[] = {"What should I do whe this happen?", "How to gain inspiration?", "How to else?"};
    private String deskripsi[] = {"I do this everyday, but somehow...", "When it happens, I don't know what to do. I need inspiration.", "bla bla bla..."};

    private Image gambar[][];
    private int kategoriSoal[]= {1, 2, 0, 1};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_harus_dijawab, container, false);

        daftarTanya = view.findViewById(R.id.daftar_pertanyaan_wadah);
        AdapterDaftarPertanyaan adpTanya = new AdapterDaftarPertanyaan();
        daftarTanya.setAdapter(adpTanya);

        daftarTanya.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle paketDetailPetanyaan = new Bundle();
                paketDetailPetanyaan.putString("judul_pertanyaan", judul[position]);
                paketDetailPetanyaan.putString("deskripsi_pertanyaan", deskripsi[position]);
                Intent inten = new Intent(getContext(), DetailPertanyaanActivityWkr.class);
                inten.putExtra("paket_detail_pertanyaan", paketDetailPetanyaan);
                startActivity(inten);
            }
        });

        tmbTambah = view.findViewById(R.id.daftar_pertanyaan_tambah);
        tmbTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(getContext(), TambahJawabanExprt.class);
                inten.putExtra("idHalaman", R.layout.activity_tambah_jawaban_exprt);
                startActivity(inten);
            }
        });
        return view;
    }

    private class AdapterDaftarPertanyaan extends BaseAdapter {

        AdapterDaftarPertanyaan() {
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
            View view = getLayoutInflater().inflate(R.layout.model_daftar_pertanyaan, parent, false);

            TextView teksJudul = view.findViewById(R.id.daftar_pertanyaan_judul);
            TextView teksDesc = view.findViewById(R.id.daftar_pertanyaan_deskripsi);
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
}
