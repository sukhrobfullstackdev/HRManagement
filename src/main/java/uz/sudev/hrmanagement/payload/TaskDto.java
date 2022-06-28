package uz.sudev.hrmanagement.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TaskDto {
    private String name;
    private String description;
    private Timestamp shouldDoneAt;
    private UUID userId;
    private Integer stateId;
}
