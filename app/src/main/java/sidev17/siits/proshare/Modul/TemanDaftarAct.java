package sidev17.siits.proshare.Modul;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import de.hdodenhof.circleimageview.CircleImageView;
import sidev17.siits.proshare.Model.Pengguna;
import sidev17.siits.proshare.Modul.Expert.Tab.FeedbackActExprt;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Array;
import sidev17.siits.proshare.Utils.ViewTool.BitmapHandler;

public class TemanDaftarAct extends AppCompatActivity {

    private TextView vJmlTeman;

    private ImageView vCariTeman;
    private ImageView vTambahTeman;

    private ListView vDaftarTeman;

    private Array<String> namaTeman= new Array<>();
    private Array<String> bidangTeman= new Array<>();
    private Array<BitmapHandler> fotoTeman= new Array<>();
    private Array<Integer> statusTeman= new Array<>(); //Worker, Expert, Verified Expert

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teman_daftar);

        vJmlTeman = findViewById(R.id.teman_jml);
        vCariTeman = findViewById(R.id.teman_cari);
        vCariTeman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        vTambahTeman = findViewById(R.id.teman_tambah);
        vTambahTeman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keTambahTeman= new Intent(TemanDaftarAct.this, TemanTambahAct.class);

                keTambahTeman.putExtra("idUser", -1 /*MSH DUMY!!!*/);
                startActivity(keTambahTeman);
            }
        });
        vDaftarTeman = findViewById(R.id.teman_daftar);

        ambilData();

        AdapterDaftarTeman adp= new AdapterDaftarTeman();
        vDaftarTeman.setAdapter(adp);
    }

    //msh dumy
    private void ambilData(){
        String nama[]= {"Mr. Joko", "Alimun", "Xanderson"};
        String bidang[]= {"Construction", "Philosopher", "Psychiatrist"};
        int status[]= {Pengguna.Status.PENGGUNA_BIASA, Pengguna.Status.PENGGUNA_EXPERT_TERVERIFIKASI,
                Pengguna.Status.PENGGUNA_EXPERT};

        namaTeman.tambah(nama);
        bidangTeman.tambah(bidang);
        statusTeman.dariArrayPrimitif(status);
    }

    private class AdapterDaftarTeman extends BaseAdapter{
        @Override
        public int getCount() {
            return namaTeman.ukuran();
        }

        @Override
        public Object getItem(int position) {
            return namaTeman.ambil(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View viewKolom= getLayoutInflater().inflate(R.layout.model_kolom_chat, null);

            TextView vNamaTeman= viewKolom.findViewById(R.id.feedback_orang_nama);
            TextView vBidangTeman= viewKolom.findViewById(R.id.feedback_orang_chat);
            CircleImageView vFotoTeman= viewKolom.findViewById(R.id.tl_orang_gambar);
            ImageView vStatus= viewKolom.findViewById(R.id.tl_orang_centang);

            vNamaTeman.setText(namaTeman.ambil(position));
            vBidangTeman.setText(bidangTeman.ambil(position));
            Pengguna.Status.pasangIndikatorStatus(vStatus, statusTeman.ambil(position));

            return viewKolom;
        }
    }
}
