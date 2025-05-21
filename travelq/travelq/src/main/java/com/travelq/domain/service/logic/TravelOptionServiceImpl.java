package com.travelq.domain.service.logic;

import com.travelq.dto.TravelOptionDto;
import com.travelq.exception.TravelOptionNotFoundException;
import com.travelq.domain.model.TravelOptionEntity;
import com.travelq.domain.repository.TravelOptionRepository;
import com.travelq.domain.service.TravelOptionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelOptionServiceImpl implements TravelOptionService {

    private final TravelOptionRepository traveloptionRepository;
    private final ModelMapper modelMapper;

    @Override
    public TravelOptionDto addTravelOption(TravelOptionDto traveloptionDto) {
        TravelOptionEntity travelOptionEntity = modelMapper.map(traveloptionDto, TravelOptionEntity.class);
        TravelOptionEntity saved = traveloptionRepository.save(travelOptionEntity);
        return modelMapper.map(saved, TravelOptionDto.class);
    }

    @Override
    public List<TravelOptionDto> getAllTravelOptions() {
        List<TravelOptionEntity> travelOptionList = traveloptionRepository.findAll();
        return travelOptionList.stream()
                .map(travelOptionEntity -> modelMapper.map(travelOptionEntity, TravelOptionDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TravelOptionDto getTravelOptionById(Long travelOptionId) {
        TravelOptionEntity travelOptionEntity = traveloptionRepository.findById(travelOptionId)
                .orElseThrow(() -> new TravelOptionNotFoundException(travelOptionId));
        return modelMapper.map(travelOptionEntity, TravelOptionDto.class);
    }

    @Override
    public TravelOptionDto updateTravelOption(Long travelOptionId, TravelOptionDto traveloptionDto) {
        TravelOptionEntity existingEntity = traveloptionRepository.findById(travelOptionId)
                .orElseThrow(() -> new TravelOptionNotFoundException(travelOptionId));
        modelMapper.map(traveloptionDto, existingEntity);
        TravelOptionEntity updatedEntity = traveloptionRepository.save(existingEntity);
        return modelMapper.map(updatedEntity, TravelOptionDto.class);
    }

    @Override
    public void deleteTravelOption(Long travelOptionId) {
        if (!traveloptionRepository.existsById(travelOptionId)) {
            throw new TravelOptionNotFoundException(travelOptionId);
        }
        traveloptionRepository.deleteById(travelOptionId);
    }
}
