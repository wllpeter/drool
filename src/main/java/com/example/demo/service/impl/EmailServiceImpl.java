package com.example.demo.service.impl;

import com.example.demo.modal.People;
import com.example.demo.service.EmailService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by 86131 on 2020/1/13.
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    public static void send(){
        EmailServiceImpl service = new EmailServiceImpl();
        service.sendSimpleMail();
    }

    public void sendSimpleMail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("zhushuiyang@paixueche.com");
        message.setSubject("测试");
        message.setText("朱水洋是富二代吗？");
        message.setFrom("13162579298@163.com");
        if(mailSender == null){
            mailSender = new JavaMailSenderImpl();
            mailSender.setUsername("13162579298@163.com");
            mailSender.setHost("smtp.163.com");
            mailSender.setPassword("wll1994629520");
            mailSender.setPort(25);
            mailSender.setProtocol("smtp");
            mailSender.setDefaultEncoding("UTF-8");
        }
        mailSender.send(message);
    }


    public  Boolean sendEmailHaveAttachment() {
        Boolean result = false;
        List<People> peoples = new ArrayList<>();
        People people1 = new People();
        people1.setId(1);
        people1.setName("李光洁");
        people1.setSchool("清华大学");
        People people2 = new People();
        people2.setId(2);
        people2.setName("李凯旋");
        people2.setSchool("北京大学");
        peoples.add(people1);
        peoples.add(people2);

        try {
            InputStream is = export(peoples, "测试");

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("1282418933@qq.com");
            helper.setTo("1282418933@qq.com");
            helper.setSubject("主题：有附件");
            helper.setText("来自wllpeter的邮件");
            DataSource source = new ByteArrayDataSource(is, "application/msexcel");
            helper.addAttachment("测试.xls", source);
            mailSender.send(mimeMessage);
            result = true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    public  <T> InputStream export(List<T> list, String title) {
        InputStream is = null;
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet("测试");
        //设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(10);
        //生成一个样式
        HSSFCellStyle styleHeader = getCellStyle(workbook);
        styleHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleHeader.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleHeader.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //生成一个字体
        HSSFFont font = getFont(workbook);
        //把字体应用到当前样式
        styleHeader.setFont(font);

        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);

        List<String> header = new ArrayList<>(Arrays.asList("ID", "姓名", "学校名称"));

        //生成表格标题
        //标题第一行
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 300);
        HSSFCell cell = null;
        for (int i = 0; i < header.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(styleHeader);
            HSSFRichTextString text = new HSSFRichTextString(header.get(i));
            cell.setCellValue(text);
        }


        //利用反射，根据JavaBean属性的先后顺序，动态调用get方法得到属性的值
        Object value = null;
        String fieldName = "";
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            T t = list.get(i);
            //利用反射，根据JavaBean属性的先后顺序，动态调用get方法得到属性的值
            List<Field> origlist = getAllField(list.get(i).getClass());
            Field[] fields = new Field[origlist.size()];
            origlist.toArray(fields);

            try {
                int m = 0;
                for (int j = 0; j < header.size(); j++) {

                    cell = row.createCell(m++);
                    cell.setCellStyle(style);
                    fieldName = fields[j].getName();
                    String methodName = "get" + fields[j].getName().substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method getMethod = t.getClass().getMethod(methodName);
                    value = getMethod.invoke(t);

                    String type1 = getMethod.getReturnType().toString();
                    if ("class java.lang.String".equals(type1)) {
                        cell.setCellValue(value.toString());
                    } else if ("class java.lang.Integer".equals(type1) || "class java.lang.Double".equals(type1) || "class java.math.BigDecimal".equals(type1)) {
                        cell.setCellValue(new Double(value.toString()));
                    } else if (value instanceof Date) {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        value = df.format(value);
                        cell.setCellValue(value.toString());

                    }


                }
            } catch (Exception e) {
            }
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            baos.flush();
            byte[] bt = baos.toByteArray();
            is = new ByteArrayInputStream(bt, 0, bt.length);
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }


    /**
     * 获取包括父类的成员变量
     *
     * @param tempClass
     * @return
     */
    private  List<Field> getAllField(Class tempClass) {

        List<Field> fieldList = new ArrayList<>();
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }
        return fieldList;
    }

    /**
     * @param @param  workbook
     * @param @return
     * @return HSSFCellStyle
     * @throws
     * @Title: getCellStyle
     * @Description: 获取单元格格式
     */
    public  HSSFCellStyle getCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        return style;
    }


    /**
     * @param @param  workbook
     * @param @return
     * @return HSSFFont
     * @throws
     * @Title: getFont
     * @Description: 生成字体样式
     */
    public  HSSFFont getFont(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.WHITE.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        return font;
    }
}
