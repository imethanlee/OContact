package com.example.gg.ocontact;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import org.litepal.LitePal;

import java.util.List;

/*
 * To-do:12.26
 * 1.跳转到EditActivity
 * 2.完成分享功能
 * 3.完成头像载入
 * 4.完善删除功能
 * */
public class DetailActivity extends AppCompatActivity {

    int personId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //setTitle("");
        Intent intent=getIntent();
        String name=intent.getStringExtra("person_name");
        Log.d("DetailActivity", name);

        initInfo(name);//初始化详细信息
        CollapsingToolbarLayout collapsingToolbar=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView photo=(ImageView) findViewById(R.id.detail_image_view);//联系人头像

        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // 返回主界面
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        //collapsingToolbar.setTitle("姓名");
        //Glide.with(this).load( ).into(photo); //头像载入工具

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this,"返回主界面", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.edit:
                Intent intent=new Intent(DetailActivity.this, EditActivity.class);
                intent.putExtra("person_id",personId);
                startActivityForResult(intent, 1);
                //Toast.makeText(this,"编辑", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("是否删除此联系人？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LitePal.delete(ContactDatabase.class,personId);
                            }
                        }).setNegativeButton("取消",null).create();
                dialog.show();
                //dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor();
                break;
            case R.id.share:
                Toast.makeText(this,"分享", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void initInfo(String name) {
        List<ContactDatabase> list;
        list = LitePal.where("name = ?", name).find(ContactDatabase.class);

        TextView person_name=(TextView) findViewById(R.id.detail_name);
        TextView temp;
        TextView temp_content;
        for (ContactDatabase one: list) {
            person_name.setText(one.getName());
            personId=one.getId();

            temp_content=(TextView) findViewById(R.id.detail_work_content);
            temp_content.setText(one.getJob());
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp=(TextView) findViewById(R.id.detail_work);
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }

            temp_content=(TextView) findViewById(R.id.detail_number_content);
            temp_content.setText(one.getPhoneNumber());
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp=(TextView) findViewById(R.id.detail_number);
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }

            temp_content=(TextView) findViewById(R.id.detail_mail_content);
            temp_content.setText(one.getEmail());
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp=(TextView) findViewById(R.id.detail_mail);
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }

            temp_content=(TextView) findViewById(R.id.detail_address_content);
            temp_content.setText(one.getAddress());
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp=(TextView) findViewById(R.id.detail_address);
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }

            temp_content=(TextView) findViewById(R.id.detail_remark_content);
            temp_content.setText(one.getComment());
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp=(TextView) findViewById(R.id.detail_remark);
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }

            temp_content=(TextView) findViewById(R.id.detail_birthday_content);
            temp_content.setText(one.getBirthday());
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp=(TextView) findViewById(R.id.detail_birthday);
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
