package com.example.gg.ocontact;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import static android.view.View.INVISIBLE;

/*
 * To-do:12.27
 * Done
 * */
public class DetailActivity extends SwipeBackActivity {
    private SwipeBackLayout mSwipeBackLayout;

    int personId;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyThemeUtils.initTheme(DetailActivity.this);
        setContentView(R.layout.activity_detail);

        // 可以调用该方法，设置是否允许滑动退出
        setSwipeBackEnable(true);
        mSwipeBackLayout = getSwipeBackLayout();
        // 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        // 滑动退出的效果只能从边界滑动才有效果，如果要扩大touch的范围，可以调用这个方法
        mSwipeBackLayout.setEdgeSize(100);

        Intent intent=getIntent();
        name=intent.getStringExtra("id");
        Log.d("DetailActivity", name);

        initInfo(name);//初始化详细信息

        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

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
                onBackPressed();
                return true;
            case R.id.edit:
                Intent intent=new Intent(DetailActivity.this, EditActivity.class);
                intent.putExtra("id",String.valueOf(personId));
                Log.d("DetailActivity_id", String.valueOf(personId));
                startActivityForResult(intent, 1);
                break;
            case R.id.delete:
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Are you sure to delete?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LitePal.delete(ContactDatabase.class,personId);
                                Toast.makeText(DetailActivity.this,"Deleted", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        }).setNegativeButton("Cancel",null).create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                break;
            case R.id.share:
                List<ContactDatabase> list;
                list = LitePal.where("id = ?", String.valueOf(personId)).find(ContactDatabase.class);
                TextView person_name=(TextView) findViewById(R.id.detail_name);
                String share="Name:"+person_name.getText();
                TextView temp_content;
                for (ContactDatabase one: list) {
                    temp_content=(TextView) findViewById(R.id.detail_work_content);
                    if (!TextUtils.isEmpty(temp_content.getText())) {
                        share+="\nWork:"+temp_content.getText();
                    }

                    temp_content=(TextView) findViewById(R.id.detail_number_content);
                    if (!TextUtils.isEmpty(temp_content.getText())) {
                        share+="\nPhone number:"+temp_content.getText();
                    }

                    temp_content=(TextView) findViewById(R.id.detail_mail_content);
                    if (!TextUtils.isEmpty(temp_content.getText())) {
                        share+="\nEmail:"+temp_content.getText();
                    }

                    temp_content=(TextView) findViewById(R.id.detail_remark_content);
                    if (!TextUtils.isEmpty(temp_content.getText())) {
                        share+="\nComments:"+temp_content.getText();
                    }

                    temp_content=(TextView) findViewById(R.id.detail_address_content);
                    if (!TextUtils.isEmpty(temp_content.getText())) {
                        share+="\nAddress:"+temp_content.getText();
                    }

                    temp_content=(TextView) findViewById(R.id.detail_birthday_content);
                    if (!TextUtils.isEmpty(temp_content.getText())) {
                        share+="\nBirthday:"+temp_content.getText();
                    }
                }
                Intent sendIntent = new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, share).setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"Share to ..."));
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void initInfo(String name) {
        CollapsingToolbarLayout collapsingToolbar=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView photo=(ImageView) findViewById(R.id.detail_image_view);//联系人头像
        //collapsingToolbar.setTitle("姓名");
        //
        String photoPath=null;

        List<ContactDatabase> list;
        list = LitePal.where("id = ?", name).find(ContactDatabase.class);

        TextView person_name=(TextView) findViewById(R.id.detail_name);
        TextView temp;
        TextView temp_content;
        for (ContactDatabase one: list) {
            person_name.setText(one.getName());
            personId=one.getId();
            photoPath=one.getPhotoPath();

            temp_content=(TextView) findViewById(R.id.detail_work_content);

            temp_content.setText(one.getJob());
            temp=(TextView) findViewById(R.id.detail_work);
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }
            else {
                temp.setVisibility(View.VISIBLE);
                temp_content.setVisibility(View.VISIBLE);
            }

            temp_content=(TextView) findViewById(R.id.detail_number_content);
            temp_content.setText(one.getPhoneNumber());
            temp=(TextView) findViewById(R.id.detail_number);
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }
            else {
                temp.setVisibility(View.VISIBLE);
                temp_content.setVisibility(View.VISIBLE);
            }

            temp_content=(TextView) findViewById(R.id.detail_mail_content);
            temp_content.setText(one.getEmail());
            temp=(TextView) findViewById(R.id.detail_mail);
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }
            else {
                temp.setVisibility(View.VISIBLE);
                temp_content.setVisibility(View.VISIBLE);
            }

            temp_content=(TextView) findViewById(R.id.detail_address_content);
            temp_content.setText(one.getAddress());
            temp=(TextView) findViewById(R.id.detail_address);
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }
            else {
                temp.setVisibility(View.VISIBLE);
                temp_content.setVisibility(View.VISIBLE);
            }

            temp_content=(TextView) findViewById(R.id.detail_remark_content);
            temp_content.setText(one.getComment());
            temp=(TextView) findViewById(R.id.detail_remark);
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }
            else {
                temp.setVisibility(View.VISIBLE);
                temp_content.setVisibility(View.VISIBLE);
            }

            temp_content=(TextView) findViewById(R.id.detail_birthday_content);
            temp_content.setText(one.getBirthday());
            temp=(TextView) findViewById(R.id.detail_birthday);
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }
            else {
                temp.setVisibility(View.VISIBLE);
                temp_content.setVisibility(View.VISIBLE);
            }
        }

        FloatingActionButton camera= (FloatingActionButton) findViewById(R.id.camera);
        if(!TextUtils.isEmpty(photoPath)) {
            camera.setVisibility(INVISIBLE);
            Glide.with(this).load(photoPath).into(photo); //头像载入工具
        }
        else {
            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent2=new Intent(DetailActivity.this, EditActivity.class);
                    intent2.putExtra("id",String.valueOf(personId));
                    startActivityForResult(intent2, 1);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        //Toast.makeText(this,"更新", Toast.LENGTH_SHORT).show();
        initInfo(name);
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
