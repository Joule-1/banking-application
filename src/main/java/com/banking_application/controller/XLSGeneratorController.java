package com.banking_application.controller;

import com.banking_application.dto.AmountDTO;
import com.banking_application.service.PDFGeneratorService;
import com.banking_application.service.XLSGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/transactions/generateXls")

public class XLSGeneratorController {
    @Autowired
    private XLSGeneratorService xlsGeneratorService;

    @GetMapping("/{id}")
    public ResponseEntity<String> generatePdf(@PathVariable("id") Long id){
        return ResponseEntity.status(201).body(xlsGeneratorService.generateXlsReport(id));
    }

    @PostMapping
    public ResponseEntity<String> generatePdf(@RequestBody AmountDTO amountDTO){
        return ResponseEntity.status(201).body(xlsGeneratorService.generateXlsReport(amountDTO.getAmount()));
    }
}
