package com.despertador.core;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class Alarm {
    private final UUID id;
    private LocalTime time;
    private String label;
    private boolean isActive;
    private boolean isSnoozed;
    private LocalDateTime nextSnoozeTime;
    
    private RepeatStrategy repeatStrategy;
    private StopStrategy stopStrategy;

    public Alarm(LocalTime time, String label, RepeatStrategy repeatStrategy, StopStrategy stopStrategy) {
        this.id = UUID.randomUUID();
        this.time = time;
        this.label = label;
        this.isActive = true;
        this.isSnoozed = false;
        this.repeatStrategy = repeatStrategy;
        this.stopStrategy = stopStrategy;
    }

    // Métodos de control de comportamiento
    public void trigger() {
        System.out.println("🎵 Reproduciendo sonido asignado a la alarma: " + label);
    }

    public void snooze(int minutes) {
        this.isSnoozed = true;
        this.nextSnoozeTime = LocalDateTime.now().plusMinutes(minutes);
        System.out.println("💤 Alarma '" + label + "' pospuesta por " + minutes + " minutos.");
    }

    public void stop() {
        if (stopStrategy.canStop()) {
            this.isSnoozed = false;
            this.nextSnoozeTime = null;
            System.out.println("🛑 Alarma '" + label + "' apagada con éxito.");
        } else {
            System.out.println("❌ No se puede apagar la alarma. Requisito no completado.");
        }
    }

    // Getters y Setters necesarios para el AlarmManager
    public UUID getId() { return id; }
    public LocalTime getTime() { return time; }
    public String getLabel() { return label; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public boolean isSnoozed() { return isSnoozed; }
    public LocalDateTime getNextSnoozeTime() { return nextSnoozeTime; }
    public RepeatStrategy getRepeatStrategy() { return repeatStrategy; }
    public StopStrategy getStopStrategy() { return stopStrategy; }
}