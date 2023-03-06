package com.example.speedsideproject.applyment.controller;


import com.example.speedsideproject.applyment.dto.ApplymentRequestDto;
import com.example.speedsideproject.applyment.service.ApplymentService;
import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequestMapping("/applyments")
@RestController
@RequiredArgsConstructor
public class ApplymentController {
    private final ApplymentService applymentService;

    // 지원쓰기 api
    @PostMapping
    public ResponseDto<?> createApplyment(@RequestBody ApplymentRequestDto requestDto, @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(applymentService.createApplyment(requestDto, userDetails.getAccount()));
    }

    // 지원수정 api
    @PatchMapping("/{id}")
    public ResponseDto<?> updateApplyment(@RequestBody ApplymentRequestDto requestDto, @PathVariable Long id, @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(applymentService.updateApplyment(requestDto, id, userDetails.getAccount()));
    }

    // 지원삭제 api
    @DeleteMapping("/{id}")
    public ResponseDto<?> deleteApplyment(@PathVariable Long id, @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(applymentService.deleteApplyment(id, userDetails.getAccount()));
    }

    //지원 1개 읽기 api
    @GetMapping("/{id}")
    public ResponseDto<?> getOneApplyment(@PathVariable Long id, @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(applymentService.getOneApplyment(id));
    }

    //내 모든 지원 보여주기
    @GetMapping("/my")
    public ResponseDto<?> getAllMyApplyments(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(applymentService.getAllMyApplyments(userDetails.getAccount()));
    }


}
