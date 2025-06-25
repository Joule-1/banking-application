package com.banking_application.controller;

import com.banking_application.dto.AmountDTO;
import com.banking_application.service.PDFGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/transactions/generatePdf")
public class PDFGeneratorController {

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @GetMapping("/{id}")
    public ResponseEntity<String> generatePdf(@PathVariable("id") Long id){
        return ResponseEntity.status(201).body(pdfGeneratorService.generatePdfReport(id));
    }

    @PostMapping
    public ResponseEntity<String> generatePdf(@RequestBody AmountDTO amountDTO){
        return ResponseEntity.status(201).body(pdfGeneratorService.generatePdfReport(amountDTO.getAmount()));
    }

}
