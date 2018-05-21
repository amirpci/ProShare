package sidev17.siits.proshare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import sidev17.siits.proshare.R;

public class Test_For_Database extends AppCompatActivity {
    private EditText addData,searchData;
    private Button add, search;
    private TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_for_database);
        addData = (EditText)findViewById(R.id.add_database);
        searchData = (EditText)findViewById(R.id.search_database);
        result = (TextView) findViewById(R.id.found_database);
        add = (Button)findViewById(R.id.add_database_btn);
        search = (Button)findViewById(R.id.search_database_btn);
    }
}
