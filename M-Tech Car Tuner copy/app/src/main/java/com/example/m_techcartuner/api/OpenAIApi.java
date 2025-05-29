package com.example.m_techcartuner.api;

import com.example.m_techcartuner.model.ChatRequest;
import com.example.m_techcartuner.model.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenAIApi {

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer Your Own API Key"
    })
    @POST("chat/completions")
    Call<ChatResponse> getChatCompletion(@Body ChatRequest request);
}
