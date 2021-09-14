package sample;

import javafx.beans.property.SimpleStringProperty;

public class MyLog {
    private final SimpleStringProperty timeCol;
    private final SimpleStringProperty typeCol;
    private final SimpleStringProperty textCol;

    public MyLog(String timeCol, String typeCol, String textCol) {

        this.timeCol =  new SimpleStringProperty(timeCol);
        this.typeCol =  new SimpleStringProperty(typeCol);
        this.textCol =  new SimpleStringProperty(textCol);
    }

    public String getTimeCol() {
        return timeCol.get();
    }

    public SimpleStringProperty timeColProperty() {
        return timeCol;
    }

    public void setTimeCol(String timeCol) {
        this.timeCol.set(timeCol);
    }

    public String getTypeCol() {
        return typeCol.get();
    }

    public SimpleStringProperty typeColProperty() {
        return typeCol;
    }

    public void setTypeCol(String typeCol) {
        this.typeCol.set(typeCol);
    }

    public String getTextCol() {
        return textCol.get();
    }

    public SimpleStringProperty textColProperty() {
        return textCol;
    }

    public void setTextCol(String textCol) {
        this.textCol.set(textCol);
    }
}
