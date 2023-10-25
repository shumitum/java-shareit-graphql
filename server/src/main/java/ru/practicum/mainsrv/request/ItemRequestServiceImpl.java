package ru.practicum.mainsrv.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainsrv.item.ItemMapper;
import ru.practicum.mainsrv.item.ItemRepository;
import ru.practicum.mainsrv.item.dto.OutputItemDto;
import ru.practicum.mainsrv.request.dto.InputItemRequestDto;
import ru.practicum.mainsrv.request.dto.ItemRequestDto;
import ru.practicum.mainsrv.user.UserService;
import ru.practicum.mainsrv.utils.PageParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final ItemRequestMapper itemRequestMapper;

    @Transactional
    @Override
    public ItemRequest createItemRequest(InputItemRequestDto itemRequestDto, Long requesterId) {
        final ItemRequest newItemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        newItemRequest.setCreated(LocalDateTime.now());
        newItemRequest.setRequester(userService.findUserById(requesterId));
        return itemRequestRepository.save(newItemRequest);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemRequestDto getItemRequestById(Long requestId, Long userId) {
        userService.checkUserExistence(userId);
        final ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Заявки с ID=%d не существует", requestId)));
        final ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);
        final List<OutputItemDto> items = itemRepository.findByRequestId(requestId)
                .stream()
                .map(itemMapper::toOutputItemDto)
                .toList();
        itemRequestDto.setItems(items);
        return itemRequestDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDto> getUserItemRequests(Long userId) {
        userService.checkUserExistence(userId);
        return itemRequestRepository.findByRequesterIdOrderByCreatedDesc(userId).stream()
                .map(itemRequestMapper::toItemRequestDto)
                .peek(itemRequest -> itemRequest.setItems(getItemDtoList(itemRequest.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDto> getOthersItemRequests(Long userId, Integer from, Integer size) {
        userService.checkUserExistence(userId);
        return itemRequestRepository.findItemRequestsByRequesterIdNotOrderByCreatedDesc(userId, PageParam.pageFrom(from, size))
                .stream()
                .map(itemRequestMapper::toItemRequestDto)
                .peek(itemRequest -> itemRequest.setItems(getItemDtoList(itemRequest.getId())))
                .toList();
    }

    private List<OutputItemDto> getItemDtoList(Long itemRequestId) {
        return itemRepository.findByRequestId(itemRequestId)
                .stream()
                .map(itemMapper::toOutputItemDto)
                .toList();
    }
}