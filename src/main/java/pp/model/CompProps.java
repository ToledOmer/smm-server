package pp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class CompProps extends BasicProps {
    private int audioChannels;
    private String audioCodec;
    private int audioSampleRate; //KHz
    private int audioBitRate; // kbit/s
    private String videoCodec = "libx264";
    private int frames ;
    private int per ;
    private int[] resolution = {0,0} ;
    // format var is in father class
    private int videoBitRate ; //kbps

    public int getAudioChannels() {
        return audioChannels;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public int getAudioSampleRate() {
        return audioSampleRate;
    }

    public int getVideoBitRate() {
        return videoBitRate;
    }

    public int getAudioBitRate() {
        return audioBitRate;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public int getFrames() {
        return frames;
    }

    public int getPer() {
        return per;
    }



    public int[] getResolution() {
        return resolution;
    }

    public CompProps(@JsonProperty("in") String in,
                     @JsonProperty("out") String out,
                     @JsonProperty("format") String format,
                     @JsonProperty("audioChannels") int audioChannels,
                     @JsonProperty("audioCodec") String audioCodec,
                     @JsonProperty("audioSampleRate") int audioSampleRate,
                     @JsonProperty("audioBitRate") int audioBitRate,
                     @JsonProperty("videoCodec") String videoCodec,
                     @JsonProperty("frames") int frames,
                     @JsonProperty("per") int per,
                     @JsonProperty("resolution") int[] resolution,
                     @JsonProperty("videoBitRate") int videoBitRate) {
        super(in,
//                out,
                format);
        this.audioChannels = audioChannels;
        this.audioCodec = audioCodec;
        this.audioSampleRate = audioSampleRate;
        this.audioBitRate = audioBitRate;
        this.videoCodec = videoCodec;
        this.frames = frames;
        this.per = per;
        this.resolution = resolution;
        this.videoBitRate = videoBitRate;
    }
}
