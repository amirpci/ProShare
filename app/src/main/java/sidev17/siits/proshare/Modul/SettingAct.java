package sidev17.siits.proshare.Modul;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Login_Register.Login;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Utilities;

public class SettingAct extends AppCompatActivity implements View.OnClickListener{

    private View gantiPassword, logout, hapusAkun, gantiBahasa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        gantiPassword= findViewById(R.id.setting_isi_1);
        logout= findViewById(R.id.setting_isi_2);
        hapusAkun= findViewById(R.id.setting_isi_3);
        gantiBahasa= findViewById(R.id.setting_isi_4);
        gantiBahasa.setOnClickListener(this);
        logout.setOnClickListener(this);
        hapusAkun.setOnClickListener(this);
        gantiPassword.setOnClickListener(this);
    }

    void gantiPassword(){
        //lakukan sesuatu
    }
    void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(SettingAct.this, Login.class);
        intent.putExtra(Konstanta.LOGIN_INTENT, Konstanta.LOGIN_LOGOUT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //lakukan sesuatu
    }
    void hapusAkun(){
        //lakukan sesuatu
    }
    void gantiBahasa(){
        //lakukan sesuatu
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_isi_1 : {
                gantiPassword();
                break;
            }
            case R.id.setting_isi_2 : {
                logout();
                break;
            }
            case R.id.setting_isi_3 : {
                hapusAkun();
                break;
            }
            case R.id.setting_isi_4 : {
                gantiBahasa();
                break;
            }
        }
    }
}
