package com.example.edtech.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CloudinaryResponse {
    private final String url;
    private final String id;
}
