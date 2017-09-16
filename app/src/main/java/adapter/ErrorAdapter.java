package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.product.xxd.latestliftapplication.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import util.ErrorInfo;

/**
 * Created by SEUXXD on 2017-09-15.
 */

public class ErrorAdapter extends BaseAdapter {
    private Context mContext;
    private static List<Integer> mErrorInfoNumberValue;
    private static List<String> mErrorInfoStringValue;
    private List<String> mErrorListKey = ErrorInfo.getmErrorChineseInfo();
    public ErrorAdapter(Context context , List<Integer> list) {
        mContext = context;
        mErrorInfoNumberValue = list;
        mErrorInfoStringValue = new ArrayList<>();
        for (int i = 0 ; i < mErrorInfoNumberValue.size() ; i ++){
            switch (mErrorInfoNumberValue.get(i)){
                case 0:
                    mErrorInfoStringValue.add("正常");
                    break;
                case 1:
                    mErrorInfoStringValue.add("发生");
                    break;
                case 2:
                    mErrorInfoStringValue.add("数据错误");
                    break;
                case 3:
                    mErrorInfoStringValue.add("无数据");
                    break;
                default:
                    break;
            }
        }
    }
//    ViewHolder类
    static class ViewHolder{
    @BindView(R.id.error_info_key)
    TextView mErrorInfoKey;
    @BindView(R.id.error_info_value)
    TextView mErrorInfoValue;

    public ViewHolder(View view) {
        ButterKnife.bind(this,view);
    }
}

    @Override
    public int getCount() {
        return mErrorListKey.size();
    }

    @Override
    public Object getItem(int position) {
        return mErrorListKey.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.errorlist_content,null);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);

        }
        else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mErrorInfoKey.setText(mErrorListKey.get(position));
        mViewHolder.mErrorInfoValue.setText(mErrorInfoStringValue.get(position));

        return convertView;
    }
}
