package pp.service;

import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pp.bucket.BucketName;
import pp.dao.ProcDB;
import pp.filestore.FileStore;
import pp.model.UserSMM;

import java.io.IOException;
import java.util.*;

@Service
public class UserService {
    private final ProcDB procDB;
    private final FileStore fileStore;

    @Autowired
    public UserService(ProcDB procDB, FileStore fileStore) {
        this.procDB = procDB;
        this.fileStore = fileStore;
    }


    public void addUser(UserSMM userSMM) {
        procDB.insertUser(userSMM);
    }

    public UserSMM getUser(UserSMM userSMM) {
        return procDB.getUser(userSMM);
    }

    public UserSMM getUser(String userEmail) {
        return procDB.getUserByEmail(userEmail);
    }
    public UserSMM getUser(UUID id) {
        return procDB.getUserByUUID(id);
    }


    public boolean EmailExist(String email) {
        return procDB.EmailExist(email);
    }

    public List<UserSMM> getAllUsers() {
        return procDB.getAllUsers();
    }

    public void UploadVideo(UUID uuid, MultipartFile file) {

        //1, Check if video is not empty
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
//        String check = file.getContentType();
//        System.out.println("file.getContentType() is " + check);
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
//        System.out.println("file.getContentType() is " + extension);


        //2. if file is an video
        IsVideoSupproted(extension);

        //3.the user exist in our database
        UserSMM user = procDB.getUserByUUID(uuid);
        if (user == null) {
            throw new IllegalStateException(String.format("user %s hasnt found", uuid));
        }

        //4.Grab some metadata from file if any


        Map<String, String> metadata = extractMetaData(file);

        //5. store the video in s3 and update database (userProfileVideoLink) with s3 video link
        String path = String.format("%s/%s", BucketName.PROFILE_VIDEO.getBucketName(), user.getId());
        String fileName = String.format("%sUUID-%s", file.getOriginalFilename(), UUID.randomUUID());
        user.setCompFile(fileName);

        try {
            fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    @NotNull
    private Map<String, String> extractMetaData(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private void IsVideoSupproted(String extension) {
        if (!Arrays.asList("mp4", "flv", "avi").contains(extension)) {
            throw new IllegalStateException("file type not supproted");

        }
    }

    public void ConnectUser(String email, String password) {
        UserSMM user = procDB.getUserByEmail(email);
        if(user == null){
            throw new IllegalStateException("user doesnt exist");

        }
        if(!user.getPassword().equals(password)){
            throw new IllegalStateException("Wrong password");

        }
        else
            user.setIsConncted(1);
    }

    public void DisconnectUser(UUID uuid) {
        UserSMM user = procDB.getUserByUUID(uuid);
        if(user == null){
            throw new IllegalStateException("user doesnt exist");
        }
            user.setIsConncted(0);
    }

    public String getDownloadLink(UUID uuid) {
        UserSMM user = procDB.getUserByUUID(uuid);
        if(user == null){
            throw new IllegalStateException("user doesnt exist");
        }
        String path = String.format("%s/%s",
                 uuid,
                user.getCompFile());

        return fileStore.getDownloadLink(BucketName.PROFILE_VIDEO.getBucketName(), path);
    }


}
