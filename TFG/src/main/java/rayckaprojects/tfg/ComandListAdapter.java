package rayckaprojects.tfg;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Bernat on 3/31/2017.
 */

public class ComandListAdapter extends BaseAdapter {

    private Activity activity;
    private List<AdapterListComandModel> data;
    private LayoutInflater inflater;
    private int item_layout;

    public ComandListAdapter(Activity activity, List<AdapterListComandModel> data, int item_layout) {
        this.activity = activity;
        this.data = data;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.item_layout = item_layout;
    }

    @Override
    public int getCount() { return this.data.size(); }

    @Override
    public AdapterListComandModel getItem(int position) {  return this.data.get(position);  }

    @Override
    public long getItemId(int position) {  return position;  }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){  convertView = this.inflater.inflate(item_layout,null);  }

        TextView itemName = (TextView) convertView.findViewById(R.id.ComandName);
        TextView itemText = (TextView) convertView.findViewById(R.id.ComandText);
        itemName.setText(data.get(position).getComandName());
        itemText.setText(data.get(position).getComandText());

        return convertView;
    }
}
