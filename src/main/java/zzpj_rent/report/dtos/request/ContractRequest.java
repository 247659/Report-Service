package zzpj_rent.report.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractRequest {
    private String city;
    private String landlordIdNumber;
    private String tenantIdNumber;
    private Double area;
    private Double fee;
    private int payDay;
    private Double deposit;
}
