package com.banking_application.service;

import com.banking_application.entity.Transaction;
import com.itextpdf.text.*;
import com.banking_application.exception.ApiException;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PDFGeneratorService {

    @Value("${Dir}")
    private String pdfDir;

    @Value("${reportFileName}")
    private String reportFileName;

    @Value("${reportFileNameDataFormat}")
    private String reportFileNameDateFormat;

    @Value("${localDateFormat}")
    private String localDateFormat;

    @Value("${table_noOfColumns}")
    private int noOfColumns;

    @Value("${table.columnNames}")
    private List<String> columnNames;

    @Autowired
    AccountService accountService;

    private static Font TIMES_ROMAN_LARGE = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL);
    private static Font TIMES_ROMAN_SMALL = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
    private static Font TIMES_ROMAN_EXTRA_EXTRA_SMALL = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.NORMAL);

    public String generatePdfReport(Long accountId) {
        if(accountId == null)
            throw new ApiException(404, "Account Id is required to generate PDF report");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(getPdfNameWithDate()));
            document.open();
            addDocTitle(document);
            createTable(document, noOfColumns, accountId);
            addFooter(document);
            document.close();
            return "PDF Report Generated Successfully";
        } catch (Exception e) {
            throw new ApiException(404, "Failed to generate PDF Report: " + e.getMessage());
        }
    }

    public String generatePdfReport(BigDecimal amount) {
        if(amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
            throw new ApiException(404, "Account must be not null and non-negative to generate PDF");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(getPdfNameWithDate()));
            document.open();
            addDocTitle(document);
            createTable(document, noOfColumns, amount);
            addFooter(document);
            document.close();
            return "PDF Report Generated Successfully";
        } catch (Exception e) {
            throw new ApiException(404, "Failed to generate PDF report: " + e.getMessage());
        }
    }

    private void addDocTitle(Document document) {
        try{
            Paragraph p1 = new Paragraph();
            leaveEmptyLine(p1, 1);
            p1.add(new Paragraph(reportFileName, TIMES_ROMAN_LARGE));
            leaveEmptyLine(p1, 1);
            p1.setAlignment(Element.ALIGN_CENTER);
            document.add(p1);
        }catch(DocumentException e){
            throw new ApiException(404, "Error adding document title: " + e.getMessage());
        }
    }

    private static void leaveEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private String getPdfNameWithDate() {
        String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern(reportFileNameDateFormat));
        return pdfDir + reportFileName + "-" + localDateString + ".pdf";
    }

    private void addFooter(Document document) throws DocumentException {
        try{
            Paragraph p2 = new Paragraph();
            leaveEmptyLine(p2, 5);

            String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern(localDateFormat));

            Paragraph line1 = new Paragraph("Report generated on " + localDateString, TIMES_ROMAN_SMALL);
            line1.setAlignment(Element.ALIGN_CENTER);
            p2.add(line1);

            leaveEmptyLine(p2, 1);

            Paragraph line2 = new Paragraph("------------------------End Of " + reportFileName + "--------------------", TIMES_ROMAN_SMALL);
            line2.setAlignment(Element.ALIGN_CENTER);
            p2.add(line2);

            document.add(p2);

        }catch(DocumentException e){
            throw new ApiException(404, "Error adding footer: " + e.getMessage());
        }


    }

    private void createTable(Document document, int noOfColumns, Long accountId) throws DocumentException {
        try{
            Paragraph paragraph = new Paragraph();
            leaveEmptyLine(paragraph, 3);
            document.add(paragraph);

            PdfPTable table = new PdfPTable(noOfColumns);

            for(int i = 0; i < noOfColumns; i++) {
                Phrase phrase = new Phrase(columnNames.get(i), TIMES_ROMAN_SMALL);
                PdfPCell cell = new PdfPCell(phrase);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.CYAN);
                leaveEmptyLine(new Paragraph(" "), 1);
                table.addCell(cell);
            }

            table.setHeaderRows(1);
            getDbData(table, accountId);
            document.add(table);

        } catch(Exception e){
            throw new ApiException(494, "Error creating PDF table: " + e.getMessage());
        }
    }

    private void createTable(Document document, int noOfColumns, BigDecimal amount) throws DocumentException {
        try{
            Paragraph paragraph = new Paragraph();
            leaveEmptyLine(paragraph, 3);
            document.add(paragraph);

            PdfPTable table = new PdfPTable(noOfColumns);

            for(int i = 0; i < noOfColumns; i++) {
                Phrase phrase = new Phrase(columnNames.get(i), TIMES_ROMAN_SMALL);
                PdfPCell cell = new PdfPCell(phrase);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.CYAN);
                leaveEmptyLine(new Paragraph(" "), 1);
                table.addCell(cell);
            }

            table.setHeaderRows(1);
            getDbData(table, amount);
            document.add(table);

        } catch (Exception e) {
            throw new ApiException(404, "Error creating PDF table: " + e.getMessage());
        }
    }

    private void getDbData(PdfPTable table, Long accountId){
        List<Transaction> transactionList = accountService.getAccountTransactionsByAccountId(accountId);

        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBackgroundColor(BaseColor.GRAY);

        for(Transaction transaction : transactionList){
            table.addCell(createStyledCell(String.valueOf(transaction.getTransaction_id()), TIMES_ROMAN_SMALL));
            table.addCell(createStyledCell(String.valueOf(transaction.getAmount()), TIMES_ROMAN_SMALL));
            table.addCell(createStyledCell(String.valueOf(transaction.getTimestamp()), TIMES_ROMAN_EXTRA_EXTRA_SMALL));
            table.addCell(createStyledCell(String.valueOf(transaction.getTransaction_type()), TIMES_ROMAN_SMALL));
            table.addCell(createStyledCell(String.valueOf(transaction.getFrom_account_id().getAccount_id()), TIMES_ROMAN_SMALL));
            table.addCell(createStyledCell(String.valueOf(transaction.getTo_account_id().getAccount_id()), TIMES_ROMAN_SMALL));
        }

    }

    private void getDbData(PdfPTable table, BigDecimal amount){
        List<Transaction> transactionList = accountService.getAccountTransactionsByAmount(amount);

        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBackgroundColor(BaseColor.GRAY);

        for(Transaction transaction : transactionList){
            table.addCell(createStyledCell(String.valueOf(transaction.getTransaction_id()), TIMES_ROMAN_SMALL));
            table.addCell(createStyledCell(String.valueOf(transaction.getAmount()), TIMES_ROMAN_SMALL));
            table.addCell(createStyledCell(String.valueOf(transaction.getTimestamp()), TIMES_ROMAN_EXTRA_EXTRA_SMALL));
            table.addCell(createStyledCell(String.valueOf(transaction.getTransaction_type()), TIMES_ROMAN_SMALL));
            table.addCell(createStyledCell(String.valueOf(transaction.getFrom_account_id().getAccount_id()), TIMES_ROMAN_SMALL));
            table.addCell(createStyledCell(String.valueOf(transaction.getTo_account_id().getAccount_id()), TIMES_ROMAN_SMALL));
        }

    }

    private PdfPCell createStyledCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

}
