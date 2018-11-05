package sidev17.siits.proshare.Modul;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.rmtheis.yandtran.language.Language;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sidev17.siits.proshare.Model.ChatPesanItem;
import sidev17.siits.proshare.Model.Pengguna;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Chat;
import sidev17.siits.proshare.Utils.Utilities;

public class ChatActivity extends AppCompatActivity{
    CircleImageView fotoOrgTujuan;
    TextView namaOrgTujuan;
    TextView bidangOrgTujuan;
    RecyclerView wadahChat;

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
        muatChat(getIntent().getStringExtra("idPesan"));
        /*ChatAdapter adp= new ChatAdapter();
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
        */

     //   inisiasiChat();
//        ambilChatSemua();
        aturInfoOrg();

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

   /* void muatUlangAdapter(){
        ChatAdapter adp= new ChatAdapter();
        int posisi= wadahChat.getFirstVisiblePosition();
        wadahChat.setAdapter(adp);
        wadahChat.setSelection(posisi);
    } */

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

   /* void aturFotoOrg(){
        Bitmap foto= chat.ambilFotoOrg();
        fotoOrgTujuan.setImageBitmap(chat.ambilFotoOrg());
        if(foto== null) {
            fotoOrgTujuan.setImageResource(R.drawable.obj_profilephoto);
            fotoOrgTujuan.setColorFilter(getResources().getColor(R.color.abuLebihTua));
        }
    } */
    void aturInfoOrg(){
        Pengguna orang = (Pengguna) getIntent().getSerializableExtra("pengguna");
        namaOrgTujuan.setText(orang.getNama());
        Language languageID=null;
        switch (orang.getNegara()){
            case "Indonesia" : languageID=Language.INDONESIAN; break;
            case "United States" : languageID=Language.ENGLISH; break;
            case "United Kingdom" : languageID=Language.ENGLISH; break;
            case "Japan" : languageID=Language.JAPANESE; break;
        }
        com.rmtheis.yandtran.translate.Translate.setKey(getString(R.string.yandex_api_key));
        try {
            bidangOrgTujuan.setText(com.rmtheis.yandtran.translate.Translate.execute(orang.getBidang(), Language.ENGLISH, languageID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(orang.getPhotoProfile()!=null){
            Glide.with(getApplicationContext()).load(orang.getPhotoProfile()).into(fotoOrgTujuan);
        }
    }

    void muatChat(String namaOrang){
        final String idPesan = getIntent().getStringExtra("idPesan");
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(true);
        wadahChat.setLayoutManager(linearLayoutManager);
        FirebaseRecyclerOptions<ChatPesanItem> options =
                new FirebaseRecyclerOptions.Builder<ChatPesanItem>()
                        .setQuery(Utilities.getChatRoomRef(namaOrang, ChatActivity.this), ChatPesanItem.class)
                        .build();
        // RC_FirebaseChat adapter = new RC_FirebaseChat(options, getApplicationContext());
        final FirebaseRecyclerAdapter rcAdapter = new FirebaseRecyclerAdapter<ChatPesanItem, VhChatRoom>(options) {

            @NonNull
            @Override
            protected void onBindViewHolder(@NonNull VhChatRoom holder, int position, @NonNull ChatPesanItem model) {
                holder.siapkanItem(model);
            }

            @NonNull
            @Override
            public VhChatRoom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new VhChatRoom(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_chat, parent, false));
            }
        };
        rcAdapter.startListening();
        rcAdapter.notifyDataSetChanged();
        wadahChat.setAdapter(rcAdapter);
        tmbKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String waktuJam = Jam();
                final Pengguna orang = (Pengguna) getIntent().getSerializableExtra("pengguna");
                final String isi_pesan = isiPesan.getText().toString();
                final String idpesan = getIntent().getStringExtra("idPesan");
                String uid = Utilities.getUid();
         //       final DatabaseReference refPesanListItem = Utilities.getChatListRef(ChatActivity.this).child(getIntent().getStringExtra("idPesan"));
           //     final DatabaseReference refPesanListItemOrang = Utilities.getChatListRef(ChatActivity.this).child(getIntent().getStringExtra("idPesanOrang"));
             //   final ChatListItem listItem = new ChatListItem();
               // listItem.setNama(orang.getNama());
               // listItem.setJam(waktuJam);
              //  Toast.makeText(ChatActivity.this, , Toast.LENGTH_SHORT).show();
               // Toast.makeText(ChatActivity.this, Utilities.getUserID(getApplicationContext()), Toast.LENGTH_SHORT).show();
               // final String idpesanBaruOrang = Utilities.getUid()+" "+Utilities.getUserID(getApplication());
                //final String idpesanBaru = Utilities.getUid()+" "+idpesan;

                final Map<String, String> waktu = ServerValue.TIMESTAMP;
                final ChatPesanItem pesan = new ChatPesanItem();
                pesan.setPengirim(Utilities.getUserID(ChatActivity.this));
                pesan.setPesan(isi_pesan);
                pesan.setWaktu(waktuJam);
                final DatabaseReference reference = Utilities.getChatRoomRef(idpesan,ChatActivity.this).child(uid);
                final DatabaseReference referenceOrang = FirebaseDatabase.getInstance().getReference("Chat Room/" +orang.getEmail().replace(".",",")+"/"+idpesan).child(uid);
                final DatabaseReference refListChatJam = Utilities.getChatListRef(ChatActivity.this).child(idPesan).child("jam");
                final DatabaseReference refListChatprewMessage = Utilities.getChatListRef(ChatActivity.this).child(idPesan).child("prewMessage");
                final DatabaseReference refListChatJamOrang = Utilities.getChatListRef(ChatActivity.this).child(idPesan).child("jam");
                final DatabaseReference refListChatprewMessageOrang = Utilities.getChatListRef(ChatActivity.this).child(idPesan).child("prewMessage");
                reference.setValue(pesan).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        wadahChat.scrollToPosition(rcAdapter.getItemCount() - 1);
                        refListChatprewMessage.setValue(isi_pesan);
                        refListChatJam.setValue(waktu).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    referenceOrang.setValue(pesan);
                                }
                            }
                        });
                    }
                });
                isiPesan.setText("");
            }
        });

    }

    String tanggal(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c);
    }
    private String Jam() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
    }

    public class VhChatRoom extends RecyclerView.ViewHolder{
        private RelativeLayout chatOrang, chatKita;
        private TextView waktu_orang, waktu_kita, pesan_orang, pesan_kita;
        public VhChatRoom(View itemView) {
            super(itemView);
            chatKita = (RelativeLayout)itemView.findViewById(R.id.chat_kita);
            chatOrang = (RelativeLayout)itemView.findViewById(R.id.chat_orang);
            waktu_kita = (TextView) itemView.findViewById(R.id.chat_waktu_kita);
            waktu_orang = (TextView) itemView.findViewById(R.id.chat_waktu_orang);
            pesan_kita = (TextView) itemView.findViewById(R.id.chat_teks_kita);
            pesan_orang = (TextView) itemView.findViewById(R.id.chat_teks_orang);

        }

        public void siapkanItem(ChatPesanItem pesanItem) {
            if(pesanItem.getPengirim().equals(Utilities.getUserID(getApplicationContext()))){
                chatKita.setVisibility(View.VISIBLE);
                waktu_kita.setText(pesanItem.getWaktu());
                pesan_kita.setText(pesanItem.getPesan());
            }else{
                chatOrang.setVisibility(View.VISIBLE);
                waktu_orang.setText(pesanItem.getWaktu());
                pesan_orang.setText(pesanItem.getPesan());
            }
        }
    }
    /*void aturBidangOrg(){
        bidangOrgTujuan.setText("Material Engineer"); //setText(chat.ambilBidangOrg());
    }*/

   /* private class ChatAdapter extends BaseAdapter{
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
    } */
}