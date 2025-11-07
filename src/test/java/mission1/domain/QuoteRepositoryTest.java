package mission1.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class QuoteRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("save: 파일과 메모리에 저장된다")
    void save_persistsToFileAndMemory() throws Exception {
        QuoteRepository repo = new QuoteRepository(tempDir);

        Quote saved = repo.save("content", "author");

        assertThat(saved.getId()).isEqualTo(1);
        assertThat(Files.exists(tempDir.resolve("1.json"))).isTrue();
        assertThat(Files.exists(tempDir.resolve("lastId.txt"))).isTrue();

        Quote found = repo.findById(1);
        assertThat(found).isNotNull();
        assertThat(found.getContent()).isEqualTo("content");
        assertThat(found.getAuthor()).isEqualTo("author");
    }

    @Test
    @DisplayName("findAll: ID 내림차순(최근 등록 우선)으로 정렬된다")
    void findAll_returnsDescendingById() {
        QuoteRepository repo = new QuoteRepository(tempDir);
        Quote first  = repo.save("content1", "author1");
        Quote second = repo.save("content2", "author2");

        List<Quote> list = repo.findAll();

        assertThat(list).extracting(Quote::getId).containsExactly(second.getId(), first.getId());
        assertThat(list.get(0).getContent()).isEqualTo("content2");
        assertThat(list.get(1).getContent()).isEqualTo("content1");
    }

    @Test
    @DisplayName("deleteById시 파일도 함께 삭제되며, 없는 ID는 false를 반환한다")
    void deleteById_removesFileAndReturnsFalseForMissing() {
        QuoteRepository repo = new QuoteRepository(tempDir);
        Quote saved = repo.save("to be deleted", "deleter");

        boolean deleted = repo.deleteById(saved.getId());
        assertThat(deleted).isTrue();
        assertThat(Files.exists(tempDir.resolve(saved.getId() + ".json"))).isFalse();

        boolean notDeleted = repo.deleteById(999);
        assertThat(notDeleted).isFalse();
    }

    @Test
    @DisplayName("update: 메모리와 파일 내용이 함께 갱신된다")
    void update_updatesMemoryAndFile() {
        QuoteRepository repo = new QuoteRepository(tempDir);
        Quote saved = repo.save("original content", "original author");

        repo.update(saved.getId(), "updated content", "updated author");

        Quote quote = repo.findById(saved.getId());
        assertThat(quote.getContent()).isEqualTo("updated content");
        assertThat(quote.getAuthor()).isEqualTo("updated author");
    }

    @Test
    @DisplayName("재기동 가정: 동일 폴더로 새 인스턴스를 만들면 JSON을 로드하고 nextId를 이어간다")
    void reload_loadsJsonAndContinuesNextId() {
        QuoteRepository r1 = new QuoteRepository(tempDir);
        r1.save("content1", "author1");
        r1.save("content2", "author2");

        QuoteRepository r2 = new QuoteRepository(tempDir);
        assertThat(r2.findAll()).hasSize(2);

        Quote third = r2.save("content3", "author3");
        assertThat(third.getId()).isEqualTo(3);
    }

    @Test
    @DisplayName("buildDataJson: data.json이 생성되고 ID 오름차순으로 기록된다")
    void buildDataJson_createsDataJsonInAscendingOrder() {
        QuoteRepository repo = new QuoteRepository(tempDir);
        repo.save("content1", "author1");
        repo.save("content2", "author2");

        repo.buildDataJson();

        Path dataJson = tempDir.resolve("data.json");
        assertThat(Files.exists(dataJson)).isTrue();
    }
}
