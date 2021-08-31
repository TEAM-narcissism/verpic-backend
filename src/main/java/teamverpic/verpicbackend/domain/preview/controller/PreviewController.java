package teamverpic.verpicbackend.domain.preview.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.domain.preview.dto.PreviewSetResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.preview.PreviewResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.preview.PreviewSaveRequestDto;
import teamverpic.verpicbackend.domain.preview.service.PreviewService;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class PreviewController {

    private final PreviewService previewService;

    @PostMapping("/topics/{topic_id}/previews")
    public Long save(@PathVariable Long topic_id, @RequestBody PreviewSaveRequestDto requestDto) {
        return previewService.save(topic_id, requestDto);
    }

    @GetMapping("/previews/{preview_id}")
    public PreviewResponseDto findById(@PathVariable Long preview_id) {
        return previewService.findById(preview_id);
    }

    @GetMapping("/previewsets/{preview_id}")
    public PreviewSetResponseDto previewSet(@PathVariable Long preview_id) {
        return previewService.previewSet(preview_id);
    }
}
