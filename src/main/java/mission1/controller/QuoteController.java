package mission1.controller;

import mission1.domain.Quote;
import mission1.service.QuoteService;
import mission1.utils.QuoteValidator;
import mission1.view.InputView;
import mission1.view.OutputView;

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
        } else if ("목록".equalsIgnoreCase(command)) {
            listQuotes();
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

    private void listQuotes() {
        var quotes = service.findAllQuotes();
        OutputView.printQuoteList(quotes);
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
