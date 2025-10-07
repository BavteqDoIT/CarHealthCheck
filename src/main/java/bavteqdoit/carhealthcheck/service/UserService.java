package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.UserRepository;
import bavteqdoit.carhealthcheck.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
