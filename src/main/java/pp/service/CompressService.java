package pp.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import pp.bucket.BucketName;
import pp.dao.ProcDB;
import pp.filestore.FileStore;
import pp.model.CompProps;
import pp.model.UserSMM;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


@Service
public class CompressService implements SmmService {

    public final ProcDB procDB;
    public final ThreadPoolTaskExecutor executor;
    public final FileStore fileStore;
    public final UserService userService;
    public final SendMail sendMail;
    public FFmpeg ffmpeg;
    public FFprobe ffprobe;

    @Autowired
    public CompressService(@Qualifier("fakeDB") ProcDB procDB, @Qualifier("simpleExe") ThreadPoolTaskExecutor executor,
                           FileStore fileStore, UserService userService,
                           SendMail sendMail) {
        this.sendMail = sendMail;
        this.procDB = procDB;
        this.executor = executor;
        this.fileStore = fileStore;
        this.userService = userService;
        constructFFMPEG();
    }

    private void constructFFMPEG() {
        String wDir = System.getProperty("user.dir");
        File f = new File(wDir + "\\ffmpeg.exe");
        try {
            ffmpeg = new FFmpeg(f.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        f = new File(wDir + "\\ffprobe.exe");
        try {
            ffprobe = new FFprobe(f.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ffmpeg == null || ffprobe == null) {
            throw new IllegalStateException("ffmpeg or ffprobe is null");
        }
    }


    public List<CompProps> getAll() {

        return procDB.getAll();

    }

    public int getSizeOfDB() {
        return procDB.getSize();
    }


    public File compressFile(CompProps prop, String filePath) {

        File file = new File(filePath);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd - HH.mm.ss");
        LocalDateTime now = LocalDateTime.now();
//        System.out.println("start at " + dtf.format(now));
        String name = file.getName().substring(0, file.getName().lastIndexOf("."));
        String out = file.getParent();
        out = out + "\\" + name + "(compressed)" + dtf.format(now) + "." + prop.getFormat();
        //if the file got
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(filePath) // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(out) // Filename for the destination
                .setFormat(prop.getFormat()) // Format is inferred from filename, or can be set
                .setAudioChannels(prop.getAudioChannels())
                .setAudioCodec(prop.getAudioCodec()) // using the aac codec
                .setAudioSampleRate(prop.getAudioSampleRate()) // at 48KHz
                .setAudioBitRate(prop.getAudioBitRate()) // at 64 kbit/s
                .setVideoCodec(prop.getVideoCodec()) // Video using x264
                .setVideoFrameRate(prop.getFrames(), prop.getPer()) // at 24 frames per second
                .setVideoResolution(prop.getResolution()[0], prop.getResolution()[1]) // at 640x480 resolution
                .setVideoBitRate(prop.getVideoBitRate())
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run(); // Run a one-pass encode    }
        return new File(out);
    }


    public void Compress(CompProps prop, UUID id) {

        executor.submit(() -> {
            //download
            File downloadedFile = download(id);
            File compressedFile = compressFile(prop, downloadedFile.getAbsolutePath());

            //upload file
            //path at bucket
            String pathAndName = String.format("%s/%s", id, compressedFile.getName());
            //upload
            fileStore.UploadFile(BucketName.PROFILE_VIDEO.getBucketName(), pathAndName, compressedFile);
            fileStore.RenameFile(BucketName.PROFILE_VIDEO.getBucketName() ,
                    String.format("%s/%s", id, downloadedFile.getName())
                    ,String.format("%s/%s", id, userService.getUser(id).getCompFile())
                    );

            downloadedFile.delete();
            compressedFile.delete();
            //generate download link
            String downloadLink = fileStore.getDownloadLink(BucketName.PROFILE_VIDEO.getBucketName(), pathAndName);

            sendMail.SendFromGmail("toledoomer@gmail.com", "OmerNLP123!", downloadLink);
        });
    }

    public File download(UUID uuid) {

        UserSMM user = procDB.getUserByUUID(uuid);
        if (user == null) {
            throw new IllegalStateException("user doesnt exist");
        }
        String path = String.format("%s/%s",
                uuid,
                user.getCompFile());

        return fileStore.download(BucketName.PROFILE_VIDEO.getBucketName(), path);

    }

}
