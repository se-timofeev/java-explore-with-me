package ru.practicum.explore.with.me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.with.me.model.categories.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
}
