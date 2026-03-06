package com.revworkforce.admin.config;

import com.revworkforce.admin.entity.Department;
import com.revworkforce.admin.entity.Designation;
import com.revworkforce.admin.repository.DepartmentRepository;
import com.revworkforce.admin.repository.DesignationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadInitialData(DepartmentRepository departmentRepository,
                                            DesignationRepository designationRepository) {
        return args -> {
            // Load initial departments if not exist
            if (departmentRepository.count() == 0) {
                departmentRepository.save(Department.builder().name("IT").build());
                departmentRepository.save(Department.builder().name("HR").build());
                departmentRepository.save(Department.builder().name("Finance").build());
                System.out.println("Loaded initial departments: IT, HR, Finance");
            }

            // Load initial designations if not exist
            if (designationRepository.count() == 0) {
                designationRepository.save(Designation.builder().name("Software Engineer").build());
                designationRepository.save(Designation.builder().name("Team Lead").build());
                designationRepository.save(Designation.builder().name("HR Executive").build());
                designationRepository.save(Designation.builder().name("Administrator").build());
                System.out.println("Loaded initial designations: Software Engineer, Team Lead, HR Executive, Administrator");
            }
        };
    }
}
