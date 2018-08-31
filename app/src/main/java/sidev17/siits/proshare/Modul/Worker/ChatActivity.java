package sidev17.siits.proshare.Modul.Worker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Chat;

public class ChatActivity extends AppCompatActivity{

    ImageView fotoOrgTujuan;
    TextView namaOrgTujuan;
    TextView bidangOrgTujuan;
    ListView wadahChat;

    RelativeLayout barIsi;
    ImageView tmbSisipkan;
    ImageView iconKirim;
    EditText isiPesan;
    RelativeLayout tmbKirim;

    Chat chat;
    String pesan[]= {"Assalamualaikum", "Waalaikumussalam", "Perkenalkan, saya Makan", "Mhs Sistem Informasi, ITS", "Iya mas Amir", "Saya ingin bertanya", "Gak jadi pak", "Makasih :)", "Lhe... -_-"};
    String pukul[]= {"10:11", "10:30", "10:31", "10:31", "10:41", "10:41", "10:41", "10:41", "12:21"};
    int diriSendiri[]= {Chat.DIRI_SENDIRI, Chat.ORANG_LAIN, Chat.DIRI_SENDIRI, Chat.DIRI_SENDIRI, Chat.ORANG_LAIN, Chat.DIRI_SENDIRI, Chat.DIRI_SENDIRI, Chat.DIRI_SENDIRI, Chat.ORANG_LAIN};

    final String fileInternal= Environment.getExternalStorageDirectory() +"/ProShare/Chat/data";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        fotoOrgTujuan= findViewById(R.id.chat_bar_status_foto);
        namaOrgTujuan= findViewById(R.id.chat_bar_status_nama);
        bidangOrgTujuan= findViewById(R.id.chat_bar_status_major);

        barIsi= findViewById(R.id.chat_bar_isi);
        barIsi.addView((RelativeLayout) getLayoutInflater().inflate(R.layout.model_tab_edit_text, null, false));
        tmbSisipkan= barIsi.findViewById(R.id.tab_text_gambar);
        tmbKirim= barIsi.findViewById(R.id.tab_text_tindakan);
        iconKirim= barIsi.findViewById(R.id.tab_text_tindakan_gambar);
        isiPesan= barIsi.findViewById(R.id.tab_text_teks);

        wadahChat= findViewById(R.id.chat_wadah);
        ChatAdapter adp= new ChatAdapter();
        wadahChat.setDivider(null);
        wadahChat.setAdapter(adp);
        wadahChat.setSelection(pesan.length-1);
        wadahChat.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem== pesan.length -2) {
                    if(chat.adaSisaChatDiInternal()) {
                        perlebarChat();
                        bacaInternal();
                        ambilChatSemua();
                        muatUlangAdapter();
                    }
                }
            }
        });

        inisiasiChat();
//        ambilChatSemua();
        aturNamaOrg();
        aturFotoOrg();
        aturBidangOrg();

        aturDefaultBarTeks();
    }

    void inisiasiChat(){
        chat= new Chat(fileInternal);
        chat.resetData(30);
        chat.rampingkanChat();
    }
    void ambilChatSemua(){
        pesan= chat.ambilPesanSemua();
        pukul= chat.ambilPukulSemua();
        diriSendiri= chat.ambilDiriSendiriSemua();
    }
    void perlebarChat(){
        chat.perlebarChat(30);
    }
    void bacaInternal(){
        chat.bacaInternal(pesan.length,30);
        chat.rampingkanChat();
    }

    void muatUlangAdapter(){
        ChatAdapter adp= new ChatAdapter();
        int posisi= wadahChat.getFirstVisiblePosition();
        wadahChat.setAdapter(adp);
        wadahChat.setSelection(posisi);
    }

    void aturDefaultBarTeks(){
        isiPesan.setHint("Your message here");
        tmbSisipkan.setImageResource(R.drawable.icon_klip);
        iconKirim.setImageResource(R.drawable.icon_kirim);
    }
    void kirimChat(){
        String pesanKirim= isiPesan.getText().toString();
        Date waktuKirim= new Date();
        chat.tambahChat(pesanKirim, waktuKirim, Chat.DIRI_SENDIRI);
    }

    void aturFotoOrg(){
        Bitmap foto= chat.ambilFotoOrg();
        fotoOrgTujuan.setImageBitmap(chat.ambilFotoOrg());
        if(foto== null) {
            fotoOrgTujuan.setImageResource(R.drawable.obj_profilephoto);
            fotoOrgTujuan.setColorFilter(getResources().getColor(R.color.abuLebihTua));
        }
    }
    void aturNamaOrg(){
        namaOrgTujuan.setText("Johnson Maumakan"); //setText(chat.ambilNamaOrg());
    }
    void aturBidangOrg(){
        bidangOrgTujuan.setText("Material Engineer"); //setText(chat.ambilBidangOrg());
    }

    private class ChatAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return pesan.length;
        }

        @Override
        public Object getItem(int position) {
            return pesan[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if(diriSendiri[position]== Chat.DIRI_SENDIRI) {
                view = getLayoutInflater().inflate(R.layout.model_chat_diri_sendiri, null, false);
                RelativeLayout latarChat= view.findViewById(R.id.chat_latar);
                ImageView latarPusuk= view.findViewById(R.id.chat_pucuk);

                latarChat.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.ADD);
                latarPusuk.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
            }
            else
                view= getLayoutInflater().inflate(R.layout.model_chat_org_lain, null, false);

            TextView pesanChat= view.findViewById(R.id.chat_teks);
            TextView waktuChat= view.findViewById(R.id.chat_waktu);

            pesanChat.setText(pesan[position]);
            waktuChat.setText(pukul[position]);

            if(diriSendiri[position]== Chat.DIRI_SENDIRI)
                pesanChat.setTextColor(Color.parseColor("#FFFFFF"));

            return view;
        }
    }
}