package com.example.gg.ocontact;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    List<ItemBean> list = new ArrayList<>();
    EditListviewAdapter adapter;
    private String who;
    private String photoPath;


    String nameInput = "";
    String workInput = "";
    String phoneInput = "";
    String emailInput = "";
    String pathInput = "";
    String addressInput = "";
    String birthInput = "";
    String noteInput = "";

    ItemBean photoImage ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        who = null;

        initialize(who);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                    //    指定下拉列表的显示数据
                    final String[] choices = {"拍照", "从相册选择"};
                    //    设置一个下拉的列表选择项
                    builder.setItems(choices, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switch (which)
                            {
                                case 0:
                                    break;
                                case 1:
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    builder.show();
                }
            }
        });


        adapter = new EditListviewAdapter( list,this);
        listView.setAdapter(adapter);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button save = findViewById(R.id.Save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOutput = ((ItemBean)adapter.getItem(1)).getText();
                String workOutput = ((ItemBean)adapter.getItem(2)).getText();
                String phoneOutput = ((ItemBean)adapter.getItem(3)).getText();
                String emailOutput = ((ItemBean)adapter.getItem(4)).getText();
                String tipOutput = ((ItemBean)adapter.getItem(5)).getText();
                String addressOutput = ((ItemBean)adapter.getItem(6)).getText();
                String birthOutput = ((ItemBean)adapter.getItem(7)).getText();


                //数据库存储
                if(!TextUtils.isEmpty(who))
                  saveData(nameOutput,workOutput,phoneOutput,emailOutput,tipOutput,addressOutput,birthOutput);
                else updateData(nameOutput,workOutput,phoneOutput,emailOutput,tipOutput,addressOutput,birthOutput);
                Log.d(EditActivity.class.getSimpleName(), nameOutput+workOutput+phoneOutput);

                finish();
            }
        });
    }


    private void initialize(String id)
    {
        if (!TextUtils.isEmpty(id))
            findData(id);

        photoImage = new ItemBean();
        list.add(photoImage);

        ItemBean editname = new ItemBean();
        editname.setHint("姓名");
        if(!TextUtils.isEmpty(nameInput)) {
            editname.setExitMessage(true);
            editname.setText(nameInput);
        }
        list.add(editname);

        ItemBean editwork = new ItemBean();
        editwork.setHint("工作");
        if(!TextUtils.isEmpty(workInput)) {
            editwork.setExitMessage(true);
            editwork.setText(workInput);
        }
        list.add(editwork);

        ItemBean editphone = new ItemBean();
        editphone.setHint("手机");
        if(!TextUtils.isEmpty(phoneInput)) {
            editphone.setExitMessage(true);
            editphone.setText(phoneInput);
        }
        list.add(editphone);

        ItemBean editemail = new ItemBean();
        editemail.setHint("电子邮件");
        if(!TextUtils.isEmpty(emailInput)) {
            editemail.setExitMessage(true);
            editemail.setText(emailInput);
        }
        list.add(editemail);

        ItemBean edittip = new ItemBean();
        edittip.setHint("备注");
        if(!TextUtils.isEmpty(noteInput)) {
            edittip.setExitMessage(true);
            edittip.setText(noteInput);
        }
        list.add(edittip);

        ItemBean editaddress = new ItemBean();
        editaddress.setHint("住址");
        if(!TextUtils.isEmpty(addressInput)) {
            editaddress.setExitMessage(true);
            editaddress.setText(addressInput);
        }
        list.add(editaddress);

        ItemBean editbirth = new ItemBean();
        editbirth.setHint("生日");
        if(!TextUtils.isEmpty(birthInput)) {
            editbirth.setExitMessage(true);
            editbirth.setText(birthInput);
        }
        list.add(editbirth);

    }

    private void saveData(String n, String w, String p, String e, String no, String ad, String b)
    {
        ContactDatabase contactDatabase = new ContactDatabase();
        contactDatabase.setName(n);
        contactDatabase.setJob(w);
        contactDatabase.setPhoneNumber(p);
        contactDatabase.setBirthday(b);
        contactDatabase.setEmail(e);
        contactDatabase.setComment(no);
        contactDatabase.setAddress(ad);
        contactDatabase.setPhotoPath(photoPath);
        contactDatabase.save();
    }

    private void updateData(String n, String w, String p, String e, String no, String ad, String b)
    {
        ContactDatabase contactDatabase = new ContactDatabase();
        contactDatabase.setName(nameInput);
        contactDatabase.setJob(workInput);
        contactDatabase.setPhoneNumber(phoneInput);
        contactDatabase.setBirthday(birthInput);
        contactDatabase.setEmail(emailInput);
        contactDatabase.setComment(noteInput);
        contactDatabase.setAddress(addressInput);
        contactDatabase.setPhotoPath(pathInput);
        contactDatabase.save();

        contactDatabase.setName(n);
        contactDatabase.setJob(w);
        contactDatabase.setPhoneNumber(p);
        contactDatabase.setBirthday(b);
        contactDatabase.setEmail(e);
        contactDatabase.setComment(no);
        contactDatabase.setAddress(ad);
        contactDatabase.setPhotoPath(photoPath);
        contactDatabase.save();
    }

    private void findData(String id)
    {
        //int i = Integer.parseInt(id);
        List<ContactDatabase> contactDatas = DataSupport.where("id = ?",id).find(ContactDatabase.class);
        for (ContactDatabase contactData : contactDatas){
            nameInput = contactData.getName();
            workInput = contactData.getJob();
            phoneInput = contactData.getPhoneNumber();
            emailInput = contactData.getEmail();
            pathInput = contactData.getPhotoPath();
            addressInput = contactData.getAddress();
            birthInput = contactData.getBirthday();
            noteInput = contactData.getComment();

        }
    }

}
