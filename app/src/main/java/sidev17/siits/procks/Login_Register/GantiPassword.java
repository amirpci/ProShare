package sidev17.siits.procks.Login_Register;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import sidev17.siits.procks.R;
import sidev17.siits.procks.Utils.PackBahasa;
import sidev17.siits.procks.Utils.Terjemahan;
import sidev17.siits.procks.Utils.Utilities;

public class GantiPassword extends AppCompatActivity implements View.OnClickListener {
    private TextView passLama, passBaru, passBaruKonfirm;
    private ImageView centangPass;
    private Button ganti;

    private boolean passKuat = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);

        passLama = findViewById(R.id.pass_lama_isi);
        passBaru = findViewById(R.id.pass_baru_isi);
        passBaruKonfirm = findViewById(R.id.pass_konfirm_isi);
        centangPass = findViewById(R.id.passStrong);
        ganti = findViewById(R.id.ganti);

        passBaru.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(passwordKuat(s.toString())){
                    passKuat = true;
                    centangPass.setColorFilter(ContextCompat.getColor(GantiPassword.this, R.color.ijo));
                }else{
                    passKuat = false;
                    centangPass.setColorFilter(ContextCompat.getColor(GantiPassword.this, android.R.color.white));
                }
            }
        });

        ganti.setOnClickListener(this);
    }

    boolean passwordKuat(String pass){
        if(pass.length()>5 && Pattern.compile( "[0-9]" ).matcher(pass).find()
                && Pattern.compile("[a-zA-Z]+").matcher(pass).find()){
            return true;
        }else {
            return false;
        }
    }

    public void gantiPassword(){
        if(passLama.getText().toString().trim().matches("") || passBaru.getText().toString().trim().matches("") || passBaruKonfirm.getText().toString().trim().matches("")){
            Toast.makeText(this, PackBahasa.gantiPassword[Terjemahan.indexBahasa(this)][0], Toast.LENGTH_LONG).show();
        }else if(!passBaru.getText().toString().equals(passBaruKonfirm.getText().toString())){
            Toast.makeText(this, PackBahasa.gantiPassword[Terjemahan.indexBahasa(this)][1], Toast.LENGTH_LONG).show();
        }else if(passBaru.getText().toString().length()<6){
            Toast.makeText(this, PackBahasa.gantiPassword[Terjemahan.indexBahasa(this)][2], Toast.LENGTH_LONG).show();
        }else if(!passKuat) {
            Toast.makeText(this, PackBahasa.gantiPassword[Terjemahan.indexBahasa(this)][3], Toast.LENGTH_LONG).show();
        }else{
           //password diganti disini
            gantiDiFirebase(passLama.getText().toString(), passBaru.getText().toString());
        }
    }

    public void gantiDiFirebase(String passLama, final String passBaru){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(Utilities.getUserID(this), passLama);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(passBaru).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(GantiPassword.this, PackBahasa.gantiPassword[Terjemahan.indexBahasa(GantiPassword.this)][4], Toast.LENGTH_LONG).show();
                                        GantiPassword.this.finish();
                                    } else {
                                        Toast.makeText(GantiPassword.this, PackBahasa.gantiPassword[Terjemahan.indexBahasa(GantiPassword.this)][5], Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(GantiPassword.this, PackBahasa.gantiPassword[Terjemahan.indexBahasa(GantiPassword.this)][5], Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v == ganti)
            gantiPassword();
    }
}
