package jarofthings.jar_of_little_things.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "jar-of-little-things",
                            "resource_type", "image"
                    )
            );

            return uploadResult.get("secure_url").toString();

        } catch (IOException exception) {
            throw new RuntimeException(
                    "Could not upload image to Cloudinary",
                    exception
            );
        }
    }
}
