package com.example.gg.ocontact;

import android.content.Context;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // ToolBar 中的 Menu
        Toolbar toolbar = findViewById(R.id.contact_toolbar);
        toolbar.inflateMenu(R.menu.contact_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.contact_menu_delete:
                        showToast("Delete clicked");
                        break;
                    case R.id.contact_menu_share_contacts:
                        showToast("Share clicked");
                        break;
                    case R.id.contact_menu_contact_us:
                        showToast("Contact us clicked");
                        break;
                }
                return false;
            }
        });


        final Button btn_back = findViewById(R.id.contact_btn_back);
        final EditText editText_search = findViewById(R.id.contact_edit_text_search);

        // 搜索框返回 Button
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText_search.setFocusable(false);
                editText_search.setFocusableInTouchMode(false);

                // 隐藏输入法
                InputMethodManager imm = (InputMethodManager) editText_search.getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm != null) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }

                btn_back.setVisibility(View.GONE);
            }
        });


        // 搜索框 EditText
        editText_search.setFocusableInTouchMode(false);

        editText_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_back.setVisibility(View.VISIBLE);
                editText_search.setFocusable(true);
                editText_search.setFocusableInTouchMode(true);
                editText_search.requestFocus();
                editText_search.requestFocusFromTouch();

                // 显示输入法
                InputMethodManager imm = (InputMethodManager) editText_search.getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm != null) {
                    imm.showSoftInput(editText_search, 0);
                }
            }
        });



        // 联系人添加 FAB -- !!! 跳转 EditActivity !!!
        FloatingActionButton fab_add = findViewById(R.id.contact_fab_add_contact);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("'fab_add' clicked");
            }
        });


        // 回到最上方 FAB
        FloatingActionButton fab_to_the_top = findViewById(R.id.contact_fab_to_the_top);
        fab_to_the_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("'fab_to_the_top' clicked");
            }
        });


        // 联系人列表 RecyclerView
        RecyclerView recyclerView = findViewById(R.id.contact_recyclerView);


    }





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
}
