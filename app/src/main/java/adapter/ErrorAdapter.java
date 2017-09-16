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
import butterknife.OnClick;
import util.ErrorInfo;

/**
 * Created by SEUXXD on 2017-09-15.
 */

public class ErrorAdapter extends BaseAdapter {
    private Context mContext;
    private static List<Integer> mErrorInfoNumberValue;

    private static List<String> mErrorInfoStringValue;

    private static List<Integer> mErrorInfoNumberValueOrigin;

    private static List<String> mErrorInfoStringValueOrigin;

    public static List<Integer> getmErrorInfoNumberValueOrigin() {
        return mErrorInfoNumberValueOrigin;
    }

    public static List<String> getmErrorInfoStringValueOrigin() {
        return mErrorInfoStringValueOrigin;
    }

    public static List<Integer> getmErrorInfoNumberValue() {
        return mErrorInfoNumberValue;
    }

    public static List<String> getmErrorInfoStringValue() {
        return mErrorInfoStringValue;
    }

    private final String[] mErrorContent = {"正常","发生","数据错误","无数据"};
    private List<String> mErrorListKey = ErrorInfo.getmErrorChineseInfo();
    public ErrorAdapter(Context context , List<Integer> list) {
        mContext = context;
        mErrorInfoNumberValue = list;
        mErrorInfoStringValue = new ArrayList<>();
        mErrorInfoNumberValueOrigin = new ArrayList<>();
        mErrorInfoStringValueOrigin = new ArrayList<>();
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
        for (int i = 0 ; i < mErrorListKey.size() ; i ++){
            mErrorInfoNumberValueOrigin.add(list.get(i));
            mErrorInfoStringValueOrigin.add(mErrorInfoStringValue.get(i));
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;
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
//        为了使用position这个参数，不得不使用该方法
        mViewHolder.mErrorInfoValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = mErrorInfoNumberValue.get(position);
                temp = (temp + 1) % 4;
                mErrorInfoNumberValue.set(position,temp);
                mErrorInfoStringValue.set(position,mErrorContent[temp]);
                mViewHolder.mErrorInfoValue.setText(mErrorContent[temp]);
            }
        });
        return convertView;
    }
}
