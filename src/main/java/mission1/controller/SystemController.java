package mission1.controller;

import mission1.view.OutputView;

public class SystemController {
    public void showHeader() {
        OutputView.printHeader();
    }

    public boolean isExitCommand(String command) {
        if ("종료".equals(command)) {
            OutputView.printExit();
            return true;
        }
        return false;
    }
}
