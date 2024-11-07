package com.example.backend.controller;

import com.example.backend.dto.CheckBusinessDTO;
import com.example.backend.service.BusinessService;
import com.example.backend.util.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
@Slf4j
public class BusinessController {

    private final BusinessService businessService;
    private final TokenProvider tokenProvider;

    // 사업자 인증
    @PostMapping("/check")
    public ResponseEntity<String> checkBusiness(HttpServletRequest request, @RequestBody CheckBusinessDTO checkBusinessRequest) {
        // 요청에서 토큰을 가져와 memberId를 추출
        String token = tokenProvider.resolveAccessToken(request);
        Long memberId = tokenProvider.getMemberIdFromToken(token);

        // BusinessService 호출
        businessService.checkBusiness(memberId, checkBusinessRequest);
        return ResponseEntity.ok("사업자 인증 성공");
    }
}