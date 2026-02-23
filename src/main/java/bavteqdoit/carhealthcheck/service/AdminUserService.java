package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.UserRepository;
import bavteqdoit.carhealthcheck.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    @Transactional
    public void changeRole(Long userId, String newRole, String currentUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o id: " + userId));

        if (user.getUsername().equals(currentUsername)) {
            throw new IllegalStateException("cannotChangeOwnRole");
        }

        user.setRole(newRole);
        userRepository.save(user);
    }
}
