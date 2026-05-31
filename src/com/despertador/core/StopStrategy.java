package com.despertador.core;

/**
 * Interfaz para aplicar el patrón Strategy al método de apagado de la alarma.
 */
public interface StopStrategy {
    boolean canStop();
    String getChallengeInstructions();
    boolean evaluateAnswer(String answer);
}