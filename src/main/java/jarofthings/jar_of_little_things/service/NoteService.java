package jarofthings.jar_of_little_things.service;

import jarofthings.jar_of_little_things.dto.CreateNoteRequest;
import jarofthings.jar_of_little_things.dto.NoteResponse;
import jarofthings.jar_of_little_things.dto.UpdateNoteRequest;
import jarofthings.jar_of_little_things.entity.Category;
import jarofthings.jar_of_little_things.entity.Note;
import jarofthings.jar_of_little_things.entity.User;
import jarofthings.jar_of_little_things.exception.ResourceNotFoundException;
import jarofthings.jar_of_little_things.repository.CategoryRepository;
import jarofthings.jar_of_little_things.repository.NoteRepository;
import jarofthings.jar_of_little_things.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public NoteService(
            NoteRepository noteRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            CloudinaryService cloudinaryService
    ) {
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }

    // CREATE NOTE WITH OPTIONAL IMAGE
    public NoteResponse createNote(
            CreateNoteRequest request,
            MultipartFile image
    ) {
        Category category = categoryRepository.findById(
                request.categoryId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Category not found")
        );

        User creator = userRepository.findById(
                request.createdBy()
        ).orElseThrow(() ->
                new ResourceNotFoundException("User not found")
        );

        String imageUrl = cloudinaryService.uploadImage(image);

        Note note = Note.builder()
                .content(request.content())
                .category(category)
                .createdBy(creator)
                .unlockAt(request.unlockAt())
                .createdAt(LocalDateTime.now())
                .active(true)
                .imageUrl(imageUrl)
                .build();

        return NoteResponse.fromEntity(noteRepository.save(note));
    }

    // READ ALL
    @Transactional(readOnly = true)
    public List<NoteResponse> getAllNotes() {
        return noteRepository
                .findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(NoteResponse::fromEntity)
                .toList();
    }

    // READ ONE
    @Transactional(readOnly = true)
    public NoteResponse getNoteById(Long id) {
        Note note = getNoteEntity(id);
        return NoteResponse.fromEntity(note);
    }

    // UPDATE
    public NoteResponse updateNote(
            Long id,
            UpdateNoteRequest request
    ) {
        Note note = getNoteEntity(id);

        Category category = categoryRepository.findById(
                request.categoryId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Category not found")
        );

        note.setContent(request.content());
        note.setCategory(category);
        note.setUnlockAt(request.unlockAt());
        note.setActive(request.active());

        return NoteResponse.fromEntity(noteRepository.save(note));
    }

    // DELETE
    public void deleteNote(Long id) {
        Note note = getNoteEntity(id);
        noteRepository.delete(note);
    }

    // RANDOM UNLOCKED NOTE
    @Transactional(readOnly = true)
    public NoteResponse getRandomUnlockedNote() {

        List<Note> notes = noteRepository.findUnlockedNotes(
                LocalDateTime.now()
        );

        if (notes.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No unlocked notes are available right now"
            );
        }

        int randomIndex = ThreadLocalRandom.current()
                .nextInt(notes.size());

        return NoteResponse.fromEntity(notes.get(randomIndex));
    }

    // FILTER BY CATEGORY
    @Transactional(readOnly = true)
    public List<NoteResponse> getNotesByCategory(Long categoryId) {

        categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        return noteRepository
                .findByCategoryIdAndActiveTrue(categoryId)
                .stream()
                .map(NoteResponse::fromEntity)
                .toList();
    }

    private Note getNoteEntity(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Note not found: " + id
                        )
                );
    }
}