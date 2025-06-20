package com.technicaltest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;
}