package miu.edu.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private List<AddressDTO> addresses;
    private List<CardDTO> cards;
}