package zzpj_rent.report.controllers;

import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzpj_rent.report.services.ReportService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/report")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReportController {

    @Autowired
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateReport() {
        try {
            JasperPrint jasperPrint;
            jasperPrint = reportService.createReport();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .body(pdfBytes);
        } catch (JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Błąd podczas generowania raportu".getBytes());
        }
    }
}
