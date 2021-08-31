package teamverpic.verpicbackend.domain.preview.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.preview.domain.DetailTopic;
import teamverpic.verpicbackend.domain.preview.domain.Expression;
import teamverpic.verpicbackend.domain.preview.domain.Preview;
import teamverpic.verpicbackend.domain.preview.dto.PreviewSetResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.detailtopic.DetailTopicResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.expression.ExpressionResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.preview.PreviewResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.preview.PreviewSaveRequestDto;
import teamverpic.verpicbackend.domain.preview.dao.PreviewRepository;
import teamverpic.verpicbackend.domain.topic.dao.TopicRepository;
import teamverpic.verpicbackend.domain.topic.domain.Topic;
import teamverpic.verpicbackend.domain.topic.dto.TopicDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PreviewService {

    private final PreviewRepository previewRepository;
    private final TopicRepository topicRepository;

    @Transactional
    public Long save(Long topic_id, PreviewSaveRequestDto requestDto) {
        Topic topic = topicRepository.findById(topic_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 Topic이 없습니다. id="+topic_id));

        Preview preview = requestDto.toEntity();

        preview.setTopic(topic);
        topic.setPreview(preview);

        return previewRepository.save(preview).getId();
    }

    @Transactional
    public void edit(Long topic_id, PreviewSaveRequestDto requestDto){
        Topic topic = topicRepository.findById(topic_id)
                .orElseThrow(()-> new IllegalStateException("해당 Topic이 없습니다. id="+topic_id));
        Preview preview = previewRepository.findById(topic_id)
                .orElseThrow(() -> new IllegalStateException("해당 Preview가 없습니다. id=" + topic_id));

        preview.setContext(requestDto.getContext());
        previewRepository.save(preview);
    }

    @Transactional
    public PreviewResponseDto findById(Long id) {
        Preview entity = previewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 Preview가 없습니다. id="+id));

        return new PreviewResponseDto(entity);
    }

    @Transactional
    public PreviewSetResponseDto previewSet(Long preview_id) {
        Preview preview = previewRepository.findById(preview_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 Preview가 없습니다. id="+preview_id));

        PreviewResponseDto previewResponseDto = new PreviewResponseDto(preview);

        List<DetailTopicResponseDto> detailTopicResponseDtoList =
                preview.getDetailTopicList().stream().map(
                        DetailTopicResponseDto::new
                ).collect(Collectors.toList());

        List<ExpressionResponseDto> expressionResponseDtoList =
                preview.getExpressionList().stream().map(
                        ExpressionResponseDto::new
                ).collect(Collectors.toList());

        TopicDto topicDto = new TopicDto(preview.getTopic());

        return new PreviewSetResponseDto(previewResponseDto, topicDto, detailTopicResponseDtoList, expressionResponseDtoList);
    }
}
