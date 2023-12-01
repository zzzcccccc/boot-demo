package cn.study.service.impl;

import cn.hutool.core.date.DateUtil;

import cn.study.service.TtMailService;
import cn.study.util.POIUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;


/**
 * @author zhangcengceng
 * 邮箱发送实现类
 */
@Service
@Component
public class TtTtMailServiceImpl implements TtMailService {

    @Autowired
    ResourceLoader resourceLoader;


    // 发送者别名
    private static final String SENDER_NAME = "job_zcc@163.com";
    // 发送邮箱地址
    private static final String SENDER_ADDRESS = "job_zcc@163.com";
    // 发送邮箱的授权码
    private static final String SENDER_PWD = "CCKTLRYWEUIINUVZ";
    // 密送的邮箱地址
    private static final String PRIVATE_ADDRESS = "job_zcc@163.com";
    /**
     * 发送邮件的环境对象
     */
    private static final Session EMAIL_SESSION = getEmailSession();

    /**
     * 信息（导入）
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public void getExcel(MultipartFile file) {
        try {
            List<Map<String, Object>> maps = POIUtil.readExcel(file);
            for (Map<String, Object> map : maps) {
                List<String[]> date = (List<String[]>) map.get("data");
                String result = "0";
                if (date != null) {
//                    List<ExcelSignpay> excelSignpays = new ArrayList<>();
                    for (String[] strings : date) {
                        Resource resource = resourceLoader.getResource("classpath:templates/zihao1115.html");
                        InputStream inputStream = resource.getInputStream();
                        String mailTemplate = IOUtils.toString(inputStream, "utf-8");
                        String send = strings[0];
                        String cc = strings[1];
                        String[] ccs = cc.split(",");
                        String title = strings[2];
                        mailTemplate = mailTemplate.replace("{正文占位符1}", strings[3]);
                        mailTemplate = mailTemplate.replace("{正文占位符2}", strings[4]);
                        mailTemplate = mailTemplate.replace("{正文占位符3}", strings[5]);
                        mailTemplate = mailTemplate.replace("{正文占位符4}", strings[6]);
                        mailTemplate = mailTemplate.replace("{正文占位符5}", strings[7]);
                        mailTemplate = mailTemplate.replace("{正文占位符6}", strings[8]);
                        mailTemplate = mailTemplate.replace("{正文占位符7}", strings[9]);
                        mailTemplate = mailTemplate.replace("{正文占位符8}", strings[10]);
                        mailTemplate = mailTemplate.replace("{正文占位符9}", strings[11]);
                        mailTemplate = mailTemplate.replace("{正文占位符10}", strings[12]);
                        String currentDate = DateUtil.format(new Date(), "yyyy年MM月dd日");
                        mailTemplate = mailTemplate.replace("系统自动生成", currentDate);
                        sendEmail(Arrays.asList(send), ccs, "[测试]" + title, mailTemplate, null);
//                        ExcelSignpay excelSignpay = new ExcelSignpay();
//                        excelSignpay.setAddressee(strings[0]);
//                        excelSignpay.setCc(strings[1]);
//                        excelSignpay.setTheme(strings[2]);
//                        excelSignpay.setValue1(strings[3]);
//                        excelSignpay.setValue2(strings[4]);
//                        excelSignpay.setValue3(strings[5]);
//                        excelSignpay.setValue4(strings[6]);
//                        excelSignpay.setValue5(strings[7]);
//                        excelSignpay.setValue6(strings[8]);
//                        excelSignpay.setValue7(strings[9]);
//                        excelSignpay.setValue8(strings[10]);
//                        excelSignpay.setValue9(strings[11]);
//                        excelSignpay.setValue10(strings[12]);
//                        excelSignpay.setPhone(strings[13]);
//                        excelSignpay.setMessage(strings[14]);
//                        excelSignpays.add(excelSignpay);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return "0";
    }


    /**
     * 批量发送电子邮件
     *
     * @param emailAddressList 邮箱地址
     * @param ccs            抄送人
     * @param content          邮件内容
     * @param title            邮件标题
     * @param fileList         附件
     * @throws Exception
     */
    public static synchronized void sendEmail(List<String> emailAddressList, String[] ccs,
                                              String title,
                                              String content,
                                              List<File> fileList) throws Exception {
        MimeMessage mimeMessage = getMimeMessage(emailAddressList, ccs, title, content);
        if (!CollectionUtils.isEmpty(fileList)) {
            // 处理附件
            Multipart multipart = getMultipart(fileList);

            //  添加邮件内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(content, "text/html;charset=UTF-8");
            // 将multipart对象放入message中
            multipart.addBodyPart(contentPart);
        }
        Transport.send(mimeMessage);

    }

    private static MimeMessage getMimeMessage(List<String> emailAddressList, String[] ccs,
                                              String title, String content) throws Exception {
        // 创建邮件消息
        MimeMessage message = new MimeMessage(EMAIL_SESSION);
        // 设置发件人
        message.setFrom(new InternetAddress(SENDER_ADDRESS, SENDER_NAME));
        // 设置收件人
        InternetAddress[] address = new InternetAddress[emailAddressList.size()];
        for (int i = 0; i < emailAddressList.size(); i++) {
            address[i] = new InternetAddress(emailAddressList.get(i));
        }
        message.setRecipients(Message.RecipientType.TO, address);
        // 获取抄送者信息
        if (ccs != null){
            // 为每个邮件接收者创建一个地址
            Address[] ccAdresses = new InternetAddress[ccs.length];
            for (int i=0; i<ccs.length; i++){
                ccAdresses[i] = new InternetAddress(ccs[i]);
            }
            // 将抄送者信息设置到邮件信息中，注意类型为Message.RecipientType.CC
            message.setRecipients(Message.RecipientType.CC, ccAdresses);
        }
        // 设置密送
        message.setRecipient(Message.RecipientType.BCC, new InternetAddress(PRIVATE_ADDRESS));
        // 设置邮件标题
        message.setSubject(title, "UTF-8");
        // 设置邮件的内容体
        message.setContent(content, "text/html;charset=UTF-8");
        // 设置发送时间
        message.setSentDate(new Date());
        return message;
    }

    private static Multipart getMultipart(List<File> fileList) {
//        if (CollectionUtils.isEmpty(fileList)) return null;
//        Multipart multipart = new MimeMultipart();
//        // 添加附件的内容
//        fileList.stream().parallel().forEach(file -> {
//            try {
//                BodyPart attachmentBodyPart = new MimeBodyPart();
//                DataSource source = new FileDataSource(file);
//                attachmentBodyPart.setDataHandler(new DataHandler(source));
//                // MimeUtility.encodeWord可以避免文件名乱码
//                attachmentBodyPart.setFileName(MimeUtility.encodeWord(file.getName()));
//                multipart.addBodyPart(attachmentBodyPart);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
        //return multipart;
        return  null;
    }

    private static Session getEmailSession() {
        // 配置发送邮件的环境属性
        Properties props = new Properties();
        //设置用户的认证方式
        props.setProperty("mail.smtp.auth", "true");
        //设置传输协议
        props.setProperty("mail.transport.protocol", "smtp");
        //设置发件人的SMTP服务器地址
        props.setProperty("mail.smtp.host", "smtp.163.com");
        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                return new PasswordAuthentication(SENDER_ADDRESS, SENDER_PWD);
            }
        };
        return Session.getInstance(props, authenticator);
    }
}
