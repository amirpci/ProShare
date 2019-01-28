package sidev17.siits.proshare.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Utilities;

public class RcLampiran extends RecyclerView.Adapter<RcLampiran.vHolder> {
    private ArrayList<String> url;
    private int ukuran;
    private final int UTAMA = 323;
    private final int SEPARATOR = 232;
    public RcLampiran(ArrayList<String> url, int ukuran){
        this.url = url;
        this.ukuran = ukuran;
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
        return url.size()*2-3;
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
            Utilities.setFotoDariUrlSingle(url.get(posisi), gambar, ukuran);
        }
    }
}
