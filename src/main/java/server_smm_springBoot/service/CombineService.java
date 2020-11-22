package server_smm_springBoot.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import server_smm_springBoot.dao.ProcDB;
import server_smm_springBoot.model.CombProp;
import server_smm_springBoot.model.CompProps;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;


@Service
public class CombineService implements SmmService {

    public final ProcDB procDB;
    public final  ThreadPoolTaskExecutor executor;

    @Autowired
    public CombineService(@Qualifier("fakeDB") ProcDB procDB, @Qualifier("simpleExe") ThreadPoolTaskExecutor executor) {
        this.procDB = procDB;
        this.executor = executor;
    }

    public List<CompProps> getAll(){

        return procDB.getAll();

    }

    public int getSizeOfDB(){
        return procDB.getSize();
    }



    /**
     * combine a list of video files into one video file<br> </br>
     * if list.soze == 1 --> nothing will happen!!!<br> </br>
     *
     */
    public String combineList(CombProp p) throws InterruptedException, IOException {
        LinkedList<String> list = p.getIns();
        File f = new File("C:\\ffmpeg-20200715-a54b367-win64-static\\bin\\ffmpeg.exe");
        FFmpeg ffmpeg1 = null;
        try {
            ffmpeg1 = new FFmpeg(f.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        f = new File("C:\\ffmpeg-20200715-a54b367-win64-static\\bin\\ffprobe.exe");
        FFprobe ffprobe = null;
        try {
            ffprobe = new FFprobe(f.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fmpeg = ffmpeg1.getPath();
        String path = "C:\\Users\\Omer\\Desktop\\pp\\Tmp";
        LinkedList<File> toRemove = new LinkedList<File>();
        String c = "concat:";
        int t = 0;
        File pp = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH.mm.ss");
        LocalDateTime now = LocalDateTime.now();
        Process processDuration;
//        System.err.println(list.size());
        while (list.size() > 1) {
            String p1 = list.get(0);
            File file1 = new File(p1);
            String out1 = file1.getParent() + "\\" + "temp" + t++ + dtf.format(now) + "." + "ts";
            new File(out1).deleteOnExit();
            toRemove.add(new File(out1));
//            Settings.deleteList.add(new File(out1));
            new File(out1).deleteOnExit();

            processDuration = new ProcessBuilder(fmpeg, "-i", p1, "-c", "copy", "-bsf:v", "h264_mp4toannexb", "-f", "mpegts", out1).redirectErrorStream(true).start();
                processDuration.waitFor();


            String p2 = list.get(1);

            File file2 = new File(p2);
            String out2 = file2.getParent() + "\\" + "temp" + t++ + dtf.format(now) + "." + "ts";
            toRemove.add(new File(out2));
//            Settings.deleteList.add(new File(out2));
            new File(out2).deleteOnExit();
            processDuration = new ProcessBuilder(fmpeg, "-i", p2, "-c", "copy", "-bsf:v", "h264_mp4toannexb", "-f", "mpegts", out2).redirectErrorStream(true).start();
                processDuration.waitFor();

//ts - sved in the same path of the "origignal" file
            // tmp combo is in the dest combo path

            String tmpP = path;
            path += "\\" + "combo" + dtf.format(now) + t++ + ".mp4";
            processDuration = new ProcessBuilder(fmpeg, "-i", c + out1 + "|" + out2, "-c", "copy", "-bsf:a", "aac_adtstoasc", path).redirectErrorStream(true).start();
            try {
                processDuration.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pp = new File(path);
//            Settings.deleteList.add(pp);
            new File(out1).delete();

            list.addFirst(path);

            path = tmpP;

            list.remove(1);
            list.remove(1);
            if (list.size() != 1) {
                pp.deleteOnExit();
                toRemove.add(pp);
//                Settings.deleteList.add(pp);
            } else {

            }
            if (toRemove.size() > 2) {
                toRemove.pop().delete();

            }
        }
        for (File file : toRemove) {
            file.delete();
        }
        for (File file : toRemove) {
            file.delete();
        }

        int j = 1;
        String name = path + "\\" + "comboFinal" + ".mp4";
        while (new File(name).exists()) {
            name = path + "\\" + "comboFinal" + "(" + j + ")" + ".mp4";
            j++;
        }
        if (pp != null) {
            pp.renameTo(new File(name));
            pp = new File(name);
//            Settings.deleteList.remove(pp);
            return pp.getAbsolutePath();

        }
        return null;
    }





    public void Combine(CombProp prop) {

        executor.submit(()-> {
            try {
                combineList(prop);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            // TODO: 19/08/2020   send mail with download + (? prop log )
        });
    }
}
