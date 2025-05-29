package com.example.corespring.customs;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomMultipartFile implements MultipartFile {
    byte[] fileContent;
    String fileName;
    String contentType;

    public CustomMultipartFile(File file, String contentType) throws IOException {
        this.fileName = file.getName();
        this.contentType = contentType;
        this.fileContent = readFileToByteArray(file);
    }

    private byte[] readFileToByteArray(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return inputStream.readAllBytes();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "file";
    }

    @Override
    public String getOriginalFilename() {
        return this.fileName != null ? this.fileName : "file";
    }

    @Override
    public String getContentType() {
        return this.contentType != null ? this.contentType : "application/octet-stream";
    }

    @Override
    public boolean isEmpty() {
        return this.fileContent == null || this.fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return this.fileContent != null ? this.fileContent.length : 0;
    }

    @NotNull
    @Override
    public byte[] getBytes(){
        return this.fileContent != null ? this.fileContent : new byte[0];
    }

    @NotNull
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.fileContent);
    }

    @Override
    public void transferTo(@NotNull File dest) throws IOException, IllegalStateException {
        try (OutputStream out = new FileOutputStream(dest)) {
            out.write(fileContent);
        }
    }
}
