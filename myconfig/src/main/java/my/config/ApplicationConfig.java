package my.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties("my")
public class ApplicationConfig {

    @Value("${google.oauth.callback.uri}")
    private String CALLBACK_URI;

    @Value("${google.secret.key.path}")
    private Resource gdSecretKeys;

    @Value("${google.credentials.folder.path}")
    private Resource credentialsFolder;

    @Value("${myapp.temp.path}")
    private String temporaryFolder;

    public String getCALLBACK_URI() {
        return CALLBACK_URI;
    }

    public void setCALLBACK_URI(String CALLBACK_URI) {
        this.CALLBACK_URI = CALLBACK_URI;
    }

    public Resource getGdSecretKeys() {
        return gdSecretKeys;
    }

    public void setGdSecretKeys(Resource gdSecretKeys) {
        this.gdSecretKeys = gdSecretKeys;
    }

    public Resource getCredentialsFolder() {
        return credentialsFolder;
    }

    public void setCredentialsFolder(Resource credentialsFolder) {
        this.credentialsFolder = credentialsFolder;
    }

    public String getTemporaryFolder() { return temporaryFolder; }

    public void setTemporaryFolder(String temporaryFolder) { this.temporaryFolder = temporaryFolder; }
}
