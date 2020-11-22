package server_smm_springBoot.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server_smm_springBoot.model.CombProp;
import server_smm_springBoot.service.CombineService;

import javax.validation.Valid;

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
