package sidev17.siits.proshare.Login_Register.RegisterPage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sidev17.siits.proshare.Konstanta;
import sidev17.siits.proshare.Login_Register.Register;
import sidev17.siits.proshare.Model.Bidang;
import sidev17.siits.proshare.Model.Country;
import sidev17.siits.proshare.R;
import sidev17.siits.proshare.Utils.Utilities;

public class Register1 extends Fragment {
    private Register registerRef1;
    private Spinner PilihanBidang, PilihanNegara;
    private EditText Nama, Email;
    private Button Next;
    private ImageView PasswordStrong;
    private LinearLayout pDataContainer;
    private String Negara="", Bidang="";
    private boolean emailAda;
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
      //  PilihanBidang = (Spinner)v.findViewById(R.id.specialization_option);
       // PilihanNegara = (Spinner)v.findViewById(R.id.pil_negara);
        PasswordStrong = (ImageView)v.findViewById(R.id.passStrong);
      //  ArrayAdapter<String> spPilihanBidang = new ArrayAdapter<String>(getActivity(),
       //         R.layout.spinner_item,registerRef1.specialization);
       // spPilihanBidang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      //  PilihanBidang.setAdapter(spPilihanBidang);

        ArrayList<Country> lsCtry = new ArrayList<>();
        initPilihanNegara(lsCtry, v);
        loadPilihanNegaraServer(v);

        ArrayList<Bidang> lsBdg = new ArrayList<>();
        initPilihanMajority(lsBdg, v);
        loadPilihanMajorityServer(v);

        registerRef1.halamanPertama=true;
        //set untuk inisiasi savedState
        Nama.setText(registerRef1.namaReg);
        Email.setText(registerRef1.emailReg);
        PilihanBidang.setSelection(registerRef1.positionBidang);
        PilihanNegara.setSelection(registerRef1.positionNegara);
        initEmail();
        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String str = s.toString();
                Utilities.getUserRef().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(registerRef1.halamanPertama){
                            if(!dataSnapshot.child(str.replace(".",",")).exists() && cekEmail(str.toCharArray())){
                                PasswordStrong.setColorFilter(ContextCompat.getColor(getActivity(), R.color.ijo));
                            }else{
                                PasswordStrong.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.white));
                            }
                            if(dataSnapshot.child(str.replace(".",",")).exists()){
                                emailAda=true;
                            }else{
                                emailAda=false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        PilihanNegara.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 if(position!=0) {
                     negara_ = position;
                     Negara = String.valueOf(position+1);
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
                    bidang_ = position;
                    Bidang = String.valueOf(position+1);
                }
                registerRef1.contHeight = pDataContainer.getHeight();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    private void initEmail(){
        if(registerRef1.emailBenar){
            PasswordStrong.setColorFilter(ContextCompat.getColor(getActivity(), R.color.ijo));
        }else{
            PasswordStrong.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.white));
        }
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

    void initPilihanMajority(ArrayList<Bidang> majority, View v){
        PilihanBidang = (Spinner)v.findViewById(R.id.specialization_option);
        ArrayAdapter<String> spPilihanBidang = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, Utilities.listBidangkeArray(majority));
        spPilihanBidang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PilihanBidang.setAdapter(spPilihanBidang);
    }

    void loadPilihanMajorityServer(final View v){
        final ArrayList<sidev17.siits.proshare.Model.Bidang> bdg = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Konstanta.DAFTAR_BIDANG, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                bdg.clear();
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Bidang bidang = new Bidang();
                        bidang.setId(obj.getString("id"));
                        bidang.setBidang(obj.getString("bidang"));
                        bdg.add(bidang);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                initPilihanMajority(bdg, v);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getActivity()).add(request);
    }

    void initPilihanNegara(ArrayList<Country> country, View v){
        PilihanNegara = (Spinner)v.findViewById(R.id.pil_negara);
        ArrayAdapter<String> spPilihanNegara = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, Utilities.listNegarakeArray(country));
        spPilihanNegara.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PilihanNegara.setAdapter(spPilihanNegara);
    }

    void loadPilihanNegaraServer(final View v){
        final ArrayList<Country> ctry = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Konstanta.DAFTAR_NEGARA, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ctry.clear();
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Country country = new Country();
                        country.setId(obj.getString("id"));
                        country.setNegara(obj.getString("negara"));
                        ctry.add(country);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                initPilihanNegara(ctry, v);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getActivity()).add(request);
    }

    public boolean next(){
        boolean returnVal=false;
        if(cekKosong()){
            Toast.makeText(getActivity(), "Please fill the blank!", Toast.LENGTH_SHORT).show();
        }else if(!cekEmail(Email.getText().toString().toCharArray())){
            Toast.makeText(getActivity(), "Wrong email address format!", Toast.LENGTH_SHORT).show();
        }else if(emailAda) {
            Toast.makeText(getActivity(), "Email address is already exist!", Toast.LENGTH_SHORT).show();
        }else {
            registerRef1.emailBenar=true;
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

        if(isEmail==1 && (mail_char.length-position-1)>4 && mail_char[position+1]!='.' && mail_char[mail_char.length-1]!='.'){
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
