package org.sid.ebankingbackend.entities;
import org.sid.ebankingbackend.entities.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.ebankingbackend.enums.AccountStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE",  length=4 , discriminatorType = DiscriminatorType.STRING )
@Data @NoArgsConstructor @AllArgsConstructor
public  class   BankAccount {
    /**Abstract : si on utilise la stratégie table per class on n'a pas besoin de crée une table de bank account
     * dans la BD puisque on a soit un compte courant soit un compte épargne d'ou il faut ajouter le abstract
     */
    @Id
    private String id;
    private double balance;
    private Date dateCreation;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount" , fetch = FetchType.LAZY)
    List<BankAccountOperation> bankAccountOperationList;
}
