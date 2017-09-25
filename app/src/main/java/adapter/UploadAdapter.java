package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.product.xxd.latestliftapplication.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SEUXXD on 2017/9/25.
 */

public class UploadAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mValueList;
    private List<String> mKeyList;

    public UploadAdapter(Context context,List<String> valueList, List<String> keyList) {
        mContext = context;
        mValueList = valueList;
        mKeyList = keyList;
    }

    @Override
    public int getCount() {
        return mValueList.size();
    }

    @Override
    public Object getItem(int position) {
        return mValueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        @BindView(R.id.upload_key)
        TextView mKey;
        @BindView(R.id.upload_value)
        TextView mValue;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.upload_content,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mKey.setText(mKeyList.get(position));
        viewHolder.mValue.setText(mValueList.get(position));
        return convertView;
    }
}
