package sidev17.siits.proshare.Tab_Menu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import sidev17.siits.proshare.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by USER on 02/05/2018.
 */

public class TanyaAct extends Fragment {
    private LinearLayout relatedQuestion, addQuestion, loading;
    private EditText search_input,question_input;
    private TextView fileName;
    private ImageView search_btn, uploadPhoto, uploadedPhoto;
    private Button add_question;
    private ProgressBar uploadPhotoProgress;
    private ProgressDialog addQuestionLoading;
    private DatabaseReference dataRef;
    private StorageReference storageRef;
    private static final int UpPhotoID = 2;
    private boolean photoDiambil = false, videoDiambil = false;
    private int QuestionID=0;
    private Uri uriPhoto;
    private String Photo_url;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tanya, container, false);
        storageRef = FirebaseStorage.getInstance().getReference();
        dataRef = FirebaseDatabase.getInstance().getReference();
        relatedQuestion = (LinearLayout)v.findViewById(R.id.tanya_related_q);
        addQuestion = (LinearLayout)v.findViewById(R.id.tanya_add_question);
        search_input = (EditText)v.findViewById(R.id.tanya_search);
        question_input = (EditText)v.findViewById(R.id.tanya_desc);
        fileName = (TextView)v.findViewById(R.id.tanya_file_name);
        search_btn = (ImageView)v.findViewById(R.id.tanya_cari_icon);
        uploadPhoto = (ImageView)v.findViewById(R.id.tanya_add_photo);
        uploadedPhoto = (ImageView)v.findViewById(R.id.tanya_uploaded_photo);
        add_question = (Button)v.findViewById(R.id.tanya_addQuestion_btn);
        uploadPhotoProgress = (ProgressBar) v.findViewById(R.id.tanya_upPhoto_loading);
        loading = (LinearLayout)v.findViewById(R.id.tanya_progress);

        relatedQuestion.setVisibility(View.GONE);
        addQuestion.setVisibility(View.GONE);
        //buat izin storage untuk API 23++
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.animate().translationY(0).alpha(0.0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loading.setVisibility(View.GONE);
                    }
                });
                searchQuestion(search_input.getText().toString());
            }
        });

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //buat izin storage untuk API 23++
                //ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                Intent add = new Intent(Intent.ACTION_PICK);
                add.setType("image/*");
                startActivityForResult(add, UpPhotoID);
            }
        });

        add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(question_input.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Please fill the blank!", Toast.LENGTH_LONG).show();
                }else{
                    addQuestionLoading = new ProgressDialog(getActivity());
                    addQuestionLoading.setMessage("Adding question...");
                    addQuestionLoading.show();
                    if(tambahPertanyaan()){
                        addQuestionLoading.dismiss();
                        Toast.makeText(getActivity(), "Question added!", Toast.LENGTH_SHORT).show();
                        transisiFadeOutBawah(addQuestion);
                        transisiFadeIn(loading);
                    }
                }
            }
        });
        return v;
    }
    //cuma method percobaan
    void searchQuestion(final String input){
        dataRef.child("search").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(input.equals(dataSnapshot.getValue(String.class))){
                    transisiFadeIn(relatedQuestion);
                    transisiFadeOutBawah(addQuestion);
                }else{
                    transisiFadeIn(addQuestion);
                    transisiFadeOutAtas(relatedQuestion);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    boolean tambahPertanyaan(){
        getQuestionID();
        if(photoDiambil){
            dataRef.child("Problems").child(String.valueOf(QuestionID)).child("photo").setValue(Photo_url);
        }else{
            dataRef.child("Problems").child(String.valueOf(QuestionID)).child("photo").setValue("-");
        }
        if(!videoDiambil){
            dataRef.child("Problems").child(String.valueOf(QuestionID)).child("video").setValue("-");
        }
        dataRef.child("Problems").child(String.valueOf(QuestionID)).child("text").setValue(question_input.getText().toString());
        return true;
    }

    void transisiFadeOutBawah(final View v){
        v.animate()
                .translationY(v.getHeight())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.clearAnimation();
                        v.setVisibility(View.GONE);
                    }
                });
    }

    void transisiFadeOutAtas(final View v){
        v.animate()
                .translationY(-v.getHeight())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.clearAnimation();
                        v.setVisibility(View.GONE);
                    }
                });
    }

    void transisiFadeIn(final View v){
        v.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.setVisibility(View.VISIBLE);
                    }
                });
    }
    //untuk generate ID pertanyaan
    void getQuestionID(){
        Query lastQuery = dataRef.child("Problems").orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot lastChild=null;
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    lastChild=child;
                }
                QuestionID=Integer.parseInt(lastChild.getKey())+1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==UpPhotoID && resultCode==RESULT_OK){
            uploadPhoto.setVisibility(View.INVISIBLE);
            uploadPhotoProgress.setVisibility(View.VISIBLE);
            String Photo_name;
            uriPhoto = data.getData();
            if(uriPhoto.getLastPathSegment().length()>=10){
                Photo_name="..."+uriPhoto.getLastPathSegment().substring(uriPhoto.getLastPathSegment().length()-10);
            }else{
                Photo_name="..."+uriPhoto.getLastPathSegment();
            }
            fileName.setText(Photo_name);
            getQuestionID();
            StorageReference filepath = storageRef.child("Photos").child(String.valueOf(QuestionID)).child(uriPhoto.getLastPathSegment());
            filepath.putFile(uriPhoto).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadPhoto.setVisibility(View.GONE);
                    uploadPhotoProgress.setVisibility(View.GONE);
                    photoDiambil=true;
                    Photo_url = taskSnapshot.getDownloadUrl().toString();
                    Glide.with(getActivity()).load(Photo_url).into(uploadedPhoto);
                    uploadedPhoto.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), "Question Added", Toast.LENGTH_LONG).show();
                   // dataRef.child("Problems").child(String.valueOf(QuestionID)).child("photos").setValue(Photo_url);
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

