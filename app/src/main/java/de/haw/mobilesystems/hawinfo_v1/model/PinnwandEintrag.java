package de.haw.mobilesystems.hawinfo_v1.model;

/**
 * Created by jackl on 22.03.2017.
 */

public class PinnwandEintrag {

    private String timestamp;
    private String name;
    private String text;

    public PinnwandEintrag(String text){
        this.setText(text);
    }

    public PinnwandEintrag(){

    }

    public void setText(String text){
        this.text = text;

    }

    public String getText(){
        return text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
