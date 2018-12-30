package com.example.gg.ocontact;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.example.gg.ocontact.MyThemeUtils.changeTheme;

public class ContactActivity extends AppCompatActivity {

    private List<Person> personList=new ArrayList<>();
    private PersonAdapter adapter;
    private DeletionReceiver receiver;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_menu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyThemeUtils.initTheme(ContactActivity.this);
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


        /*/ 删除更新广播
        receiver = new DeletionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.gg.ocontact.DeletionService");
        //registerReceiver(receiver, filter);
        */

        // ToolBar 中的 Menu
        Toolbar toolbar = findViewById(R.id.contact_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //toolbar.inflateMenu(R.menu.contact_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.contact_menu_contact_us:
                        Intent toGithub = new Intent(Intent.ACTION_VIEW);
                        toGithub.setData(Uri.parse("https://github.com/Atlas666/OContact"));
                        startActivity(toGithub);
                        break;
                    case R.id.item_search:
                        Intent toSearchActivity = new Intent(ContactActivity.this, SearchActivity.class);
                        startActivity(toSearchActivity);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case R.id.change_theme:
                        showThemeChooseDialog();
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
                intent.putExtra("id", "-1");
                startActivity(intent);
            }
        });


        // 回到最上方 FAB
        FloatingActionButton fab_to_the_top = findViewById(R.id.contact_fab_to_the_top);
        fab_to_the_top.getBackground().setAlpha(180);
        fab_to_the_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.scrollToPosition(0);
            }
        });



        //RecyclerView recyclerView = findViewById(R.id.contact_recyclerView);

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


    private void initRecycleView() {
        personList.clear();

        List<ContactDatabase> allContacts = LitePal.order("name asc").find(ContactDatabase.class);

        for(ContactDatabase person0: allContacts) {
            // 图片未成功
            Person person = new Person(person0.getId(), person0.getName(),
                    person0.getPhotoPath(), person0.getPhoneNumber());
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
        //Toast.makeText(this,"返回了主界面", Toast.LENGTH_SHORT).show();
       initRecycleView();
    }


    // 返回界面时，重新加载recycler view
    @Override
    protected void onResume() {
        super.onResume();
        initRecycleView();
        RecyclerView recyclerView = findViewById(R.id.contact_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PersonAdapter(personList, 1);
        recyclerView.setAdapter(adapter);
        setHeaderView(recyclerView);
        setFooterView(recyclerView);
    }

    public class DeletionReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //initRecycleView();
        }
    }

    private void showThemeChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactActivity.this);
        builder.setTitle("Choose theme");
        Integer[] res = new Integer[]{R.drawable.red_round, R.drawable.brown_round, R.drawable.blue_round,
                R.drawable.blue_grey_round, R.drawable.yellow_round, R.drawable.deep_purple_round,
                R.drawable.pink_round, R.drawable.green_round, R.drawable.deep_orange_round,
                R.drawable.grey_round, R.drawable.cyan_round, R.drawable.amber_round};
        List<Integer> list = Arrays.asList(res);
        ColorsListAdapter adapter = new ColorsListAdapter(ContactActivity.this, list);
        adapter.setCheckItem(MyThemeUtils.getCurrentTheme(ContactActivity.this).getIntValue());
        GridView gridView = (GridView) LayoutInflater.from(ContactActivity.this).inflate(R.layout.colors_panel_layout, null);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setCacheColorHint(0);
        gridView.setAdapter(adapter);
        builder.setView(gridView);
        final AlertDialog dialog = builder.show();
        gridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        int value = MyThemeUtils.getCurrentTheme(ContactActivity.this).getIntValue();
                        if (value != position) {
                            PreferenceUtils.getInstance(ContactActivity.this).saveParam("change_theme_key", position);
                            changeTheme(ContactActivity.this,MyThemeUtils.Theme.mapValueToTheme(position));
                            recreate();
                        }
                    }
                }

        );
    }

}
