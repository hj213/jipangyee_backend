package com.example.backend.service.POS;

import com.example.backend.dto.pos.OrderResponseDTO;
import com.example.backend.dto.pos.PosRequestDTO;
import com.example.backend.model.BUSINESS.BusinessRegistration;
import com.example.backend.model.Member;
import com.example.backend.model.POS.Pos;
import com.example.backend.repository.BusinessRegistrationRepository;
import com.example.backend.repository.MemberRepository;
import com.example.backend.repository.PosRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PosOrderService {

    private final PosRepository posRepository;
    private final BusinessRegistrationRepository businessRegistrationRepository;

    @Autowired
    @Qualifier("webClient8083")
    private WebClient webClient;
    private final MemberRepository memberRepository;

    public List<OrderResponseDTO> fetchOrdersFromPos() {
        return webClient.get()
                .uri("/api/orders/all")
                .retrieve()
                .bodyToFlux(OrderResponseDTO.class)
                .collectList()
                .block();
    }


}
