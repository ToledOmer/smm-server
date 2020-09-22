package pp.filestore;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AbstractAmazonS3;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pp.bucket.BucketName;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {

    private final AmazonS3 s3;

    @Autowired
    public FileStore(AmazonS3 s3) {
        this.s3 = s3;
    }


    public void UploadFile(String bucketName, String path, File fileToUpload) {

        s3.putObject(
                bucketName,
                path,
                fileToUpload
        );

    }

    /**
     * saving to s3
     *
     * @param path
     * @param fileName
     * @param optionalMetadata
     * @param inputStream
     */
    public void save(String path,
                     String fileName,
                     Optional<Map<String, String>> optionalMetadata,
                     InputStream inputStream) {
        ObjectMetadata metadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map -> {
            if (!map.isEmpty()) {
//                map.forEach((key,val)-> metadata.addUserMetadata(key,val));
                map.forEach(metadata::addUserMetadata);
            }

        });
        try {
            s3.putObject(path, fileName, inputStream, metadata);

        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to store file to s3", e);

        }


    }

    public String getDownloadLink(String bucketName, String path) {
        URL url;
        String newPath = path;
        try {
            // Set the presigned URL to expire after one hour.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            //1 hours * 24
            expTimeMillis += 1000 * 60 * 60 * 24;
            expiration.setTime(expTimeMillis);
            // Generate the presigned URL.

            if (path.contains("UUID")) {
                newPath = path.substring(0, path.indexOf("UUID"));

                RenameFile(bucketName, path, newPath);
            }

            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, newPath)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            url = s3.generatePresignedUrl(generatePresignedUrlRequest);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to get  download link to s3", e);
        }

        //return download link
        return url.toString();
    }


    public File download(String bucketName, String path) {
        //get the name of the file
        String name = path.split("/")[1].substring(0, path.indexOf("UUID"));
        // removing the uuid
        if (name.contains("UUID")) {
            name = name.substring(0, name.indexOf("UUID"));
        }
        //get the user uuid
        String userUUID = path.split("/")[0];
        //path of the new file                                        //users dir must be initialized
        String abPath = String.format(System.getProperty("user.dir") + "\\users" + "\\%s\\%s", userUUID, name);
        //create the dir - userUUID
        File f = new File(abPath.substring(0, abPath.lastIndexOf("\\")));
        f.mkdir();
        String url = getDownloadLink(bucketName, path);
//        File f = null;
        try {
            System.out.println(abPath);

            //create file himself
            int j = 1;
            String extension = name.substring(name.indexOf(".")); //including the dot
            String noExstension = name.substring(0, name.indexOf("."));

            while (new File(abPath).exists()) {
                abPath = String.format(System.getProperty("user.dir") + "\\users" + "\\%s\\%s", userUUID,
                        noExstension + "(" + j + ")" + "." + extension);
                System.out.println(abPath);

                j++;
            }
            new File(abPath).createNewFile();


        } catch (IOException e) {
            e.printStackTrace();
        }
        //get the url input stream

        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOS = new FileOutputStream(abPath)) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                //writing to the file
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            // handles IO exceptions
            throw new IllegalStateException("Failed to  download from:   " + url + "\n to: " + abPath, e);

        }

        return new File(abPath);
    }


    public void RenameFile(String bucketName, String oldPath, String newPath) {
        CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName,
                oldPath, bucketName, newPath);
        s3.copyObject(copyObjRequest);

        s3.deleteObject(new DeleteObjectRequest(bucketName, oldPath));
    }
}
