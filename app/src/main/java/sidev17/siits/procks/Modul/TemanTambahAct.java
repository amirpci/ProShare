package sidev17.siits.procks.Modul;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import sidev17.siits.procks.Interface.BidangListener;
import sidev17.siits.procks.Model.Pengguna;
import sidev17.siits.procks.Model.Teman;
import sidev17.siits.procks.R;
import sidev17.siits.procks.Utils.PackBahasa;
import sidev17.siits.procks.Utils.Terjemahan;
import sidev17.siits.procks.Utils.Utilities;

public class TemanTambahAct extends AppCompatActivity implements BidangListener {

    private EditText vKolomCari;
    private ImageView vTmbCari;

    private TextView vNamaOrang;
    private TextView vBidangOrang;
    private TextView cariJudul;

    private RelativeLayout vKelFoto, vInformasiOrang;
    private CircleImageView vFotoOrang;
    private ImageView vStatusOrang;
    private ImageView vSilang;

    private ImageView vTmbTambah;

    private boolean orangDitemukan = false;
    private Teman kontak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teman_tambah);

        vKolomCari= findViewById(R.id.teman_kolom_cari_teks);
        vTmbCari= findViewById(R.id.teman_kolom_cari_icon);

        vNamaOrang= findViewById(R.id.teman_nama);
        vBidangOrang= findViewById(R.id.teman_bidang);

        vKelFoto= findViewById(R.id.teman_foto);
        vInformasiOrang = findViewById(R.id.teman_informasi);
        vFotoOrang= findViewById(R.id.teman_foto_gambar);
        vStatusOrang= findViewById(R.id.teman_foto_status);
        vSilang= findViewById(R.id.teman_foto_silang);

        vTmbTambah= findViewById(R.id.teman_tambah_baru);

        cariJudul = findViewById(R.id.teman_judul);

        ubahBahasa();

        vKolomCari.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    cariOrang(vKolomCari.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void cariOrang(final String email){
        Utilities.getUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(email.toLowerCase().replace(".",","))) {
                    Pengguna pengguna = snapshot.child(email.toLowerCase().replace(".", ",")).getValue(Pengguna.class);
                    if(getApplicationContext() != null){
                        vInformasiOrang.setVisibility(View.VISIBLE);
                        vTmbTambah.setVisibility(View.VISIBLE);
                        vBidangOrang.setVisibility(View.VISIBLE);
                        vBidangOrang.setText("");
                        vNamaOrang.setText(pengguna.getNama());
                        vStatusOrang.setVisibility(View.VISIBLE);
                        vSilang.setVisibility(View.GONE);
                        Pengguna.Status.pasangIndikatorStatus(vStatusOrang, (int)pengguna.getStatus());

                        orangDitemukan = true;
                        kontak = new Teman();
                        kontak.setNama(pengguna.getNama());
                        kontak.setBidang("");
                        kontak.setEmail(pengguna.getEmail());
                        kontak.setStatus(pengguna.getStatus());
                        kontak.setUrlFoto(pengguna.getPhotoProfile());
                        
                        vTmbTambah.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tambahTeman(kontak);
                            }
                        });
                        
                        new AsyncTask<String, Void, String>(){

                            @Override
                            protected String doInBackground(String... integers) {
                                return Utilities.loadBidangKu(integers[0], TemanTambahAct.this);
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                muatBidang(s, false);
                            }
                        }.execute(pengguna.getBidang());
                    }
                }else{
                    if(getApplicationContext() != null){
                        kontak=null;
                        vInformasiOrang.setVisibility(View.VISIBLE);
                        vStatusOrang.setVisibility(View.GONE);
                        vBidangOrang.setVisibility(View.GONE);
                        vSilang.setVisibility(View.VISIBLE);
                        vNamaOrang.setText(PackBahasa.chat[Terjemahan.indexBahasa(TemanTambahAct.this)][3]);
                        vTmbTambah.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void tambahTeman(final Teman teman){
        Utilities.getKontakRef(this).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(teman.getEmail().replace(".", ","))){
                    Utilities.getKontakRef(TemanTambahAct.this).child(teman.getEmail().replace(".", ",")).setValue(teman).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(TemanTambahAct.this, PackBahasa.chatToast[Terjemahan.indexBahasa(getApplicationContext())][0], Toast.LENGTH_SHORT).show();
                                finish();
                            }else Toast.makeText(TemanTambahAct.this, PackBahasa.chatToast[Terjemahan.indexBahasa(getApplicationContext())][1], Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(TemanTambahAct.this, PackBahasa.chatToast[Terjemahan.indexBahasa(getApplicationContext())][2], Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    
    private void ubahBahasa(){
        vNamaOrang.setText(PackBahasa.chat[Terjemahan.indexBahasa(this)][3]);
        cariJudul.setText(PackBahasa.chat[Terjemahan.indexBahasa(this)][1]);
        vKolomCari.setHint(PackBahasa.chat[Terjemahan.indexBahasa(this)][2]);
    }

    private void initAwal(){
        vSilang.setVisibility(View.GONE);
        vStatusOrang.setVisibility(View.GONE);
        vFotoOrang.getDrawable().setAlpha(100);
//        vKelFoto.setVisibility(View.GONE);

        vBidangOrang.setVisibility(View.GONE);

        vTmbTambah.setVisibility(View.GONE);
    }

    @Override
    public void muatBidang(String bidang, boolean diterjemahkan) {
        if(!diterjemahkan){
            if(vBidangOrang != null)
                vBidangOrang.setText(bidang);
            new AsyncTask<String, Void, String>(){

                @Override
                protected String doInBackground(String... strings) {
                    return Terjemahan.terjemahkan(strings[0], Utilities.deteksiBahasa(strings[0], TemanTambahAct.this), Utilities.getUserBahasa(TemanTambahAct.this));
                }

                @Override
                protected void onPostExecute(String s) {
                    muatBidang(s, true);
                }
            }.execute(bidang);
        }else{
            if(vBidangOrang != null)
                vBidangOrang.setText(bidang);
        }
        kontak.setBidang(bidang);
    }
}
