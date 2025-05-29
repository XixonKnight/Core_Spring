package com.example.corespring.utils;

import lombok.experimental.UtilityClass;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.regex.Pattern;

@UtilityClass
public class VietnameseTextUtils {
    private final char[] SOURCE_CHARACTERS = new char[]{'à', 'á', 'ạ', 'ả', 'ã', 'â', 'ầ', 'ấ', 'ậ', 'ẩ', 'ẫ',
            'ă', 'ằ', 'ắ', 'ặ', 'ẳ', 'ẵ', 'è', 'é', 'ẹ', 'ẻ', 'ẽ', 'ê', 'ề', 'ế', 'ệ', 'ể', 'ễ', 'ì', 'í', 'ị', 'ỉ',
            'ĩ', 'ò', 'ó', 'ọ', 'ỏ', 'õ', 'ô', 'ồ', 'ố', 'ộ', 'ổ', 'ỗ', 'ơ', 'ờ', 'ớ', 'ợ', 'ở', 'ỡ', 'ù', 'ú', 'ụ',
            'ủ', 'ũ', 'ư', 'ừ', 'ứ', 'ự', 'ử', 'ữ', 'ỳ', 'ý', 'ỵ', 'ỷ', 'ỹ', 'đ', 'À', 'Á', 'Ạ', 'Ả', 'Ã', 'Â', 'Ầ',
            'Ấ', 'Ậ', 'Ẩ', 'Ẫ', 'Ă', 'Ằ', 'Ắ', 'Ặ', 'Ẳ', 'Ẵ', 'È', 'É', 'Ẹ', 'Ẻ', 'Ẽ', 'Ê', 'Ề', 'Ế', 'Ệ', 'Ể', 'Ễ',
            'Ì', 'Í', 'Ị', 'Ỉ', 'Ĩ', 'Ò', 'Ó', 'Ọ', 'Ỏ', 'Õ', 'Ô', 'Ồ', 'Ố', 'Ộ', 'Ổ', 'Ỗ', 'Ơ', 'Ờ', 'Ớ', 'Ợ', 'Ở',
            'Ỡ', 'Ù', 'Ú', 'Ụ', 'Ủ', 'Ũ', 'Ư', 'Ừ', 'Ứ', 'Ự', 'Ử', 'Ữ', 'Ỳ', 'Ý', 'Ỵ', 'Ỷ', 'Ỹ', 'Đ'};
    private final char[] DESTINATION_CHARACTERS = new char[]{'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
            'a', 'a', 'a', 'a', 'a', 'a', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'i', 'i', 'i', 'i',
            'i', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'u', 'u', 'u',
            'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'y', 'y', 'y', 'y', 'y', 'd', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
            'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E',
            'I', 'I', 'I', 'I', 'I', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O',
            'O', 'U', 'U', 'U', 'U', 'U', 'U', 'U', 'U', 'U', 'U', 'U', 'Y', 'Y', 'Y', 'Y', 'Y', 'D'};

    public static String removeAccentsAndSpecialChars(String s) {
        // Bước 1: Loại bỏ dấu tiếng Việt
        String data = removeAccent(s, true);

        // Bước 2: Loại bỏ các ký tự đặc biệt, giữ lại chữ cái, chữ số và khoảng trắng
        String alphanumeric = data.replaceAll("[^a-zA-Z0-9\\s]", "");

        // Bước 3: Chuẩn hóa khoảng trắng (loại bỏ khoảng trắng thừa và thay thế bằng một khoảng trắng duy nhất)
        return alphanumeric.trim().replaceAll("\\s+", "").replace(" ", "");
    }

    public static String removeAccent(String s) {
        return removeAccent(s, true);
    }

    public static String removeAccent(String s, boolean replaceSpecialCharacter) {
        s = preRemoveAccent(s, replaceSpecialCharacter);
        StringBuilder sb = new StringBuilder(s);

        for (int i = 0; i < sb.length(); ++i) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }

        return sb.toString();
    }

    public static char removeAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }

        return ch;
    }

    public static String preRemoveAccent(String s, boolean replaceSpecialCharacter) {
        if (s == null) {
            s = "";
        }

        s = s.trim();
        if (replaceSpecialCharacter) {
            s = s.replace("&", "VA");
        }
        // Bước 1: Loại bỏ ký tự đặc biệt (chỉ giữ chữ cái, số và khoảng trắng)
        s = s.replaceAll("[^\\p{L}\\p{N}]", " "); // \p{L}: chữ, \p{N}: số
        s = s.replace(" ", " ");
        s = s.replaceAll("\\s+", " ").trim().toUpperCase();
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}
