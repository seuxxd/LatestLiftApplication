package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.product.xxd.latestliftapplication.R;
import com.product.xxd.latestliftapplication.UIActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import util.RunningStatus;

/**
 * Created by SEUXXD on 2017-09-15.
 */

public class StatusAdapter extends BaseAdapter {
//    这个是运行信息的key
    private List<String> mStatusListKey = RunningStatus.getStatusChineseList();
//    这个是运行信息对应的value值，用数字来表示
    private static List<Integer> mStatusListNumberValue;
//    运行时对应的value值，字符串表示
    private static List<String> mStatusListStringValue;

    public static List<Integer> getStatusListNumberValue() {
        return mStatusListNumberValue;
    }

    public static void setStatusListNumberValue(List<Integer> statusListNumberValue) {
        mStatusListNumberValue = statusListNumberValue;
    }

//    Context信息
    private Context mContext;
//    value信息

    public StatusAdapter(Context context , List<Integer> list) {
        mContext = context;
        mStatusListNumberValue = list;
        mStatusListStringValue = new ArrayList<>();
        for (int i = 0 ; i < mStatusListNumberValue.size() ; i ++){
            switch (mStatusListNumberValue.get(i)){
                case 0:
                    mStatusListStringValue.add(RunningStatus.getZeroRunningInfoValue().get(i));
                    break;
                case 1:
                    mStatusListStringValue.add(RunningStatus.getOneRunningInfoValue().get(i));
                    break;
                case 2:
                    mStatusListStringValue.add("数据错误");
                    break;
                case 3:
                    mStatusListStringValue.add("无数据");
                    break;
            }
        }
    }

    //    ViewHolder提高ListView效率
    static class ViewHolder{
    @BindView(R.id.info_key)
    TextView mInfoKey;
    @BindView(R.id.info_value)
    TextView mInfoValue;

    public ViewHolder(View view) {
        ButterKnife.bind(this,view);
    }
}
    @Override
    public int getCount() {
        return mStatusListKey.size();
    }

    @Override
    public Object getItem(int position) {
        return mStatusListKey.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView != null){
            mHolder = (ViewHolder) convertView.getTag();
        }
        else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.infolist_content,null);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        }
        mHolder.mInfoKey.setText(mStatusListKey.get(position));
//        接下来把数字转换成对应的字符串,方便客户端阅读
        mHolder.mInfoValue.setText(mStatusListStringValue.get(position));

        return convertView;
    }
}
