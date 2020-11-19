package ru.bmtsu.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bmtsu.store.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
