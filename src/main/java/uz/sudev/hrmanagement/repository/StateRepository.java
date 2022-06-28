package uz.sudev.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.sudev.hrmanagement.entity.State;
import uz.sudev.hrmanagement.entity.enums.StateName;

import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State,Integer> {
    Optional<State> findByStateName(StateName stateName);
}
