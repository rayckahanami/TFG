package rayckaprojects.tfg;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Created by RayckaPC on 02/14/17.
 */

public class CallMethods {
    private static TextToSpeech t1;

    private static Context context;

    public static void call(Context cont, String StringToText, SharedPreferences SP){
        t1=new TextToSpeech(cont.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.getDefault());
                }
            }
        });
        context=cont;
        StringToText = StringToText.replace("llama a ", "");

        if (isValidLong(StringToText)) {
         //   Toast.makeText(cont, ""+StringToText,Toast.LENGTH_SHORT).show();
            calling(cont,StringToText);
        }else {
            //IS TEXT
            StringToText=StringToText.trim();
            if (SP.contains(StringToText)){
                //Exist in SharredPreferences
                String result =SP.getString(StringToText,"");
                Toast.makeText(cont,"El nombre buscado esta en la SharedPreferences "+result,Toast.LENGTH_SHORT).show();
                calling(cont,result);
            }else{
                SharedPreferences SPTEL = cont.getSharedPreferences("sharedPrefTel",cont.MODE_PRIVATE);
                Toast.makeText(cont,"NO ESTA EN LA SHARED PREFERENCES: "+StringToText,Toast.LENGTH_SHORT).show();
                Map<String,?> map = SPTEL.getAll();
                int value=0;
                ArrayList<Phone> arrayList = new ArrayList<>();
                for (Map.Entry<String, ?> entry : map.entrySet()){
                    if (entry.getKey().toLowerCase().contains(StringToText)){
                        if (entry.getKey().toLowerCase().equals(StringToText)){
                            calling(cont,entry.getValue().toString());
                            break;
                        }
                        arrayList.add(new Phone(entry.getKey(),entry.getValue().toString()));
                    }

                }
                if (arrayList.size()!=0 && arrayList.size() ==1){
                    calling(cont,arrayList.get(0).getPhone());

                }else {
                    Toast.makeText(cont,"Hay " + arrayList.size()+" coincidencias",Toast.LENGTH_SHORT).show();
                    Iterator<Phone> it = arrayList.iterator();

                    while(it.hasNext()){
                        Phone p = it.next();
                        Toast.makeText(cont,p.getName(),Toast.LENGTH_SHORT).show();
                        t1.speak("Existe un "+p.getName(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }

            }
        }


        }
    public static Boolean isValidLong(String value) {
        try {
            value=value.replace(" ","");
        //    Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
            Long val = Long.parseLong(value);
       //     Toast.makeText(context,""+val, Toast.LENGTH_SHORT).show();

            if (val != null) {
                return true;
            } else {
                return false;
            }

        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static void calling(Context cont, String StringToText){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
     //           Toast.makeText(cont, "Aplicación sin permisos para llamada",Toast.LENGTH_SHORT).show();
            }else{
      //          Toast.makeText(cont, "hola tengo permisos para llamar!",Toast.LENGTH_SHORT).show();
                cont.startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", StringToText, null)));
            }
        }else{
            cont.startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", StringToText, null)));
        }
    }

    public static ArrayList<Phone> readContacts(Context cont) {
        ArrayList<Phone> contacts= new ArrayList<Phone>();
        ContentResolver cr = cont.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phoneNumber="";
            //    Log.i("Names", name);
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    // Query phone here. Covered next
                    Cursor phones = cont.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    while (phones.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                      //  Log.i("Number", phoneNumber);


                    }
                    phones.close();
                }
                Phone p = new Phone(name.toLowerCase(),phoneNumber);
                contacts.add(p);
            }
        }
        return  contacts;
    }
}


