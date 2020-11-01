package miu.edu.usermanagement.service;

import miu.edu.usermanagement.dto.CardDTO;
import miu.edu.usermanagement.entity.Card;
import miu.edu.usermanagement.entity.User;
import miu.edu.usermanagement.repository.CardRepo;
import miu.edu.usermanagement.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService implements ICardService {

    private UserRepo userRepo;
    private CardRepo cardRepo;

    public CardService(UserRepo userRepo, CardRepo cardRepo){
        this.userRepo = userRepo;
        this.cardRepo = cardRepo;
    }

    @Override
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

            retMessage = "The card number " + card.getId() + " was added successfully";
        }
        else{
            retMessage = "The user " + curUser + " doesn't exist";
        }

        return retMessage;
    }

    @Override
    public CardDTO getCardInfo(String curUser, String cardNumber) {
        Optional<User> user = userRepo.findUserByUsername(curUser);
        if(user.isPresent()){
            User entityUser = user.get();
            List<Card> cards = entityUser.getListCards();
            Optional<Card> firstCard = cards.stream().filter(c -> c.getCardNumber().matches(cardNumber)).findFirst();
            if(firstCard.isPresent()){
                return mapCardEntityToDTO(firstCard.get());
            }
        }
        return null;
    }

    private CardDTO mapCardEntityToDTO(Card entityCard) {
        CardDTO dtoCard = new CardDTO();
        dtoCard.setCardNumber(entityCard.getCardNumber());
        dtoCard.setName(entityCard.getName());
        dtoCard.setCvv(entityCard.getCvv());
        dtoCard.setExpiredDate(entityCard.getExpiredDate());
        dtoCard.setDefault(entityCard.isDefault());

        return dtoCard;
    }

    private Card mapCardDTOtoEntity(CardDTO cardDTO) {
        Card card = new Card();
        card.setCardNumber(cardDTO.getCardNumber());
        card.setDefault(true);
        card.setExpiredDate(cardDTO.getExpiredDate());
        card.setName(cardDTO.getName());
        card.setCvv(cardDTO.getCvv());
        card.setCreateDate(LocalDateTime.now());

        return card;
    }

    @Override
    @Transactional
    public String removeCard(String userName, String cardNo) {
        String retMessage = "";
        //find user id by user name
        Optional<User> user = userRepo.findUserByUsername(userName);
        if(user.isPresent()){
            //cardRepo.removeCardByUserIdAndCardNumber(user.get().getId(), cardNo);
            //TODO It removes all cards, not cards by user name
            cardRepo.deleteCardsByCardNumber(cardNo);
            //lambda
//            User entityUser = user.get();
//            List<Card> cards = entityUser.getListCards();
//            cards.stream().
            retMessage = "The card " + cardNo + " was removed successfully";
        }
        else{
            retMessage = "The card " + cardNo + " was not removed";
        }

        return retMessage;
    }

    @Override
    public boolean updateCard(String userName, String cardNumber, CardDTO cardDTO) {
        Optional<User> user = userRepo.findUserByUsername(userName);
        if(user.isPresent()){
            User entityUser = user.get();
            List<Card> listCards = entityUser.getListCards();
            Optional<Card> opCard = listCards.stream().filter(c -> c.getCardNumber().matches(cardNumber)).findFirst();
            if(opCard.isPresent()){
                Card card = opCard.get();
                card.setCardNumber(cardNumber);
                card.setName(cardDTO.getName());
                card.setCvv(cardDTO.getCvv());
                card.setExpiredDate(cardDTO.getExpiredDate());
                card.setDefault(cardDTO.isDefault());
                userRepo.flush();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setDefaultCard(String userName, String cardNumber) {
        Optional<User> user = userRepo.findUserByUsername(userName);
        if(user.isPresent()) {
            User entityUser = user.get();
            List<Card> listCards = entityUser.getListCards();
            listCards.forEach(c -> c.setDefault(false));
            Optional<Card> opCard = listCards.stream().filter(c -> c.getCardNumber().matches(cardNumber)).findFirst();
            if (opCard.isPresent()) {
                opCard.get().setDefault(true);
                userRepo.flush();
                return true;
            }
        }
        return false;
    }
}
