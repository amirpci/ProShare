package sidev17.siits.proshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import sidev17.siits.proshare.Login_Register.Login;
import sidev17.siits.proshare.Modul.Expert.MainActivityExprt;
import sidev17.siits.proshare.Modul.Worker.MainActivityWkr;

public class MainActDarurat extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    void keExpert(View v){
        startActivity(new Intent(this, MainActivityExprt.class));
    }
    void keWorker(View v){
        startActivity(new Intent(this, MainActivityWkr.class));
    }
}
