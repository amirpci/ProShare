package sidev17.siits.proshare.Utils;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Amir MB
 */
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import sidev17.siits.proshare.Model.Permasalahan;
import sidev17.siits.proshare.Model.Problem.Solusi;

public class AlgoritmaKesamaan {
    static Scanner in = new Scanner(System.in);
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static String[] bukanKalimatDasar = {"how", "should", "i", "you", "we", "they", "us", "them", "she", "he", "is", "which", "whom", "whose", "to", "use", "in", "of"
            , "a", "?", "!", "can", "that", "doesn't", "what's", "what", "the", "between", "", " ", "\n", "am", "doesnt", "and", "are", "if", "for", "not", "as", "about", "whats", "out",
            "start", "zero", "good", "enough","or"};
    static ArrayList<Solusi> permasalahan;
    static String searchKalimat;

    public AlgoritmaKesamaan(ArrayList<Solusi> permasalahan, String searchKalimat) {
        this.permasalahan = permasalahan;
        this.searchKalimat = searchKalimat;
    }

    public ArrayList<Solusi> listKetemu() {
        ArrayList<Solusi> listKetemu = new ArrayList<>();
        ArrayList<Double> listKetemuIndex = new ArrayList<>();
        String cari = searchKalimat;
        ArrayList<Keyword> listSemuaKeyword = keywordKejadian();
        ArrayList<ArrayList<CVektor>> cvList = listFaqCV(listSemuaKeyword);
        ArrayList<CVektor> pertanyaan = dapatkanCV(cari, listSemuaKeyword);

        int jawabanTercocok = -1;
        double bobotTertinggi = 0.0;
        int listKetemuCounter = 0;
        for (int i = 0; i < cvList.size(); i++) {
            Log.d("FAQ ke ", String.valueOf((i + 1)));
            //keyword sama faq dengan pertanyaan
            int[] keySamaQ = new int[cvList.get(i).size()];
            int[] keySamaPertanyaan = new int[cvList.get(i).size()];
            int countSama = 0;
            for (int j = 0; j < cvList.get(i).size(); j++) {
                int keyPosisi = cvList.get(i).get(j).getKeyword();
                double weight = cvList.get(i).get(j).getWeight();
                for (int k = 0; k < pertanyaan.size(); k++) {
                    if (keyPosisi == pertanyaan.get(k).getKeyword()) {
                        keySamaQ[countSama] = j;
                        keySamaPertanyaan[countSama] = k;
                        countSama++;
                    }
                }
                Log.d("keyword weight :   ","(" + listSemuaKeyword.get(keyPosisi).getKeyword() + "," + weight + " - " + cvList.get(i).get(j).getMuncul() + "/" + listSemuaKeyword.get(keyPosisi).getKejadian() + ")");
            }
            double D = 0;
            for (int j = 0; j < countSama; j++) {
                D += cvList.get(i).get(keySamaQ[j]).getWeight() * pertanyaan.get(keySamaPertanyaan[j]).getWeight();
            }
            if(D>0.0){
                listKetemuIndex.add(D);
                listKetemu.add(permasalahan.get(i));
                Log.d("judul ",listKetemu.get(listKetemuCounter++).getProblem().getproblem_title());
            }
            // cuman buat testing
            if (D > bobotTertinggi) {
                jawabanTercocok = i;
                bobotTertinggi = D;
            }
            Log.d("","\n");
            Log.d("D = " , String.valueOf(D));
            Log.d("","-----------");
        }
        Log.d("","\n");
        Log.d("","\n");
        if (jawabanTercocok != -1) {
            System.out.println("Mungkin maksud ada pertanyaannya adalah : ");
            System.out.println(permasalahan.get(jawabanTercocok).getProblem().getproblem_title());
        } else {
            System.out.println("Mohon maaf pertanyaan anda tidak ditemukan :(");
        }
        int panjangKetemu = listKetemu.size();
        for(int i=0;i<panjangKetemu-1;i++){
            int indexSekarang = i;
            double terbesar = listKetemuIndex.get(i);
            for(int j=i+1;j<panjangKetemu;j++){
                if(terbesar<listKetemuIndex.get(j)){
                    indexSekarang = j;
                    terbesar = listKetemuIndex.get(j);
                }else if(terbesar==listKetemuIndex.get(j)){
                    if(listKetemu.get(j).getProblem().getproblem_title().length()<listKetemu.get(i).getProblem().getproblem_title().length()){
                        double temp = listKetemuIndex.get(j);
                        listKetemuIndex.set(j, listKetemuIndex.get(i));
                        listKetemuIndex.set(i, temp);
                        Solusi temp1 = listKetemu.get(j);
                        listKetemu.set(j, listKetemu.get(i));
                        listKetemu.set(i, temp1);
                    }
                }
            }
            if(indexSekarang!=i){
                double temp = listKetemuIndex.get(indexSekarang);
                listKetemuIndex.set(indexSekarang, listKetemuIndex.get(i));
                listKetemuIndex.set(i, temp);

                Solusi temp1 = listKetemu.get(indexSekarang);
                listKetemu.set(indexSekarang, listKetemu.get(i));
                listKetemu.set(i, temp1);
            }
        }
        return listKetemu;
    }

    static boolean bukanKataDasar(String str) {
        for (int i = 0; i < bukanKalimatDasar.length; i++) {
            if (str.toLowerCase().equals(bukanKalimatDasar[i]))
                return false;
        }
        return true;
    }

    static String hilangkanSimbol(String str) {
        char[] huruf = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '-'};
        String kataBenar = "";
        for (int i = 0; i < str.length(); i++) {
            boolean bukanHuruf = false;
            for (int j = 0; j < huruf.length; j++) {
                if (str.charAt(i) == huruf[j]) {
                    bukanHuruf = true;
                    break;
                }
            }
            if (bukanHuruf) {
                kataBenar += String.valueOf(str.charAt(i));
            }
        }
        return kataBenar;
    }

    //dapatkan list CV
    static ArrayList<CVektor> dapatkanCV(String str, ArrayList<Keyword> listSemuaKeyword) {
        String[] pecahKey = str.split(" ");
        ArrayList<CVektor> qCV = new ArrayList<CVektor>();
        for (int j = 0; j < pecahKey.length; j++) {
            String key = hilangkanSimbol(pecahKey[j].toLowerCase());
            boolean duplikat = false; // biar kata yang sama gk ditambahin lagi
            for(int i = 0 ; i < qCV.size(); i ++){
                if(key.equalsIgnoreCase(listSemuaKeyword.get(qCV.get(i).getKeyword()).getKeyword()));{
                    duplikat = true;
                    break;
                }
            }
            if(!duplikat){
                boolean keywordKetemu = false;
                int count = 1;
                int keywordPosisi = -1;
                for (int k = 0; k < listSemuaKeyword.size(); k++) {
                    if (key.equals(listSemuaKeyword.get(k).getKeyword())) {
                        keywordPosisi = k;
                        for (int l = 0; l < pecahKey.length; l++) {
                            String keyL = hilangkanSimbol(pecahKey[l].toLowerCase());
                            if (j != l) {
                                if (key.equals(keyL)) {
                                    count++;
                                }
                            }
                        }
                        keywordKetemu = true;
                        break;
                    }
                }
                if (keywordKetemu) {
                    double weight = (double) count / (double) listSemuaKeyword.get(keywordPosisi).getKejadian();
                    qCV.add(new CVektor(keywordPosisi, weight, count));
                }
            }
        }
        return qCV;
    }


    // mendapatkan list cv setiap faq
    static ArrayList<ArrayList<CVektor>> listFaqCV(ArrayList<Keyword> listSemuaKeyword) {
        ArrayList<ArrayList<CVektor>> semuaFaq = new ArrayList<ArrayList<CVektor>>();
        for (int i = 0; i < permasalahan.size(); i++) {
            semuaFaq.add(dapatkanCV(permasalahan.get(i).getProblem().getproblem_title(), listSemuaKeyword));
        }
        return semuaFaq;
    }

    //mendapatkan kesuluruhan keyword
    static ArrayList<Keyword> keywordKejadian() {
        ArrayList<Keyword> keyword = new ArrayList<>();
        ArrayList<String> keywordStr = new ArrayList<>();
        for (int i = 0; i < permasalahan.size(); i++) {
            String[] pecahanKeyword = permasalahan.get(i).getProblem().getproblem_title().split(" ");
            for (int j = 0; j < pecahanKeyword.length; j++) {
                String keywordNow = hilangkanSimbol(pecahanKeyword[j].toLowerCase());
                Log.d("",keywordNow + ";");
                boolean ada = false;
                for (int k = 0; k < keywordStr.size(); k++) {
                    if (keywordStr.get(k).equals(keywordNow)) {
                        ada = true;
                        break;
                    }
                }
                if (!ada && bukanKataDasar(keywordNow))
                    keywordStr.add(keywordNow);
            }
        }

        for (int i = 0; i < keywordStr.size(); i++) {
            int jumlahKeyword = 0;
            for (int j = 0; j < permasalahan.size(); j++) {
                String[] keySplit = permasalahan.get(j).getProblem().getproblem_title().split(" ");
                for (int k = 0; k < keySplit.length; k++) {
                    if (keywordStr.get(i).equals(hilangkanSimbol(keySplit[k].toLowerCase())))
                        jumlahKeyword++;
                }
            }
            keyword.add(new Keyword(keywordStr.get(i), jumlahKeyword));
        }

        for (int i = 0; i < keyword.size(); i++) {
            Log.d("",keyword.get(i).getKejadian() + " : " + keyword.get(i).getKeyword()+"\n");
        }
        return keyword;
    }

    public static class CVektor {
        int keyword;
        int muncul;
        double weight;

        public CVektor(int keyword, double weight, int muncul) {
            this.keyword = keyword;
            this.weight = weight;
            this.muncul = muncul;
        }

        public int getKeyword() {
            return keyword;
        }

        public double getWeight() {
            return weight;
        }

        public int getMuncul() {
            return muncul;
        }

    }

    public static class Keyword {
        private String keyword;
        private int kejadian;

        public Keyword(String keyword, int kejadian) {
            this.keyword = keyword;
            this.kejadian = kejadian;
        }

        public String getKeyword() {
            return keyword;
        }

        public int getKejadian() {
            return kejadian;
        }
    }
}
