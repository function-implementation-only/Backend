package com.example.speedsideproject.quarydsl.test;

import com.example.speedsideproject.post.Post;
import com.example.speedsideproject.post.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class PostQuery {

private final PostQueryRepository postQueryRepository;

@GetMapping("/test/v1")
public List<?> queryTest(@RequestParam(name = "size") long size,
                         @RequestParam(name = "offset")long offset){
     return postQueryRepository.testQuery1(size,offset).stream().map(x->new PostResponseDto((Post) x)).collect(Collectors.toList());
}
}
