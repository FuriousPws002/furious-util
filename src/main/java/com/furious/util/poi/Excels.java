package com.furious.util.poi;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.furious.util.Throwables;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 基于poi库excel工具类
 */
public enum Excels {
    ;

    private static final int SHEET_MAX_COUNT = 1000000;

    public static <T> List<T> transfer(InputStream input, Class<T> clazz, List<String> fields, int sheetIndex, int startRow) {
        XSSFWorkbook xssf;
        List<T> list = new LinkedList<>();
        try {
            xssf = new XSSFWorkbook(input);
            Sheet sheet = xssf.getSheetAt(sheetIndex);

            int rowNum = sheet.getLastRowNum();

            T obj;

            Row header = sheet.getRow(1);
            short columnsNum = header.getLastCellNum();
            if (CollectionUtils.isEmpty(fields)) {
                fields = new ArrayList<>();
                for (short i = 0; i < columnsNum; i++) {
                    fields.add(header.getCell(i).getStringCellValue());
                }
            }

            for (int i = startRow; i <= rowNum; i++) {
                Row row = sheet.getRow(i);
                if (Objects.isNull(row)) {
                    continue;
                }
                obj = clazz.newInstance();
                int falseCount = 0;
                for (short j = 0; j < columnsNum; j++) {
                    if (!setField(obj, fields.get(j), row.getCell(j))) {
                        falseCount++;
                    }
                }

                // 只要有一个Cell有值且设置成功
                if (falseCount < columnsNum) {
                    list.add(obj);
                }
                // else {} // 该行为空白行或者fields和T字段名称不对应
            }
        } catch (IllegalAccessException |
                IOException |
                InstantiationException e) {
            Throwables.raise(e);
        } finally {
            try {
                input.close();
            } catch (IOException ignored) {
            }
        }
        return list;
    }

    /**
     * 设置字段值，Cell有值且设置成功返回true，否则返回false
     */
    private static boolean setField(Object obj, String field, Cell cell) throws IllegalAccessException {
        if (cell == null) {
            return false;
        }

        Class clazz = obj.getClass();
        Field declaredField = field(clazz, field);

        if (declaredField == null) {
            return false;
        }

        declaredField.setAccessible(true);
        CellType cellType = cell.getCellType();

        switch (cellType) {
            case STRING:
                String val = StringUtils.trimToNull(cell.getStringCellValue());
                if (null == val) {
                    return false;
                }
                declaredField.set(obj, val);
                break;
            case NUMERIC: {
                double numeric = cell.getNumericCellValue();
                Class<?> fieldType = declaredField.getType();
                if (fieldType == byte.class || fieldType == Byte.class) {
                    declaredField.set(obj, (byte) numeric);
                } else if (fieldType == short.class || fieldType == Short.class) {
                    declaredField.set(obj, (short) numeric);
                } else if (fieldType == int.class || fieldType == Integer.class) {
                    declaredField.set(obj, (int) numeric);
                } else if (fieldType == long.class || fieldType == Long.class) {
                    declaredField.set(obj, (long) numeric);
                } else if (fieldType == float.class || fieldType == Float.class) {
                    declaredField.set(obj, (float) numeric);
                } else if (fieldType == double.class || fieldType == Double.class) {
                    declaredField.set(obj, numeric);
                } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                    declaredField.set(obj, BooleanUtils.toBoolean((int) numeric));
                } else if (CharSequence.class.isAssignableFrom(fieldType)) {
                    declaredField.set(obj, String.valueOf(numeric));
                }
                break;
            }
            case BOOLEAN:
                declaredField.set(obj, cell.getBooleanCellValue());
                break;
            default:
                return false;
        }

        return true;
    }

    private static Field field(Class clazz, String name) {
        Field field = null;
        while (clazz != Object.class && field == null) {
            try {
                field = clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return field;
    }

    public static void export(String excelName, List<?> list, String[] columns, String[] fields,
                              HttpServletRequest request, HttpServletResponse response) {
        //待导出的总条数
        int size;
        if (list == null || (size = list.size()) == 0) {
            return;
        }
        if (columns.length != fields.length) {
            return;
        }

        //统计sheet的个数
        int var1 = size % SHEET_MAX_COUNT;
        int sheetCount = var1 == 0 ? size / SHEET_MAX_COUNT : size / SHEET_MAX_COUNT + 1;

        InputStream input = null;
        ServletOutputStream output = null;
        try {
            //根据模板excel创建workbook工作薄
            input = Excels.class.getClassLoader().getResourceAsStream("META-INF/resources/export.xlsx");
            XSSFWorkbook xssf = new XSSFWorkbook(input);
            SXSSFWorkbook wb = new SXSSFWorkbook(xssf);

            //设置边框
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.MEDIUM);
            cellStyle.setBorderBottom(BorderStyle.MEDIUM);
            cellStyle.setBorderLeft(BorderStyle.MEDIUM);
            cellStyle.setBorderRight(BorderStyle.MEDIUM);

            Sheet sheet;
            //每个sheet中起止条数
            int fromfix, tofix;
            for (int k = 1; k <= sheetCount; k++) {
                //由于模板中只有一个sheet，只有超过1个sheet时，才会创建新的sheet
                if (k >= 2) {
                    wb.createSheet("Sheet" + k);
                }
                sheet = wb.getSheetAt(k - 1);

                //填充表头
                Row head = sheet.createRow(0);
                for (int i = 0; i < columns.length; i++) {
                    Cell cell = head.createCell(i);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(columns[i]);
                }

                //填充数据，确定每个sheet中遍历的起止点
                fromfix = (k - 1) * SHEET_MAX_COUNT;
                if (k == sheetCount) {
                    tofix = fromfix + var1;
                } else {
                    tofix = fromfix + SHEET_MAX_COUNT;
                }
                for (int i = fromfix; i < tofix; i++) {
                    Object obj = list.get(i);
                    //创建行时，i-fromfix保证了下一个sheet是第一行是从最顶上开始的
                    Row row = sheet.createRow(i - fromfix + 1);
                    for (int j = 0; j < fields.length; j++) {
                        Cell cell = row.createCell(j);
                        cell.setCellStyle(cellStyle);
                        String field = fields[j];
                        Field declaredField = field(obj.getClass(), field);
                        declaredField.setAccessible(true);
                        /*
                         * 通过反射获取对象中属性所对应的值，这里只处理了空对象、字符串对象、时间对象，
                         * 若为时间类型，转换成固定的时间格式，若为其他非空类型，统一转为字符串
                         */
                        Object value = declaredField.get(obj);
                        String cellValue = null;
                        if (value != null) {
                            if (value instanceof Date) {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                cellValue = format.format(value);
                            } else {
                                cellValue = String.valueOf(value);
                            }
                        }
                        cell.setCellValue(cellValue);
                    }
                }
            }
            //写到浏览器
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            String filename = excelName;
            String agent = request.getHeader("USER-AGENT");
            if (agent != null && agent.trim().length() > 0 && agent.toLowerCase().indexOf("firefox") > 0) {
                filename = "=?UTF-8?B?" + Arrays.toString(Base64.getEncoder().encode(filename.getBytes())) + "?=";
            } else {
                filename = URLEncoder.encode(filename, "utf-8");
            }
            response.addHeader("Content-Disposition", "attachment;filename=" + filename + ".xlsx");
            output = response.getOutputStream();
            wb.write(output);
            output.flush();
            response.flushBuffer();
        } catch (IllegalAccessException |
                IOException e) {
            Throwables.raise(e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ignored) {
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

}
