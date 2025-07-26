package com.alura.literalura.services;

import com.alura.literalura.entities.LiterAluraAuthorEntity;
import com.alura.literalura.entities.LiterAluraBooksEntity;
import com.alura.literalura.repositories.LiterAluraAuthorRepository;
import com.alura.literalura.repositories.LiterAluraBookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LiterAluraBookImportService {
    private final LiterAluraBookRepository bookRepository;
    private final LiterAluraAuthorRepository authorRepository;
    private final RestTemplate restTemplate;
    private static final String GUTENDEX_API = "https://gutendex.com/books/";

    @Transactional
    public LiterAluraBooksEntity searchAndSaveBook(String title) throws IOException {
        // 1. Busca na API
        String url = GUTENDEX_API + "?search=" + URLEncoder.encode(title, "UTF-8");
        JsonNode response = restTemplate.getForObject(url, JsonNode.class);
        JsonNode firstResult = response.path("results").get(0);
        Long bookId = firstResult.path("id").asLong();

        // 2. Verifica se já existe no banco (modificado)
        Optional<LiterAluraBooksEntity> existingBook = bookRepository.findById(bookId);
        if (existingBook.isPresent()) {
            return existingBook.get(); // Retorna livro existente
        }

        // 3. Cria novo livro (só executa se não existir)
        LiterAluraBooksEntity newBook = new LiterAluraBooksEntity();
        newBook.setId(bookId);
        newBook.setTitle(firstResult.path("title").asText());
        newBook.setDownloadCount(firstResult.path("download_count").asInt());

        // Pega o idioma
        JsonNode languages = firstResult.path("languages");
        newBook.setLanguage(languages.isEmpty() ? "en" : languages.get(0).asText());

        // 4. Processa autor
        processAuthor(firstResult, newBook);

        return bookRepository.save(newBook);
    }

    private void processAuthor(JsonNode bookNode, LiterAluraBooksEntity book) {
        JsonNode authorNode = bookNode.path("authors").get(0);
        if (authorNode != null) {
            String authorName = authorNode.path("name").asText();
            LiterAluraAuthorEntity author = authorRepository.findByName(authorName)
                    .orElseGet(() -> {
                        LiterAluraAuthorEntity newAuthor = new LiterAluraAuthorEntity();
                        newAuthor.setName(authorName);
                        newAuthor.setBirthYear(getNullableInt(authorNode, "birth_year"));
                        newAuthor.setDeathYear(getNullableInt(authorNode, "death_year"));
                        return authorRepository.save(newAuthor);
                    });
            book.setAuthor(author);
        }
    }

    private Integer getNullableInt(JsonNode node, String field) {
        JsonNode valueNode = node.path(field);
        if (valueNode.isMissingNode() || valueNode.isNull()) {
            return null;
        }

        // Verifica se o valor é zero e se deve ser considerado como null
        int value = valueNode.asInt();
        return value == 0 ? null : value; // Ou simplesmente return valueNode.asInt();
    }

    public List<LiterAluraBooksEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public List<LiterAluraAuthorEntity> getAllAuthors() {
        return authorRepository.findAllWithBooks();
    }

    @Transactional(readOnly = true)
    public List<LiterAluraAuthorEntity> findAuthorsAliveInYear(int year) {
        return authorRepository.findAliveInYear(year);
    }

    @Transactional(readOnly = true)
    public List<String> getAvailableLanguages() {
        return bookRepository.findDistinctLanguages();
    }

    @Transactional(readOnly = true)
    public List<LiterAluraBooksEntity> findBooksByLanguage(String language) {
        return bookRepository.findByLanguageIgnoreCase(language);
    }
}