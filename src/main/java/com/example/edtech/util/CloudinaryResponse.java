package com.example.edtech.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data

public class CloudinaryResponse {
    private final String url;
    private final String id;
    public  CloudinaryResponse(String id,String url){
        this.id=id;
        this.url=url;
    }
}


