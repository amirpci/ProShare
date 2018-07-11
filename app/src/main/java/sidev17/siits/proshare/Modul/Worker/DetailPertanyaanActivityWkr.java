package sidev17.siits.proshare.Modul.Worker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import sidev17.siits.proshare.Adapter.BaseAdapterPertanyaan;
import sidev17.siits.proshare.R;

public class DetailPertanyaanActivityWkr extends AppCompatActivity {

    String orang[]= {"Mr. A", "Mr. B", "Mrs. C"};
    String job[]= {"Psychiatrist", "Motivator", "Engineer"};

    String judul[]= {"What should I do whe this happen?", "How to gain inspiration?", "How to else?"};
    String desc[]= {"I do this everyday, but somehow...", "When it happens, I don't know what to do. I need inspiration.", "bla bla bla..."};

    LinearLayout viewPertanyaan;
    ListView viewSolusi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pertanyaan);

        View viewTanyaBaru= getLayoutInflater().inflate(R.layout.model_timeline_pertanyaan, null);
        viewPertanyaan= findViewById(R.id.detail_wadah_pertanyaan);
        viewPertanyaan.addView(viewTanyaBaru);

        viewSolusi= findViewById(R.id.detail_wadah_solusi);
        BaseAdapterPertanyaan adpSolusi= new BaseAdapterPertanyaan(R.layout.model_kolom_komentar, R.id.komentar_orang_nama, R.id.komentar_orang_job,
                orang, job);
        viewSolusi.setAdapter(adpSolusi);

    }
}
