package jarofthings.jar_of_little_things.controller;

import jarofthings.jar_of_little_things.dto.NoteResponse;
import jarofthings.jar_of_little_things.service.CollectionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/collections")
@CrossOrigin(origins = "https://jar-of-things-frontend.onrender.com")
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @PostMapping("/{noteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> collectNote(
            @PathVariable Long noteId,
            @RequestParam Long userId) {

        collectionService.collectNote(userId, noteId);

        return Map.of("message", "Note collected successfully");
    }

    @GetMapping
    public List<NoteResponse> getCollectedNotes(
            @RequestParam Long userId) {

        return collectionService.getCollectedNotes(userId);
    }

    @GetMapping("/count")
    public Map<String, Long> getCollectionCount(
            @RequestParam Long userId) {

        long count = collectionService.getCollectionCount(userId);

        return Map.of("count", count);
    }
}