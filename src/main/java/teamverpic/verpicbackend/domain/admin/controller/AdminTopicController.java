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
                           @RequestParam("expressionPronunciation") List<MultipartFile> multipartFiles,
                           Model model) throws ParseException, IOException {
        System.out.println("here1");
        TopicDto newTopic = topicService.createTopic(param, multipartFile);
        System.out.println("here2");
        PreviewSaveRequestDto previewDto=new PreviewSaveRequestDto(param.get("context"));
        String fileName[]=new String[10];
        String contentType[]=new String[10];
        byte[] data[]=new byte[10][];
        int count=0;
        System.out.println("here3");
        for (MultipartFile file : multipartFiles){
            fileName[count]=StringUtils.cleanPath(file.getOriginalFilename());
            contentType[count]=file.getContentType();
            data[count++]=file.getBytes();
        }
        System.out.println("here4");
        for(int i=0;i<10;i++){
            System.out.println("fileName["+i+"] = " + Arrays.toString(fileName[i].getBytes()));
        }
        System.out.println("here5");
        DetailTopicSaveRequestDto detailTopicDto1=new DetailTopicSaveRequestDto(param.get("detailContext1"));
        DetailTopicSaveRequestDto detailTopicDto2=new DetailTopicSaveRequestDto(param.get("detailContext2"));
        System.out.println("here6");
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
        ExpressionSaveRequestDto expressionDto6=new ExpressionSaveRequestDto(
                param.get("expressionWord6"), param.get("expressionMeaning6"), param.get("expressionExample6"),
                fileName[5], contentType[5], data[5]);
        ExpressionSaveRequestDto expressionDto7=new ExpressionSaveRequestDto(
                param.get("expressionWord7"), param.get("expressionMeaning7"), param.get("expressionExample7"),
                fileName[6], contentType[6], data[6]);
        ExpressionSaveRequestDto expressionDto8=new ExpressionSaveRequestDto(
                param.get("expressionWord8"), param.get("expressionMeaning8"), param.get("expressionExample8"),
                fileName[7], contentType[7], data[7]);
        ExpressionSaveRequestDto expressionDto9=new ExpressionSaveRequestDto(
                param.get("expressionWord9"), param.get("expressionMeaning9"), param.get("expressionExample9"),
                fileName[8], contentType[8], data[8]);
        ExpressionSaveRequestDto expressionDto10=new ExpressionSaveRequestDto(
                param.get("expressionWord10"), param.get("expressionMeaning10"), param.get("expressionExample10"),
                fileName[9], contentType[9], data[9]);
        System.out.println("here7");
        previewService.save(newTopic.getId(), previewDto);
        System.out.println("here8");
        detailTopicService.save(newTopic.getId(), detailTopicDto1);
        detailTopicService.save(newTopic.getId(), detailTopicDto2);
        System.out.println("here9");
        expressionService.save(newTopic.getId(), expressionDto1);
        expressionService.save(newTopic.getId(), expressionDto2);
        expressionService.save(newTopic.getId(), expressionDto3);
        expressionService.save(newTopic.getId(), expressionDto4);
        expressionService.save(newTopic.getId(), expressionDto5);
        expressionService.save(newTopic.getId(), expressionDto6);
        expressionService.save(newTopic.getId(), expressionDto7);
        expressionService.save(newTopic.getId(), expressionDto8);
        expressionService.save(newTopic.getId(), expressionDto9);
        expressionService.save(newTopic.getId(), expressionDto10);
        System.out.println("here10");
        List<TopicDto> allTopics = topicService.getAllTopics();
        model.addAttribute("topics", allTopics);
        System.out.println("here11");
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
        String base64EncodedAudio6 = Base64.encodeBase64String(expressionDtoList.get(5).getPronounce());
        String base64EncodedAudio7 = Base64.encodeBase64String(expressionDtoList.get(6).getPronounce());
        String base64EncodedAudio8 = Base64.encodeBase64String(expressionDtoList.get(7).getPronounce());
        String base64EncodedAudio9 = Base64.encodeBase64String(expressionDtoList.get(8).getPronounce());
        String base64EncodedAudio10 = Base64.encodeBase64String(expressionDtoList.get(9).getPronounce());

        model.addAttribute("base64EncodedAudio1", base64EncodedAudio1);
        model.addAttribute("base64EncodedAudio2", base64EncodedAudio2);
        model.addAttribute("base64EncodedAudio3", base64EncodedAudio3);
        model.addAttribute("base64EncodedAudio4", base64EncodedAudio4);
        model.addAttribute("base64EncodedAudio5", base64EncodedAudio5);
        model.addAttribute("base64EncodedAudio6", base64EncodedAudio6);
        model.addAttribute("base64EncodedAudio7", base64EncodedAudio7);
        model.addAttribute("base64EncodedAudio8", base64EncodedAudio8);
        model.addAttribute("base64EncodedAudio9", base64EncodedAudio9);
        model.addAttribute("base64EncodedAudio10", base64EncodedAudio10);

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
        String fileName[]=new String[10];
        String contentType[]=new String[10];
        byte[] data[]=new byte[10][];
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
        ExpressionSaveRequestDto expressionDto6=new ExpressionSaveRequestDto(
                param.get("expressionWord6"), param.get("expressionMeaning6"), param.get("expressionExample6"),
                fileName[5], contentType[5], data[5]);
        ExpressionSaveRequestDto expressionDto7=new ExpressionSaveRequestDto(
                param.get("expressionWord7"), param.get("expressionMeaning7"), param.get("expressionExample7"),
                fileName[6], contentType[6], data[6]);
        ExpressionSaveRequestDto expressionDto8=new ExpressionSaveRequestDto(
                param.get("expressionWord8"), param.get("expressionMeaning8"), param.get("expressionExample8"),
                fileName[7], contentType[7], data[7]);
        ExpressionSaveRequestDto expressionDto9=new ExpressionSaveRequestDto(
                param.get("expressionWord9"), param.get("expressionMeaning9"), param.get("expressionExample9"),
                fileName[8], contentType[8], data[8]);
        ExpressionSaveRequestDto expressionDto10=new ExpressionSaveRequestDto(
                param.get("expressionWord10"), param.get("expressionMeaning10"), param.get("expressionExample10"),
                fileName[9], contentType[9], data[9]);
        expressionDtos.add(expressionDto1);
        expressionDtos.add(expressionDto2);
        expressionDtos.add(expressionDto3);
        expressionDtos.add(expressionDto4);
        expressionDtos.add(expressionDto5);
        expressionDtos.add(expressionDto6);
        expressionDtos.add(expressionDto7);
        expressionDtos.add(expressionDto8);
        expressionDtos.add(expressionDto9);
        expressionDtos.add(expressionDto10);

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
        String base64EncodedAudio6 = Base64.encodeBase64String(expressionDtoList.get(5).getPronounce());
        String base64EncodedAudio7 = Base64.encodeBase64String(expressionDtoList.get(6).getPronounce());
        String base64EncodedAudio8 = Base64.encodeBase64String(expressionDtoList.get(7).getPronounce());
        String base64EncodedAudio9 = Base64.encodeBase64String(expressionDtoList.get(8).getPronounce());
        String base64EncodedAudio10 = Base64.encodeBase64String(expressionDtoList.get(9).getPronounce());

        model.addAttribute("base64EncodedAudio1", base64EncodedAudio1);
        model.addAttribute("base64EncodedAudio2", base64EncodedAudio2);
        model.addAttribute("base64EncodedAudio3", base64EncodedAudio3);
        model.addAttribute("base64EncodedAudio4", base64EncodedAudio4);
        model.addAttribute("base64EncodedAudio5", base64EncodedAudio5);
        model.addAttribute("base64EncodedAudio6", base64EncodedAudio6);
        model.addAttribute("base64EncodedAudio7", base64EncodedAudio7);
        model.addAttribute("base64EncodedAudio8", base64EncodedAudio8);
        model.addAttribute("base64EncodedAudio9", base64EncodedAudio9);
        model.addAttribute("base64EncodedAudio10", base64EncodedAudio10);
        model.addAttribute("preview", preview);
        model.addAttribute("detailTopicList", detailTopicDtoList);
        model.addAttribute("expressionList", expressionDtoList);

        return "redirect:/administration/topics/preview/{previewId}";
    }
}
