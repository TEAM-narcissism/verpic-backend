package teamverpic.verpicbackend.domain.preview.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.preview.domain.Expression;
import teamverpic.verpicbackend.domain.preview.domain.Preview;
import teamverpic.verpicbackend.domain.preview.dto.expression.ExpressionResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.expression.ExpressionSaveRequestDto;
import teamverpic.verpicbackend.domain.preview.dao.ExpressionRepository;
import teamverpic.verpicbackend.domain.preview.dao.PreviewRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ExpressionService {

    private final ExpressionRepository expressionRepository;
    private final PreviewRepository previewRepository;

    @Transactional
    public Long save(Long preview_id, ExpressionSaveRequestDto requestDto) {
        Preview preview = previewRepository.findById(preview_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 Preview가 없습니다. id="+preview_id));

        Expression expression = requestDto.toEntity();

        preview.setExpressionList(new ArrayList<>());
        preview.addExpression(expression);

        return expressionRepository.save(expression).getId();
    }

    @Transactional
    public void edit(Long preview_id, List<ExpressionSaveRequestDto> requestDtos){
        List<Expression> expressionList = expressionRepository.findByPreviewId(preview_id);
        Preview preview=previewRepository.getById(preview_id);

        int count=0;
        for(Expression expression : expressionList){

            expression.setWord(requestDtos.get(count).getWord());
            expression.setMeaning(requestDtos.get(count).getMeaning());
            expression.setExample(requestDtos.get(count).getExample());
            expression.setAudios(requestDtos.get(count).getAudios());
            expression.setContentType(requestDtos.get(count).getContentType());
            expression.setPronounce(requestDtos.get(count++).getPronounce());
            expression.setPreview(preview);
        }

        expressionList.forEach(expression ->{
            expressionRepository.save(expression);
        });
    }

    @Transactional
    public ExpressionResponseDto findById(Long id) {
        Expression entity = expressionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 Expression이 없습니다. id=" + id));

        return new ExpressionResponseDto(entity);
    }

    @Transactional
    public List<ExpressionResponseDto> findByPreviewId(Long id){
        List<Expression> list = expressionRepository.findByPreviewId(id);
        List<ExpressionResponseDto> expressionList=new ArrayList<>();

        list.forEach(expression->{
            expressionList.add(new ExpressionResponseDto(expression));
        });

        return expressionList;
    }
}
