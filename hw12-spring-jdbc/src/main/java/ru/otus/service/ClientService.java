package ru.otus.service;

import ru.otus.model.dto.request.ClientCreateDto;
import ru.otus.model.dto.response.ClientResponseDto;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    ClientResponseDto saveClient(ClientCreateDto clientCreateDto);

    Optional<ClientResponseDto> findById(long id);

    List<ClientResponseDto> findAll();
}