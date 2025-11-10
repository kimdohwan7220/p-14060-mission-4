package mission1.controller;

import java.util.Map;
import mission1.domain.Quote;
import mission1.service.QuoteService;
import mission1.utils.InputValidator;
import mission1.view.InputView;
import mission1.view.OutputView;
import mission1.utils.CommandParser;

public class QuoteController {

    private static final String CMD_REGISTER = "등록";
    private static final String CMD_LIST = "목록";
    private static final String CMD_DELETE = "삭제?id=";
    private static final String CMD_UPDATE = "수정?id=";
    private static final String CMD_BUILD = "빌드";

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
        var params = CommandParser.parseQuery(command);

        if (params.isEmpty()) {
            printAllQuotes();
            return;
        }

        try {
            handleSearchQuotes(params);
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

    private void printAllQuotes() {
        var quotes = service.findAllQuotes();
        OutputView.printQuoteList(quotes);
    }

    private void handleSearchQuotes(Map<String, String> params) {
        String keywordType = InputValidator.validateListSearchParams(params);
        String keyword = params.get("keyword");

        OutputView.printSearchHeader(keywordType, keyword);
        var quotes = service.findQuotes(keywordType, keyword);
        OutputView.printQuoteList(quotes);
    }

    private void buildData() {
        try {
            service.buildDataJson();
            OutputView.printBuildDone();
        } catch (Exception e) {
            OutputView.printError(e.getMessage());
        }
    }
}
