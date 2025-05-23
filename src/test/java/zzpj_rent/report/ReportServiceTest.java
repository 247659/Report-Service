package zzpj_rent.report;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import zzpj_rent.report.dtos.request.ContractRequest;
import zzpj_rent.report.exceptions.BadRequestException;
import zzpj_rent.report.services.ReportService;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    @InjectMocks
    private ReportService reportService;

    private ContractRequest validRequest() {
        ContractRequest request = new ContractRequest();
        request.setCity("Warszawa");
        request.setLandlordIdNumber("123456789");
        request.setTenantIdNumber("987654321");
        request.setArea(45.0);
        request.setFee(1500.0);
        request.setPayDay(15);
        request.setDeposit(500.0);
        return request;
    }


    @Test
    void shouldCreateContractSuccessfully() throws JRException {
        ContractRequest request = new ContractRequest();
        request.setCity("Warszawa");
        request.setLandlordIdNumber("123456789");
        request.setTenantIdNumber("987654321");
        request.setArea(50.0);
        request.setFee(2000.0);
        request.setPayDay(10);
        request.setDeposit(1000.0);

        JasperPrint jasperPrint = reportService.createContract(request);

        assertNotNull(jasperPrint);
    }

    @Test
    void shouldThrowWhenCityIsNull() {
        ContractRequest request = validRequest();
        request.setCity(null);

        assertThrows(BadRequestException.class, () -> reportService.createContract(request));
    }

    @Test
    void shouldThrowWhenCityIsEmpty() {
        ContractRequest request = validRequest();
        request.setCity("");

        assertThrows(BadRequestException.class, () -> reportService.createContract(request));
    }

    @Test
    void shouldThrowWhenTenantIdNumberTooShort() {
        ContractRequest request = validRequest();
        request.setTenantIdNumber("123");

        BadRequestException ex = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Tenant Id Number must be 9 characters", ex.getMessage());
    }

    @Test
    void shouldThrowWhenTenantIdNumberTooLong() {
        ContractRequest request = validRequest();
        request.setTenantIdNumber("12345678912");

        BadRequestException ex = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Tenant Id Number must be 9 characters", ex.getMessage());
    }

    @Test
    void shouldThrowWhenLandlordIdNumberTooShort() {
        ContractRequest request = validRequest();
        request.setLandlordIdNumber("123");

        BadRequestException ex = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Landlord Id Number must be 9 characters", ex.getMessage());
    }

    @Test
    void shouldThrowWhenLandlordIdNumberTooLong() {
        ContractRequest request = validRequest();
        request.setLandlordIdNumber("12345678912");

        BadRequestException ex = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Landlord Id Number must be 9 characters", ex.getMessage());
    }

    @Test
    void LandlordIdNumberIsNullorEmpty() {
        ContractRequest request = validRequest();
        request.setLandlordIdNumber(null);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Landlord Id Number is required", ex.getMessage());

        request.setLandlordIdNumber("");
        BadRequestException ex2 = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Landlord Id Number is required", ex2.getMessage());
    }

    @Test
    void TenantIdNumberIsNullorEmpty() {
        ContractRequest request = validRequest();
        request.setTenantIdNumber(null);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Tenant Id Number is required", ex.getMessage());

        request.setTenantIdNumber("");
        BadRequestException ex2 = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Tenant Id Number is required", ex2.getMessage());
    }

    @Test
    void areaIsLowerThan0() {
        ContractRequest request = validRequest();
        request.setArea((double) 0);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Area is lower or equal to 0", ex.getMessage());
    }

    @Test
    void areaIsNull() {
        ContractRequest request = validRequest();
        request.setArea(null);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Area is required", ex.getMessage());
    }

    @Test
    void shouldThrowWhenPayDayOutOfRange() {
        ContractRequest request = validRequest();
        request.setPayDay(50);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Pay day must be between 1 and 31", ex.getMessage());

        request.setPayDay(0);

        BadRequestException ex2 = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Pay day must be between 1 and 31", ex2.getMessage());
    }

    @Test
    void feeIsNull() {
        ContractRequest request = validRequest();
        request.setFee(null);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Fee is required", ex.getMessage());
    }

    @Test
    void feeIsLowerThan0() {
        ContractRequest request = validRequest();
        request.setFee((double) 0);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Fee is lower or equal to 0", ex.getMessage());
    }

    @Test
    void depositIsLowerThan0() {
        ContractRequest request = validRequest();
        request.setDeposit((double) -1);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Deposit is lower than 0", ex.getMessage());
    }

    @Test
    void depositIsNull() {
        ContractRequest request = validRequest();
        request.setDeposit(null);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> reportService.createContract(request));
        assertEquals("Deposit is required", ex.getMessage());
    }

//    @Test
//    void shouldThrowWhenReportFileIsMissing() {
//        ContractRequest request = validRequest();
//
//        // Możesz np. tymczasowo zmodyfikować createContract, by wskazywał na nieistniejący plik
//
//        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> reportService.createContract(request));
//        assertTrue(ex.getMessage().contains("Plik raportu nie został znaleziony"));
//    }



}
