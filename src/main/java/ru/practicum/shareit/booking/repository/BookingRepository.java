package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    //by booker
    @Query("select b from Booking b where b.booker.id = ?1 order by b.end desc")
    List<Booking> findAllByBookerId(Long userId);

    @Query("select b from Booking b where b.booker.id = ?1 " +
            " and (current_timestamp between b.start and b.end) order by b.end desc")
    List<Booking> findAllByBookerIdAndCurrentState(Long userId);

    @Query("select b from Booking b where b.booker.id = ?1 " +
            " and (b.end < current_timestamp) order by b.end desc")
    List<Booking> findAllByBookerIdAndPastState(Long userId);

    @Query("select b from Booking b where b.booker.id = ?1 " +
            " and (b.start > current_timestamp) order by b.end desc")
    List<Booking> findAllByBookerIdAndFutureState(Long userId);

    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2 order by b.end desc")
    List<Booking> findAllByBookerIdAndWaitingOrRejectedState(Long userId, Status status);

    //by owner
    @Query("select b from Booking b where b.item.owner = ?1 order by b.end desc")
    List<Booking> findAllByOwnerId(Long userId);

    @Query("select b from Booking b where b.item.owner = ?1 " +
            " and (current_timestamp between b.start and b.end) order by b.end desc")
    List<Booking> findAllByOwnerIdAndCurrentState(Long userId);

    @Query("select b from Booking b where b.item.owner = ?1 " +
            " and (b.end < current_timestamp) order by b.end desc")
    List<Booking> findAllByOwnerIdAndPastState(Long userId);

    @Query("select b from Booking b where b.item.owner = ?1 " +
            " and (b.start > current_timestamp) order by b.end desc")
    List<Booking> findAllByOwnerIdAndFutureState(Long userId);

    @Query("select b from Booking b where b.item.owner = ?1 and b.status = ?2 order by b.end desc")
    List<Booking> findAllByOwnerIdAndWaitingOrRejectedState(Long userId, Status status);

    //by items
    @Query("select b from Booking b where b.item.id in ?1")
    List<Booking> findAllByItemsId(List<Long> itemsId);
}
