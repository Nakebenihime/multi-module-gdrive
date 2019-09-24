package my.service;

import org.springframework.web.multipart.MultipartFile;

public interface DriveService {

    void upload(MultipartFile multipartFile) throws Exception;
}
