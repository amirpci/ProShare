package sidev17.siits.procks.Modul.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sidev17.siits.procks.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LampiranPertanyaan extends Fragment {


    public LampiranPertanyaan() {
        // Required empty public constructor
    }

    public LampiranPertanyaan(String id_problem){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment\
        View v = inflater.inflate(R.layout.fragment_lampiran_pertanyaan, container, false);

        return v;
    }

}
