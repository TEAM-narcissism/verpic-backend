package teamverpic.verpicbackend.domain.preview.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.domain.preview.dto.useranswer.UserAnswerResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.useranswer.UserAnswerSaveRequestDto;
import teamverpic.verpicbackend.domain.preview.dto.useranswer.UserAnswerUpdateRequestDto;
import teamverpic.verpicbackend.domain.preview.service.UserAnswerService;

@RequiredArgsConstructor
@RestController
public class UserAnswerController {

    private final UserAnswerService userAnswerService;

    @PostMapping("/detail_topics/{detail_topic_id}/user_answers")
    public Long save(@PathVariable Long detail_topic_id, @RequestBody UserAnswerSaveRequestDto requestDto, Authentication authentication) {
        String email = authentication.getName();
        return userAnswerService.save(email, detail_topic_id, requestDto);
    }

    @PutMapping("/user_answers/{user_answer_id}")
    public Long update(@PathVariable Long user_answer_id, @RequestBody UserAnswerUpdateRequestDto requestDto) {
        return userAnswerService.update(user_answer_id, requestDto);
    }

    @GetMapping("/user_answers/{user_answer_id}")
    public UserAnswerResponseDto findById (@PathVariable Long user_answer_id) {
        return userAnswerService.findById(user_answer_id);
    }

    @DeleteMapping("/user_answers/{user_answer_id}")
    public Long delete(@PathVariable Long user_answer_id) {
        userAnswerService.delete(user_answer_id);
        return user_answer_id;
    }
}
