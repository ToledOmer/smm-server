package pp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;
import pp.model.TrimProp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class TrimServiceTest {

    @Test
    public void trimFile() {
    TrimService trimService = new TrimService();
        {
            int t= 0;
            new File("C:\\Users\\Omer\\Videos\\Call of Duty  Modern Warfare 2019\\check(compressed)(Trimmed).mp4").deleteOnExit();
            for (int m = 0; m < 20; m++){
                for (int s = 0; s <60 ; ) {
                        if(t==0)
                        {
                            s = 40;
                            m=17;
                            t++;
                        }
                    LocalDateTime before = LocalDateTime.now();

                    TrimProp tmp = new TrimProp("C:\\\\Users\\\\Omer\\\\Videos\\\\Call of Duty  Modern Warfare 2019\\\\check(compressed).mp4"
                            ,"mp4" , 0,0,0,0,0,m,s,0 );

                    trimService.trimFile(tmp);
                    LocalDateTime after = LocalDateTime.now();
                    Long diffInMilli = java.time.Duration.between(before, after).toMillis();
                    Long diffInSeconds = java.time.Duration.between(before, after).getSeconds();
                    Long diffInMinutes = java.time.Duration.between(before, after).toMinutes();

                    Map<String, Long> diff = Map.of("diffInMilli",diffInMilli,
                            "diffInSeconds", diffInSeconds,"diffInMinutes",diffInMinutes);
                    String json="";

                    Long secondsLen = TimeUnit.SECONDS.toMillis(tmp.getSecLen());
                    Long minutesLen = TimeUnit.MINUTES.toMillis(tmp.getMinLen());

                    Object times[] = {secondsLen + minutesLen ,diffInMilli};
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    try {
                        json  = ow.writeValueAsString(times);
                        System.out.println(json);

                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    json +="\n";
//                    ObjectWriter ow2 = new ObjectMapper().writer().withDefaultPrettyPrinter();
//                    try {
//                        String json = ow2.writeValueAsString(times);
//                        System.out.println(json);
//                    } catch (JsonProcessingException e) {
//                        e.printStackTrace();
//                    }
                    try {
                        Files.write(Paths.get("C:\\Users\\Omer\\Desktop\\JsonLog.txt"), json.getBytes(), StandardOpenOption.APPEND);
                    }catch (IOException e) {
                        //exception handling left as an exercise for the reader
                    }
                    s= s+10;
                }



            }

            new File("C:\\Users\\Omer\\Videos\\Call of Duty  Modern Warfare 2019\\check(compressed)(Trimmed).mp4").delete();

            Runtime runtime = Runtime.getRuntime();
            try {
                Process proc = runtime.exec("shutdown -s -t 0");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }
}