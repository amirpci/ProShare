package sidev17.siits.procks.Adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sidev17.siits.procks.Model.ChatPesanItem;
import sidev17.siits.procks.R;
import sidev17.siits.procks.Utils.Terjemahan;
import sidev17.siits.procks.Utils.Utilities;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VhChatRoom> {
    private static final int CHAT_KITA = 2223;
    private static final int CHAT_ORANG = 3332;
    private Activity c;
    private ArrayList<ChatPesanItem> chat;
    public ChatAdapter(Activity c, ArrayList<ChatPesanItem> chat){
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
    public void onBindViewHolder(@NonNull final VhChatRoom holder, final int position) {
        holder.siapkanItem(chat.get(position));
        final ViewTreeObserver observer = holder.latar.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(!holder.loaded[position]){
                    int lebarSekarang = holder.latar.getWidth();
                    if(lebarSekarang>holder.maxLebar)
                        holder.latar.getLayoutParams().width = holder.maxLebar;
                    holder.loaded[position] = true;
                }
            }
        });
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
        private TextView waktu, pesan, pesanDiterjemahkan;
        private RelativeLayout latar, latarTerjemahan;
        private int maxLebar, lebar;
        private boolean[] loaded;
        private View view;
        public VhChatRoom(View itemView) {
            super(itemView);
            view = itemView;
            waktu = itemView.findViewById(R.id.chat_waktu);
            pesan = itemView.findViewById(R.id.chat_teks);
            pesanDiterjemahkan = itemView.findViewById(R.id.chat_teks_translated);
            latarTerjemahan = itemView.findViewById(R.id.chat_latar_translated);
            latar = itemView.findViewById(R.id.chat_latar);
            loaded = new boolean[chat.size()];
            for(int i = 0; i<loaded.length; i++)
                loaded[i] = false;
        }

        public void siapkanItem(final ChatPesanItem pesanItem) {
            DisplayMetrics dm = new DisplayMetrics();
            c.getWindowManager().getDefaultDisplay().getMetrics(dm);
            lebar = dm.widthPixels;
            maxLebar = lebar - Utilities.dpToPx(100.0f, c);
            waktu.setText(pesanItem.getWaktu());
            pesan.setText(pesanItem.getPesan());
            if(!pesanItem.getPengirim().equals(Utilities.getUserNow().getEmail())){
                final Terjemahan terjemahan = new Terjemahan(c);
                int responTerjemahan = terjemahan.terjemahkanChat(view, pesanItem, Utilities.getUserBahasa(c));
                if(responTerjemahan==Terjemahan.TerjemahanDbHelper.TERJEMAHAN_TIDAK_DITEMUKAN){
                    new AsyncTask<String, Void, String>(){

                        @Override
                        protected String doInBackground(String... strings) {
                            String output = strings[0];
                            String bahasaDeteksi = Utilities.deteksiBahasa(output, c);
                            String bahasaKita = Utilities.getUserBahasa(c);
                            Log.d("Bahasa", bahasaDeteksi + " " + bahasaKita);
                            if(!bahasaDeteksi.equalsIgnoreCase(bahasaKita))
                                output = Terjemahan.terjemahkan(output, bahasaDeteksi,  bahasaKita);
                            else output = Terjemahan.TerjemahanDbHelper.TERJEMAHAN_TIDAK_PERLU;
                            return output;
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            if (!s.equalsIgnoreCase(Terjemahan.TerjemahanDbHelper.TERJEMAHAN_TIDAK_PERLU) && !s.equalsIgnoreCase(pesanItem.getPesan())) {
                                latarTerjemahan.setVisibility(View.VISIBLE);
                                pesanDiterjemahkan.setText(s);
                                terjemahan.simpanTerjemahanChat(pesanItem, s, Utilities.getUserBahasa(c));
                            } else {
                                latarTerjemahan.setVisibility(View.GONE);
                                terjemahan.simpanTerjemahanChat(pesanItem, pesanItem.getPesan(), Utilities.getUserBahasa(c));
                            }
                        }
                    }.execute(pesanItem.getPesan());
                }else if(responTerjemahan==Terjemahan.TerjemahanDbHelper.TERJEMAHAN_PERLU_DIPERBARUI){
                    new AsyncTask<String, Void, String>(){

                        @Override
                        protected String doInBackground(String... strings) {
                            String output = strings[0];
                            String bahasaDeteksi = Utilities.deteksiBahasa(output, c);
                            String bahasaKita = Utilities.getUserBahasa(c);
                            Log.d("Bahasa", bahasaDeteksi + " " + bahasaKita);
                            if(!bahasaDeteksi.equalsIgnoreCase(bahasaKita))
                               output = Terjemahan.terjemahkan(output, bahasaDeteksi,bahasaKita);
                            else output = Terjemahan.TerjemahanDbHelper.TERJEMAHAN_TIDAK_PERLU;
                            return output;
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            if(!s.equalsIgnoreCase(Terjemahan.TerjemahanDbHelper.TERJEMAHAN_TIDAK_PERLU) && !s.equalsIgnoreCase(pesanItem.getPesan())) {
                                latarTerjemahan.setVisibility(View.VISIBLE);
                                pesanDiterjemahkan.setText(s);
                                terjemahan.perbaruiTerjemahanChat(pesanItem.getId(), s, Utilities.getUserBahasa(c));
                            }else {
                                latarTerjemahan.setVisibility(View.GONE);
                                terjemahan.simpanTerjemahanChat(pesanItem, pesanItem.getPesan(), Utilities.getUserBahasa(c));
                            }
                        }
                    }.execute(pesanItem.getPesan());
                }else if(responTerjemahan==Terjemahan.TerjemahanDbHelper.TERJEMAHAN_DITEMUKAN);
            }
        }
    }
}
