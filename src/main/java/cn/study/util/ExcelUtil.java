package cn.study.util;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;

/**
 * @author T016071
 * @data 2023/08/09
 * excel导出
 */
public class ExcelUtil {

    static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 生成Excel
     *
     * @param response
     * @param name     文件名
     * @param list     接口返回的数据，要导出的list
     * @param cla      excel模板（list对应的类）
     * @param <U>
     */
    public static <U> RES exportExcel(HttpServletResponse response, String name, Object list, Class<U> cla) {
        try {
            if (CollectionUtil.isEmpty((Collection<?>) list)) {
                logger.info(">>>>>>数据为空：",name);
                return RES.no(CommonConstants.FAIL, "数据为空");
            }
            String fileName = URLEncoder.encode(name, "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), cla).sheet("Sheet1")
                    .doWrite((Collection<?>) list);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return RES.ok(CommonConstants.SUCCESS, "success", "导出成功");
    }
}
