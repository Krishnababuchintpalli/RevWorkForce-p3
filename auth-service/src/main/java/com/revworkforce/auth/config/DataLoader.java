package com.revworkforce.auth.config;

import com.revworkforce.auth.entity.RoleMaster;
import com.revworkforce.auth.entity.User;
import com.revworkforce.auth.enums.Role;
import com.revworkforce.auth.repository.RoleMasterRepository;
import com.revworkforce.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(RoleMasterRepository roleMasterRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed roles if not already present
            if (roleMasterRepository.count() == 0) {
                roleMasterRepository.save(RoleMaster.builder()
                        .id(Role.EMPLOYEE.getId())
                        .name(Role.EMPLOYEE.name())
                        .build());

                roleMasterRepository.save(RoleMaster.builder()
                        .id(Role.MANAGER.getId())
                        .name(Role.MANAGER.name())
                        .build());

                roleMasterRepository.save(RoleMaster.builder()
                        .id(Role.ADMIN.getId())
                        .name(Role.ADMIN.name())
                        .build());

                System.out.println("Roles seeded successfully");
            }

            // Create default admin user if not already present
            if (!userRepository.existsByEmail("admin@revworkforce.com")) {
                User adminUser = User.builder()
                        .email("admin@revworkforce.com")
                        .password(passwordEncoder.encode("Admin@123"))
                        .fullName("System Administrator")
                        .roleId(Role.ADMIN.getId())
                        .status("ACTIVE")
                        .build();

                userRepository.save(adminUser);
                System.out.println("Default admin user created - Email: admin@revworkforce.com, Password: Admin@123");
            }
        };
    }
}
