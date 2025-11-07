package mission1.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Quote {
    private final int id;
    private final String content;
    private final String author;

    @JsonCreator
    public Quote(
            @JsonProperty("id") int id,
            @JsonProperty("content") String content,
            @JsonProperty("author") String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}
