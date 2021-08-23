package teamverpic.verpicbackend.domain.preview.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.domain.matching.dao.MatchUserRepository;
import teamverpic.verpicbackend.domain.matching.domain.MatchUser;
import teamverpic.verpicbackend.domain.preview.dto.detailtopic.DetailTopicResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.detailtopic.DetailTopicSaveRequestDto;
import teamverpic.verpicbackend.domain.preview.service.DetailTopicService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
public class DetailTopicController {

    private final DetailTopicService detailTopicService;

    @PostMapping("/previews/{preview_id}/detail_topics")
    public Long save(@PathVariable Long preview_id, @RequestBody DetailTopicSaveRequestDto requestDto) {
        return detailTopicService.save(preview_id, requestDto);
    }

    @GetMapping("detail_topics/{detail_topic_id}")
    public DetailTopicResponseDto findById(@PathVariable Long detail_topic_id) {
        return detailTopicService.findById(detail_topic_id);
    }

    @GetMapping("/detail_topics_match_id/{matchId}")
    public List<DetailTopicResponseDto> findByMatchId(@PathVariable Long matchId) {
        return detailTopicService.findByMatchId(matchId);
    }
}
