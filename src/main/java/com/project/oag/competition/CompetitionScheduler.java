package com.project.oag.competition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
@Component
public class CompetitionScheduler {
    @Autowired
    private CompetitionService competitionService;
    @Scheduled(fixedRate = 60000)
    public void checkCompetitionEndTime() {
        List<Competition> expiredCompetitions = competitionService.getExpiredCompetitions();
        for (Competition competition : expiredCompetitions) {
            if (!competition.isWinnerAnnounced()) {
                Competitor winner = competitionService.determineWinner(competition.getId());
                competition.setWinner(winner);
                competition.setWinnerAnnounced(true);
                competition.setVotingClosed(true);
                competitionService.addCompetition(competition);
            }
        }
    }
}
