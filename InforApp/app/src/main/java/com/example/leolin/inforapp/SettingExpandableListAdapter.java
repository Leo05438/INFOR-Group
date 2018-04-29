package com.example.leolin.inforapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.content.ClipData.Item;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.security.acl.Group;
import java.util.ArrayList;

public class SettingExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<MGroup> gData;
    private ArrayList<ArrayList<MItem>> iData;
    private Context mContext;

    public SettingExpandableListAdapter(ArrayList<MGroup> gData, ArrayList<ArrayList<MItem>> iData, Context mContext) {
        this.gData = gData;
        this.iData = iData;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return gData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return iData.get(groupPosition).size();
    }

    @Override
    public MGroup getGroup(int groupPosition) {
        return gData.get(groupPosition);
    }

    @Override
    public MItem getChild(int groupPosition, int childPosition) {
        return iData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolderGroup groupHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.setting_exlist_title, parent, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.tv_group_name = (TextView) convertView.findViewById(R.id.setting_title);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
        groupHolder.tv_group_name.setText(gData.get(groupPosition).getgName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem itemHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.setting_exlist_item, parent, false);
            itemHolder = new ViewHolderItem();
            itemHolder.tv_name = (TextView) convertView.findViewById(R.id.setting_exlist_text);
            itemHolder.tb_check = (ToggleButton) convertView.findViewById(R.id.setting_exlist_toggle_button);
            convertView.setTag(itemHolder);
        }else{
            itemHolder = (ViewHolderItem) convertView.getTag();
        }
        itemHolder.tv_name.setText(iData.get(groupPosition).get(childPosition).getiName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private static class ViewHolderGroup{
        private TextView tv_group_name;
    }

    private static class ViewHolderItem{
        private TextView tv_name;
        private ToggleButton tb_check;
    }

}
