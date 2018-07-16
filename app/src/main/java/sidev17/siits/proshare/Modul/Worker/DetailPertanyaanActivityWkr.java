package sidev17.siits.proshare.Modul.Worker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import sidev17.siits.proshare.Adapter.BaseAdapterPertanyaan;
import sidev17.siits.proshare.R;

public class DetailPertanyaanActivityWkr extends AppCompatActivity {
    public final int PENGGUNA_EXPERT_TERVERIFIKASI= 2;
    public final int PENGGUNA_EXPERT= 1;
    public final int PENGGUNA_BIASA= 0;

    private String judulPertanyaan;
    private String deskripsiPertanyaan;

    private String orang[]= {"Mr. A", "Mr. B", "Mrs. C", "Will Smith"};
    private String job[]= {"Psychiatrist", "Motivator", "Engineer", "Actor"};
    private int kategoriExpert[]= {2, 1, 0, 2};

    private String judulSolusi[]= {"What should I do whe this happen?", "How to gain inspiration?", "How to else?", "Stay cool"};
    private String descSolusi[]= {"I do this everyday, but somehow...", "When it happens, I don't know what to do. I need inspiration.", "bla bla bla...", "Just bel cool bro!"};

    private ListView detailPrtanyaan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pertanyaan);

        Bundle paketDetailPertanyaan= getIntent().getBundleExtra("paket_detail_pertanyaan");

        judulPertanyaan= paketDetailPertanyaan.getString("judul_pertanyaan");
        deskripsiPertanyaan= paketDetailPertanyaan.getString("deskripsi_pertanyaan");

        detailPrtanyaan= (ListView) findViewById(R.id.detail_pertanyaan);
        DetailPertanyaanAdapter adpSolusi= new DetailPertanyaanAdapter();
        detailPrtanyaan.setAdapter(adpSolusi);
    }

    class DetailPertanyaanAdapter extends BaseAdapter{
/*
        String nama[];
        String job[];
        String pp[];

        String solusi[];
        String tgl[];
*/
        DetailPertanyaanAdapter (){
        }

        @Override
        public int getCount() {
            return orang.length +1;
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
            if(position > 0){
                view= getLayoutInflater().inflate(R.layout.model_kolom_komentar, parent, false);

                TextView teksOrang= view.findViewById(R.id.komentar_orang_nama);
                TextView teksJob= view.findViewById(R.id.komentar_orang_job);
                TextView teksSolusi= view.findViewById(R.id.komentar_deskripsi);
                ImageView centang= view.findViewById(R.id.komentar_orang_centang);

                teksOrang.setText(orang[position-1]);
                teksJob.setText(job[position-1]);
                teksSolusi.setText(descSolusi[position-1]);

                if(kategoriExpert[position-1]== PENGGUNA_EXPERT)
                    centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
                else if(kategoriExpert[position-1]== PENGGUNA_EXPERT_TERVERIFIKASI)
                    centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);
            }
            else{
                view= getLayoutInflater().inflate(R.layout.model_timeline_pertanyaan, parent, false);
                TextView teksJudul= view.findViewById(R.id.tl_judul);
                TextView teksDeskripsi= view.findViewById(R.id.tl_deskripsi);

                teksJudul.setText(judulPertanyaan);
                teksDeskripsi.setText(deskripsiPertanyaan);
            }
            return view;
        }
    }
}
