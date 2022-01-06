package liuru.world.vote_master.entity;

import lombok.Data;
import org.springframework.stereotype.Repository;

@Repository
@Data
public class User {
    private Integer id;
    private String name;
    private String password;
}
