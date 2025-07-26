package com.alura.literalura;

import com.alura.literalura.services.LiterAluraMenuService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    final private LiterAluraMenuService menuService;


    public LiteraluraApplication(LiterAluraMenuService menuService) {
        this.menuService = menuService;
    }


    @Override
    public void run(String... args) throws Exception {
        menuService.displayMenu();
    }

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }
}