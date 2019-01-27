package sidev17.siits.proshare.Modul;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import sidev17.siits.proshare.R;

public class TemanTambahAct extends AppCompatActivity {

    private EditText vKolomCari;
    private ImageView vTmbCari;

    private TextView vNamaOrang;
    private TextView vBidangOrang;

    private RelativeLayout vKelFoto;
    private CircleImageView vFotoOrang;
    private ImageView vStatusOrang;
    private ImageView vSilang;

    private ImageView vTmbTambah;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teman_tambah);

        vKolomCari= findViewById(R.id.teman_kolom_cari_teks);
        vTmbCari= findViewById(R.id.teman_kolom_cari_icon);

        vNamaOrang= findViewById(R.id.teman_nama);
        vBidangOrang= findViewById(R.id.teman_bidang);

        vKelFoto= findViewById(R.id.teman_foto);
        vFotoOrang= findViewById(R.id.teman_foto_gambar);
        vStatusOrang= findViewById(R.id.teman_foto_status);
        vSilang= findViewById(R.id.teman_foto_silang);

        vTmbTambah= findViewById(R.id.teman_tambah_baru);
    }

    private void initAwal(){
        vSilang.setVisibility(View.GONE);
        vStatusOrang.setVisibility(View.GONE);
        vFotoOrang.getDrawable().setAlpha(100);
//        vKelFoto.setVisibility(View.GONE);

        vBidangOrang.setVisibility(View.GONE);

        vTmbTambah.setVisibility(View.GONE);
    }
}
