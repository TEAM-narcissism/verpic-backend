package teamverpic.verpicbackend.domain.topic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.preview.dao.PreviewRepository;
import teamverpic.verpicbackend.domain.preview.domain.Preview;
import teamverpic.verpicbackend.domain.topic.domain.Day;
//import teamverpic.verpicbackend.domain.Image;
import teamverpic.verpicbackend.domain.topic.domain.Topic;
//import teamverpic.verpicbackend.dto.ImageDto;
import teamverpic.verpicbackend.domain.topic.dao.TopicRepository;
import teamverpic.verpicbackend.domain.topic.dto.TopicDto;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final PreviewRepository previewRepository;

//<<<<<<< HEAD
    public TopicDto createTopic(Map<String, String> topic) throws ParseException {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(topic.get("studyDate"));
        Day today = getToday(date);

        Topic newtopic=Topic.builder().studyDay(today)
                .studyDate(date).numOfParticipant(0)
                .theme(topic.get("theme"))
                .build();

        Topic save = topicRepository.save(newtopic);

        return new TopicDto(save);
    }

    public TopicDto editTopic(Map<String, String> topic, Long id) throws ParseException {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(topic.get("studyDate"));
        Day today = getToday(date);

        Topic newtopic=topicRepository.getById(id);
        Preview byId = previewRepository.getById(id);
        newtopic.setPreview(byId);
        newtopic.setId(id);
        newtopic.setStudyDay(today);
        newtopic.setStudyDate(date);
        newtopic.setNumOfParticipant(Integer.valueOf(topic.get("numOfParticipant")));
        newtopic.setTheme(topic.get("theme"));

        Topic save = topicRepository.save(newtopic);

        return new TopicDto(save);
//=======
//    public Long createTopic(Map<String, String> topic) throws ParseException {
//        Date studyDate=new SimpleDateFormat("MM/dd").parse(topic.get("studyDate"));
//
////        Image image=Image.builder()
////                .imgName(topic.get("imgName"))
////                .imgPath(topic.get("imgPath"))
////                .origImgName(topic.get("origImgName"))
////                .build();
//        Topic savedTopic = topicRepository.save(Topic.builder()
//                .studyDay(Day.valueOf(topic.get("studyDay")))
//                .studyDate(studyDate)
//                .theme(topic.get("theme"))
////                .image(image)
//                .origImgName("origImgName")
//                .imgName("imgName")
//                .imgPath("imgPath")
//                .build());
//
//        return savedTopic.getId();
//>>>>>>> user_profile
    }

//    public Page<Topic> getTopics(Pageable pageable, Day day){
//        List<Topic> result = topicRepository.findAllByStudyDay(day);
//
//
//
//        int count=result.size();
//        return new PageImpl<Topic>(result, pageable, count);
//    }

    public List<TopicDto> getAllTopics(){
        List<TopicDto> topicDtos=new ArrayList<>();
        topicRepository.findAll().forEach(topic->topicDtos.add(new TopicDto(topic)));

        return topicDtos;
    }

    public TopicDto getTopicByTopicId(Long id){
        Topic topic = topicRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("해당 Topic이 존재하지 않음."));
        return new TopicDto(topic);
    }

    public List<TopicDto> getTopicsByDay(Day day){
        List<TopicDto> topicDtos=new ArrayList<>();
        topicRepository.findAllByStudyDay(day).forEach(topic->topicDtos.add(new TopicDto(topic)));

        return topicDtos;
    }

    private Day getToday(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("u");

        String format = simpleDateFormat.format(date);
        Day today;

        switch(Integer.parseInt(format)){
            case 1: today=Day.MON;
                break;
            case 2: today=Day.TUES;
                break;
            case 3: today=Day.WED;
                break;
            case 4: today=Day.THUR;
                break;
            case 5: today=Day.FRI;
                break;
            case 6: today=Day.SAT;
                break;
            default: today=Day.SUN;
        }
        return today;
    }
}
