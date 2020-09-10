package pp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class BasicProps {
    String in;
//    String out;
    String format;

    public String getIn() {
        return in;
    }

//    public String getOut() {
//        return out;
//    }

    public String getFormat() {
        return format;
    }

    public BasicProps(@JsonProperty("in") String in,
//                      @JsonProperty("out") String out,
                      @JsonProperty("format") String format) {
        this.in = in;
//        this.out = out;
        this.format = format;
    }
}
