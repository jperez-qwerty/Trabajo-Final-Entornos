package com.despertador.core;

import java.time.LocalDate;

/**
 * Interfaz para aplicar el patrón Strategy a las repeticiones de la alarma.
 */
public interface RepeatStrategy {
    boolean shouldRing(LocalDate date);
}