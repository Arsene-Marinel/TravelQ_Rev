package com.travelq.domain.service;

import com.travelq.dto.TravelOptionDto;

import java.util.List;

public interface TravelOptionService {

    TravelOptionDto addTravelOption(TravelOptionDto traveloptionDto);

    List<TravelOptionDto> getAllTravelOptions();

    TravelOptionDto getTravelOptionById(Long travelOptionId);

    TravelOptionDto updateTravelOption(Long travelOptionId, TravelOptionDto traveloptionDto);

    void deleteTravelOption(Long travelOptionId);
}
