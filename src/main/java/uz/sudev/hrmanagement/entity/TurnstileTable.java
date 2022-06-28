package uz.sudev.hrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurnstileTable {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne(optional = false)
    private User user;
    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp timestamp;
    @ManyToOne(optional = false)
    private Turnstile turnstile;
}
