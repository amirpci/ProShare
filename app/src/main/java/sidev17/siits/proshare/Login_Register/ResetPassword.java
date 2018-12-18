package sidev17.siits.proshare.Login_Register;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import sidev17.siits.proshare.R;

public class ResetPassword extends AppCompatActivity {
    private TextView back;
    private EditText Email;
    private Button Reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        back = (TextView)findViewById(R.id.register_reset);
        Email = (EditText)findViewById(R.id.email_reset);
        Reset = (Button)findViewById(R.id.btn_reset);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(Email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast toast4 = Toast.makeText(ResetPassword.this, "Password reset request has been sent to "+Email.getText().toString(), Toast.LENGTH_SHORT);
                            TextView v4 = (TextView) toast4.getView().findViewById(android.R.id.message);
                            if (v4 != null) v4.setGravity(Gravity.CENTER);
                            toast4.show();
                        }else{
                            Toast.makeText(ResetPassword.this, "Invalid email address!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
