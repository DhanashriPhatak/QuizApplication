package com.dhanashri.Response.Service.Controller;

import com.dhanashri.Response.Service.Module.Response;
import com.dhanashri.Response.Service.Service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("response")
public class ResponseController {

    @Autowired
    ResponseService responseService;

    @PostMapping("getScore")
    public ResponseEntity<String> getScore(@RequestBody List<Response> response)
    {
        return responseService.getScore(response);
    }

}
