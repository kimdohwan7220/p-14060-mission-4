package mission1.controller;

import mission1.domain.Quote;
import mission1.domain.QuoteRepository;
import mission1.service.QuoteService;
import mission1.view.InputView;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

class QuoteControllerTest {

    @TempDir
    Path tempDir;

    private PrintStream consoleOut;
    private ByteArrayOutputStream testOutContent;

    @BeforeEach
    void setUp() {
        consoleOut = System.out;
        testOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOutContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(consoleOut);
        InputView.resetScanner();
    }

    private String output() {
        return testOutContent.toString();
    }

    private QuoteController controllerWithTempRepo() {
        QuoteService service = new QuoteService(new QuoteRepository(tempDir));
        return new QuoteController(service);
    }

    @Test
    @DisplayName("통합: 등록 → 수정 → 삭제 → 목록 (입출력 포함 풀 시나리오)")
    void 통합_등록_수정_삭제_목록() {
        QuoteController controller = controllerWithTempRepo();

        // 등록(id=1)
        InputView.setScanner(new Scanner("content1\nauthor1\n"));
        controller.handleCommand("등록");

        // 등록(id=2)
        InputView.setScanner(new Scanner("content2\nauthor2\n"));
        controller.handleCommand("등록");

        // 수정(id=1)
        InputView.setScanner(new Scanner("new content\nnew author\n"));
        controller.handleCommand("수정?id=1");

        // 삭제(id=2)
        controller.handleCommand("삭제?id=2");

        controller.handleCommand("목록");

        String out = output();
        assertThat(out)
                .contains("1번 명언이 등록되었습니다.")
                .contains("2번 명언이 등록되었습니다.")

                .contains("명언(기존) : content1")
                .contains("작가(기존) : author1")

                .contains("1번 명언이 수정되었습니다.")

                .contains("2번 명언이 삭제되었습니다.")
                .contains("번호 / 작가 / 명언")
                .contains("1 / new author / new content")
                .doesNotContain("2 /");
    }

    @Test
    @DisplayName("에러: 존재하지 않는 ID에 대한 수정/삭제는 오류 메시지 출력")
    void 에러_존재하지않는_ID_오류메시지() {
        QuoteController controller = controllerWithTempRepo();

        controller.handleCommand("수정?id=999");
        controller.handleCommand("삭제?id=999");

        assertThat(output())
                .contains("999번 명언은 존재하지 않습니다.");
    }
}
