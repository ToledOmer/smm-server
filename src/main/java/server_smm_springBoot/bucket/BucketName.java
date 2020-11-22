package server_smm_springBoot.bucket;

public enum BucketName {
    PROFILE_VIDEO("simple-montage-maker") ;
    public final String BuckeName;

    BucketName(String buckeName) {
        BuckeName = buckeName;
    }

    public String getBucketName() {
        return BuckeName;
    }
}
