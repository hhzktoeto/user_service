package school.faang.user_service.service.user;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.json.student.PersonSchemaForUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import school.faang.user_service.dto.CountryDto;
import school.faang.user_service.dto.UserDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.UserProfilePic;
import school.faang.user_service.mapper.UserMapperImpl;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.service.amazon.AvatarService;
import school.faang.user_service.validator.user.UserValidator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Spy
    private UserMapperImpl userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private AvatarService avatarService;
    @Mock
    private CsvMapper csvMapper;
    @Mock
    private CsvSchema csvSchema;
    @Mock
    private CountryService countryService;
    @InjectMocks
    private UserService userService;
    @Value("${services.dice-bear.url}")
    private String URL;
    @Value("${services.dice-bear.size}")
    private String SIZE;

    @Test
    public void testCreateUser() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .country(CountryDto.builder().title("test").build())
                .username("test")
                .build();

        User user = User.builder()
                .id(1L)
                .username("test")
                .build();

        Mockito.when(userMapper.toEntity(userDto))
                .thenReturn(user);
        Mockito.when(userRepository.save(user))
                .thenReturn(user);
        Mockito.when(countryService.getIdByTitle(userDto.getCountry().getTitle()))
                .thenReturn(Optional.of(1L));

        userService.createUser(userDto);

        Mockito.verify(userMapper, Mockito.times(2))
                .toEntity(userDto);
        Mockito.verify(userRepository, Mockito.times(1))
                .save(user);
        Mockito.verify(userMapper, Mockito.times(1))
                .toDto(user);
    }

    @Test
    public void testGetCountryId_Exist() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .country(CountryDto.builder().title("test").build())
                .username("test")
                .build();

        User user = User.builder()
                .id(1L)
                .username("test")
                .build();

        Mockito.when(userMapper.toEntity(userDto))
                .thenReturn(user);
        Mockito.when(userRepository.save(user))
                .thenReturn(user);
        Mockito.when(countryService.getIdByTitle(userDto.getCountry().getTitle()))
                .thenReturn(Optional.of(1L));

        userService.createUser(userDto);

        Mockito.verify(countryService, Mockito.times(1))
                .getIdByTitle(userDto.getCountry().getTitle());
        Mockito.verify(countryService, Mockito.times(0))
                .create(userDto.getCountry());
    }

    @Test
    public void testGetCountryId_DoesNotExist() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .country(CountryDto.builder().title("test").build())
                .username("test")
                .build();

        User user = User.builder()
                .id(1L)
                .username("test")
                .build();

        Mockito.when(userMapper.toEntity(userDto))
                .thenReturn(user);
        Mockito.when(userRepository.save(user))
                .thenReturn(user);
        Mockito.when(countryService.getIdByTitle(userDto.getCountry().getTitle()))
                .thenReturn(Optional.empty());
        Mockito.when(countryService.create(userDto.getCountry()))
                .thenReturn(userDto.getCountry());

        userService.createUser(userDto);

        Mockito.verify(countryService, Mockito.times(1))
                .getIdByTitle(userDto.getCountry().getTitle());
        Mockito.verify(countryService, Mockito.times(1))
                .create(userDto.getCountry());
    }

    @Test
    public void testCreateUserCSV_ThrowsException() {
        ObjectReader testObject = Mockito.mock(ObjectReader.class);
        InputStream inputStream = new ByteArrayInputStream("file".getBytes());

        Mockito.when(csvMapper.readerFor(any(Class.class)))
                .thenReturn(testObject);
        Mockito.when(testObject.with(csvSchema))
                .thenReturn(testObject);

        assertThrows(Exception.class, () -> userService.createUserCSV(inputStream));
    }

    @Test
    public void testMakeFutureList() throws IOException {
        ObjectReader testObject = Mockito.mock(ObjectReader.class);
        InputStream inputStream = new ByteArrayInputStream("file".getBytes());
        MappingIterator<Object> iterator = Mockito.mock(MappingIterator.class);
        UserDto userDto = UserDto.builder()
                .country(CountryDto.builder().title("test").build())
                .build();
        User user = User.builder().build();

        PersonSchemaForUser person = new PersonSchemaForUser();
        List<Object> persons = List.of(person);

        Mockito.when(csvMapper.readerFor(any(Class.class)))
                .thenReturn(testObject);
        Mockito.when(testObject.with(csvSchema))
                .thenReturn(testObject);
        Mockito.when(testObject.readValues(any(InputStream.class)))
                .thenReturn(iterator);
        Mockito.when(iterator.readAll())
                .thenReturn(persons);

        Mockito.when(userMapper.personToUserDto(person))
                .thenReturn(userDto);
        Mockito.when(userMapper.toEntity(userDto))
                .thenReturn(user);
        Mockito.when(userRepository.save(user))
                .thenReturn(user);
        Mockito.when(userMapper.toDto(any(User.class)))
                .thenReturn(userDto);

        userService.createUserCSV(inputStream);

        assertTrue(userDto.getPassword() != null);
        Mockito.verify(userMapper, Mockito.times(2))
                .personToUserDto(person);
        Mockito.verify(userValidator, Mockito.times(1))
                .validateUserDto(userDto);
    }

    @Test
    public void testParseCsv() throws IOException {
        ObjectReader testObject = Mockito.mock(ObjectReader.class);
        InputStream inputStream = new ByteArrayInputStream("file".getBytes());
        MappingIterator<Object> iterator = Mockito.mock(MappingIterator.class);

        Mockito.when(csvMapper.readerFor(any(Class.class)))
                .thenReturn(testObject);
        Mockito.when(testObject.with(csvSchema))
                .thenReturn(testObject);
        Mockito.when(testObject.readValues(any(InputStream.class)))
                .thenReturn(iterator);
        Mockito.when(iterator.readAll())
                .thenReturn(new ArrayList<>());

        userService.createUserCSV(inputStream);

        Mockito.verify(csvMapper, Mockito.times(1))
                .readerFor(PersonSchemaForUser.class);
        Mockito.verify(testObject, Mockito.times(1))
                .with(csvSchema);
        Mockito.verify(testObject, Mockito.times(1))
                .readValues(inputStream);
        Mockito.verify(iterator, Mockito.times(1))
                .readAll();
    }
    @Test
    public void testAddCreateData() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .country(CountryDto.builder().title("test").build())
                .username("test")
                .build();
        User user = new User();
        Mockito.when(userMapper.toEntity(userDto))
                .thenReturn(user);
        Mockito.when(userRepository.save(user))
                .thenReturn(user);
        Mockito.when(countryService.getIdByTitle(userDto.getCountry().getTitle()))
                        .thenReturn(Optional.of(1L));

        userService.createUser(userDto);

        assertTrue(user.getCreatedAt().isBefore(LocalDateTime.now()));
        assertTrue(user.getUserProfilePic().getName() != null);
    }

    @Test
    public void testCreateDiceBearAvatar() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .country(CountryDto.builder().title("test").build())
                .username("test")
                .build();

        User user = User.builder()
                .id(1L)
                .username("test")
                .build();

        Mockito.when(userMapper.toEntity(userDto))
                .thenReturn(user);
        Mockito.when(userRepository.save(user))
                .thenReturn(user);
        Mockito.when(countryService.getIdByTitle(userDto.getCountry().getTitle()))
                .thenReturn(Optional.of(1L));

        userService.createUser(userDto);

        Mockito.verify(avatarService, Mockito.times(1))
                .saveToAmazonS3(any(UserProfilePic.class));
    }

    @Test
    void areOwnedSkills() {
        assertTrue(userService.areOwnedSkills(1L, List.of()));
    }

    @Test
    void areOwnedSkillsFalse() {
        Mockito.when(userRepository.countOwnedSkills(1L, List.of(2L))).thenReturn(3);
        assertFalse(userService.areOwnedSkills(1L, List.of(2L)));
    }

    @Test
    void getUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        userService.getUser(1L);
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void getUsersByIds() {
        Mockito.when(userRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(User.builder().id(1L).build(), User.builder().id(2L).build()));
        List<UserDto> users = userService.getUsersByIds(List.of(1L, 2L));
        assertEquals(2, users.size());
    }
}