package my.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import my.config.ApplicationConfig;
import my.context.ApplicationConstantVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

@Service
public class DriveServiceImpl implements DriveService {

    private Logger logger = LoggerFactory.getLogger(DriveServiceImpl.class);

    private Drive driveService;

    private AuthorizationService authorizationService;

    @Autowired
    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    private ApplicationConfig applicationConfig;

    @Autowired
    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @PostConstruct
    public void init() throws Exception {
        Credential credential = authorizationService.getCredentials();
        driveService = new Drive.Builder(ApplicationConstantVariables.HTTP_TRANSPORT, ApplicationConstantVariables.JSON_FACTORY, credential)
                .setApplicationName(ApplicationConstantVariables.APPLICATION_NAME).build();
    }

    @Override
    public void upload(MultipartFile multipartFile) throws Exception {

        String path = applicationConfig.getTemporaryFolder();
        String name = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();

        java.io.File transferedFile = new java.io.File(path, name);
        multipartFile.transferTo(transferedFile);

        File fileMetadata = new File();
        fileMetadata.setName(name);

        FileContent fileContent = new FileContent(contentType, transferedFile);
        File uploadedFile = driveService.files().create(fileMetadata, fileContent).setFields("id").execute();

        logger.debug("File ID: " + uploadedFile.getName() + ", " + uploadedFile.getId());
    }
}
