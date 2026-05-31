package com.despertador.core;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Clase principal encargada de gestionar el ciclo de vida de todas las alarmas
 * y coordinar el estado global del despertador inteligente.
 */
public class AlarmManager {

    private final List<Alarm> alarms;
    private boolean vacationMode;

    public AlarmManager() {
        this.alarms = new ArrayList<>();
        this.vacationMode = false;
    }

    /**
     * Añade una alarma al sistema validando previamente que no existan conflictos graves.
     * @param alarm Alarma a añadir.
     * @return true si se añadió con éxito, false si se denegó por conflicto estricto.
     */
    public boolean addAlarm(Alarm alarm) {
        if (alarm == null) return false;

        // Regla de Negocio Avanzada: Detectar conflictos entre alarmas muy cercanas (menos de 2 minutos)
        for (Alarm existingAlarm : alarms) {
            long minuteDifference = Math.abs(ChronoUnit.MINUTES.between(existingAlarm.getTime(), alarm.getTime()));
            if (minuteDifference < 2 && existingAlarm.isActive() && alarm.isActive()) {
                System.out.println("[⚠️ ALERTA INTERNA] Conflicto potencial: La alarma '" + alarm.getLabel() 
                        + "' está a menos de 2 minutos de '" + existingAlarm.getLabel() + "'.");
                // Decisión de diseño: Permitimos añadirla pero notificamos el conflicto.
            }
        }

        return alarms.add(alarm);
    }

    /**
     * Elimina una alarma del sistema mediante su identificador único.
     */
    public boolean removeAlarm(UUID id) {
        return alarms.removeIf(alarm -> alarm.getId().equals(id));
    }

    /**
     * Activa o desactiva el Modo Vacaciones.
     * Si está activo, ninguna alarma programada sonará temporalmente.
     */
    public void setVacationMode(boolean active) {
        this.vacationMode = active;
        System.out.println("[INFO] Modo Vacaciones: " + (active ? "ACTIVADO" : "DESACTIVADO"));
    }

    public boolean isVacationMode() {
        return vacationMode;
    }

    /**
     * Filtra y devuelve una lista de todas las alarmas actualmente habilitadas en el sistema.
     */
    public List<Alarm> getActiveAlarms() {
        return alarms.stream()
                .filter(Alarm::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve una lista de alarmas ordenadas cronológicamente por su hora de ejecución.
     */
    public List<Alarm> getUpcomingAlarms() {
        return alarms.stream()
                .sorted((a1, a2) -> a1.getTime().compareTo(a2.getTime()))
                .collect(Collectors.toList());
    }

    /**
     * Método crítico: Simula el "tick" del reloj del sistema. Debe llamarse de forma recurrente
     * (por ejemplo, cada minuto simulado) para comprobar qué alarmas deben dispararse.
     * * @param currentTime Fecha y hora simulada o real a evaluar.
     */
    public void checkAlarms(LocalDateTime currentTime) {
        if (vacationMode) {
            // Si el modo vacaciones está activo, se ignora el chequeo temporal
            return;
        }

        LocalTime timeToCheck = currentTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES);
        List<Alarm> triggeredInThisTick = new ArrayList<>();

        for (Alarm alarm : alarms) {
            if (alarm.isActive()) {
                // 1. Comprobar si es la hora exacta (filtrando segundos)
                boolean isExactTime = alarm.getTime().truncatedTo(ChronoUnit.MINUTES).equals(timeToCheck);
                
                // 2. Comprobar si la estrategia de repetición valida el día actual
                boolean shouldRingToday = alarm.getRepeatStrategy().shouldRing(currentTime.toLocalDate());

                // 3. Comprobar si hay un Snooze (pospuesto) activo programado para este momento exacto
                boolean isSnoozeActive = alarm.isSnoozed() && 
                        alarm.getNextSnoozeTime() != null &&
                        alarm.getNextSnoozeTime().truncatedTo(ChronoUnit.MINUTES).equals(currentTime.truncatedTo(ChronoUnit.MINUTES));

                if ((isExactTime && shouldRingToday) || isSnoozeActive) {
                    triggeredInThisTick.add(alarm);
                }
            }
        }

        // Gestión de alarmas simultáneas (Aspecto obligatorio a analizar)
        if (!triggeredInThisTick.isEmpty()) {
            consolidateAndTriggerAlarms(triggeredInThisTick, currentTime);
        }
    }

    /**
     * Maneja la simultaneidad. Si varias alarmas coinciden al mismo tiempo, las consolida
     * para evitar la superposición caótica de flujos.
     */
    private void consolidateAndTriggerAlarms(List<Alarm> triggeredAlarms, LocalDateTime currentTime) {
        System.out.println("\n=========================================");
        System.out.println("⏰ [ALERTA] RANG DEL DESPERTADOR A LAS " + currentTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES));
        System.out.println("=========================================");
        
        for (Alarm alarm : triggeredAlarms) {
            System.out.println("🔔 Ejecutando: [" + alarm.getLabel() + "]");
            alarm.trigger(); // Cambia el estado de la alarma a "SONANDO"
        }
    }
}