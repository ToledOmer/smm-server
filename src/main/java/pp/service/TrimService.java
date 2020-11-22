package pp.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import pp.dao.ProcDB;
import pp.model.CompProps;
import pp.model.TrimProp;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class TrimService implements SmmService {

    public final ProcDB procDB;
    public final ThreadPoolTaskExecutor executor;

    @Autowired
    public TrimService(@Qualifier("fakeDB") ProcDB procDB, @Qualifier("simpleExe") ThreadPoolTaskExecutor executor) {
        this.procDB = procDB;
        this.executor = executor;
    }


    public TrimService() {
        this.procDB = null;
        this.executor = null;
    }

    public List<CompProps> getAll(){

        return procDB.getAll();

    }

    public int getSizeOfDB(){
        return procDB.getSize();
    }

    public void trimFile(TrimProp tprop) {

        FFmpeg ffmpeg = null;

        try {
            ffmpeg = new FFmpeg("C:\\ffmpeg-20200715-a54b367-win64-static\\bin\\ffmpeg.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
        FFprobe ffprobe = null;
        try {
            ffprobe = new FFprobe("C:\\ffmpeg-20200715-a54b367-win64-static\\bin\\ffprobe.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Long secondsOffset = TimeUnit.SECONDS.toMillis(tprop.getSec());
        Long minutesOffset = TimeUnit.MINUTES.toMillis(tprop.getMin());
        Long hoursOffset = TimeUnit.HOURS.toMillis   (tprop.getHours());
        Long secondsLen = TimeUnit.SECONDS.toMillis(tprop.getSecLen());
        Long minutesLen = TimeUnit.MINUTES.toMillis(tprop.getMinLen());
        Long hoursLen = TimeUnit.HOURS.toMillis    (tprop.getHoursLen());

        File file = new File(tprop.getIn());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String out = file.getParent();
        String name = file.getName().substring(0, file.getName().indexOf("."));
        out = out + "\\" + name + "(Trimmed)"
                + "." + tprop.getFormat();
        FFmpegBuilder builder;
        String in =tprop.getIn();
        builder = new FFmpegBuilder()
                .setInput(in) // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(out) // Filename for the destination
                .setStartOffset(hoursOffset + minutesOffset + secondsOffset + tprop.getMs(), TimeUnit.MILLISECONDS)
                .setDuration(hoursLen + minutesLen + secondsLen + tprop.getMsLen() , TimeUnit.MILLISECONDS)
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run(); // Run a one-pass encode    }
        now = LocalDateTime.now();
        System.out.println("file " + file.getName() + " trimmed!, time:" + dtf.format(now));

    }


    public void Trim(TrimProp tprop) {

        executor.submit(()-> {
            trimFile(tprop);
            // TODO: 19/08/2020   send mail with download + (? prop log )
        });
    }





    }
