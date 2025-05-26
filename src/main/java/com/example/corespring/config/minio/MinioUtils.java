package com.example.corespring.config.minio;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MinioUtils {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    // Upload Files
    @SneakyThrows
    public void putObject(String bucketName, MultipartFile multipartFile, String filename, String fileType) {

        log.info("MinioUtil | putObject is called");

        log.info("MinioUtil | putObject | filename : {}", filename);
        log.info("MinioUtil | putObject | fileType : {}", fileType);

        InputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes());

        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(filename).stream(
                                inputStream, -1, minioConfig.getFileSize())
                        .contentType(fileType)
                        .build());
    }

    // Check if bucket name exists
    @SneakyThrows
    public boolean bucketExists(String bucketName) {

        log.info("MinioUtil | bucketExists is called");

        boolean found =
                minioClient.bucketExists(
                        BucketExistsArgs.builder().
                                bucket(bucketName).
                                build());

        log.info("MinioUtil | bucketExists | found : {}", found);

        if (found) {
            log.info("MinioUtil | bucketExists | message : {} exists", bucketName);
        } else {
            log.info("MinioUtil | bucketExists | message : {} does not exist", bucketName);
        }
        return found;
    }

    // Create bucket name
    @SneakyThrows
    public boolean makeBucket(String bucketName) {

        log.info("MinioUtil | makeBucket is called");

        boolean flag = bucketExists(bucketName);

        log.info("MinioUtil | makeBucket | flag : " + flag);

        if (!flag) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build());

            return true;
        } else {
            return false;
        }
    }

    // List all buckets
    @SneakyThrows
    public List<Bucket> listBuckets() {
        log.info("MinioUtil | listBuckets is called");

        return minioClient.listBuckets();
    }

    // List all bucket names
    @SneakyThrows
    public List<String> listBucketNames() {

        log.info("MinioUtil | listBucketNames is called");

        List<Bucket> bucketList = listBuckets();

        log.info("MinioUtil | listBucketNames | bucketList size : {}", bucketList.size());

        List<String> bucketListName = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }

        log.info("MinioUtil | listBucketNames | bucketListName size : {}", bucketListName.size());

        return bucketListName;
    }

    // List all objects from the specified bucket
    @SneakyThrows
    public Iterable<Result<Item>> listObjects(String bucketName) {

        log.info("MinioUtil | listObjects is called");

        boolean flag = bucketExists(bucketName);

        log.info("MinioUtil | listObjects | flag : {}", flag);

        if (flag) {
            return minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).build());
        }
        return null;
    }

    // Delete Bucket by its name from the specified bucket
    @SneakyThrows
    public boolean removeBucket(String bucketName) {

        log.info("MinioUtil | removeBucket is called");

        boolean flag = bucketExists(bucketName);
        log.info("MinioUtil | removeBucket | flag : {}", flag);

        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);

            for (Result<Item> result : myObjects) {
                Item item = result.get();
                //  Delete failed when There are object files in bucket

                log.info("MinioUtil | removeBucket | item size : {}", item.size());

                if (item.size() > 0) {
                    return false;
                }
            }

            //  Delete bucket when bucket is empty
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            flag = bucketExists(bucketName);

            log.info("MinioUtil | removeBucket | flag : {}", flag);
            if (!flag) {
                return true;
            }
        }
        return false;
    }

    // List all object names from the specified bucket
    @SneakyThrows
    public List<String> listObjectNames(String bucketName) {

        log.info("MinioUtil | listObjectNames is called");

        List<String> listObjectNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);

        log.info("MinioUtil | listObjectNames | flag : {}", flag);

        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        } else {
            listObjectNames.add(" Bucket does not exist ");
        }

        log.info("MinioUtil | listObjectNames | listObjectNames size : " + listObjectNames.size());

        return listObjectNames;
    }

    // Delete object from the specified bucket
    @SneakyThrows
    public boolean removeObject(String bucketName, String objectName) {

        log.info("MinioUtil | removeObject is called");

        boolean flag = bucketExists(bucketName);

        log.info("MinioUtil | removeObject | flag : {}", flag);

        if (flag) {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        }
        return false;
    }

    // Get file path from the specified bucket
    @SneakyThrows
    public String getObjectUrl(String bucketName, String objectName) {

        log.info("MinioUtil | getObjectUrl is called");
        boolean flag = bucketExists(bucketName);
        log.info("MinioUtil | getObjectUrl | flag : {}", flag);

        String url = "";

        if (flag) {
            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
//                            .expiry(2, TimeUnit.MINUTES)
                            .build());
            log.info("MinioUtil | getObjectUrl | url : {}", url);
        }
        return url;
    }

    // Get metadata of the object from the specified bucket
    @SneakyThrows
    public StatObjectResponse statObject(String bucketName, String objectName) {
        log.info("MinioUtil | statObject is called");

        boolean flag = bucketExists(bucketName);
        log.info("MinioUtil | statObject | flag : {}", flag);
        if (flag) {
            StatObjectResponse stat =
                    minioClient.statObject(
                            StatObjectArgs.builder().bucket(bucketName).object(objectName).build());

            log.info("MinioUtil | statObject | stat : {}", stat.toString());

            return stat;
        }
        return null;
    }

    // Get a file object as a stream from the specified bucket
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName) {
        log.info("MinioUtil | getObject is called");

        boolean flag = bucketExists(bucketName);
        log.info("MinioUtil | getObject | flag : {}", flag);

        if (flag) {
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                InputStream stream =
                        minioClient.getObject(
                                GetObjectArgs.builder()
                                        .bucket(bucketName)
                                        .object(objectName)
                                        .build());

                log.info("MinioUtil | getObject | stream : {}", stream.toString());
                return stream;
            }
        }
        return null;
    }

    // Get a file object as a stream from the specified bucket （ Breakpoint download )
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName, long offset, Long length) {

        log.info("MinioUtil | getObject is called");

        boolean flag = bucketExists(bucketName);
        log.info("MinioUtil | getObject | flag : {}", flag);

        if (flag) {
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                InputStream stream =
                        minioClient.getObject(
                                GetObjectArgs.builder()
                                        .bucket(bucketName)
                                        .object(objectName)
                                        .offset(offset)
                                        .length(length)
                                        .build());

                log.info("MinioUtil | getObject | stream : {}", stream.toString());
                return stream;
            }
        }
        return null;
    }

    // Delete multiple file objects from the specified bucket
    @SneakyThrows
    public boolean removeObject(String bucketName, List<String> objectNames) {
        log.info("MinioUtil | removeObject is called");

        boolean flag = bucketExists(bucketName);
        log.info("MinioUtil | removeObject | flag : {}", flag);

        if (flag) {
            List<DeleteObject> objects = new LinkedList<>();
            for (int i = 0; i < objectNames.size(); i++) {
                objects.add(new DeleteObject(objectNames.get(i)));
            }
            Iterable<Result<DeleteError>> results =
                    minioClient.removeObjects(
                            RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());

            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();

                log.info("MinioUtil | removeObject | error : {} {}", error.objectName(), error.message());

                return false;
            }
        }
        return true;
    }

    // Upload InputStream object to the specified bucket
    @SneakyThrows
    public boolean putObject(String bucketName, String objectName, InputStream inputStream, String contentType) {

        log.info("MinioUtil | putObject is called");

        boolean flag = bucketExists(bucketName);
        log.info("MinioUtil | putObject | flag : {}", flag);

        if (flag) {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                                    inputStream, -1, minioConfig.getFileSize())
                            .contentType(contentType)
                            .build());
            StatObjectResponse statObject = statObject(bucketName, objectName);

            log.info("MinioUtil | putObject | statObject != null : " + (statObject != null));
            log.info("MinioUtil | putObject | statObject.size() : " + statObject.size());

            if (statObject != null && statObject.size() > 0) {
                return true;
            }
        }
        return false;
    }
}