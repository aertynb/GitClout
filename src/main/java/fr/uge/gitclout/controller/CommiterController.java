package fr.uge.gitclout.controller;

import fr.uge.gitclout.entity.Commiter;
import fr.uge.gitclout.service.CommiterService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/commiters")
public class CommiterController {
    private final CommiterService commiterService;

    public CommiterController(CommiterService commiterService) {
        this.commiterService = commiterService;
    }

    @Operation(summary = "Get all committers", description = "Retrieve a list of all committers")
    @GetMapping
    public List<Commiter> getCommiters() {
        return commiterService.findAll();
    }
}

