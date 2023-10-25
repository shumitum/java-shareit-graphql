package ru.practicum.mainsrv.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequesterIdOrderByCreatedDesc(long userId);

    List<ItemRequest> findItemRequestsByRequesterIdNotOrderByCreatedDesc(long userId, Pageable page);
}