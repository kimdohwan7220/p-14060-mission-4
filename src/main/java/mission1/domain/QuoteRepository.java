package mission1.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.file.*;

public class QuoteRepository {
    private static final String DATA_JSON = "data.json";
    private static final String LAST_ID_FILE = "lastId.txt";

    private final Path baseDir;
    private final Path dataJson;
    private final Path lastIdFile;

    private final Map<Integer, Quote> store = new LinkedHashMap<>();
    private int nextId = 1;

    private final ObjectMapper mapper
            = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public QuoteRepository() {
        this(Paths.get("db", "wiseSaying"));
    }

    public QuoteRepository(Path baseDir) {
        this.baseDir = baseDir;
        this.dataJson = baseDir.resolve(DATA_JSON);
        this.lastIdFile = baseDir.resolve(LAST_ID_FILE);
        init();
    }

    private void init() {
        try {
            if (!Files.exists(baseDir)) Files.createDirectories(baseDir);

            if (Files.exists(lastIdFile)) {
                String lastIdText = Files.readString(lastIdFile, StandardCharsets.UTF_8).trim();
                if (!lastIdText.isEmpty()) nextId = Integer.parseInt(lastIdText) + 1;
            }

            try (var files = Files.list(baseDir)) {
                for (Path jsonFile : files
                        .filter(p -> p.getFileName().toString().matches("\\d+\\.json"))
                        .toList()) {
                    Quote quote = mapper.readValue(jsonFile.toFile(), Quote.class);
                    store.put(quote.getId(), quote);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("저장소 초기화 실패", e);
        }
    }

    public Quote save(String content, String author) {
        int id = getNextId();
        Quote quote = new Quote(id, content, author);
        store.put(id, quote);
        writeQuote(quote);
        writeLastId(id);
        return quote;
    }

    private Path jsonPath(int id) {
        return baseDir.resolve(id + ".json");
    }

    private void writeQuote(Quote quote) {
        try {
            mapper.writeValue(jsonPath(quote.getId()).toFile(), quote);
        } catch (IOException e) {
            throw new RuntimeException("명언 저장 실패", e);
        }
    }

    private void writeLastId(int lastId) {
        try {
            Files.writeString(
                    lastIdFile,
                    String.valueOf(lastId),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException ignored) {}
    }

    private int getNextId() {
        return nextId++;
    }

    public List<Quote> findAll() {
        List<Quote> list = new ArrayList<>(store.values());
        list.sort(Comparator.comparingInt(Quote::getId).reversed());
        return list;
    }

    public Quote findById(int id) {
        return store.get(id);
    }

    public boolean deleteById(int id) {
        Quote removed = store.remove(id);
        try {
            Files.deleteIfExists(jsonPath(id));
        } catch (IOException ignored) {}
        return removed != null;
    }

    public void update(int id, String content, String author) {
        if (!store.containsKey(id)) return;
        Quote updated = new Quote(id, content, author);
        store.put(id, updated);
        writeQuote(updated);
    }

    public List<Quote> search(String keywordType, String keyword) {
        if (keyword == null || keyword.isBlank()) return findAll();

        String normalizedKeyword = keyword.toLowerCase();

        return store.values().stream()
                .sorted(Comparator.comparingInt(Quote::getId).reversed())
                .filter(q -> {
                    if ("content".equals(keywordType)) {
                        return q.getContent() != null && q.getContent().toLowerCase().contains(normalizedKeyword);
                    } else if ("author".equals(keywordType)) {
                        return q.getAuthor() != null && q.getAuthor().toLowerCase().contains(normalizedKeyword);
                    }
                    return false;
                })
                .toList();
    }

    public void buildDataJson() {
        try {
            List<Quote> list = new ArrayList<>(store.values());
            list.sort(Comparator.comparingInt(Quote::getId)); // 1 → N 오름차순

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(dataJson.toFile(), list);

        } catch (IOException e) {
            throw new RuntimeException("data.json 생성/갱신 실패", e);
        }
    }
}
