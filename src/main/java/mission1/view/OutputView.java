package mission1.view;

import java.util.List;
import mission1.domain.Quote;

public class OutputView {

    public static void printHeader() {
        System.out.println("== 명언 앱 ==");
    }

    public static void printExit() {
        System.out.println("프로그램을 종료합니다.");
    }

    public static void printQuoteRegistered(Quote quote) {
        System.out.println(quote.getId() + "번 명언이 등록되었습니다.");
    }

    public static void printQuoteList(List<Quote> quotes) {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for (Quote q : quotes) {
            System.out.printf("%d / %s / %s%n", q.getId(), q.getAuthor(), q.getContent());
        }
    }

    public static void printQuoteDeleted(int id) {
        System.out.println(id + "번 명언이 삭제되었습니다.");
    }

    public static void printQuoteUpdated(int id) {
        System.out.println(id + "번 명언이 수정되었습니다.");
    }

    public static void printError(String message) {
        System.out.println(message);
    }

    public static void printBuildDone() {
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
}
