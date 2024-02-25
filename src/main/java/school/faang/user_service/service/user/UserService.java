package school.faang.user_service.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import school.faang.user_service.dto.user.UserDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.mapper.UserMapper;
import school.faang.user_service.repository.UserRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CsvOfPeopleToUserParser csvParser;

    @Transactional(readOnly = true)
    public User getExistingUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id = " + id + " is not found in database"));
    }

    @Transactional
    public List<UserDto> generateUsersFromCsv(MultipartFile csvFile) throws IOException {
        List<User> savedUsers = csvParser.parse(csvFile);
        userRepository.saveAll(savedUsers);
        log.info("People saved from csv file as users. Saved accounts count: {}", savedUsers.size());
        return userMapper.listToDto(savedUsers);
    }
}