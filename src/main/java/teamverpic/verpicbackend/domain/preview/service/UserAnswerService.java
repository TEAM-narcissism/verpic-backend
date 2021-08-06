package teamverpic.verpicbackend.domain.preview.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.preview.domain.DetailTopic;
import teamverpic.verpicbackend.domain.preview.domain.UserAnswer;
import teamverpic.verpicbackend.domain.preview.dto.useranswer.UserAnswerResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.useranswer.UserAnswerSaveRequestDto;
import teamverpic.verpicbackend.domain.preview.dto.useranswer.UserAnswerUpdateRequestDto;
import teamverpic.verpicbackend.domain.preview.dao.DetailTopicRepository;
import teamverpic.verpicbackend.domain.preview.dao.UserAnswerRepository;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class UserAnswerService {
    private final UserAnswerRepository userAnswerRepository;
    private final DetailTopicRepository detailTopicRepository;

    @Transactional
    public Long save(Long detail_topic_id, UserAnswerSaveRequestDto requestDto) {
        DetailTopic detailTopic = detailTopicRepository.findById(detail_topic_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 DetailTopic이 없습니다. id=" + detail_topic_id));

        UserAnswer userAnswer = requestDto.toEntity();
        detailTopic.addUserAnswer(userAnswer);

        return userAnswerRepository.save(userAnswer).getId();
    }

    @Transactional
    public Long update(Long id, UserAnswerUpdateRequestDto requestDto) {
        UserAnswer userAnswer = userAnswerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 useranswer가 없습니다. id=" + id));

        userAnswer.update(requestDto.getAnswer());

        return id;
    }

    @Transactional
    public UserAnswerResponseDto findById(Long id) {
        UserAnswer entity = userAnswerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 useranswer가 없습니다. id=" + id));

        return new UserAnswerResponseDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        UserAnswer userAnswer = userAnswerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 useranswer가 없습니다. id=" + id));

        userAnswerRepository.delete(userAnswer);
    }
}
