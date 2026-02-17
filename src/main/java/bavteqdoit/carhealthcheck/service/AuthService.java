package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.UserRepository;
import bavteqdoit.carhealthcheck.dto.RegisterError;
import bavteqdoit.carhealthcheck.dto.RegisterResult;
import bavteqdoit.carhealthcheck.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResult register(User user) {

        // USERNAME
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return RegisterResult.fail(RegisterError.USERNAME_EMPTY);
        }

        String username = user.getUsername().trim();

        if (userRepository.existsByUsername(username)) {
            return RegisterResult.fail(RegisterError.USERNAME_TAKEN);
        }

        // EMAIL
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return RegisterResult.fail(RegisterError.EMAIL_EMPTY);
        }

        String email = user.getEmail().trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return RegisterResult.fail(RegisterError.EMAIL_INVALID);
        }

        if (userRepository.existsByEmail(email)) {
            return RegisterResult.fail(RegisterError.EMAIL_TAKEN);
        }

        // PASSWORD
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return RegisterResult.fail(RegisterError.PASSWORD_EMPTY);
        }

        if (user.getPassword().length() < 6) {
            return RegisterResult.fail(RegisterError.PASSWORD_TOO_SHORT);
        }

        if (user.getConfirmPassword() == null ||
                !user.getPassword().equals(user.getConfirmPassword())) {
            return RegisterResult.fail(RegisterError.PASSWORD_MISMATCH);
        }

        // ZAPIS
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

        return RegisterResult.ok();
    }
}
