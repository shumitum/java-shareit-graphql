package ru.practicum.mainsrv.item;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mainsrv.exception.ItemNotFoundException;
import ru.practicum.mainsrv.item.comment.Comment;
import ru.practicum.mainsrv.item.comment.CommentRepository;
import ru.practicum.mainsrv.item.comment.dto.CommentDto;
import ru.practicum.mainsrv.item.dto.ItemDto;
import ru.practicum.mainsrv.user.User;
import ru.practicum.mainsrv.user.UserService;
import ru.practicum.mainsrv.utils.PageParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final UserService userService;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Item createItem(ItemDto itemDto, Long userId) {
        final Item item = itemMapper.toItem(itemDto);
        item.setOwner(userService.findUserById(userId));
        //item.setRequest(itemRequestRepository.findById(itemDto.getRequestId()).orElse(null));
        return itemRepository.save(item);
    }

    @Transactional
    @Override
    public Item updateItem(Long itemId, ItemDto itemDto, Long ownerId) {
        final Item updatingItem = findItemById(itemId);
        if (ownerId.equals(updatingItem.getOwner().getId())) {
            Optional.ofNullable(itemDto.getName()).ifPresent(updatingItem::setName);
            Optional.ofNullable(itemDto.getDescription()).ifPresent(updatingItem::setDescription);
            Optional.ofNullable(itemDto.getAvailable()).ifPresent(updatingItem::setAvailable);
/*            if (itemDto.getName() != null) {
                updatingItem.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                updatingItem.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                updatingItem.setAvailable(itemDto.getAvailable());
            }*/
        } else {
            throw new IllegalArgumentException("Редактировать информацию о вещи может только её владелец");
        }
        return updatingItem;
    }

    @Transactional(readOnly = true)
    @Override
    public Item getItemById(Long itemId, Long userId) {
        userService.checkUserExistence(userId);
        final Item item = findItemById(itemId);
        //final ItemDto itemDto = itemMapper.toItemDto(item);
/*        if (userId == item.getOwner().getId()) {
            itemDto.setLastBooking(getLastBookingInfoDto(itemId));
            itemDto.setNextBooking(getNextBookingInfoDto(itemId));
        }
        List<CommentDto> comments = commentRepository.findByItemId(itemId)
                .stream()
                //.map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
        itemDto.setComments(comments);*/
        return item;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Item> getUserItems(Long userId, Integer from, Integer size) {
        //final Long userId = params.getUserId();
        userService.checkUserExistence(userId);
/*        Map<Long, List<CommentDto>> commentsByItemId = commentRepository.findByItemOwnerId(userId)
                .stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId(),
                        mapping(commentMapper::toCommentDto, Collectors.toList())));*/
        return itemRepository.findAllByOwnerIdOrderByIdAsc(userId, PageParam.pageFrom(from, size))
                .stream()
                //.peek(item -> item.setLastBooking(getLastBookingInfoDto(item.getId())))
                //.peek(item -> item.setNextBooking(getNextBookingInfoDto(item.getId())))
                //.peek(item -> item.setComments(commentsByItemId.get(item.getId())))
                .toList();
                //.collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Item> searchItem(String searchRequest, Long userId, Integer from, Integer size) {
        if (!searchRequest.isBlank()) {
            userService.checkUserExistence(userId);
            return new ArrayList<>(itemRepository.findItem(searchRequest.trim(), PageParam.pageFrom(from, size)));
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    @Override
    public void deleteItemById(Long itemId, Long userId) {
        userService.checkUserExistence(userId);
        final Item item = findItemById(itemId);
        if (item.getOwner().getId().equals(userId)) {
            if (item.getAvailable().equals(Boolean.TRUE)) {
                itemRepository.deleteById(itemId);
            }
        } else {
            throw new IllegalArgumentException("Удалить вещь может только её владелец");
        }
    }

    @Transactional
    @Override
    public Comment createComment(CommentDto commentDto, Long itemId, Long commentatorId) {
        final User commentator = userService.findUserById(commentatorId);
        final Item item = findItemById(itemId);
/*        bookingRepository.findByBookerIdAndItemIdAndEndBefore(commentatorId, itemId, LocalDateTime.now())
                .stream()
                .findAny()
                .orElseThrow(() -> new InvalidArgumentException(String.format("Пользователь с ID=%d не может "
                        + "комментировать вещь с ID=%d, которую ранее не арендовал", commentatorId, itemId)));*/
        final Comment comment = Comment.builder()
                .text(commentDto.getText())
                .item(item)
                .author(commentator)
                .created(LocalDateTime.now())
                .build();
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Item с ID=%d не существует", itemId)));
    }

/*
    private final ItemRequestRepository itemRequestRepository;

    private final BookingRepository bookingRepository;


    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    public BookingInfoDto getLastBookingInfoDto(long itemId) {
        findItemById(itemId);
        List<Booking> bookings = bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByStartDesc(itemId,
                LocalDateTime.now(), BookingStatus.APPROVED);
        return !bookings.isEmpty() ? bookingMapper.toBookingInfoDto(bookings.get(0)) : null;
    }

    @Transactional(readOnly = true)
    public BookingInfoDto getNextBookingInfoDto(long itemId) {
        findItemById(itemId);
        List<Booking> bookings = bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(itemId,
                LocalDateTime.now(), BookingStatus.APPROVED);
        return !bookings.isEmpty() ? bookingMapper.toBookingInfoDto(bookings.get(0)) : null;
    }*/
}