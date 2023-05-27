package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.model.dto.request.ClientCreateDto;
import ru.otus.model.dto.response.ClientResponseDto;
import ru.otus.service.ClientService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping({"/", "/client/list"})
    public String clientsListView(Model model) {
        List<ClientResponseDto> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clientsList";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        model.addAttribute("client", new ClientCreateDto());
        return "clientCreate";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute ClientCreateDto clientCreateDto) {
        clientService.saveClient(clientCreateDto);
        return new RedirectView("/", true);
    }
}