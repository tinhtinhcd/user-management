package miu.edu.usermanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Card extends BaseModel{

    private String cardNumber;
    private String type;
    private Date expiredDate;
    private boolean isDefault;
}
