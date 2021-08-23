package teamverpic.verpicbackend.domain.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class ScriptDto {
    private Long requestUserId;
    private List<SentenceDto> messages;
}
