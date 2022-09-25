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
@Table(name = "customer")
public class Customer implements Serializable {
    @Id
    @Column(name = "id")
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customer")
    @ToString.Exclude
    private Set <Account> accounts = new HashSet<>();
}
