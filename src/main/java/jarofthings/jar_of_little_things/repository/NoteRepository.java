package jarofthings.jar_of_little_things.repository;

import jarofthings.jar_of_little_things.entity.Note;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NoteRepository
        extends JpaRepository<Note, Long> {

    List<Note> findByActiveTrueOrderByCreatedAtDesc();

    List<Note> findByCategoryIdAndActiveTrue(Long categoryId);

    @Query("""
        SELECT n
        FROM Note n
        WHERE n.active = true
        AND (n.unlockAt IS NULL OR n.unlockAt <= :now)
        ORDER BY n.id
    """)
    List<Note> findUnlockedNotes(
            @Param("now") LocalDateTime now
    );
}