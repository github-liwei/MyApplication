package com.liwei.clock.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.liwei.clock.R;

/**
 * ListView适配器
 * Created by LIWEI on 2017/10/19.
 */
public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater inflater;

    private String[] group = new String[]{"我的好友", "同学", "同事", "GirlFriends"};
    private String[][] childs = new String[][]{{"习大大", "李克强", "普京", "金正恩", "安倍晋三"},
            {"刘铁男", "万庆良", "周永康", "徐才厚", "谷俊山", "令计划", "郭伯雄", "苏荣", "陈水扁", "蒋洁敏", "李东生", "白恩培"},
            {"马云", "麻花藤", "李彦宏", "周鸿祎", "雷布斯", "库克"},
            {"李冰冰", "范冰冰", "李小璐", "杨颖", "周冬雨", "Lady GaGa", "千颂伊", "尹恩惠"}};

    public MyExpandableListViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return group.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return childs[i].length;
    }

    @Override
    public Object getGroup(int i) {
        return group[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return childs[i][i1];
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
        tv_group_number.setText(childs[i].length + "/" + childs[i].length);

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
        View view = inflater.inflate(R.layout.item_elv_child, null);
        //TODO 设置  child 的图标和 信息
        ImageView iv_child_icon = view.findViewById(R.id.iv_child_icon);
        TextView tv_child_info = view.findViewById(R.id.tv_child_info);

        TextView tv_child_name = view.findViewById(R.id.tv_child_name);
        TextView tv_child_network = view.findViewById(R.id.tv_child_network);

        tv_child_name.setText(childs[i][i1]);
        tv_child_network.setText(i1 % 2 == 0 ? "5G" : "6G");
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
