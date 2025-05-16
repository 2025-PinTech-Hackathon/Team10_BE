package com.enp.service;

import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GptService {
    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final OkHttpClient client = new OkHttpClient();

    public String askGpt(String question) throws JSONException {
        JSONObject message = new JSONObject()
                .put("role", "user")
                .put("content", question);

        JSONObject body = new JSONObject()
                .put("model", "gpt-3.5-turbo")
                .put("messages", List.of(message));

        RequestBody requestBody = RequestBody.create(
                body.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", OPENAI_API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);
                return jsonResponse
                        .getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                        .trim();
            }
        } catch (Exception e) {
            return "GPT 응답 중 오류 발생: " + e.getMessage();
        }

        return "GPT 응답 없음";
    }
}
