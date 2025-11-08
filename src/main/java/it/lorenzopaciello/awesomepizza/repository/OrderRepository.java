package it.lorenzopaciello.awesomepizza.repository;

import it.lorenzopaciello.awesomepizza.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
