package miu.edu.usermanagement.controller;

import miu.edu.usermanagement.dto.CardDTO;
import miu.edu.usermanagement.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CardManagement {
    private CardService cardService;

    @Autowired
    public CardManagement(CardService cardService){
        this.cardService = cardService;
    }

    @PostMapping(value = "api/card/add")
    public @ResponseBody String addNewCard(@RequestParam(name = "un") String userName, @Valid @RequestBody CardDTO cardDTO){
        String retMessage = cardService.addNewCard(userName, cardDTO);

        return retMessage;
    }
}
