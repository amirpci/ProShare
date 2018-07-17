package sidev17.siits.proshare.Modul.Expert.Tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import sidev17.siits.proshare.R;

public class FeedbackActExprt extends Fragment {
    public final int PENGGUNA_EXPERT_TERVERIFIKASI= 2;
    public final int PENGGUNA_EXPERT= 1;
    public final int PENGGUNA_BIASA= 0;

    private String orang[]= {"Mr. A", "Mr. B", "Mrs. C", "Will Smith"};
//    private String chat[]= {"Psychiatrist", "Motivator", "Engineer", "Actor"};
    private int kategoriExpert[]= {2, 1, 0, 2};

//    private String judulSolusi[]= {"What should I do whe this happen?", "How to gain inspiration?", "How to else?", "Stay cool"};
    private String feedback[][]= {{"I do this everyday, but somehow..."}, {"When it happens, I don't know what to do. I need inspiration."}, {"bla bla bla..."}, {"Just bel cool bro!"}};

    private ListView wadahFeedback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_feedback_exprt, container, false);
        wadahFeedback= view.findViewById(R.id.feedback_wadah);
        AdapterFeedback adpFeedback= new AdapterFeedback();
        wadahFeedback.setAdapter(adpFeedback);

        return view;
    }

    class AdapterFeedback extends BaseAdapter {
        /*
                String nama[];
                String job[];
                String pp[];

                String solusi[];
                String tgl[];
        */
        AdapterFeedback (){
        }

        @Override
        public int getCount() {
            return orang.length;
        }

        @Override
        public Object getItem(int position) {
            return orang[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
                view= getLayoutInflater().inflate(R.layout.model_kolom_feedback, parent, false);

                TextView teksOrang= view.findViewById(R.id.feedback_orang_nama);
                TextView teksChat= view.findViewById(R.id.feedback_orang_chat);
                TextView teksWaktu= view.findViewById(R.id.feedback_waktu);
                ImageView centang= view.findViewById(R.id.feedback_orang_centang);

                teksOrang.setText(orang[position]);
                teksChat.setText(feedback[position][0]);
//                teksWaktu.setText(descSolusi[position-1]);

                if(kategoriExpert[position]== PENGGUNA_EXPERT)
                    centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full_polos);
                else if(kategoriExpert[position]== PENGGUNA_EXPERT_TERVERIFIKASI)
                    centang.setBackgroundResource(R.drawable.obj_centang_lingkaran_full);
            return view;
        }
    }
}
