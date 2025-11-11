package it.lorenzopaciello.awesomepizza.repository;

import it.lorenzopaciello.awesomepizza.model.OrderAction;
import it.lorenzopaciello.awesomepizza.model.enums.OrderActionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderActionRepository extends JpaRepository<OrderAction, Long> {
    List<OrderAction> findByOperator_usernameAndActionAndIsCompleteFalse(String operatorUsername, OrderActionEnum orderAction);
}
