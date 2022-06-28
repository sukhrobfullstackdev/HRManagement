package uz.sudev.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.sudev.hrmanagement.entity.Turnstile;

@Repository
public interface TurnstileRepository extends JpaRepository<Turnstile,Integer> {
}
