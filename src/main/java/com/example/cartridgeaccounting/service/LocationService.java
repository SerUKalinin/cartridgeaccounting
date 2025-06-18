package com.example.cartridgeaccounting.service;

import com.example.cartridgeaccounting.dto.CreateLocationRequest;
import com.example.cartridgeaccounting.dto.LocationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с объектами/местами хранения.
 * Предоставляет методы для создания, обновления, удаления и поиска объектов.
 *
 * @author Система учёта картриджей
 * @version 1.0
 */
public interface LocationService {
    /**
     * Создает новый объект/место хранения
     * @param request данные для создания объекта
     * @return созданный объект
     */
    LocationDto createLocation(CreateLocationRequest request);
    
    /**
     * Получает объект по ID
     * @param id идентификатор объекта
     * @return объект
     */
    LocationDto getLocationById(UUID id);
    
    /**
     * Получает объект по названию
     * @param name название объекта
     * @return объект
     */
    LocationDto getLocationByName(String name);
    
    /**
     * Получает все объекты с пагинацией
     * @param pageable параметры пагинации
     * @return страница объектов
     */
    Page<LocationDto> getAllLocations(Pageable pageable);
    
    /**
     * Получает все активные объекты
     * @return список активных объектов
     */
    List<LocationDto> getActiveLocations();
    
    /**
     * Ищет объекты по адресу
     * @param address адрес для поиска
     * @return список найденных объектов
     */
    List<LocationDto> searchLocationsByAddress(String address);
    
    /**
     * Ищет объекты по контактному лицу
     * @param contactPerson контактное лицо для поиска
     * @return список найденных объектов
     */
    List<LocationDto> searchLocationsByContactPerson(String contactPerson);
    
    /**
     * Обновляет объект
     * @param id идентификатор объекта
     * @param request данные для обновления
     * @return обновленный объект
     */
    LocationDto updateLocation(UUID id, CreateLocationRequest request);
    
    /**
     * Удаляет объект
     * @param id идентификатор объекта
     */
    void deleteLocation(UUID id);
    
    /**
     * Изменяет статус объекта (активен/неактивен)
     * @param id идентификатор объекта
     * @param active новый статус
     */
    void changeLocationStatus(UUID id, boolean active);
} 