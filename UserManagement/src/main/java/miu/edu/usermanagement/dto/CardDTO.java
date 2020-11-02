package miu.edu.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    @Size(min=16, max=16, message = "{error.length.minmax}")
    private String cardNumber;
    @Size(min=2, max=10, message = "{error.length.minmax}")
    private String name;
    @Size(min=3, max=3, message = "{error.length.minmax}")
    private String cvv;
    @Pattern(regexp= "^((0[1-9])|(1[0-2]))(\\d{2})$", message = "{error.expire.format}")
    private String expiredDate;
    private boolean isDefault;
}
