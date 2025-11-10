package mission1.utils;

import java.util.Map;

public class InputValidator {

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static void requireNotBlank(String value, String fieldName) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(fieldName + "을(를) 입력해주세요.");
        }
    }

    public static int parseIntOrThrow(String text, String fieldName) {
        try {
            requireNotBlank(text, fieldName);
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + "은(는) 숫자여야 합니다.");
        }
    }

    public static String normalizeKeywordType(String keywordType) {
        if (isBlank(keywordType)) {
            throw new IllegalArgumentException("검색 타입(keywordType)을 입력해주세요. (content 또는 author)");
        }

        String t = keywordType.trim().toLowerCase();

        if ("content".equals(t) || "author".equals(t)) {
            return t;
        }

        throw new IllegalArgumentException("검색 타입(keywordType)은 content 또는 author만 가능합니다.");
    }

    public static String validateListSearchParams(Map<String, String> params) {
        String type = normalizeKeywordType(params.get("keywordType"));
        String keyword = params.get("keyword");

        if (isBlank(keyword)) {
            throw new IllegalArgumentException("검색어(keyword)를 입력해주세요.");
        }

        return type;
    }

    public static void validatePageRange(int page, int totalPages) {
        if (page < 1) {
            throw new IllegalArgumentException("page는 1 이상이어야 합니다.");
        }
        if (page > totalPages) {
            throw new IllegalArgumentException("페이지는 1 이상 " + totalPages + " 이하만 가능합니다.");
        }
    }

    public static int parsePageOrDefault(Map<String, String> params, int defaultPage) {
        String pageText = params.get("page");
        if (pageText == null || pageText.isBlank()) {
            return defaultPage;
        }

        return parseIntOrThrow(pageText, "page");
    }
}
