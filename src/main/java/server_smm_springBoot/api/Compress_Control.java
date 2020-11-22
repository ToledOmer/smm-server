package server_smm_springBoot.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server_smm_springBoot.model.CompProps;
import server_smm_springBoot.service.CompressService;

import javax.validation.Valid;
import java.io.IOException;

@RequestMapping("api/v1/compress")

@RestController
public class Compress_Control {

    private final CompressService compressService;


    @Autowired
    public Compress_Control(CompressService compressService) {
        this.compressService = compressService;
    }

    @PostMapping
    public void CompresFile(@Valid @NonNull @RequestBody CompProps prop) throws IOException {
//    compressService.addProc(prop);
//        compressService.Compress(prop,"");

    }


}
