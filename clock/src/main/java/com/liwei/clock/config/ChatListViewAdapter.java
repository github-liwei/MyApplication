package com.liwei.clock.config;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;
import com.liwei.clock.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天界面适配器
 * Created by LIWEI on 2017/10/23.
 */
public class ChatListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Message> datas = new ArrayList<>();

    private ViewHolder viewHolder;

    public ChatListViewAdapter(Activity activity, List<Message> datas) {
        this.context = activity.getApplicationContext();
        this.datas = datas;
    }

    public void addAllMessage(List<Message> ms) {
        datas = ms;
    }

    public void addMessage(Message m) {
        datas.add(m);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        String left = "";
        String right = "";
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_chat_msg, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (datas.get(i).getDirect() == MessageDirect.receive) {
            left = ((TextContent) datas.get(i).getContent()).getText();
            viewHolder.text_left.setText(left);
            viewHolder.left.setVisibility(View.VISIBLE);
            viewHolder.right.setVisibility(View.INVISIBLE);
        } else {
            right = ((TextContent) datas.get(i).getContent()).getText();
            viewHolder.text_right.setText(right);
            viewHolder.right.setVisibility(View.VISIBLE);
            viewHolder.left.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView text_left;
        public LinearLayout left;
        public TextView text_right;
        public LinearLayout right;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.text_left = rootView.findViewById(R.id.text_left);
            this.left = rootView.findViewById(R.id.left);
            this.text_right = rootView.findViewById(R.id.text_right);
            this.right = rootView.findViewById(R.id.right);
        }

    }

}
