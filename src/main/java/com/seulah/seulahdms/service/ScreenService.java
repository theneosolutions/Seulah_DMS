package com.seulah.seulahdms.service;

import com.seulah.seulahdms.entity.ScreenName;
import com.seulah.seulahdms.repository.ScreenRepository;
import com.seulah.seulahdms.request.ScreenRequest;
import com.seulah.seulahdms.response.ScreenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ScreenService {
    private final ScreenRepository screenRepository;
    ScreenResponse sc;

    public ScreenService(ScreenRepository screenRepository) {
        this.screenRepository = screenRepository;
    }

    public ResponseEntity<?> addScreen(ScreenRequest screenRequest) {
        if(screenRequest!=null){
            ScreenName screenName = new ScreenName();
            for (Long id: screenRequest.getQuestionIds()){
                screenName.setScreenHeading(screenRequest.getScreenHeading());
                screenName.setSetId(screenRequest.getSetId());
                screenName.setQuestionIds(id);
                screenRepository.save(screenName);
            }

        }

        return ResponseEntity.ok().body(null);

    }
    public  ScreenName getScreen(){
      return screenRepository.findByScreenHeading("home");
    }
    public ResponseEntity<?> getQuestionCheck(String questionId) {
        long id = Long.parseLong(questionId);
        if (screenRepository.existsByQuestionIds(id)) {
            ScreenName item = screenRepository.findByQuestionIds(questionId);
            sc = new ScreenResponse(true, item.getScreenHeading(), "Question is already Exists");
            return ResponseEntity.ok().body(sc);
        } else {
            sc = new ScreenResponse(false, null, "No Record found");
            return ResponseEntity.badRequest().body(sc);
        }
    }
}
