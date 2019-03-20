package com.example.historytips;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private AutoCompleteTextView input_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_address = findViewById(R.id.input_address);
        initAutoComplete(input_address);

        Button btn_search = findViewById(R.id.btn_search);
        Button btn_clear = findViewById(R.id.btn_clear);
        btn_search.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                saveHistory(input_address);
                break;
            case R.id.btn_clear:
                clearHistory(v);
                break;
        }
    }

    private void initAutoComplete(AutoCompleteTextView input_address) {
        SharedPreferences sp = getSharedPreferences("searchList", 0);
        String history = sp.getString("history", "github.com");
        String[] hisArray = history.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, hisArray);

        if (hisArray.length > 5) {
            String[] newArrays = new String[5];
            System.arraycopy(hisArray, 0, newArrays, 0, 5);
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, newArrays);
        }

        input_address.setAdapter(adapter);
        input_address.setDropDownHeight(350);
        input_address.setThreshold(1);

        input_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                    view.showDropDown();
                }
            }
        });
    }

    private void saveHistory(AutoCompleteTextView input_address) {
        String text = input_address.getText().toString();
        SharedPreferences sp = getSharedPreferences("searchList", 0);
        String history = sp.getString("history", "github.com");
        if (!history.contains(text + ",")) {
            StringBuilder sb = new StringBuilder(history);
            sb.insert(0, text + ",");
            sp.edit().putString("history", sb.toString()).apply();
        }
    }

    public void clearHistory(View v) {
        SharedPreferences sp = getSharedPreferences("searchList", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(this, "Clear Success!", Toast.LENGTH_SHORT).show();
    }
}
