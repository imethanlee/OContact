package com.example.gg.ocontact;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
/*
 * To-do:
 * 1.完善HomeAsUp按钮返回主界面的跳转功能
 * 2.在detail_info.xml中创建“详细信息”栏中的模板信息
 * 3.Java完成数据读取和展示
 * */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("");
        Intent intent=getIntent();
        //String name=intent.getStringExtra()
        CollapsingToolbarLayout collapsingToolbar=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView photo=(ImageView) findViewById(R.id.detail_image_view);//联系人头像
        //TextView
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            case R.id.home:
                Toast.makeText(this,"返回主界面", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.edit:
                Toast.makeText(this,"编辑", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this,"删除", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Toast.makeText(this,"分享", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
