package pp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pp.model.TrimProp;
import pp.service.TrimService;

import javax.validation.Valid;
import java.io.IOException;

@RequestMapping("api/v1/trim")

@RestController
public class Trim_Control {

    private final TrimService trimService;

    @Autowired
    public Trim_Control(TrimService trimService) {
        this.trimService = trimService;
    }


    @PostMapping
    public void TrimFile(@Valid @NonNull @RequestBody TrimProp prop) throws IOException {
//    compressService.addProc(prop);
        trimService.Trim(prop);

    }


}
