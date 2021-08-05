package teamverpic.verpicbackend.controller.preview;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.dto.preview.expression.ExpressionResponseDto;
import teamverpic.verpicbackend.dto.preview.expression.ExpressionSaveRequestDto;
import teamverpic.verpicbackend.service.preview.ExpressionService;

@RequiredArgsConstructor
@RestController
public class ExpressionController {

    private final ExpressionService expressionService;

    @PostMapping("/previews/{preview_id}/expressions")
    public Long save(@PathVariable Long preview_id, @RequestBody ExpressionSaveRequestDto requestDto) {
        return expressionService.save(preview_id, requestDto);
    }

    @GetMapping("/previews/{preview_id}/expressions/{expression_id}")
    public ExpressionResponseDto findById(@PathVariable Long expression_id) {
        return expressionService.findById(expression_id);
    }
}
