package com.technicaltest.controller;

import com.technicaltest.dto.PaginatedResponse;
import com.technicaltest.model.Post;
import com.technicaltest.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/get/data")
@RequiredArgsConstructor
public class GetDataController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<Post>> getPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        PaginatedResponse<Post> response = postService.getPosts(page, pageSize);
        return ResponseEntity.ok(response);
    }
}
