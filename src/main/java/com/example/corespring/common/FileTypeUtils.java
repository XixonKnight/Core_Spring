package com.example.corespring.common;

import cn.hutool.core.io.FileTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class FileTypeUtils {

    private final static String IMAGE_TYPE = "image/";
    private final static String AUDIO_TYPE = "audio/";
    private final static String VIDEO_TYPE = "video/";
    private final static String APPLICATION_TYPE = "application/";
    private final static String TXT_TYPE = "text/";

    public static String getFileType(MultipartFile multipartFile) {

        InputStream inputStream = null;
        String type = null;

        try {
            inputStream = multipartFile.getInputStream();
            type = FileTypeUtil.getType(inputStream);
            log.info("FileTypeUtils | getFileType | type : {}", type);

            if (type.equalsIgnoreCase("JPG") || type.equalsIgnoreCase("JPEG")
                    || type.equalsIgnoreCase("GIF") || type.equalsIgnoreCase("PNG")
                    || type.equalsIgnoreCase("BMP") || type.equalsIgnoreCase("PCX")
                    || type.equalsIgnoreCase("TGA") || type.equalsIgnoreCase("PSD")
                    || type.equalsIgnoreCase("TIFF")) {

                log.info("FileTypeUtils | getFileType | IMAGE_TYPE+type : " + IMAGE_TYPE+type);
                return IMAGE_TYPE+type;
            }

            if (type.equalsIgnoreCase("mp3") || type.equalsIgnoreCase("OGG")
                    || type.equalsIgnoreCase("WAV") || type.equalsIgnoreCase("REAL")
                    || type.equalsIgnoreCase("APE") || type.equalsIgnoreCase("MODULE")
                    || type.equalsIgnoreCase("MIDI") || type.equalsIgnoreCase("VQF")
                    || type.equalsIgnoreCase("CD")) {

                log.info("FileTypeUtils | getFileType | AUDIO_TYPE+type : " + AUDIO_TYPE + "{}", type);
                return AUDIO_TYPE+type;
            }
            if (type.equalsIgnoreCase("mp4") || type.equalsIgnoreCase("avi")
                    || type.equalsIgnoreCase("MPEG-1") || type.equalsIgnoreCase("RM")
                    || type.equalsIgnoreCase("ASF") || type.equalsIgnoreCase("WMV")
                    || type.equalsIgnoreCase("qlv") || type.equalsIgnoreCase("MPEG-2")
                    || type.equalsIgnoreCase("MPEG4") || type.equalsIgnoreCase("mov")
                    || type.equalsIgnoreCase("3gp")) {

                log.info("FileTypeUtils | getFileType | VIDEO_TYPE+type : " + VIDEO_TYPE+type);
                return VIDEO_TYPE+type;
            }
            if (type.equalsIgnoreCase("doc") || type.equalsIgnoreCase("docx")
                    || type.equalsIgnoreCase("ppt") || type.equalsIgnoreCase("pptx")
                    || type.equalsIgnoreCase("xls") || type.equalsIgnoreCase("xlsx")
                    || type.equalsIgnoreCase("zip")||type.equalsIgnoreCase("jar")) {

                log.info("FileTypeUtils | getFileType | APPLICATION_TYPE+type : " + APPLICATION_TYPE + "{}", type);
                return APPLICATION_TYPE+type;
            }
            if (type.equalsIgnoreCase("txt")) {

                log.info("FileTypeUtils | getFileType | TXT_TYPE+type : " + TXT_TYPE + "{}", type);
                return TXT_TYPE+type;
            }

        }catch (IOException e){
            log.info("FileTypeUtils | getFileType | IOException : {}", e.getMessage());
        }

        return null;
    }
}
