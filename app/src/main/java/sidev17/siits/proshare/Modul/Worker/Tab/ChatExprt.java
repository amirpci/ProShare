package sidev17.siits.proshare.Modul.Worker.Tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import sidev17.siits.proshare.Model.ChatListItem;
import sidev17.siits.proshare.Modul.ChatActivity;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Utilities;

public class ChatExprt extends Fragment {
    private RecyclerView wadahFeedback;
    private FirebaseRecyclerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_exprt, container, false);
        wadahFeedback= (RecyclerView)view.findViewById(R.id.feedback_wadah);
        initDaftarChat();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        wadahFeedback.setLayoutManager(linearLayoutManager);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        wadahFeedback.setAdapter(adapter);
        return view;
    }

    void initDaftarChat(){
        FirebaseRecyclerOptions<ChatListItem> options =
                new FirebaseRecyclerOptions.Builder<ChatListItem>()
                        .setQuery(Utilities.getChatListRef(getActivity()).orderByChild("jam"), ChatListItem.class)
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
    public class vhChatList extends RecyclerView.ViewHolder{
        private TextView prevChat, nama, waktu;
        private ImageView centang;
        private CircleImageView foto;
        private View view;
        public vhChatList(View itemView) {
            super(itemView);
            view = itemView;
            prevChat = (TextView)itemView.findViewById(R.id.feedback_orang_chat);
            nama = (TextView)itemView.findViewById(R.id.feedback_orang_nama);
            waktu = (TextView)itemView.findViewById(R.id.feedback_waktu);
            centang = (ImageView)itemView.findViewById(R.id.feedback_orang_centang);
            foto = (CircleImageView)itemView.findViewById(R.id.feedback_orang_gambar);
        }

        public void initList(final ChatListItem listPesan) {
            //Data Untuk orang
            if(listPesan.getOrang().getStatus()==201){
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
            }else if(listPesan.getOrang().getStatus()==202){
                centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);
            }
            String delegate = "hh:mm aaa";
            prevChat.setText(listPesan.getPrewMessage());
            nama.setText(listPesan.getNama());
            waktu.setText(String.valueOf(DateFormat.format(delegate,listPesan.getJam())));
            if(listPesan.getOrang().getPhotoProfile()!=null){
                foto.setPadding(0,0,0,0);
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
        }
    }
    private String Jam() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
    }
}