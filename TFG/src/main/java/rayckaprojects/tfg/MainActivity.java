package rayckaprojects.tfg;
/**
 * Owned by Bernat Alvira Perez under GNU General Public License.
 * Started in November 2016
 *
 * Summary: This Android Module  is an SpeechToText Recognizer, a Virtual Assistant.
 *  Implemented functionalities:
 *      - Speech Recognizer Implementation by Google Methods.
 *      - Calling methods
 *
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE1 = 5463;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private EditText txtSpeechInput;
    private ImageButton btnSpeak;
    private ImageButton btnKeyboard;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    public TextProcessor txtP;
    public TextToSpeech t1;
    public String sharedPref ="sharedPref";
    public String sharedPrefTel="sharedPrefTel";
    public ArrayList<Phone>p= null;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtP = new TextProcessor(getApplicationContext());
        setContentView(R.layout.activity_main);


        initializeSharedPrefs();        //Initiaizer method for the SharedPreferences used in this Module
        permissionCheckMethod();        //Checker method for Modules' permissions
        dbInitializerMethod();          //Initializer Method for our DataBase
        textToSpeechInitializer();      //Initializer Method for textToSpeech voice output

        //Set views to Objects - START
            txtSpeechInput = (EditText) findViewById(R.id.txtSpeechInput);
            btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
            btnSpeak.setOnClickListener(this);
            btnKeyboard = (ImageButton) findViewById(R.id.btnkeyboard);
            btnKeyboard.setOnClickListener(this);
        //Set views to Objects - END

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        textToSpeechInitializer();
    }

    /**
     * @param view -> the view
     * Summary: onClick implementation for buttons on the main screen.
     *
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSpeak:
                promptSpeechInput();
                break;
            case R.id.btnkeyboard:
             //   startService(new Intent(getApplication(), ChatHeadService.class));
                callTextProcesor();
                break;
        }
    }

    /**
     * Summary: Method displaying Google SpeechRecognizer display.
     *
     * */
    public void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     *
     * @param requestCode -> code identifier of the request
     * @param resultCode  -> code showing the result of the intent called in "startForActivityResult"
     * @param data        -> Data returned by the intent called in "startForActivityResult"
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                }if (requestCode == REQUEST_CODE1) {
                    /** if so check once again if we have permission */
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(this)) {
                            // continue here - permission was granted
                        }
                    }
                }
                break;
            }

        }

    }
    public void onStop(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
    public  void addSharedPrefs(){
        SharedPreferences sharedPref1 = this.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref1.edit();
        editor.putString("bernat", "675215911");
        editor.commit();
    }
    public  void addSharedPrefsPhone(ArrayList<Phone> p){
        SharedPreferences sharedPrefTel1 = this.getSharedPreferences(sharedPrefTel, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPrefTel1.edit();
        Iterator<Phone> iterator = p.iterator();
        while (iterator.hasNext()){
            Phone phone = iterator.next();
            editor1.putString(phone.getName(),phone.getPhone());
        }
        editor1.commit();
    }

    private void callTextProcesor(){
        String resp= txtSpeechInput.getText().toString().toLowerCase();
      //  Toast.makeText(MainActivity.this,resp,Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPref1 = this.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        SharedPreferences sharedPrefTel1 = this.getSharedPreferences(sharedPrefTel, Context.MODE_PRIVATE);
        txtP.StringProcessor(resp,sharedPref1,sharedPrefTel1);
        t1.speak(resp, TextToSpeech.QUEUE_FLUSH, null);
    }



    public void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                /** if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                /** request permission via start activity for result */
                startActivityForResult(intent, REQUEST_CODE1);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean readContacts = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(readContacts)
                    {
                        p = CallMethods.readContacts(getApplication());
                        addSharedPrefsPhone(p);
                    }
                }
                break;
        }


    }
    public void initializeSharedPrefs(){
        this.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        this.getSharedPreferences(sharedPrefTel, Context.MODE_PRIVATE);
        addSharedPrefs();
    }
    public void permissionCheckMethod(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.CALL_PHONE},
                    PERMISSIONS_MULTIPLE_REQUEST);
            checkDrawOverlayPermission();
        }
    }
    public void dbInitializerMethod(){
        DatabaseHandler db = new DatabaseHandler(getApplicationContext(),"Comands");

        if (db.getComandsCount() ==0) {
          //  Toast.makeText(this,"crea comands",Toast.LENGTH_SHORT).show();
            db.addBasicComands();
        }else {
            //Toast.makeText(this,"ya hay comands",Toast.LENGTH_SHORT).show();
        }
    }
    public void textToSpeechInitializer(){
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.getDefault());
                }
            }
        });
    }


}
