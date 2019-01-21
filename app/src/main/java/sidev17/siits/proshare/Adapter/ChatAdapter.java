package sidev17.siits.proshare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sidev17.siits.proshare.Model.ChatPesanItem;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Utilities;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VhChatRoom> {
    private static final int CHAT_KITA = 2223;
    private static final int CHAT_ORANG = 3332;
    private Context c;
    private ArrayList<ChatPesanItem> chat;
    public ChatAdapter(Context c, ArrayList<ChatPesanItem> chat){
        this.c = c;
        this.chat = chat;
    }
    @NonNull
    @Override
    public VhChatRoom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_chat_diri_sendiri, parent, false);
        if(viewType == CHAT_KITA){
            return new VhKita(v);
        }else if(viewType == CHAT_ORANG){
            return new VhOrang(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_chat_org_lain, parent, false));
        }
        return new VhChatRoom(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VhChatRoom holder, int position) {
        holder.siapkanItem(chat.get(position));
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    @Override
    public int getItemViewType(int position) {
       // return super.getItemViewType(position);
        String email = Utilities.getUserNow().getEmail();
        if(chat.get(position).getPengirim().equals(email)){
            return CHAT_KITA;
        }else{
            return CHAT_ORANG;
        }
    }

    public class VhKita extends VhChatRoom{

        public VhKita(View itemView) {
            super(itemView);
        }
    }

    public class VhOrang extends VhChatRoom{

        public VhOrang(View itemView) {
            super(itemView);
        }
    }

    public class VhChatRoom extends RecyclerView.ViewHolder{
        private TextView waktu, pesan;
        public VhChatRoom(View itemView) {
            super(itemView);
            waktu = itemView.findViewById(R.id.chat_waktu);
            pesan = itemView.findViewById(R.id.chat_teks);
        }

        public void siapkanItem(ChatPesanItem pesanItem) {
            waktu.setText(pesanItem.getWaktu());
            pesan.setText(pesanItem.getPesan());
        }
    }
}
