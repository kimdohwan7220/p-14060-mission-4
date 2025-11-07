package mission1.utils;

import java.util.List;
import mission1.domain.Quote;
import mission1.domain.QuoteRepository;

public class QuoteValidator {
    private final QuoteRepository repository;

    public QuoteValidator(QuoteRepository repository) {
        this.repository = repository;
    }

    public void validateQuoteExists(int id) {
        if (repository.findById(id) == null) {
            throw new IllegalArgumentException(id + "번 명언은 존재하지 않습니다.");
        }
    }
}
