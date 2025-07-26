package com.alura.literalura.services;

import com.alura.literalura.entities.LiterAluraAuthorEntity;
import com.alura.literalura.entities.LiterAluraBooksEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class LiterAluraMenuService {
    private final Scanner scanner = new Scanner(System.in);
    private final LiterAluraBookImportService bookService;

    public void displayMenu() {
        boolean running = true;
        while (running) {
            try {
                System.out.println("\n=== Menu LiterAlura ===");
                System.out.println("1) Buscar livros pelo título");
                System.out.println("2) Listar livros registrados");
                System.out.println("3) Listar autores registrados");
                System.out.println("4) Listar autores vivos em um ano");
                System.out.println("5) Listar livros por idioma");
                System.out.println("0) Sair");
                System.out.print("Opção: ");

                try {
                    int option = scanner.nextInt();
                    scanner.nextLine(); // Limpa buffer

                    switch (option) {
                        case 1:
                            searchBook();
                            break;
                        case 2:
                            listRegisteredBooks();
                            break;
                        case 3:
                            listRegisteredAuthors();
                            break;
                        case 4:
                            listAuthorsAliveInYear();
                            break;
                        case 5:
                            listBooksByLanguage();
                            break;
                        case 0:
                            running = false;
                            System.out.println("Encerrando...");
                            closeResources();
                            break;
                        default:
                            System.out.println("Opção inválida!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Erro: Digite apenas números!");
                    scanner.nextLine();

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void searchBook() {
        System.out.print("\nDigite o título: ");
        String title = scanner.nextLine();

        try {
            LiterAluraBooksEntity book = bookService.searchAndSaveBook(title);
            System.out.println("\nLIVRO SALVO:");
            System.out.println("Título: " + book.getTitle());
            System.out.println("Autor: " + book.getAuthor().getName());
            System.out.println("Idioma: " + book.getLanguage());
            System.out.println("Número de downloads: " + book.getDownloadCount());

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listRegisteredBooks() {
        List<LiterAluraBooksEntity> books = bookService.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("\nNenhum livro registrado no banco de dados.");
            return;
        }

        System.out.println("\n=== LIVROS REGISTRADOS ===");
        books.forEach(book -> {
            System.out.println("Título: " + book.getTitle());
            System.out.println("Autor: " + book.getAuthor().getName());
            System.out.println("Idioma: " + book.getLanguage());
            System.out.println("Downloads: " + book.getDownloadCount());
            System.out.println("-----------------------");
        });
    }

    private void listRegisteredAuthors() {
        List<LiterAluraAuthorEntity> authors = bookService.getAllAuthors();

        if (authors.isEmpty()) {
            System.out.println("\nNenhum autor registrado no banco de dados.");
            return;
        }

        System.out.println("\n=== AUTORES REGISTRADOS ===");
        authors.forEach(author -> {
            System.out.println("Nome: " + author.getName());
            System.out.println("Ano de nascimento: " +
                    (author.getBirthYear() != null ? author.getBirthYear() : "Desconhecido"));
            System.out.println("Ano de falecimento: " +
                    (author.getDeathYear() != null ? author.getDeathYear() : "Desconhecido"));
            System.out.println("Livros (" + author.getBooks().size() + "):");
            author.getBooks().forEach(book -> {
                System.out.println("  - " + book.getTitle() +
                        " (" + book.getLanguage() + ")" +
                        " | Downloads: " + book.getDownloadCount());
            });
            System.out.println("-----------------------");
        });
    }

    private void listAuthorsAliveInYear() {
        try {
            System.out.print("\nDigite o ano para pesquisa: ");
            int year = scanner.nextInt();
            scanner.nextLine(); // Limpa buffer

            List<LiterAluraAuthorEntity> authors = bookService.findAuthorsAliveInYear(year);

            if (authors.isEmpty()) {
                System.out.println("\nNenhum autor vivo encontrado no ano " + year);
                return;
            }

            System.out.println("\n=== AUTORES VIVOS EM " + year + " ===");
            authors.forEach(author -> {
                System.out.println("\nNome: " + author.getName());
                System.out.println("Nascimento: " + author.getBirthYear());
                System.out.println("Falecimento: " +
                        (author.getDeathYear() != null ? author.getDeathYear() : "Presente"));
                System.out.println("Idade em " + year + ": " +
                        calculateAge(author.getBirthYear(), author.getDeathYear(), year));
                System.out.println("Total de livros: " + author.getBooks().size());
                author.getBooks().forEach(book -> {
                    System.out.println("  - " + book.getTitle());
                });
            });
        } catch (InputMismatchException e) {
            System.out.println("Erro: Digite um ano válido (número inteiro)");
            scanner.nextLine();
        }
    }

    private String calculateAge(Integer birthYear, Integer deathYear, int targetYear) {
        if (birthYear == null) return "Desconhecida";
        if (birthYear > targetYear) return "Ainda não nascido";
        if (deathYear != null && deathYear < targetYear) return "Já falecido";

        int age = targetYear - birthYear;
        return age + " anos";
    }

    private void listBooksByLanguage() {
        try {
            System.out.println("\nIdiomas disponíveis:");
            List<String> languages = bookService.getAvailableLanguages();
            languages.forEach(lang -> System.out.println(" - " + lang));

            System.out.print("\nDigite o idioma (código ex: en, pt, es): ");
            String language = scanner.nextLine().trim().toLowerCase();

            List<LiterAluraBooksEntity> books = bookService.findBooksByLanguage(language);

            if (books.isEmpty()) {
                System.out.println("\nNenhum livro encontrado no idioma '" + language + "'");
                return;
            }

            System.out.println("\n=== LIVROS EM " + language.toUpperCase() + " ===");
            System.out.println("Total encontrado: " + books.size());
            books.forEach(book -> {
                System.out.println("\nTítulo: " + book.getTitle());
                System.out.println("Autor: " + book.getAuthor().getName());
                System.out.println("Downloads: " + book.getDownloadCount());
            });
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void closeResources() {
        scanner.close();  // Fecha o Scanner para evitar vazamento de recursos
        System.exit(0);   // Encerra a JVM (opcional, mas garante que tudo pare)
    }
}