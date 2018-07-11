package sidev17.siits.proshare.Modul.Worker.Tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import sidev17.siits.proshare.Adapter.BaseAdapterPertanyaan;
import sidev17.siits.proshare.Modul.Worker.DetailPertanyaanActivityWkr;
import sidev17.siits.proshare.Modul.Worker.MainActivityWkr;
import sidev17.siits.proshare.R;


/**
 * Created by USER on 02/05/2018.
 */

public class DaftarTanyaActWkr extends Fragment {

    ListView daftarTanya;

    String judul[]= {"What should I do whe this happen?", "How to gain inspiration?", "How to else?"};
    String desc[]= {"I do this everyday, but somehow...", "When it happens, I don't know what to do. I need inspiration.", "bla bla bla..."};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_daftar_tanya_wkr, container, false);

        daftarTanya= view.findViewById(R.id.daftar_pertanyaan_wadah);
        BaseAdapterPertanyaan adpTanya= new BaseAdapterPertanyaan(R.layout.model_daftar_pertanyaan, R.id.tambah_judul, R.id.tambah_deskripsi,
                judul, desc);
        daftarTanya.setAdapter(adpTanya);

        daftarTanya.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent inten= new Intent(getContext(), DetailPertanyaanActivityWkr.class);
                startActivity(inten);
            }
        });

        return view;
    }
}
