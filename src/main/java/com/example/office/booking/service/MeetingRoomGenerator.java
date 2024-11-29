package com.example.office.booking.service;

import com.example.office.booking.entity.MeetingRoom;
import com.example.office.booking.entity.MeetingRoom;
import com.example.office.booking.repository.MeetingRoomRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MeetingRoomGenerator {
    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    private static final String[] ROOM_DESCRIPTIONS = {
            "Современная переговорная комната с мультимедийным оборудованием.",
            "Уютная комната для встреч с комфортной мебелью.",
            "Переговорная с панорамными окнами и отличным видом.",
            "Комната для переговоров с возможностью видеоконференций.",
            "Просторная комната для больших групп.",
            "Инновационная переговорная с интерактивной доской.",
            "Комната для переговоров с уютной атмосферой.",
            "Переговорная с возможностью настройки освещения.",
            "Комната для встреч с доступом к кофе-уголку.",
            "Стильная переговорная с современным дизайном."
    };

    private final Random random = new Random();

    private String getRoomName(int index) {
        return "Переговорная комната " + (index + 1);
    }

    private int getRandomArea() {
        return 15 + random.nextInt(85); // Площадь в квадратных метрах
    }

    private int getRandomInternetSpeed() {
        return 100 + random.nextInt(200); // Скорость интернета в Мбит/с
    }

    private int getRandomCapacity() {
        return 6 + random.nextInt(15); // Вместимость
    }

    private int getRandomFloor() {
        return 1 + random.nextInt(25); // Этаж
    }

    private String getRandomPhotoURL() {
        return "/" + (1+random.nextInt(14)) + ".jpeg"; // URL фотографии
    }

    public List<MeetingRoom> generateMeetingRooms(int count) {
        return IntStream.range(0, count).mapToObj(
                i -> new MeetingRoom(
                        null,
                        getRoomName(i), // Используем индекс для формирования названия
                        getRandomArea(),
                        getRandomInternetSpeed(),
                        getRandomCapacity(),
                        getRandomFloor(),
                        getRandomPhotoURL(),
                        ROOM_DESCRIPTIONS[random.nextInt(ROOM_DESCRIPTIONS.length)]
                )
        ).collect(Collectors.toList());
    }

    @PostConstruct
    public void fillDatabase() {
        List<MeetingRoom> meetingRooms = generateMeetingRooms(15);
        meetingRoomRepository.saveAll(meetingRooms);
    }
}