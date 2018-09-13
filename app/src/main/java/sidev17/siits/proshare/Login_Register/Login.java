package sidev17.siits.proshare.Login_Register;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Model.Pengguna;
import sidev17.siits.proshare.Modul.Expert.MainActivityExprt;
import sidev17.siits.proshare.MainActivity;
import sidev17.siits.proshare.Modul.Worker.MainActivityWkr;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Utilities;

public class Login extends AppCompatActivity {
    private EditText Email,Password;
    private TextView Register;
    private Button Login;
    private ProgressDialog progress;
    private String AccountType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progress = new ProgressDialog(this);
        Email=(EditText)findViewById(R.id.email_login);
        Password=(EditText)findViewById(R.id.pass_login);
        Login=(Button)findViewById(R.id.btn_login);
        Register=(TextView) findViewById(R.id.register_login);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //Utilities.getUserBidang(getApplicationContext())!=null
        if(Utilities.getUserBidang(getApplicationContext())!=0){
            //Utilities.getUserBidang(getApplicationContext())
            switch ((int)Utilities.getUserBidang(getApplicationContext())){
                case 201:{
                    progress.dismiss();
                    startActivity(new Intent(Login.this, MainActivityExprt.class));
                    break;
                }
                case 202:{
                    progress.dismiss();
                    startActivity(new Intent(Login.this, MainActivityExprt.class));
                    break;
                }
                case 200:{
                    progress.dismiss();
                    startActivity(new Intent(Login.this, MainActivityWkr.class));
                    break;
                }
            }
            finish();
        }

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }

        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Email.getText().toString().matches("") || Password.getText().toString().matches("")){
                    Toast.makeText(Login.this, "Mohon isi dengan benar!", Toast.LENGTH_LONG).show();
                }else {
                    final String Email_=Email.getText().toString();
                    final String Pass_=Password.getText().toString();
                    progress.setMessage("Logging in...");
                    progress.show();
                    ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
                    if (activeNetwork != null) { // connected to the internet
                        Utilities.getUserRef().addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child(Email_.replace(".",",")).exists()){
                                    Pengguna user = dataSnapshot.child(Email_.replace(".",",")).getValue(Pengguna.class);
                                    if(Pass_.replace(".",",").equals(user.getPassword())){
                                        // untuk menyimpan email dalam aplikasi
                                        SharedPreferences.Editor editor = getSharedPreferences(Konstanta.PENGGUNA_PREFS, MODE_PRIVATE).edit();
                                        editor.putString("user", user.getEmail());
                                        editor.putLong("bidang", user.getStatus());
                                        editor.apply();
                                        switch ((int)user.getStatus()){
                                            case 201:{
                                                progress.dismiss();
                                                Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(Login.this, MainActivityExprt.class));
                                                break;
                                            }
                                            case 200:{
                                                progress.dismiss();
                                                Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(Login.this, MainActivityWkr.class));
                                                break;
                                            }
                                        }
                                        finish();
                                    }
                                }else{
                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Email address doesn't exist!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "No internet connection found!", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });


    }

   // @Override
   // protected void onStart() {
      //  super.onStart();
  //      authUser.addAuthStateListener(authListener);
    //}
}
