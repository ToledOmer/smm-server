package pp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.LinkedList;

public class CombProp  extends  BasicProps{
    private LinkedList<String> ins;
    private String audio;
    private boolean mute;



    public CombProp(@JsonProperty("in") String in,
                    @JsonProperty("format") String format,
                    @JsonProperty("ins") LinkedList<String> ins,
                    @JsonProperty("audio")String audio,
                    @JsonProperty("mute")boolean mute) {
        super(
//                in,
                format);
        this.ins = ins;
        this.audio = audio;
        this.mute = mute;
    }



    public LinkedList<String> getIns() {
        return ins;
    }

    public void setIns(LinkedList<String> ins) {
        this.ins = ins;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }


}
