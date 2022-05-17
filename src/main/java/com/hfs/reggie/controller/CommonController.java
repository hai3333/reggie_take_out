package com.hfs.reggie.controller;

import com.hfs.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j

public class CommonController {
    @Value("${reggie.path}")
    private String basePath;


    @PostMapping("/upload")//  参数名需要和请求名相同
    public R<String> upload(MultipartFile file) {
        //临时文件 需要转存储 否则会失效
        log.info(file.toString());
        //原始文件名
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));

        //使用uuid重新生成文件名
        String FileName = UUID.randomUUID().toString() + suffix;

        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        //转存
        try {
            file.transferTo(new File(basePath + FileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(FileName);
    }
    /*
     * 文件下载
     *
     * */

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        //输入流
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
