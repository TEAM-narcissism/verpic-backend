package teamverpic.verpicbackend.dto.preview;

import lombok.Getter;
import teamverpic.verpicbackend.dto.preview.detailtopic.DetailTopicResponseDto;
import teamverpic.verpicbackend.dto.preview.expression.ExpressionResponseDto;
import teamverpic.verpicbackend.dto.preview.preview.PreviewResponseDto;

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
