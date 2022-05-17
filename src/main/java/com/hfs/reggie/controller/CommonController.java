package com.hfs.reggie.controller;

import com.hfs.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/common")
@Slf4j

public class CommonController {
      @PostMapping("/upload")
                                    //  参数名需要和请求名相同
    public R<String> upload(MultipartFile file){
       //临时文件 需要转存储 否则会失效
        log.info(file.toString());
        return null;
    }


}
