package sidev17.siits.proshare.Login_Register;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sidev17.siits.proshare.Login_Register.RegisterPage.Register1;
import sidev17.siits.proshare.Login_Register.RegisterPage.Register2;
import sidev17.siits.proshare.R;

public class Register extends AppCompatActivity {
    private EditText Nama, Email, Country, Password, Re_password, specialized;
    private Button Register, Previous;
    public Button Next;
    private TextView Sp_Q;
    private Switch expert;
    private FirebaseAuth authUser;
    private DatabaseReference db;
    private ProgressDialog progress;
    public LinearLayout Btn2;
    public FrameLayout registerLayout;
    public String namaReg="", emailReg="", negaraReg="", spesialisasiReg="", passwordReg="";
    public int positionNegara=0,positionBidang=0;
    public int clickCounter=0;
    public int contHeight = 0;
    public static final String[] negara = {"Select...","Indonesia", "United States", "United Kingdom", "Japan"};
    public static final String[] specialization = {"Select...","Safety Engineering", "Civil Engineering", "Constructional", "Enviromental Engineering","Materials Science","More Options"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progress = new ProgressDialog(this);
        db = FirebaseDatabase.getInstance().getReference().child("User");
        authUser = FirebaseAuth.getInstance();
        Btn2 = (LinearLayout)findViewById(R.id.reg2_btn);
        Nama = (EditText)findViewById(R.id.nama_reg);
        Email = (EditText)findViewById(R.id.email_reg);
        //Country = (EditText)findViewById(R.id.country_reg);
        specialized = (EditText)findViewById(R.id.specialized);
        Password = (EditText)findViewById(R.id.pass_reg);
        Re_password = (EditText)findViewById(R.id.retypepass_reg);
        expert = (Switch)findViewById(R.id.expert_switch);
        Previous = (Button)findViewById(R.id.prev_reg);
        Register = (Button)findViewById(R.id.finish_reg);
        Next = (Button)findViewById(R.id.next_reg);

        tampilkan(new Register1());

        Btn2.setVisibility(View.GONE);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register1 fragment = (Register1) getSupportFragmentManager().findFragmentById(R.id.fragment_register);
                if(fragment.next()){
                    Next.setVisibility(View.GONE);
                    Btn2.setVisibility(View.VISIBLE);
                    tampilkan(new Register2());
                }
            }
        });
        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btn2.setVisibility(View.GONE);
                Next.setVisibility(View.VISIBLE);
                tampilkan(new Register1());
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register2 fragment = (Register2) getSupportFragmentManager().findFragmentById(R.id.fragment_register);
                fragment.SignUp();
            }
        });

    }
    public void tampilkan(Fragment frag) {
        if(getFragmentManager().findFragmentById(R.id.fragment_register) != null) {
            getFragmentManager()
                    .beginTransaction().
                    remove(getFragmentManager().findFragmentById(R.id.fragment_register)).commit();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_register, frag);
        ft.commit();
    }

    public void Register(final String Nama_,final String Email_,final String Negara_,final String Bidang_,final String Pass_){
        progress.setMessage("Signing Up...");
        progress.show();
        authUser.createUserWithEmailAndPassword(Email_, Pass_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    AlertDialog.Builder builder_ = new AlertDialog.Builder(Register.this);
                    String id_user = authUser.getCurrentUser().getUid();
                    DatabaseReference idRef =  db.child(id_user);
                    idRef.child("Nama").setValue(Nama_);
                    idRef.child("Email").setValue(Email_);
                    idRef.child("Negara").setValue(Negara_);
                    idRef.child("Specialization").setValue(Bidang_);
                    idRef.child("Type").setValue("Worker");
                    idRef.child("Score").child("Answered").setValue(0);
                    idRef.child("Score").child("Rater").setValue(0);
                    idRef.child("Score").child("Rating").setValue(0);
                    idRef.child("Photo").setValue("-");
                    progress.dismiss();
                }else {
                    progress.dismiss();
                    Toast.makeText(Register.this, "Register failed!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public Boolean cekEmail(char[] mail_char){
        int isEmail=0;
        Boolean isEmailBisa=false;
        int position=0;
        for(int i=0;i<mail_char.length;i++){
            if(mail_char[i]=='@'){
                isEmail++;
                position=i;
            }
            if(mail_char[i]=='!' || mail_char[i]=='#' || mail_char[i]=='$' || mail_char[i]=='%' || mail_char[i]=='^' ||
                    mail_char[i]=='&' || mail_char[i]=='*' || mail_char[i]=='(' || mail_char[i]==')' || mail_char[i]=='-' || mail_char[i]=='=' ||
                    mail_char[i]=='+' || mail_char[i]=='[' || mail_char[i]==']' || mail_char[i]=='{' || mail_char[i]=='}' || mail_char[i]==':' || mail_char[i]==';' || mail_char[i]=='/' ||
                    mail_char[i]=='?' || mail_char[i]=='>' || mail_char[i]=='<' || mail_char[i]==',' || mail_char[i]=='`' || mail_char[i]=='~'){
                isEmail=0;
                break;
            }
        }

        if(isEmail==1 && (mail_char.length-position-1)>4 && mail_char[position+1]!='.'){
            int pointChar=0;
            int[] titikPosisi = new int[mail_char.length-position-1];
            for(int i=position+1; i<mail_char.length;i++){
                if(mail_char[i]=='.'){
                    titikPosisi[pointChar]=i;
                    pointChar++;
                }
            }
            if(pointChar>0 && pointChar<3 && (titikPosisi[1]-titikPosisi[0]!=1) && titikPosisi[1]!=(mail_char.length-1)){
                isEmailBisa=true;
            }
        }
        return isEmailBisa;
    }
}
