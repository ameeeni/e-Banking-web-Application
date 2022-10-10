package org.sid.ebankingbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity /**On ajoute l'entité puisque on utilise la propriété Single table dans l'inheritance il faut ajouter la column de interset Rate*/
@DiscriminatorValue("SAV")
@Data @AllArgsConstructor @NoArgsConstructor
public class SavingAccount extends BankAccount {
    private double interestRate;
}
