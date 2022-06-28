package uz.sudev.hrmanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.sudev.hrmanagement.entity.Task;
import uz.sudev.hrmanagement.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findAllByUser(User user, Pageable pageable);
    Optional<Task> findByUserAndId(User user, UUID id);
}
