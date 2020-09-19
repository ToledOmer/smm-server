package pp.model;

import com.fasterxml.jackson.annotation.*;

public class TrimProp extends BasicProps  {


    private int hours;

    private int min;

    private int sec;

    private int ms;

    private int hoursLen;

    private int minLen;

    private int secLen;
    private int msLen;

    public TrimProp(@JsonProperty("in") String in,
                    @JsonProperty("format") String format,
                    @JsonProperty("hours") int hours,
                    @JsonProperty("min")int min,
                    @JsonProperty("sec") int sec,
                    @JsonProperty("ms")int ms,
                    @JsonProperty("hoursLen") int hoursLen,
                    @JsonProperty("minLen") int minLen,
                    @JsonProperty("secLen") int secLen,
                    @JsonProperty("msLen") int msLen) {
        super(
//                in,
                format);
        this.hours = hours;
        this.min = min;
        this.sec = sec;
        this.ms = ms;
        this.hoursLen = hoursLen;
        this.minLen = minLen;
        this.secLen = secLen;
        this.msLen = msLen;
    }

    //    public TrimProp(,


    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }

    public int getMs() {
        return ms;
    }

    public void setMs(int ms) {
        this.ms = ms;
    }


    public int getHoursLen() {
        return hoursLen;
    }

    public void setHoursLen(int hoursLen) {
        this.hoursLen = hoursLen;
    }

    public int getMinLen() {
        return minLen;
    }

    public void setMinLen(int minLen) {
        this.minLen = minLen;
    }

    public int getSecLen() {
        return secLen;
    }

    public void setSecLen(int secLen) {
        this.secLen = secLen;
    }

    public int getMsLen() {
        return msLen;
    }

    public void setMsLen(int msLen) {
        this.msLen = msLen;
    }

}
