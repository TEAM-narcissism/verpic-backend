package teamverpic.verpicbackend.domain.preview.dto;

import lombok.Getter;
import teamverpic.verpicbackend.domain.preview.dto.detailtopic.DetailTopicResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.expression.ExpressionResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.preview.PreviewResponseDto;

import java.util.List;

@Getter
public class PreviewSetResponseDto {

    private PreviewResponseDto preview;
    private List<DetailTopicResponseDto> detailTopicList;
    private List<ExpressionResponseDto> expressionList;

    public PreviewSetResponseDto(PreviewResponseDto preview, List<DetailTopicResponseDto> detailTopicList, List<ExpressionResponseDto> expressionList) {
        this.preview = preview;
        this.detailTopicList = detailTopicList;
        this.expressionList = expressionList;
    }
}
