package com.enotes.Enotes_INDUS.scheduler;


import com.enotes.Enotes_INDUS.model.Notes;
import com.enotes.Enotes_INDUS.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotesScheduler {

    @Autowired
        private NotesRepository notesRepository;


    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void deleteNotesScheduler(){
        LocalDateTime cutOffDate =    LocalDateTime.now().minusDays(7);
      List<Notes> notesList= notesRepository.findAllByIsDeletedAndDeletedOnBefore(true,cutOffDate);
      notesRepository.deleteAll(notesList);

    }
}
