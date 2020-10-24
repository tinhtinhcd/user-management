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
//    @Id
//    @GeneratedValue
//    private Long id;
    private int lv;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "role_privilege", joinColumns = {
            @JoinColumn(name = "role_id")}, inverseJoinColumns = {
            @JoinColumn(name = "privilege_id")})
    private List<Privilege> privileges;

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }


//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getLv() {
//        return lv;
//    }
//
//    public void setLv(int lv) {
//        this.lv = lv;
//    }
//
//    public List<Privilege> getPrivileges() {
//        return privileges;
//    }
//
//    public void setPrivileges(List<Privilege> privileges) {
//        this.privileges = privileges;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
}
