package cn.study.controller;

import org.apache.poi.util.IOUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@EnableAutoConfiguration
@RequestMapping("/a")
public class TtHtmlController {

    @RequestMapping("/getHtml")
    public String getHtml() {
        return "zihao1115";
    }

    @RequestMapping(value = "/pdfDownload", method = RequestMethod.GET)
    public ResponseEntity download() throws IOException {
        File file = new File("C:\\aaaa\\新建.pdf");

        InputStream in = new FileInputStream(file);

        final HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/pdf");

        headers.add("Content-Disposition", "attachment; filename=" + file.getName() );

        return new ResponseEntity<>(IOUtils.toByteArray(in), headers, HttpStatus.OK);

    }
}
