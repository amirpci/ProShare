package sidev17.siits.proshare.Modul;

import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.UtteranceProgressListener;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.rmtheis.yandtran.language.Language;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sidev17.siits.proshare.Adapter.ChatAdapter;
import sidev17.siits.proshare.Model.ChatListItem;
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

    String emailKita;
    Pengguna orang, orangKita;
    Chat chat;
    ChatAdapter adapter;
    ArrayList<ChatPesanItem> listPesan;
    String pesan[]= {"Assalamualaikum", "Waalaikumussalam", "Perkenalkan, saya Makan", "Mhs Sistem Informasi, ITS", "Iya mas Amir", "Saya ingin bertanya", "Gak jadi pak", "Makasih :)", "Lhe... -_-"};
    String pukul[]= {"10:11", "10:30", "10:31", "10:31", "10:41", "10:41", "10:41", "10:41", "12:21"};
    int diriSendiri[]= {Chat.DIRI_SENDIRI, Chat.ORANG_LAIN, Chat.DIRI_SENDIRI, Chat.DIRI_SENDIRI, Chat.ORANG_LAIN, Chat.DIRI_SENDIRI, Chat.DIRI_SENDIRI, Chat.DIRI_SENDIRI, Chat.ORANG_LAIN};

    final String fileInternal= Environment.getExternalStorageDirectory() +"/ProShare/Chat/data";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        orang = (Pengguna)getIntent().getSerializableExtra("pengguna");
        orangKita = new Pengguna();
        orangKita.setEmail(Utilities.getUserID(this));
        orangKita.setNama(Utilities.getUserNama(this));
        orangKita.setBidang(Utilities.getUserMajor(this));
        orangKita.setStatus(Utilities.getUserBidang(this));
        orangKita.setNegara(Utilities.getUserNegara(this));

        fotoOrgTujuan= findViewById(R.id.chat_bar_status_foto);
        namaOrgTujuan= findViewById(R.id.chat_bar_status_nama);
        bidangOrgTujuan= findViewById(R.id.chat_bar_status_major);


        barIsi= findViewById(R.id.chat_bar_isi);
        barIsi.addView(getLayoutInflater().inflate(R.layout.model_tab_edit_text, null, false));
        tmbSisipkan= barIsi.findViewById(R.id.tab_text_indikator_gambar);
        tmbKirim= barIsi.findViewById(R.id.tab_text_tindakan);
        iconKirim= barIsi.findViewById(R.id.tab_text_tindakan_gambar);
        isiPesan= barIsi.findViewById(R.id.tab_text_hint);

        wadahChat= findViewById(R.id.chat_wadah);
        muatChat();
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


    void aturInfoOrg(){
        namaOrgTujuan.setText(orang.getNama());
        Utilities.loadBidang(orang.getBidang(), Utilities.getUserNegara(this), bidangOrgTujuan, this);
        if(orang.getPhotoProfile()!=null){
            Glide.with(getApplicationContext()).load(orang.getPhotoProfile()).into(fotoOrgTujuan);
        }
    }

    void muatChat(){
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(true);
        wadahChat.setLayoutManager(linearLayoutManager);
        bacaPesan(orangKita.getEmail(), orang.getEmail());

        tmbKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimPesan(orangKita.getEmail(), orang.getEmail(), isiPesan.getText().toString());
            }
        });
    }

    private void kirimPesan(final String pengirim, final String penerima, final String pesan){
        String id_pesan = Utilities.getUid();
        ChatPesanItem itempesan = new ChatPesanItem();
        itempesan.setPengirim(pengirim);
        itempesan.setPenerima(penerima);
        itempesan.setPesan(pesan);
        itempesan.setWaktu(Jam());
        itempesan.setId(id_pesan);
        Utilities.getChatRef().child(id_pesan).setValue(itempesan)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                         //   bacaPesan(pengirim, penerima);
                            final Map<String, String> waktu = ServerValue.TIMESTAMP;
                            ChatListItem chatListItem = new ChatListItem();
                            chatListItem.setOrang(orang);
                            chatListItem.setPrewMessage(pesan);
                            chatListItem.setNama(orang.getNama());
                            chatListItem.setJam(0000000);
                            ChatListItem chatListItemOrang = new ChatListItem();
                            chatListItemOrang.setOrang(orangKita);
                            chatListItemOrang.setPrewMessage(pesan);
                            chatListItemOrang.setNama(orangKita.getNama());
                            chatListItemOrang.setJam(0000000);
                            final DatabaseReference refListChatJam = Utilities.getChatListRef(ChatActivity.this).child(orangKita.getEmail().replace(".",",")).child(orang.getEmail().replace(".",",")).child("jam");
                            final DatabaseReference refListChat = Utilities.getChatListRef(ChatActivity.this).child(orangKita.getEmail().replace(".",",")).child(orang.getEmail().replace(".",","));
                            final DatabaseReference refListChatJamOrang = Utilities.getChatListRef(ChatActivity.this).child(orang.getEmail().replace(".",",")).child(orangKita.getEmail().replace(".",",")).child("jam");
                            final DatabaseReference refListChatOrang = Utilities.getChatListRef(ChatActivity.this).child(orang.getEmail().replace(".",",")).child(orangKita.getEmail().replace(".",","));
                            refListChat.setValue(chatListItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        refListChatJam.setValue(waktu);
                                    }
                                }
                            });
                            refListChatOrang.setValue(chatListItemOrang).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        refListChatJamOrang.setValue(waktu);
                                    }
                                }
                            });
                            isiPesan.setText("");
                        }else{
                            Toast.makeText(ChatActivity.this, "Failed to send message!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void bacaPesan(final String pengirim, final String penerima){
        listPesan = new ArrayList<>();
        Utilities.getChatRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPesan.clear();
                int count = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatPesanItem chat = snapshot.getValue(ChatPesanItem.class);
                    if((chat.getPenerima().equals(penerima) && chat.getPengirim().equals(pengirim)) ||
                            (chat.getPenerima().equals(pengirim) && chat.getPengirim().equals(penerima))){
                        listPesan.add(chat);
                    }
                }
                adapter = new ChatAdapter(ChatActivity.this, listPesan);
                wadahChat.setAdapter(adapter);
                wadahChat.scrollToPosition(adapter.getItemCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
}