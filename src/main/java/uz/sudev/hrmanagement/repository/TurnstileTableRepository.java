package uz.sudev.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.sudev.hrmanagement.entity.TurnstileTable;

import java.util.UUID;

public interface TurnstileTableRepository extends JpaRepository<TurnstileTable, UUID> {
}
