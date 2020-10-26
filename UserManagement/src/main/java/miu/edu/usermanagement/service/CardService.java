package miu.edu.usermanagement.service;

import miu.edu.usermanagement.dto.CardDTO;
import miu.edu.usermanagement.entity.Card;
import miu.edu.usermanagement.entity.User;
import miu.edu.usermanagement.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private UserRepo userRepo;

    public CardService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    public String addNewCard(String curUser, CardDTO cardDTO) {
        String retMessage = "";
//        String curUser = "admin";//TODO: how to get the current login user
        //get the entity User from DB by the current user login
        Optional<User> user = userRepo.findUserByUsername(curUser);

        if(user.isPresent()){
            //mapping card DTO to entity
            Card card = mapCardDTOtoEntity(cardDTO);
            List<Card> listCard = user.get().getListCards();
            if(listCard == null){
                listCard = new ArrayList<>();
            }
            listCard.add(card);
            userRepo.flush();

            retMessage = "The card number " + card.getCardNumber() + " was added successfully";
        }
        else{
            retMessage = "The user " + curUser + " doesn't exist";
        }


        return retMessage;
    }

    private Card mapCardDTOtoEntity(CardDTO cardDTO) {
        Card card = new Card();
        card.setCardNumber(cardDTO.getCardNumber());
        card.setDefault(true);
        card.setExpiredDate(cardDTO.getExpiredDate());
        card.setType(cardDTO.getType());
        card.setCreateDate(LocalDateTime.now());

        return card;
    }
}
