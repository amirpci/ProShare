package sidev17.siits.procks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import sidev17.siits.procks.R;

public class Share_knowledge extends AppCompatActivity {
    private EditText Problem_desc;
    private Button add_newProblem, add_Photos, add_Videos;
    private static final int GAL_INTENT=2;
    private StorageReference storageRef;
    private DatabaseReference db;
    private String PID,Photos_name,Photos_url,Problem_description;
    private boolean Photos_picked=false;
    private Uri uriPhotos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_knowledge);
        storageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance().getReference();
        Problem_desc = (EditText)findViewById(R.id.problem_desc);
        add_newProblem = (Button)findViewById(R.id.add_new_problem);
        add_Photos = (Button)findViewById(R.id.add_photos);
        add_Videos = (Button)findViewById(R.id.add_videos);

        add_Photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(Intent.ACTION_PICK);
                add.setType("image/*");
                startActivityForResult(add, GAL_INTENT);
            }
        });
        add_newProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Photos_picked){
                    StorageReference filepath = storageRef.child("Photos").child(PID).child(uriPhotos.getLastPathSegment());
                    filepath.putFile(uriPhotos).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Photos_url = taskSnapshot.getDownloadUrl().toString();
                            Toast.makeText(getApplicationContext(), "Problem Added", Toast.LENGTH_LONG).show();
                            db.child("Problems").child(PID).child("Photos").setValue(Photos_url);
                        }
                    });
                }
                db.child("Problems").child(PID).child("Text").setValue(Problem_desc.getText().toString());
            }
        });
        Query lastQuery = db.child("Problems").orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot lastChild=null;
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    lastChild=child;
                }
                PID=String.valueOf(Integer.parseInt(lastChild.getKey())+1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });
        ActivityCompat.requestPermissions(Share_knowledge.this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GAL_INTENT && resultCode==RESULT_OK){
            uriPhotos = data.getData();
            if(uriPhotos.getLastPathSegment().length()>=10){
                Photos_name="..."+uriPhotos.getLastPathSegment().substring(uriPhotos.getLastPathSegment().length()-10);
            }else{
                Photos_name="..."+uriPhotos.getLastPathSegment();
            }
            add_Photos.setText(Photos_name);
            Photos_picked=true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Share_knowledge.this, "Permission granted!", Toast.LENGTH_SHORT).show();
                } else {
                   Toast.makeText(Share_knowledge.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
