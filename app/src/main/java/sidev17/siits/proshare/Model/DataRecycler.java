package sidev17.siits.proshare.Model;

public class DataRecycler<T> {

    private T dataArray[];
    private int ukuran;
    private int titikTengah;
    private int cursor;

    private boolean bisaDiperlebar= false;


    public DataRecycler(int ukuran){
        this.ukuran= ukuran;
        dataArray= (T[]) new Object[ukuran];
        cursor= 0;
    } public DataRecycler(int ukuran, boolean bisaDiperlebar){
        this.ukuran= ukuran;
        dataArray= (T[]) new Object[ukuran];
        cursor= 0;
        this.bisaDiperlebar= bisaDiperlebar;
    }

    private interface Recycler{
        void aksesData(int posisi);
    }

    public void bisaDiperlebar(boolean diperlebar){
        bisaDiperlebar= diperlebar;
    }
    public boolean bisaDiperlebarKah(){
        return bisaDiperlebar;
    }

    public void tambah(T elemen){
        if(dataArray[cursor % dataArray.length]== null) {
            if (cursor == dataArray.length && bisaDiperlebar)
                perlebarArray();
            dataArray[cursor % dataArray.length] = elemen;
            cursor++;
        } else {
            cursor++;
            tambah(elemen);
        }
    }
    public T ambil(int ind){
        if(ind < 0)
            throw new IndexOutOfBoundsException("ind = min");
        else if(dataArray[ind]== null)
            throw new IndexOutOfBoundsException("data[ind] = null");

        return dataArray[ind % dataArray.length];
    }
    public void hapus(int ind){
        if(ind < 0)
            throw new IndexOutOfBoundsException("ind = min");
        else if(dataArray[ind]== null)
            throw new IndexOutOfBoundsException("data[ind] = null");

        ind= ind % dataArray.length;
        T arraySmtr[]= dataArray;
        int indJalan= 0;
        for(int i= 0; i<arraySmtr.length; i++)
            if(i!= ind){
                dataArray[indJalan]= arraySmtr[i];
                indJalan++;
            }
        cursor--;
    }
    public T[] jadiArray(){
        return dataArray;
    }

    public void perlebarArray(){
        T arraySmtr[]= dataArray;
        dataArray= (T[]) new Object[dataArray.length +ukuran];
        for(int i= 0; i<arraySmtr.length; i++)
            dataArray[i]= arraySmtr[i];
    }
}
