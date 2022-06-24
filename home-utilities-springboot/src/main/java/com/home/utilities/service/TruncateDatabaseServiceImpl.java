package com.home.utilities.service;

import com.home.utilities.entity.AccountStatus;
import com.home.utilities.entity.Gender;
import com.home.utilities.entity.User;
import com.home.utilities.entity.UserRole;
import com.home.utilities.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TruncateDatabaseServiceImpl implements TruncateDatabaseService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager em;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username:}")
    private String dbUsername;

    @Value("${spring.datasource.password:}")
    private String dbPassword;

    @Value("${admin.username:}")
    private String adminUsername;

    @Value("${admin.password:}")
    private String adminPassword;

    @Value("${student.username:}")
    private String studentUsername;

    @Value("${student.password:}")
    private String studentPassword;

    @Override
    @Transactional
    public void truncateAllTablesFromDatabaseAndInsertTwoUsers() throws SQLException {
        final List<String> tables = new ArrayList<>();
        try (final var connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {

            final DatabaseMetaData metaData = connection.getMetaData();
            final var types = new String[]{"TABLE"};

            final ResultSet rs = metaData.getTables(null, null, "%", types);
            while (rs.next()) {
                tables.add(rs.getString(3));
            }
        }

        tables.forEach(t -> em.createNativeQuery("truncate table " + t + " restart identity cascade").executeUpdate());

        final String adminEncodedPassword = passwordEncoder.encode(adminPassword);
        final String studentEncodedPassword = passwordEncoder.encode(studentPassword);
        final var userAdmin = new User(adminUsername, adminEncodedPassword, "Admin",
              "User", Gender.MALE, true, true, UserRole.ADMIN, AccountStatus.ACTIVE, 0, null);
        userRepository.save(userAdmin);
        final var userStudent = new User(studentUsername, studentEncodedPassword, "Student",
              "User", Gender.FEMALE, true, true, UserRole.USER, AccountStatus.ACTIVE, 0, null);
        userRepository.save(userStudent);
    }
}
