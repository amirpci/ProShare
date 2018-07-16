package sidev17.siits.proshare.Modul.Worker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import sidev17.siits.proshare.R;

public class TambahPertanyaanWkr extends AppCompatActivity {

    private GridView wadahCell;
    private TabBarIcon tabBarIcon;

    private EditText teksJudul;
    private EditText teksDeskripsi;

    private ImageView tmbCentang;

    private int tabIcon[]= {R.id.tambah_gambar, R.id.tambah_video, R.id.tambah_link};
    private final int WARNA_DITEKAN= ambilWarna(R.color.biruLaut);
    private final int WARNA_TAK_DITEKAN= ambilWarna(R.color.abuLebihTua);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pertanyaan);

        wadahCell= findViewById(R.id.tambah_properti_cell_wadah);
        tabBarIcon= new TabBarIcon((View) findViewById(R.id.tambah_properti_wadah));
        tabBarIcon.setTabIcon(tabIcon);
        tabBarIcon.setIdWarnaDitekan(WARNA_DITEKAN);
        tabBarIcon.setIdWarnaTakDitekan(WARNA_TAK_DITEKAN);

        teksJudul= findViewById(R.id.tambah_judul);
        teksDeskripsi= findViewById(R.id.tambah_deskripsi);
        tmbCentang= findViewById(R.id.tambah_ok);
    }
    int ambilWarna(int id){
//        return getResources().getColor(id);
        return id;
    }

    void tekan(View v){
        tabBarIcon.tekanItem(v);
        AdapterPropertiCell adpCell= new AdapterPropertiCell();
        wadahCell.setAdapter(adpCell);
    }

    class TabBarIcon {
        private int tabIcon[];
        private int tabGaris[];

        private int idWarnaDitekan;
        private int idWarnaTakDitekan;

        private int trahirDitekan= -1;
        private boolean ditekanKah= false;;

        View view;

        TabBarIcon(){}
        TabBarIcon(View view){
            this.view= view;
        }
        TabBarIcon(View view, int tabIcon[], int tabGaris[], int idWarnaDitekan, int idWarnaTakDitekan){
            this.view= view;
            this.tabIcon= tabIcon;
            this.tabGaris= tabGaris;
            this.idWarnaDitekan= idWarnaDitekan;
            this.idWarnaTakDitekan= idWarnaTakDitekan;
        }
        void setTabIcon(int tabIcon[]){
            this.tabIcon= tabIcon;
        }
        void setTabGaris(int tabGaris[]){
            this.tabGaris= tabGaris;
        }
        void setIdWarnaDitekan(int idWarnaDitekan){
            this.idWarnaDitekan= idWarnaDitekan;
        }
        void setIdWarnaTakDitekan(int idWarnaTakDitekan){
            this.idWarnaTakDitekan= idWarnaTakDitekan;
        }

        int getTrahirDitekan(){
            return trahirDitekan;
        }

        public void tekanItem(View v){
            View icon;
            for(int i= 0; i< tabIcon.length; i++) {
                icon = view.findViewById(tabIcon[i]);
                if (icon == v) {
                    tekan(i);
                    break;
                }
            }
        }
        void tekan(int ind){
            tekanGantiWarna(ind);
            final float tinggiAwal = view.getHeight();
            final float batasTinggi= tinggiAwal +400;
            float lebar = view.getWidth();

            view.setLayoutParams(new RelativeLayout.LayoutParams((int) lebar, (int) batasTinggi));
/*
            if(tinggiAwal <batasTinggi) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        float tinggiUbah= tinggiAwal;
                        float lebar = view.getWidth();
                        while (tinggiUbah <= batasTinggi) {
                            try {
                                sleep(5);
                            } catch (Exception e) {
                            } finally {
                                view.setLayoutParams(new RelativeLayout.LayoutParams((int) lebar, (int) tinggiUbah));
                            }
                            tinggiUbah++;
                        }
                        super.run();
                    }
                };
                thread.run();
            }
*/
        }
        void tekanGantiWarna(int ind){
            if(ind != trahirDitekan) {
                ImageView iconDitekanSkrg = view.findViewById(tabIcon[ind]);
                if (ditekanKah) {
                    ImageView iconDitekanTadi = view.findViewById(tabIcon[trahirDitekan]);
                    iconDitekanTadi.setColorFilter(idWarnaTakDitekan);
                }
                iconDitekanSkrg.setColorFilter(idWarnaDitekan);
            }
        }
    }

    class AdapterPropertiCell extends BaseAdapter{

        AdapterPropertiCell(){}

        @Override
        public int getCount() {
            return 20;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.model_tambah_properti_cell, null);

            ImageView bg;
            final ImageView centang= view.findViewById(R.id.tambah_cell_centang);
            centang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!centang.isSelected()) {
                        centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_bolong);
                        centang.setBackgroundColor(ambilWarna(R.color.biruLaut));
                        centang.setSelected(true);
                    }else{
                        centang.setBackgroundResource(R.drawable.latar_lingkaran);
                        centang.setBackgroundColor(Color.parseColor("#33000000"));
                        centang.setSelected(false);
                    }
                }
            });

            return null;
        }
    }
}
