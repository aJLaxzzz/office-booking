package com.example.office.booking.service;

import com.example.office.booking.entity.RealEstateObject;
import com.example.office.booking.repository.RealEstateObjectRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class RealEstateInitGenerator {
    @Autowired
    private RealEstateObjectRepository realEstateObjectRepository;

    private static final String[] MOSCOW_STREETS = {
            "ул. Тверская", "ул. Арбат", "пр-т Ленинский",
            "ул. Новый Арбат", "ул. Садовая", "ул. Бауманская",
            "ул. Покровка", "ул. Лубянка", "ул. Мясницкая",
            "ул. Варшавское шоссе", "ул. Щепкина", "ул. Остоженка",
            "ул. Покрышкина", "ул. Люсиновская", "пр-т Кутузовский",
            "ул. Большая Якиманка", "ул. Кузнецкий Мост", "пр-т Вернадского",
            "ул. Пречистенка", "бул. Чистопрудный", "ул. Земляной Вал",
            "ул. Большая Никитская", "ул. Рождественка", "пр-т Мира",
            "ул. Пятницкая", "ул. Сретенка", "ул. Большая Полянка",
            "ул. Зубовский бульвар", "ул. Таганская", "ул. Смоленская"
    };

    private static final String[] APARTMENT_DESCRIPTIONS = {
            "Уютная квартира в тихом районе с развитой инфраструктурой.",
            "Светлая и просторная квартира с панорамными окнами и красивым видом на город.",
            "Недавно отремонтированная квартира с современной отделкой и новой мебелью.",
            "Компактная студия, идеально подходящая для студентов или молодых специалистов.",
            "Просторная квартира с двумя спальнями и большой кухней-гостиной.",
            "Эксклюзивная квартира в престижном районе с закрытой территорией.",
            "Квартира с дизайнерским ремонтом в стиле лофт, открытая планировка.",
            "Квартира в новостройке с закрытым двором и подземным паркингом.",
            "Двухуровневая квартира с большой террасой и видом на парк.",
            "Квартира в историческом центре города, высокие потолки, классический стиль.",
            "Семейная квартира с детской площадкой во дворе и развитой инфраструктурой района.",
            "Малогабаритная квартира с удобным расположением рядом с метро.",
            "Квартира с авторским ремонтом, эксклюзивная мебель и бытовая техника.",
            "Двухкомнатная квартира с современной планировкой и качественным ремонтом.",
            "Трехкомнатная квартира для большой семьи, много света и пространства.",
            "Квартира в спокойном районе с развитой инфраструктурой и удобным транспортным доступом.",
            "Квартира на последнем этаже с шикарным видом на вечерний город.",
            "Экологически чистая квартира с использованием натуральных материалов в отделке.",
            "Небольшая уютная квартира рядом с парком, идеально для молодой пары.",
            "Квартира с лоджией и красивым видом, удобное расположение рядом с торговым центром.",
            "Элегантная квартира в старинном здании с роскошной отделкой.",
            "Минималистичная квартира с открытой планировкой и современными удобствами.",
            "Функциональная квартира с умным домом, управление через мобильное приложение.",
            "Квартира с собственным садом и верандой, уединенное и спокойное место.",
            "Мансардная квартира с уникальной архитектурой и мансардными окнами.",
            "Квартира с историей, расположенная в здании начала XX века.",
            "Стильная квартира в динамичном и молодежном районе города.",
            "Просторная квартира с видом на реку и доступом к набережной.",
            "Квартира в тихом закрытом жилом комплексе с современной инфраструктурой.",
            "Пентхаус с камином и выходом на крышу, эксклюзивное предложение.",
            "Семейная квартира с дополнительной гостевой комнатой и рабочим кабинетом."
    };


    private final Random random = new Random();

    private String getRandomAddress() {
        String street = MOSCOW_STREETS[random.nextInt(MOSCOW_STREETS.length)];
        int houseNumber = 1 + random.nextInt(100);
        return street + ", дом " + houseNumber;
    }

    private int getRandomArea() {
        return 20 + random.nextInt(230);
    }

    private int getRandomPrice() {
        return 5000000 + random.nextInt(20000000);
    }

    private String getRandomDescription() {
        return APARTMENT_DESCRIPTIONS[random.nextInt(APARTMENT_DESCRIPTIONS.length)];
    }

    private int getRandomBuildYear() {
        return 1900 + random.nextInt(123);
    }

    private int getRandomFloor() {
        return 1 + random.nextInt(25);
    }

    private String getRandomPhotoURL() {return "/" + random.nextInt(30) + ".jpeg";}

    public List<RealEstateObject> generateRealEstateObjects(int count) {
        return IntStream.range(0, count).mapToObj(
            i -> new RealEstateObject(
                null,
                getRandomAddress(),
                getRandomArea(),
                getRandomPrice(),
                getRandomDescription(),
                getRandomBuildYear(),
                getRandomFloor(),
                getRandomPhotoURL()
            )
        ).collect(Collectors.toList());
    }

    @PostConstruct
    public void fillDatabase() {
        List<RealEstateObject> realEstateObjects = generateRealEstateObjects(5);
        realEstateObjectRepository.saveAll(realEstateObjects);

    }
}
