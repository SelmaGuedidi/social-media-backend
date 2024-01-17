package com.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagingRequest {
    private int pageNumber = 0;

    @Nullable
    @Builder.Default
    private int pageSize = 5;

    @Nullable
    String sortField;
    String sortOrder = "DESC";
}
