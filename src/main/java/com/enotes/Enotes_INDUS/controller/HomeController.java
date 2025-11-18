package com.enotes.Enotes_INDUS.controller;

import com.enotes.Enotes_INDUS.exceptions.RegisterException;
import com.enotes.Enotes_INDUS.service.HomeService;
import com.enotes.Enotes_INDUS.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam Integer uid,@RequestParam String code) throws RegisterException {
        Boolean status=homeService.verifyUser(uid,code);
        if(status){
            return CommonUtil.createBuildResponseMessage("Registered Successfully", HttpStatus.CREATED);
        }
            return CommonUtil.createBuildResponseMessage("Invalid Link ", HttpStatus.BAD_REQUEST);
    }

}
