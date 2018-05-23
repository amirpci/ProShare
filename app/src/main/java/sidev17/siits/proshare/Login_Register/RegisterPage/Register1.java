package sidev17.siits.proshare.Login_Register.RegisterPage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import sidev17.siits.proshare.Login_Register.Register;
import sidev17.siits.proshare.R;

public class Register1 extends Fragment {
    private Register registerRef1;
    private Spinner PilihanBidang, PilihanNegara;
    private EditText Nama, Email;
    private Button Next;
   private LinearLayout pDataContainer;
    private String Negara="", Bidang="";
    //savedStateData
    private String nama_="",email_="";
    private int bidang_=0,negara_=0;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("NAMA", Nama.getText().toString());
        outState.putString("EMAIL", Email.getText().toString());
        outState.putInt("BIDANG", bidang_);
        outState.putInt("NEGARA", negara_);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            nama_ = savedInstanceState.getString("NAMA");
            email_ = savedInstanceState.getString("EMAIL");
            bidang_ = savedInstanceState.getInt("BIDANG");
            negara_ = savedInstanceState.getInt("NEGARA");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register1, container, false);
        registerRef1 = (Register)getActivity();
        pDataContainer = (LinearLayout)v.findViewById(R.id.PersonalDataContainer);
        //pDataContainer = new LinearLayout(getActivity());
        Next = (Button)v.findViewById(R.id.next_reg);
        Nama = (EditText)v.findViewById(R.id.nama_reg);
        Email = (EditText)v.findViewById(R.id.email_reg);
        PilihanBidang = (Spinner)v.findViewById(R.id.specialization_option);
        PilihanNegara = (Spinner)v.findViewById(R.id.pil_negara);
        ArrayAdapter<String> spPilihanBidang = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item,registerRef1.specialization);
        spPilihanBidang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PilihanBidang.setAdapter(spPilihanBidang);
        ArrayAdapter<String> spPilihanNegara = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item,registerRef1.negara);
        spPilihanNegara.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PilihanNegara.setAdapter(spPilihanNegara);

        //set untuk inisiasi savedState
        Nama.setText(registerRef1.namaReg);
        Email.setText(registerRef1.emailReg);
        PilihanBidang.setSelection(registerRef1.positionBidang);
        PilihanNegara.setSelection(registerRef1.positionNegara);

        PilihanNegara.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 if(position!=0) {
                     Negara = registerRef1.negara[position];
                     negara_ = position;
                 }
                 registerRef1.contHeight = pDataContainer.getHeight();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        PilihanBidang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    Bidang = registerRef1.specialization[position];
                    bidang_ = position;
                }
                registerRef1.contHeight = pDataContainer.getHeight();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }
/*
    void setTinggi(int a){
        switch (a){
            case 0: ViewGroup.LayoutParams params = pDataContainer.getLayoutParams();
                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                pDataContainer.requestLayout();
                 break;
            case 1: ViewGroup.LayoutParams params1 = pDataContainer.getLayoutParams();
                params1.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params1.height = 180;
                pDataContainer.requestLayout();
                 break;
        }
    }
    */

    public boolean next(){
        boolean returnVal=false;
        if(cekKosong()){
            Toast.makeText(getActivity(), "Please fill the blank!", Toast.LENGTH_LONG).show();
        }else if(!cekEmail(Email.getText().toString().toCharArray())){
            Toast.makeText(getActivity(), "Wrong email format!", Toast.LENGTH_LONG).show();
        }else{
            registerRef1.namaReg=Nama.getText().toString();
            registerRef1.emailReg=Email.getText().toString();
            registerRef1.negaraReg=Negara;
            registerRef1.spesialisasiReg=Bidang;
            registerRef1.positionBidang=bidang_;
            registerRef1.positionNegara=negara_;
            registerRef1.clickCounter++;
            registerRef1.tampilkan(new Register2());
            returnVal=true;
        }
        return returnVal;
    }

    void tampilkan(Fragment frag) {
        /*if(registerRef1.clickCounter==1){
            registerRef1.registerLayout.removeAllViewsInLayout();
        }*/
        if(getFragmentManager().findFragmentById(R.id.fragment_register) != null) {
            getFragmentManager()
                    .beginTransaction().
                    remove(getFragmentManager().findFragmentById(R.id.fragment_register)).commit();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_register, frag);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void clearStackFragment() {
        //Here we are clearing back stack fragment entries
        int backStackEntry = getFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getFragmentManager().popBackStackImmediate();
            }
        }

        //Here we are removing all the fragment that are shown here
        if (getFragmentManager().getFragments() != null && getFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }

    boolean cekKosong(){
        String nama = Nama.getText().toString().trim();
        String email = Email.getText().toString().trim();
        if(nama.matches("") || email.matches("") || bidang_==0 || negara_==0){
            return true;
        }
        return false;
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
