package sidev17.siits.proshare.Modul.Worker;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sidev17.siits.proshare.Modul.Expert.Tab.ProfileActExprt;
import sidev17.siits.proshare.R;

public class DetailPertanyaanActivityWkr extends AppCompatActivity {
    public final int PENGGUNA_EXPERT_TERVERIFIKASI= 2;
    public final int PENGGUNA_EXPERT= 1;
    public final int PENGGUNA_BIASA= 0;

    private String judulPertanyaan;
    private String deskripsiPertanyaan;
    private ArrayList<Bitmap> gambar;

    private String orang[]= {"Mr. A", "Mr. B", "Mrs. C", "Will Smith"};
    private String job[]= {"Psychiatrist", "Motivator", "Engineer", "Actor"};
    private int kategoriExpert[]= {2, 1, 0, 2};

    private String judulSolusi[]= {"What should I do whe this happen?", "How to gain inspiration?", "How to else?", "Stay cool"};
    private String descSolusi[]= {"I do this everyday, but somehow...", "When it happens, I don't know what to do. I need inspiration.", "bla bla bla...", "Just bel cool bro!"};

    private ViewGroup viewLatar;
    private ListView detailPertanyaan;
    private GridView gambarPertanyaan;
    private AlertDialog dialog;

    private View viewKomentar[];
    private int indViewKomentar[];
    private View viewPertanyaan;
    private final int BATAS_BUFFER_VIEW= 20;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pertanyaan);

        isiDataPertanyaan();

        detailPertanyaan = (ListView) findViewById(R.id.detail_pertanyaan);
        DetailPertanyaanAdapter adpSolusi= new DetailPertanyaanAdapter();
        detailPertanyaan.setAdapter(adpSolusi);

        viewLatar= (ViewGroup) detailPertanyaan.getParent();
/*
        gambarPertanyaan= (GridView) findViewById(R.id.tl_gambar);
        int lebar= gambarPertanyaan.getWidth() /gambarPertanyaan.getNumColumns();
        AdapterGambar adpGambar= new AdapterGambar(lebar);
        gambarPertanyaan.setAdapter(adpGambar);

        if(gambar.size() <= 3)
            aturTinggiGrid(gambarPertanyaan, lebar);
        else
            aturTinggiGrid(gambarPertanyaan, lebar+90);
*/
        int jmlBuffer= (orang.length > BATAS_BUFFER_VIEW) ? BATAS_BUFFER_VIEW : orang.length;
        initViewKomentar(jmlBuffer);
        initIndViewKomentar(jmlBuffer);
        isiViewPertanyaan();
    }

    private void isiDataPertanyaan(){
        Bundle paketDetailPertanyaan= getIntent().getBundleExtra("paket_detail_pertanyaan");

        judulPertanyaan= paketDetailPertanyaan.getString("judul_pertanyaan");
        deskripsiPertanyaan= paketDetailPertanyaan.getString("deskripsi_pertanyaan");
        gambar= ambilGambarDariServer();
    }
    ArrayList<Bitmap> ambilGambarDariServer(){
        //lakukan sesuatu...
        return new ArrayList<Bitmap>();
    }

    private void initViewKomentar(int jml){
        viewKomentar= new View[jml];
        indViewKomentar= new int[jml];
    }
    private void initIndViewKomentar(int init){
        for(int i= 0; i<indViewKomentar.length; i++)
            indViewKomentar[i]= init;
    }
    private void isiViewKomentar(final int ind){
        int indView= ind % viewKomentar.length;
        View view= getLayoutInflater().inflate(R.layout.model_kolom_komentar, null, false);

        TextView teksOrang= view.findViewById(R.id.komentar_orang_nama);
        TextView teksJob= view.findViewById(R.id.komentar_orang_job);
        TextView teksSolusi= view.findViewById(R.id.komentar_deskripsi);
        ImageView centang= view.findViewById(R.id.komentar_orang_centang);
        ImageView fotoProfil= view.findViewById(R.id.komentar_orang_gambar);
        View profil= view.findViewById(R.id.komentar_orang_dp);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perbesar FotoProfl
                munculkanAlertDialog(R.layout.model_detail_user, ind);
            }
        });

        teksOrang.setText(orang[indView]);
        teksJob.setText(job[indView]);
        teksSolusi.setText(descSolusi[indView]);

        if(kategoriExpert[indView]== PENGGUNA_EXPERT)
            centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
        else if(kategoriExpert[indView]== PENGGUNA_EXPERT_TERVERIFIKASI)
            centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);

        viewKomentar[indView]= view;
        indViewKomentar[indView]= ind;
    }
    private void isiViewPertanyaan(){
        viewPertanyaan= getLayoutInflater().inflate(R.layout.model_timeline_pertanyaan, null, false);
        TextView teksJudul= viewPertanyaan.findViewById(R.id.tl_judul);
        TextView teksDeskripsi= viewPertanyaan.findViewById(R.id.tl_deskripsi);

        TextView jmlSuka= viewPertanyaan.findViewById(R.id.tl_orang_angka);
        ImageView tmbSuka= viewPertanyaan.findViewById(R.id.tl_suka_gambar);
        tmbSuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahSuka();
            }
        });

        teksJudul.setText(judulPertanyaan);
        teksDeskripsi.setText(deskripsiPertanyaan);
    }
    private void tambahSuka(){
        //lakukan sesuatu jika orang kamu menyukai postingan itu
    }

    private void munculkanAlertDialog(int idHalaman, int indUser){
        final View view= getLayoutInflater().inflate(idHalaman, null, false);
        ImageView viewFoto= view.findViewById(R.id.user_profil);
        TextView viewNama= view.findViewById(R.id.user_nama);
        TextView viewStatus= view.findViewById(R.id.user_status);

        viewNama.setText(orang[indUser]);
        viewStatus.setText(job[indUser]);

        View latarBel= view.findViewById(R.id.user_latar);
        latarBel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLatar.removeView(view);
            }
        });
        ImageView aksiChat= view.findViewById(R.id.user_aksi_chat);
        aksiChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inten ke chat orang itu
            }
        });
        ImageView aksiProfil= view.findViewById(R.id.user_aksi_lihat);
        aksiProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inten ke profil orang itu
            }
        });
/*
        AlertDialog.Builder builder= new AlertDialog.Builder(getBaseContext());
        builder.setView(view);

        dialog= builder.create();
        dialog.show();

        Dialog dialog= new Dialog(getBaseContext());
        dialog.setContentView(view);
        dialog.show();
*/
        viewLatar.addView(view);

        Toast.makeText(getBaseContext(), orang[indUser], Toast.LENGTH_LONG).show();
    }


    void aturTinggiGrid(GridView grid, int tinggi){
        grid.getLayoutParams().height= tinggi;
    }


    class AdapterGambar extends BaseAdapter{
        private int lebar;

        AdapterGambar(int lebar){
            this.lebar= lebar;
        }

        @Override
        public int getCount() {
            return gambar.size();
        }

        @Override
        public Object getItem(int position) {
            return gambar.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parentInn) {
            View view= getLayoutInflater().inflate(R.layout.model_tambah_properti_cell, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(lebar, lebar));

            ImageView bg= view.findViewById(R.id.tambah_cell_pratinjau);
            bg.setImageBitmap(gambar.get(position));

            ImageView centang = view.findViewById(R.id.tambah_cell_centang);

            ViewGroup vg= (ViewGroup) view;
            vg.removeView(centang);

            return view;
        }
    }


    class DetailPertanyaanAdapter extends BaseAdapter{
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
                if(indViewKomentar[position % indViewKomentar.length] != position -1)
                    isiViewKomentar(position -1);
                view= viewKomentar[position-1];
            }
            else{
                view= viewPertanyaan;
            }
            return view;
        }
    }
}
