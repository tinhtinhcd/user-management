package miu.edu.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    @Size(min=10, max=10, message = "{error.length.minmax}")
    private String cardNumber;
    @Size(min=2, max=10, message = "{error.length.minmax}")
    private String type;
    private Date expiredDate;
    private boolean isDefault;
}
