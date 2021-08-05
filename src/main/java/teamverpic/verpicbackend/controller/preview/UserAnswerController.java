package teamverpic.verpicbackend.controller.preview;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.dto.preview.useranswer.UserAnswerResponseDto;
import teamverpic.verpicbackend.dto.preview.useranswer.UserAnswerSaveRequestDto;
import teamverpic.verpicbackend.dto.preview.useranswer.UserAnswerUpdateRequestDto;
import teamverpic.verpicbackend.service.preview.UserAnswerService;

@RequiredArgsConstructor
@RestController
public class UserAnswerController {

    private final UserAnswerService userAnswerService;

    @PostMapping("/previews/{preview_id}/detail_topics/{detail_topic_id}")
    public Long save(@PathVariable Long detail_topic_id, @RequestBody UserAnswerSaveRequestDto requestDto) {
        return userAnswerService.save(detail_topic_id, requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody UserAnswerUpdateRequestDto requestDto) {
        return userAnswerService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public UserAnswerResponseDto findById (@PathVariable Long id) {
        return userAnswerService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        userAnswerService.delete(id);
        return id;
    }
}
