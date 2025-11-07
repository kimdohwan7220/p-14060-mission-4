package mission1.service;

import static org.assertj.core.api.Assertions.*;

import mission1.domain.Quote;
import mission1.domain.QuoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

class QuoteServiceTest {

    @TempDir Path tempDir;

    @Test
    @DisplayName("등록,조회,수정,삭제 흐름이 정상 동작한다")
    void service_flow_ok() {
        QuoteService service = new QuoteService(new QuoteRepository(tempDir));

        Quote q1 = service.registerQuote("content1", "author1");
        Quote q2 = service.registerQuote("content2", "author2");

        List<Quote> all = service.findAllQuotes();
        assertThat(all).extracting(Quote::getId).containsExactly(q2.getId(), q1.getId());

        service.updateQuote(q1.getId(), "updated content1", "updated author1");
        Quote updated = service.findQuoteById(q1.getId());
        assertThat(updated.getContent()).isEqualTo("updated content1");
        assertThat(updated.getAuthor()).isEqualTo("updated author1");

        service.deleteQuote(q2.getId());
        assertThat(service.findAllQuotes()).hasSize(1);
    }

    @Test
    @DisplayName("없는 ID 조회/수정/삭제는 IllegalArgumentException")
    void throws_for_missing_id() {
        QuoteService service = new QuoteService(new QuoteRepository(tempDir));

        assertThatThrownBy(() -> service.findQuoteById(999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("999번 명언은 존재하지 않습니다.");

        assertThatThrownBy(() -> service.updateQuote(999, "x", "y"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("999번 명언은 존재하지 않습니다.");

        assertThatThrownBy(() -> service.deleteQuote(999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("999번 명언은 존재하지 않습니다.");
    }
}
