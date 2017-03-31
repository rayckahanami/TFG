package rayckaprojects.tfg;

/**
 * Created by RayckaPC on 01/20/17.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TextProcessor {
    static TextToSpeech t1;
    private static Context cont;
    private static CallMethods callMe= new CallMethods();

    public TextProcessor(Context cont) {
        this.cont = cont;
    }

    public void StringProcessor(String SpeechToTexto, SharedPreferences SP,SharedPreferences SPTEL) {

        DatabaseHandler db = new DatabaseHandler(cont);
        int countActual = db.getComandsCount();
        Toast.makeText(cont,"Actualmente hay "+ String.valueOf(countActual) +"asignaciones de texto a comando", Toast.LENGTH_SHORT).show();
        List<Comand> comandlist = db.getAllComands();
        boolean flag = false;
        for (int i = 0; i < countActual; i++) {
            String help = comandlist.get(i).getComandName();
            if (SpeechToTexto.contains(help)) {
                flag = true;
                String textValue = comandlist.get(i).getComandText();
                Toast.makeText(cont, "La comanda existe y es ", Toast.LENGTH_SHORT).show();
                Toast.makeText(cont, textValue, Toast.LENGTH_SHORT).show();
                menuReconocido(textValue, SpeechToTexto, SP,SPTEL);
            }
        }
        if (flag == false) {
            Toast.makeText(cont, "Comando no reconocido", Toast.LENGTH_SHORT).show();

        }
    }

    public static void menuReconocido(String textValue, String StringToText, SharedPreferences SP,SharedPreferences SPTEL) {

        DatabaseHandler db = new DatabaseHandler(cont);
        if (textValue.equalsIgnoreCase("call")) {
        callMe.call(cont,StringToText,SP);

        }else if (textValue.equalsIgnoreCase("showcomands")){
            int count =db.getComandsCount();
            List<Comand> comandlistA = db.getAllComands();
            for (int i=0;i<count;i++){
                Toast.makeText(cont,String.valueOf(comandlistA.get(i).getComand_id()),Toast.LENGTH_SHORT).show();
                Toast.makeText(cont,"Caller: "+comandlistA.get(i).getComandName()+", Valor asignado: "+comandlistA.get(i).getComandText(),Toast.LENGTH_LONG).show();
            }
            cont.startActivity(new Intent(cont,ComandShowActivity.class));

        }else if (textValue.equalsIgnoreCase("DeleteAll")) {
            db.deleteAll();
            Toast.makeText(cont, "borrados", Toast.LENGTH_SHORT).show();
        }else if(textValue.equalsIgnoreCase("websearch")){
            String helper =StringToText.replace("busca ","");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //   intent.setData(Uri.parse("http://www.lasallegracia.cat"));
            intent.setData(Uri.parse("https://www.google.es/?gws_rd=ssl#q="+helper));
            cont.startActivity(intent);
        }else if(textValue.equalsIgnoreCase("addcomand")){
            String helper =StringToText.replace("añadir comando","");
            String array[] =helper.split(" ");
            String comand=array[array.length - 1];
            String text= helper.replace(comand,"");
            text=text.trim();
            int count= db.getComandsCount() +1;
            db.addComand(new Comand(count,text,comand));

            Toast.makeText(cont, "id: "+db.getComand(count).getComand_id()+", caller: "+db.getComand(count).getComandName()+", Value: "+db.getComand(count).getComandText(), Toast.LENGTH_SHORT).show();
        }

    }
}
