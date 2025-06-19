package com.example.cartridgeaccounting.service.impl;

import com.example.cartridgeaccounting.dto.CreateLocationRequest;
import com.example.cartridgeaccounting.dto.LocationDto;
import com.example.cartridgeaccounting.entity.Location;
import com.example.cartridgeaccounting.exception.LocationNotFoundException;
import com.example.cartridgeaccounting.exception.LocationHasCartridgesException;
import com.example.cartridgeaccounting.repository.LocationRepository;
import com.example.cartridgeaccounting.repository.CartridgeRepository;
import com.example.cartridgeaccounting.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с объектами/местами хранения.
 * Предоставляет методы для создания, обновления, удаления и поиска объектов.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LocationServiceImpl implements LocationService {
    
    private final LocationRepository locationRepository;
    private final CartridgeRepository cartridgeRepository;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public LocationDto createLocation(CreateLocationRequest request) {
        log.info("Создание объекта с названием: {}", request.getName());
        
        Location location = new Location();
        location.setName(request.getName());
        location.setAddress(request.getAddress());
        location.setCabinet(request.getCabinet());
        location.setContactPerson(request.getContactPerson());
        location.setContactPhone(request.getContactPhone());
        location.setDescription(request.getDescription());
        location.setActive(request.isActive());
        
        Location savedLocation = locationRepository.save(location);
        log.info("Объект создан с ID: {}", savedLocation.getId());
        
        return convertToDto(savedLocation);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public LocationDto getLocationById(UUID id) {
        log.info("Получение объекта по ID: {}", id);
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(id));
        return convertToDto(location);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public LocationDto getLocationByName(String name) {
        log.info("Получение объекта по названию: {}", name);
        Location location = locationRepository.findByName(name)
                .orElseThrow(() -> new LocationNotFoundException(name));
        return convertToDto(location);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LocationDto> getAllLocations(Pageable pageable) {
        log.info("Получение всех объектов с пагинацией");
        return locationRepository.findAll(pageable).map(this::convertToDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocationDto> getActiveLocations() {
        log.info("Получение активных объектов");
        return locationRepository.findByActive(true).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocationDto> searchLocationsByAddress(String address) {
        log.info("Поиск объектов по адресу: {}", address);
        return locationRepository.findByAddressContainingIgnoreCase(address).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocationDto> searchLocationsByContactPerson(String contactPerson) {
        log.info("Поиск объектов по контактному лицу: {}", contactPerson);
        return locationRepository.findByContactPersonContainingIgnoreCase(contactPerson).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public LocationDto updateLocation(UUID id, CreateLocationRequest request) {
        log.info("Обновление объекта с ID: {}", id);
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(id));
        
        location.setName(request.getName());
        location.setAddress(request.getAddress());
        location.setCabinet(request.getCabinet());
        location.setContactPerson(request.getContactPerson());
        location.setContactPhone(request.getContactPhone());
        location.setDescription(request.getDescription());
        location.setActive(request.isActive());
        
        Location updatedLocation = locationRepository.save(location);
        log.info("Объект обновлен с ID: {}", updatedLocation.getId());
        
        return convertToDto(updatedLocation);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteLocation(UUID id) {
        log.info("Удаление объекта с ID: {}", id);
        
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(id));
        
        // Проверяем, есть ли картриджи, связанные с этим объектом
        long cartridgeCount = cartridgeRepository.countByCurrentLocation(location);
        if (cartridgeCount > 0) {
            log.warn("Попытка удаления объекта с ID: {}, который имеет {} связанных картриджей", id, cartridgeCount);
            throw new LocationHasCartridgesException(id, location.getName(), cartridgeCount);
        }
        
        locationRepository.deleteById(id);
        log.info("Объект удален с ID: {}", id);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void changeLocationStatus(UUID id, boolean active) {
        log.info("Изменение статуса объекта с ID: {} на активный: {}", id, active);
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(id));
        
        location.setActive(active);
        locationRepository.save(location);
        log.info("Статус объекта изменен для ID: {}", id);
    }
    
    /**
     * Преобразует сущность объекта в DTO
     * 
     * @param location сущность объекта
     * @return DTO объекта
     */
    private LocationDto convertToDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setAddress(location.getAddress());
        dto.setCabinet(location.getCabinet());
        dto.setContactPerson(location.getContactPerson());
        dto.setContactPhone(location.getContactPhone());
        dto.setDescription(location.getDescription());
        dto.setActive(location.isActive());
        
        // Подсчитываем количество картриджей на объекте
        long cartridgeCount = cartridgeRepository.countByCurrentLocation(location);
        dto.setCartridgeCount(cartridgeCount);
        
        return dto;
    }
} 