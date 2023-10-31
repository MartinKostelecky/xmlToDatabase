package cz.martinkostelecky;

import cz.martinkostelecky.controller.CityController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppMain {

    public static void main(String[] args) {
        SpringApplication.run(AppMain.class, args);
    }
    @Bean
    public CommandLineRunner initializer(
            CityController cityController,
            @Value("${city.fileURL}") String fileURL,
            @Value("${city.saveDir}") String saveDir) {
        return args -> {
            cityController.processCitiesAndCityParts(fileURL, saveDir);
        };
    }
}



