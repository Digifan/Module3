package Entity;

import Louncher.MyMoney;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "operation")

public class Operation {

    @Id
    @GeneratedValue
    @Column(name ="id")
    private UUID uuid;
    @Column(name = "datemark")
    private LocalDateTime dateTime;
    @Column(name = "sum")
    private Double sum;
    @Column(name = "income")
    @Enumerated(EnumType.STRING)
    private MyMoney.Income incomeCategory;
    @Column(name = "expense")
    @Enumerated(EnumType.STRING)
    private MyMoney.Expense expenseCategory;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account_id")
    private Account account;

}

