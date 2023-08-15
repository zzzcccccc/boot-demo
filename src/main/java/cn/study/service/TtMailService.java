package cn.study.service;

import cn.study.dto.MailDTO;
import org.springframework.web.multipart.MultipartFile;


public interface TtMailService {

    void getExcel(MultipartFile file);

//    void send(MailDTO mailDTO);
}
