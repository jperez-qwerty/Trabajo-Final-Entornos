package com.despertador.core;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Scanner;

/**
 * Clase de ejecución principal que sirve como banco de pruebas (Simulador).
 * Permite validar el cumplimiento de los requisitos funcionales sin interfaz gráfica.
 */
public class Principal {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("⏰ INICIANDO SIMULADOR DE DESPERTADOR INTELIGENTE ⏰");
        System.out.println("==================================================");

        // 1. Instanciar el gestor central
        AlarmManager manager = new AlarmManager();

        // 2. Crear una estrategia de repetición (Lunes a Viernes)
        WeeklyRepeatStrategy diasLaborables = WeeklyRepeatStrategy.weekdays();

        // 3. Crear una estrategia de apagado avanzada (Reto Matemático Medio)
        MathChallengeStrategy retoMatematico = new MathChallengeStrategy(Difficulty.MEDIUM);

        // 4. Crear la Alarma Principal (07:00 AM)
        Alarm alarmaDespertar = new Alarm(
                LocalTime.of(7, 0),
                "Despertar para ir a clase/trabajo",
                diasLaborables,
                retoMatematico
        );

        // 5. Crear una Alarma Secundaria muy cercana (07:01 AM) para testear la regla de conflicto
        Alarm alarmaSecundaria = new Alarm(
                LocalTime.of(7, 1),
                "Tomar café rápido",
                diasLaborables,
                new StopStrategy() { 
                    // Una estrategia simple temporal (Anónima) para no requerir retos en esta
                    @Override public boolean canStop() { return true; }
                    @Override public String getChallengeInstructions() { return "Presiona detener para apagar."; }
                    @Override public boolean evaluateAnswer(String ans) { return true; }
                }
        );

        // 6. Registrar las alarmas en el sistema
        System.out.println("\n[Configuración] Registrando Alarma 1...");
        manager.addAlarm(alarmaDespertar);
        
        System.out.println("[Configuración] Registrando Alarma 2 (Prueba de conflicto)...");
        manager.addAlarm(alarmaSecundaria);

        // 7. COMENZAR LA SIMULACIÓN TEMPORAL
        // Vamos a simular un Lunes cualquiera (2026-06-01 es Lunes) comenzando a las 06:58 AM
        LocalDateTime relojSimulado = LocalDateTime.of(2026, 6, 1, 6, 58);
        
        System.out.println("\n==================================================");
        System.out.println("🚀 INICIANDO VIAJE EN EL TIEMPO SIMULADO (Minuto a Minuto)");
        System.out.println("==================================================");

        Scanner scanner = new Scanner(System.in);

        // Simularemos 5 minutos de paso del tiempo
        for (int i = 0; i < 5; i++) {
            System.out.println("\n⏰ [Reloj del Sistema]: " + relojSimulado.toLocalTime() + " (" + relojSimulado.getDayOfWeek() + ")");
            
            // El gestor comprueba si alguna alarma debe dispararse en este minuto exacto
            manager.checkAlarms(relojSimulado);

            // Si la alarma del reto matemático está sonando en este minuto...
            if (relojSimulado.toLocalTime().getHour() == 7 && relojSimulado.toLocalTime().getMinute() == 0) {
                System.out.println("\n" + retoMatematico.getChallengeInstructions());
                
                boolean resuelto = false;
                while (!resuelto) {
                    System.out.print("Introduce tu respuesta en consola: ");
                    String respuesta = scanner.nextLine();
                    
                    // Evaluamos la respuesta introducida por el usuario
                    resuelto = retoMatematico.evaluateAnswer(respuesta);
                    
                    if (resuelto) {
                        alarmaDespertar.stop();
                    } else {
                        System.out.println("❌ El despertador sigue sonando ruidosamente... ¡Despierta!");
                    }
                }
            }

            // Avanzamos el reloj simulado 1 minuto para el siguiente ciclo
            relojSimulado = relojSimulado.plusMinutes(1);
            
            // Pequeña pausa estética en la consola para simular realismo
            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        System.out.println("\n==================================================");
        System.out.println("🏁 FIN DE LA SIMULACIÓN COMPLETA SIN ERRORES");
        System.out.println("==================================================");
        scanner.close();
    }
}