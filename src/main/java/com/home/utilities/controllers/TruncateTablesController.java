package com.home.utilities.controllers;

import com.home.utilities.payload.dto.ApiResponse;
import com.home.utilities.service.TruncateDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TruncateTablesController {

    private final TruncateDatabaseService truncateDatabaseService;

    @DeleteMapping("/api/admin/tables/truncate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> truncateAllTables() {
        try {
            truncateDatabaseService.truncateAllTablesFromDatabaseAndInsertTwoUsers();
            return ResponseEntity.ok(new ApiResponse(true, "Operation successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Operation failed: " + e.getMessage()));
        }
    }
}
