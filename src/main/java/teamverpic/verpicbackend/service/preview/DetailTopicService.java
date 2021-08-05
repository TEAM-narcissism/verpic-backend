package teamverpic.verpicbackend.service.preview;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.preview.DetailTopic;
import teamverpic.verpicbackend.domain.preview.Preview;
import teamverpic.verpicbackend.dto.preview.detailtopic.DetailTopicResponseDto;
import teamverpic.verpicbackend.dto.preview.detailtopic.DetailTopicSaveRequestDto;
import teamverpic.verpicbackend.repository.preview.DetailTopicRepository;
import teamverpic.verpicbackend.repository.preview.PreviewRepository;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class DetailTopicService {

    private final DetailTopicRepository detailTopicRepository;
    private final PreviewRepository previewRepository;

    @Transactional
    public Long save(Long preview_id, DetailTopicSaveRequestDto requestDto) {
        Preview preview = previewRepository.findById(preview_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 Preview가 없습니다. id="+preview_id));

        DetailTopic detailTopic = requestDto.toEntity();
        preview.addDetailTopic(detailTopic);

        return detailTopicRepository.save(detailTopic).getId();
    }

    @Transactional
    public DetailTopicResponseDto findById(Long id) {
        DetailTopic entity = detailTopicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 DetailTopic이 없습니다. id=" + id));

        return new DetailTopicResponseDto(entity);
    }
}
