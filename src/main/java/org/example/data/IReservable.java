package org.example.data;

public interface IReservable {
    int seatCount();
    int resevableSeatCount();
    Boolean isReservable(int passengerCount);
    void reserveSeat(int seatCount);
}
