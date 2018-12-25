package com.example.gg.ocontact;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    private boolean search_edit_text_on = false;
    private android.support.v7.widget.Toolbar toolbar;
    private EditText editText_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        editText_search = findViewById(R.id.search_edit_text_search);
        toolbar = findViewById(R.id.search_toolbar);

        setSupportActionBar(toolbar);

        // 搜索框 EditText
        editText_search.setFocusableInTouchMode(false);
        editText_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                editText_search.setFocusable(true);
                editText_search.setFocusableInTouchMode(true);
                editText_search.requestFocus();
                editText_search.requestFocusFromTouch();
                search_edit_text_on = true;

                // 显示输入法
                InputMethodManager imm = (InputMethodManager) editText_search.getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm != null) {
                    imm.showSoftInput(editText_search, 0);
                }
            }
        });


        // EditText 内容变化 查询数据库
        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            //finish();

            editText_search.setText("");
            editText_search.setFocusable(false);
            editText_search.setFocusableInTouchMode(false);
            search_edit_text_on = false;

            InputMethodManager imm = (InputMethodManager) editText_search.getContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null){
                imm.hideSoftInputFromWindow(editText_search.getApplicationWindowToken(), 0);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
