package sidev17.siits.proshare.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Model.Pengguna;

import static android.content.Context.MODE_PRIVATE;

public class Utilities {

    private static Pengguna currentUser;
    //untuk mendapatkan referensi pengguna
    public static DatabaseReference getUserRef(String email){
        return FirebaseDatabase.getInstance()
                .getReference(Konstanta.penggunaKey)
                .child(email.replace(".",","));
    }
    public static DatabaseReference getUserRef(){
        return FirebaseDatabase.getInstance()
                .getReference(Konstanta.penggunaKey);
    }
    //untuk mendapatkan referensi key permasalahan
    public static DatabaseReference getProblemRef(){
        return FirebaseDatabase.getInstance()
                .getReference(Konstanta.permasalahanKey);
    }
    //untuk mendapatkan referensi key solusi
    public static DatabaseReference getSolusiRef(){
        return FirebaseDatabase.getInstance()
                .getReference(Konstanta.solusiKey);
    }
    //untuk mendapatkan referensi key vote permasalahan
    public static DatabaseReference getPermasalahanVoteRef(String problemId){
        return FirebaseDatabase.getInstance()
                .getReference(Konstanta.voteProblem+"/"+problemId);
    }
    //untuk mendapatkan referensi key vote solusi
    public static DatabaseReference getSolusiVoteRef(String solusiId){
        return FirebaseDatabase.getInstance()
                .getReference(Konstanta.voteSolusi+"/"+solusiId);
    }
    //untuk mendapatkan id user/email pengguna
    public static String getUserID(Context c){
        SharedPreferences prefs = c.getSharedPreferences(Konstanta.PENGGUNA_PREFS, MODE_PRIVATE);
        String id = prefs.getString("user", null);
        return  id;
    }
    //untuk mendapatkan status pengguna
    public static String getUserBidang(Context c){
        SharedPreferences prefs = c.getSharedPreferences(Konstanta.PENGGUNA_PREFS, MODE_PRIVATE);
        String bidang = prefs.getString("bidang", null);
        return  bidang;
    }
    //untuk menghapus data login
    public static void removeDataLogin(Context c){
        SharedPreferences.Editor editor = c.getSharedPreferences(Konstanta.PENGGUNA_PREFS, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
    //untuk mendapatkan data pengguna
    public static Pengguna getCurrentUser(Context c){
        getUserRef(getUserID(c)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(Pengguna.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return currentUser;
    }
    //untuk mendapatkan key unik
    public static String getUid(){
        String path = FirebaseDatabase.getInstance().getReference().push().toString();
        return path.substring(path.lastIndexOf("/") + 1);
    }
    //untuk mendapatkan referensi photo profile
    public static StorageReference getProfileImageStorageRef(Context c){
        return FirebaseStorage.getInstance().getReference(Konstanta.photoProfileKey+"/"+getUserID(c));
    }
    //referensi url pp
    public static DatabaseReference getProfileImageRef(Context c){
        return getUserRef(getUserID(c)).child("photoProfile");
    }
    //untuk mendapatkan referensi photo problem
    public static StorageReference getProblemImagesRef(String problemID){
        return FirebaseStorage.getInstance().getReference("Problem/"+problemID+"photos");
    }
    //untuk mendapatkan referensi video problem
    public static StorageReference getProblemVideosRef(String problemID){
        return FirebaseStorage.getInstance().getReference("Problem/"+problemID+"videos");
    }
    //untuk referensi daftar pertanyaan yang pernah diajukan
    public static DatabaseReference getMyQuestionRef(Context c){
        return FirebaseDatabase.getInstance().getReference(Konstanta.pertanyaanSayaKey)
                .child(getUserID(c));
    }
    //untuk mendapatkan track record dari pengguna yang kita miliki
    public static DatabaseReference getMyRecordRef(Context c){
        return FirebaseDatabase.getInstance().getReference(Konstanta.recordPengguna)
                .child(getUserID(c));
    }

    public static void addToMyRecord(String node, final String id, Context c){
        getMyRecordRef(c).child(node).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ArrayList<String> myRecordCollection;
                if(mutableData.getValue() == null){
                    myRecordCollection = new ArrayList<String>(1);
                    myRecordCollection.add(id);
                }else{
                    myRecordCollection = (ArrayList<String>) mutableData.getValue();
                    myRecordCollection.add(id);
                }

                mutableData.setValue(myRecordCollection);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

}
