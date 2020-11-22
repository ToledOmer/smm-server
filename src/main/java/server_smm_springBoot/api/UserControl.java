package server_smm_springBoot.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server_smm_springBoot.model.CompProps;
import server_smm_springBoot.model.UserSMM;
import server_smm_springBoot.service.CompressService;
import server_smm_springBoot.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/users")
@RestController
@CrossOrigin("*")
public class UserControl {

    public final UserService userService;
    public final CompressService compressService;

    @Autowired
    public UserControl(UserService userService, CompressService compressService) {
        this.userService = userService;
        this.compressService = compressService;
    }

//    public UserControl(UserService userService) {
//        this.userService = userService;
//    }

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


//    @GetMapping("{id}/video/download")
//    public void downloadVideo(
//            @PathVariable("id") UUID uuid) {
//
//        userService.download(uuid);
//    }


    @PostMapping("{id}/video/compress")
    public void Compress(@PathVariable("id") UUID uuid,
                         @Valid @NonNull @RequestBody CompProps prop) {
        UserSMM user = userService.getUser(uuid);
        System.out.println(prop.toString());
        compressService.Compress(prop, uuid);

    }


    @PostMapping(value = "/connect")
    public void ConnectUser(@RequestParam("email") String email,
                            @RequestParam("password") String password) {
        userService.ConnectUser(email, password);

    }

    @PostMapping(value = "/{id}/disconnect")
    public void DisconnectUser(@PathVariable("id") UUID uuid) {
        userService.DisconnectUser(uuid);

    }


}

