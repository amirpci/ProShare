package sidev17.siits.proshare.Login_Register;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import gr.net.maroulis.library.EasySplashScreen;
import sidev17.siits.proshare.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_depan);
/*
        EasySplashScreen config = new EasySplashScreen(this)
                .withTargetActivity(Login.class)
                .withSplashTimeOut(800);
        View easy = config.create();
*/

        Thread thread= new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                }
                catch(Exception e){
                    Toast.makeText(getBaseContext(), "Kesalahan ke Halaman Selanjutnya!", Toast.LENGTH_SHORT).show();
                }
                finally {
                    Intent i= new Intent(getBaseContext(), Login.class);
                    startActivity(i);
                }
                finish();
                super.run();
            }
        };
        thread.start();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
