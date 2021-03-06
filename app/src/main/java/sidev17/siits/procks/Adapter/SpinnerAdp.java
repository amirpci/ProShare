package sidev17.siits.procks.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sidev17.siits.procks.Model.Bidang;

public class SpinnerAdp extends BaseAdapter {

    int jmlElemen;
    ArrayList<Bidang> elemen;
    Context c;
    int warnaTeks;

    public SpinnerAdp(ArrayList<Bidang> elemen, Context c, int warnaTeks){
        this.warnaTeks= warnaTeks;
        this.elemen = elemen;
        jmlElemen = elemen.size();
        this.c = c;
    }

    @Override
    public int getCount() {
        return jmlElemen;
    }

    @Override
    public Object getItem(int position) {
        return elemen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewGroup.LayoutParams lpElemen= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT/*Ukuran.DpKePx(30, c.getResources().getDisplayMetrics())*/);

        TextView teksElemen= new TextView(c);
        teksElemen.setGravity(Gravity.LEFT);
        teksElemen.setTextColor(warnaTeks);
        teksElemen.setTextSize(19);
        teksElemen.setPadding(15,0,15,0);
        teksElemen.setText(elemen.get(position).getBidang());

        teksElemen.setLayoutParams(lpElemen);

        return teksElemen;
    }
}