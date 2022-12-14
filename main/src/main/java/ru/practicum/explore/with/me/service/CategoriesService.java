package ru.practicum.explore.with.me.service;

import ru.practicum.explore.with.me.model.categories.dto.CategoriesDto;

import java.util.List;

public interface CategoriesService {

    CategoriesDto add(CategoriesDto categoriesDto);

    CategoriesDto edit(CategoriesDto categoriesDto);

    void delete(Long catId);

    CategoriesDto getOne(Long catId);

    List<CategoriesDto> getAll(Integer from, Integer size);
}
