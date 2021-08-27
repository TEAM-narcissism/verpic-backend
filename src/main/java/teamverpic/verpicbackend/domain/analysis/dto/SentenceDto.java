package teamverpic.verpicbackend.domain.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class SentenceDto implements Comparable<SentenceDto> {
    private String sender;
    private String message;
    private Long userId;
    private int order;
    private double startSecond;
    private double endSecond;

    @Override
    public int compareTo(SentenceDto o) {
        if(getOrder() != o.getOrder())
            return getOrder() - o.getOrder();
        return (int)((getStartSecond() - o.getStartSecond()) * 10);
    }
}
