package jarofthings.jar_of_little_things.service;


import jarofthings.jar_of_little_things.dto.NoteResponse;
import jarofthings.jar_of_little_things.entity.Note;
import jarofthings.jar_of_little_things.entity.NoteCollection;
import jarofthings.jar_of_little_things.entity.User;
import jarofthings.jar_of_little_things.exception.ResourceNotFoundException;
import jarofthings.jar_of_little_things.repository.NoteCollectionRepository;
import jarofthings.jar_of_little_things.repository.NoteRepository;
import jarofthings.jar_of_little_things.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CollectionService {

    private final NoteCollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;

    public CollectionService(
            NoteCollectionRepository collectionRepository,
            UserRepository userRepository,
            NoteRepository noteRepository) {

        this.collectionRepository = collectionRepository;
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    @Transactional
    public void collectNote(Long userId, Long noteId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Note not found"));

        if (!note.isActive()
                || (note.getUnlockAt() != null
                && note.getUnlockAt().isAfter(LocalDateTime.now()))) {

            throw new IllegalStateException(
                    "This note is not available for collection"
            );
        }

        if (collectionRepository.existsByUserIdAndNoteId(
                userId, noteId)) {

            throw new IllegalStateException(
                    "You have already collected this note"
            );
        }

        NoteCollection collection = NoteCollection.builder()
                .user(user)
                .note(note)
                .collectedAt(LocalDateTime.now())
                .build();

        collectionRepository.save(collection);
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> getCollectedNotes(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return collectionRepository
                .findByUserIdOrderByCollectedAtDesc(userId)
                .stream()
                .map(collection ->
                        NoteResponse.fromEntity(collection.getNote()))
                .toList();
    }

    public long getCollectionCount(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return collectionRepository.countByUserId(userId);
    }
}