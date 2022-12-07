package ru.practicum.explore.with.me.model.categories.dto;

import ru.practicum.explore.with.me.model.categories.Categories;

public class CategoriesMapper {

    public static CategoriesDto toCategoriesDto(Categories categories) {
        return CategoriesDto.builder()
                .id(categories.getId())
                .name(categories.getName())
                .build();
    }

    public static Categories toCategories(CategoriesDto categoriesDto) {
        return Categories.builder()
                .id(categoriesDto.getId())
                .name(categoriesDto.getName())
                .build();
    }

}
