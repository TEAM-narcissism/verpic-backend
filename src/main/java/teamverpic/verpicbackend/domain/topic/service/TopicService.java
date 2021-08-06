package teamverpic.verpicbackend.domain.topic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.topic.domain.Day;
//import teamverpic.verpicbackend.domain.Image;
import teamverpic.verpicbackend.domain.topic.domain.Topic;
//import teamverpic.verpicbackend.dto.ImageDto;
import teamverpic.verpicbackend.domain.topic.dao.TopicRepository;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public Long createTopic(Map<String, String> topic) throws ParseException {
        Date studyDate=new SimpleDateFormat("MM/dd").parse(topic.get("studyDate"));

//        Image image=Image.builder()
//                .imgName(topic.get("imgName"))
//                .imgPath(topic.get("imgPath"))
//                .origImgName(topic.get("origImgName"))
//                .build();
        Topic savedTopic = topicRepository.save(Topic.builder()
                .studyDay(Day.valueOf(topic.get("studyDay")))
                .studyDate(studyDate)
                .numOfParticipant(Integer.parseInt(topic.get("numOfParticipant")))
                .previewId(Long.valueOf(topic.get("previewId")))
                .theme(topic.get("theme"))
//                .image(image)
                .origImgName("origImgName")
                .imgName("imgName")
                .imgPath("imgPath")
                .build());

        return savedTopic.getTopicId();
    }

//    public Page<Topic> getTopics(Pageable pageable, Day day){
//        List<Topic> result = topicRepository.findAllByStudyDay(day);
//
//
//
//        int count=result.size();
//        return new PageImpl<Topic>(result, pageable, count);
//    }
    public List<Topic> getTopics(Day day){
        return topicRepository.findAllByStudyDay(day);
    }
}
