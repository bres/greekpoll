package com.project.greekpoll.greekpoll.excel;

import com.project.greekpoll.greekpoll.entity.AnswersEntity;
import com.project.greekpoll.greekpoll.entity.QuestionsEntity;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.apache.poi.ss.util.CellUtil.createCell;

public class PollToExcel {



    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<QuestionsEntity> questionsEntities ;
    private List<AnswersEntity> answersEntities ;
    private String poll;

    public PollToExcel(List<QuestionsEntity> questionsEntities ,List<AnswersEntity> answersEntities , String poll ) {
        this.questionsEntities = questionsEntities;
        this.answersEntities = answersEntities;
        this.poll = poll;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        //set excel properties
        sheet = workbook.createSheet("ερωτηματολόγιο");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        int countColumn = 0;
        //header for program

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        createCell(row, 0, poll, style);
        row = sheet.createRow(1);
        createCell(row, countColumn++, " Ερώτηση", style);
        createCell(row, countColumn++, " Αποδεκτές Απαντήσεις", style);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 6));
        createCell(row, countColumn++, "Πιθανές Απαντήσεις", style);

    }


    private void writeDataLines() {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);


        // style BLUE
        CellStyle styleRed = workbook.createCellStyle();
        XSSFFont fontRed = workbook.createFont();
        fontRed.setFontHeight(14);
        fontRed.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        styleRed.setFont(fontRed);


        //write data for each record
        int rowCount = 2;
        int counter = 0;
        boolean foundPr = false;
        for (QuestionsEntity que : questionsEntities) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, que.getText() == null ? "" : que.getText().toString(),  style);
            createCell(row, columnCount++, que.getCategory() == 1 ? "Μια απάντηση" : "Περισσότερες Απαντήσεις",  styleRed);

            for (AnswersEntity ans : answersEntities){

                if (ans.getQuestionId().getId().equals(que.getId())){
                    createCell(row, columnCount++, ans.getAnswer() == null ? "" : ans.getAnswer(), style);
                }

            }


        }

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }


    public void export(HttpServletResponse response) throws IOException {

        writeHeaderLine();
        writeDataLines();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }



}
