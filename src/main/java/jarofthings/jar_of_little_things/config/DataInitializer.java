package jarofthings.jar_of_little_things.config;


import jarofthings.jar_of_little_things.entity.Category;
import jarofthings.jar_of_little_things.entity.User;
import jarofthings.jar_of_little_things.repository.CategoryRepository;
import jarofthings.jar_of_little_things.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(
            UserRepository userRepository,
            CategoryRepository categoryRepository) {

        return args -> {

            // Seed users
            if (userRepository.count() == 0) {

                userRepository.save(
                        User.builder()
                                .name("Jar Owner")
                                .email("owner@example.com")
                                .role(User.Role.OWNER)
                                .build()
                );

                userRepository.save(
                        User.builder()
                                .name("Best Friend")
                                .email("friend@example.com")
                                .role(User.Role.FRIEND)
                                .build()
                );
            }

            // Seed categories
            if (categoryRepository.count() == 0) {

                categoryRepository.save(
                        Category.builder()
                                .name("MEMORY")
                                .color("#F2B8C6")
                                .icon("heart")
                                .build()
                );

                categoryRepository.save(
                        Category.builder()
                                .name("JOKE")
                                .color("#F4D9A6")
                                .icon("laugh")
                                .build()
                );

                categoryRepository.save(
                        Category.builder()
                                .name("ENCOURAGEMENT")
                                .color("#C8DDBE")
                                .icon("sun")
                                .build()
                );

                categoryRepository.save(
                        Category.builder()
                                .name("CHALLENGE")
                                .color("#B9C8F2")
                                .icon("star")
                                .build()
                );
            }
        };
    }
}