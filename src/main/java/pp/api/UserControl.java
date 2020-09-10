package pp.api;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pp.model.UserSMM;
import pp.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/users")
@RestController
@CrossOrigin("*")
public class UserControl {

    public final UserService userService;

    @Autowired
    public UserControl(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/exist")
    public int ContainsMail(@Valid @NonNull @RequestBody UserSMM userSMM) {
        return userService.EmailExist(userSMM.getEmail()) ? 1 : 0;
    }

    @GetMapping(value = "/all")
    public List<UserSMM> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping(value = "/first")
    public UserSMM getFirstUsers() {
        return userService.getAllUsers().get(0);
    }


    @PostMapping(value = "/add")
    public void addUser(@Valid @NonNull @RequestBody UserSMM userSMM) {

        userService.addUser(userSMM);

    }

    @PostMapping(
            path = {"{id}/video/upload"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadVideo(
                        @PathVariable("id") UUID uuid,
                            @RequestParam("file") MultipartFile multipartFile) {

        userService.UploadVideo(uuid, multipartFile);
    }


    @GetMapping(
           "{id}/video/download"
    )
    public String downloadVideo(
            @PathVariable("id") UUID uuid) {

        return userService.downloadVideo(uuid);
    }

    @PostMapping(value = "/connect")
    public void ConnectUser(@RequestParam("email") String email,
                            @RequestParam("password") String password) {
        userService.ConnectUser(email,password);

    }

    @PostMapping(value = "/{id}/disconnect")
    public void DisconnectUser(@PathVariable("id") UUID uuid) {
        userService.DisconnectUser(uuid);

    }

}

