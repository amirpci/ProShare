package sidev17.siits.proshare.Utils;

import android.view.View;

import java.util.Date;

public class ArrayMod {

    public static final int ARAH_MAJU= 12;
    public static final int ARAH_MUNDUR= 11;



    public static <Tipe> Tipe[] ubahArray(Tipe array[], int pjg){
        Tipe arraySalin[]= array;
        array= (Tipe[]) new Object[pjg];
        int batas= arraySalin.length;
        if(pjg < arraySalin.length)
            batas= pjg;
        System.arraycopy(arraySalin, 0, array, 0, batas);
/*
        for(int i= 0; i<batas; i++)
            array[i]= arraySalin[i];
*/
        return array;
    }
    public static <Tipe> Tipe[][] ubahArray(Tipe array[][], int pjg){
        Tipe arraySalin[][]= array;
        array= (Tipe[][]) new Object[pjg][];
        int batas= arraySalin.length;
        if(pjg < arraySalin.length)
            batas= pjg;
        System.arraycopy(arraySalin, 0, array, 0, batas);
/*
        for(int i= 0; i<batas; i++)
            array[i]= arraySalin[i];
*/
        return array;
    }

    public static <Tipe> Tipe[] tambahArray(Tipe array[], Tipe masuk){
        array= ubahArray(array, array.length+1);
        array[array.length-1]= masuk;
        return array;
    }
    public static <Tipe> Tipe[][] tambahArray(Tipe array[][], Tipe masuk[]){
        array= ubahArray(array, array.length+1);
        array[array.length-1]= masuk;
        return array;
    }
    public static <Tipe> Tipe[] kurangiArray(Tipe array[], Tipe keluar){
        int ind= cariIndDlmArray(array, keluar);
        return kurangiArray(array, ind);
    } public static <Tipe> Tipe[] kurangiArray(Tipe array[], int indKeluar){
        Tipe arraySalin[]= array;
        array= (Tipe[]) new Object[array.length-1];
        int hitung= 0;
        for(int i= 0; i<arraySalin.length; i++)
            if(i != indKeluar) {
                array[hitung]= arraySalin[i];
                hitung++;
            }
        return array;
    }
    public static <Tipe> Tipe[][] kurangiArray(Tipe array[][], int indKeluar){
        Tipe arraySalin[][]= array;
        array= (Tipe[][]) new Object[array.length-1][];
        int hitung= 0;
        for(int i= 0; i<arraySalin.length; i++)
            if(i != indKeluar) {
                array[hitung]= arraySalin[i];
                hitung++;
            }
        return array;
    }



    public static Date[] tambahArray(Date array[], Date masuk){
        array= ubahArray(array, array.length+1);
        array[array.length-1]= masuk;
        return array;
    }
    public static String[] tambahArray(String array[], String masuk){
        array= ubahArray(array, array.length+1);
        array[array.length-1]= masuk;
        return array;
    }
    public static String[][] tambahArray(String array[][], String masuk[]){
        array= ubahArray(array, array.length+1);
        array[array.length-1]= masuk;
        return array;
    }
    public static String[] kurangiArray(String array[], String keluar){
        int ind= cariIndDlmArray(array, keluar);
        return kurangiArray(array, ind);
    } public static String[] kurangiArray(String array[], int indKeluar){
        String arraySalin[]= array;
        array= new String[array.length-1];
        int hitung= 0;
        for(int i= 0; i<arraySalin.length; i++)
            if(i != indKeluar) {
                array[hitung]= arraySalin[i];
                hitung++;
            }
        return array;
    }
    public static String[][] kurangiArray(String array[][], int indKeluar){
        String arraySalin[][]= array;
        array= new String[array.length-1][];
        int hitung= 0;
        for(int i= 0; i<arraySalin.length; i++)
            if(i != indKeluar) {
                array[hitung]= arraySalin[i];
                hitung++;
            }
        return array;
    } public static boolean[] kurangiArray(boolean array[], int indKeluar){
        boolean arraySalin[]= array;
        array= new boolean[array.length-1];
        int hitung= 0;
        for(int i= 0; i<arraySalin.length; i++)
            if(i != indKeluar) {
                array[hitung]= arraySalin[i];
                hitung++;
            }
        return array;
    }
    public static Date[] ubahArray(Date array[], int pjg){
        Date arraySalin[]= array;
        array= new Date[pjg];
        int batas= arraySalin.length;
        if(pjg < arraySalin.length)
            batas= pjg;
        System.arraycopy(arraySalin, 0, array, 0, batas);
/*
        for(int i= 0; i<batas; i++)
            array[i]= arraySalin[i];
*/
        return array;
    }
    public static String[] ubahArray(String array[], int pjg){
        String arraySalin[]= array;
        array= new String[pjg];
        int batas= arraySalin.length;
        if(pjg < arraySalin.length)
            batas= pjg;
        System.arraycopy(arraySalin, 0, array, 0, batas);
/*
        for(int i= 0; i<batas; i++)
            array[i]= arraySalin[i];
*/
        return array;
    }
    public static String[][] ubahArray(String array[][], int pjg){
        String arraySalin[][]= array;
        array= new String[pjg][];
        int batas= arraySalin.length;
        if(pjg < arraySalin.length)
            batas= pjg;
        System.arraycopy(arraySalin, 0, array, 0, batas);
/*
        for(int i= 0; i<batas; i++)
            array[i]= arraySalin[i];
*/
        return array;
    }

    public static int[] tambahArray(int array[], int masuk){
        array= ubahArray(array, array.length+1);
        array[array.length-1]= masuk;
        return array;
    }
    public static int[][] tambahArray(int array[][], int masuk[]){
        array= ubahArray(array, array.length+1);
        array[array.length-1]= masuk;
        return array;
    }

    public static int[] kurangiArray(int array[], int keluar){
        int indKeluar= cariIndDlmArray(array, keluar);
        int arraySalin[]= array;
        array= new int[array.length-1];
        int hitung= 0;
        for(int i= 0; i<arraySalin.length; i++)
            if(i != indKeluar) {
                array[hitung]= arraySalin[i];
                hitung++;
            }
        return array;
    }
    public static int[][] kurangiArray(int array[][], int indKeluar){
        int arraySalin[][]= array;
        array= new int[array.length-1][];
        int hitung= 0;
        for(int i= 0; i<arraySalin.length; i++)
            if(i != indKeluar) {
                array[hitung]= arraySalin[i];
                hitung++;
            }
        return array;
    }

    public static int[] ubahArray(int array[], int pjg){
        int arraySalin[]= array;
        array= new int[pjg];
        int batas= arraySalin.length;
        if(pjg < arraySalin.length)
            batas= pjg;
        System.arraycopy(arraySalin, 0, array, 0, batas);
/*
        for(int i= 0; i<batas; i++)
            array[i]= arraySalin[i];
*/
        return array;
    }
    public static int[][] ubahArray(int array[][], int pjg){
        int arraySalin[][]= array;
        array= new int[pjg][];
        int batas= arraySalin.length;
        if(pjg < arraySalin.length)
            batas= pjg;
        System.arraycopy(arraySalin, 0, array, 0, batas);
/*
        for(int i= 0; i<batas; i++)
            array[i]= arraySalin[i];
*/
        return array;
    }

    public static int[] hapusArrayDepan(int array[], int pjgDihapus){
        int pjg= array.length -pjgDihapus;
        int arraySalin[]= array;
        array= new int[pjg];
        pjgDihapus-= 2;
        for(int i= 0; i<pjg; i++)
            array[i]= arraySalin[pjgDihapus++];
        return array;
    } public static String[] hapusArrayDepan(String array[], int pjgDihapus){
        int pjg= array.length -pjgDihapus;
        String arraySalin[]= array;
        array= new String[pjg];
        pjgDihapus-= 2;
        for(int i= 0; i<pjg; i++)
            array[i]= arraySalin[pjgDihapus++];
        return array;
    } public static Date[] hapusArrayDepan(Date array[], int pjgDihapus){
        int pjg= array.length -pjgDihapus;
        Date arraySalin[]= array;
        array= new Date[pjg];
        pjgDihapus-= 2;
        for(int i= 0; i<pjg; i++)
            array[i]= arraySalin[pjgDihapus++];
        return array;
    }

    public static <Tipe> int cariIndDlmArray(Tipe array[], Tipe elemen){
        int indek[]= cariIndDlmArray(array, elemen, false);
        return (indek.length == 0) ? -1 : indek[0];
    }
    public static <Tipe> int[] cariIndDlmArray(Tipe array[], Tipe elemen, boolean semuaIndek){
        int ind[]= new int[0];
        int i= 0;
        int mirip= 0;
        int batas= (semuaIndek) ? array.length : 1;
        do {
            if (array[i] == elemen) {
                ind = tambahArray(ind, i);
                mirip++;
            }
        } while (mirip<batas && ++i<array.length);
        return ind;
    }

    public static int cariIndDlmArray(Comparable array[], Comparable elemen){
        int indek[]= cariIndDlmArray(array, elemen, false);
        return (indek.length == 0) ? -1 : indek[0];
    }
    public static int[] cariIndDlmArray(Comparable array[], Comparable elemen, boolean semuaIndek){
        int ind[]= new int[0];
        int i= 0;
        int mirip= 0;
        int batas= (semuaIndek) ? array.length : 1;
        do {
            if (array[i].compareTo(elemen) == 0) {
                ind = tambahArray(ind, i);
                mirip++;
            }
        } while (mirip<batas && ++i<array.length);
        return ind;
    }

    public static int cariIndDlmArray(int array[], int elemen){
        int indek[]= cariIndDlmArray(array, elemen, false);
        return (indek.length == 0) ? -1 : indek[0];
    }
    public static int[] cariIndDlmArray(int array[], int elemen, boolean semuaIndek){
        int ind[]= new int[0];
        int i= 0;
        int mirip= 0;
        int batas= (semuaIndek) ? array.length : 1;
        do {
            if (array[i] == elemen) {
                ind = tambahArray(ind, i);
                mirip++;
            }
        } while(mirip<batas && ++i<array.length);
        return ind;
    }

    public static int cariIndDlmArray(String array[], String str){
        int indek[]= cariIndDlmArray(array, str, false);
        return (indek.length == 0) ? -1 : indek[0];
    }
    public static int[] cariIndDlmArray(String array[], String str, boolean semuaIndek){
        int ind[]= new int[0];
        int i= 0;
        int mirip= 0;
        int batas= (semuaIndek) ? array.length : 1;
        do {
            if (array[i].toUpperCase().equals(str.toUpperCase())) {
                ind = tambahArray(ind, i);
                mirip++;
            }
        } while(mirip<batas && ++i<array.length);
        return ind;
    }

    public static int cariIndKosongDlmArray(Object array[], int arahArus){
        if(arahArus== ARAH_MAJU) {
            for (int i = 0; i < array.length; i++)
                if (array[i] == null)
                    return i;
        }
        else if(arahArus== ARAH_MUNDUR) {
            for (int i = array.length-1; i >= 0; i--)
                if (array[i] == null)
                    return i;
        }
        return -1;
    }


    public static int cariIndDlmArrayPukul(Date array[], String str){
        String arrayStr[]= new String[array.length];
        for(int i= 0; i<array.length; i++)
            arrayStr[i]= Chat.formatWaktu.format(array[i]);

        int ind= 0;
        for(int i= 0; i<array.length; i++)
            if(arrayStr[i].toUpperCase().equals(str.toUpperCase())){
                ind= i;
                break;
            }
        return ind;
    }


    public static <Tipe> int cariKosong(Tipe array[]){
        int indKosong[]= cariKosong(array, false);
        return (indKosong.length == 0) ? -1 : indKosong[0];
    }
    public static <Tipe> int[] cariKosong(Tipe array[], boolean semua){
        return cariKosong(array, semua, array.length);
    }
    //(#param) sampai -> batas indek array yang dicari
    public static <Tipe> int[] cariKosong(Tipe array[], boolean semua, int sampai){
        int indKosong[]= new int[0];
        int i= 0;
        int mirip= 0;
        int batas= (semua) ? sampai : 1;
        do {
            if (array[i] == null) {
                indKosong = tambahArray(indKosong, i);
                mirip++;
            }
        } while(mirip<batas && ++i<array.length);
        return indKosong;
    }

    public static int cariKosong(int array[]){
        int indKosong[]= cariKosong(array, false);
        return (indKosong.length == 0) ? -1 : indKosong[0];
    }
    public static int[] cariKosong(int array[], boolean semua){
        return cariKosong(array, semua, array.length);
    }
    //(#param) sampai -> batas indek array yang dicari
    public static int[] cariKosong(int array[], boolean semua, int sampai){
        int indKosong[]= new int[0];
        int i= 0;
        int mirip= 0;
        int batas= (semua) ? sampai : 1;
        do {
            if (array[i]== 0) {
                indKosong = tambahArray(indKosong, i);
                mirip++;
            }
        } while(mirip<batas && ++i<array.length);
        return indKosong;
    }


/*
=========================
View
=========================
*/

    public static View[] tambahArray(View array[], View masuk){
        array= ubahArray(array, array.length+1);
        array[array.length-1]= masuk;
        return array;
    } public static View[] tambahArray(View array[], View masuk[]){
        for(int i= 0; i<array.length; i++)
            array= tambahArray(array, masuk[i]);
        return array;
    }
    public static View[][] tambahArray(View array[][], View masuk[]){
        array= ubahArray(array, array.length+1);
        array[array.length-1]= masuk;
        return array;
    } public static View[][] tambahArray(View array[][], View masuk[][]){
        for(int i= 0; i<array.length; i++)
            array= tambahArray(array, masuk[i]);
        return array;
    }

    public static View[] kurangiArray(View array[], View keluar){
        int ind= cariIndDlmArray(array, keluar);
        return kurangiArray(array, ind);
    } public static View[] kurangiArray(View array[], int indKeluar){
        View arraySalin[]= array;
        array= new View[array.length-1];
        int hitung= 0;
        for(int i= 0; i<arraySalin.length; i++)
            if(i != indKeluar) {
                array[hitung]= arraySalin[i];
                hitung++;
            }
        return array;
    }
    public static View[][] kurangiArray(View array[][], int indKeluar){
        View arraySalin[][]= array;
        array= new View[array.length-1][];
        int hitung= 0;
        for(int i= 0; i<arraySalin.length; i++)
            if(i != indKeluar) {
                array[hitung]= arraySalin[i];
                hitung++;
            }
        return array;
    }

    public static View[] ubahArray(View array[], int pjg){
        View arraySalin[]= array;
        array= new View[pjg];
        int batas= arraySalin.length;
        if(pjg < arraySalin.length)
            batas= pjg;
        System.arraycopy(arraySalin, 0, array, 0, batas);
/*
        for(int i= 0; i<batas; i++)
            array[i]= arraySalin[i];
*/
        return array;
    }
    public static View[][] ubahArray(View array[][], int pjg){
        View arraySalin[][]= array;
        array= new View[pjg][];
        int batas= arraySalin.length;
        if(pjg < arraySalin.length)
            batas= pjg;
        System.arraycopy(arraySalin, 0, array, 0, batas);
/*
        for(int i= 0; i<batas; i++)
            array[i]= arraySalin[i];
*/
        return array;
    }



    /*
=========================
Pengurutan
=========================
hanya bisa dilakukan dengan array angka (int)
*/
    public static int[] urutkan(int array[]){
        int smtr;
        for(int i= 0; i< array.length; i++)
            for(int u= i+1; u< array.length; u++)
                if(array[i] > array[u]){
                    smtr= array[i];
                    array[i]= array[u];
                    array[u]= smtr;
                }
        return array;
    }

}
