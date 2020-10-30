package miu.edu.usermanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User extends BaseModel{

    @Column(unique = true)
    @NotNull
    @Size(max=10, message = "{error.username.length}")
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean status;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="userId")
    private List<Address> lstAddress;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = {
            @JoinColumn(name = "user_id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id")})
    private List<Role> roles;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="userId")
    private List<Card> listCards;

}
