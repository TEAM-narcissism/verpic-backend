package teamverpic.verpicbackend.controller.preview;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.dto.preview.PreviewSetResponseDto;
import teamverpic.verpicbackend.dto.preview.preview.PreviewResponseDto;
import teamverpic.verpicbackend.dto.preview.preview.PreviewSaveRequestDto;
import teamverpic.verpicbackend.service.preview.PreviewService;

@RequiredArgsConstructor
@RestController
public class PreviewController {

    private final PreviewService previewService;

    @PostMapping("/previews")
    public Long save(@RequestBody PreviewSaveRequestDto requestDto) {
        return previewService.save(requestDto);
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
