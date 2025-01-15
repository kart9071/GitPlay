package com.GitPlay.GitPlay.Controller;

import com.GitPlay.GitPlay.Object.CompileRequest;
import com.GitPlay.GitPlay.Object.CompilerResponse;
import com.GitPlay.GitPlay.Service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class CompilerController {
    private final CompilerService compilerService;

    public CompilerController(CompilerService compilerService) {
        this.compilerService = compilerService;
    }


    @PostMapping("/execute")
    public ResponseEntity<CompilerResponse> compileCode(@RequestBody CompileRequest request) {
        try {
            // Use the autowired service here
            String result = compilerService.executeCode(request.getLanguage(), request.getCode());
            return ResponseEntity.ok(new CompilerResponse(result));
        } catch (Exception e) {
            // Return error response in case of exceptions
            return ResponseEntity.status(500).body(new CompilerResponse("Error: " + e.getMessage()));
        }
    }
}
