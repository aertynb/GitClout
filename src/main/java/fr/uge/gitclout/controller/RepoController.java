package fr.uge.gitclout.controller;

import fr.uge.gitclout.entity.Repo;
import fr.uge.gitclout.service.RepoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/repository")
public class RepoController {
    private final RepoService repoService;

    public RepoController(RepoService repoService) {
        this.repoService = repoService;
    }

    @Operation(summary = "Get all repositories", description = "Retrieve a list of all repositories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of repositories retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public List<Repo> getRepo() {
        return repoService.findAll();
    }
}

