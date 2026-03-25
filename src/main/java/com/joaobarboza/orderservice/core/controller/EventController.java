package com.joaobarboza.orderservice.core.controller;

import com.joaobarboza.orderservice.core.repository.mongo.document.Event;
import com.joaobarboza.orderservice.core.dto.EventFilters;
import com.joaobarboza.orderservice.core.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @GetMapping()
    public Event findByFilters(@RequestBody EventFilters filters) {
        return eventService.findByFilters(filters);
    }

    @GetMapping("all")
    public List<Event> findAll() {
        return eventService.findAll();
    }
}
