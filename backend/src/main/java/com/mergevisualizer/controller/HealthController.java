package com.mergevisualizer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://127.0.0.1:5173",
        "https://git-merge-conflict-visualizer-frontend.onrender.com"
})
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Backend is running";
    }
}
