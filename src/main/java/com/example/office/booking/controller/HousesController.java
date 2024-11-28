package com.example.office.booking.controller;

import com.example.office.booking.entity.RealEstateObject;
import com.example.office.booking.entity.User;
import com.example.office.booking.repository.RealEstateObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.office.booking.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class HousesController {
    @Autowired
    private RealEstateObjectRepository realEstateObjectRepository;

    @Autowired
    private UserRepository userRepository;


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


    @GetMapping("/admin")
    public String admin(Model model, Authentication authentication) {
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
        return "admin";
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
