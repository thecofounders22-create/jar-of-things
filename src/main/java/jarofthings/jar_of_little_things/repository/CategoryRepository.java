package jarofthings.jar_of_little_things.repository;



import jarofthings.jar_of_little_things.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
}