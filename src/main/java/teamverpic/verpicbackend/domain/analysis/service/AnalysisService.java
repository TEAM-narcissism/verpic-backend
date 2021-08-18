package teamverpic.verpicbackend.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import teamverpic.verpicbackend.domain.analysis.dao.AudioRepository;
import teamverpic.verpicbackend.domain.analysis.domain.AudioFile;
import teamverpic.verpicbackend.domain.matching.dao.MatchRepository;
import teamverpic.verpicbackend.domain.matching.domain.Match;
import teamverpic.verpicbackend.domain.reservation.domain.Language;
import teamverpic.verpicbackend.domain.user.dao.UserRepository;
import teamverpic.verpicbackend.domain.user.domain.User;
import teamverpic.verpicbackend.domain.user.exception.CustomAuthenticationException;

import java.io.*;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final AudioRepository audioRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    public Long saveAudio(MultipartFile multipartFile, String email, Language lang, Integer order, Long matchId) throws IOException, CustomAuthenticationException {
        String fileDir = saveFile(multipartFile, email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + email));

        Match match = matchRepository.findById(matchId).orElseThrow(() -> new IllegalArgumentException("해당 매치가 없습니다. matchId=" + matchId));

        AudioFile audioFile = AudioFile.builder()
                .fileDir(fileDir)
                .lang(lang)
                .sessionOrder(order)
                .build();
        user.addAudioFile(audioFile);
        match.addAudioFile(audioFile);
        return audioRepository.save(audioFile
        ).getAudioFileId();
    }


    public String saveFile(MultipartFile multipartFile, String userName) throws IOException {

        File dir = new File("database/audiofile/" + userName + "/");
        if(!dir.exists())
            dir.mkdir();
        String fileDir = "database/audiofile/" + fileNameGen(userName);
        File file = new File(fileDir);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        return fileDir;
    }

    public String fileNameGen(String prefix) {
        String alphabet =
                "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        int n = alphabet.length();
        String generatedString = "";
        Random r = new Random();

        for (int i = 0; i < 25; i++) // 12
            generatedString = generatedString + alphabet.charAt(r.nextInt(n));
        return prefix + "/" + generatedString + ".wav";
    }
}
