package ru.otus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.model.dto.request.ClientCreateDto;
import ru.otus.model.dto.response.AddressResponseDto;
import ru.otus.model.dto.response.ClientResponseDto;
import ru.otus.model.dto.response.PhoneResponseDto;
import ru.otus.model.entity.Address;
import ru.otus.model.entity.Client;
import ru.otus.model.entity.Phone;
import ru.otus.repository.ClientRepository;

import java.util.*;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public ClientResponseDto saveClient(ClientCreateDto clientCreateDto) {
        var name = clientCreateDto.getName();
        var street = clientCreateDto.getAddress();
        Address address = null;
        if (nonNull(street) && !street.isEmpty()) {
            address = new Address(street);
        }

        var phoneNumbers = clientCreateDto.getPhones();
        Set<Phone> phones = new HashSet<>();
        if (nonNull(phoneNumbers) && !phoneNumbers.isEmpty()) {
            phoneNumbers.stream()
                    .filter(Objects::nonNull)
                    .filter(f -> !f.isEmpty())
                    .map(Phone::new)
                    .forEach(phones::add);
        }
        var client = new Client(name, address, phones);
        var saveClient = clientRepository.save(client);

        log.info("Saved client: {}", clientCreateDto);
        return getClientResponseDto(saveClient);
    }

    @Override
    public Optional<ClientResponseDto> findById(long id) {
        var clientOptional = clientRepository.findById(id);
        log.info("client: {}", clientOptional);
        return clientOptional.map(ClientServiceImpl::getClientResponseDto);
    }

    @Override
    public List<ClientResponseDto> findAll() {
        var clientList = new ArrayList<ClientResponseDto>();
        for (Client client : clientRepository.findAll()) {
            clientList.add(getClientResponseDto(client));
        }
        log.info("clientList:{}", clientList);
        return clientList;
    }

    private static ClientResponseDto getClientResponseDto(Client client) {
        return ClientResponseDto.builder()
                .id(client.getId())
                .name(client.getName())
                .address(Optional.ofNullable(client.getAddress())
                        .map(address -> AddressResponseDto.builder()
                                .id(address.getId())
                                .street(address.getStreet())
                                .build())
                        .orElse(null)
                )
                .phones(client.getPhones().stream()
                        .map(phone -> PhoneResponseDto.builder()
                                .id(phone.getId())
                                .number(phone.getNumber())
                                .build())
                        .toList())
                .build();
    }
}