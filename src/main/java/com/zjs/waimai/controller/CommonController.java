package com.zjs.waimai.controller;

import com.zjs.waimai.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载处理
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${waimai.path}")
    private String basepath;

    @PostMapping("/upload")
    //参数的名字 file 不能乱起，要和前台请求的参数一致
    //file是一个临时文件，需要转存到指定位置，否则本次请求完成后将会被删除
    public R<String> upload(MultipartFile file) {
        log.info(file.toString());
        //获取原始文件名
        String originFilename = file.getOriginalFilename();
        String suffix=originFilename.substring(originFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;//dfsdfdfd.jpg
        //创建一个目录对象
        File dir = new File(basepath);
        //判断当前目录是否存在
        if(!dir.exists()){
            //目录不存在，需要创建
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basepath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        //输入流，获取文件内容
        try {
            FileInputStream fileInputStream =new FileInputStream(new File(basepath+name));
            //输出流，将文件写回浏览器，用于展示
            ServletOutputStream outputStream=response.getOutputStream();
            response.setContentType("image/jpeg");
            int len=0;
            byte[] bytes=new byte[1024];
            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
