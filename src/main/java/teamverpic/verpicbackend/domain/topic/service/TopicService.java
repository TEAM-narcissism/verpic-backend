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
import teamverpic.verpicbackend.domain.topic.dto.TopicResponseDto;
import teamverpic.verpicbackend.domain.topic.dto.TopicSaveRequestDto;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class TopicService {

    private final TopicRepository topicRepository;

    @Transactional
    public Long save(TopicSaveRequestDto requestDto) {
        return topicRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public TopicResponseDto findById(Long id) {
        Topic entity = topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 Topic이 없습니다. id="+id));

        return new TopicResponseDto(entity);
    }

    public TopicDto createTopic(Map<String, String> topic,
                                MultipartFile multipartFile) throws ParseException, IOException {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(topic.get("studyDate"));
        Day today = getToday(date);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        Topic newtopic=Topic.builder().studyDay(today)
                .studyDate(date).numOfParticipant(0)
                .theme(topic.get("theme")).photos(fileName)
                .contentType(multipartFile.getContentType())
                .data(multipartFile.getBytes())
                .build();

        Topic save = topicRepository.save(newtopic);

        return new TopicDto(save);
    }

    public TopicDto editTopic(Map<String, String> topic, MultipartFile multipartFile,
                              Long id) throws ParseException, IOException {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(topic.get("studyDate"));
        Day today = getToday(date);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        Topic newtopic=topicRepository.getById(id);
        newtopic.setStudyDay(today);
        newtopic.setStudyDate(date);
        newtopic.setTheme(topic.get("theme"));
        newtopic.setPhotos(fileName);
        newtopic.setContentType(multipartFile.getContentType());
        newtopic.setData(multipartFile.getBytes());

        Topic save = topicRepository.save(newtopic);

        return new TopicDto(save);
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
        List<TopicDto> topicDtos =new ArrayList<>();
        topicRepository.findAll().forEach(topic-> topicDtos.add(new TopicDto(topic)));

        return topicDtos;
    }

    public TopicDto getTopicByTopicId(Long id){
        Topic topic = topicRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("해당 Topic이 존재하지 않음."));
        return new TopicDto(topic);
    }

    public List<TopicDto> getTopicsByDay(Day day){
        List<TopicDto> topicDtos =new ArrayList<>();
        topicRepository.findAllByStudyDay(day).forEach(topic-> topicDtos.add(new TopicDto(topic)));

        return topicDtos;
    }

    public List<TopicDto> getTopicsByReservations(List<StudyReservation> reservations, Day day){
        List<TopicDto> topics=new ArrayList<>();

        for(StudyReservation reservation : reservations){
            if(day==reservation.getTopic().getStudyDay()){
                topics.add(new TopicDto(reservation.getTopic()));
            }
        }
        return topics;
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
