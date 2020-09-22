package pp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class CompProps extends BasicProps {
    private int audioChannels;
    private String audioCodec;
    private int audioSampleRate; //KHz
    private int audioBitRate; // kbit/s
    private String videoCodec = "libx264";
    private int frames;
    private int per;
    private int[] resolution = {0, 0};
    // format var is in father class
    private int videoBitRate; //kbps

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

    public CompProps(
            @JsonProperty("Format") String format,
            @JsonProperty("Audio Channels") String audioChannels,
            @JsonProperty("Audio Codec") String audioCodec,
            @JsonProperty("Audio Sample Rate") String audioSampleRate,
            @JsonProperty("Audio Bit Rate") String audioBitRate,
            @JsonProperty("Video Codec") String videoCodec,

            @JsonProperty("Video Frame Rate") String frameRate,

            @JsonProperty("Resolutions") String resolution,
            @JsonProperty("Video Bit Rate") String videoBitRate) {
        super(

                format);
        this.per = Integer.valueOf(frameRate.split("/")[1]);
        this.frames = Integer.valueOf(frameRate.split("/")[0]);
        this.audioChannels = Integer.valueOf(audioChannels);
        this.audioCodec = audioCodec;

        //50 khz --> 50*1000
        this.audioSampleRate = Integer.valueOf(audioSampleRate.split(" ")[0]) * 1000;

        //64 kbits --> 64*1024
        this.audioBitRate = Integer.valueOf(audioBitRate.split(" ")[0]) * 1024;
        this.videoCodec = videoCodec;
//        this.frames = frames;
//        this.per = per;

        this.resolution = new int[]{
                Integer.valueOf(resolution.split(" x ")[0]),
                Integer.valueOf(resolution.split(" x ")[1])};
        //5000 kbps --> 5000*1024
        this.videoBitRate = Integer.valueOf(videoBitRate.split(" ")[0]) * 1024;
    }
}
