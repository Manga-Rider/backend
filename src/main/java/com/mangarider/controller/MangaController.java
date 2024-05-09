package com.mangarider.controller;

import com.mangarider.model.dto.MangaDTO;
import com.mangarider.service.MangaService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manga")
@RequiredArgsConstructor
public class MangaController {
    private final MangaService service;


    @GetMapping
    public Page<MangaDTO> getMangas(
            @RequestParam(value = "size", defaultValue = "20")
            @Min(1) @Max(1000) int size,
            @RequestParam(value = "num", defaultValue = "0")
            @PositiveOrZero int num,
            @RequestParam(value = "orderBy", defaultValue = "DESC")
            @Pattern(
                    regexp = "asc|desc",
                    flags = {Pattern.Flag.CASE_INSENSITIVE},
                    message = "error.validation.sort.direction.message"
            )
            String order,
            @RequestParam(value = "properties", defaultValue = "createdAt")
            String[] properties
    ) {
        return service.getPage(PageRequest.of(num, size, Sort.Direction.fromString(order), properties));
    }
}
