package sidev17.siits.proshare.Utils;

import java.util.Date;

public class ArrayModification {

    public static final int ARAH_MAJU= 12;
    public static final int ARAH_MUNDUR= 11;

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
        for(int i= 0; i<batas; i++)
            array[i]= arraySalin[i];
        return array;
    }
    public static String[] ubahArray(String array[], int pjg){
        String arraySalin[]= array;
        array= new String[pjg];
        int batas= arraySalin.length;
        if(pjg < arraySalin.length)
            batas= pjg;
        for(int i= 0; i<batas; i++)
            array[i]= arraySalin[i];
        return array;
    }
    public static String[][] ubahArray(String array[][], int pjg){
        String arraySalin[][]= array;
        array= new String[pjg][];
        int batas= arraySalin.length;
        if(pjg < arraySalin.length)
            batas= pjg;
        for(int i= 0; i<batas; i++)
            array[i]= arraySalin[i];
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
    public static int[] ubahArray(int array[], int pjg){
        int arraySalin[]= array;
        array= new int[pjg];
        int batas= arraySalin.length;
        if(pjg < arraySalin.length)
            batas= pjg;
        for(int i= 0; i<batas; i++)
            array[i]= arraySalin[i];
        return array;
    }
    public static int[][] ubahArray(int array[][], int pjg){
        int arraySalin[][]= array;
        array= new int[pjg][];
        int batas= arraySalin.length;
        if(pjg < arraySalin.length)
            batas= pjg;
        for(int i= 0; i<batas; i++)
            array[i]= arraySalin[i];
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

    public static int cariIndDlmArray(String array[], String str){
        int ind= -1;
        for(int i= 0; i<array.length; i++)
            if(array[i].toUpperCase().equals(str.toUpperCase())){
                ind= i;
                break;
            }
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

}
