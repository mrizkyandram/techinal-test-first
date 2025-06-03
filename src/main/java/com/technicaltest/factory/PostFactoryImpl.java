package com.technicaltest.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.technicaltest.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFactoryImpl implements PostFactory {

    @Override
    public Post createPost(JsonNode jsonNode) {
        Post post = new Post();
        post.setId(jsonNode.get("id").asLong());
        post.setTitle(jsonNode.get("title").asText());
        return post;
    }
}
