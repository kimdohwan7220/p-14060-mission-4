package mission1.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuoteTest {

    @DisplayName("Quote 객체 생성 테스트")
    @Test
    void Quote_객체_생성_테스트() {
        int id = 1;
        String content = "현재를 사랑하라.";
        String author = "작자미상";

        Quote quote = new Quote(id, content, author);

        assertThat(quote).isNotNull();
        assertThat(quote.getId()).isEqualTo(id);
        assertThat(quote.getContent()).isEqualTo(content);
        assertThat(quote.getAuthor()).isEqualTo(author);
    }
}