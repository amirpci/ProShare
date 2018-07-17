package sidev17.siits.proshare.Modul.Worker;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sidev17.siits.proshare.R;

public class TambahPertanyaanWkr extends AppCompatActivity {

    private GridView wadahCell;
    private TabBarIcon tabBarIcon;

    private EditText teksJudul;
    private EditText teksDeskripsi;

    private ImageView tmbCentang;

    private int tabIcon[]= {R.id.tambah_gambar, R.id.tambah_video, R.id.tambah_link};
    private final String WARNA_DITEKAN= "#4972AD"; //biruLaut
    private final String WARNA_TAK_DITEKAN= "#ADADAD"; //ambilWarna(R.color.abuLebihTua)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pertanyaan);

        wadahCell= findViewById(R.id.tambah_properti_cell_wadah);
        tabBarIcon= new TabBarIcon((View) findViewById(R.id.tambah_properti_wadah),
                (View) findViewById(R.id.tambah_properti_icon));
        tabBarIcon.setTabIcon(tabIcon);
        tabBarIcon.setIdWarnaDitekan(WARNA_DITEKAN);
        tabBarIcon.setIdWarnaTakDitekan(WARNA_TAK_DITEKAN);

        teksJudul= findViewById(R.id.tambah_judul);
        teksDeskripsi= findViewById(R.id.tambah_deskripsi);
        tmbCentang= findViewById(R.id.tambah_ok);
    }
    int ambilWarna(int id){
        return getResources().getColor(id);
    }

    void tekan(View v){
        tabBarIcon.tekanItem(v);
//        wadahCell.setY(tabBarIcon.ambilLetakY());
    }

    class TabBarIcon {
        private int tabIcon[];
        private int tabGaris[];

        private String idWarnaDitekan;
        private String idWarnaTakDitekan;

        private int trahirDitekan= -1;
        private boolean ditekanKah= false;

        View view;
        View viewIcon;

        TabBarIcon(){}
        TabBarIcon(View view, View viewIcon){
            this.view= view;
            this.viewIcon= viewIcon;
        }
        TabBarIcon(View view, int tabIcon[], int tabGaris[], String idWarnaDitekan, String idWarnaTakDitekan){
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
        void setIdWarnaDitekan(String idWarnaDitekan){
            this.idWarnaDitekan= idWarnaDitekan;
        }
        void setIdWarnaTakDitekan(String idWarnaTakDitekan){
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
                    tekanItem(i);
                    break;
                }
            }
        }
        void tekanItem(int ind){
            if(ind != trahirDitekan) {
                if (!ditekanKah) {
                    final float batasTinggi = view.getHeight() + 400;

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) batasTinggi);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    view.setLayoutParams(lp);
                }
                if(ind== 2)
                    wadahCell.setNumColumns(1);
                else
                    wadahCell.setNumColumns(3);

                int lebar = wadahCell.getWidth() / wadahCell.getNumColumns();
                AdapterPropertiCell adpCell = new AdapterPropertiCell(lebar, ind, view);
                wadahCell.setAdapter(adpCell);
            } else if(ditekanKah && ind == trahirDitekan){
                int tinggiAwal= viewIcon.getHeight();

                RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, tinggiAwal);
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                view.setLayoutParams(lp);
            }
            tekanGantiWarna(ind);
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
                    iconDitekanTadi.setColorFilter(Color.parseColor(idWarnaTakDitekan));
                }
                iconDitekanSkrg.setColorFilter(Color.parseColor(idWarnaDitekan));
                trahirDitekan= ind;
                ditekanKah= true;
            } else{
                ImageView iconDitekanTadi = view.findViewById(tabIcon[trahirDitekan]);
                iconDitekanTadi.setColorFilter(Color.parseColor(idWarnaTakDitekan));
                trahirDitekan= -1;
                ditekanKah= false;
            }
        }
    }

    class AdapterPropertiCell extends BaseAdapter{

        int lebar;
        int indPosisi;

        View parent;

        AdapterPropertiCell(int lebar, int indPosisi, View parent){
            this.lebar= lebar;
            this.indPosisi= indPosisi;
            this.parent= parent;
        }

        @Override
        public int getCount() {
            if(indPosisi== 2)
                return 1;
            else
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

            if(indPosisi!= 2) {
                View view= getLayoutInflater().inflate(R.layout.model_tambah_properti_cell, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(lebar, lebar));

                ImageView bg;
                final ImageView centang = view.findViewById(R.id.tambah_cell_centang);
                centang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!centang.isSelected()) {
                            centang.setImageResource(R.drawable.obj_centang_lingkaran_bolong);
                            centang.setColorFilter(Color.parseColor(WARNA_DITEKAN));
                            centang.setSelected(true);
                        } else {
                            centang.setImageResource(R.drawable.latar_lingkaran_bolong);
                            centang.setColorFilter(Color.parseColor("#EEFFFFFF"));
                            centang.setSelected(false);
                        }
                    }
                });
                if (indPosisi == 1) {
                    ImageView indiVideo = view.findViewById(R.id.tambah_cell_indek_video);
                    indiVideo.setImageResource(R.drawable.obj_indek_video_lingkaran);
                }
                return  view;
            } else{

                EditText teksLink= new EditText(getBaseContext());
                RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(40, 40, 40, 40);

                SpannableString string= new SpannableString("insert your link here...");
                string.setSpan(new UnderlineSpan(), 0, string.length(), 0);

                teksLink.setLayoutParams(lp);
                teksLink.setHint(string);
                teksLink.setHintTextColor(Color.parseColor(WARNA_DITEKAN));

                teksLink.setTextColor(Color.parseColor(WARNA_DITEKAN));
                teksLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                return teksLink;
            }
        }
    }
}
