package com.example.gg.ocontact;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    private List<Person>mPersonList;
    private boolean visibleBoard[];
    private int mode;
    private int previousPosition;
    private View previousV;

    //boolean visible=false;
    void switchvisible(View v,int pos){
        if(visibleBoard[pos]==false) {
            visibleBoard[pos] = true;
            //Toast.makeText(v.getContext(),"可见",Toast.LENGTH_SHORT).show();
        }
        else{
            visibleBoard[pos]= false;
            //Toast.makeText(v.getContext(),"不可见",Toast.LENGTH_SHORT).show();
        }
    }




    class ViewHolder extends RecyclerView.ViewHolder{
        //此处列出需要响应点击事件的布局
        ImageView personImage;
        TextView personName;
        LinearLayout lowerLayout;
        LinearLayout textLayout;
        Button phoneCallButton1;
        //Button phoneCallButton2;
        Button informationButton;
        Button messageButton;
        TextView footerText;
        TextView headerText;


        public ViewHolder(View view)
        {
            super(view);
            if (itemView == mHeaderView){
                headerText=(TextView)view.findViewById(R.id.header_text);
                return;
            }
            if (itemView == mFooterView){
                footerText=(TextView)view.findViewById(R.id.footer_text);
                return;
            }

            personImage=(ImageView)view.findViewById(R.id.person_image);
            personName=(TextView)view.findViewById(R.id.person_name);
            lowerLayout=(LinearLayout)view.findViewById(R.id.lower_layout);
            textLayout=(LinearLayout)view.findViewById(R.id.text_layout);
            phoneCallButton1=(Button)view.findViewById(R.id.phone_call1);
            //phoneCallButton2=(Button)view.findViewById(R.id.phone_call3);
            informationButton=(Button)view.findViewById(R.id.details);
            messageButton=(Button)view.findViewById(R.id.message);


        }
    }
    public PersonAdapter(List<Person>personList, int mode){
        this.mode=mode;

        mPersonList=personList;
        visibleBoard=new boolean[mPersonList.size()];
        for(int i=0;i<mPersonList.size();i++)
        {
            visibleBoard[i]=false;
        }
    }
    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new ViewHolder(mHeaderView);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new ViewHolder(mFooterView);
        }//判断是否是开头或者结尾

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item,parent,false);
        //获取person_item的布局
        final ViewHolder holder=new ViewHolder(view);

        holder.lowerLayout.setVisibility(View.GONE);

        holder.textLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (mode == 1){
                    int position=holder.getAdapterPosition() - 1;
                    final int position_anim = position;
                    final View v_anim = v;
                    Person person = mPersonList.get(position);
                    //Toast.makeText(v.getContext(),"你点击了文本布局："+person.getName(),Toast.LENGTH_SHORT).show();

                /*
                for(int i=0;i<mPersonList.size();i++)
                {
                    Person person0=mPersonList.get(i);
                    if(person!=person0)
                        holder.lowerLayout.setVisibility(View.GONE);
                }
                */
                    Toast.makeText(v.getContext(),"你点击了图片:"+String.valueOf(position),Toast.LENGTH_SHORT).show();

                    if (visibleBoard[position] == false) {
                        holder.lowerLayout.setVisibility(View.VISIBLE);
                        switchvisible(v_anim,position_anim);

                        TranslateAnimation mShowAction;
                        TranslateAnimation mHiddenAction;
                        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                                -0.3f, Animation.RELATIVE_TO_SELF, 0.0f);
                        mShowAction.setDuration(500);
                        holder.lowerLayout.startAnimation(mShowAction);
                    }
                    else {
                        /*
                        TranslateAnimation mShowAction;
                        final TranslateAnimation mHiddenAction;
                        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                                0f, Animation.RELATIVE_TO_SELF, -0.3f);
                        mHiddenAction.setDuration(500);
                        holder.lowerLayout.startAnimation(mHiddenAction);
                        */

                        holder.lowerLayout.setVisibility(View.GONE);
                        switchvisible(v_anim,position_anim);

                    }

                    previousPosition = position;
                }
                else {
                    int position = holder.getAdapterPosition() - 1;
                    Toast.makeText(v.getContext(),"Heading to result "+String.valueOf(position) + "'s detail",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.personImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition()-1;
                Person person=mPersonList.get(position);
                Toast.makeText(v.getContext(),"你点击了图片:"+person.getName(),Toast.LENGTH_SHORT).show();

            }
        });



        holder.phoneCallButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition()-1;//因为头部占据一层
                Person person=mPersonList.get(position);
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+person.getPhoneNumber()));
                view.getContext().startActivity(intent);
                //点击电话1按钮后的逻辑。
            }
        });
        /*

        holder.phoneCallButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Person person=mPersonList.get(position);
                //点击电话2按钮后的逻辑。
            }
        });

*/
        holder.messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition()-1;
                Person person=mPersonList.get(position);
                //点击短信按钮后的逻辑。
                Intent intent=new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:"+person.getPhoneNumber()));
                view.getContext().startActivity(intent);
            }
        });


        holder.informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition()-1;
                Person person=mPersonList.get(position);
                Intent intent=new Intent(view.getContext(),DetailActivity.class);
                intent.putExtra("id",Integer.toString(person.getId()));
                ((Activity)view.getContext()).startActivityForResult(intent, 1);

                //点击详情按钮后的逻辑。


            }
        });

        return holder;
    }

    /*
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        //从列表中获取对象 并向布局中设置图片。
        Person person=mPersonList.get(position);
        holder.personImage.setImageResource(person.getImageId());
        holder.personName.setText(person.getName());
    }

    @Override
    public int getItemCount()
    {
        return mPersonList.size();
    }
    */


    //设置底部和头部布局的代码部分
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    private View mHeaderView;
    private View mFooterView;
    public View getHeaderView() {
        return mHeaderView;
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount()-1);
    }
    // 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    *
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null){
            return TYPE_NORMAL;
        }
        if (position == 0){
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount()-1){
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
            Person person=mPersonList.get(position-1);
            holder.personImage.setImageResource(person.getImageId());
            //holder.personName.setText("123");
            holder.personName.setText(person.getName());

            return;
        }else if(getItemViewType(position) == TYPE_FOOTER){
            String str;
            if(mode==1) {
                str = "这里一共有：" + String.valueOf(mPersonList.size()) + "名联系人";
            }
            else{
                if (mPersonList.size() != 0){
                    str = String.valueOf(mPersonList.size()) + " FOUND";
                }
                else {
                    str = "";
                }
            }
            //Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
            holder.footerText.setText(str);
            return;
        }else{

            String str;
            if (mode == 1){
                str="Life is easier with O Contact";
            }
            else{
                if (mPersonList.size() != 0){
                    str = "";
                    holder.headerText.setVisibility(View.GONE);
                }
                else {
                    str = "\n\n\n\n\n\nOops! Seems to be no match";
                }
            }
            //Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
            holder.headerText.setText(str);

            return;
        }
    }

    @Override
    public int getItemCount() {
        if(mHeaderView == null && mFooterView == null){
            return mPersonList.size();
        }else if(mHeaderView == null && mFooterView != null){
            return mPersonList.size()+ 1;
        }else if (mHeaderView != null && mFooterView == null){
            return mPersonList.size() + 1;
        }else {
            return mPersonList.size() + 2;
        }

    }

}
