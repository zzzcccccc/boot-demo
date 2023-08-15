package cn.study.util;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class WordUtils {

    /**
     * 生成 word 文档方法
     *
     * @param dataMap      要填充的数据
     * @param templateName 模版名称
     * @param fileName     要输出的文件路径
     * @throws Exception 抛出的异常
     */
    public static void generateWord(HttpServletResponse response, Map<String, Object> dataMap,
                                    String templateName, String fileName) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/doc");
        response.setHeader("Content-Disposition",
                "inline;filename=" + URLEncoder.encode("test.doc", "UTF-8"));

        // 设置FreeMarker的版本和编码格式
        Configuration configuration = new Configuration(new Version("2.3.28"));
        configuration.setDefaultEncoding("UTF-8");

        // 设置FreeMarker生成Word文档所需要的模板的路径
        // configuration.setDirectoryForTemplateLoading(new File("/Users/xxx/Desktop/"));
        // 此处把word转成的ftl模版文件都放在 resources 下的 templates文件夹中
        configuration.setClassForTemplateLoading(WordUtils.class, "/templates");

        // 设置FreeMarker生成Word文档所需要的模板
        Template tem = configuration.getTemplate(templateName, "UTF-8");
        // 创建一个Word文档的输出流
//        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName)),
//                StandardCharsets.UTF_8));
        Writer out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(),
                StandardCharsets.UTF_8));
        // FreeMarker使用Word模板和数据生成Word文档
        tem.process(dataMap, out);
        out.flush();
        out.close();

    }
}