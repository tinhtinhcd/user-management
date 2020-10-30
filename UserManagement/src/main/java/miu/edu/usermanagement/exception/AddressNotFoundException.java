package miu.edu.usermanagement.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(Long addressId) {
        super(String.format("Advice: The address ID %s doesn't exist", addressId));
    }
}
