package sidev17.siits.proshare.Utils;


public class Array<Tipe> {
    private final int UKURAN_DEFAULT= 10;

    private Object elemen[];
    private boolean bolehRumpang= false;
    private int ukuran= 0;
    private int indIsi= 0;  //berguna untuk arrayyang boleh rumpang
    private int batas= -1;

    public final ArrayPrimitif PRIMITIF= new ArrayPrimitif();

    private PenungguTraverse<Tipe> pngTraverse;

//    private int mapping[]= new int[0];

//    private int ukuranAwal; //ukuran yang ditentukan pengguna
//    private int indIsi;


    public Array(Tipe array[]){
        elemen= array;
        batas= array.length-1;
        int indekKosong[]= indekKosong(true);
        if(indekKosong.length > 0){
            bolehRumpang= true;
            ukuran= batas +1 -indekKosong.length;
            while(elemen[batas] == null)
                batas--;
            indIsi= indekKosong[0];
        } else{
//            bolehRumpang= false;
            ukuran= batas;
        }
    }
    public Array(int ukuranAwal){
//        bolehRumpang= false;
        elemen= new Object[ukuranAwal];
    }
    public Array(boolean bolehRumpang){
        this.bolehRumpang= bolehRumpang;
        elemen= new Object[UKURAN_DEFAULT];
    }
    public Array(){
//        bolehRumpang= false;
        elemen= new Object[UKURAN_DEFAULT];
    }

    public void tambah(Tipe masuk){
        if(ukuran >= elemen.length)
            elemen= ArrayMod.ubahArray(elemen, ukuran +10);
        if(!bolehRumpang) {
            elemen[ukuran++]= masuk;
            batas++;
        }
        else {
            elemen[indIsi]= masuk;
            ukuran++;
            if(indIsi > batas)
                batas= indIsi;
            kosongSelanjutnya();
        }
    }
    public void tambah(Tipe masuk, int indek){
        if(!bolehRumpang && indek > ukuran)
            throw new IndexOutOfBoundsException(pesanOutOfBound(indek));
        if (ukuran >= elemen.length || indek >= elemen.length){
            int pengali= (indek-elemen.length)/10 +1;
            elemen = ArrayMod.ubahArray(elemen, elemen.length + (10*pengali));
        }

        if(elemen[indek] != null)
            for (int i= ukuran; i> indek; i--)
                elemen[i]= elemen[i-1];

        elemen[indek] = masuk;
        ukuran++;

        if(indek > batas)
            batas= indek;
        else if(ukuran > batas)
            batas= ukuran-1;

        if(indek == indIsi)
            kosongSelanjutnya();
    }

    public void tambah(Tipe array[]){
        if(array.length > elemen.length -ukuran){
            int pengali= (array.length-(elemen.length -ukuran))/10 +1;
            elemen = ArrayMod.ubahArray(elemen, elemen.length + (10*pengali));
        }
        if(!bolehRumpang){
            for(int i= 0; i< array.length; i++){
                if(array[i] != null){
                    elemen[ukuran++]= array[i];
                    batas++;
                }
            }
        } else{
            int jalan= 0;
            for(int i= indIsi; i< elemen.length; i++)
                if(jalan >= array.length)
                    break;
                else if(elemen[i] == null) {
                    elemen[i]= array[jalan];
                    if(array[jalan++] != null)
                        ukuran++;
                    if(i > batas)
                        batas= i;
                }
            kosongSelanjutnya();
        }
    }

    public Tipe ambil(int indek){
        if((!bolehRumpang && indek >= ukuran) || indek >= elemen.length)
            throw new IndexOutOfBoundsException(pesanOutOfBound(indek));
        return (Tipe) elemen[indek];
    }

    public Tipe[] jadikanArray(){
        //int batas= (!bolehRumpang) ? ukuran : this.batas+1;
        Tipe array[]= (Tipe[]) new Object[batas+1];
        for(int i= 0; i<= batas; i++)
            array[i]= (Tipe) elemen[i];
        return array;
    }

    public class ArrayPrimitif{
        //parameter hanya sebagai pembeda method, bukan pengubah array

        public char[] arrayChar(){
            Object cek= elemen[0];
            if(!(cek instanceof Character))
                throw new ClassCastException(pesanClassCast(cek.getClass().getName(), char.class.getName()));
//            int batas= (!bolehRumpang) ? ukuran : elemen.length;
            char array[]= new char[batas+1];
            for(int i= 0; i<= batas; i++)
                array[i]= (Character) elemen[i];
//            System.arraycopy(elemen, 0, array, 0, ukuran);
            return array;
        }
        public int[] arrayInt(){
            Object cek= elemen[0];
            if(!(cek instanceof Integer))
                throw new ClassCastException(pesanClassCast(cek.getClass().getName(), int.class.getName()));
//            int batas= (!bolehRumpang) ? ukuran : elemen.length;
            int array[]= new int[batas+1];
            for(int i= 0; i<= batas; i++)
                array[i]= (Integer) elemen[i];
//            System.arraycopy(elemen, 0, array, 0, ukuran);
            return array;
        }
        public byte[] arrayByte(){
            Object cek= elemen[0];
            if(!(cek instanceof Byte))
                throw new ClassCastException(pesanClassCast(cek.getClass().getName(), byte.class.getName()));
//            int batas= (!bolehRumpang) ? ukuran : elemen.length;
            byte array[]= new byte[batas+1];
            for(int i= 0; i<= batas; i++)
                array[i]= (Byte) elemen[i];
//            System.arraycopy(elemen, 0, array, 0, ukuran);
            return array;
        }
        public boolean[] arrayBoolean(){
            Object cek= elemen[0];
            if(!(cek instanceof Boolean))
                throw new ClassCastException(pesanClassCast(cek.getClass().getName(), boolean.class.getName()));
//            int batas= (!bolehRumpang) ? ukuran : elemen.length;
            boolean array[]= new boolean[batas+1];
            for(int i= 0; i<= batas; i++)
                array[i]= (Boolean) elemen[i];
//            System.arraycopy(elemen, 0, array, 0, ukuran);
            return array;
        }
        public short[] arrayShort(){
            Object cek= elemen[0];
            if(!(cek instanceof Short))
                throw new ClassCastException(pesanClassCast(cek.getClass().getName(), short.class.getName()));
//            int batas= (!bolehRumpang) ? ukuran : elemen.length;
            short array[]= new short[batas+1];
            for(int i= 0; i<= batas; i++)
                array[i]= (Short) elemen[i];
//            System.arraycopy(elemen, 0, array, 0, ukuran);
            return array;
        }
        public long[] arrayLong(){
            Object cek= elemen[0];
            if(!(cek instanceof Long))
                throw new ClassCastException(pesanClassCast(cek.getClass().getName(), long.class.getName()));
//            int batas= (!bolehRumpang) ? ukuran : elemen.length;
            long array[]= new long[batas+1];
            for(int i= 0; i<= batas; i++)
                array[i]= (Long) elemen[i];
//            System.arraycopy(elemen, 0, array, 0, ukuran);
            return array;
        }
        public float[] arrayFloat(){
            Object cek= elemen[0];
            if(!(cek instanceof Float))
                throw new ClassCastException(pesanClassCast(cek.getClass().getName(), float.class.getName()));
//            int batas= (!bolehRumpang) ? ukuran : elemen.length;
            float array[]= new float[batas+1];
            for(int i= 0; i<= batas; i++)
                array[i]= (Float) elemen[i];
//            System.arraycopy(elemen, 0, array, 0, ukuran);
            return array;
        }
        public double[] arrayDouble(){
            Object cek= elemen[0];
            if(!(cek instanceof Double))
                throw new ClassCastException(pesanClassCast(cek.getClass().getName(), double.class.getName()));
//            int batas= (!bolehRumpang) ? ukuran : elemen.length;
            double array[]= new double[batas+1];
            for(int i= 0; i<= batas; i++)
                array[i]= (Double) elemen[i];
//            System.arraycopy(elemen, 0, array, 0, ukuran);
            return array;
        }
    }

    private Object arrayPrimitif(Class<?> tipeData, int ukuran) throws NegativeArraySizeException {
        if (tipeData == char.class || tipeData == Character.class) {
            return new char[ukuran];
        } else if (tipeData == int.class || tipeData == Integer.class) {
            return new int[ukuran];
        } else if (tipeData == byte.class || tipeData == Byte.class) {
            return new byte[ukuran];
        } else if (tipeData == boolean.class || tipeData == Boolean.class) {
            return new boolean[ukuran];
        } else if (tipeData == short.class || tipeData == Short.class) {
            return new short[ukuran];
        } else if (tipeData == long.class || tipeData == Long.class) {
            return new long[ukuran];
        } else if (tipeData == float.class || tipeData == Float.class) {
            return new float[ukuran];
        } else if (tipeData == double.class || tipeData == Double.class) {
            return new double[ukuran];
        } else if (tipeData == void.class || tipeData == Void.class) {
            throw new IllegalArgumentException("Gak bisa buat array tipe void");
        }
        throw new AssertionError();
    }

    public void ganti(int indek, Tipe masuk){
        if((!bolehRumpang && indek >= ukuran) || indek >= elemen.length)
            throw new IndexOutOfBoundsException(pesanOutOfBound(indek));
        elemen[indek]= masuk;
    }
    public boolean ganti(Tipe keluar, Tipe masuk, boolean semuaYgMirip){
        if(!semuaYgMirip)
            return ganti(keluar, masuk);
        if(!keluar.getClass().getName().equals(masuk.getClass().getName()))
            throw new ClassCastException(pesanClassCast(keluar.getClass(), masuk.getClass()));

        int jmlYgMirip= 0;
        int batas= this.batas +1;
        if(keluar instanceof Comparable){
            for(int i= 0; i< batas; i++)
                if(elemen[i] != null && (((Comparable)elemen[i]).compareTo(keluar) == 0)){
                    elemen[i]= masuk;
                    jmlYgMirip++;
                }
        } else{
            for(int i= 0; i< batas; i++)
                if(elemen[i] != null && (elemen[i].equals(keluar))){ /*&& jmlYgMirip < batasJmlYgMirip*/
                    elemen[i]= masuk;
                    jmlYgMirip++;
                }
        }
        if(jmlYgMirip == 0)
            return false;
        return true;
    }
    public boolean ganti(Tipe keluar, Tipe masuk){
        if(!keluar.getClass().getName().equals(masuk.getClass().getName()))
            throw new ClassCastException(pesanClassCast(keluar.getClass(), masuk.getClass()));
        int indek= -1;
        if(keluar instanceof Comparable){
            for(int i= 0; i< ukuran; i++)
                if(((Comparable)elemen[i]).compareTo(keluar) == 0){
                    indek= i;
                    break;
                }
        } else{
            for(int i= 0; i< ukuran; i++)
                if(elemen[i].equals(keluar)){
                    indek= i;
                    break;
                }
        }
        if(indek != -1){
            elemen[indek]= masuk;
            return true;
        }
        return false;
    }


    public Tipe hapus(int indek){
        if((!bolehRumpang && indek >= ukuran) || indek >= elemen.length)
            return null;

        Tipe keluar= (Tipe) elemen[indek];
        if(!bolehRumpang){
            int ygGeser= ukuran -indek -1;
            System.arraycopy(elemen, indek+1, elemen, indek, ygGeser);
            elemen[--ukuran]= null;
            batas--;
        } else{
            elemen[indek]= null;
            ukuran--;
            if(indek == batas){
                for(int i= batas-1; i>= 0; i--)
                    if(elemen[i] != null) {
                        batas = i;
                        break;
                    }
            }
            if(indek < indIsi)
                indIsi= indek;
        }
        return keluar;
    }
    public boolean hapus(int daftarInd[]){
        int jmlDihapus= daftarInd.length;
        if(jmlDihapus == 0 || jmlDihapus > ukuran)
            return false;

        ArrayMod.urutkan(daftarInd);

        if(!bolehRumpang){
            int batas= this.batas +1;
            int ygGeser= batas -jmlDihapus;
            int jalanHapus= 0;
            int offset= 0;
            for(int i= daftarInd[0]; i<batas; i++)
                if(ygGeser == 0)
                    break;
                else if(i == daftarInd[jalanHapus]) {
                    jalanHapus= (jalanHapus+1) %jmlDihapus;
                    offset++;
                } else if(offset > 0) {
                    elemen[i -offset]= elemen[i];
                    ygGeser--;
                } else if(daftarInd[jalanHapus] > this.batas)
                    throw new IndexOutOfBoundsException(pesanOutOfBound(daftarInd[i]));

            this.batas-= jmlDihapus;
            while(jmlDihapus-- > 0){
                elemen[--ukuran]= null;
            }
        } else{
            for(int i= 0; i< daftarInd.length; i++){
                elemen[daftarInd[i]]= null;
                ukuran--;
            }
            if(elemen[batas]== null){
                for(int i= batas-1; i>= 0; i--)
                    if(elemen[i] != null) {
                        batas = i;
                        break;
                    }
            }
            if(daftarInd[0] < indIsi)
                indIsi= daftarInd[0];
        }
        return true;
    }

    public boolean hapus(Tipe keluar){
        int indek= -1;
        if(keluar instanceof Comparable){
            for(int i= 0; i< ukuran; i++)
                if(((Comparable)elemen[i]).compareTo(keluar) == 0){
                    indek= i;
                    break;
                }
        } else{
            for(int i= 0; i< ukuran; i++)
                if(elemen[i].equals(keluar)){
                    indek= i;
                    break;
                }
        }
        if(indek != -1){
            if(!bolehRumpang){
                int ygGeser= ukuran -indek -1;
                System.arraycopy(elemen, indek+1, elemen, indek, ygGeser);
                elemen[--ukuran]= null;
                batas= ukuran-1;
                return true;
            } else{
                elemen[indek]= null;
                ukuran--;
                if(indek == batas){
                    for(int i= batas-1; i>= 0; i--)
                        if(elemen[i] != null) {
                            batas = i;
                            break;
                        }
                }
                if(indek < indIsi)
                    indIsi= indek;
            }
        }
        return false;
    }
    public boolean hapus(Tipe keluar, boolean semuaYgMirip){
        if(!semuaYgMirip)
            return hapus(keluar);
        int jmlYgMirip= 0;
        int batas= (!bolehRumpang) ? ukuran : elemen.length;
//        int batasJmlYgMirip= ukuran;

        if(!bolehRumpang){
            if(keluar instanceof Comparable){
                for(int i= 0; i< batas; i++)
                    if(((Comparable)elemen[i]).compareTo(keluar) == 0) {
                        jmlYgMirip++;
                    } else if(jmlYgMirip > 0)
                        elemen[i-jmlYgMirip]= elemen[i];
            } else{
                for(int i= 0; i< batas; i++)
                    if(elemen[i].equals(keluar) /*&& jmlYgMirip < batasJmlYgMirip*/ ) {
//                ukuran--;
                        jmlYgMirip++;
                    } else if(jmlYgMirip > 0)
                        elemen[i-jmlYgMirip]= elemen[i];
            }

            this.batas-= jmlYgMirip;
            while(jmlYgMirip-- > 0)
                elemen[--ukuran]= null;
        } else{
            if(keluar instanceof Comparable){
                for(int i= 0; i< batas; i++)
                    if(((Comparable)elemen[i]).compareTo(keluar) == 0) {
                        elemen[i]= null;
                        ukuran--;
                        if(i < indIsi)
                            indIsi= i;
                    }
            } else{
                for(int i= 0; i< batas; i++)
                    if(elemen[i].equals(keluar) /*&& jmlYgMirip < batasJmlYgMirip*/ ) {
                        elemen[i]= null;
                        ukuran--;
                        if(i < indIsi)
                            indIsi= i;
                    }
            }
            if(elemen[batas]== null){
                for(int i= batas-1; i>= 0; i--)
                    if(elemen[i] != null) {
                        this.batas = i;
                        break;
                    }
            }
        }
        if(jmlYgMirip == 0)
            return false;
        return true;
    }


    public int indekKe(int ke, Tipe isi){
        int indek[]= indek(isi);
        if(ke >= indek.length)
            throw new IndexOutOfBoundsException(pesanOutOfBound(indek, ke));
        else if(indek.length == 0)
            return -1;
        return indek[ke];
    }
    public int indekAwal(Tipe isi){
        if(isi instanceof Comparable) {
            Comparable<Tipe> array[]= new Comparable[batas+1];
            salinArray(elemen, 0, array, 0, batas+1);
            return ArrayMod.cariIndDlmArray(array, isi);
        } else
            return ArrayMod.cariIndDlmArray(elemen, isi);
    }
    public int indekAkhir(Tipe isi){
        int indek[]= indek(isi);
        return indek[indek.length-1];
    }

    public int[] indek(Tipe isi){
        if(isi instanceof Comparable){
            Comparable<Tipe> array[]= new Comparable[batas+1];
            salinArray(elemen, 0, array, 0, batas+1);
            return ArrayMod.cariIndDlmArray(array, isi, true);
        }
        return ArrayMod.cariIndDlmArray(elemen, isi, true);
    }


    //true jika elemen rumpang/bolong-bolong;
    //false jika elemen gak rumpang
    private boolean rumpang(){
        int indekKosong[]= indekKosong(false);
        if(indekKosong.length == 0)
            return false;
        else
            return true;
    }

    public int[] indekKosong(boolean semua){
        int batas= this.batas +1;
        Object elemenPenuh[]= new Object[batas];
        for(int i= 0; i< batas; i++)
            elemenPenuh[i]= elemen[i];
        return ArrayMod.cariKosong(elemenPenuh, semua);
    }

    //jml elemen yang berisi (tidak null)
    public int ukuran(){
        return ukuran;
    }
    //ukuran seluruh array (jml tidak null + null)
    public int ukuranWadah(){
        return elemen.length;
    }

    //indek terakhir array yang terisi
    public int batas(){
        return batas;
    }

    public boolean traverse(){
        if(pngTraverse != null){
//            int batas= (!bolehRumpang) ? ukuran : (this.batas+1);
            int batas= this.batas+1;
            for(int i= 0; i< batas; i++)
                pngTraverse.traverse(i, (Tipe) elemen[i]);
            return true;
        }
        return false;
    }

    public void aturPenungguTraverse(PenungguTraverse p){
        pngTraverse= p;
    }
    public interface PenungguTraverse<Tipe>{
        void traverse(int indek, Tipe isi);
    }

    /*
    =========================
    Struktur Array Elemen
    =========================
    */
    public void aturBolehRumpang(boolean boleh){
        if(boleh= bolehRumpang)
            return;
        if(bolehRumpang){
            rampingkan();
            indIsi= -1;
        } else{
            indIsi= ukuran;
        }
        bolehRumpang= boleh;
    }
    public boolean bolehRumpang(){
        return bolehRumpang;
    }

    private boolean rampingkan(){
        int indekKosong[]= indekKosong(true);
        return rampingkan(indekKosong);
    }
    private boolean rampingkan(int indekKosong[]){
        if(indekKosong.length == 0)
            return false;

        boolean bolehRumpangAwal= bolehRumpang;
        bolehRumpang= false;
        hapus(indekKosong);
        bolehRumpang= bolehRumpangAwal;
        return true;
    }
    private void kosongSelanjutnya(){
        int batas= this.batas +2;
        for(int i= indIsi; i< batas; i++)
            if(elemen[i]== null){
                indIsi= i;
                break;
            }
    }


    /*
    =========================
    Exception
    =========================
    */
    private String pesanOutOfBound(int indek) {
        int ukuran= (!bolehRumpang) ? this.ukuran : elemen.length;
        return "Indek: " +indek +", Ukuran: " +ukuran;
    }
    private String pesanOutOfBound(int array[], int indek){
        int ukuran= array.length;
        return "Indek: " +indek +", Ukuran: " +ukuran;
    }

    private String pesanClassCast(String tipeObjek, String tipePrimitif) {
        return "Tipe Objek: " +tipeObjek +", Tipe Primitif: " +tipePrimitif;
    }
    private String pesanClassCast(Class objekA, Class objekB) {
        return "Tipe Objek A: "+objekA.getName() +", Tipe Objek B: " +objekB.getName();
    }

    //seperti System.arraycopy();
    private <T> void salinArray(Object[] sumber, int mulaiSumber, T[] tujuan, int mulaiTujuan, int pjg){
        int batas= mulaiSumber +pjg;
        for(int i= mulaiSumber; i< batas; i++)
            tujuan[mulaiTujuan++]= (T) sumber[i];
    }

/*
    private void perbaruiMapping(){
        if(mapping.length == elemen.length)
            return;
        int jalan= mapping.length;
        mapping = ArrayMod.ubahArray(mapping, elemen.length);

        while(jalan < mapping.length)
            mapping[jalan++]= -1;
    }


    public Object jadikanArrayPrimitif(){
        Object cek= elemen[0];
        Object array= null;

        if(cek instanceof Character) {
            array = new char[ukuran];
            System.out.println("yes!!");
        }
        else if(cek instanceof Integer) {
            int arrayInt[] = new int[ukuran];
            System.out.println("yes!!");
//            System.arraycopy(elemen, 0, arrayInt, 0, ukuran);
            for(int i= 0; i< ukuran; i++)
                arrayInt[i]= (Integer) elemen[i];
            return arrayInt;
        }
        else if(cek instanceof Byte)
            array= new byte[ukuran];
        else if(cek instanceof Boolean)
            array= new boolean[ukuran];
        else if(cek instanceof Short)
            array= new short[ukuran];
        else if(cek instanceof Long)
            array= new long[ukuran];
        else if(cek instanceof Float)
            array= new float[ukuran];
        else if(cek instanceof Double)
            array= new double[ukuran];

        System.arraycopy(elemen, 0, array, 0, ukuran);
        return array;
    }
*/
}
