package it.unibz.src.reservation;

import java.util.List;

public class ReservationRepository {
	
	public static List<String> getReservationNames(List<Reservation> reservations) {
        if(reservations == null)
            throw new RuntimeException("Invalid list of Reservations");
        return reservations.stream().map(Reservation::getReservationName).distinct().collect(Collectors.toList());
    }

    public static String getReservationDetails(Class<? extends Reservation> reservationClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return reservationClass.getConstructor().newInstance().getReservationDetails();
    }

    public static String getReservationDetails(String reservationName, List<Reservation> reservations) {
        Reservation reservation = reservations.stream().filter(reservation -> reservation.getReservationName().equals(reservationName)).findFirst().orElse(null);

        if(reservation == null)
            throw new RuntimeException("No matching reservation names");
        else
            return reservation.getReservationDetails();
    }

    public static Reservation getReservationByID(int reservationID, List<Reservation> reservations) {
        Reservation reservation = reservations.stream().filter(reservation -> reservation.getId() == reservationID).findFirst().orElse(null);

        if(reservation == null)
            throw new RuntimeException("No reservation present with give ID");
        else
            return reservation;
    }

}
