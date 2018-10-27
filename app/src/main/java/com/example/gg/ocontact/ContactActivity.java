package com.example.gg.ocontact;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

        // 联系人添加按钮
        FloatingActionButton fab = findViewById(R.id.contact_add_contact_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Add Contact clicked");
            }
        });

    }





    // 避免多次 add_contact_toast 多次重复提示
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
