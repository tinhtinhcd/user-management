package miu.edu.usermanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="userId")
    private List<Address> lstAddress;
}
