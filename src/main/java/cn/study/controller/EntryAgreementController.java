package cn.study.controller;

import cn.study.service.impl.EntryAgreementService;
import cn.study.util.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/entry")
public class EntryAgreementController {
    @Autowired
    EntryAgreementService entryAgreementService;


    @GetMapping("/getFileInfo")
    public String getFileInfo(HttpServletResponse resp, String fdId) {


        //这个map存的是ftl（word文件）里需要填入从数据库里查出来的字段部分
        //key是ftl文件里的${}部分，value是需要替换的数据
        //例如${student1Name},工具会去map里找key为student1Name的键值对，将其对应的值替换进去
        //例如studentList是从数据库里查出来的
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("jiafang", "studentList(i).getName()");
        dataMap.put("fading", "studentList(i).bbbbb()");
        dataMap.put("zhuzhi", "studentList(i).ccccc()");

        dataMap.put("yifang", "乙方");
        dataMap.put("shenfenzhenghao", "studentList(i).对赌地()");
        dataMap.put("tongxunzhuzhi", "studentList(i).ccccc()");
        dataMap.put("dianhua", "studentList(i).ccccc()");
        dataMap.put("youxiang", "邮箱");


        //写出word文件
        try {
            WordUtils.generateWord(resp, dataMap, "content.ftl", "C:\\workSpace\\学生名称.doc");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return "1";
    }


    @GetMapping("/getFileInfoPDF")
    public String getFileInfoPDF(HttpServletResponse resp, String fdId) {

        String fileInfo = entryAgreementService.getFileInfo(resp, fdId);
        return fileInfo;

    }

    @GetMapping("/downloadWord")
    public String  downloadWord(HttpServletResponse resp,String fdId) {

        entryAgreementService.downloadWord(resp,fdId);

        return "!";

    }

}
