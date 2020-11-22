package pp.config;

import org.springframework.beans.factory.annotation.Value;

public class UserConfig {
    @Value("${user.ffmpegPath}")
     String ffmpegPath;

    @Value("${user.ffprobePath}")
     String ffprobePath;


    }






