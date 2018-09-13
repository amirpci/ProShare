package sidev17.siits.proshare.Modul.Worker.Tab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;

import sidev17.siits.proshare.Login_Register.Login;
import sidev17.siits.proshare.Model.Pengguna;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Utilities;

import com.rmtheis.yandtran.language.Language;

import static android.app.Activity.RESULT_OK;

/**
 * Created by USER on 02/05/2018.
 */

public class ProfileActWkr extends Fragment {
    //private DatabaseReference dataRef;
   // private StorageReference storageRef;
    private TextView nama, bidang, status, terjawab, rating, penilai;
    private String idUser,pp_url;
    private ImageView signout,pp_view, addPhoto;
    private ProgressDialog loading,uploading;
    private de.hdodenhof.circleimageview.CircleImageView profile_photo;
    private static final int ambilPhoto=2;
    private Uri alamatPhoto;
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
        //dataRef = FirebaseDatabase.getInstance().getReference("User");
        //storageRef = FirebaseStorage.getInstance().getReference("User");
        uploading = new ProgressDialog(getActivity());
        nama = (TextView)v.findViewById(R.id.nama_profile);
        bidang = (TextView)v.findViewById(R.id.bidang_profile);
        status = (TextView)v.findViewById(R.id.status_profile);
        terjawab = (TextView)v.findViewById(R.id.answered_profile);
        rating = (TextView)v.findViewById(R.id.rating_profile);
        penilai = (TextView)v.findViewById(R.id.rater_profile);
        signout = (ImageView)v.findViewById(R.id.signOut);
        pp_view = (ImageView)v.findViewById(R.id.pp_preview);
        addPhoto = (ImageView)v.findViewById(R.id.addphoto_profile);
        profile_photo = (de.hdodenhof.circleimageview.CircleImageView)v.findViewById(R.id.img_profile);
        profile_photo.setVisibility(View.GONE);
        idUser = FirebaseAuth.getInstance().getUid();
        loadData();
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(Intent.ACTION_PICK);
                add.setType("image/*");
                startActivityForResult(add, ambilPhoto);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.removeDataLogin(getActivity());
                //FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        return v;
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
                        case "Indonesia" : languageID=Language.INDONESIAN; break;
                        case "United States" : languageID=Language.ENGLISH; break;
                        case "United Kingdom" : languageID=Language.ENGLISH; break;
                        case "Japan" : languageID=Language.JAPANESE; break;
                    }
                    com.rmtheis.yandtran.translate.Translate.setKey(getString(R.string.yandex_api_key));
                    try {
                        bidang.setText(com.rmtheis.yandtran.translate.Translate.execute(bidang_, Language.ENGLISH, languageID));
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
