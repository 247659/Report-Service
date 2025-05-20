package zzpj_rent.report.services;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import net.sf.jasperreports.engine.*;

@Service
public class ReportService {

    public JasperPrint createReport() throws JRException {
        System.out.println("Create Report");
        Long id = 324123L;
        double kwota = 100.50;
        double latitude = 45.0;
        double longitude = 13.0;
        String location = "Warszawa, Bemowo";
        int rooms = 4;
        String rentalType = "Długoterminowy";
        boolean available = true;
        Date nowDate = new Date();
        String avab;

        if (available) {
            avab = "Dostępny";
        } else {
            avab = "Niedostępny";
        }

        //JRBeanCollectionDataSource taskDataSource = new JRBeanCollectionDataSource(user);

        Map<String, Object> parameters = new HashMap<>();
        //parameters.put("resourcesDataSet3", taskDataSource);
        parameters.put("Data", nowDate);
         parameters.put("kwota", kwota);
        parameters.put("latitude", latitude);
        parameters.put("longitude", longitude);
        parameters.put("location", location);
        parameters.put("rooms", rooms);
        parameters.put("rentalType", rentalType);
        parameters.put("available", avab);
        parameters.put("id", id);


        String filePath = "reportsModels/Report.jrxml";
        InputStream reportStream = getClass().getClassLoader().getResourceAsStream(filePath);

        if (reportStream == null) {
            throw new IllegalArgumentException("Plik raportu nie został znaleziony: " + filePath);
        }

        JasperReport report = JasperCompileManager.compileReport(reportStream);
        return JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
    }
}
