package com.liwei.clock.config;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.im.android.api.model.UserInfo;
import com.liwei.clock.R;
import com.liwei.clock.activity.InnerItemOnclickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView适配器
 * Created by LIWEI on 2017/10/19.
 */
public class MyExpandableListViewAdapter extends BaseExpandableListAdapter implements View.OnClickListener {
    private InnerItemOnclickListener mListener;
    private Context context;
    private LayoutInflater inflater;

    private String[] group = new String[]{"我的好友"};
    private List<List<UserInfo>> childs = new ArrayList<>();

    public MyExpandableListViewAdapter(Context context, List<UserInfo> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        childs.add(0, list);
        Log.i(this.getClass().toString(), list.toString());
    }

    @Override
    public int getGroupCount() {
        return group.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return childs.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return group[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return childs.get(i).get(i1).getUserName();
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.item_elv_group, null);

        ImageView iv_group_icon = view.findViewById(R.id.iv_group_icon);
        TextView tv_group_name = view.findViewById(R.id.tv_group_name);
        TextView tv_group_number = view.findViewById(R.id.tv_group_number);

        tv_group_name.setText(group[i]);
        tv_group_number.setText(childs.get(i).size() + "/" + childs.get(i).size());

        //TODO b 子列表是否展开,判断后设置图标
        if (b) {
            iv_group_icon.setImageResource(android.R.drawable.ic_input_add);
        } else {
            iv_group_icon.setImageResource(android.R.drawable.ic_menu_view);
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {

//        final ViewHolder viewHolder;
//        if (convertView == null) {
//            viewHolder = new ViewHolder();
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_elv_child,
//                    null);
//            viewHolder.bt1 = convertView.findViewById(R.id.bt_child_id);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
        View view = inflater.inflate(R.layout.item_elv_child, null);
        //TODO 设置  child 的图标和 信息
        ImageView iv_child_icon = view.findViewById(R.id.iv_child_icon);
        TextView tv_child_info = view.findViewById(R.id.tv_child_info);
        Button bt_child_id = view.findViewById(R.id.bt_child_id);

        TextView tv_child_name = view.findViewById(R.id.tv_child_name);
        TextView tv_child_network = view.findViewById(R.id.tv_child_network);

        bt_child_id.setText(childs.get(i).get(i1).getUserName());
        bt_child_id.setOnClickListener(this);
        tv_child_info.setText(childs.get(i).get(i1).getNickname());
        tv_child_name.setText(childs.get(i).get(i1).getUserName());
        tv_child_network.setText(i1 % 2 == 0 ? "5G" : "6G");
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public final static class ViewHolder {
        Button bt1, bt2;
        TextView tv;
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }
}
