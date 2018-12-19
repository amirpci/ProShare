package sidev17.siits.proshare.Login_Register;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

import gr.net.maroulis.library.EasySplashScreen;
import sidev17.siits.proshare.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasySplashScreen config = new EasySplashScreen(this)
                .withTargetActivity(Login.class)
                .withSplashTimeOut(800);
        View easy = config.create();
        setContentView(easy);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
