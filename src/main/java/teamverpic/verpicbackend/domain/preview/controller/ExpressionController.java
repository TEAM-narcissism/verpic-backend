package teamverpic.verpicbackend.domain.preview.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.domain.preview.dto.expression.ExpressionResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.expression.ExpressionSaveRequestDto;
import teamverpic.verpicbackend.domain.preview.service.ExpressionService;

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
