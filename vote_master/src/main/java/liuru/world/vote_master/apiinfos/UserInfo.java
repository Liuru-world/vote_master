package liuru.world.vote_master.apiinfos;

import liuru.world.vote_master.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class UserInfo {
    // 获取用户列表
    private static RestTemplate restTemplate = new RestTemplate();
    public static List<User> getUsers(){
        ResponseEntity<List> entity = restTemplate.getForEntity("http://localhost:8080/user/userlist", List.class);
        return entity.getBody() ;
    }
}
