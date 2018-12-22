package sidev17.siits.proshare.Modul.Worker.Tab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.method.KeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Model.Pengguna;
import sidev17.siits.proshare.Model.View.ImgViewTouch;
import sidev17.siits.proshare.Model.View.MenuBarView;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Modul.SettingAct;
import sidev17.siits.proshare.Utils.ArrayModification;
import sidev17.siits.proshare.Utils.PackBahasa;
import sidev17.siits.proshare.Utils.Utilities;
import sidev17.siits.proshare.Utils.Warna;

import com.rmtheis.yandtran.language.Language;

import org.json.JSONArray;
import org.json.JSONException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by USER on 02/05/2018.
 */

public class ProfileActWkr extends Fragment {
    //private DatabaseReference dataRef;
   // private StorageReference storageRef;
    private EditText nama;
    private ImageView editNama;
    private TextView status, terjawab, rating, penilai;
    private TextView[] textProfile;
    private String idUser,pp_url;
    private ImageView signout,pp_view, addPhoto;
    private ProgressDialog loading,uploading;
    private de.hdodenhof.circleimageview.CircleImageView profile_photo;
    private static final int ambilPhoto=2;
    private Uri alamatPhoto;

    private MenuBarView menuBar;

    private Drawable bgAwalSpinner;
    private Spinner bidang;
    private String bidangUser;

    private ListView daftarSkill;
    private String skill[];
    private int tingkatRekom[];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_wkr, container, false);
        // untuk menghindari kesalahan multithreading dalam network connection
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        textProfile = new TextView[4];
        textProfile[0] = v.findViewById(R.id.txt_profile_0);
        textProfile[1] = v.findViewById(R.id.txt_profile_1);
        textProfile[2] = v.findViewById(R.id.txt_profile_2);
        textProfile[3] = v.findViewById(R.id.txt_profile_3);
        //dataRef = FirebaseDatabase.getInstance().getReference("User");
        //storageRef = FirebaseStorage.getInstance().getReference("User");
        uploading = new ProgressDialog(getActivity());
        nama = (EditText) v.findViewById(R.id.nama_profile);
        editNama= v.findViewById(R.id.prfile_edit_nama);
        initEditNama();
        bidang = v.findViewById(R.id.bidang_profile);
        status = (TextView)v.findViewById(R.id.status_profile);
        terjawab = (TextView)v.findViewById(R.id.answered_profile);
        rating = (TextView)v.findViewById(R.id.rating_profile);
        penilai = (TextView)v.findViewById(R.id.rater_profile);
//        signout = (ImageView)v.findViewById(R.id.signOut);
        pp_view = (ImageView)v.findViewById(R.id.pp_preview);
        addPhoto = (ImageView)v.findViewById(R.id.addphoto_profile);
        menuBar= v.findViewById(R.id.opsi_profil);
        daftarSkill= v.findViewById(R.id.skill_daftar);
        profile_photo = (de.hdodenhof.circleimageview.CircleImageView)v.findViewById(R.id.img_profile);
        profile_photo.setVisibility(View.GONE);
        idUser = FirebaseAuth.getInstance().getUid();
        loadData();
        gantiBahasa(getActivity());
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(Intent.ACTION_PICK);
                add.setType("image/*");
                startActivityForResult(add, ambilPhoto);
            }
        });
/*
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Utilities.removeDataLogin(getActivity());
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                intent.putExtra(Konstanta.LOGIN_INTENT, Konstanta.LOGIN_LOGOUT);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
*/
        initMenuBar();
        initBidang();

        return v;
    }

    void initBidang(){
        //ambil semua bidang dari server
        String bidangSeluruh[]= new String[0];

        bidang.setAdapter(new AdapterBidang(bidangSeluruh));
        bidang.setSelection(ArrayModification.cariIndDlmArray(bidangSeluruh, bidangUser));
        bgAwalSpinner= bidang.getBackground();
        enablePilihBidang(false);
    }
    void enablePilihBidang(boolean enable){
        bidang.setEnabled(enable);
        if(enable)
            bidang.setBackground(bgAwalSpinner);
        else
            bidang.setBackground(null);
    }

    class AdapterBidang extends BaseAdapter{

        String bidang[];

        AdapterBidang(String bidang[]){
            this.bidang= bidang;
        }

        @Override
        public int getCount() {
            return bidang.length;
        }

        @Override
        public Object getItem(int position) {
            return bidang[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGroup.LayoutParams lp= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            TextView tv= new TextView(getContext());
            tv.setLayoutParams(lp);
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setGravity(Gravity.CENTER);
            tv.setText(bidang[position]);

            return tv;
        }
    }
/*
==========
Belum selesai masalah untuk menentukan apakah user sudah me-rekom skill user lain
==========
*/
    void initDaftarSkill(){

    }
    class AdapterSkill extends BaseAdapter{
        @Override
        public int getCount() {
            return skill.length;
        }

        @Override
        public Object getItem(int position) {
            return skill[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vSkill= getLayoutInflater().inflate(R.layout.model_skill_recom, null);
            TextView nama= vSkill.findViewById(R.id.skill_nama);
            TextView tingkat= vSkill.findViewById(R.id.skill_tingkat);
            ImageView tombolRekom= vSkill.findViewById(R.id.skill_recommend);

            nama.setText(skill[position]);
            tingkat.setText(Integer.toString(tingkatRekom[position]));

            return vSkill;
        }
    }

    void initMenuBar(){
        int gmbOpsi[]= {R.drawable.icon_edit,
                R.drawable.icon_setting};
        int tersedia[]= {menuBar.ITEM_TERSEDIA, menuBar.ITEM_TERSEDIA};
        menuBar.aturGmbItem(gmbOpsi);
        menuBar.aturItemTersedia(tersedia);
//        menuBar.aturArahBar(menuBar.ARAH_VERTICAL);
        menuBar.aturLetakRelatif(menuBar.BAR_DI_BAWAH);
        menuBar.aturWarnaTersedia("#FFFFFF");
        menuBar.aturWarnaTakTersedia();
        menuBar.aturWarnaKuat(Warna.ambilStringWarna(getResources().getColor(R.color.biruLaut)));
        menuBar.aturPenungguKlikBar(new MenuBarView.PenungguKlik_BarView() {
            @Override
            public void klik(MenuBarView v, boolean menuDitampilkan) {
                if(!menuDitampilkan) {
                    v.sembunyikanLatar();
                    v.setBackgroundColor(getResources().getColor(R.color.abuTua));
                }
                else{
                    v.latarIndukAwal();
                }
            }
        });
        menuBar.aturAksiKlikItem(0, new ImgViewTouch.PenungguKlik() {
            @Override
            public void klik(View v) {
                if(!menuBar.isSelected()) {
                    editProfil(true);
                    menuBar.aturGambarInduk(R.drawable.obj_centang);
                    menuBar.aturWarnaInduk("#ffffff");
                    menuBar.aturWarnaLatar("#4972AD");
                    menuBar.setSelected(true);
                    menuBar.klik();
                } else{
                    editProfil(false);
                    menuBar.aturGambarInduk(R.drawable.obj_titik_tiga_horizontal);
                    menuBar.aturWarnaInduk("#ffffff");
                    menuBar.aturWarnaLatar("#C9C9C9");
                    menuBar.setSelected(false);
                    simpanProfil(nama.getText().toString(), (String) bidang.getSelectedItem());
                }
            }
        });
        menuBar.aturAksiKlikItem(1, new ImgViewTouch.PenungguKlik() {
            @Override
            public void klik(View v) {
/*
                Utilities.removeDataLogin(getActivity());
                //FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                intent.putExtra(Konstanta.LOGIN_INTENT, Konstanta.LOGIN_LOGOUT);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
*/
                startActivity(new Intent(getContext(), SettingAct.class));
            }
        });
        menuBar.latarIndukAwal();
    }
    void editProfil(boolean enable){
        int visibility= (enable) ? View.VISIBLE : View.GONE;
        editNama.setVisibility(visibility);
        enablePilihBidang(enable);
    }
    void simpanProfil(String nama, String bidang){
        //lakukan sesuatu
    }

    void gantiBahasa(Context c){
        for(int i=0;i<textProfile.length;i++){
            textProfile[i].setText(Utilities.ubahBahasa(PackBahasa.BahasaProfile[i], Utilities.getUserNegara(c), c));
            Toast.makeText(c, "test" + String.valueOf(i), Toast.LENGTH_SHORT).show();
        }
    }
    void initEditNama(){
        editNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editNama.isSelected()) {
                    editNama.setSelected(true);
                    enableEditText(nama, InputType.TYPE_TEXT_FLAG_CAP_WORDS, true);
                    editNama.setImageResource(R.drawable.obj_centang);
                    editNama.setColorFilter(getResources().getColor(R.color.biruLaut));
                } else{
                    //Saat simpan perubahan
                    editNama.setSelected(false);
                    enableEditText(nama, InputType.TYPE_NULL, false);
                    editNama.setImageResource(R.drawable.obj_pensil);
                    editNama.setColorFilter(getResources().getColor(R.color.abuLebihTua));
                }
            }
        });
        enableEditText(nama, InputType.TYPE_NULL, false);
    }
    void enableEditText(EditText ed, final int inputType, boolean enable){
        if(enable){
            ed.setKeyListener(new KeyListener() {
                @Override
                public int getInputType() {
                    return inputType;
                }

                @Override
                public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
                    return true;
                }

                @Override
                public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
                    return true;
                }

                @Override
                public boolean onKeyOther(View view, Editable text, KeyEvent event) {
                    return true;
                }

                @Override
                public void clearMetaKeyState(View view, Editable content, int states) {

                }
            });
            ed.getBackground().setAlpha(255);
            ed.getBackground().setTint(Color.parseColor("#000000"));
            InputMethodManager imm= (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(ed, InputMethodManager.SHOW_IMPLICIT);
            Selection.setSelection(ed.getText(), ed.length());
        } else{
            ed.setInputType(InputType.TYPE_NULL);
            ed.setKeyListener(null);
            ed.getBackground().setAlpha(0);
            InputMethodManager imm= (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
        }
    }

    void loadUploadedPP(){
        Utilities.getUserRef(Utilities.getUserID(getActivity())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String alamatPP = dataSnapshot.child("photoProfile").getValue(String.class);
                if(alamatPP!=null){
                    Glide.with(getActivity()).load(alamatPP).into(profile_photo);
                    pp_view.setVisibility(View.GONE);
                    addPhoto.setVisibility(View.GONE);
                    profile_photo.setVisibility(View.VISIBLE);
                    uploading.dismiss();
                    Toast.makeText(getActivity(), "Profile photo updated!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
    }

    void loadData(){
        loading = new ProgressDialog(getActivity());
        loading.setMessage("loading...");
        loading.show();
        ConnectivityManager connectivity = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
        if(activeNetwork!=null){
            Utilities.getUserRef(Utilities.getUserID(getActivity())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Pengguna user = dataSnapshot.getValue(Pengguna.class);
                    String nama_ = user.getNama();
                    long status_ = user.getStatus();
                    String photo_ = user.getPhotoProfile();
                    String bidang_ = user.getBidang();
                    String langID = user.getNegara();
                    Language languageID=null;
                    switch (langID){
                        case "2" : languageID=Language.INDONESIAN; break;
                        case "3" : languageID=Language.ENGLISH; break;
                        case "4" : languageID=Language.ENGLISH; break;
                        case "5" : languageID=Language.JAPANESE; break;
                    }
                    com.rmtheis.yandtran.translate.Translate.setKey(getString(R.string.yandex_api_key));
                    try {
                        loadBidang(bidang_, languageID);
                        switch ((int)status_){
                            case 200 : status.setText(com.rmtheis.yandtran.translate.Translate.execute("Worker", Language.ENGLISH, languageID)); break;
                            case 201 : status.setText(com.rmtheis.yandtran.translate.Translate.execute("Expert", Language.ENGLISH, languageID)); break;
                            case 202 : status.setText(com.rmtheis.yandtran.translate.Translate.execute("Verified Expert", Language.ENGLISH, languageID)); break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    nama.setText(nama_);
                    if(photo_!=null){
                        Glide.with(getActivity()).load(photo_).into(profile_photo);
                        pp_view.setVisibility(View.GONE);
                        addPhoto.setVisibility(View.GONE);
                        profile_photo.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(getActivity(), "coba", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}

            });
        }else{
            loading.dismiss();
            Toast.makeText(getActivity(), "No internet connection found!", Toast.LENGTH_LONG).show();
        }
        /*
        dataRef.child(idUser).child("Score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String rating_ = String.valueOf(map.get("Rating"));
                String rater_ = String.valueOf(map.get("Rater"));
                String terjawab_ = String.valueOf(map.get("Answered"));

                penilai.setText(rater_);
                rating.setText(rating_);
                terjawab.setText(terjawab_);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */
    }

    private void loadBidang(final String bidang_, final Language languageID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Konstanta.BIDANGKU,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            String bidang = jsonArr.getJSONObject(0).getString("bidang");
                            try {
//                                major.setText(com.rmtheis.yandtran.translate.Translate.execute(bidang, Language.ENGLISH, languageID));
                                bidangUser= com.rmtheis.yandtran.translate.Translate.execute(bidang, Language.ENGLISH, languageID);
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
                Toast.makeText(getActivity(), "Terjadi kesalahan jaringan!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> masalah = new HashMap<>();
                masalah.put("majority_id", bidang_);
                return masalah;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==ambilPhoto && resultCode==RESULT_OK){
            uploading.setMessage("uploading...");
            uploading.show();
            alamatPhoto = data.getData();
            StorageReference filepath = Utilities.getProfileImageStorageRef(getActivity()).child("myProfile");
            filepath.putFile(alamatPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //    pp_url = taskSnapshot.getDownloadUrl().toString();
                    Utilities.getProfileImageRef(getActivity()).setValue(pp_url);
                    loadUploadedPP();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Upload failed!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
