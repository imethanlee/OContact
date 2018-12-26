package com.example.gg.ocontact;

import android.content.Context;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

public class EditListviewAdapter extends BaseAdapter implements View.OnClickListener, View.OnTouchListener, View.OnFocusChangeListener, View.OnLongClickListener {
    private int selectedEditTextPosition = -1;
    private List<ItemBean> mList;
    private Context mContext;
    public EditListviewAdapter(List<ItemBean> mList,Context mContext) {
        this.mContext=mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.edit_activity_views, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.editText.setOnTouchListener(this); // 正确写法
        vh.editText.setOnFocusChangeListener(this);
        vh.editText.setTag(position);
        if (selectedEditTextPosition != -1 && position == selectedEditTextPosition) { // 保证每个时刻只有一个EditText能获取到焦点
            vh.editText.requestFocus();
        } else {
            vh.editText.clearFocus();
        }
        String text = mList.get(position).getText();
        String hint = mList.get(position).getHint();
        //////////
        if(position==0)
        {
            vh.imageView.setVisibility(View.VISIBLE);
            vh.editText.setVisibility(View.GONE);
            vh.editText.setFocusable(false);
            if(mList.get(position).getBitmap()!=null)
                vh.imageView.setImageBitmap(mList.get(position).getBitmap());
        }

        if(mList.get(position).getExitMessage())
            vh.editText.setText(text);
        vh.editText.setHint(hint);


        vh.editText.setSelection(vh.editText.length());
        convertView.setTag(R.id.item_root, position); // 应该在这里让convertView绑定position
        //convertView.setOnClickListener(this);
        //convertView.setOnLongClickListener(this);
        return convertView;
    }

    private TextWatcher mTextWatcher = new MyTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (selectedEditTextPosition != -1) {
                Log.w("MyEditAdapter", "onTextPosiotion " + selectedEditTextPosition);
                ItemBean itemTest = (ItemBean) getItem(selectedEditTextPosition);
                itemTest.setText(s.toString());
            }
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            EditText editText = (EditText) v;
            selectedEditTextPosition = (int) editText.getTag();
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText editText = (EditText) v;
        if (hasFocus) {
            editText.addTextChangedListener(mTextWatcher);
        } else {
            editText.removeTextChangedListener(mTextWatcher);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onLongClick(View view) {
        //    if (view.getId() == R.id.item_root) {
        //        int position = (int) view.getTag(R.id.item_root);
        //        Toast.makeText(mContext, "长按第 " + position + " 个item", Toast.LENGTH_SHORT).show();
        //    }
        return true;
    }

    public class ViewHolder {
        EditText editText;
        ImageView imageView;
        public ViewHolder(View convertView) {
            editText = (EditText) convertView.findViewById(R.id.et_test);
            imageView = (ImageView)convertView.findViewById(R.id.im_test);
        }
    }
}
