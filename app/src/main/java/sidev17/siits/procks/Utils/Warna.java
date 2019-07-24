package sidev17.siits.procks.Utils;

import android.content.res.ColorStateList;
import android.graphics.Color;

public class Warna {

    public static final int BATAS_KEGELAPAN_STANDAR= 600;
    public static String[] bandingkanKegelapanWarna(String warnaGelap, String warnaTerang){
        //hasil[0]= warnaGelap; hasil[1]= warnaTerang;
        String hasil[]= new String[2];

        int merahGelap = Integer.parseInt(warnaGelap.substring(1, 3), 16);
        int hijauGelap = Integer.parseInt(warnaGelap.substring(3, 5), 16);
        int biruGelap = Integer.parseInt(warnaGelap.substring(5, 7), 16);
        int totalGelap= merahGelap +hijauGelap +biruGelap;

        int merahTerang = Integer.parseInt(warnaTerang.substring(1, 3), 16);
        int hijauTerang = Integer.parseInt(warnaTerang.substring(3, 5), 16);
        int biruTerang = Integer.parseInt(warnaTerang.substring(5, 7), 16);
        int totalTerang= merahTerang +hijauTerang +biruTerang;

        if(totalGelap <= totalTerang) {
            hasil[0]= warnaGelap;
            hasil[1]= warnaTerang;
        } else{
            hasil[0]= warnaTerang;
            hasil[1]= warnaGelap;
        }
        return hasil;
    }
    public static int sesuaikanKegelapan(String id){
        return sesuaikanKegelapan(id, "#000000", "#FFFFFF");
    }
    public static int sesuaikanKegelapan(String id, String warnaGelap, String warnaTerang){
        try {
            String gelapTerang[]= bandingkanKegelapanWarna(warnaGelap, warnaTerang);

            int merah = Integer.parseInt(id.substring(1, 3), 16);
            int hijau = Integer.parseInt(id.substring(3, 5), 16);
            int biru = Integer.parseInt(id.substring(5, 7), 16);
            int alfa;

            if (id.length() == 7 && id.startsWith("#")) {
                return sesuaikanKegelapan(merah, hijau, biru, gelapTerang[0], gelapTerang[1]);
            }
            else{
                alfa= Integer.parseInt(id.substring(7, 9), 16);
                return sesuaikanKegelapan(merah, hijau, biru, alfa, gelapTerang[0], gelapTerang[1]);
            }
        } catch(Exception e){
            String pesan= "Harap masukan format string warna yang benar >> #000000";
            return 0;
        }
    }

    public static int sesuaikanKegelapan(int merah, int hijau, int biru, String warnaGelap, String warnaTerang){
//        if(merah + hijau + biru <= 612)
        if(merah + hijau + biru <= BATAS_KEGELAPAN_STANDAR)
            return Color.parseColor(warnaTerang);
        else
            return Color.parseColor(warnaGelap);
    }

    public static int sesuaikanKegelapan(int merah, int hijau, int biru, int alfa, String warnaGelap, String warnaTerang){
//        if(merah + hijau + biru <= 612)
        if(merah + hijau + biru <= BATAS_KEGELAPAN_STANDAR)
            return Color.parseColor(warnaTerang);
        else
            return Color.parseColor(warnaGelap);
    }

    public static String ambilStringWarna(int warna){
        return ambilStringWarna(Color.red(warna), Color.green(warna), Color.blue(warna));
    }
    public static String ambilStringWarna(int merah, int hijau, int biru){
        String merahStr= Integer.toString(merah, 16);
        String hijauStr= Integer.toString(hijau, 16);
        String biruStr= Integer.toString(biru, 16);

        if(merahStr.length() < 2)
            merahStr= "0" +merahStr;
        if(hijauStr.length() < 2)
            hijauStr= "0" +hijauStr;
        if(biruStr.length() < 2)
            biruStr= "0" +biruStr;

        String warna= "#" +merahStr +hijauStr +biruStr;
        return warna;
    }

    public static int aturIntensitasWarna(String warna, int tingkatKecerahan){
        if(warna.length() == 7 && warna.startsWith("#")){
            int merah = normalisasikanRgb(Integer.parseInt(warna.substring(1, 3), 16) +tingkatKecerahan);
            int hijau = normalisasikanRgb(Integer.parseInt(warna.substring(3, 5), 16) +tingkatKecerahan);
            int biru = normalisasikanRgb(Integer.parseInt(warna.substring(5, 7), 16) +tingkatKecerahan);
            return Color.parseColor(ambilStringWarna(merah, hijau, biru));
        }
        return -1;
    }

    private static int normalisasikanRgb(int komponenWarna){
        if(komponenWarna < 0)
            return 0;
        else if(komponenWarna > 255)
            return 255;
        return komponenWarna;
    }

    public static boolean cekWarnaGelap(String warna){
        if(warna.length() == 7 && warna.startsWith("#")) {
            int merah = Integer.parseInt(warna.substring(1, 3), 16);
            int hijau = Integer.parseInt(warna.substring(3, 5), 16);
            int biru = Integer.parseInt(warna.substring(5, 7), 16);
            if(merah +hijau +biru <= BATAS_KEGELAPAN_STANDAR)
                return true;
        }
        return false;
    }

    public static int lawanWarna(String warna){
        int rgb[]= new int[3];
        rgb[0]= Integer.parseInt(warna.substring(0,2), 16);
        rgb[1]= Integer.parseInt(warna.substring(2,4), 16);
        rgb[2]= Integer.parseInt(warna.substring(4,6), 16);

        int indMin= (rgb[0] < rgb[1]) ? ((rgb[0] < rgb[2]) ? 0 : 2) : 1; //2 3 1
        int indMax= (rgb[0] > rgb[1]) ? ((rgb[0] > rgb[2]) ? 0 : 2) : 1; //3 2 4
        int indSisa= (indMax == 0) ? ((indMin == 1) ? 2 : 1) : ((indMin == 0) ? ((indMax == 1) ? 2 : 1) : 0); // x0x
        int target= rgb[indMin] +rgb[indMax];

        String warnaLawan[]= new String[3];
        warnaLawan[indMin]= Integer.toString(rgb[indMax], 16);
        warnaLawan[indMax]= Integer.toString(rgb[indMin], 16);
        warnaLawan[indSisa]= Integer.toString(target -rgb[indSisa], 16);

        return Color.parseColor("#" +warnaLawan[0] +warnaLawan[1] +warnaLawan[2]);
    }


    public static ColorStateList buatDaftarWarna(int warnaNormal, int warnaDitekan, int warnaDicentang, int warnaDipilih){
        int state[][]= {
                {android.R.attr.state_pressed}, {android.R.attr.state_checked}, {android.R.attr.state_selected}, {-android.R.attr.state_checked}
        };
        int warnaState[]= {
                warnaDitekan, warnaDicentang, warnaDipilih, warnaNormal
        };
        return new ColorStateList(state, warnaState);
    }
    public static ColorStateList buatDaftarWarna(int warnaNormal, int warnaDitekan, int warnaDipilih){
        int state[][]= {
                {android.R.attr.state_pressed}, {android.R.attr.state_selected}, {-android.R.attr.state_checked}
        };
        int warnaState[]= {
                warnaDitekan, warnaDipilih, warnaNormal
        };
        return new ColorStateList(state, warnaState);
    }
    public static ColorStateList buatDaftarWarna(int warnaNormal, int warnaDitekan){
        int state[][]= {
                {android.R.attr.state_pressed},  {-android.R.attr.state_checked}
        };
        int warnaState[]= {
                warnaDitekan, warnaNormal
        };
        return new ColorStateList(state, warnaState);
    }

    public static boolean cekWarnaSah(String warna, String batas){
        if(warna.length() == 0 || warna.toUpperCase().equals(batas.toUpperCase()))
            return false;
        return cekWarnaSah(warna);
    } public static boolean cekWarnaSah(String warna){
        int w1, w2, w3;
        if(warna.length() == 7 && warna.startsWith("#")){
            try {
                w1 = Integer.parseInt(warna.substring(1, 3), 16);
                w2 = Integer.parseInt(warna.substring(3, 5), 16);
                w3 = Integer.parseInt(warna.substring(5, 7), 16);
                return true;
            } catch (Exception e){
                return false;
            }
        }
        return false;
    }
    public static boolean cekWarnaSah(int warna, int batas){
        if(warna == batas)
            return false;
        return cekWarnaSah(warna);
    } public static boolean cekWarnaSah(int warna){
        try{
            ambilStringWarna(warna);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
