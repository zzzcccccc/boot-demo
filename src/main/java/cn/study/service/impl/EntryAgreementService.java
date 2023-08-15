package cn.study.service.impl;


import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.deepoove.poi.XWPFTemplate;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author T016071
 * 入职保密协议
 * @date 2023-07-19
 */
@Service
@Component
public class EntryAgreementService {

    @Autowired
    ResourceLoader resourceLoader;


    /**
     * html 转 pdf
     *
     * @param resp
     * @param fdId
     * @return
     */
    public String getFileInfo(HttpServletResponse resp, String fdId) {
        try {
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("content-Type", "application/pdf");
            resp.setHeader("Content-Disposition",
                    "inline;filename=" + URLEncoder.encode("测试.pdf", "UTF-8"));

            String mailTemplate = readHtmlFile();
            mailTemplate = mailTemplate.replace("{正文占位符1}", "测试");
            mailTemplate = mailTemplate.replace("{正文占位符2}", "测试");
            mailTemplate = mailTemplate.replace("{正文占位符3}", "测试");
            mailTemplate = mailTemplate.replace("{正文占位符4}", "测试");
            mailTemplate = mailTemplate.replace("{正文占位符5}", "测试");
            mailTemplate = mailTemplate.replace("{正文占位符6}", "测试");
            mailTemplate = mailTemplate.replace("{正文占位符7}", "测试");
            mailTemplate = mailTemplate.replace("{正文占位符8}", "测试");

            //定义pdf文件尺寸，采用A4横切
            Document document = new Document(PageSize.A4, 25, 25, 15, 40);// 左、右、上、下间距
            //定义输出路径
            PdfWriter writer = PdfWriter.getInstance(document, resp.getOutputStream());


            writer.setInitialLeading(60f); //设置行距
            writer.addViewerPreference(PdfName.PRINTSCALING, PdfName.NONE);
            document.open();
//            Paragraph paragraph = new Paragraph();
//            paragraph.add().setFirstLineIndent(14);//14改为你想要的大小


            // CSS
            CSSResolver cssResolver = new StyleAttrCSSResolver();
            CssAppliers cssAppliers = new CssAppliersImpl(new XMLWorkerFontProvider() {
                @Override
                public Font getFont(String fontname, String encoding, boolean embedded,
                                    float size, int style, BaseColor color) {
                    try {
                        //用于中文显示的Provider
//                    BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

                        String fontPath = "C:/WINDOWS/FONTS/simfang.ttf";  // 字体
                        BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

                        return new Font(baseFont, size, Font.NORMAL | style);
                    } catch (Exception e) {
                        return super.getFont(fontname, encoding, size, style);
                    }
                }
            });

            //html
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            htmlContext.setImageProvider(new AbstractImageProvider() {
                @Override
                public Image retrieve(String src) {
                    //支持图片显示
                    int pos = src.indexOf("base64,");
                    try {
                        if (src.startsWith("data") && pos > 0) {
                            byte[] img = Base64.decode(src.substring(pos + 7));
                            return Image.getInstance(img);
                        } else if (src.startsWith("http")) {
                            return Image.getInstance(src);
                        }
                    } catch (BadElementException ex) {
                        return null;
                    } catch (IOException ex) {
                        return null;
                    }
                    return null;
                }

                @Override
                public String getImageRootPath() {
                    return null;
                }
            });


            // Pipelines
            PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
            HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

            // XML Worker
            // mailTemplate 字符串类型，动态赋值后转pdf
            XMLWorker worker = new XMLWorker(css, true);
            XMLParser p = new XMLParser(worker);
            p.parse(new ByteArrayInputStream(mailTemplate.getBytes()));

            //直接导入html 转 pdf
//            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
//                    new FileInputStream("C:\\ideawork\\hrbp\\hrplatformnew\\hr-platform\\hr-platform-api-web\\src\\main\\resources\\templates\\ruzhiDemo.html"),
//                    null,
//                    Charset.forName("UTF-8"));
//            document.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * html  转 字符串
     *
     * @return
     */
    private static String readHtmlFile() {
        StringBuffer textHtml = new StringBuffer();
        try {
            File file = new File("C:\\ideawork\\hrbp\\hrplatformnew\\hr-platform\\hr-platform-api-web\\src\\main\\resources\\templates\\pdfModel.html");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                textHtml.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            return null;
        }
        return textHtml.toString();
    }


    /**
     * 1根据word模板动态赋值  2转pdf
     *
     * @param resp
     * @param id
     */
    public void downloadWord(HttpServletResponse resp, String id) {
        try {
            // 获取文件流
            InputStream stream = EntryAgreementService.class.getClassLoader()
                    .getResourceAsStream("templates/model/wordModel.docx");
            // 填充数据
            Map<String, String> data = new HashMap<>();
            data.put("fd_bmxy_jfmc", "fffff.getName()");
            data.put("fd_bmxy_fddbr", "ffff.getReason()");

            XWPFTemplate template = XWPFTemplate.compile(stream).render(data);
            //输出流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] bytes = getBytes(template, outputStream);
            InputStream input = new ByteArrayInputStream(bytes);
            FileOutputStream outputStream1 = new FileOutputStream("C:\\workSpace\\DDDDD.pdf");
            wordTopdfByAspose(input, outputStream1);
            //  本地的pdf文件
            String path = "C:\\workSpace\\DDDDD.pdf";
            showPdf(resp, path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    private byte[] getBytes(XWPFTemplate template, ByteArrayOutputStream os) {
        byte[] bytes = new byte[0];
        try {
            template.write(os);
            bytes = os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                template.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }


    public static boolean wordTopdfByAspose(InputStream inputStream, OutputStream outputStream) {
        // 验证License 若不验证则转化出的pdf文档会有水印产生
        if (!getLicense()) {
            return false;
        }

        try {
            // 将源文件保存在com.aspose.words.Document中，具体的转换格式依靠里面的save方法
            com.aspose.words.Document doc = new com.aspose.words.Document(inputStream);

            // 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,EPUB, XPS, SWF 相互转换
            doc.save(outputStream, SaveFormat.PDF);
            System.out.println("word转换完毕");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    // 官方文档的要求 无需理会
    public static boolean getLicense() {
        boolean result = false;
        try {
            String s = "<License><Data><Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products><EditionType>Enterprise</EditionType><SubscriptionExpiry>20991231</SubscriptionExpiry><LicenseExpiry>20991231</LicenseExpiry><SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber></Data><Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature></License>";
            ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes());
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 预览pdf文件工具类
     *
     * @param response
     * @param fileName
     */
    public static void showPdf(HttpServletResponse response, String fileName) throws IOException {
        //添加后pdf 可在线预览
//        response.setCharacterEncoding("UTF-8");
//        response.setHeader("content-Type", "application/pdf");
//        response.setHeader("Content-Disposition",
//                "inline;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        response.setContentType("application/pdf");
        FileInputStream in = new FileInputStream(new File(fileName));
        OutputStream out = response.getOutputStream();
        byte[] b = new byte[1024];
        while ((in.read(b)) != -1) {
            out.write(b);
        }
        out.flush();
        in.close();
        out.close();
    }

}
