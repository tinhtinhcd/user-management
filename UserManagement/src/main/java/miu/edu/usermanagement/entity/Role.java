package miu.edu.usermanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Role extends BaseModel {

    @Column(unique = true)
    private String name;
    private String description;
    private int lv;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "role_privilege", joinColumns = {
            @JoinColumn(name = "role_id")}, inverseJoinColumns = {
            @JoinColumn(name = "privilege_id")})
    private List<Privilege> privileges;
}
