# Despertador Inteligente - Núcleo de Lógica en Java

Este proyecto implementa la lógica de negocio interna y desacoplada para una aplicación de despertador inteligente similar a la de un smartphone moderno, desarrollada con un enfoque robusto orientado a objetos y siguiendo principios de diseño limpio (SOLID).

## 🚀 Características del Sistema

### Funcionalidades Básicas
* **Gestión Completa de Alarmas:** Permite la creación, activación, desactivación y borrado de múltiples alarmas concurrentes.
* **Repetición Semanal Flexible:** Implementación de alarmas mediante estrategias específicas para ejecutarse en días laborables, fines de semana o combinaciones personalizadas.
* **Control de Alarmas Activas:** Consulta y ordenación cronológica de los próximos eventos programados.

### Funcionalidades Avanzadas Implementadas
1. **Alarmas Inteligentes (Detección de Conflictos):** El sistema analiza en tiempo real si dos alarmas activas están programadas con menos de 2 minutos de diferencia, emitiendo una alerta para evitar la superposición caótica de eventos.
2. **Retos Matemáticos de Apagado:** Implementa el patrón *Strategy* para bloquear el apagado de la alarma hasta que el usuario resuelva con éxito una operación matemática adaptada al nivel de dificultad configurado (Fácil, Medio, Difícil).
3. **Modo Vacaciones:** Un interruptor global que pausa la ejecución de toda la parrilla horaria sin necesidad de borrar o desactivar individualmente las alarmas.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java (Versión 21)
* **Entorno de Desarrollo (IDE):** Eclipse IDE / VS Code
* **Control de Versiones:** Git & GitHub

---

## 📂 Estructura del Proyecto

```text
Trabajo_Final_Entornos/
├── src/
│   └── com/
│       └── despertador/
│           └── core/
│               ├── Alarm.java
│               ├── AlarmManager.java
│               ├── Difficulty.java
│               ├── MathChallengeStrategy.java
│               ├── Principal.java
│               ├── RepeatStrategy.java
│               ├── StopStrategy.java
│               └── WeeklyRepeatStrategy.java
└── README.md

---

## ⚙️ Instalación y Ejecución

1. Clona este repositorio en tu máquina local:
   ```bash
   git clone [https://github.com/TU_USUARIO/TU_REPOSITORIO.git](https://github.com/TU_USUARIO/TU_REPOSITORIO.git)