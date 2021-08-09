package teamverpic.verpicbackend.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.domain.topic.dao.TopicRepository;
import teamverpic.verpicbackend.domain.topic.domain.Day;
import teamverpic.verpicbackend.domain.topic.domain.Topic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/administration/topics")
public class AdminController {

    private final TopicRepository topicRepository;

    @GetMapping("")
    public String showTopicList(Model model){
        List<Topic> topics = topicRepository.findAll();
        model.addAttribute("topics", topics);
        return "administration/topics";
    }

    @GetMapping("/{topicId}")
    public String item(@PathVariable Long topicId, Model model) {
        Topic topic = topicRepository.findById(topicId).get();
        model.addAttribute("topic", topic);
        return "administration/topic";
    }

    @GetMapping("/add")
    public String addFrom(Model model) {

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date d=new Date();
        String today=format.format(d);

        model.addAttribute("today", today);
        return "administration/addTopicForm";
    }

    @PostMapping("/add")
    public String addTopic(@RequestParam Map<String, String> param, Model model) throws ParseException {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(param.get("studyDate"));
        Day today = getToday(date);

        Topic newtopic=Topic.builder().studyDay(today)
                .studyDate(date).numOfParticipant(0)
                .previewId(9l).theme(param.get("theme"))
                .origImgName("set").imgName("set")
                .imgPath("set").build();

        topicRepository.save(newtopic);
        model.addAttribute("topic", newtopic);

        return "administration/topic";
    }

    @GetMapping("/{topicId}/edit")
    public String editTopicForm(@PathVariable Long topicId, Model model) {
        List<Day> days=new ArrayList<>();
        days.add(Day.MON); days.add(Day.TUES); days.add(Day.WED); days.add(Day.THUR);
        days.add(Day.FRI); days.add(Day.SAT); days.add(Day.SUN);
        Topic topic = topicRepository.findById(topicId).get();

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date d=new Date();
        String today=format.format(d);

        model.addAttribute("days", days);
        model.addAttribute("topic", topic);
        model.addAttribute("today", today);
        return "administration/editTopicForm";
    }

    @PostMapping("/{topicId}/edit")
    public String edit(@PathVariable Long topicId,
                       @RequestParam Map<String, String> param,
                       Model model) throws Exception {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(param.get("studyDate"));
        Day day = getToday(date);

        Topic updatedtopic=Topic.builder()
                .topicId(Long.valueOf(param.get("topicId")))
                .studyDay(day)
                .studyDate(date)
                .numOfParticipant(Integer.valueOf(param.get("numOfParticipant")))
                .previewId(Long.valueOf(param.get("previewId")))
                .theme(param.get("theme"))
                .origImgName("changed")
                .imgName("changed")
                .imgPath("changed")
                .build();

        topicRepository.save(updatedtopic);
        return "redirect:/administration/topics/{topicId}";
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
