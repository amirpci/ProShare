package sidev17.siits.proshare.Modul.Worker;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rmtheis.yandtran.language.Language;

import java.util.ArrayList;

import sidev17.siits.proshare.Model.Pengguna;
import sidev17.siits.proshare.Modul.AmbilGambarAct;
import sidev17.siits.proshare.Modul.Expert.Tab.ProfileActExprt;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.GaleriLoader;
import sidev17.siits.proshare.Utils.Utilities;

public class DetailPertanyaanActivityWkr extends AppCompatActivity {
    public final int PENGGUNA_EXPERT_TERVERIFIKASI= 2;
    public final int PENGGUNA_EXPERT= 1;
    public final int PENGGUNA_BIASA= 0;

    private static final int AMBIL_GAMBAR= 11;

    private String namaPemilik;
    private String majorPemilik;
    private int statusPemilik;

    private String judulPertanyaan;
    private String majorityPertanyaan;
    private String deskripsiPertanyaan;
    private String emailOrang;
    private ArrayList<Bitmap> gambar;

    private String orang[]= {"Mr. A", "Mr. B", "Mrs. C", "Will Smith"};
    private String job[]= {"Psychiatrist", "Motivator", "Engineer", "Actor"};
    private int kategoriExpert[]= {2, 1, 0, 2};

    private String judulSolusi[]= {"What should I do whe this happen?", "How to gain inspiration?", "How to else?", "Stay cool"};
    private String descSolusi[]= {"I do this everyday, but somehow...", "When it happens, I don't know what to do. I need inspiration.", "bla bla bla...", "Just bel cool bro!"};

    private ViewGroup viewLatar;
    private ListView detailPertanyaan;
//    private GridView gambarPertanyaan;
    private View detailProfil;
    private AlertDialog dialog;
    private boolean profilDitampilkan= false;

    private int indViewKomentar[];
    private View viewPertanyaan;
    private View viewSolusi;
    private View viewKomentar[];
    private View viewBarKomen;
    private final int BATAS_BUFFER_VIEW= 7;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pertanyaan);

        isiDataPertanyaan();

        detailPertanyaan = (ListView) findViewById(R.id.detail_pertanyaan);
        DetailPertanyaanAdapter adpSolusi= new DetailPertanyaanAdapter();
        detailPertanyaan.setAdapter(adpSolusi);

        viewLatar= (ViewGroup) detailPertanyaan.getRootView();
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
        isiViewSolusi();
        isiViewPertanyaan();
        initBarKomen();
        ((ViewGroup) findViewById(R.id.detail_bar_komen)).addView(viewBarKomen);
    }

    private void isiDataPertanyaan(){
        Bundle paketDetailPertanyaan= getIntent().getBundleExtra("paket_detail_pertanyaan");

        judulPertanyaan= paketDetailPertanyaan.getString("judul_pertanyaan");
        deskripsiPertanyaan= paketDetailPertanyaan.getString("deskripsi_pertanyaan");
        emailOrang = paketDetailPertanyaan.getString("owner");

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

        final TextView jmlSuka= view.findViewById(R.id.komentar_vote_posisi);
        //Belum selesai
//        jmlSuka.setText(Integer.toString(ambilDaftarVoter("id").length));

        View profil= view.findViewById(R.id.komentar_orang_dp);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perbesar FotoProfl
                munculkanAlertDialog(R.layout.model_detail_user, ind);
            }
        });
        ImageView tmbVoteUp= view.findViewById(R.id.komentar_vote_up);
        tmbVoteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteUp("id", "emailVoter", jmlSuka);
            }
        });
        ImageView tmbVoteDown= view.findViewById(R.id.komentar_vote_down);
        tmbVoteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteDown("id", "emailVoter", jmlSuka);
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
    private void isiViewSolusi(){
        viewSolusi= getLayoutInflater().inflate(R.layout.model_solusi_expert, null, false);

        TextView teksOrang= viewSolusi.findViewById(R.id.komentar_orang_nama);
        TextView teksJob= viewSolusi.findViewById(R.id.komentar_orang_job);
        TextView teksSolusi= viewSolusi.findViewById(R.id.komentar_deskripsi);
        ImageView centang= viewSolusi.findViewById(R.id.komentar_orang_centang);
        ImageView fotoProfil= viewSolusi.findViewById(R.id.komentar_orang_gambar);

        final TextView jmlSuka= viewSolusi.findViewById(R.id.komentar_vote_posisi);
        //Belum selesai
//        jmlSuka.setText(Integer.toString(ambilDaftarVoter("id").length));

        View profil= viewSolusi.findViewById(R.id.komentar_orang_dp);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perbesar FotoProfl
                munculkanAlertDialog(R.layout.model_detail_user, 0);
            }
        });
        ImageView tmbVoteUp= viewSolusi.findViewById(R.id.komentar_vote_up);
        tmbVoteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteUp("id", "emailVoter", jmlSuka);
            }
        });
        ImageView tmbVoteDown= viewSolusi.findViewById(R.id.komentar_vote_down);
        tmbVoteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteDown("id", "emailVoter", jmlSuka);
            }
        });

        teksOrang.setText(orang[0]);
        teksJob.setText(job[0]);
        teksSolusi.setText(descSolusi[0]);

        if(kategoriExpert[0]== PENGGUNA_EXPERT)
            centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
        else if(kategoriExpert[0]== PENGGUNA_EXPERT_TERVERIFIKASI)
            centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);

        indViewKomentar[0]= 0;
    }
    private void isiViewPertanyaan(){
        viewPertanyaan= getLayoutInflater().inflate(R.layout.model_timeline_pertanyaan, null, false);
        TextView teksJudul= viewPertanyaan.findViewById(R.id.tl_judul);
        TextView teksMajority= viewPertanyaan.findViewById(R.id.tl_majority);
        TextView teksDeskripsi= viewPertanyaan.findViewById(R.id.tl_deskripsi);
        TextView teksNama = viewPertanyaan.findViewById(R.id.tl_nama_orang);
        TextView teksStatusOrang = viewPertanyaan.findViewById(R.id.tl_status_orang);
        initOrang(teksNama, teksStatusOrang);

        final TextView jmlSuka= viewPertanyaan.findViewById(R.id.komentar_vote_posisi);
        //Belum selesai
//        jmlSuka.setText(Integer.toString(ambilDaftarVoter("id").length));

        View profil= viewPertanyaan.findViewById(R.id.tl_orang_gambar);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perbesar FotoProfl
                munculkanAlertDialog(R.layout.model_detail_user, -1);
            }
        });
        ImageView tmbVoteUp= viewPertanyaan.findViewById(R.id.komentar_vote_up);
        tmbVoteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteUp("id", "emailVoter", jmlSuka);
            }
        });
        ImageView tmbVoteDown= viewPertanyaan.findViewById(R.id.komentar_vote_down);
        tmbVoteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteDown("id", "emailVoter", jmlSuka);
            }
        });

        teksJudul.setText(judulPertanyaan);
        teksMajority.setText(majorityPertanyaan);
        teksDeskripsi.setText(deskripsiPertanyaan);
    }
    private void initBarKomen(){
//        RelativeLayout.LayoutParams lpBar= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lpBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        lpBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        viewBarKomen= getLayoutInflater().inflate(R.layout.model_tab_edit_text, null, false);
//        viewBarKomen.setLayoutParams(lpBar);
        viewBarKomen.setMinimumHeight(60);

        View klip= viewBarKomen.findViewById(R.id.tab_text_indikator);
        TextView hint= viewBarKomen.findViewById(R.id.tab_text_hint);
        View kirim= viewBarKomen.findViewById(R.id.tab_text_tindakan);

        ((ImageView) klip.findViewById(R.id.tab_text_indikator_gambar)).setImageResource(R.drawable.icon_klip);
        hint.setHint("Masukan komentar Anda");
        hint.setMaxLines(4);
        ((ImageView) kirim.findViewById(R.id.tab_text_tindakan_gambar)).setImageResource(R.drawable.icon_kirim);

        klip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keAmbilGambar= new Intent(DetailPertanyaanActivityWkr.this, AmbilGambarAct.class);
                startActivityForResult(keAmbilGambar, AMBIL_GAMBAR);
            }
        });
    }
    private void kirimKomentar(String idKomentator, String komentar, Bitmap gambar[]){
        //lakukan sesuatu
    }
    private GaleriLoader initLoader(String pathFile[]){
        final GaleriLoader loader= new GaleriLoader(getBaseContext(), this, pathFile, 18, GaleriLoader.JENIS_FOTO);
        loader.aturBentukFoto(GaleriLoader.BENTUK_KOTAK);
        loader.aturUkuranPratinjau(250);
        loader.aturSumberBg(R.drawable.obj_gambar_kotak);
        loader.aturSumberBgTakBisa(R.drawable.obj_tanda_seru_lingkaran_garis);
        loader.aturIdElemenImg(R.id.tambah_cell_pratinjau);

        loader.aturModeBg(false);

        loader.aturAksiPilihFoto(new GaleriLoader.AksiPilihFoto() {
            @Override
            public void pilihFoto(View v, int posisi) {
                TextView noUrut = v.findViewById(R.id.tambah_cell_centang_no);
                noUrut.setText(Integer.toString(loader.ambilUrutanDipilih(posisi)));
                noUrut.getBackground().setTint(getResources().getColor(R.color.biruLaut));
                noUrut.getBackground().setAlpha(255);
            }
            @Override
            public void batalPilihFoto(View v, int posisi) {
                int indDipilih[]= loader.ambilSemuaUrutanDipilih();
                ArrayList<View> viewDipilih= loader.ambilViewDipilih();
                int indYgDibatalkan= loader.ambilUrutanDipilih(posisi) -1;
                int indBaru= indYgDibatalkan +1;

                TextView noUrut = v.findViewById(R.id.tambah_cell_centang_no);
                noUrut.setText("");
                noUrut.getBackground().setAlpha(0);

                for(int i= 0; i< indDipilih.length; i++) {
                    if(i > indYgDibatalkan) {
                        noUrut = viewDipilih.get(i).findViewById(R.id.tambah_cell_centang_no);
                        noUrut.setText(Integer.toString(indBaru++));
                    }
                }
            }
        });

        loader.aturAksiBuffer(new GaleriLoader.AksiBuffer() {
            @Override
            public void bufferThumbnail(final int posisi, final int jmlBuffer) {
                View view= loader.ambilView(posisi);
                final int lebar= view.getLayoutParams().width;
/*
                final TextView indDipilih = view.findViewById(R.id.tambah_cell_centang_no);
                final GridView liveQuestion= parentUtama.findViewById(R.id.tambah_properti_cell_dipilih);
                indDipilih.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!indDipilih.isSelected()) {
                            loader.pilihFoto(posisi);
                            indDipilih.setSelected(true);
                            TambahPertanyaanWkr.AdapterPropertiDipilih adpDipilih= new TambahPertanyaanWkr.AdapterPropertiDipilih(lebar, loader.ambilFotoDipilih());
                            liveQuestion.setAdapter(adpDipilih);
                            if(loader.ambilFotoDipilih().size() <= 3)
                                aturTinggiGrid(liveQuestion, lebar);
                            else
                                aturTinggiGrid(liveQuestion, lebar+90);
                        } else {
                            loader.batalPilihFoto(posisi);
                            indDipilih.setSelected(false);
                            TambahPertanyaanWkr.AdapterPropertiDipilih adpDipilih= new TambahPertanyaanWkr.AdapterPropertiDipilih(lebar, loader.ambilFotoDipilih());
                            liveQuestion.setAdapter(adpDipilih);
                            if(loader.ambilFotoDipilih().size() == 0)
                                aturTinggiGrid(liveQuestion, 0);
                            else if(loader.ambilFotoDipilih().size() <= 3)
                                aturTinggiGrid(liveQuestion, lebar);
                            else
                                aturTinggiGrid(liveQuestion, lebar+90);
                        }
                    }
                });
*/
            }

            @Override
            public void bufferUtama(int posisi, int jmlBuffer) {

            }
        });
        return loader;
    }

    class AdapterPropertiCell extends BaseAdapter {

        GaleriLoader loader;
        int lebar;
        int jml;

        AdapterPropertiCell(GaleriLoader loader, int lebar, int jml){
            this.loader= loader;
            this.lebar= lebar;
            this.jml= jml;
        }

        @Override
        public int getCount() {
            return jml;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parentInn) {
            View view= getLayoutInflater().inflate(R.layout.model_tambah_properti_cell, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(lebar, lebar));

            view= loader.buatFoto(view, position);
            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AMBIL_GAMBAR && resultCode == RESULT_OK){
            final GridView gambarDipilih= viewBarKomen.findViewById(R.id.wadah_gambar);
            float batasTinggi = viewBarKomen.getHeight() + 400;
            String pathFoto[]= data.getStringArrayExtra("pathFoto");

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) batasTinggi);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            viewBarKomen.setLayoutParams(lp);
            gambarDipilih.setAdapter(new AdapterPropertiCell(initLoader(pathFoto), gambarDipilih.getWidth() / gambarDipilih.getNumColumns(), pathFoto.length));

            ((EditText) viewBarKomen.findViewById(R.id.tab_text_hint)).setMaxLines(1);
            final ImageView tmbBatal= viewBarKomen.findViewById(R.id.batal);
            tmbBatal.setVisibility(View.VISIBLE);
            tmbBatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    gambarDipilih.setAdapter(null);
                    viewBarKomen.setLayoutParams(lp);
                    ((EditText) viewBarKomen.findViewById(R.id.tab_text_hint)).setMaxLines(4);
                    tmbBatal.setVisibility(View.GONE);
                }
            });
        }
    }

    private void initOrang(final TextView nama, final TextView status){
        Utilities.getUserRef(emailOrang).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pengguna user = dataSnapshot.getValue(Pengguna.class);
                namaPemilik= user.getNama();
                nama.setText(namaPemilik);
                Language languageID=null;
                switch (user.getNegara()){
                    case "Indonesia" : languageID=Language.INDONESIAN; break;
                    case "United States" : languageID=Language.ENGLISH; break;
                    case "United Kingdom" : languageID=Language.ENGLISH; break;
                    case "Japan" : languageID=Language.JAPANESE; break;
                }
                com.rmtheis.yandtran.translate.Translate.setKey(getString(R.string.yandex_api_key));
                try {
                    switch ((int)user.getStatus()){
                        case 200 : status.setText(com.rmtheis.yandtran.translate.Translate.execute("Worker", Language.ENGLISH, languageID));
                            statusPemilik= PENGGUNA_BIASA;
                            majorPemilik= "Worker";
                            break;
                        case 201 : status.setText(com.rmtheis.yandtran.translate.Translate.execute("Expert", Language.ENGLISH, languageID));
                            statusPemilik= PENGGUNA_EXPERT;
                            majorPemilik= "Expert";
                            break;
                        case 202 : status.setText(com.rmtheis.yandtran.translate.Translate.execute("Verified Expert", Language.ENGLISH, languageID));
                            statusPemilik= PENGGUNA_EXPERT_TERVERIFIKASI;
                            majorPemilik= "Verified Expert";
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void voteUp(String idPostingan, String idVoter, TextView jmlVote){
        //lakukan sesuatu jika user menyukai postingan itu.
        //setiap postingan (pertanyaan, solusi, atau komentar) dengan "idPostingan" memiliki daftar
        //user (idVoter) yang nge-voteUp postingan itu.
        //jumlah vote (jmlVote) akan diupdate berdasarkan panjang data "idPostingan" di server.
        //         if(disukai)
        //         else if(dibatalkan)
    }
    private void voteDown(String idPostingan, String idUser, TextView jmlVote){
        //lakukan sesuatu jika orang kamu menyukai postingan itu.
        //setiap postingan (pertanyaan, solusi, atau komentar) dengan "idPostingan" memiliki daftar
        //user (idVoter) yang nge-voteDown postingan itu.
        //jumlah vote (jmlVote) akan diupdate berdasarkan panjang data "idPostingan" di server.
        //         if(tidakDisukai)
        //         else if(dibatalkan)
    }
    private String[] ambilDaftarVoter(String idPostingan){
        //ambil daftar voter tiap postingan (pertanyaan, solusi, atau komentar).
        return null;
    }

    private void munculkanAlertDialog(int idHalaman, int indUser){
        if(detailProfil == null){
        detailProfil= getLayoutInflater().inflate(idHalaman, null, false);

        View latarBel= detailProfil.findViewById(R.id.user_latar);
        latarBel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLatar.removeView(detailProfil);
                profilDitampilkan= false;
            }
        });
        ImageView aksiChat= detailProfil.findViewById(R.id.user_aksi_chat);
        aksiChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inten ke chat orang itu
            }
        });
        ImageView aksiProfil= detailProfil.findViewById(R.id.user_aksi_lihat);
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
        }
        if(!profilDitampilkan) {
            viewLatar.addView(detailProfil);
            ImageView viewFoto= detailProfil.findViewById(R.id.user_profil);
            TextView viewNama= detailProfil.findViewById(R.id.user_nama);
            TextView viewStatus= detailProfil.findViewById(R.id.user_status);

            if(indUser >= 0) {
                viewNama.setText(orang[indUser]);
                viewStatus.setText(job[indUser]);
                Toast.makeText(getBaseContext(), orang[indUser], Toast.LENGTH_LONG).show();
            } else if(indUser == -1){
                viewNama.setText(namaPemilik);
                viewStatus.setText(majorPemilik);
                Toast.makeText(getBaseContext(), namaPemilik, Toast.LENGTH_LONG).show();
            }
            profilDitampilkan= true;
        }
        else {
            viewLatar.removeView(detailProfil);
            profilDitampilkan= false;
        }

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
            if(position > 1){
                if(indViewKomentar[position % indViewKomentar.length] != position -1)
                    isiViewKomentar(position -1);
                view= viewKomentar[position-1];
            }
            else if(position == 0)
                view= viewPertanyaan;
            else
                view= viewSolusi;
            return view;
        }
    }

    private class GridAdp extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
