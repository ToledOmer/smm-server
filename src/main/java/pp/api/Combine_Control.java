package pp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pp.model.CombProp;
import pp.model.CompProps;
import pp.service.CombineService;
import pp.service.CompressService;

import javax.validation.Valid;
import java.io.IOException;

@RequestMapping("api/v1/combine")

@RestController
public class Combine_Control {
//this is combine class
    private final CombineService combineService;

    @Autowired
    public Combine_Control(CombineService combineService) {
        this.combineService = combineService;
    }

    @PostMapping
    public void CombineFiles(@Valid @NonNull @RequestBody CombProp prop)  {
    combineService.Combine(prop);
    }


}
