package org.example.data;

public class Transport implements IReservable{

    @Override
    public int seatCount() {
        return seatCount;
    }

    @Override
    public int resevableSeatCount() {
        return availableSeatCount;
    }

    @Override
    public Boolean isReservable(int passengerCount) {
        return this.availableSeatCount >= passengerCount;
    }

    @Override
    public void reserveSeat(int seatCount){
        if (seatCount > this.availableSeatCount) throw new RuntimeException("no empty seat");
        this.availableSeatCount = this.availableSeatCount - seatCount;
        this.reservedSeatCount += seatCount;
    }

    private int seatCount;

    public Transport(int seatCount) {
        this.seatCount = seatCount;
        this.availableSeatCount = seatCount;
    }

    private int reservedSeatCount = 0;

    private int availableSeatCount;

}
