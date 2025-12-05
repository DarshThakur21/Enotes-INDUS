package com.enotes.Enotes_INDUS.endpoints;

import com.enotes.Enotes_INDUS.dto.CategoryDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/category")
public interface CategoryEndpoint {

    @PostMapping("/save-category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveCategory(@Valid @RequestBody CategoryDto categoryDto);

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCategories();

    @GetMapping("/active")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getActiveCategories();

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id);

}
