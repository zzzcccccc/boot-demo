package cn.study.util;

import cn.hutool.core.util.StrUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.poi.ss.usermodel.CellType.*;

/**
 * excel读写工具类
 */

@Component
public class POIUtil {

    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    //图片及位置获取
    public static List<Map<String, Object>> readExcel(MultipartFile file) throws Exception {
        //创建返回对象，把每个sheet中的值作为一个Map，所有sheet作为一个集合返回
        List<Map<String, Object>> list = new ArrayList<>();
        // 创建Workbook
        Workbook wb = getWorkBook(file);
        // 创建sheet
        Sheet sheet = null;
        //获取excel sheet总数
        int sheetNumbers = wb.getNumberOfSheets();
        // 循环sheet
        for (int i = 0; i < sheetNumbers; i++) {
            // 获得当前sheet工作表
            sheet = wb.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            /*
             *	获取excel中数据
             */
            // 把每行中的值作为一个数组，所有行作为一个集合返回
            List<String[]> data = new ArrayList<String[]>();
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //循环除了第一行的所有行
            for (int rowNum = firstRowNum + 1; rowNum <=lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null || row.getPhysicalNumberOfCells()==0) {
                    continue;
                }
                //获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                //获取第一行的列数
                int lastCellNum = sheet.getRow(0).getPhysicalNumberOfCells();
                String[] cells = new String[lastCellNum];
                //循环当前行
                for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    if (cell!=null &&  cell.getCellType() != BLANK){
                        cells[cellNum] = getCellValue(cell);
                    }
                }
                if (cells.length>0 && !StrUtil.isAllBlank(cells)){
                    data.add(cells);
                }
            }
            if (data.size()>0 && data!=null){
                Map map = new HashMap();
                map.put("data", data);  //数据集
                list.add(map);
            }
        }
        return list;
    }

    //获得Workbook工作薄对象
    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith(xls)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(xlsx)) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
//            log.info(e.getMessage());
        }
        return workbook;
    }

    // 获取当前行当前列的数据
    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if (cell.getCellType() == NUMERIC) {
            cell.setCellType(STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()) {
            case NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: //空值
                cellValue = "";
                break;
            case ERROR: //故障
                cellValue = "";
                break;
            default:
                cellValue = "";
                break;
        }
        return cellValue;
    }
}
