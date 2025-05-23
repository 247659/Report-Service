package zzpj_rent.report.services;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import net.sf.jasperreports.engine.*;
import zzpj_rent.report.dtos.request.ContractRequest;
import zzpj_rent.report.exceptions.BadRequestException;
import zzpj_rent.report.exceptions.NotFoundException;

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

    public JasperPrint createContract(ContractRequest request) throws JRException {
        System.out.println("Contract request: " + request);

        if (request.getCity() == null || request.getCity().isEmpty()) {
            throw new BadRequestException("City is required");
        } else if (request.getLandlordIdNumber() == null || request.getLandlordIdNumber().isEmpty()) {
            throw new BadRequestException("Landlord Id Number is required");
        } else if (request.getLandlordIdNumber().length() != 9) {
            throw new BadRequestException("Landlord Id Number must be 9 characters");
        } else if (request.getTenantIdNumber() == null || request.getTenantIdNumber().isEmpty()) {
            throw new BadRequestException("Tenant Id Number is required");
        } else if (request.getTenantIdNumber().length() != 9) {
            throw new BadRequestException("Tenant Id Number must be 9 characters");
        } else if (request.getArea() == null) {
            throw new BadRequestException("Area is required");
        } else if (request.getArea() <= 0) {
            throw new BadRequestException("Area is lower or equal to 0");
        } else if (request.getFee() == null) {
            throw new BadRequestException("Fee is required");
        } else if (request.getFee() <= 0) {
            throw new BadRequestException("Fee is lower or equal to 0");
        } else if (request.getPayDay() > 31 || request.getPayDay() < 1) {
            throw new BadRequestException("Pay day must be between 1 and 31");
        } else if (request.getDeposit() == null) {
            throw new BadRequestException("Deposit is required");
        } else if (request.getDeposit() < 0) {
            throw new BadRequestException("Deposit is lower than 0");
        }


        String location = "Warszawa ul. Zamenhofa 31/15";
        int rooms = 4;
        Date nowDate = new Date();
        LocalDate sinceLocalDate = LocalDate.now().plusDays(1);
        LocalDate untilLocalDate = LocalDate.now().plusDays(5);

        Date sinceDate = java.util.Date.from(sinceLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date untilDate = java.util.Date.from(untilLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());



        //JRBeanCollectionDataSource taskDataSource = new JRBeanCollectionDataSource(user);

        Map<String, Object> parameters = new HashMap<>();
        //parameters.put("resourcesDataSet3", taskDataSource);
        parameters.put("Data", nowDate);
        parameters.put("location", location);
        parameters.put("rooms", rooms);
        parameters.put("city", request.getCity());
        parameters.put("landlordName", "Dominik Gałkowski");
        parameters.put("landlordCity", "Warszawa");
        parameters.put("landlordAddress", "Sasanek 35");
        parameters.put("landlordIdNumber", request.getLandlordIdNumber());
        parameters.put("tenantName", "Marek Zawadzki");
        parameters.put("tenantCity", "Warszawa");
        parameters.put("tenantAddress", "Ciepła 35");
        parameters.put("tenantIdNumber", request.getTenantIdNumber());
        parameters.put("area", request.getArea());
        parameters.put("sinceDate", sinceDate);
        parameters.put("untilDate", untilDate);
        parameters.put("fee", request.getFee());
        parameters.put("payDay", request.getPayDay());
        parameters.put("transmissionDate", sinceDate);
        parameters.put("deposit", request.getDeposit());



        String filePath = "reportsModels/Contract.jrxml";
        InputStream reportStream = getClass().getClassLoader().getResourceAsStream(filePath);

        if (reportStream == null) {
            throw new NotFoundException("Plik raportu nie został znaleziony: " + filePath);
        }

        JasperReport report = JasperCompileManager.compileReport(reportStream);
        return JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
    }
}
