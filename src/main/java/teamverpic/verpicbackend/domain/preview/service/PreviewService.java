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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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
    public PreviewResponseDto findById(Long id) {
        Preview entity = previewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 Preview가 없습니다. id="+id));

        return new PreviewResponseDto(entity);
    }

    @Transactional
    public PreviewSetResponseDto previewSet(Long preview_id) {
        Preview preview = previewRepository.findById(preview_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 Preview가 없습니다. id="+preview_id));

        PreviewResponseDto part_preview = new PreviewResponseDto(preview);

        List<DetailTopicResponseDto> part_detail_topic = new ArrayList<>();
        for (DetailTopic dt : preview.getDetailTopicList()) {
            DetailTopicResponseDto dtResponseDto = new DetailTopicResponseDto(dt);
            part_detail_topic.add(dtResponseDto);
        }

        List<ExpressionResponseDto> part_expression = new ArrayList<>();
        for (Expression ex : preview.getExpressionList()) {
            ExpressionResponseDto exResponseDto = new ExpressionResponseDto(ex);
            part_expression.add(exResponseDto);
        }

        return new PreviewSetResponseDto(part_preview, part_detail_topic, part_expression);
    }
}
