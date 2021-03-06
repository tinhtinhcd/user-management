package miu.edu.usermanagement.controller;

import miu.edu.usermanagement.dto.CardDTO;
import miu.edu.usermanagement.dto.UserDTO;
import miu.edu.usermanagement.dto.response.ResponseMessage;
import miu.edu.usermanagement.service.CardService;
import miu.edu.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@CrossOrigin(maxAge = 60)
@RestController
public class CardManagement {
    private CardService cardService;
    private UserService userService;
    private MessageSource messageSource;

    @Autowired
    public CardManagement(CardService cardService, UserService userService, MessageSource messageSource){
        this.cardService = cardService;
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @PostMapping(value = "api/user/{username}/cards")
    public ResponseEntity<ResponseMessage> addNewCard(@PathVariable(name = "username") String userName, @Valid @RequestBody CardDTO cardDTO){
        String retMessage = cardService.addNewCard(userName, cardDTO);

        return ResponseEntity.ok(ResponseMessage.builder().message(retMessage).build());
    }

    @GetMapping(value = "/api/user/{username}/cards/{cardnumber}")
    public @ResponseBody CardDTO getCardByUserNameAndCardNumber(@PathVariable(name = "username") String userName, @PathVariable(name = "cardnumber") String cardNumber){
        return cardService.getCardInfo(userName, cardNumber);
    }

    @GetMapping(value = "api/user/{username}/cards")
    public @ResponseBody List<CardDTO> listAllCards(@PathVariable(name = "username") String userName){
        UserDTO dtoUser = userService.queryUserByUserName(userName);
        if(dtoUser != null) {
            return dtoUser.getCards();
        }
        else{
            return null;
        }
    }

    @PutMapping(value = "/api/user/{username}/cards/{cardnumber}")
    public ResponseEntity<ResponseMessage> updateCardByUserName(@PathVariable(name = "username") String userName, @PathVariable(name = "cardnumber") String cardNumber, @RequestBody CardDTO dtoCard){
        String retMessage = "";
        if(cardService.updateCard(userName, cardNumber, dtoCard)){
            retMessage = messageSource.getMessage("card.update.success", null, Locale.US);
        }
        else{
            retMessage = messageSource.getMessage("card.update.fail", null, Locale.US);
        }

        return ResponseEntity.ok(ResponseMessage.builder().message(retMessage).build());
    }

    @DeleteMapping(value = "api/user/{username}/cards/{cardnumber}")
    public ResponseEntity<ResponseMessage> removeCard(@PathVariable(name = "username") String userName, @PathVariable(name = "cardnumber") String cardNo){
        String retMessage = cardService.removeCard(userName, cardNo);
        return ResponseEntity.ok(ResponseMessage.builder().message(retMessage).build());
    }

    @PutMapping(value = "api/user/{username}/cards/default/{cardnumber}")
    public ResponseEntity<ResponseMessage> setDefaultCard(@PathVariable(name = "username") String userName, @PathVariable(name = "cardnumber") String cardNo){
        String retMessage = "";
        if(cardService.setDefaultCard(userName, cardNo)){
            retMessage = messageSource.getMessage("card.default.success", null, Locale.US);
        }
        else{
            retMessage = messageSource.getMessage("card.default.fail", null, Locale.US);
        }

        return ResponseEntity.ok(ResponseMessage.builder().message(retMessage).build());
    }
}
