package rayckaprojects.tfg;


import java.io.Serializable;

/**
 * Created by RayckaPC on 02/03/17.
 */

public class Comand implements Serializable {
    int comand_id;
    String comandName;
    String comandText;

    public Comand() {
    }
    public Comand(int comand_id,String comandName,String comandText) {
        this.comand_id = comand_id;
        this.comandName=comandName;
        this.comandText= comandText;
    }
    public Comand(String comandName,String comandText) {
        this.comandName=comandName;
        this.comandText= comandText;
    }

    public String getComandName() {
        return comandName;
    }

    public void setComandName(String comandName) {
        this.comandName = comandName;
    }

    public void setComand_id(int comand_id) {
        this.comand_id = comand_id;
    }

    public int getComand_id() {
        return comand_id;
    }

    public String getComandText() {
        return this.comandText;
    }

    public void setComandText(String comandText) {
        this.comandText = comandText;
    }

}
