package teamverpic.verpicbackend.domain.topic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;
import teamverpic.verpicbackend.domain.topic.domain.Day;
import teamverpic.verpicbackend.domain.topic.domain.Topic;
import teamverpic.verpicbackend.domain.topic.dao.TopicRepository;
import teamverpic.verpicbackend.domain.topic.dto.TopicDto;


import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class TopicService {
    private final TopicRepository topicRepository;

    public TopicDto createTopic(Map<String, String> topicMap,
                                MultipartFile multipartFile) throws ParseException, IOException {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(topicMap.get("studyDate"));
        Day today = getToday(date);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        Topic topic=Topic.builder().studyDay(today)
                .studyDate(date).numOfParticipant(0)
                .korTheme(topicMap.get("korTheme"))
                .engTheme(topicMap.get("engTheme"))
                .photos(fileName)
                .contentType(multipartFile.getContentType())
                .data(multipartFile.getBytes())
                .build();

        Topic save = topicRepository.save(topic);

        return new TopicDto(save);
    }

    public TopicDto editTopic(Map<String, String> topicMap, MultipartFile multipartFile,
                              Long id) throws ParseException, IOException {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(topicMap.get("studyDate"));
        Day today = getToday(date);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        Topic topic=topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 Topic이 없습니다. id="+id));

        topic.update(date, today, topicMap.get("korTheme"), topicMap.get("engTheme"), fileName, multipartFile.getContentType(), multipartFile.getBytes());

        Topic save = topicRepository.save(topic);
        return new TopicDto(save);
    }


    public List<TopicDto> getAllTopics(){
        return topicRepository.findAll().stream().map(
                TopicDto::new
        ).collect(Collectors.toList());
    }

    public TopicDto getTopicByTopicId(Long id){
        Topic topic = topicRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("해당 Topic이 존재하지 않음."));
        return new TopicDto(topic);
    }

    public List<TopicDto> getTopicsByReservationsLaterThanToday(List<StudyReservation> reservations) throws ParseException {
        SimpleDateFormat topicDateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date=new Date();
        String today = topicDateFormat.format(date);
        Date parsedToday = topicDateFormat.parse(today);

        List<TopicDto> topics=new ArrayList<>();

        for(StudyReservation reservation : reservations){
            Date studyDate = reservation.getTopic().getStudyDate();
            if(parsedToday.compareTo(studyDate)<=0){
                topics.add(new TopicDto(reservation.getTopic()));
            }
        }

        topics.sort(Comparator.comparing(TopicDto::getStudyDate));
        return topics;
    }

    public List<TopicDto> getTopicsLaterThanToday() throws ParseException {
        SimpleDateFormat topicDateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date=new Date();
        String today = topicDateFormat.format(date);
        Date parsedToday = topicDateFormat.parse(today);

        List<TopicDto> allTopicsLaterThanToday=new ArrayList<>();
        List<Topic> allTopics = topicRepository.findAll();

        for(Topic topic : allTopics){
            Date parsedTopicDate = topicDateFormat.parse(String.valueOf(topic.getStudyDate()));
            if(parsedToday.compareTo(parsedTopicDate)<=0){
                System.out.println("parsedTopicDate = " + parsedTopicDate);
                allTopicsLaterThanToday.add(new TopicDto(topic));
            }
        }
        allTopicsLaterThanToday.sort(Comparator.comparing(TopicDto::getStudyDate));

        return allTopicsLaterThanToday;
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
