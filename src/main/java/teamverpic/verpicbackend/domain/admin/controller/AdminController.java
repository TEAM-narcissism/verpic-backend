package teamverpic.verpicbackend.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.domain.topic.domain.Day;
import teamverpic.verpicbackend.domain.topic.dto.TopicDto;
import teamverpic.verpicbackend.domain.topic.service.TopicService;

import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;

@RequiredArgsConstructor
@Controller
@RequestMapping("/administration/topics")
public class AdminController {

    private final TopicService topicService;

    @GetMapping("")
    public String showTopicList(Model model){
        List<TopicDto> allTopics = topicService.getAllTopics();
        model.addAttribute("topics", allTopics);
        return "administration/topics";
    }

    @GetMapping("/{topicId}")
    public String item(@PathVariable Long topicId, Model model) {
        TopicDto topic = topicService.getTopicByTopicId(topicId);
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

        TopicDto newTopic = topicService.createTopic(param);
        model.addAttribute("topic", newTopic);

        return "administration/topic";
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
        return "administration/editTopicForm";
    }

    @PostMapping("/{topicId}/edit")
    public String edit(@PathVariable Long topicId,
                       @RequestParam Map<String, String> param,
                       Model model) throws Exception {

        topicService.editTopic(param, topicId);
        return "redirect:/administration/topics/{topicId}";
    }
}
