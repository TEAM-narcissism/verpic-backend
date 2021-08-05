package teamverpic.verpicbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.domain.Day;
import teamverpic.verpicbackend.domain.Topic;
import teamverpic.verpicbackend.dto.TopicDto;
import teamverpic.verpicbackend.dto.TopicSearchDto;
import teamverpic.verpicbackend.service.TopicService;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Controller
public class TopicController {

    private final TopicService topicService;

    @PostMapping("/topic")
    public ResponseEntity<TopicDto> addTopic(@RequestBody Map<String, String> topic) {

        TopicDto topicDto=new TopicDto();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            topicService.createTopic(topic);
        }
        catch(Exception e){
            if(e instanceof IllegalStateException){
                topicDto.setMessage("중복 토픽 등록 불가");
                topicDto.setHttpStatus(HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(topicDto, headers, HttpStatus.BAD_REQUEST);
            }
            else if(e instanceof ParseException){
                topicDto.setMessage("날짜 요일 형식 오류");
                topicDto.setHttpStatus(HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(topicDto, headers, HttpStatus.BAD_REQUEST);
            }
        }

        topicDto.setMessage("토픽 등록 성공");
        topicDto.setHttpStatus(HttpStatus.CREATED);
        return new ResponseEntity<>(topicDto, headers, HttpStatus.CREATED);
    }

//    @GetMapping("/topic/{day}")
//    public ResponseEntity<TopicSearchDto> showTopics(@PathVariable Day day, final Pageable pageable){
//        TopicSearchDto body=new TopicSearchDto();
//        HttpHeaders headers= new HttpHeaders();
//
//        Page<Topic> topics = topicService.getTopics(pageable, day);
//
//        body.setMessage("토픽 검색 완료");
//        Map data = new HashMap<String, Object>();
//        data.put("topic list", topics);
//        body.setTopic(data);
//
//        return new ResponseEntity<>(body, headers, HttpStatus.OK);
//    }

    @GetMapping("/topic/{day}")
    public List<Topic> showTopics(@PathVariable Day day){

        List<Topic> topics = topicService.getTopics(day);

        return topics;
    }
}
