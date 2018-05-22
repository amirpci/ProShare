package sidev17.siits.proshare.Login_Register;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import sidev17.siits.proshare.MainActivity;
import sidev17.siits.proshare.R;

public class Login extends AppCompatActivity {
    private EditText Email,Password;
    private TextView Register;
    private Button Login;
    private ProgressDialog progress;
    private FirebaseAuth authUser;
    private FirebaseAuth.AuthStateListener authListener;
    private String AccountType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progress = new ProgressDialog(this);
        authUser = FirebaseAuth.getInstance();
        Email=(EditText)findViewById(R.id.email_login);
        Password=(EditText)findViewById(R.id.pass_login);
        Login=(Button)findViewById(R.id.btn_login);
       // LoginExpert = (Button)findViewById(R.id.btn_login_expert);
        Register=(TextView) findViewById(R.id.register_login);
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    progress.dismiss();
                    startActivity(new Intent(Login.this, MainActivity.class));
                }
            }

        };

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }

        });

       /* LoginExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginExpert();
            }
        });
        */
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Email.getText().toString().matches("") || Password.getText().toString().matches("")){
                    Toast.makeText(Login.this, "Mohon isi dengan benar!", Toast.LENGTH_LONG).show();
                }else {
                    String Email_=Email.getText().toString();
                    String Pass_=Password.getText().toString();
                    progress.setMessage("Logging in...");
                    progress.show();
                    authUser.signInWithEmailAndPassword(Email_,Pass_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                progress.dismiss();
                                AlertDialog.Builder builder_ = new AlertDialog.Builder(getApplicationContext());
                                builder_.setMessage("Login failed, please try again!")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog alert = builder_.create();
                                alert.setTitle("We're sorry!");
                                alert.show();
                            }
                        }
                    });
                }
            }
        });


    }
/*
    public void LoginExpert(){
        if(Email.getText().toString().matches("") || Password.getText().toString().matches("")){
            Toast.makeText(Login.this, "Mohon isi dengan benar!", Toast.LENGTH_LONG).show();
        }else {
            String Email_=Email.getText().toString();
            String Pass_=Password.getText().toString();
            progress.setMessage("Sedang masuk...!");
            progress.show();
            authUser.signInWithEmailAndPassword(Email_,Pass_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        progress.dismiss();
                        AlertDialog.Builder builder_ = new AlertDialog.Builder(Login.this);
                        builder_.setMessage("Anda belum berhasil login.")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alert = builder_.create();
                        alert.setTitle("Mohon maaf !");
                        alert.show();
                    }else {
                        String id_user = authUser.getCurrentUser().getUid();
                        DatabaseReference refDatabase = FirebaseDatabase.getInstance().getReference();
                        refDatabase.child("User").child(id_user).child("Type").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                String type = (String) snapshot.getValue();
                                if (!type.equals("Expert")) {
                                    progress.dismiss();
                                    AlertDialog.Builder builder_ = new AlertDialog.Builder(Login.this);
                                    builder_.setMessage("You're not registered as an Expert")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                    FirebaseAuth.getInstance().signOut();
                                                }
                                            });
                                    AlertDialog alert = builder_.create();
                                    alert.setTitle("We're Sorry !");
                                    alert.show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Something wrong happens", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        }
    }
    */
    @Override
    protected void onStart() {
        super.onStart();
        authUser.addAuthStateListener(authListener);
    }
}
