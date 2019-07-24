package sidev17.siits.procks.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import sidev17.siits.procks.R;
import sidev17.siits.procks.Utils.Utilities;

public class RcLampiran extends RecyclerView.Adapter<RcLampiran.vHolder> {
    private ArrayList<String> urlFoto, urlVideo;
    private int ukuran, batasVideo, padding;
    private final int UTAMA = 323;
    private final int SEPARATOR = 232;
    private Activity act;
    public RcLampiran(ArrayList<String> urlFoto, ArrayList<String> urlVideo , int ukuran, int batasVideo, Activity act){
        this.urlFoto = urlFoto;
        this.urlVideo = urlVideo;
        this.ukuran = ukuran;
        this.batasVideo = batasVideo;
        this.act = act;
        padding = ukuran/3;
    }
    @NonNull
    @Override
    public vHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==UTAMA)
            return new vHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lampiran_more, parent, false));
        return new vHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lampiran_more_separator, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull vHolder holder, int position) {
        if(position%2==0)
            holder.aturView(position/2+1);
    }

    @Override
    public int getItemCount() {
        return urlFoto.size()*2-3;
    }

    @Override
    public int getItemViewType(int position) {
        if(position%2==0)
            return UTAMA;
        else
            return SEPARATOR;
    }

    public class vHolder extends RecyclerView.ViewHolder{
        ImageView gambar;
        public vHolder(View itemView) {
            super(itemView);
            gambar = itemView.findViewById(R.id.lampiran_pertanyaan_gambar);
        }
        public void aturView(int posisi){
            gambar.getLayoutParams().height = ukuran;
            gambar.getLayoutParams().width = ukuran;
            if(posisi>=batasVideo - 1 || batasVideo == -1){
                gambar.setBackgroundColor(act.getResources().getColor(R.color.abuLebihTua));
                gambar.setPadding(padding, padding, padding, padding);
                gambar.setImageDrawable(act.getResources().getDrawable(R.drawable.obj_indek_video_lingkaran));
            }else
                Utilities.setFotoDariUrlSingle(urlFoto.get(posisi), gambar, ukuran);
        }
    }
}
