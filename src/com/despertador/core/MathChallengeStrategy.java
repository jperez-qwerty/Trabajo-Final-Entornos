package com.despertador.core;

import java.util.Random;

/**
 * Estrategia de apagado que obliga al usuario a resolver un problema matemático.
 * Cumple con la funcionalidad avanzada requerida en la práctica.
 */
public class MathChallengeStrategy implements StopStrategy {

    private final Difficulty difficulty;
    private int expectedAnswer;
    private String currentProblem;
    private boolean isSolved;
    private final Random random;

    public MathChallengeStrategy(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.random = new Random();
        this.isSolved = false;
        generateProblem();
    }

    private void generateProblem() {
        int a = 0;
        int b = 0;

        switch (difficulty) {
            case EASY: // Sumas sencillas (1-20)
                a = random.nextInt(20) + 1;
                b = random.nextInt(20) + 1;
                expectedAnswer = a + b;
                currentProblem = "¿Cuánto es " + a + " + " + b + "?";
                break;
            case MEDIUM: // Multiplicaciones básicas (1-10)
                a = random.nextInt(10) + 1;
                b = random.nextInt(10) + 1;
                expectedAnswer = a * b;
                currentProblem = "¿Cuánto es " + a + " * " + b + "?";
                break;
            case HARD: // Operaciones combinadas complejas
                a = random.nextInt(90) + 10; // 10 a 99
                b = random.nextInt(9) + 2;   // 2 a 10
                expectedAnswer = a * b;
                currentProblem = "¿Cuánto es " + a + " * " + b + "?";
                break;
        }
    }

    @Override
    public boolean canStop() {
        return isSolved;
    }

    @Override
    public String getChallengeInstructions() {
        return "¡RETO MATEMÁTICO! Para apagar la alarma resuelve: " + currentProblem;
    }

    @Override
    public boolean evaluateAnswer(String answer) {
        try {
            int userAnswer = Integer.parseInt(answer.trim());
            if (userAnswer == expectedAnswer) {
                isSolved = true;
                System.out.println("✅ ¡Respuesta correcta! Alarma autorizada para apagarse.");
                return true;
            } else {
                System.out.println("❌ Respuesta incorrecta. Inténtalo de nuevo.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Por favor, introduce un número entero válido.");
            return false;
        }
    }
}