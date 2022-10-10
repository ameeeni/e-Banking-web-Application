package org.sid.ebankingbackend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
public class Customer
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    /**le client peut avoir un a plusieurs compte*/
    @OneToMany(mappedBy = "customer") /**Le clé etranger dans le bank account **/
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)/*C'est pas important de nous afficher la liste des BankAccount**/
    private List<BankAccount> bankAccountList;
}
