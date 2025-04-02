package com.umcsuser.carrent.repositories.impl.jdbc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umcsuser.carrent.db.JdbcConnectionManager;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.UserRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserJdbcRepository implements UserRepository {
    private final Gson gson = new Gson();

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String attrJson = rs.getString("attributes");
                Map<String, Object> attributes = gson.fromJson(attrJson, new TypeToken<Map<String, Object>>(){}.getType());

                User user = User.builder()
                        .id(rs.getString("id"))
                        .login(rs.getString("login"))
                        .password(rs.getString("password"))
                        .role(rs.getString("role")).build();
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading users", e);
        }
        return list;
    }

    @Override
    public Optional<User> findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = User.builder()
                            .id(rs.getString("id"))
                            .login(rs.getString("login"))
                            .password(rs.getString("password"))
                            .role(rs.getString("role")).build();
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading users", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(2, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = User.builder()
                            .id(rs.getString("id"))
                            .login(rs.getString("login"))
                            .password(rs.getString("password"))
                            .role(rs.getString("role")).build();
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading users", e);
        }
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(UUID.randomUUID().toString());
        } else {
            deleteById(user.getId());
        }

        String sql = "INSERT INTO users (id, login, password, role) VALUES (?, ?, ?, ?)";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, user.getId());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while saving user", e);
        }
        return user;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deleting user", e);
        }
    }
}
