package rayckaprojects.tfg;

/**
 * Created by Bernat on 3/31/2017.
 */

public class AdapterListComandModel {
    private String comandName, comandText;

    public AdapterListComandModel(String comandName, String comandText) {
        this.comandName = comandName;
        this.comandText = comandText;
    }

    public String getComandName() {
        return comandName;
    }

    public void setComandName(String comandName) {
        this.comandName = comandName;
    }

    public String getComandText() {
        return comandText;
    }

    public void setComandText(String comandText) {
        this.comandText = comandText;
    }
}
