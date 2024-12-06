package com.example.backend.controller.BANK;

import com.example.backend.dto.account.*;
import com.example.backend.model.BANK.Account;
import com.example.backend.model.BANK.AccountHistory;
import com.example.backend.service.BANK.AccountService;
import com.example.backend.service.CardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final CardService cardService;


    // 지출 간단 보기
    @GetMapping("/expense")
    public ResponseEntity<ExpenseDTO> expense(
            @RequestParam("month") String month,
            @AuthenticationPrincipal Long memberId) {  // JWT에서 추출한 memberId
        YearMonth yearMonth = YearMonth.parse(month);
        ExpenseDTO expenseSummary = accountService.showSimpleExpense(memberId, yearMonth);

        return ResponseEntity.ok(expenseSummary);
    }

    // 지출 상세보기
    @GetMapping("/expense/detail")
    public ResponseEntity<ExpenseDetailDTO> expenseDetail(
            @RequestParam("month") String month,
            @AuthenticationPrincipal Long memberId) {  // JWT에서 추출한 memberId
        YearMonth yearMonth = YearMonth.parse(month);
        ExpenseDetailDTO expenseDetails = accountService.showDetailExpense(memberId, yearMonth);
        return ResponseEntity.ok(expenseDetails);
    }

    // 순 이익 (총수익 - 총지출)
    @GetMapping("/profit")
    public ResponseEntity<BigDecimal> netProfit(
            @RequestParam("month") String month,
            @AuthenticationPrincipal Long memberId) {  // JWT에서 추출한 memberId
        YearMonth yearMonth = YearMonth.parse(month);
        BigDecimal netProfit = accountService.showNetProfit(memberId, yearMonth);
        return ResponseEntity.ok(netProfit);
    }

    // 순이익 상세 (총 수익, 매출 원가, 운영 비용, 세금, 순 이익)
    @GetMapping("/profit/detail")
    public ResponseEntity<ProfitDetailDTO> profitDetail(
            @RequestParam("month") String month,
            @AuthenticationPrincipal Long memberId){
        YearMonth yearMonth = YearMonth.parse(month);
        ProfitDetailDTO profitDetail = accountService.showProfitDetail(memberId, yearMonth);
        return ResponseEntity.ok(profitDetail);
    }

    // 주차별 지출 (월요일 기준으로 주차 시작)
    @GetMapping("/expense/week")
    public ResponseEntity<ExpenseWeekDTO> expenseWeek(
            @RequestParam("month") String month,
            @AuthenticationPrincipal Long memberId
    ) {
        YearMonth yearMonth = YearMonth.parse(month);
        ExpenseWeekDTO expense = accountService.showWeekExpense(memberId, yearMonth);
        return ResponseEntity.ok(expense);
    }


}
