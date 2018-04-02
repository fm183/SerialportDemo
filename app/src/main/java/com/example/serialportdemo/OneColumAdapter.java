package com.example.serialportdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

public class OneColumAdapter extends BaseAdapter {

    private Context context;
    private List<String> mList;
    private LayoutInflater inflater;

    public OneColumAdapter(Context context, List<String> mList){
        this.context = context;
        this.mList = mList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            View v = inflater.inflate(R.layout.one_colum_item, null);
            viewHolder.itemTv = (TextView) v.findViewById(R.id.tv_item);
            v.setTag(viewHolder);
            convertView = v;
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemTv.setText(mList.get(position));
        return convertView;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int positon) {
        return mList.get(positon);
    }

    @Override
    public long getItemId(int positon) {
        // TODO Auto-generated method stub
        return positon;
    }

    public class ViewHolder {
        TextView itemTv;
    }

}

