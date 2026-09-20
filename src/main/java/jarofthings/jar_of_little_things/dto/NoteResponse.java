package jarofthings.jar_of_little_things.dto;

import jarofthings.jar_of_little_things.entity.Note;

import java.time.LocalDateTime;

public record NoteResponse(
        Long id,
        String content,
        Long categoryId,
        String categoryName,
        String categoryColor,
        String categoryIcon,
        Long createdBy,
        String creatorName,
        LocalDateTime unlockAt,
        LocalDateTime createdAt,
        boolean active,
        String imageUrl
) {

    public static NoteResponse fromEntity(Note note) {

        return new NoteResponse(
                note.getId(),
                note.getContent(),
                note.getCategory().getId(),
                note.getCategory().getName(),
                note.getCategory().getColor(),
                note.getCategory().getIcon(),
                note.getCreatedBy().getId(),
                note.getCreatedBy().getName(),
                note.getUnlockAt(),
                note.getCreatedAt(),
                note.isActive(),
                note.getImageUrl()
        );
    }
}