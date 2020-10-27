package miu.edu.usermanagement.repository;

import miu.edu.usermanagement.entity.Card;
import miu.edu.usermanagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepo extends JpaRepository<Card, Long> {

//    @Query(value = "delete from card c where c.user_id = ?1 and c.card_number = ?2", nativeQuery = true)
//    void removeCardByUserIdAndCardNumber(Long id, String cardNo);
    void deleteCardsByCardNumber(String cardNumber);
//    void removeCardsByCardNumber(String cardNo);
}
