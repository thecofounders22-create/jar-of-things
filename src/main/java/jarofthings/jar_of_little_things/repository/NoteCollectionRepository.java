package jarofthings.jar_of_little_things.repository;


import jarofthings.jar_of_little_things.entity.NoteCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteCollectionRepository
        extends JpaRepository<NoteCollection, Long> {

    boolean existsByUserIdAndNoteId(Long userId, Long noteId);

    List<NoteCollection> findByUserIdOrderByCollectedAtDesc(
            Long userId
    );

    long countByUserId(Long userId);
}