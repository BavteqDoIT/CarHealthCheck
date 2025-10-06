package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.model.User;
import bavteqdoit.carhealthcheck.data.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                User admin = User.builder()
                        .username("admin")
                        .email("admin@admin")
                        .password(encoder.encode("admin"))
                        .role("ADMIN")
                        .build();

                userRepository.save(admin);
                System.out.println("✅ Administrator account created: admin / admin");
            } else {
                System.out.println("ℹ️ Admin account already exists — skipping creation.");
            }
        };
    }
}
