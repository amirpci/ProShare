package sidev17.siits.procks.Login_Register.RegisterPage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.regex.Pattern;

import sidev17.siits.procks.Login_Register.Register;
import sidev17.siits.procks.R;


public class Register2 extends Fragment {
    private Register registerRef2;
    private EditText Password, RetypePassword;
    private Button Previous, Register;
    private ImageView PasswordStrong;
    private LinearLayout passwordContainer;
    private Switch statusSwitch;
    private boolean passStrong = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register2, container, false);
        registerRef2 = (Register)getActivity();
        Password = (EditText)v.findViewById(R.id.pass_reg);
        RetypePassword = (EditText)v.findViewById(R.id.retypepass_reg);
        Previous = (Button)v.findViewById(R.id.prev_reg);
        Register = (Button)v.findViewById(R.id.finish_reg);
        PasswordStrong = (ImageView)v.findViewById(R.id.passStrong);
        passwordContainer = (LinearLayout)v.findViewById(R.id.passContainer);
        statusSwitch = (Switch)v.findViewById(R.id.register_expert_switch);
        // set tinggi default sama dengan sebelumnya
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                registerRef2.contHeight);
        passwordContainer.setLayoutParams(lp);
        registerRef2.halamanPertama=false;
        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(passwordKuat(s.toString())){
                    passStrong = true;
                    PasswordStrong.setColorFilter(ContextCompat.getColor(getActivity(), R.color.ijo));
                }else{
                    passStrong = false;
                    PasswordStrong.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.white));
                }
            }
        });

        return v;
    }

    boolean passwordKuat(String pass){
        if(pass.length()>5 && Pattern.compile( "[0-9]" ).matcher(pass).find()
                && Pattern.compile("[a-zA-Z]+").matcher(pass).find()){
            return true;
        }else {
            return false;
        }
    }
    void tampilkan(Fragment frag) {
        if(getFragmentManager().findFragmentById(R.id.fragment_register) != null) {
            getFragmentManager()
                    .beginTransaction().
                    remove(getFragmentManager().findFragmentById(R.id.fragment_register)).commit();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_register, frag);
        ft.commit();
    }
    public void SignUp(){
        if(Password.getText().toString().trim().matches("") || RetypePassword.getText().toString().trim().matches("")){
            Toast.makeText(getActivity(), "Please fill the blank!", Toast.LENGTH_LONG).show();
        }else if(!Password.getText().toString().equals(RetypePassword.getText().toString())){
            Toast.makeText(getActivity(), "Password doesn't match!", Toast.LENGTH_LONG).show();
        }else if(Password.getText().toString().length()<6){
            Toast.makeText(getActivity(), "Password should have at least 6 character length!", Toast.LENGTH_LONG).show();
        }else if(!passStrong) {
            Toast.makeText(getActivity(), "Password should contain letter and number!", Toast.LENGTH_LONG).show();
        }else{
            registerRef2.Register(registerRef2.namaReg,registerRef2.emailReg, registerRef2.negaraReg, registerRef2.spesialisasiReg, Password.getText().toString(), statusSwitch.isChecked());
        }
    }
}
