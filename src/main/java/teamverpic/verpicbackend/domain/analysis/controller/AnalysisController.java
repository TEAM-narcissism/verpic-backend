package teamverpic.verpicbackend.domain.analysis.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.language.bm.Lang;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import teamverpic.verpicbackend.domain.analysis.dao.AudioRepository;
import teamverpic.verpicbackend.domain.analysis.service.AnalysisService;
import teamverpic.verpicbackend.domain.reservation.domain.Language;
import teamverpic.verpicbackend.domain.user.dao.UserRepository;
import teamverpic.verpicbackend.domain.user.dto.HttpResponseDto;
import teamverpic.verpicbackend.domain.user.exception.CustomAuthenticationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping(value = "/save")
    public ResponseEntity<HttpResponseDto> saveAudio(
            Authentication authentication,
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("lang") String langString,
            @RequestParam("order") String order,
            @RequestParam("matchId") String matchId
    ) throws IOException, CustomAuthenticationException{
        HttpHeaders headers= new HttpHeaders();
        HttpResponseDto body = new HttpResponseDto();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        if(isAnonymousUser(authentication.getName())) {
            throw new CustomAuthenticationException("로그인 오류");
        }

        Language lang;
        if (langString.equals("ko"))
            lang = Language.KOR;
        else
            lang = Language.ENG;
        analysisService.saveAudio(
                multipartFile,
                authentication.getName(),
                lang,
                Integer.parseInt(order),
                Long.parseLong(matchId)
        );

        body.setHttpStatus(HttpStatus.OK);
        body.setMessage("음성 파일 저장 완료");
        body.setData(null);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/create-script")
    public ResponseEntity<HttpResponseDto> createScript(
            Authentication authentication,
            @RequestParam("matchId") String matchId
    ) throws IOException, CustomAuthenticationException, ExecutionException, InterruptedException {
        HttpHeaders headers= new HttpHeaders();
        HttpResponseDto body = new HttpResponseDto();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        if(isAnonymousUser(authentication.getName())) {
            throw new CustomAuthenticationException("로그인 오류");
        }

        analysisService.soundToText(authentication.getName(), Long.parseLong(matchId));

        body.setHttpStatus(HttpStatus.OK);
        body.setMessage("완료");
        body.setData(null);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    private boolean isAnonymousUser(String username) {
        return username == "anonymousUser";
    }

}
