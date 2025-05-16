package com.enp.service;

import com.enp.domain.dto.response.NewsDTO;
import com.enp.domain.dto.response.NewsDetailDTO;
import com.enp.domain.dto.response.NewspaperResponseDTO;
import com.enp.domain.entity.Newspaper;
import com.enp.domain.entity.User;
import com.enp.repository.NewspaperRepository;
import com.enp.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NewspaperService {
    private final UserRepository userRepository;
    private final NewspaperRepository newspaperRepository;

    public NewspaperResponseDTO getNewsView(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("뉴스 목록 조회"+userId + "인 사용자를 찾을 수 없습니다."));

        List<NewsDTO> newsList = new ArrayList<>();
        List<Newspaper> newspaperList = newspaperRepository.findAll();
        for(int i=0;i<newspaperList.size();i++){
            String title = newspaperList.get(i).getTitle();
            String summary = newspaperList.get(i).getSummary();
            NewsDTO newsDTO = NewsDTO.builder()
                    .NewspaperId(newspaperList.get(i).getId())
                    .title(title)
                    .summary(summary)
                    .build();
            newsList.add(newsDTO);
        }

        return NewspaperResponseDTO.builder()
                .newsList(newsList)
                .build();
    }

    public NewsDetailDTO getNewsDetail(Long userId, Long newsId){
        Newspaper newspaper = newspaperRepository.findById(newsId).orElseThrow(()->new RuntimeException("뉴스 세부 조회 "+ newsId + "인 사용자를 찾을 수 없습니다."));
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("뉴스 세부 조회 "+userId + "인 사용자를 찾을 수 없습니다."));

        String title = newspaper.getTitle();
        String content = newspaper.getContent();
        String reporter = newspaper.getReporter();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = newspaper.getDate().toLocalDateTime().format(formatter);

        return NewsDetailDTO.builder()
                .title(title)
                .content(content)
                .reporter(reporter)
                .date(Timestamp.valueOf(formattedDate))
                .build();
    }

}
