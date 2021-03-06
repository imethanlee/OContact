package com.example.gg.ocontact;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public class SearchActivity extends SwipeBackActivity {
    private SwipeBackLayout mSwipeBackLayout;

    private boolean search_edit_text_on = false;
    private android.support.v7.widget.Toolbar toolbar;
    private EditText editText_search;
    private List<ContactDatabase> result_list;

    private RecyclerView recyclerView;
    private PersonAdapter adapter;
    private List<Person> personList = new ArrayList<>();
    private LinearLayoutManager layoutManager;

    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyThemeUtils.initTheme(SearchActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 可以调用该方法，设置是否允许滑动退出
        setSwipeBackEnable(true);
        mSwipeBackLayout = getSwipeBackLayout();
        // 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        // 滑动退出的效果只能从边界滑动才有效果，如果要扩大touch的范围，可以调用这个方法
        mSwipeBackLayout.setEdgeSize(100);

        editText_search = (EditText) findViewById(R.id.search_edit_text_search);
        toolbar = (Toolbar) findViewById(R.id.search_toolbar);

        // 滑动布局
        recyclerView = (RecyclerView) findViewById(R.id.search_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // 返回按钮
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();


        // 搜索框 EditText
        editText_search.setFocusableInTouchMode(false);
        editText_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionBar.setDisplayHomeAsUpEnabled(true);
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
                // 重置
                personList.clear();

                String keyword = editText_search.getText().toString();

                // 模糊搜索 数据库
                if (!editText_search.getText().toString().equals("")){
                    result_list = LitePal.where("name like ? or phoneNumber like ?"
                            , "%" + keyword + "%", "%" + keyword + "%").
                            find(ContactDatabase.class);
                }
                else {
                    result_list.clear();
                }


                for(ContactDatabase one: result_list) {
                    Person person= new Person(one.getId(), one.getName(),
                            one.getPhotoPath(), one.getPhoneNumber());
                    personList.add(person);
                }
                adapter = new PersonAdapter(personList, 2);

                recyclerView.setAdapter(adapter);
                setFooterView(recyclerView);
                setHeaderView(recyclerView);
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
            if (!editText_search.getText().toString().equals("")){
                editText_search.setText("");
                editText_search.setFocusable(false);
                editText_search.setFocusableInTouchMode(false);
            }
            else {
                editText_search.setFocusable(false);
            }

            search_edit_text_on = false;

            InputMethodManager imm = (InputMethodManager) editText_search.getContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null){
                imm.hideSoftInputFromWindow(editText_search.getApplicationWindowToken(), 0);
            }
            actionBar.setDisplayHomeAsUpEnabled(false);

            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setHeaderView(RecyclerView view){
        View header = LayoutInflater.from(this).inflate(R.layout.header, view, false);
        adapter.setHeaderView(header);
    }

    private void setFooterView(RecyclerView view){
        View footer = LayoutInflater.from(this).inflate(R.layout.footer, view, false);
        adapter.setFooterView(footer);
    }

    @Override
    public void onBackPressed() {
        if (search_edit_text_on){
            if (!editText_search.getText().toString().equals("")){
                editText_search.setText("");
                editText_search.setFocusable(false);
                editText_search.setFocusableInTouchMode(false);
            }
            else {
                editText_search.setFocusable(false);
            }
            search_edit_text_on = false;
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

