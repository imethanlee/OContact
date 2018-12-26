package com.example.gg.ocontact;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gg.ocontact.ContactDatabase;
import com.example.gg.ocontact.EditListviewAdapter;
import com.example.gg.ocontact.ItemBean;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    List<ItemBean> list = new ArrayList<>();
    EditListviewAdapter adapter;
    private String who;
    private String photoPath;

    //更改//
    public static final int TAKE_PHOTO = 1;
    public static final int CHOSE_PHOTO = 2;

    private Uri imageUri;
    private ListView listView;

    String nameInput = "";
    String workInput = "";
    String phoneInput = "";
    String emailInput = "";
    String pathInput = "";
    String addressInput = "";
    String birthInput = "";
    String noteInput = "";

    ItemBean photoImage ;
    ImageView imageView =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        who = getIntent().getStringExtra("id");
        if (who.equals("-1"))
            who = null;

        initialize(who);

        listView = (ListView) findViewById(R.id.list_view);
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
                            imageView = findViewById(R.id.im_test);
                            switch (which)
                            {
                                case 0:
                                    Date date = new Date();
                                    String photoName = Long.toString(date.getTime());
                                    File outputImage = new File(getExternalCacheDir(),photoName+".jpg");

                                    try {
                                        if(outputImage.exists()){
                                            outputImage.delete();
                                        }
                                        outputImage.createNewFile();
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }

                                    if(Build.VERSION.SDK_INT>=24){
                                        imageUri = FileProvider.getUriForFile(EditActivity.this,
                                                "com.example.gg.ocontact.fileprovider",outputImage);
                                        Toast.makeText(getApplicationContext(),
                                                imageUri.toString(), Toast.LENGTH_LONG).show();
                                    }else {
                                        imageUri = Uri.fromFile(outputImage);
                                    }


                                    photoPath = outputImage.getPath();
                                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                    startActivityForResult(intent,TAKE_PHOTO);

                                    break;
                                case 1:
                                    if(ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                                        ActivityCompat.requestPermissions(EditActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                                    }else {
                                        openAlbum();
                                    }
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


                //更改//
                if(TextUtils.isEmpty(nameOutput)|| TextUtils.isEmpty(phoneOutput)) {
                    Toast.makeText(EditActivity.this, "Name and phone number cannot be empty!", Toast.LENGTH_LONG).show();
                }
                else{
                    //数据库存储
                    if(TextUtils.isEmpty(who)){
                        saveData(nameOutput,workOutput,phoneOutput,emailOutput,tipOutput,addressOutput,birthOutput);
                        Toast.makeText(EditActivity.this, "save!"+nameOutput+phoneOutput, Toast.LENGTH_LONG).show();
                    }
                    else{
                        updateData(nameOutput,workOutput,phoneOutput,emailOutput,tipOutput,addressOutput,birthOutput);
                        Toast.makeText(EditActivity.this, "update!"+nameOutput+phoneOutput, Toast.LENGTH_LONG).show();
                    }
                    //Log.d(EditActivity.class.getSimpleName(), nameOutput+workOutput+phoneOutput);

                    finish();
                }

            }
        });
    }


    private void initialize(String id)
    {
        if (!TextUtils.isEmpty(id))
            findData(id);

        //更改//

        photoImage = new ItemBean();
        if(!TextUtils.isEmpty(pathInput)) {
            Bitmap bitmap = displayImage(pathInput);
            //photoImage.setBitmap(bitmap);
        }
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

    //更改//

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        photoImage.setBitmap(bitmap);
                        imageView.setImageBitmap(bitmap);
                        if (bitmap == null){
                            Toast.makeText(this, "456789123", Toast.LENGTH_SHORT).show();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    if (Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        photoImage.setBitmap(displayImage(imagePath));
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        photoImage.setBitmap(displayImage(imagePath));
    }

    private String getImagePath(Uri uri,String selection){
        String path =null;

        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private Bitmap displayImage(String imagePath){
        if(imagePath != null){
            photoPath = imagePath;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Toast.makeText(getApplicationContext(), "path is " + imagePath,
                    Toast.LENGTH_LONG).show();
            //imageView.setImageBitmap(bitmap);
            return bitmap;
        }else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
            return null;
        }
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
        contactDatabase.setName(n);
        contactDatabase.setJob(w);
        contactDatabase.setPhoneNumber(p);
        contactDatabase.setBirthday(b);
        contactDatabase.setEmail(e);
        contactDatabase.setComment(no);
        contactDatabase.setAddress(ad);
        contactDatabase.setPhotoPath(photoPath);

        contactDatabase.updateAll("id = ?",who);
    }

    private void findData(String id)
    {
        //int i = Integer.parseInt(id);
        List<ContactDatabase> contactDatas = LitePal.where("id = ?",id).find(ContactDatabase.class);
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
