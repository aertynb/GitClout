package fr.uge.gitclout.service;

import fr.uge.gitclout.repository.CommiterRepository;
import fr.uge.gitclout.entity.Commiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommiterService {
    private final CommiterRepository commiterRepository;

    @Autowired
    public CommiterService(CommiterRepository commiterRepository) {
        this.commiterRepository = commiterRepository;
    }

    public Commiter addCommiter(Commiter commiter) {
        return commiterRepository.save(commiter);
    }
}
