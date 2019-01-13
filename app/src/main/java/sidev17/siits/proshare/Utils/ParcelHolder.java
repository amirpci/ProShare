package sidev17.siits.proshare.Utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ParcelHolder<Tipe> implements Parcelable {

    private Tipe objList[];

    public ParcelHolder(Tipe[] objList){
        this.objList= objList;
    } public ParcelHolder(Tipe obj){
        objList= (Tipe[]) new Object[0];
        tambah(obj);
    } public ParcelHolder(){
        objList= (Tipe[]) new Object[0];
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeArray(objList);
    }

    public static final Parcelable.Creator<ParcelHolder> CREATOR = new Parcelable.Creator<ParcelHolder>(){
        @Override
        public ParcelHolder createFromParcel(Parcel source) {
            return new ParcelHolder(source);
        }

        @Override
        public ParcelHolder[] newArray(int size) {
            return new ParcelHolder[size];
        }
    };


    public void tambah(Tipe obj){
        objList= ArrayMod.tambahArray(objList, obj);
    }
    public void hapus(Tipe obj){
        objList= ArrayMod.kurangiArray(objList, obj);
    } public void hapus(int ind){
        objList= ArrayMod.kurangiArray(objList, ind);
    }
    public void atur(int ind, Tipe obj){
        objList[ind]= obj;
    }

    public Tipe ambil(){
        return objList[objList.length-1];
    }
    public Tipe ambil(int ind){
        if(ind >= objList.length)
            ind= objList.length-1;
        return objList[ind];
    }

    public Tipe[] ambilArray(){
        return objList;
    }
    public ArrayList<Tipe> ambilList(){
        ArrayList<Tipe> list= new ArrayList<Tipe>();
        for(int i= 0; i<ukuran(); i++)
            list.add(objList[i]);
        return list;
    }

    public int ukuran(){
        return objList.length;
    }

}
