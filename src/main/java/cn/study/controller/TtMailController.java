package cn.study.controller;

import cn.study.dto.MailDTO;
import cn.study.mapper.TtMailMapper;
import cn.study.model.ExcelSignpay;
import cn.study.service.TtMailService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("email")
public class TtMailController {

    @Autowired
    private  TtMailService ttMailService;


    @PostMapping
    public void send(@RequestBody MailDTO mailDTO) {
//        ttMailService.send(mailDTO);


    }

    @RequestMapping(value = "/getExcel", method = RequestMethod.GET)
    public void getExcel(MultipartFile file) {
        ttMailService.getExcel(file);
    }

//    public static void main(String[] args) {
//        //时间差
//        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date star = dft.parse("2020-01-01");//开始时间
//            Date endDay=dft.parse("2020-01-10");//结束时间
//
//            Float a = 0f;
//
//            String starHm = "08:30";
//            String endHm = "17:30";
//
//            String hm1 = "08:30";
//            String hm2 = "11:30";
//            String hm3 = "17:30";
//
//            Long starTime=star.getTime();
//            Long endTime=endDay.getTime();
//            Long num=endTime-starTime;//时间戳相差的毫秒数
//            Float day = Float.valueOf((num/24/60/60/1000)+1);
//            System.out.println("相差天数为："+num/24/60/60/1000);//除以一天的毫秒数
//
//            if (starHm.equals(hm1)){
//                a=0f;
//            }else if(starHm.equals(hm2)){
//                a=0.5f;
//            }
//
//            if (endHm.equals(hm3)){
//                a=0f;
//            }else if(endHm.equals(hm2)){
//                a=0.5f;
//            }
//            Float days = day -a;
//            System.out.println(days);
//
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

}