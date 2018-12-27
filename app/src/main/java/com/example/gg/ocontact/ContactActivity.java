package com.example.gg.ocontact;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactActivity extends AppCompatActivity {

    private List<Person> personList=new ArrayList<>();
    private PersonAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_menu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);

        // 创建数据库(如无)
        LitePal.getDatabase();

        // 联系人列表 RecyclerView

        initRecycleView();

        final RecyclerView recyclerView = findViewById(R.id.contact_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PersonAdapter(personList, 1);
        recyclerView.setAdapter(adapter);
        setHeaderView(recyclerView);
        setFooterView(recyclerView);

        // ToolBar 中的 Menu
        Toolbar toolbar = findViewById(R.id.contact_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.inflateMenu(R.menu.contact_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.contact_menu_contact_us:
                        showToast("Contact us clicked");
                        break;
                    case R.id.item_search:
                        Intent toSearchActivity = new Intent(ContactActivity.this, SearchActivity.class);
                        startActivity(toSearchActivity);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                }
                return false;
            }
        });


        // 联系人添加 FAB -- !!! 跳转 EditActivity !!!
        FloatingActionButton fab_add = findViewById(R.id.contact_fab_add_contact);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivity.this, EditActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                showToast("'fab_add' clicked");
            }
        });


        // 回到最上方 FAB
        FloatingActionButton fab_to_the_top = findViewById(R.id.contact_fab_to_the_top);
        fab_to_the_top.getBackground().setAlpha(180);
        fab_to_the_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.scrollToPosition(0);
                showToast("'fab_to_the_top' clicked");
            }
        });



        //RecyclerView recyclerView = findViewById(R.id.contact_recyclerView);


    }


    // 让系统返回键实现和搜索栏返回键一样的功能
    /*
    @Override
    public void onBackPressed() {
        if (search_edit_text_on){
            final EditText editText_search = findViewById(R.id.contact_edit_text_search);
            final Button btn_back = findViewById(R.id.contact_btn_back);

            editText_search.setText("");
            editText_search.setFocusable(false);
            editText_search.setFocusableInTouchMode(false);
            search_edit_text_on = false;

            // 隐藏输入法
            InputMethodManager imm = (InputMethodManager) editText_search.getContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                imm.hideSoftInputFromWindow(Objects.requireNonNull(ContactActivity.this.getCurrentFocus()).getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

            btn_back.setVisibility(View.GONE);
        }
        else{
            super.onBackPressed();
        }
    }
    */



    // 避免多次 add_contact_toast 多次重复提示 (辅助功能)
    private Toast toast;
    private void showToast(String msg){
        if (toast != null) {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(this,msg,Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private void initRecycleView() {
        personList.clear();

        List<ContactDatabase> allContacts = LitePal.order("name asc").find(ContactDatabase.class);

        for(ContactDatabase person0: allContacts) {
            // 图片未成功
            Person person = new Person(person0.getName(), R.mipmap.account);
            personList.add(person);
        }
        //这一步本应该从数据库中导入数据，然后登记到类上
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
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        Toast.makeText(this,"返回了主界面", Toast.LENGTH_SHORT).show();
       initRecycleView();
    }


    // 返回界面时，重新加载recycler view
    @Override
    protected void onStart() {
        super.onStart();
        initRecycleView();
        RecyclerView recyclerView = findViewById(R.id.contact_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PersonAdapter(personList, 1);
        recyclerView.setAdapter(adapter);
        setHeaderView(recyclerView);
        setFooterView(recyclerView);
    }
}
