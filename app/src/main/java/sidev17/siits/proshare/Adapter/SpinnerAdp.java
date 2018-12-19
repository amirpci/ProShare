package sidev17.siits.proshare.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sidev17.siits.proshare.Model.Bidang;
import sidev17.siits.proshare.Utils.Ukuran;

public class SpinnerAdp extends BaseAdapter {

    int jmlElemen;
    ArrayList<Bidang> elemen;
    Context c;

    public SpinnerAdp(ArrayList<Bidang> elemen, Context c ){
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
        ViewGroup.LayoutParams lpElemen= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Ukuran.DpKePx(30, c.getResources().getDisplayMetrics()));

        TextView teksElemen= new TextView(c);
        teksElemen.setGravity(Gravity.LEFT);
        teksElemen.setTextColor(Color.parseColor("#000000"));
        teksElemen.setTextSize(19);
        teksElemen.setPadding(15,0,15,0);
        teksElemen.setText(elemen.get(position).getBidang());

        teksElemen.setLayoutParams(lpElemen);

        return teksElemen;
    }
}