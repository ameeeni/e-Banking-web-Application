package org.sid.ebankingbackend.dtos;

import lombok.Data;
import org.sid.ebankingbackend.enums.OperationType;

import java.util.Date;


@Data
public class BankAccountOperationDTO {

    private Long id;
    private Date date;
    private double amount;
    private OperationType operationType;
    private String description;
}
