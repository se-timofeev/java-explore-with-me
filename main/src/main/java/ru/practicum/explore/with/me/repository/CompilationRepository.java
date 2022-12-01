package ru.practicum.explore.with.me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explore.with.me.model.compilations.Compilations;

public interface CompilationRepository extends JpaRepository<Compilations, Long>, JpaSpecificationExecutor<Compilations> {
}
