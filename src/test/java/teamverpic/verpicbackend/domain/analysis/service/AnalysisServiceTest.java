package teamverpic.verpicbackend.domain.analysis.service;

import org.json.JSONArray;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import teamverpic.verpicbackend.domain.reservation.domain.Language;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AnalysisServiceTest {

    @Autowired
    private AnalysisService analysisService;

    public void AnalysisTest() throws IOException {
        JSONArray objects = analysisService.requestMostUsedWord("hi hello my name hello hello", Language.ENG);
        System.out.println("objects = " + objects);
    }

}