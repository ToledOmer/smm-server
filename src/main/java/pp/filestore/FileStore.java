package pp.filestore;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AbstractAmazonS3;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pp.bucket.BucketName;

import java.io.InputStream;
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

    /**
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

    public String download(String bucketName, String path) {
        URL url;
        try {
            // Set the presigned URL to expire after one hour.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
                            //1 hours * 24
            expTimeMillis += 1000 * 60 * 60*24;
            expiration.setTime(expTimeMillis);
            // Generate the presigned URL.
            String newPath = path.substring(0, path.indexOf("UUID"));

            RenameFile(bucketName, path, newPath);


            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, newPath)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            url = s3.generatePresignedUrl(generatePresignedUrlRequest);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to download file to s3", e);
        }
        return url.toString();
    }

    private void RenameFile(String bucketName, String oldPath, String newPath) {
        CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName,
                oldPath, bucketName, newPath);
        s3.copyObject(copyObjRequest);

        s3.deleteObject(new DeleteObjectRequest(bucketName, oldPath));
    }
}
