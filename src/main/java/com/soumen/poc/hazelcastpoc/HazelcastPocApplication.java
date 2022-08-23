package com.soumen.poc.hazelcastpoc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication(exclude = HazelcastAutoConfiguration.class)
@EnableScheduling
public class HazelcastPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(HazelcastPocApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(EmployeeRepository repository) {
        return args -> {
            Stream.of("Simon", "David", "Ian", "Bob", "Vasilis", "Adam", "Eliza")
                    .map(name -> new Employee(null, name))
                    .forEach(repository::save);
        };
    }
}

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
class EmployeeController {
    private final EmployeeRepository employeeRepository;

    @GetMapping("/employee")
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }
}

@Repository
interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Employee {
    @Id
    @GeneratedValue
    Long id;
    String name;
}
