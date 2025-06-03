package com.technicaltest.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.technicaltest.model.Post;

public interface PostFactory {
    Post createPost(JsonNode jsonNode);
}
