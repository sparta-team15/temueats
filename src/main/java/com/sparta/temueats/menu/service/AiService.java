package com.sparta.temueats.menu.service;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.menu.entity.P_aiLog;
import com.sparta.temueats.menu.repository.AiRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.sparta.temueats.global.ResponseDto.FAILURE;
import static com.sparta.temueats.global.ResponseDto.SUCCESS;

@Service
@RequiredArgsConstructor
public class AiService {

//    @Value("${ai.api.url}")
    private String url;

    public static final String CONSTRAINT = "답변을 최대한 간결하게 50자 이하로";

    private final RestTemplate restTemplate;
    private final AiRepository aiRepository;

    public ResponseDto<String> request(Map<String, String> aiReqMap) {
        String aiReq = aiReqMap.get("request");
        if (aiReq == null || aiReq.trim().isEmpty()) {
            return new ResponseDto<>(FAILURE, "요청 메시지는 필수입니다.");
        }

        String res = sendReq(aiReq);
        P_aiLog aiLog = P_aiLog.builder()
                .request(aiReq)
                .response(res)
                .build();
        aiRepository.save(aiLog);
        return new ResponseDto<>(SUCCESS, "요청 성공", res);
    }

    private String sendReq(String aiReq) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        String reqBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + aiReq + CONSTRAINT + "\"}]}]}";
        HttpEntity<String> entity = new HttpEntity<>(reqBody, headers);

        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        String resBody = res.getBody();
        JSONObject jsonRes = new JSONObject(resBody);
        JSONArray candidates = jsonRes.getJSONArray("candidates");
        JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");
        return parts.getJSONObject(0).getString("text");
    }

}
