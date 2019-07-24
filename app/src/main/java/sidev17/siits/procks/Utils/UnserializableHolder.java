package sidev17.siits.procks.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class UnserializableHolder<Tipe> implements Serializable {

    private ArrayList<Tipe> objList;

    public UnserializableHolder(ArrayList<Tipe> objList){
        this.objList= objList;
    } public UnserializableHolder(Tipe obj){
        objList= new ArrayList<Tipe>();
        objList.add(obj);
    } public UnserializableHolder(){
        objList= new ArrayList<Tipe>();
    }

    public void tambah(Tipe obj){
        objList.add(obj);
    }
    public void hapus(Tipe obj){
        objList.remove(obj);
    } public void hapus(int ind){
        objList.remove(ind);
    }
    public void atur(int ind, Tipe obj){
        objList.set(ind, obj);
    }

    public Tipe ambil(){
        return ambil(objList.size()-1);
    }
    public Tipe ambil(int ind){
        if(ind >= objList.size())
            ind= objList.size()-1;
        return objList.get(ind);
    }

    public ArrayList<Tipe> ambilList(){
        return objList;
    }

    public int ukuran(){
        return objList.size();
    }
}
