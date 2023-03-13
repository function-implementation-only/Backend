package com.example.speedsideproject.domain.applyment.controller;


import com.example.speedsideproject.domain.applyment.dto.ApplymentRequestDto;
import com.example.speedsideproject.domain.applyment.service.ApplymentService;
import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.global.security.user.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequestMapping("/applyments")
@RestController
@RequiredArgsConstructor
public class ApplymentController {
    private final ApplymentService applymentService;

    // 지원하기
    @ApiOperation(value = "지원 하기", notes = "지원 하기")
    @PostMapping
    public ResponseDto<?> createApplyment(@RequestBody ApplymentRequestDto requestDto, @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(applymentService.createApplyment(requestDto, userDetails.getAccount()));
    }

    // 지원수정
    @ApiOperation(value = "지원 수정", notes = "지원을 수정합니다")
    @PatchMapping("/{id}")
    public ResponseDto<?> updateApplyment(@RequestBody ApplymentRequestDto requestDto, @PathVariable Long id, @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(applymentService.updateApplyment(requestDto, id, userDetails.getAccount()));
    }

    // 지원삭제
    @ApiOperation(value = "지원 삭제", notes = "지원 삭제")
    @DeleteMapping("/{id}")
    public ResponseDto<?> deleteApplyment(@PathVariable Long id, @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(applymentService.deleteApplyment(id, userDetails.getAccount()));
    }

    //지원 1개 읽기
    @ApiOperation(value = "지원 1개 읽기", notes = "지원 1개 읽기")
    @GetMapping("/{id}")
    public ResponseDto<?> getOneApplyment(@PathVariable Long id, @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(applymentService.getOneApplyment(id));
    }

    //내 모든 지원 보여주기
    @ApiOperation(value = "내 모든 지원 조회", notes = "내 모든 지원 조회")
    @GetMapping("/my")
    public ResponseDto<?> getAllMyApplyments(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(applymentService.getAllMyApplyments(userDetails.getAccount()));
    }


}
