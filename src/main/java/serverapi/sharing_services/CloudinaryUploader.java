package serverapi.sharing_services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Map;

public class CloudinaryUploader {
    private Map paramsConfig = ObjectUtils.asMap(
            "api_key", System.getenv("CLOUDINARY_API_KEY"),
            "api_secret", System.getenv("CLOUDINARY_API_SECRET"),
            "cloud_name", "mangacrawlers"
    );
    private Cloudinary cloudinary;
    private static volatile CloudinaryUploader cloudinaryUploader;


    private CloudinaryUploader(){
        cloudinary = new Cloudinary(paramsConfig);
    }

    public static synchronized CloudinaryUploader getInstance(){
        if(cloudinaryUploader == null){
            System.err.println("null ne???????");
            cloudinaryUploader = new CloudinaryUploader();
        }

        return cloudinaryUploader;
    }




    public Map uploadImg(byte[] fileBytes, String fileName, String folder, boolean usePublicIdAsFileName) throws IOException {
        Map params = ObjectUtils.asMap(
                "public_id", usePublicIdAsFileName ? fileName : null,
                "folder", folder,
                "use_filename", true,
                "unique_filename", true,
                "resource_type", "auto"
        );


        Map uploadResult = cloudinary.uploader().upload(fileBytes, params);

        return uploadResult;
    }


    public Map deleteImg(String publicId) throws IOException {
        Map params = ObjectUtils.asMap(
                "invalidate", true
        );

        Map deleteResult = cloudinary.uploader().destroy(publicId, params);

        return deleteResult;
    }

}
