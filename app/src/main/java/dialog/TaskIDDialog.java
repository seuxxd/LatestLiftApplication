package dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.product.xxd.latestliftapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SEUXXD on 2017/9/25.
 */

public class TaskIDDialog extends Dialog {

    @BindView(R.id.dialog_taskid_title)
    TextView mTitle;
    @BindView(R.id.taskid_list)
    ListView mListView;
    public TaskIDDialog(Context context) {
        super(context,R.style.taskid_dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_taskid,null);
        setContentView(view);
        ButterKnife.bind(this,view);
    }
    public void setTitle(String str){
        mTitle.setText(str);
    }
    public void setAdapter(BaseAdapter adapter){
        mListView.setAdapter(adapter);
    }
    public ListView getListView(){
        return mListView;
    }
}
