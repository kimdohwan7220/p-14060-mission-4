package mission1.service;

import java.util.List;
import mission1.domain.Quote;
import mission1.domain.QuoteRepository;
import mission1.utils.QuoteValidator;

public class QuoteService {
    private final QuoteRepository repository;
    private final QuoteValidator validator;

    public QuoteService() {
        this(new QuoteRepository());
    }

    public QuoteService(QuoteRepository repository) {
        this.repository = repository;
        this.validator = new QuoteValidator(repository);
    }

    public Quote registerQuote(String content, String author) {
        return repository.save(content, author);
    }

    public List<Quote> findAllQuotes() {
        return repository.findAll();
    }

    public List<Quote> findQuotes(String keywordType, String keyword) {
        return repository.search(keywordType, keyword);
    }

    public Quote findQuoteById(int id) {
        validator.validateQuoteExists(id);
        return repository.findById(id);
    }

    public void deleteQuote(int id) {
        validator.validateQuoteExists(id);
        repository.deleteById(id);
    }

    public void updateQuote(int id, String content, String author) {
        validator.validateQuoteExists(id);
        repository.update(id, content, author);
    }

    public void buildDataJson() {
        repository.buildDataJson();
    }

    public QuoteRepository getRepository() {
        return repository;
    }
}
