package sidev17.siits.procks.Modul;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import sidev17.siits.procks.Model.Pengguna;
import sidev17.siits.procks.Model.Teman;
import sidev17.siits.procks.R;
import sidev17.siits.procks.Utils.Array;
import sidev17.siits.procks.Utils.PackBahasa;
import sidev17.siits.procks.Utils.Terjemahan;
import sidev17.siits.procks.Utils.Utilities;
import sidev17.siits.procks.Utils.ViewTool.BitmapHandler;

public class TemanDaftarAct extends AppCompatActivity {

    private TextView vJmlTeman, vTemanJudul;

    private ImageView vCariTeman;
    private ImageView vTambahTeman;
    private EditText vKolomCariTeman;

    private ListView vDaftarTeman;

    private Array<Teman> daftarTeman = new Array<>();
    private Array<Teman> daftarTemanTemp  = new Array<>();
    private Array<String> namaTeman= new Array<>();
    private Array<String> bidangTeman= new Array<>();
    private Array<BitmapHandler> fotoTeman= new Array<>();
    private Array<Integer> statusTeman= new Array<>(); //Worker, Expert, Verified Expert

    private AdapterDaftarTeman adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teman_daftar);

        vTemanJudul= findViewById(R.id.teman_judul);
        vJmlTeman = findViewById(R.id.teman_jml);
        vTambahTeman = findViewById(R.id.teman_tambah);
        vTambahTeman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!vCariTeman.isSelected()) {
                    Intent keTambahTeman = new Intent(TemanDaftarAct.this, TemanTambahAct.class);

                    keTambahTeman.putExtra("idUser", -1 /*MSH DUMY!!!*/);
                    startActivity(keTambahTeman);
                } else{
                    vKolomCariTeman.setVisibility(View.GONE);
                    vJmlTeman.setVisibility(View.VISIBLE);
                    vTemanJudul.setVisibility(View.VISIBLE);
                    vTambahTeman.setImageResource(R.drawable.icon_tambah_teman);
//                    vTambahTeman.getDrawable().setTint(getResources().getColor(R.color.hitam));
                    vCariTeman.setSelected(false);
                    if(daftarTemanTemp != null){
                        daftarTeman = daftarTemanTemp;
                       // Toast.makeText(TemanDaftarAct.this, "teman ok "+String.valueOf(daftarTeman.ukuran()), Toast.LENGTH_SHORT).show();
                        vDaftarTeman.setAdapter(adp);
                    }
                }
            }
        });
        vDaftarTeman = findViewById(R.id.teman_daftar);
        vKolomCariTeman= findViewById(R.id.teman_kolom_cari);
        vCariTeman = findViewById(R.id.teman_cari);
        vCariTeman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!v.isSelected()) {
                    vKolomCariTeman.setVisibility(View.VISIBLE);
                    vJmlTeman.setVisibility(View.GONE);
                    vTemanJudul.setVisibility(View.GONE);
                    vTambahTeman.setImageResource(R.drawable.icon_silang);
                    vTambahTeman.getDrawable().setTint(getResources().getColor(R.color.merah));
                    v.setSelected(true);
                    vTemanJudul.setHint(PackBahasa.chat[Terjemahan.indexBahasa(TemanDaftarAct.this)][4]);
                } else{
                    cariTeman();
                }
            }
        });
/*
        vKolomCariTeman.addTextChangedListener(new TextWatcher() {
            int jmlBaris= 1;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(vKolomCariTeman.getLineCount() != jmlBaris){
                    vKolomCariTeman.setSelection(0);
                    jmlBaris= vKolomCariTeman.getLineCount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/
        ambilData();
        ubahBahasa();
    }

    private void ubahBahasa(){
        vTemanJudul.setText(PackBahasa.chat[Terjemahan.indexBahasa(this)][0]);
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
        Utilities.getKontakRef(this).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                daftarTeman.hapusSemua();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Teman teman = snapshot.getValue(Teman.class);
                    daftarTeman.tambah(teman);
                }
                //Log.d("loaded panjang ", String.valueOf(daftarTeman.ukuran()+count));
                daftarTemanTemp = daftarTeman;
                adp= new AdapterDaftarTeman();
                vDaftarTeman.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean cocok(String full, String bagian){
        for(int i = 0; i < full.length(); i ++){
            if(i<=full.length() - bagian.length()){
                if(full.substring(i, i + bagian.length()).equalsIgnoreCase(bagian))
                    return true;
            } else
                break;
        }
        return false;
    }

    private class AdapterDaftarTeman extends BaseAdapter{
        @Override
        public int getCount() {
            int ukuran = daftarTeman.ukuran();
            vJmlTeman.setText("("+String.valueOf(ukuran)+")");
            return ukuran;
        }

        @Override
        public Object getItem(int position) {
            return daftarTeman.ambil(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View viewKolom= getLayoutInflater().inflate(R.layout.model_kolom_chat, null);

            TextView vNamaTeman= viewKolom.findViewById(R.id.feedback_orang_nama);
            TextView vBidangTeman= viewKolom.findViewById(R.id.feedback_orang_chat);
            CircleImageView vFotoTeman= viewKolom.findViewById(R.id.tl_orang_gambar);
            ImageView vStatus= viewKolom.findViewById(R.id.tl_orang_centang);

            vNamaTeman.setText(daftarTeman.ambil(position).getNama());
            vBidangTeman.setText(daftarTeman.ambil(position).getBidang());
            Pengguna.Status.pasangIndikatorStatus(vStatus, (int)daftarTeman.ambil(position).getStatus());

            viewKolom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mulaiChat(daftarTeman.ambil(position));
                }
            });

            return viewKolom;
        }
    }

    private void mulaiChat(Teman teman){
        Utilities.getUserRef().child(teman.getEmail().replace(".", ",")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pengguna orang = dataSnapshot.getValue(Pengguna.class);
                Intent i = new Intent(TemanDaftarAct.this, ChatActivity.class);
                //i.putExtra("idPesan", listPesan.getIdPesan());
                i.putExtra("pengguna", orang);
                //Toast.makeText(getActivity(), orang.getNama(), Toast.LENGTH_SHORT).show();
                startActivity(i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void cariTeman(){
        //LAKUKAN SESUATU!!!
        if(this.daftarTemanTemp != null) {
            Array<Teman> daftarTemanTemp = new Array<>();
            for (int i = 0; i < this.daftarTemanTemp.ukuran(); i++) {
                if (cocok(this.daftarTemanTemp.ambil(i).getNama(), vKolomCariTeman.getText().toString())) {
                    daftarTemanTemp.tambah(this.daftarTemanTemp.ambil(i));
                    //daftarTeman.hapus(i);
                }
            }
            daftarTeman = daftarTemanTemp;
            vDaftarTeman.setAdapter(adp);
        }
    }
}
