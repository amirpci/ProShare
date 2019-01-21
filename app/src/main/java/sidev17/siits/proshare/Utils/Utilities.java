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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.lang.annotation.Target;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Model.Bidang;
import sidev17.siits.proshare.Model.Country;
import sidev17.siits.proshare.Model.Pengguna;
import sidev17.siits.proshare.Model.Permasalahan;
import sidev17.siits.proshare.Model.Problem.Solusi;
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

    public static String[] listNegarakeArray(ArrayList<Country> list){
        String arr[] = new String[list.size()];
        for(int i =0 ; i<arr.length;i++){
            arr[i] = list.get(i).getNegara();
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

    public static void loadBidang(final String bidang_id, final String languageID, final TextView txt_bidang, final Activity act) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.BIDANGKU,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            String bidang = jsonArr.getJSONObject(0).getString("bidang");
                            try {
//                                major.setText(com.rmtheis.yandtran.translate.Translate.execute(bidang, Language.ENGLISH, languageID));
                                txt_bidang.setText(Utilities.ubahBahasa(bidang, languageID, act));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(getActivity(), "Terjadi kesalahan jaringan!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> masalah = new HashMap<>();
                masalah.put("majority_id", bidang_id);
                return masalah;
            }
        };
        Volley.newRequestQueue(act).add(stringRequest);
    }

    public static void loadVoteCountProblem(final ImageView voteup, final ImageView votedown, final TextView txtCount, final Activity act, final String id_masalah){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.PROBLEM_VOTE_COUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONParser parser = new JSONParser();
                        try {
                            org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(response);
                            if(json.get("terhapus").toString().equals("1")){
                                if(json.get("type").toString().equals("1")){
                                    voteup.setColorFilter(ContextCompat.getColor(act, R.color.biruLaut), android.graphics.PorterDuff.Mode.SRC_IN);
                                }else{
                                    votedown.setColorFilter(ContextCompat.getColor(act, R.color.biruLaut), android.graphics.PorterDuff.Mode.SRC_IN);
                                }
                            }else{
                                voteup.setColorFilter(ContextCompat.getColor(act, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                                votedown.setColorFilter(ContextCompat.getColor(act, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                            }
                            txtCount.setText(json.get("vote_total").toString());
                        } catch (org.json.simple.parser.ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(act, Utilities.ubahBahasa("Failed to vote!", Utilities.getUserNegara(act), act), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> vote = new HashMap<>();
                vote.put("voter", Utilities.getUserID(act));
                vote.put("id_problem", id_masalah);
                return vote;
            }
        };
        Volley.newRequestQueue(act).add(stringRequest);
    }

    public static void voteProblem(final String type, final ImageView voteup, final ImageView votedown, final TextView txtCount, final Activity act, final String id_masalah){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.PROBLEM_VOTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONParser parser = new JSONParser();
                        try {
                            org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(response);
                            if(json.get("terhapus").toString().equals("1")){
                                if(!json.get("type").toString().equals(type)){
                                    Toast.makeText(act, Utilities.ubahBahasa("Vote changed!", Utilities.getUserNegara(act), act), Toast.LENGTH_SHORT).show();
                                    voteProblem(type, voteup, votedown, txtCount, act, id_masalah);
                                    if(json.get("type").toString().equals("1")){
                                        voteup.setColorFilter(ContextCompat.getColor(act, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                                    }else{
                                        votedown.setColorFilter(ContextCompat.getColor(act, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                                    }
                                }else {
                                    Toast.makeText(act, Utilities.ubahBahasa("Problem unvoted!", Utilities.getUserNegara(act), act), Toast.LENGTH_SHORT).show();
                                    loadVoteCountProblem(voteup, votedown,txtCount, act, id_masalah);
                                }
                            }else{
                                Toast.makeText(act, Utilities.ubahBahasa("Problem voted this!", Utilities.getUserNegara(act), act), Toast.LENGTH_SHORT).show();
                                loadVoteCountProblem(voteup, votedown,txtCount, act, id_masalah);
                            }
                        } catch (org.json.simple.parser.ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(act, Utilities.ubahBahasa("Failed to vote!", Utilities.getUserNegara(act), act), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> vote = new HashMap<>();
                vote.put("voter", Utilities.getUserID(act));
                vote.put("problem_id", id_masalah);
                vote.put("type", type);
                return vote;
            }
        };
        Volley.newRequestQueue(act).add(stringRequest);
    }

    public static void loadVoteCountSolusi(final ImageView voteup, final ImageView votedown, final TextView txtCount, final Activity act, final String id_solusi){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.SOLUTION_VOTE_COUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONParser parser = new JSONParser();
                        try {
                            org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(response);
                            if(json.get("terhapus").toString().equals("1")){
                                if(json.get("type").toString().equals("1")){
                                    voteup.setColorFilter(ContextCompat.getColor(act, R.color.biruLaut), android.graphics.PorterDuff.Mode.SRC_IN);
                                }else{
                                    votedown.setColorFilter(ContextCompat.getColor(act, R.color.biruLaut), android.graphics.PorterDuff.Mode.SRC_IN);
                                }
                            }else{
                                voteup.setColorFilter(ContextCompat.getColor(act, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                                votedown.setColorFilter(ContextCompat.getColor(act, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                            }
                            txtCount.setText(json.get("vote_total").toString());
                        } catch (org.json.simple.parser.ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(act, Utilities.ubahBahasa("Failed to vote!", Utilities.getUserNegara(act), act), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> vote = new HashMap<>();
                vote.put("voter", Utilities.getUserID(act));
                vote.put("id_solusi", id_solusi);
                return vote;
            }
        };
        Volley.newRequestQueue(act).add(stringRequest);
    }

    public static void voteSolusi(final String type, final ImageView voteup, final ImageView votedown, final TextView txtCount, final Activity act, final String id_solusi){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.SOLUTION_VOTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONParser parser = new JSONParser();
                        try {
                            org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(response);
                            if(json.get("terhapus").toString().equals("1")){
                                if(!json.get("type").toString().equals(type)){
                                    Toast.makeText(act, Utilities.ubahBahasa("Vote changed!", Utilities.getUserNegara(act), act), Toast.LENGTH_SHORT).show();
                                    voteSolusi(type, voteup, votedown, txtCount, act, id_solusi);
                                    if(json.get("type").toString().equals("1")){
                                        voteup.setColorFilter(ContextCompat.getColor(act, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                                    }else{
                                        votedown.setColorFilter(ContextCompat.getColor(act, R.color.abuLebihTua), android.graphics.PorterDuff.Mode.SRC_IN);
                                    }
                                }else {
                                    Toast.makeText(act, Utilities.ubahBahasa("Problem unvoted!", Utilities.getUserNegara(act), act), Toast.LENGTH_SHORT).show();
                                    loadVoteCountSolusi(voteup, votedown,txtCount, act, id_solusi);
                                }
                            }else{
                                Toast.makeText(act, Utilities.ubahBahasa("Problem voted this!", Utilities.getUserNegara(act), act), Toast.LENGTH_SHORT).show();
                                loadVoteCountSolusi(voteup, votedown,txtCount, act, id_solusi);
                            }
                        } catch (org.json.simple.parser.ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(act, Utilities.ubahBahasa("Failed to vote!", Utilities.getUserNegara(act), act), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> vote = new HashMap<>();
                vote.put("voter", Utilities.getUserID(act));
                vote.put("id_solusi", id_solusi);
                vote.put("type", type);
                return vote;
            }
        };
        Volley.newRequestQueue(act).add(stringRequest);
    }

    //Untuk masang foto.
    public static void setFotoDariUrl1(String url, ImageView gambar){
        Picasso.get().load(url).resize(200,200).centerCrop().into(gambar);
    }
    public static void setFotoDariUrl2(final ArrayList<String> url, final ImageView gambar1, final ImageView gambar2){
        Picasso.get().load(url.get(0)).resize(200,200).centerCrop().into(gambar1, new com.squareup.picasso.Callback(){

            @Override
            public void onSuccess() {
                Log.d("foto1", "sukses");
            }

            @Override
            public void onError(Exception e) {

            }
        });
        Picasso.get().load(url.get(1)).resize(200,200).centerCrop().into(gambar2, new com.squareup.picasso.Callback(){

            @Override
            public void onSuccess() {
                Log.d("foto1", "sukses");
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    public static void setFotoDariUrl3(final ArrayList<String> url, final ImageView gambar1, final ImageView gambar2, final ImageView gambar3){
        Picasso.get().load(url.get(0)).resize(200,200).centerCrop().into(gambar1, new com.squareup.picasso.Callback(){

            @Override
            public void onSuccess() {
                Log.d("foto1", "sukses");
            }

            @Override
            public void onError(Exception e) {
                Log.d("foto1 gagal", e.toString());
            }
        });
        Picasso.get().load(url.get(1)).resize(100,100).centerCrop().into(gambar2, new com.squareup.picasso.Callback(){

            @Override
            public void onSuccess() {
                Log.d("foto2", "sukses");
            }

            @Override
            public void onError(Exception e) {
                Log.d("foto2 gagal", e.toString());
            }
        });
        Picasso.get().load(url.get(2)).resize(100,100).centerCrop().into(gambar3, new com.squareup.picasso.Callback(){

            @Override
            public void onSuccess() {
                Log.d("foto3", "sukses");
            }

            @Override
            public void onError(Exception e) {
                Log.d("foto3 gagal", e.toString());
            }
        });
    }
    public static void loadFotoLampiran(final ArrayList<String> fotoLampiran, final ArrayList<String> videoLampiran, final LinearLayout lampiran, final Activity act, final String id_masalah){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.DAFTAR_FOTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            //  Toast.makeText(getActivity(), "Berhasil loading!", Toast.LENGTH_SHORT).show();
                            Solusi sol = new Solusi();
                            if(jsonArr.length()!=0){
                                for(int i = 0; i < jsonArr.length(); i++){
                                    org.json.JSONObject jsonObject = jsonArr.getJSONObject(i);
                                    fotoLampiran.add(jsonObject.getString("url_foto"));
                                }
                            }
                            loadVideoLampiran(fotoLampiran, videoLampiran, lampiran, act, id_masalah);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadVideoLampiran(fotoLampiran, videoLampiran, lampiran, act, id_masalah);
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadVideoLampiran(fotoLampiran, videoLampiran, lampiran, act, id_masalah);
               // Toast.makeText(act, Utilities.ubahBahasa("error cek solusi!", Utilities.getUserNegara(getApplicationContext()), getApplicationContext()), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> vote = new HashMap<>();
                vote.put("id_problem", id_masalah);
                return vote;
            }
        };
        Volley.newRequestQueue(act).add(stringRequest);
    }

    public static void loadVideoLampiran(final ArrayList<String> fotoLampiran, final ArrayList<String> videoLampiran, final LinearLayout lampiran, final Activity act, final String id_masalah) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.DAFTAR_VIDEO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            Solusi sol = new Solusi();
                            if(jsonArr.length()!=0){
                                for(int i = 0; i < jsonArr.length(); i++){
                                    org.json.JSONObject jsonObject = jsonArr.getJSONObject(i);
                                    videoLampiran.add(jsonObject.getString("url_video"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initViewLampiran(fotoLampiran, videoLampiran, lampiran, act);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                initViewLampiran(fotoLampiran, videoLampiran, lampiran, act);
              //  Toast.makeText(getApplicationContext(), Utilities.ubahBahasa("error cek solusi!", Utilities.getUserNegara(getApplicationContext()), getApplicationContext()), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> vote = new HashMap<>();
                vote.put("id_problem", id_masalah);
                return vote;
            }
        };
        Volley.newRequestQueue(act).add(stringRequest);
    }


    ///inisiasi foto dan video eprtanyaan
    public static void initViewLampiran(ArrayList<String> fotoLampiran, ArrayList<String> videoLampiran, LinearLayout lampiran, Activity act) {
        // fotoLampiran = directlink foto yang ada di server
        // videoLampiran = direclink video yang ada di server
        // viewGroup untuk tumbnail foto dan video di awal
        int panjangLampiran = fotoLampiran.size() + videoLampiran.size();
        Toast.makeText(act.getApplicationContext(), String.valueOf(panjangLampiran), Toast.LENGTH_LONG).show();
        if (panjangLampiran != 0) {
            // GetXMLTask task = new GetXMLTask();
            if (panjangLampiran == 1) {
                lampiran.setVisibility(View.VISIBLE);
                View v = act.getLayoutInflater().inflate(R.layout.lampiran_pertanyaan_1, null, false);
                ImageView pertanyaanGambar1 = v.findViewById(R.id.lampiran_pertanyaan_1);
                if(fotoLampiran.size()!=0)
                    Utilities.setFotoDariUrl1(fotoLampiran.get(0), pertanyaanGambar1);
                lampiran.addView(v);
            } else if (panjangLampiran == 2) {
                lampiran.setVisibility(View.VISIBLE);
                View v = act.getLayoutInflater().inflate(R.layout.lampiran_pertanyaan_2, null, false);
                ImageView pertanyaanGambar1 = v.findViewById(R.id.lampiran_pertanyaan_1);
                ImageView pertanyaanGambar2 = v.findViewById(R.id.lampiran_pertanyaan_2);
                if(fotoLampiran.size()==2){
                    Utilities.setFotoDariUrl2(fotoLampiran, pertanyaanGambar1, pertanyaanGambar2);
                }else if(fotoLampiran.size()==1)
                    Utilities.setFotoDariUrl1(fotoLampiran.get(0), pertanyaanGambar1);
                lampiran.addView(v);
            } else if (panjangLampiran == 3) {
                lampiran.setVisibility(View.VISIBLE);
                View v = act.getLayoutInflater().inflate(R.layout.lampiran_pertanyaan_3, null, false);
                ImageView pertanyaanGambar1 = v.findViewById(R.id.lampiran_pertanyaan_1);
                ImageView pertanyaanGambar2 = v.findViewById(R.id.lampiran_pertanyaan_2);
                ImageView pertanyaanGambar3 = v.findViewById(R.id.lampiran_pertanyaan_3);
                if(fotoLampiran.size()==3){
                    Utilities.setFotoDariUrl3(fotoLampiran, pertanyaanGambar1, pertanyaanGambar2, pertanyaanGambar3);
                }else if(fotoLampiran.size()==2){
                    Utilities.setFotoDariUrl2(fotoLampiran,  pertanyaanGambar1, pertanyaanGambar2);
                }else if(fotoLampiran.size()==1){
                    Utilities.setFotoDariUrl1(fotoLampiran.get(0), pertanyaanGambar1);
                }
                lampiran.addView(v);
            } else {
                lampiran.setVisibility(View.VISIBLE);
                View v = act.getLayoutInflater().inflate(R.layout.lampiran_pertanyaan_4, null, false);
                ImageView pertanyaanGambar1 = v.findViewById(R.id.lampiran_pertanyaan_1);
                ImageView pertanyaanGambar2 = v.findViewById(R.id.lampiran_pertanyaan_2);
                ImageView pertanyaanGambar3 = v.findViewById(R.id.lampiran_pertanyaan_3);
                TextView countLeft = v.findViewById(R.id.lampiran_count_left);
                if(fotoLampiran.size()>=3){
                    Utilities.setFotoDariUrl3(fotoLampiran, pertanyaanGambar1, pertanyaanGambar2, pertanyaanGambar3);
                }else if(fotoLampiran.size()==2){
                    Utilities.setFotoDariUrl2(fotoLampiran,  pertanyaanGambar1, pertanyaanGambar2);
                }else if(fotoLampiran.size()==1){
                    Utilities.setFotoDariUrl1(fotoLampiran.get(0), pertanyaanGambar1);
                }
                pertanyaanGambar3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //pindah untuk melihat list lampiran secara penuh
                    }
                });
                countLeft.setText(String.valueOf(panjangLampiran-3));
                lampiran.addView(v);
            }
        }
    }

    public static void updateFotoProfile(String url, final CircleImageView gambar){
        Picasso.get().load(url).resize(100,100).into(new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if(gambar!=null)
                    gambar.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
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
            case "2" : languageID=Language.INDONESIAN; break;
            case "3" : languageID=Language.ENGLISH; break;
            case "4" : languageID=Language.ENGLISH; break;
            case "5" : languageID=Language.JAPANESE; break;
        }
        com.rmtheis.yandtran.translate.Translate.setKey(c.getString(R.string.yandex_api_key));
        try {
            return com.rmtheis.yandtran.translate.Translate.execute(kata, Language.ENGLISH, languageID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kata;
    }

    public static  String ubahBahasaChat(String kata,String langID, Context c){
        Language languageID=null;
        switch (langID){
            case "2" : languageID=Language.INDONESIAN; break;
            case "3" : languageID=Language.ENGLISH; break;
            case "4" : languageID=Language.ENGLISH; break;
            case "5" : languageID=Language.JAPANESE; break;
        }
        com.rmtheis.yandtran.translate.Translate.setKey(c.getString(R.string.yandex_api_key));
        try {
            return com.rmtheis.yandtran.translate.Translate.execute(kata, languageID, Language.ENGLISH);
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
    //untuk mendapatkan nama pengguna
    public static String getUserNama(Context c){
        SharedPreferences prefs = c.getSharedPreferences(Konstanta.PENGGUNA_PREFS, MODE_PRIVATE);
        String nama = prefs.getString("nama", null);
        return  nama;
    }
    //untuk mendapatkan status pengguna
    public static long getUserBidang(Context c){
        SharedPreferences prefs = c.getSharedPreferences(Konstanta.PENGGUNA_PREFS, MODE_PRIVATE);
        long bidang = prefs.getLong("bidang", 0);
        return  bidang;
    }
    //untuk majority/bidang pengguna
    public static String getUserMajor(Context c){
        SharedPreferences prefs = c.getSharedPreferences(Konstanta.PENGGUNA_PREFS, MODE_PRIVATE);
        String major = prefs.getString("major", null);
        return  major;
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
                .getReference("Daftar Chat");
    }
    //untuk mendapatkan referensi key chat room list
    public static DatabaseReference getChatRoomRef(String chatListId, Context c){
        return FirebaseDatabase.getInstance()
                .getReference("Chat Room/"+Utilities.getUserID(c)+"/"+chatListId);
    }
    public static DatabaseReference getChatRef(){
        return FirebaseDatabase.getInstance()
                .getReference("Chats");
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

    //untuk mendapatkan user sekarang
    public static FirebaseUser getUserNow(){
        return FirebaseAuth.getInstance().getCurrentUser();
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
