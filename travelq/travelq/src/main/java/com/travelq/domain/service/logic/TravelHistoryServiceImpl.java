package com.travelq.domain.service.logic;

import com.travelq.dto.TravelHistoryDto;
import com.travelq.exception.TravelHistoryNotFoundException;
import com.travelq.domain.model.TravelHistoryEntity;
import com.travelq.domain.repository.TravelHistoryRepository;
import com.travelq.domain.service.TravelHistoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelHistoryServiceImpl implements TravelHistoryService {

    private final TravelHistoryRepository travelhistoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public TravelHistoryDto addTravelHistory(TravelHistoryDto travelhistoryDto) {
        TravelHistoryEntity travelHistoryEntity = modelMapper.map(travelhistoryDto, TravelHistoryEntity.class);
        TravelHistoryEntity saved = travelhistoryRepository.save(travelHistoryEntity);
        return modelMapper.map(saved, TravelHistoryDto.class);
    }

    @Override
    public List<TravelHistoryDto> getAllTravelHistories() {
        List<TravelHistoryEntity> travelHistoryList = travelhistoryRepository.findAll();
        return travelHistoryList.stream()
                .map(travelHistoryEntity -> modelMapper.map(travelHistoryEntity, TravelHistoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TravelHistoryDto getTravelHistoryById(Long travelHistoryId) {
        TravelHistoryEntity travelHistoryEntity = travelhistoryRepository.findById(travelHistoryId)
                .orElseThrow(() -> new TravelHistoryNotFoundException(travelHistoryId));
        return modelMapper.map(travelHistoryEntity, TravelHistoryDto.class);
    }

    @Override
    public TravelHistoryDto updateTravelHistory(Long travelHistoryId, TravelHistoryDto travelhistoryDto) {
        TravelHistoryEntity existingEntity = travelhistoryRepository.findById(travelHistoryId)
                .orElseThrow(() -> new TravelHistoryNotFoundException(travelHistoryId));
        modelMapper.map(travelhistoryDto, existingEntity);
        TravelHistoryEntity updatedEntity = travelhistoryRepository.save(existingEntity);
        return modelMapper.map(updatedEntity, TravelHistoryDto.class);
    }

    @Override
    public void deleteTravelHistory(Long travelHistoryId) {
        if (!travelhistoryRepository.existsById(travelHistoryId)) {
            throw new TravelHistoryNotFoundException(travelHistoryId);
        }
        travelhistoryRepository.deleteById(travelHistoryId);
    }
}
