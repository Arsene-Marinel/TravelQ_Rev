package com.travelq.service;

import com.travelq.domain.service.TravelHistoryService;
import com.travelq.domain.service.UserService;
import com.travelq.dto.TravelHistoryDto;
import com.travelq.dto.UserDto;
import com.travelq.exception.TravelHistoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TravelHistoryServiceTestProfileTest {

    @Autowired
    private TravelHistoryService travelHistoryService;

    @Autowired
    private UserService userService;

    private UserDto user;
    private TravelHistoryDto baseHistory;

    @BeforeEach
    public void setUp() {
        user = new UserDto();
        user.setUsername("history_user");
        user.setPassword("password123");
        user.setEmail("history@example.com");
        user.setFirstName("History");
        user.setLastName("User");
        user = userService.addUser(user);

        baseHistory = new TravelHistoryDto();
        baseHistory.setFlightsCount(5);
        baseHistory.setTotalSpent(1234.56);
        baseHistory.setUser(user);
    }

    @Test
    public void testAddTravelHistory() {
        TravelHistoryDto created = travelHistoryService.addTravelHistory(baseHistory);

        assertNotNull(created.getId());
        assertEquals(baseHistory.getFlightsCount(), created.getFlightsCount());
        assertEquals(baseHistory.getTotalSpent(), created.getTotalSpent());
        assertEquals(user.getId(), created.getUser().getId());
    }

    @Test
    public void testGetAllTravelHistories() {
        travelHistoryService.addTravelHistory(baseHistory);

        List<TravelHistoryDto> histories = travelHistoryService.getAllTravelHistories();
        assertFalse(histories.isEmpty());
    }

    @Test
    public void testGetTravelHistoryById() {
        TravelHistoryDto created = travelHistoryService.addTravelHistory(baseHistory);

        TravelHistoryDto found = travelHistoryService.getTravelHistoryById(created.getId());

        assertEquals(created.getFlightsCount(), found.getFlightsCount());
        assertEquals(created.getTotalSpent(), found.getTotalSpent());
    }

    @Test
    public void testUpdateTravelHistory() {
        TravelHistoryDto created = travelHistoryService.addTravelHistory(baseHistory);

        TravelHistoryDto update = new TravelHistoryDto();
        update.setFlightsCount(10);
        update.setTotalSpent(2345.67);
        update.setUser(user);

        TravelHistoryDto updated = travelHistoryService.updateTravelHistory(created.getId(), update);

        assertEquals(10, updated.getFlightsCount());
        assertEquals(2345.67, updated.getTotalSpent());
    }

    @Test
    public void testDeleteTravelHistory() {
        TravelHistoryDto created = travelHistoryService.addTravelHistory(baseHistory);
        travelHistoryService.deleteTravelHistory(created.getId());

        assertThrows(TravelHistoryNotFoundException.class, () -> travelHistoryService.getTravelHistoryById(created.getId()));
    }
}
