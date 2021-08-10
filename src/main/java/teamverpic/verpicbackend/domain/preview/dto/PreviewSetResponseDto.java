package teamverpic.verpicbackend.domain.preview.dto;

import lombok.Getter;
import teamverpic.verpicbackend.domain.preview.dto.detailtopic.DetailTopicResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.expression.ExpressionResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.preview.PreviewResponseDto;

import java.util.List;

@Getter
public class PreviewSetResponseDto {

    private PreviewResponseDto previewResponseDto;
    private List<DetailTopicResponseDto> detailTopicResponseDtoList;
    private List<ExpressionResponseDto> expressionResponseDtoList;

    public PreviewSetResponseDto(PreviewResponseDto previewResponseDto, List<DetailTopicResponseDto> detailTopicResponseDtoList, List<ExpressionResponseDto> expressionResponseDtoList) {
        this.previewResponseDto = previewResponseDto;
        this.detailTopicResponseDtoList = detailTopicResponseDtoList;
        this.expressionResponseDtoList = expressionResponseDtoList;
    }
}
