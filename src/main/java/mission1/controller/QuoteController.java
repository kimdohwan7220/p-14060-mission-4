package mission1.controller;

import java.util.List;
import java.util.Map;
import mission1.domain.Quote;
import mission1.service.QuoteService;
import mission1.utils.InputValidator;
import mission1.utils.Pagination;
import mission1.view.InputView;
import mission1.view.OutputView;
import mission1.utils.CommandParser;

public class QuoteController {

    private static final String CMD_REGISTER = "등록";
    private static final String CMD_LIST = "목록";
    private static final String CMD_DELETE = "삭제?id=";
    private static final String CMD_UPDATE = "수정?id=";
    private static final String CMD_BUILD = "빌드";

    private static final int PAGE_SIZE = 5;

    private final QuoteService service;

    public QuoteController(QuoteService service) {
        this.service = service;
    }

    public void handleCommand(String command) {
        if (CMD_REGISTER.equalsIgnoreCase(command)) {
            registerQuote(); return;
        }
        if (command.startsWith(CMD_LIST)) {
            listQuotes(command); return;
        }
        if (command.startsWith(CMD_DELETE)) {
            deleteQuote(command); return;
        }
        if (command.startsWith(CMD_UPDATE)) {
            updateQuote(command); return;
        }
        if (CMD_BUILD.equalsIgnoreCase(command)) {
            buildData(); return;
        }
    }

    private void registerQuote() {
        String content = InputView.quoteInput();
        String author = InputView.authorInput();

        Quote quote = service.registerQuote(content, author);

        OutputView.printQuoteRegistered(quote);
    }

    private void listQuotes(String command) {
        Map<String, String> params = CommandParser.parseQuery(command);

        try {
            int page = InputValidator.parsePageOrDefault(params, 1);

            if (isSearch(params)) {
                handleSearchQuotes(params, page);
            } else {
                handleListAll(page);
            }
        } catch (IllegalArgumentException e) {
            OutputView.printError(e.getMessage());
        }
    }

    private void deleteQuote(String command) {
        try {
            int id = InputValidator.parseIntOrThrow(command.substring(CMD_DELETE.length()), "id");
            service.deleteQuote(id);
            OutputView.printQuoteDeleted(id);
        } catch (IllegalArgumentException e) {
            OutputView.printError(e.getMessage());
        }
    }

    private void updateQuote(String command) {
        try {
            int id = InputValidator.parseIntOrThrow(command.substring(CMD_UPDATE.length()), "id");
            Quote existing = service.findQuoteById(id);

            String newContent = InputView.quoteInput(existing.getContent());
            String newAuthor = InputView.authorInput(existing.getAuthor());

            service.updateQuote(id, newContent, newAuthor);
            OutputView.printQuoteUpdated(id);
        } catch (IllegalArgumentException e) {
            OutputView.printError(e.getMessage());
        }
    }

    private void buildData() {
        try {
            service.buildDataJson();
            OutputView.printBuildDone();
        } catch (Exception e) {
            OutputView.printError(e.getMessage());
        }
    }

    private boolean isSearch(Map<String, String> params) {
        return params.containsKey("keywordType") || params.containsKey("keyword");
    }

    private void handleListAll(int page) {
        List<Quote> all = service.findAllQuotes();
        printPagedQuotes(all, page);
    }

    private void handleSearchQuotes(Map<String, String> params, int page) {
        String keywordType = InputValidator.validateListSearchParams(params);
        String keyword = params.get("keyword");

        OutputView.printSearchHeader(keywordType, keyword);

        List<Quote> filtered = service.findQuotes(keywordType, keyword);
        printPagedQuotes(filtered, page);
    }

    private void printPagedQuotes(List<Quote> all, int page) {
        Pagination<Quote> pagination = new Pagination<>(all, page, PAGE_SIZE);

        int totalPages = pagination.getTotalPages();
        InputValidator.validatePageRange(page, totalPages);

        List<Quote> pageList = pagination.getPageItems();
        OutputView.printQuoteList(pageList);
        OutputView.printPageInfo(page, totalPages);
    }
}
