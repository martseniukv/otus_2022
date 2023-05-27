package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.otus.model.dto.request.ClientCreateDto;
import ru.otus.model.dto.response.ClientResponseDto;
import ru.otus.service.ClientService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientRestController {

    private final ClientService clientService;

    @GetMapping("/{id}")
    public ClientResponseDto getById(@PathVariable("id") Long id) {
        return clientService.findById(id)
                .orElse(null);
    }

    @GetMapping
    public List<ClientResponseDto> getAll() {
        return clientService.findAll();
    }

    @PostMapping
    public ClientResponseDto save(@RequestBody ClientCreateDto client) {
        return clientService.saveClient(client);
    }
}