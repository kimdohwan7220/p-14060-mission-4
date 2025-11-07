package mission1.controller;

import mission1.domain.QuoteRepository;
import mission1.service.QuoteService;
import mission1.view.InputView;

public class AppController {

    private final SystemController systemController = new SystemController();
    private final QuoteController quoteController =
            new QuoteController(new QuoteService(new QuoteRepository()));

    public void run() {

        systemController.showHeader();

        while (true) {
            String command = InputView.commandInput();

            if (systemController.isExitCommand(command)) break;

            quoteController.handleCommand(command);
        }
    }
}