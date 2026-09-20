package jarofthings.jar_of_little_things.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UpdateNoteRequest(

        @NotBlank(message = "Content is required")
        @Size(max = 5000, message = "Content is too long")
        String content,

        @NotNull(message = "Category ID is required")
        Long categoryId,

        LocalDateTime unlockAt,

        boolean active
) {
}