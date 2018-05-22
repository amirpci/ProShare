package sidev17.siits.proshare.Login_Register.RegisterPage;

import android.content.Context;
import android.net.Uri;
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
import android.widget.Toast;

import java.util.regex.Pattern;

import sidev17.siits.proshare.Login_Register.Register;
import sidev17.siits.proshare.R;


public class Register2 extends Fragment {
    private Register registerRef2;
    private EditText Password, RetypePassword;
    private Button Previous, Register;
    private ImageView PasswordStrong;
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
                    PasswordStrong.setColorFilter(ContextCompat.getColor(getActivity(), R.color.green_light));
                }else{
                    passStrong = false;
                    PasswordStrong.setColorFilter(ContextCompat.getColor(getActivity(), R.color.abu));
                }
            }
        });

        return v;
    }

    boolean passwordKuat(String pass){
        if(pass.length()>5 && Pattern.compile( "[0-9]" ).matcher(pass).find()){
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
            Toast.makeText(getActivity(), "Password should contain at least one number!", Toast.LENGTH_LONG).show();
        }else{
            registerRef2.Register(registerRef2.namaReg,registerRef2.emailReg, registerRef2.negaraReg, registerRef2.spesialisasiReg, Password.getText().toString() );
        }
    }
}
