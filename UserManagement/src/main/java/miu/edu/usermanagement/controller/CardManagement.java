package miu.edu.usermanagement.controller;

import miu.edu.usermanagement.dto.CardDTO;
import miu.edu.usermanagement.dto.UserDTO;
import miu.edu.usermanagement.service.CardService;
import miu.edu.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
    public @ResponseBody String addNewCard(@PathVariable(name = "username") String userName, @Valid @RequestBody CardDTO cardDTO){
        String retMessage = cardService.addNewCard(userName, cardDTO);

        return retMessage;
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
    public String updateCardByUserName(@PathVariable(name = "username") String userName, @PathVariable(name = "cardnumber") String cardNumber, @RequestBody CardDTO dtoCard){
        if(cardService.updateCard(userName, cardNumber, dtoCard)){
            return messageSource.getMessage("card.update.success", null, Locale.US);
        }
        else{
            return messageSource.getMessage("card.update.fail", null, Locale.US);
        }
    }

    @DeleteMapping(value = "api/user/{username}/cards/{cardnumber}")
    public @ResponseBody String removeCard(@PathVariable(name = "username") String userName, @PathVariable(name = "cardnumber") String cardNo){
        return cardService.removeCard(userName, cardNo);
    }

    @PutMapping(value = "api/user/{username}/cards/default/{cardnumber}")
    public @ResponseBody String setDefaultCard(@PathVariable(name = "username") String userName, @PathVariable(name = "cardnumber") String cardNo){
        if(cardService.setDefaultCard(userName, cardNo)){
            return messageSource.getMessage("card.default.success", null, Locale.US);
        }
        else{
            return messageSource.getMessage("card.default.fail", null, Locale.US);
        }
    }
}
