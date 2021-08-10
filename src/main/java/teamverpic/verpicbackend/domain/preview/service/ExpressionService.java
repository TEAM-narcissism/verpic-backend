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
        preview.addExpression(expression);

        return expressionRepository.save(expression).getId();
    }

    @Transactional
    public ExpressionResponseDto findById(Long id) {
        Expression entity = expressionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 Expression이 없습니다. id=" + id));

        return new ExpressionResponseDto(entity);
    }
}
