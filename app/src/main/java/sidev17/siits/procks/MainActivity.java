package sidev17.siits.procks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sidev17.siits.procks.Modul.Expert.MainActivityExprt;
import sidev17.siits.procks.Modul.Worker.MainActivityWkr;

public class MainActivity extends AppCompatActivity {

    private String AccountType = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String id_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("User/"+id_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AccountType = dataSnapshot.child("Type").getValue().toString();
                if(AccountType.equals("Expert")){
                    startActivity(new Intent(getApplicationContext(), MainActivityExprt.class));
                }else{
                    startActivity(new Intent(getApplicationContext(), MainActivityWkr.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
