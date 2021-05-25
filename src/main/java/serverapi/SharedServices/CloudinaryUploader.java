package serverapi.SharedServices;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Map;

@NoArgsConstructor
public class CloudinaryUploader {

    private Map paramsConfig = ObjectUtils.asMap(
            "api_key", System.getenv("CLOUDINARY_API_KEY"),
            "api_secret", System.getenv("CLOUDINARY_API_SECRET"),
            "cloud_name", "mangacrawlers"
    );
    private Cloudinary cloudinary = new Cloudinary(paramsConfig);


    public Map uploadImg(byte[] fileBytes, String fileName) throws IOException {
        Map params = ObjectUtils.asMap(
                "public_id", fileName,
                "folder", "manga",
                "use_filename", true,
                "unique_filename", false,
                "resource_type", "auto"
        );

        Map uploadResult = cloudinary.uploader().upload(fileBytes, params);

        return uploadResult;
    }


    public void deleteImg(String publicId) throws IOException {
        Map params = ObjectUtils.asMap(
                "invalidate", true
        );

        Map uploadResult = cloudinary.uploader().destroy(publicId, params);

        System.out.println(uploadResult);
    }

}
