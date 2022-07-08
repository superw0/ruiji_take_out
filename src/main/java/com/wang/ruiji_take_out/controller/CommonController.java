package com.wang.ruiji_take_out.controller;

import com.wang.ruiji_take_out.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reiji.path}")
    private String bathPath;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){

        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;

        File dir = new File(bathPath);
        if (!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(bathPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        FileInputStream fis = null;
        ServletOutputStream os = null;
        try {
            fis=new FileInputStream(new File(bathPath+name));
            os = response.getOutputStream();

            byte[] bytes=new byte[1024];
            int len=0;
            while ((len=fis.read(bytes))!=-1){
                os.write(bytes,0,len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
