package sidev17.siits.procks.Modul.Expert.Tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import sidev17.siits.procks.Model.ChatListItem;
import sidev17.siits.procks.Model.Pengguna;
import sidev17.siits.procks.Modul.ChatActivity;
import sidev17.siits.procks.Utils.ViewTool.Fragment_Header;
import sidev17.siits.procks.Utils.ViewTool.MainAct_Header;
import sidev17.siits.procks.Modul.TemanDaftarAct;
import sidev17.siits.procks.R;
import sidev17.siits.procks.Utils.Utilities;

public class FeedbackActExprt extends Fragment_Header {
/*
    public static final int PENGGUNA_EXPERT_TERVERIFIKASI= 202;
    public static final int PENGGUNA_EXPERT= 201;
    public static final int PENGGUNA_BIASA= 200;
*/

    public final int PENGGUNA_EXPERT_TERVERIFIKASI = 202;
    public final int PENGGUNA_EXPERT = 201;
    public final int PENGGUNA_BIASA = 200;
    private FirebaseRecyclerAdapter adapter;
    //FORMAT KONSTANTA INT:
    // xxx >> digit 1: 1,2
    //DATA= 1
    //DATA= 2

    private String orang[]= {"Mr. A", "Mr. B", "Mrs. C", "Will Smith"};
//    private String chat[]= {"Psychiatrist", "Motivator", "Engineer", "Actor"};
    private int kategoriExpert[]= {Pengguna.Status.PENGGUNA_EXPERT_TERVERIFIKASI, Pengguna.Status.PENGGUNA_EXPERT,
        Pengguna.Status.PENGGUNA_BIASA, Pengguna.Status.PENGGUNA_EXPERT_TERVERIFIKASI};

    //    private String judulSolusi[]= {"What should I do whe this happen?", "How to gain inspiration?", "How to else?", "Stay cool"};
    private String feedback[][] = {{"I do this everyday, but somehow..."}, {"When it happens, I don't know what to do. I need inspiration."}, {"bla bla bla..."}, {"Just bel cool bro!"}};

    private RecyclerView wadahFeedback;
//    private ImageView mulaiChat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beranda_chat_exprt, container, false);
        wadahFeedback = (RecyclerView) view.findViewById(R.id.feedback_wadah);
//        mulaiChat = (ImageView) view.findViewById(R.id.daftar_pertanyaan_tambah);
        initDaftarChat();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        wadahFeedback.setLayoutManager(linearLayoutManager);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        wadahFeedback.setAdapter(adapter);
        // AdapterFeedback adpFeedback= new AdapterFeedback();
        //  wadahFeedback.setAdapter(adpFeedback);
        daftarkanAksiTombolUtama(new MainAct_Header.AksiTombolUtama() {
            @Override
            public void tekan(View v, int halaman) {
                Intent keDaftarTeman= new Intent(getContext(), TemanDaftarAct.class);
                startActivity(keDaftarTeman);
            }
        });
/*
        mulaiChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keDaftarTeman= new Intent(getContext(), TemanDaftarAct.class);

                startActivity(keDaftarTeman);
/*
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_mulai_chatt);
                RelativeLayout next = (RelativeLayout) dialog.findViewById(R.id.mulai_chat);
                final EditText email = (EditText) dialog.findViewById(R.id.et_email_orang);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
/*
============================
NANTI DIPAKE DI TemanTambahAct
============================
                        Utilities.getUserRef().child(email.getText().toString().replace(".", ",")).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Pengguna orang = dataSnapshot.getValue(Pengguna.class);
                                Intent i = new Intent(getActivity(), ChatActivity.class);
                                //i.putExtra("idPesan", listPesan.getIdPesan());
                                i.putExtra("pengguna", orang);
                                Toast.makeText(getActivity(), orang.getNama(), Toast.LENGTH_SHORT).show();
                                startActivity(i);
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
* /
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
* /
            }

        });
*/
//        initHeader();
        judulHeader= "Chat";
        return view;
    }

    @Override
    public void initHeader() {
//        Toast.makeText(actInduk, "INIT!!!", Toast.LENGTH_SHORT).show();
//        MainAct_Header mainAct= (MainAct_Header) actInduk;
//        actInduk.aturJudulHeader("Chat");
        actInduk.aturGambarOpsiHeader_Null(0);
//        mainAct.aturTambahanHeader("(" +adapter.getItemCount() +")");
//        int resId[]= {};
    }

    void initDaftarChat() {
        FirebaseRecyclerOptions<ChatListItem> options =
                new FirebaseRecyclerOptions.Builder<ChatListItem>()
                        .setQuery(Utilities.getChatListRef(getActivity()).child(Utilities.getUserID(getActivity()).replace(".", ",")).orderByChild("jam"), ChatListItem.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<ChatListItem, vhChatList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull vhChatList holder, int position, @NonNull ChatListItem model) {
                holder.initList(model);
            }

            @NonNull
            @Override
            public vhChatList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.model_kolom_chat, parent, false);

                return new vhChatList(view);
            }
        };
    }

    public class vhChatList extends RecyclerView.ViewHolder {
        private TextView prevChat, nama, waktu;
        private ImageView centang;
        private CircleImageView foto;
        private View view;

        public vhChatList(View itemView) {
            super(itemView);
            view = itemView;
            prevChat = (TextView) itemView.findViewById(R.id.feedback_orang_chat);
            nama = (TextView) itemView.findViewById(R.id.feedback_orang_nama);
            waktu = (TextView) itemView.findViewById(R.id.feedback_waktu);
            centang = (ImageView) itemView.findViewById(R.id.tl_orang_centang);
            foto = (CircleImageView) itemView.findViewById(R.id.tl_orang_gambar);
        }

        public void initList(final ChatListItem listPesan) {
            //Data Untuk orang

/*
            if(listPesan.getOrang().getStatus() == Pengguna.Status.PENGGUNA_EXPERT){
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
            }else if(listPesan.getOrang().getStatus() == Pengguna.Status.PENGGUNA_EXPERT_TERVERIFIKASI){
=======
            if (listPesan.getOrang().getStatus() == 201) {
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
            } else if (listPesan.getOrang().getStatus() == 202) {
>>>>>>> Stashed changes
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);
            }
*/
            Pengguna.Status.pasangIndikatorStatus(centang, (int) listPesan.getOrang().getStatus());
            String delegate = "hh:mm aaa";
            prevChat.setText(listPesan.getPrewMessage());
            nama.setText(listPesan.getNama());
            waktu.setText(String.valueOf(DateFormat.format(delegate, listPesan.getJam())));
            if (listPesan.getOrang().getPhotoProfile() != null) {
                foto.setPadding(0, 0, 0, 0);
                Glide.with(getActivity()).load(listPesan.getOrang().getPhotoProfile()).into(foto);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), ChatActivity.class);
                    i.putExtra("idPesan", listPesan.getIdPesan());
                    i.putExtra("pengguna", listPesan.getOrang());
                    startActivity(i);
                }
            });
            /*
            final Terjemahan terjemahan = new Terjemahan(getActivity());
            int responTerjemahan = terjemahan.terjemahkanDaftarChat(view, listPesan, Utilities.getUserBahasa(getActivity()), Utilities.getUserID(getActivity()));
            if (responTerjemahan == Terjemahan.TerjemahanDbHelper.TERJEMAHAN_TIDAK_DITEMUKAN) {
                new AsyncTask<String, Void, String>() {

                    @Override
                    protected String doInBackground(String... strings) {
                        String output = strings[0];
                        String bahasaDeteksi = Utilities.deteksiBahasa(output, getActivity());
                        String bahasaKita = Utilities.getUserBahasa(getActivity());
                        Log.d("Bahasa", bahasaDeteksi + " " + bahasaKita);
                        if (!bahasaDeteksi.equalsIgnoreCase(bahasaKita))
                            output = Terjemahan.terjemahkan(output, bahasaDeteksi, bahasaKita);
                        else output = Terjemahan.TerjemahanDbHelper.TERJEMAHAN_TIDAK_PERLU;
                        return output;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if (!s.equalsIgnoreCase(Terjemahan.TerjemahanDbHelper.TERJEMAHAN_TIDAK_PERLU) && !s.equalsIgnoreCase(listPesan.getPrewMessage())) {
                            prevChat.setText(s);
                            if (getActivity() != null)
                                terjemahan.simpanTerjemahanDaftarChat(listPesan, s, Utilities.getUserBahasa(getActivity()), Utilities.getUserID(getActivity()));
                        } else {
                            if (getActivity() != null)
                                terjemahan.simpanTerjemahanDaftarChat(listPesan, listPesan.getPrewMessage(), Utilities.getUserBahasa(getActivity()), Utilities.getUserID(getActivity()));
                        }
                    }
                }.execute(listPesan.getPrewMessage());
            } else if (responTerjemahan == Terjemahan.TerjemahanDbHelper.TERJEMAHAN_PERLU_DIPERBARUI) {
                new AsyncTask<String, Void, String>() {

                    @Override
                    protected String doInBackground(String... strings) {
                        String output = strings[0];
                        String bahasaDeteksi = Utilities.deteksiBahasa(output, getActivity());
                        String bahasaKita = Utilities.getUserBahasa(getActivity());
                        Log.d("Bahasa", bahasaDeteksi + " " + bahasaKita);
                        if (!bahasaDeteksi.equalsIgnoreCase(bahasaKita))
                            output = Terjemahan.terjemahkan(output, bahasaDeteksi, bahasaKita);
                        else output = Terjemahan.TerjemahanDbHelper.TERJEMAHAN_TIDAK_PERLU;
                        return output;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if (!s.equalsIgnoreCase(Terjemahan.TerjemahanDbHelper.TERJEMAHAN_TIDAK_PERLU) && !s.equalsIgnoreCase(listPesan.getPrewMessage())) {
                            prevChat.setText(s);
                            if (getActivity() != null)
                                terjemahan.perbaruiTerjemahanDaftarChat(Utilities.getUserID(getActivity()), listPesan.getOrang().getEmail(), s, Utilities.getUserBahasa(getActivity()));
                        } else {
                            if (getActivity() != null)
                                terjemahan.simpanTerjemahanDaftarChat(listPesan, listPesan.getPrewMessage(), Utilities.getUserBahasa(getActivity()), Utilities.getUserID(getActivity()));
                        }
                    }
                }.execute(listPesan.getPrewMessage());
            } else if (responTerjemahan == Terjemahan.TerjemahanDbHelper.TERJEMAHAN_DITEMUKAN) ;
            */
        }

    }

    private String Jam() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
    }
}
