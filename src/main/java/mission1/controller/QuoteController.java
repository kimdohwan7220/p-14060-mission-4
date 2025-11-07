package mission1.controller;

import java.util.Map;
import mission1.domain.Quote;
import mission1.service.QuoteService;
import mission1.utils.InputValidator;
import mission1.view.InputView;
import mission1.view.OutputView;
import mission1.utils.QuoteValidator;
import mission1.utils.CommandParser;

public class QuoteController {

    private final QuoteService service;
    private final QuoteValidator validator;

    public QuoteController(QuoteService service) {
        this.service = service;
        this.validator = new QuoteValidator(service.getRepository());
    }

    public void handleCommand(String command) {
        if ("등록".equalsIgnoreCase(command)) {
            registerQuote();
        } else if (command.startsWith("목록")) {
            listQuotes(command);
        } else if (command.startsWith("삭제?id=")) {
            deleteQuote(command);
        } else if (command.startsWith("수정?id=")) {
            updateQuote(command);
        } else if ("빌드".equalsIgnoreCase(command)) {
            buildData();
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
        try {
            InputValidator.validateListSearchParams(params);

            String keywordType = InputValidator.normalizeKeywordType(params.get("keywordType"));
            String keyword = params.get("keyword");

            OutputView.printSearchHeader(keywordType, keyword);
            var quotes = service.findQuotes(keywordType, keyword);
            OutputView.printQuoteList(quotes);
        } catch (IllegalArgumentException e) {
            OutputView.printError(e.getMessage());
        }
    }

    private void deleteQuote(String command) {
        try {
            int id = Integer.parseInt(command.substring("삭제?id=".length()));
            service.deleteQuote(id);
            OutputView.printQuoteDeleted(id);
        } catch (IllegalArgumentException e) {
            OutputView.printError(e.getMessage());
        }
    }

    private void updateQuote(String command) {
        try {
            int id = Integer.parseInt(command.substring("수정?id=".length()));
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
}
