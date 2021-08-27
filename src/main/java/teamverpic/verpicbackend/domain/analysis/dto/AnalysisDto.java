package teamverpic.verpicbackend.domain.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisDto {
    private List<MostUsedWordDto> mostUsedWordList;
    private double wpm;
    private int order;
}
