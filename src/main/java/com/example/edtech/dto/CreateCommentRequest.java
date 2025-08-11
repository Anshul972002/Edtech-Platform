package com.example.edtech.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateCommentRequest {

    @Schema(example = "great course")
    private String content;
}
