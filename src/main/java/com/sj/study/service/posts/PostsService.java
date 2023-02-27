package com.sj.study.service.posts;

import com.sj.study.domain.posts.Posts;
import com.sj.study.domain.posts.PostsRepository;
import com.sj.study.web.dto.PostSaveRequestDto;
import com.sj.study.web.dto.PostsListResponseDto;
import com.sj.study.web.dto.PostsResponseDto;
import com.sj.study.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true) // 트랜잭션 범위는 유지, 조회기능만 남겨서 조회 속도 개선 (등록,수정,삭제 기능이 전혀 없는 서비스 메소드에서 사용하길)
    public List<PostsListResponseDto> findAllDesc() {
        // postRepository 결과로 넘어온 Posts 의 Stream 을 map 을 통해 PostsListResponseDto 변환
        // -> List 로 반환
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new) // (.map(posts -> new PostsListResponseDto(posts)))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        postsRepository.delete(posts);
    }

}
