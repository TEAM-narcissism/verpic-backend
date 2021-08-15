package teamverpic.verpicbackend.domain.preview.service;

import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.preview.domain.DetailTopic;
import teamverpic.verpicbackend.domain.preview.domain.Expression;
import teamverpic.verpicbackend.domain.preview.domain.Preview;
import teamverpic.verpicbackend.domain.preview.dto.detailtopic.DetailTopicResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.detailtopic.DetailTopicSaveRequestDto;
import teamverpic.verpicbackend.domain.preview.dao.DetailTopicRepository;
import teamverpic.verpicbackend.domain.preview.dao.PreviewRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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

        preview.setDetailTopicList(new ArrayList<>());
        preview.addDetailTopic(detailTopic);

        return detailTopicRepository.save(detailTopic).getId();
    }

    @Transactional
    public void edit(Long preview_id, List<DetailTopicSaveRequestDto> requestDtos){
        List<DetailTopic> detailTopicList = detailTopicRepository.findByPreviewId(preview_id);
//        Preview preview=previewRepository.getById(preview_id);

        int count=0;
        for(DetailTopic detailTopic : detailTopicList){

            detailTopic.setContext(requestDtos.get(count++).getContext());
//            detailTopic.setPreview(preview);
        }

        detailTopicList.forEach(detailTopic ->{
            detailTopicRepository.save(detailTopic);
        });
    }

    @Transactional
    public DetailTopicResponseDto findById(Long id) {
        DetailTopic entity = detailTopicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 DetailTopic이 없습니다. id=" + id));

        return new DetailTopicResponseDto(entity);
    }

    @Transactional
    public List<DetailTopicResponseDto> findByPreviewId(Long id){
        List<DetailTopic> detailTopicList=detailTopicRepository.findByPreviewId(id);
        List<DetailTopicResponseDto> entities=new ArrayList<>();

        detailTopicList.forEach(detailTopic->{
            entities.add(new DetailTopicResponseDto(detailTopic));
        });

        return entities;
    }
}
