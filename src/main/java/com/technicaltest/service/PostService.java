package com.technicaltest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technicaltest.exception.ExternalApiException;
import com.technicaltest.factory.PostFactory;
import com.technicaltest.dto.PaginatedResponse;
import com.technicaltest.exception.InvalidPageException;
import com.technicaltest.exception.InvalidPageSizeException;
import com.technicaltest.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class PostService {

    private static final int MIN_PAGE = 1;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;
    private final PostFactory postFactory;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PaginatedResponse<Post> getPosts(int page, int pageSize) {
        // Validasi page dan pageSize
        validatePageParameters(page, pageSize);

        // Ambil data dari external API
        List<Post> allPosts = fetchPostsFromExternalApi();

        // Implementasi pagination
        return paginateResults(allPosts, page, pageSize);
    }

    private void validatePageParameters(int page, int pageSize) {
        if (page < MIN_PAGE) {
            throw new InvalidPageException("Page number must be greater than or equal to " + MIN_PAGE);
        }

        if (pageSize < MIN_PAGE_SIZE) {
            throw new InvalidPageSizeException("Page size must be greater than or equal to " + MIN_PAGE_SIZE);
        }

        if (pageSize > MAX_PAGE_SIZE) {
            throw new InvalidPageSizeException("Page size must be less than or equal to " + MAX_PAGE_SIZE);
        }
    }

    private List<Post> fetchPostsFromExternalApi() {
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new ExternalApiException("Failed to fetch data from external API. HTTP Response Code: " + responseCode);
            }

            StringBuilder response = new StringBuilder();
            try (Scanner scanner = new Scanner(url.openStream())) {
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
            }

            // Parse JSON response
            JsonNode jsonArray = objectMapper.readTree(response.toString());
            List<Post> posts = new ArrayList<>();

            for (JsonNode jsonNode : jsonArray) {
                Post post = postFactory.createPost(jsonNode);
                posts.add(post);
            }

            return posts;

        } catch (IOException e) {
            throw new ExternalApiException("Error occurred while fetching data from external API", e);
        }
    }

    private PaginatedResponse<Post> paginateResults(List<Post> allPosts, int page, int pageSize) {
        int totalElements = allPosts.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        // Validasi jika page melebihi total pages yang tersedia
        if (page > totalPages && totalElements > 0) {
            throw new InvalidPageException("Page number " + page + " exceeds total available pages: " + totalPages);
        }

        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalElements);

        List<Post> pagedPosts = new ArrayList<>();
        if (startIndex < totalElements) {
            pagedPosts = allPosts.subList(startIndex, endIndex);
        }

        PaginatedResponse<Post> response = new PaginatedResponse<>();
        response.setData(pagedPosts);
        response.setCurrentPage(page);
        response.setPageSize(pageSize);
        response.setTotalElements(totalElements);
        response.setTotalPages(totalPages);
        response.setHasNext(page < totalPages);
        response.setHasPrevious(page > 1);

        return response;
    }
}
