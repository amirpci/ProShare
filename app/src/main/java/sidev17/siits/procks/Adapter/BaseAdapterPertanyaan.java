package sidev17.siits.procks.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BaseAdapterPertanyaan extends BaseAdapter {

    String judul[];
    String deskripsi[];

    int idHalaman;
    int idJudul;
    int idDesc;
    LayoutInflater inflater;

    public BaseAdapterPertanyaan(int idHal, int idJudul, int idDesc, String judul[], String deskripsi[]){
        this.idJudul= idJudul;
        this.idDesc= idDesc;
        idHalaman= idHal;
        this.judul= judul;
        this.deskripsi= deskripsi;
    }

    public void setInflater(LayoutInflater inflater){
        this.inflater= inflater;
    }

    @Override
    public int getCount() {
        return judul.length;
    }

    @Override
    public Object getItem(int position) {
        return judul[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= inflater.inflate(idHalaman, parent, false);

        TextView teksJudul= view.findViewById(idJudul);
        TextView teksDesc= view.findViewById(idDesc);

        teksJudul.setText(judul[position]);
        teksDesc.setText(deskripsi[position]);

        return view;
    }
}
