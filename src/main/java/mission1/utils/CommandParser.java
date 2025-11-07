package mission1.utils;

import java.util.HashMap;
import java.util.Map;

public class CommandParser {
    public static Map<String, String> parseQuery(String command) {
        Map<String, String> params = new HashMap<>();
        if(!command.contains("?")) return params;

        String[] parts = command.split("\\?", 2);
        if(parts.length < 2) return params;

        String[] pairs = parts[1].split("&");
        for(String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            params.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
        }
        return params;
    }
}
