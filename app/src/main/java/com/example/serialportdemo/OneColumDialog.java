package com.example.serialportdemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;



import java.util.List;

/**
 * @author qiwenming
 * @date 2015-7-29 下午3:37:29
 * @ClassName: OneColumDialog
 * @Description: 单列选择的条目
 */
public class OneColumDialog extends Dialog {

    private TextView titleTv;
    private ListView listViewlv;
    private Context conext;
    private List<String> mList;
    private SelectListener listener;

    public OneColumDialog(Context context, List<String> mList, SelectListener listener) {
        super(context,R.style.OneColumDialog);
        this.conext = context;
        this.mList = mList;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.one_colum_layout);



        titleTv = (TextView)findViewById(R.id.tv_title);
        listViewlv = (ListView)findViewById(R.id.lv_one_list);
        listViewlv.setAdapter(new OneColumAdapter(conext,mList));
        listViewlv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                listener.selected(position,mList.get(position));
                dismiss();
            }
        });
    }

    /**
     * @author qiwenming
     * @date 2015-7-29 下午4:11:17
     * @ClassName: SelectListener
     * @Description: 回調接口
     */
    public interface SelectListener {
        void selected(int position, String value);
    }



}
