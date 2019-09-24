package my.web;

import my.domain.FileUpload;
import my.service.AuthorizationService;
import my.service.DriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

    private AuthorizationService authorizationService;

    @Autowired
    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    private DriveService driveService;

    @Autowired
    public void setDriveService(DriveService driveService) {
        this.driveService = driveService;
    }

    @GetMapping("/")
    public String showHomePage() throws Exception {
        if (authorizationService.isUserAuthenticated()) {
            return "redirect:/home.html";
        } else {
            return "redirect:/index.html";
        }
    }

    @GetMapping("/home")
    public String goToHome() {
        return "home.html";
    }

    @GetMapping("/googlesignin")
    public void doGoogleSignIn(HttpServletResponse response) throws Exception {
        response.sendRedirect(authorizationService.authenticateUserViaGoogle());
    }

    @GetMapping("/oauth/callback")
    public String saveAuthorizationCode(HttpServletRequest request) throws Exception{
        String code = request.getParameter("code");
        if (code!=null){
            authorizationService.exchangeCodeForTokens(code);
            return "redirect:/home.html";
        }
        return "redirect:/index.html";
    }

    @PostMapping("/upload")
    public String upload(@ModelAttribute FileUpload fileUpload) throws Exception{
        MultipartFile multipartFile = fileUpload.getMultipartFile();
        driveService.upload(multipartFile);
        return "redirect:/home.html?status=success";
    }
}
