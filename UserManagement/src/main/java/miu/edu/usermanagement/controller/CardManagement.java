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

@CrossOrigin(maxAge = 3600)
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

    @PostMapping(value = "api/card/add")
    public @ResponseBody String addNewCard(@RequestParam(name = "un") String userName, @Valid @RequestBody CardDTO cardDTO){
        String retMessage = cardService.addNewCard(userName, cardDTO);

        return retMessage;
    }

    @GetMapping(value = "/api/card/get")
    public @ResponseBody CardDTO getCardByUserNameAndCardNumber(@RequestParam(name = "un") String userName, @RequestParam(name = "cn") String cardNumber){
        return cardService.getCardInfo(userName, cardNumber);
    }

    @GetMapping(value = "api/card/list")
    public @ResponseBody List<CardDTO> listAllCards(@RequestParam(name = "un") String userName){
        UserDTO dtoUser = userService.queryUserByUserName(userName);
        if(dtoUser != null) {
            return dtoUser.getCards();
        }
        else{
            return null;
        }
    }

    @PostMapping(value = "/api/card/update")
    public String updateCardByUserName(@RequestParam(name = "un") String userName, @RequestParam(name = "cn") String cardNumber, @RequestBody CardDTO dtoCard){
        if(cardService.updateCard(userName, cardNumber, dtoCard)){
            return messageSource.getMessage("card.update.success", null, Locale.US);
        }
        else{
            return messageSource.getMessage("card.update.fail", null, Locale.US);
        }
    }

    @PostMapping(value = "api/card/remove")
    public @ResponseBody String removeCard(@RequestParam(name = "un") String userName, @RequestParam(name = "cn") String cardNo){
        return cardService.removeCard(userName, cardNo);
    }

    @PostMapping(value = "api/card/default")
    public @ResponseBody String setDefaultCard(@RequestParam(name = "un") String userName, @RequestParam(name = "cn") String cardNo){
        if(cardService.setDefaultCard(userName, cardNo)){
            return messageSource.getMessage("card.default.success", null, Locale.US);
        }
        else{
            return messageSource.getMessage("card.default.fail", null, Locale.US);
        }
    }
}
