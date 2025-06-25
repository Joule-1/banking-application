package com.banking_application.service;

import com.banking_application.entity.Transaction;
import com.banking_application.exception.ApiException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class XLSGeneratorService {

    @Value("${Dir}")
    private String xlsDir;

    @Value("${reportFileName}")
    private String reportFileName;

    @Value("${reportFileNameDataFormat}")
    private String reportFileNameDateFormat;

    @Value("${table.columnNames}")
    private List<String> columnNames;

    @Autowired
    AccountService accountService;

    public String generateXlsReport(Long accountId){
        if(accountId == null){
            throw new ApiException(400, "Account Id is required to generate Excel Report");
        }

        try(Workbook workbook = new HSSFWorkbook();){
            Sheet sheet = workbook.createSheet("Transaction Report");
            createHeader(sheet);
            writeData(sheet, accountService.getAccountTransactionsByAccountId(accountId));
            autoSizeColumns(sheet);
            saveToFile(workbook);
            return "Excel Report Generated Successfully";
        } catch (IOException e){
            throw new ApiException(404, "Error generating Excel Report: " + e.getMessage());
        }
    }

    public String generateXlsReport(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ApiException(400, "Amount must be non-null and non-negative");
        }

        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Transaction Report");
            createHeader(sheet);
            writeData(sheet, accountService.getAccountTransactionsByAmount(amount));
            autoSizeColumns(sheet);
            saveToFile(workbook);
            return "Excel Report Generated Successfully";
        } catch (IOException e) {
            throw new ApiException(500, "Error generating Excel report: " + e.getMessage());
        }
    }

    private void createHeader(Sheet sheet){
        Row header = sheet.createRow(0);
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        for(int i = 0; i < columnNames.size(); i++){
            Cell cell = header.createCell(i);
            cell.setCellValue(columnNames.get(i));
            cell.setCellStyle(headerStyle);
        }
    }

    private void writeData(Sheet sheet, List<Transaction> transactions){
        int rowCount = 1;
        for(Transaction t : transactions){
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(t.getTransaction_id());
            row.createCell(1).setCellValue(t.getAmount().doubleValue());
            row.createCell(2).setCellValue(t.getTimestamp().toString());
            row.createCell(3).setCellValue(t.getTransaction_type().toString());
            row.createCell(4).setCellValue(t.getFrom_account_id().getAccount_id());
            row.createCell(5).setCellValue(t.getTo_account_id().getAccount_id());
        }
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < columnNames.size(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void saveToFile(Workbook workbook) throws IOException {
        String filePath = xlsDir + reportFileName + "-" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(reportFileNameDateFormat)) + ".xls";

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
    }


}
