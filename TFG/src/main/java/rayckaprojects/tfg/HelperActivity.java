package rayckaprojects.tfg;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HelperActivity extends ActionBarActivity {  //CLASE UNDERCONSTRUCTION, FUTURAS IMPLEMENTACIONES
    ListView lv;
    ArrayAdapter<Comand> mLeadsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String contents ="";
        EditText t = (EditText)findViewById(R.id.edtxt1);
        t.setText("probando");
        try {
            URLConnection conn = new URL("http://www.metrolyrics.com/printlyric/fear-of-the-dark-lyrics-iron-maiden.html").openConnection();
            InputStream in = conn.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            t.setText(total.toString());
        } catch (MalformedURLException e) {
            Log.w("","MALFORMED URL EXCEPTION");
        } catch (IOException e) {
            Log.w(e.getMessage(), e);
        }

        t.setText(" mas.." + contents + "final");
    }
}
