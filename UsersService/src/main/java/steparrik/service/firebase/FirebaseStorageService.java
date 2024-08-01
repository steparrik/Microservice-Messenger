package steparrik.service.firebase;

import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import steparrik.model.user.User;
import steparrik.service.UserService;
import steparrik.usersservice.utils.exceptions.ExceptionEntity;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FirebaseStorageService {
    public Blob uploadFile(MultipartFile file) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob;

        if(file==null || file.isEmpty()){
            throw new IOException("Файл не может быть пустым.");
        }

        blob = bucket.create(file.getOriginalFilename(), file.getBytes(), file.getContentType());

        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        return blob;
    }

}
