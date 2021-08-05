package teamverpic.verpicbackend.domain.preview.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.preview.domain.DetailTopic;
import teamverpic.verpicbackend.domain.preview.domain.Preview;
import teamverpic.verpicbackend.domain.preview.dto.detailtopic.DetailTopicResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.detailtopic.DetailTopicSaveRequestDto;
import teamverpic.verpicbackend.domain.preview.dao.DetailTopicRepository;
import teamverpic.verpicbackend.domain.preview.dao.PreviewRepository;

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
