package Entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "account")

public class Account implements Serializable {

    @Id
    @Column(name = "id")
    private String iban;
    @Column(name ="amount")
    private Double amount;
    @Column(name = "bank")
    private String bank;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    private Customer customer;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
    @ToString.Exclude
    private Set<Operation> operations = new HashSet<>();

}
