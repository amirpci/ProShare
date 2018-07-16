package sidev17.siits.proshare.Modul.Worker.Tab;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import sidev17.siits.proshare.Modul.Worker.DetailPertanyaanActivityWkr;
import sidev17.siits.proshare.Modul.Worker.MainActivityWkr;
import sidev17.siits.proshare.Modul.Worker.TambahPertanyaanWkr;
import sidev17.siits.proshare.R;


/**
 * Created by USER on 02/05/2018.
 */

public class DaftarTanyaActWkr extends Fragment {

    private ListView daftarTanya;
    private ImageView tmbTambah;

    String judul[]= {"What should I do whe this happen?", "How to gain inspiration?", "How to else?"};
    String deskripsi[]= {"I do this everyday, but somehow...", "When it happens, I don't know what to do. I need inspiration.", "bla bla bla..."};
    Image gambar[][];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_daftar_tanya_wkr, container, false);

        daftarTanya= view.findViewById(R.id.daftar_pertanyaan_wadah);
        BaseAdapterPertanyaan adpTanya= new BaseAdapterPertanyaan();
        daftarTanya.setAdapter(adpTanya);

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
        });

        tmbTambah= view.findViewById(R.id.daftar_pertanyaan_tambah);
        tmbTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten= new Intent(getContext(), TambahPertanyaanWkr.class);
                startActivity(inten);
            }
        });

        return view;
    }
    private class BaseAdapterPertanyaan extends BaseAdapter {

        BaseAdapterPertanyaan(){
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

            teksJudul.setText(judul[position]);
            teksDesc.setText(deskripsi[position]);

            return view;
        }
    }

}
