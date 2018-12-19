package sidev17.siits.proshare.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rmtheis.yandtran.language.Language;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.annotation.Target;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Model.Bidang;
import sidev17.siits.proshare.Model.Pengguna;
import sidev17.siits.proshare.Model.Permasalahan;
import sidev17.siits.proshare.R;

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

    public static String[] listBidangkeArray(ArrayList<Bidang> list){
        String arr[] = new String[list.size()];
        for(int i =0 ; i<arr.length;i++){
            arr[i] = list.get(i).getBidang();
        }
        return arr;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void updateFoto(final String id_problem, final ImageView gambar, final Context c){
      //  Toast.makeText(c, "behasil load foto", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.DAFTAR_FOTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArr = null;
                        try {
                            jsonArr = new JSONArray(response);
                            for(int i=0; i<1; i++){
                                try {
                                   // Toast.makeText(c, "behasil load foto", Toast.LENGTH_SHORT).show();
                                    JSONObject jsonObject = jsonArr.getJSONObject(i);
                                    Picasso.get().load(jsonObject.getString("url_foto")).resize(100,100).into(new com.squareup.picasso.Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            gambar.setImageBitmap(getRoundedCornerBitmap(bitmap));
                                        }

                                        @Override
                                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c, "Terjadi kesalahan jaringan!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> masalah = new HashMap<>();
                masalah.put("id_problem", id_problem);
                return masalah;
            }
        };
        Volley.newRequestQueue(c).add(stringRequest);
    }

    public static void uploadFoto(final int urutanFile, final String[] alamatFile, final String[] urlFile, final String id, final Permasalahan masalah, final Activity c, final ProgressDialog uploading){
        uploading.setMessage("uploading..."+" "+String.valueOf(urutanFile+1)+"/"+String.valueOf(alamatFile.length)+" 0%");
        uploading.show();
        Uri file = Uri.fromFile(new File(alamatFile[urutanFile]));
        final StorageReference filepath = getProblemImagesRef(id, urutanFile);
        filepath.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String alamatUrl = uri.toString();
                        Toast.makeText(c, "tes alamat : "+alamatUrl, Toast.LENGTH_SHORT).show();
                        int urutanSekarang = urutanFile+1;
                        String[] url = new String[urutanSekarang];
                        url[urutanFile]=alamatUrl;
                        if(urutanFile<alamatFile.length){
                            for(int i=0; i<urutanFile; i++){
                                url[i]=urlFile[i];
                            }
                            if(urutanFile!=alamatFile.length-1){
                                uploadFoto(urutanSekarang, alamatFile, url, id, masalah, c, uploading);
                            }else{
                                tambahkanMasalah(c, id, masalah, uploading, 0);
                                tambahkanFotoMasalah(c, id, url);
                            }
                        }else{
                            Toast.makeText(c, "Photos succesfully uploaded!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(c, "foto ke "+String.valueOf(urutanFile+1)+" gagal diupload!", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                uploading.setMessage("uploading..."+" "+String.valueOf(urutanFile+1)+"/"+String.valueOf(alamatFile.length)+" "+(int)progress+"%");
            }
        });
    }
    public static void tambahkanFotoMasalah(final Context c, final String id_masalah, final String[] url_foto){
        Cache cache = new DiskBasedCache(c.getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue antrianRequest = new RequestQueue(cache, network);
        antrianRequest.start();
        for(int i = 0 ; i< url_foto.length; i++){
            Toast.makeText(c, "wes dijajal!", Toast.LENGTH_SHORT).show();
            final int posisi = i ;
            StringRequest sRequest = new StringRequest(Request.Method.POST, Konstanta.TAMBAH_PROBLEM_FOTO_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(c, "Wes iso nambahno foto!\n"+response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(c, "Jek gak iso nambahno foto!/n"+error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> foto = new HashMap<>();
                    foto.put("problem_id", id_masalah);
                    foto.put("url_foto", url_foto[posisi]);
                    foto.put("lokasi", "null");
                    return foto;
                }
            };
            Volley.newRequestQueue(c).add(sRequest);
        }
    }

    public static void tambahkanMasalah(final Activity c, final String id_masalah, final Permasalahan problem, final ProgressDialog uploading, final int status_foto){
        if(status_foto==1) {
            uploading.setMessage("Adding problem to the server!");
            uploading.show();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.TAMBAH_PROBLEM_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        uploading.dismiss();
                        Toast.makeText(c, response, Toast.LENGTH_SHORT).show();
                        c.finish();
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                uploading.dismiss();
                Toast.makeText(c, "Failed to add problem to server!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> masalah = new HashMap<>();
                masalah.put("id", cekNull(problem.getpid()));
                masalah.put("bidang", cekNull(problem.getmajority_id()));
                masalah.put("isitext",cekNull(problem.getproblem_desc()));
                masalah.put("judul", cekNull(problem.getproblem_title()));
                masalah.put("video_id", cekNull(problem.getvideo_id()));
                masalah.put("picture_id", cekNull(problem.getpicture_id()));
                masalah.put("tags", cekNull(problem.getTag()));
                masalah.put("problem_owner", cekNull(problem.getproblem_owner()));
                return masalah;
            }
        };
        Volley.newRequestQueue(c).add(stringRequest);
    }
    public static  String ubahBahasa(String kata,String langID, Context c){
        Language languageID=null;
        switch (langID){
            case "Indonesia" : languageID=Language.INDONESIAN; break;
            case "United States" : languageID=Language.ENGLISH; break;
            case "United Kingdom" : languageID=Language.ENGLISH; break;
            case "Japan" : languageID=Language.JAPANESE; break;
        }
        com.rmtheis.yandtran.translate.Translate.setKey(c.getString(R.string.yandex_api_key));
        try {
            return com.rmtheis.yandtran.translate.Translate.execute(kata, Language.ENGLISH, languageID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kata;
    }
    public static String cekNull(String str){
        if(str==null)
            return "null";
        return str;
    }
    // untuk meminta izin penyimpanan
    public static  boolean isStoragePermissionGranted(Activity act) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (act.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                // Log.v(TAG,"Permission is granted");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(act, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
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
    public static int getIntSharePref(String key, Context c){
        SharedPreferences prefs = c.getSharedPreferences(Konstanta.PENGGUNA_PREFS, MODE_PRIVATE);
        int val = prefs.getInt(key, 0);
        return val;
    }
    public static String getStringSharePref(String key, Context c){
        SharedPreferences prefs = c.getSharedPreferences(Konstanta.PENGGUNA_PREFS, MODE_PRIVATE);
        String val = prefs.getString(key, null);
        return val;
    }
    //untuk mendapatkan id user/email pengguna
    public static String getUserID(Context c){
        SharedPreferences prefs = c.getSharedPreferences(Konstanta.PENGGUNA_PREFS, MODE_PRIVATE);
        String id = prefs.getString("user", null);
        return  id;
    }
    //untuk mendapatkan status pengguna
    public static long getUserBidang(Context c){
        SharedPreferences prefs = c.getSharedPreferences(Konstanta.PENGGUNA_PREFS, MODE_PRIVATE);
        long bidang = prefs.getLong("bidang", 0);
        return  bidang;
    }
    //untuk mendapatkan status negara pengguna
    public static String getUserNegara(Context c){
        SharedPreferences prefs = c.getSharedPreferences(Konstanta.PENGGUNA_PREFS, MODE_PRIVATE);
        String lang = prefs.getString("lang", null);
        return lang;
    }
    //untuk menghapus data login
    public static void removeDataLogin(Context c){
        SharedPreferences.Editor editor = c.getSharedPreferences(Konstanta.PENGGUNA_PREFS, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
    //untuk mendapatkan referensi key chat list
    public static DatabaseReference getChatListRef(Context c){
      //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        return FirebaseDatabase.getInstance()
                .getReference("Daftar Chat"+"/"+getUserID(c).replace(".", ","));
    }
    //untuk mendapatkan referensi key chat room list
    public static DatabaseReference getChatRoomRef(String chatListId, Context c){
        return FirebaseDatabase.getInstance()
                .getReference("Chat Room/"+Utilities.getUserID(c)+"/"+chatListId);
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
    public static StorageReference getProblemImagesRef(String problemID, int urutan){
        return FirebaseStorage.getInstance().getReference("Problem/"+problemID+"/Photo/photo_"+String.valueOf(urutan));
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

    static class URL{
        private String url;
        public URL(){

        }
        public void setUrl(String url){
            this.url = url;
        }
        public String getUrl(){
            return url;
        }
    }
}
