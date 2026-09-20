package jarofthings.jar_of_little_things.controller;

import tools.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jarofthings.jar_of_little_things.dto.CreateNoteRequest;
import jarofthings.jar_of_little_things.dto.NoteResponse;
import jarofthings.jar_of_little_things.dto.UpdateNoteRequest;
import jarofthings.jar_of_little_things.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:5173")
public class NoteController {

    private final NoteService noteService;
    private final ObjectMapper objectMapper;

    public NoteController(
            NoteService noteService,
            ObjectMapper objectMapper
    ) {
        this.noteService = noteService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse createNote(
            @RequestPart("note") String noteJson,
            @RequestPart(value = "image", required = false)
            MultipartFile image
    ) throws IOException {

        CreateNoteRequest request = objectMapper.readValue(
                noteJson,
                CreateNoteRequest.class
        );

        return noteService.createNote(request, image);
    }

    @GetMapping
    public List<NoteResponse> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    public NoteResponse getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id);
    }

    @PutMapping("/{id}")
    public NoteResponse updateNote(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNoteRequest request
    ) {
        return noteService.updateNote(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
    }

    @GetMapping("/random")
    public NoteResponse getRandomNote() {
        return noteService.getRandomUnlockedNote();
    }

    @GetMapping("/category/{categoryId}")
    public List<NoteResponse> getNotesByCategory(
            @PathVariable Long categoryId
    ) {
        return noteService.getNotesByCategory(categoryId);
    }
}