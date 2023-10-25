package ru.practicum.mainsrv.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainsrv.exception.UserNotFoundException;
import ru.practicum.mainsrv.user.dto.UserDto;
import ru.practicum.mainsrv.utils.PageParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public User createUser(UserDto user) {
        return userRepository.save(userMapper.toUser(user));
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long userId) {
        return findUserById(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers(Integer from, Integer size) {
        return userRepository.findAll(PageParam.pageFrom(from, size)).stream()
                .toList();
    }

    @Transactional
    @Override
    public User updateUser(Long userId, UserDto userDto) {
        final User updatingUser = findUserById(userId);
        if (userDto.getEmail() != null && (!updatingUser.getEmail().equals(userDto.getEmail()))) {
            updatingUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            updatingUser.setName(userDto.getName());
        }
        return updatingUser;
    }

    @Transactional
    @Override
    public Boolean deleteUserById(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException(String.format("Невозможно удалить, пользователя с ID=%d не существует", userId));
        }
        return true;
    }

    @Transactional(readOnly = true)
    public void checkUserExistence(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователя с ID=%d не существует", userId));
        }
    }

    @Transactional(readOnly = true)
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователя с ID=%d не существует", userId)));
    }
}