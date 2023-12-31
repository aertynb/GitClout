package fr.uge.gitclout.controller;

import fr.uge.gitclout.entity.Commit;
import fr.uge.gitclout.service.CommitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/commits")
public class CommitController {
    private final CommitService commitService;

    @Autowired
    public CommitController(CommitService commitService) {
        this.commitService = commitService;
    }

    @Operation(summary = "Get all commits", description = "Retrieve a list of all commits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of commits retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public List<Commit> getCommits() {
        return commitService.findAll();
    }
}

