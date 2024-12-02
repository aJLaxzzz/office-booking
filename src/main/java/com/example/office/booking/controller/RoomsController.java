package com.example.office.booking.controller;

import com.example.office.booking.entity.Booking;
import com.example.office.booking.entity.MeetingRoom;
import com.example.office.booking.entity.User;
import com.example.office.booking.repository.BookingRepository;
import com.example.office.booking.repository.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.office.booking.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RoomsController {
    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;


    @GetMapping("/rooms")
    public String homePage(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findUserByName(username);
        User user = userOptional.orElse(null);
        Long userId = (user != null) ? user.getId() : null;
        model.addAttribute("userId", userId);
        model.addAttribute("meetingRooms", meetingRoomRepository.findAll());
        return "rooms";
    }


    @GetMapping("/admin")
    public String admin(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findUserByName(username);
        User user = userOptional.orElse(null);
        Long userId = (user != null) ? user.getId() : null;
        model.addAttribute("userId", userId);
        List<User> allUsers = userRepository.findAll();
        model.addAttribute("allUsers", allUsers);
        for (User userr : allUsers) {
            System.out.println(userr);
        }
        model.addAttribute("meetingRooms", meetingRoomRepository.findAll());
        return "admin";
    }

    @GetMapping("/account")
    public String account(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findUserByName(username);
        User user = userOptional.orElse(null);
        Long userId = (user != null) ? user.getId() : null;

        model.addAttribute("userId", userId);
        model.addAttribute("username", username);

        if (userId != null) {
            List<Booking> userBookings = bookingRepository.findByUserId(userId);
            userBookings.sort(Comparator.comparing(Booking::getStartDate));
            List<Map<String, Object>> bookingCards = userBookings.stream()
                    .map(booking -> {
                        MeetingRoom meetingRoom = meetingRoomRepository.findById(booking.getObjectId()).orElse(null);
                        Map<String, Object> card = new HashMap<>();
                        card.put("booking", booking);
                        card.put("meetingRoom", meetingRoom);
                        return card;
                    })
                    .filter(card -> card.get("meetingRoom") != null)
                    .collect(Collectors.toList());

            model.addAttribute("bookingCards", bookingCards);
        }

        return "account";
    }



    @GetMapping("/search")
    public String search(@RequestParam(required = false) String name,
                         @RequestParam(required = false) String description,
                         @RequestParam(required = false) Integer areaMin,
                         @RequestParam(required = false) Integer areaMax,
                         @RequestParam(required = false) Integer capacityMin,
                         @RequestParam(required = false) Integer capacityMax,
                         @RequestParam(required = false) Integer internetSpeedMin,
                         @RequestParam(required = false) Integer internetSpeedMax,
                         @RequestParam(required = false) Integer floorMin,
                         @RequestParam(required = false) Integer floorMax,
                         @RequestParam(defaultValue = "name") String sortBy,
                         @RequestParam(defaultValue = "asc") String sortDirection,
                         @RequestParam Long userId,
                         Model model) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        List<MeetingRoom> searchResults = meetingRoomRepository.findByCriteria(
                name, description, areaMin, areaMax, capacityMin, capacityMax,
                internetSpeedMin, internetSpeedMax, floorMin, floorMax, sort);

        model.addAttribute("meetingRooms", searchResults);
        model.addAttribute("userId", userId);
        return "rooms";
    }

}
