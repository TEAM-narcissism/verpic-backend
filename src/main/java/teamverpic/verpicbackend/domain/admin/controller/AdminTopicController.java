package teamverpic.verpicbackend.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import teamverpic.verpicbackend.domain.preview.dto.detailtopic.DetailTopicResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.detailtopic.DetailTopicSaveRequestDto;
import teamverpic.verpicbackend.domain.preview.dto.expression.ExpressionResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.expression.ExpressionSaveRequestDto;
import teamverpic.verpicbackend.domain.preview.dto.preview.PreviewResponseDto;
import teamverpic.verpicbackend.domain.preview.dto.preview.PreviewSaveRequestDto;
import teamverpic.verpicbackend.domain.preview.service.DetailTopicService;
import teamverpic.verpicbackend.domain.preview.service.ExpressionService;
import teamverpic.verpicbackend.domain.preview.service.PreviewService;
import teamverpic.verpicbackend.domain.topic.domain.Day;
import teamverpic.verpicbackend.domain.topic.dto.TopicDto;
import teamverpic.verpicbackend.domain.topic.service.TopicService;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;

@RequiredArgsConstructor
@Controller
@RequestMapping("/administration/topics")
public class AdminTopicController {

    private final TopicService topicService;
    private final PreviewService previewService;
    private final DetailTopicService detailTopicService;
    private final ExpressionService expressionService;

    @GetMapping("")
    public String showTopicList(Model model){
        List<TopicDto> allTopics = topicService.getAllTopics();
        model.addAttribute("topics", allTopics);
        return "administration/topics/topics";
    }

    @GetMapping("/{topicId}")
    public String showTopic(@PathVariable Long topicId, Model model) {
        TopicDto topic = topicService.getTopicByTopicId(topicId);
        String base64EncodedImage = Base64.encodeBase64String(topic.getData());

        model.addAttribute("topic", topic);
        model.addAttribute("base64EncodedImage", base64EncodedImage);
        return "administration/topics/topic";
    }

    @GetMapping("/addTopic")
    public String addFrom(Model model) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date d=new Date();
        String today=format.format(d);

        model.addAttribute("today", today);
        return "administration/topics/addTopicForm";
    }

    @PostMapping("/addTopic")
    public String addTopic(@RequestParam Map<String, String> param,
                           @RequestParam("image") MultipartFile multipartFile,
                           Model model) throws ParseException, IOException {

        TopicDto newTopic = topicService.createTopic(param, multipartFile);
        model.addAttribute("topicId", newTopic.getId());

        return "administration/topics/addPreviewForm";
    }

    @PostMapping("/addTopic/addPreview")
    public String addPreview(@RequestParam Map<String, String> param,
                             @RequestParam("expressionPronunciation") List<MultipartFile> multipartFiles,
                             Model model) throws IOException {
        Long topicId = Long.valueOf(param.get("previewId"));
        Long previewId = Long.valueOf(param.get("previewId"));
        PreviewSaveRequestDto previewDto=new PreviewSaveRequestDto(param.get("context"));
        String fileName[]=new String[5];
        String contentType[]=new String[5];
        byte[] data[]=new byte[5][];
        int count=0;

        for (MultipartFile file : multipartFiles){
            fileName[count]=StringUtils.cleanPath(file.getOriginalFilename());
            contentType[count]=file.getContentType();
            data[count++]=file.getBytes();
        }

        for(int i=0;i<5;i++){
            System.out.println("fileName["+i+"] = " + Arrays.toString(fileName[i].getBytes()));
        }

        DetailTopicSaveRequestDto detailTopicDto1=new DetailTopicSaveRequestDto(param.get("detailContext1"));
        DetailTopicSaveRequestDto detailTopicDto2=new DetailTopicSaveRequestDto(param.get("detailContext2"));

        ExpressionSaveRequestDto expressionDto1=new ExpressionSaveRequestDto(
                param.get("expressionWord1"), param.get("expressionMeaning1"), param.get("expressionExample1"),
                fileName[0], contentType[0], data[0]);
        ExpressionSaveRequestDto expressionDto2=new ExpressionSaveRequestDto(
                param.get("expressionWord2"), param.get("expressionMeaning2"), param.get("expressionExample2"),
                fileName[1], contentType[1], data[1]);
        ExpressionSaveRequestDto expressionDto3=new ExpressionSaveRequestDto(
                param.get("expressionWord3"), param.get("expressionMeaning3"), param.get("expressionExample3"),
                fileName[2], contentType[2], data[2]);
        ExpressionSaveRequestDto expressionDto4=new ExpressionSaveRequestDto(
                param.get("expressionWord4"), param.get("expressionMeaning4"), param.get("expressionExample4"),
                fileName[3], contentType[3], data[3]);
        ExpressionSaveRequestDto expressionDto5=new ExpressionSaveRequestDto(
                param.get("expressionWord5"), param.get("expressionMeaning5"), param.get("expressionExample5"),
                fileName[4], contentType[4], data[4]);

        previewService.save(topicId, previewDto);

        detailTopicService.save(previewId, detailTopicDto1);
        detailTopicService.save(previewId, detailTopicDto2);

        expressionService.save(previewId, expressionDto1);
        expressionService.save(previewId, expressionDto2);
        expressionService.save(previewId, expressionDto3);
        expressionService.save(previewId, expressionDto4);
        expressionService.save(previewId, expressionDto5);

        List<TopicDto> allTopics = topicService.getAllTopics();
        model.addAttribute("topics", allTopics);

        return "redirect:/administration/topics";
    }

    @GetMapping("/{topicId}/edit")
    public String editTopicForm(@PathVariable Long topicId, Model model) {
        List<Day> days=new ArrayList<>();
        days.add(Day.MON); days.add(Day.TUES); days.add(Day.WED); days.add(Day.THUR);
        days.add(Day.FRI); days.add(Day.SAT); days.add(Day.SUN);
        TopicDto topic = topicService.getTopicByTopicId(topicId);

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date d=new Date();
        String today=format.format(d);

        model.addAttribute("days", days);
        model.addAttribute("topic", topic);
        model.addAttribute("today", today);
        return "administration/topics/editTopicForm";
    }

    @PostMapping("/{topicId}/edit")
    public String editTopic(@PathVariable Long topicId,
                            @RequestParam Map<String, String> param,
                            @RequestParam("image") MultipartFile multipartFile,
                            Model model) throws Exception {

        topicService.editTopic(param, multipartFile, topicId);
        return "redirect:/administration/topics/{topicId}";
    }

    @GetMapping("/preview/{previewId}")
    public String showPreview(@PathVariable Long previewId, Model model){
        PreviewResponseDto previewDto=previewService.findById(previewId);
        List<DetailTopicResponseDto> detailTopicDtoList = detailTopicService.findByPreviewId(previewId);
        List<ExpressionResponseDto> expressionDtoList=expressionService.findByPreviewId(previewId);
        String base64EncodedAudio1 = Base64.encodeBase64String(expressionDtoList.get(0).getPronounce());
        String base64EncodedAudio2 = Base64.encodeBase64String(expressionDtoList.get(1).getPronounce());
        String base64EncodedAudio3 = Base64.encodeBase64String(expressionDtoList.get(2).getPronounce());
        String base64EncodedAudio4 = Base64.encodeBase64String(expressionDtoList.get(3).getPronounce());
        String base64EncodedAudio5 = Base64.encodeBase64String(expressionDtoList.get(4).getPronounce());

        model.addAttribute("base64EncodedAudio1", base64EncodedAudio1);
        model.addAttribute("base64EncodedAudio2", base64EncodedAudio2);
        model.addAttribute("base64EncodedAudio3", base64EncodedAudio3);
        model.addAttribute("base64EncodedAudio4", base64EncodedAudio4);
        model.addAttribute("base64EncodedAudio5", base64EncodedAudio5);
        model.addAttribute("detailTopicList", detailTopicDtoList);
        model.addAttribute("expressionList", expressionDtoList);
        model.addAttribute("preview", previewDto);
        model.addAttribute("previewId", previewId);
        return "/administration/topics/preview";
    }

    @GetMapping("/preview/{previewId}/edit")
    public String editPreviewForm(@PathVariable Long previewId, Model model){
        PreviewResponseDto previewDto=previewService.findById(previewId);
        List<DetailTopicResponseDto> detailTopicDtoList = detailTopicService.findByPreviewId(previewId);
        List<ExpressionResponseDto> expressionDtoList=expressionService.findByPreviewId(previewId);

        model.addAttribute("detailTopicList", detailTopicDtoList);
        model.addAttribute("expressionList", expressionDtoList);
        model.addAttribute("preview", previewDto);
        model.addAttribute("previewId", previewId);
        return "/administration/topics/editPreviewForm";
    }

    @PostMapping("/preview/{previewId}/edit")
    public String editPreviewForm(@PathVariable Long previewId,
                                  @RequestParam Map<String, String> param,
                                  @RequestParam("expressionPronunciation") List<MultipartFile> multipartFiles,
                                  Model model) throws IOException {
        Long topicId = Long.valueOf(param.get("id"));
        PreviewSaveRequestDto previewDto=new PreviewSaveRequestDto(param.get("context"));

        List<DetailTopicSaveRequestDto> detailTopicDtos=new ArrayList<>();
        DetailTopicSaveRequestDto detailTopicDto1=new DetailTopicSaveRequestDto(param.get("detailContext1"));
        DetailTopicSaveRequestDto detailTopicDto2=new DetailTopicSaveRequestDto(param.get("detailContext2"));
        detailTopicDtos.add(detailTopicDto1);
        detailTopicDtos.add(detailTopicDto2);

        List<ExpressionSaveRequestDto> expressionDtos=new ArrayList<>();
        String fileName[]=new String[5];
        String contentType[]=new String[5];
        byte[] data[]=new byte[5][];
        int count=0;

        for (MultipartFile file : multipartFiles){
            fileName[count]=StringUtils.cleanPath(file.getOriginalFilename());
            contentType[count]=file.getContentType();
            data[count++]=file.getBytes();
        }

        ExpressionSaveRequestDto expressionDto1=new ExpressionSaveRequestDto(
                param.get("expressionWord1"), param.get("expressionMeaning1"), param.get("expressionExample1"),
                fileName[0], contentType[0], data[0]);
        ExpressionSaveRequestDto expressionDto2=new ExpressionSaveRequestDto(
                param.get("expressionWord2"), param.get("expressionMeaning2"), param.get("expressionExample2"),
                fileName[1], contentType[1], data[1]);
        ExpressionSaveRequestDto expressionDto3=new ExpressionSaveRequestDto(
                param.get("expressionWord3"), param.get("expressionMeaning3"), param.get("expressionExample3"),
                fileName[2], contentType[2], data[2]);
        ExpressionSaveRequestDto expressionDto4=new ExpressionSaveRequestDto(
                param.get("expressionWord4"), param.get("expressionMeaning4"), param.get("expressionExample4"),
                fileName[3], contentType[3], data[3]);
        ExpressionSaveRequestDto expressionDto5=new ExpressionSaveRequestDto(
                param.get("expressionWord5"), param.get("expressionMeaning5"), param.get("expressionExample5"),
                fileName[4], contentType[4], data[4]);
        expressionDtos.add(expressionDto1);
        expressionDtos.add(expressionDto2);
        expressionDtos.add(expressionDto3);
        expressionDtos.add(expressionDto4);
        expressionDtos.add(expressionDto5);

        previewService.edit(topicId, previewDto);
        detailTopicService.edit(previewId, detailTopicDtos);
        expressionService.edit(previewId, expressionDtos);

        PreviewResponseDto preview = previewService.findById(previewId);
        List<DetailTopicResponseDto> detailTopicDtoList = detailTopicService.findByPreviewId(previewId);
        List<ExpressionResponseDto> expressionDtoList=expressionService.findByPreviewId(previewId);

        String base64EncodedAudio1 = Base64.encodeBase64String(expressionDtoList.get(0).getPronounce());
        String base64EncodedAudio2 = Base64.encodeBase64String(expressionDtoList.get(1).getPronounce());
        String base64EncodedAudio3 = Base64.encodeBase64String(expressionDtoList.get(2).getPronounce());
        String base64EncodedAudio4 = Base64.encodeBase64String(expressionDtoList.get(3).getPronounce());
        String base64EncodedAudio5 = Base64.encodeBase64String(expressionDtoList.get(4).getPronounce());

        model.addAttribute("base64EncodedAudio1", base64EncodedAudio1);
        model.addAttribute("base64EncodedAudio2", base64EncodedAudio2);
        model.addAttribute("base64EncodedAudio3", base64EncodedAudio3);
        model.addAttribute("base64EncodedAudio4", base64EncodedAudio4);
        model.addAttribute("base64EncodedAudio5", base64EncodedAudio5);
        model.addAttribute("preview", preview);
        model.addAttribute("detailTopicList", detailTopicDtoList);
        model.addAttribute("expressionList", expressionDtoList);

        return "redirect:/administration/topics/preview/{previewId}";
    }
}
