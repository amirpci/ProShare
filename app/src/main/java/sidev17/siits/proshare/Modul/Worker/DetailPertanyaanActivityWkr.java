package sidev17.siits.proshare.Modul.Worker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import sidev17.siits.proshare.Adapter.BaseAdapterPertanyaan;
import sidev17.siits.proshare.R;

public class DetailPertanyaanActivityWkr extends AppCompatActivity {

    String orang[]= {"Mr. A", "Mr. B", "Mrs. C"};
    String job[]= {"Psychiatrist", "Motivator", "Engineer"};

    String judul[]= {"What should I do whe this happen?", "How to gain inspiration?", "How to else?"};
    String desc[]= {"I do this everyday, but somehow...", "When it happens, I don't know what to do. I need inspiration.", "bla bla bla..."};

    ListView detailPrtanyaan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pertanyaan);

        detailPrtanyaan= (ListView) findViewById(R.id.detail_pertanyaan);
        DetailPertanyaanAdapter adpSolusi= new DetailPertanyaanAdapter(orang, job, desc);
        detailPrtanyaan.setAdapter(adpSolusi);

    }

    class DetailPertanyaanAdapter extends BaseAdapter{

        String nama[];
        String job[];
        String pp[];

        String solusi[];
        String tgl[];

        DetailPertanyaanAdapter (String nama[], String job[], String solusi[]){
            this.nama= nama;
            this.job= job;
            this.solusi= solusi;
        }

        @Override
        public int getCount() {
            return nama.length +1;
        }

        @Override
        public Object getItem(int position) {
            return nama[position];
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

                TextView teksJudul= view.findViewById(R.id.komentar_orang_nama);
                TextView teksJob= view.findViewById(R.id.komentar_orang_job);
                TextView teksSolusi= view.findViewById(R.id.komentar_deskripsi);

                teksJudul.setText(judul[position-1]);
                teksJob.setText(job[position-1]);
                teksSolusi.setText(solusi[position-1]);
            }
            else{
                view= getLayoutInflater().inflate(R.layout.model_timeline_pertanyaan, parent, false);
            }
            return view;
        }
    }
}
