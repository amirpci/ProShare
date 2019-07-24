package sidev17.siits.procks.Utils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Chat {
    final String header= "pesan waktu diriSendiri";

    private String penyimpananInternal;
    final String ekstensi= ".chat";
    final String tipeFile= "<!CHAT>";

    public static final SimpleDateFormat format= new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat formatWaktu= new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss:SSSS");

    private String namaOrg;
    private String bidangOrg;
    private Bitmap fotoOrg;

    private Date pukul[];
    private String pesan[];
    private int diriSendiri[];
    public static final int DIRI_SENDIRI= 112;
    public static final int ORANG_LAIN= 111;

//    Stack<String> stackChat;

//    String id;
    /*
    Chat now;
    Chat next;
    Chat before;
    */

    public Chat(String fileInternal){
        penyimpananInternal= fileInternal;
//        stackChat= new Stack<>();
        resetData();
    }


//====================
    public void resetData(int jmlAwal){
        pesan= new String[jmlAwal];
        pukul= new Date[jmlAwal];
        diriSendiri= new int[jmlAwal];
    } public void resetData(){
        resetData(0);
    }


//====================
    public String ambilPesan(String strWaktu){
        int ind= ArrayMod.cariIndDlmArrayPukul(pukul, strWaktu);
        if(ind >= 0)
            return pesan[ind];
        else
            return null;
    }
    public String[] ambilPesanSemua(){
        return pesan;
    }

    public String[] ambilPukulSemua(){
        String array[]= new String[pukul.length];
        for(int i= 0; i<pukul.length; i++)
            array[i]= formatWaktu.format(pukul[i]);
        return array;
    }
    public String ambilPukul(String strWaktu){
        int ind= ArrayMod.cariIndDlmArrayPukul(pukul, strWaktu);
        if(ind >= 0)
            return format.format(pukul[ind]);
        else
            return null;
    }
    public Date[] ambilPukulMentahSemua(){
        return pukul;
    }
    public Date ambilPukulMentah(String strWaktu){
        int ind= ArrayMod.cariIndDlmArrayPukul(pukul, strWaktu);
        if(ind >= 0)
            return pukul[ind];
        else
            return null;
    }

    public int[] ambilDiriSendiriSemua(){
        return diriSendiri;
    }
    public boolean diriSendiriKah(String strWaktu){
        int ind= ArrayMod.cariIndDlmArrayPukul(pukul, strWaktu);
        if(diriSendiri[ind] == DIRI_SENDIRI)
            return true;
        else
            return false;
    }

    public int ambilJmlPesan(){
        return pesan.length;
    }

    public void aturNamaOrg(String nama){
        namaOrg= nama;
    }
    public String ambilNamaOrg(){
        return namaOrg;
    }

    public Bitmap ambilFotoOrg(){
        return fotoOrg;
    }

    public void aturBidangOrg(String bidang){
        bidangOrg= bidang;
    }
    public String ambilBidangOrg(){
        return bidangOrg;
    }


//====================
    public void perlebarChat(int pjgTambahan){
        pesan= ArrayMod.ubahArray(pesan, pjgTambahan);
        pukul= (Date[]) ArrayMod.ubahArray(pukul, pjgTambahan);
        diriSendiri= ArrayMod.ubahArray(diriSendiri, pjgTambahan);
    }
    public void rampingkanChat(){
        int batas= ArrayMod.cariIndKosongDlmArray(pesan, ArrayMod.ARAH_MUNDUR) +1;
        if(batas > 0) {
            pesan = ArrayMod.hapusArrayDepan(pesan, batas);
            pukul = (Date[]) ArrayMod.hapusArrayDepan(pukul, batas);
            diriSendiri = ArrayMod.hapusArrayDepan(diriSendiri, batas);
        }
    }

    public void tambahChat(String pesan, Date pukul, int diriSendiri){
        tambahChatKeInternal(pesan, pukul, diriSendiri);
        tambahChatKeServer(pesan, pukul, diriSendiri);
        tambahChatData(pesan, pukul, diriSendiri);
    }
    private void tambahChatData(String pesan, Date pukul, int diriSendiri){
        this.pesan= ArrayMod.tambahArray(this.pesan, pesan);
        this.pukul= (Date[]) ArrayMod.tambahArray(this.pukul, pukul);
        this.diriSendiri= ArrayMod.tambahArray(this.diriSendiri, diriSendiri);
    }
    private void tambahChatKeServer(String pesan, Date pukul, int diriSendiri){
        //sesuatu
    }
    private void tambahChatKeInternal(String pesan, Date pukul, int diriSendiri) {
        File file = new File(penyimpananInternal + ekstensi);
        File fileTemp = new File(penyimpananInternal + "Temp" + ekstensi);
        try {
            if(file.exists()) {
                file.renameTo(fileTemp);

                Scanner input = new Scanner(fileTemp);
                PrintWriter tulis = new PrintWriter(file);

/*
            Rancangan file=
            <!CHAT>
            jml= ...
            [header]
            [isi]
 */
                tulis.println(tipeFile);
                input.nextLine();
                tulis.println("jml= " +Integer.toString(Integer.parseInt(input.nextLine().split("0")[1]) + 1));
                tulis.println(header);
                input.nextLine();
                tulis.println(pesan + " " + formatWaktu.format(pukul) + " " + diriSendiri);
                while (input.hasNextLine()) {
                    tulis.println(input.nextLine());
                }
                tulis.close();
                fileTemp.delete();
            } else{
                PrintWriter tulis = new PrintWriter(file);
                tulis.println(tipeFile);
                tulis.println("jml= 0");
                tulis.println(header);
                tulis.println(pesan + " " + formatWaktu.format(pukul) + " " + diriSendiri);
                tulis.close();
            }
        } catch (Exception e) {
        }
    }

    public void bacaInternal(int mulai, int sebanyak){
        File file= new File(penyimpananInternal +ekstensi);
        try{
            Scanner input= new Scanner(file);
            String array[];
            int batas= mulai +sebanyak;
            for(int i= 0; i<mulai; i++)
                input.nextLine();
            if(batas > pesan.length)
                perlebarChat(mulai +sebanyak -pesan.length);
            for(int i= mulai; i<batas; i++){
                if(!input.hasNextLine())
                    return;
                array= input.nextLine().split(" ");
                pesan[pesan.length -i]= array[0];
                pukul[pukul.length -i]= formatWaktu.parse(array[1]);
                diriSendiri[diriSendiri.length -i]= Integer.parseInt(array[2]);
            }

        } catch(Exception e){}
    }
    private void bacaServer(int mulai, int sebanyak){
        //sesuatu
    }

    public boolean adaSisaChatDiInternal(){
         File file= new File(penyimpananInternal +ekstensi);
         try{
             Scanner input= new Scanner(file);
             int batas= pesan.length;
             for(int i= 0; i<batas; i++)
                 input.nextLine();
             if(input.hasNextLine())
                 return true;
            } catch(Exception e){}
        return false;
    }

    private void realTimeCheck(){
/*        bacaServer();
          tambahChatKeInternal();
          tambahChatData();
*/
    }

    //Deprecated
    ///*
    //    private void ambilPesanDariServer(String strWaktu){
    //        //sesuatu
    ////        pesan= pesanBaru;
    //    }
    //    private void ambilPesanDariInternal(String strWaktu){
    //        //sesuatu
    ////        pesan= pesanBaru;
    //    }
    //
    //    private void simpanChatKeServer(String pesan, String pukul){
    //        //sesuatu
    //    }
    //    private void simpanChatKeInternal(String pesan, String pukul){
    //        //sesuatu
    //    }
    //*/
    ///*
    //    private void ambilPukulDariServer(String strWaktu){
    //        //sesuatu
    ////        pukul= formatWaktu.format(String waktu);
    //    }
    //    private void ambilPukulDariInternal(String strWaktu){
    //        //sesuatu
    //        pukul= formatWaktu.format(String waktu);
    //    }
    //*/
}
