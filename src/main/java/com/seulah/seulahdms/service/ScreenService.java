package com.seulah.seulahdms.service;

import com.seulah.seulahdms.entity.ScreenName;
import com.seulah.seulahdms.repository.ScreenRepository;
import com.seulah.seulahdms.request.ScreenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ScreenService {
    private final ScreenRepository screenRepository;

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
}
