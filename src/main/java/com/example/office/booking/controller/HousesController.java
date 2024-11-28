package com.example.office.booking.controller;

import com.example.office.booking.entity.Booking;
import com.example.office.booking.entity.RealEstateObject;
import com.example.office.booking.entity.User;
import com.example.office.booking.repository.BookingRepository;
import com.example.office.booking.repository.RealEstateObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.office.booking.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HousesController {
    @Autowired
    private RealEstateObjectRepository realEstateObjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;


    @GetMapping("/houses")
    public String homePage(Model model, Authentication authentication) {
        // Получаем имя текущего пользователя
        String username = authentication.getName();

        // Получаем пользователя из базы данных по имени
        Optional<User> userOptional = userRepository.findUserByName(username);

        // Если пользователь найден, получаем его, иначе возвращаем null
        User user = userOptional.orElse(null);

        // Если пользователь найден, получаем его ID
        Long userId = (user != null) ? user.getId() : null;

        // Добавляем информацию о текущем пользователе в модель
        model.addAttribute("userId", userId);

        // Добавляем объекты недвижимости
        model.addAttribute("realEstateObjects", realEstateObjectRepository.findAll());
        return "houses";
    }


    @GetMapping("/api/booking/object/{objectId}")
    public List<Map<String, Object>> getBookingsByObjectId(@PathVariable Long objectId) {
        // Получите список бронирований для данного объекта
        List<Booking> bookings = bookingRepository.findByObjectId(objectId);

        List<Map<String, Object>> bookingDetails = new ArrayList<>();

        for (Booking booking : bookings) {
            Map<String, Object> bookingInfo = new HashMap<>();
            bookingInfo.put("id", booking.getId());
            bookingInfo.put("startDate", booking.getStartDate());
            bookingInfo.put("endDate", booking.getEndDate());

            // Получаем пользователя по userId
            Optional<User> userOptional = userRepository.findById(booking.getUserId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                bookingInfo.put("userName", user.getName());
                bookingInfo.put("userEmail", user.getEmail());
            } else {
                bookingInfo.put("userName", "Неизвестный пользователь");
                bookingInfo.put("userEmail", "Неизвестный email");
            }

            bookingDetails.add(bookingInfo);
        }

        return bookingDetails;
    }



    @GetMapping("/account")
    public String account(Model model, Authentication authentication) {
        // Получаем имя текущего пользователя
        String username = authentication.getName();

        // Получаем пользователя из базы данных по имени
        Optional<User> userOptional = userRepository.findUserByName(username);

        // Если пользователь найден, получаем его, иначе возвращаем null
        User user = userOptional.orElse(null);

        // Если пользователь найден, получаем его ID
        Long userId = (user != null) ? user.getId() : null;

        // Добавляем информацию о текущем пользователе в модель
        model.addAttribute("userId", userId);

        // Получаем все бронирования текущего пользователя, если он найден
        if (userId != null) {
            List<Booking> userBookings = bookingRepository.findByUserId(userId);
            model.addAttribute("userBookings", userBookings);

            // Получаем список объектов недвижимости, которые забронированы этим пользователем
            List<Long> objectIds = userBookings.stream()
                    .map(Booking::getObjectId)
                    .collect(Collectors.toList());
            if (!objectIds.isEmpty()) {
                List<RealEstateObject> bookedObjects = realEstateObjectRepository.findByIdIn(objectIds);
                model.addAttribute("bookedRealEstateObjects", bookedObjects);
            } else {
                model.addAttribute("bookedRealEstateObjects", List.of()); // Если нет забронированных объектов
            }
        }

        return "account";
    }



    @GetMapping("/search")
    public String search(@RequestParam(required = false) String address,
                         @RequestParam(required = false) String description,
                         @RequestParam(required = false) Integer areaMin,
                         @RequestParam(required = false) Integer areaMax,
                         @RequestParam(required = false) Integer priceMin,
                         @RequestParam(required = false) Integer priceMax,
                         @RequestParam(required = false) Integer buildYearMin,
                         @RequestParam(required = false) Integer buildYearMax,
                         @RequestParam(required = false) Integer floorMin,
                         @RequestParam(required = false) Integer floorMax,
                         @RequestParam(defaultValue = "address") String sortBy,
                         @RequestParam(defaultValue = "asc") String sortDirection,
                         @RequestParam Long userId,
                         Model model) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        List<RealEstateObject> searchResults = realEstateObjectRepository.findByCriteria(
                address, description, areaMin, areaMax, priceMin, priceMax,
                buildYearMin, buildYearMax, floorMin, floorMax, sort);

        model.addAttribute("realEstateObjects", searchResults);
        model.addAttribute("userId", userId);
        return "houses";
    }

}
