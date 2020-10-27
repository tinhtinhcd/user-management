package miu.edu.usermanagement.service;

import miu.edu.usermanagement.dto.CardDTO;
import miu.edu.usermanagement.entity.Card;

public interface ICardService {
    public String addNewCard(String curUser, CardDTO cardDTO);
    public CardDTO getCardInfo(String curUser, String cardNumber);
    public String removeCard(String userName, String cardNo);
    public boolean updateCard(String userName, String cardNumber, CardDTO cardDTO);
    public boolean setDefaultCard(String userName, String cardNumber);
}
