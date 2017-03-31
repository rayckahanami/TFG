package rayckaprojects.tfg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ComandShowActivity extends AppCompatActivity {
    List<AdapterListComandModel> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comand_show);
        createDataModel();

        ListView listView = (ListView) this.findViewById(R.id.listView);
        ComandListAdapter adapter = new ComandListAdapter(this,data,R.layout.list_item);
        listView.setAdapter(adapter);
    }

    private void createDataModel(){
        DatabaseHandler db = new DatabaseHandler(this);
        data = new ArrayList<AdapterListComandModel>();
        AdapterListComandModel item;
        int count = db.getComandsCount();
        int count2=0;
        while(count2<count){
            item = new AdapterListComandModel(db.getComand(count+1).getComandName(),db.getComand(count+1).getComandText());
            data.add(item);

        }

    }

}
