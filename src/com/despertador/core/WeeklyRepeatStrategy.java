package com.despertador.core;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

/**
 * Estrategia de repetición basada en los días de la semana.
 * Permite configurar alarmas para días específicos, días laborables o fines de semana.
 */
public class WeeklyRepeatStrategy implements RepeatStrategy {

    private final Set<DayOfWeek> daysToRing;

    /**
     * Constructor flexible para pasar un conjunto de días personalizados.
     */
    public WeeklyRepeatStrategy(Set<DayOfWeek> daysToRing) {
        // Usamos EnumSet por rendimiento y seguridad en Java con enums
        this.daysToRing = EnumSet.copyOf(daysToRing);
    }

    /**
     * Método de conveniencia para crear rápidamente una estrategia de "Solo días laborables" (Lunes a Viernes).
     */
    public static WeeklyRepeatStrategy weekdays() {
        return new WeeklyRepeatStrategy(EnumSet.of(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
        ));
    }

    /**
     * Método de conveniencia para crear rápidamente una estrategia de "Fines de semana" (Sábado y Domingo).
     */
    public static WeeklyRepeatStrategy weekends() {
        return new WeeklyRepeatStrategy(EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));
    }

    @Override
    public boolean shouldRing(LocalDate date) {
        // Averigua qué día de la semana es la fecha que le pasa el AlarmManager
        DayOfWeek dayOfDate = date.getDayOfWeek();
        // Si ese día está en nuestro conjunto de días programados, devuelve true
        return daysToRing.contains(dayOfDate);
    }
    
    @Override
    public String toString() {
        if (daysToRing.size() == 7) return "Todos los días";
        if (daysToRing.size() == 5 && !daysToRing.contains(DayOfWeek.SATURDAY)) return "Lunes a Viernes";
        if (daysToRing.size() == 2 && daysToRing.contains(DayOfWeek.SATURDAY)) return "Fines de semana";
        return daysToRing.toString(); // Muestra los días sueltos [MONDAY, WEDNESDAY]
    }
}