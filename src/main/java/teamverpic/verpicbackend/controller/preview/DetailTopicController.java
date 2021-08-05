package teamverpic.verpicbackend.controller.preview;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.dto.preview.detailtopic.DetailTopicResponseDto;
import teamverpic.verpicbackend.dto.preview.detailtopic.DetailTopicSaveRequestDto;
import teamverpic.verpicbackend.service.preview.DetailTopicService;

@RequiredArgsConstructor
@RestController
public class DetailTopicController {

    private final DetailTopicService detailTopicService;

    @PostMapping("/previews/{preview_id}/detail_topics")
    public Long save(@PathVariable Long preview_id, @RequestBody DetailTopicSaveRequestDto requestDto) {
        return detailTopicService.save(preview_id, requestDto);
    }

    @GetMapping("/previews/{preview_id}/detail_topics/{detail_topic_id}")
    public DetailTopicResponseDto findById(@PathVariable Long detail_topic_id) {
        return detailTopicService.findById(detail_topic_id);
    }
}
