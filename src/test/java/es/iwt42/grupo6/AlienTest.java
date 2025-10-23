package es.iwt42.grupo6;

import main.Commons;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class AlienTest {
    private static final int W = Commons.BOARD_WIDTH;
    private static final int H = Commons.BOARD_HEIGHT;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
    // -------------------- Constructor Alien---------------
    @Test
    @DisplayName("Constructor: El constructor de Alien inicializa posición, imagen y bomba destruida")
    void constructorInitializesState() {
        // GIVEN
        int x = 100;
        int y = 200;

        // WHEN
        Alien alien = new Alien(x, y);

        // THEN (caja negra: usamos sólo la interfaz pública)
        // 1) Hay instancia y la bomba existe
        assertNotNull(alien, "Alien no debería ser null");
        Alien.Bomb bomb = alien.getBomb();
        assertNotNull(bomb, "La bomba del Alien debería estar inicializada");

        // 2) La posición inicial es la solicitada (vía getters heredados de Sprite)
        assertEquals(x, alien.getX(), "X inicial del Alien no coincide");
        assertEquals(y, alien.getY(), "Y inicial del Alien no coincide");

        // 3) La imagen está establecida (no validamos el archivo, sólo que hay Image)
        Image alienImg = alien.getImage();
        assertNotNull(alienImg, "Alien debería tener imagen asignada por el constructor");

        // 4) La bomba comienza marcada como 'destroyed' según contrato observable
        assertTrue(bomb.isDestroyed(), "La bomba debería iniciar destruida");

        // 5) (Opcional caja negra) La bomba hereda posición del Alien al crearse
        assertEquals(x, bomb.getX(), "X inicial de la Bomba debería coincidir con la del Alien");
        assertEquals(y, bomb.getY(), "Y inicial de la Bomba debería coincidir con la del Alien");
    }

    // ----------------------- initAlien -----------------------
    @Test
    @DisplayName("initAlien: coordenadas válidas dentro de los límites")
    void testInitAlien_ValidCoordinates() {
        Alien alien = new Alien(300, 200);
        assertEquals(300, alien.getX());
        assertEquals(200, alien.getY());
    }

    @Test @DisplayName("initAlien: X negativa -> se corrige a 0")
    void testInitAlien_NegativeX() {
        Alien alien = new Alien(-10, 100);
        assertEquals(0, alien.getX());
        assertEquals(100, alien.getY());
    }

    @Test @DisplayName("initAlien: Y negativa -> se corrige a 0")
    void testInitAlien_NegativeY() {
        Alien alien = new Alien(100, -10);
        assertEquals(100, alien.getX());
        assertEquals(0, alien.getY());
    }

    @Test @DisplayName("initAlien: X excede límite -> clamp a BOARD_WIDTH")
    void testInitAlien_InvalidCoordinatesX() {
        Alien alien = new Alien(W + 25, 100);
        assertEquals(W, alien.getX());
        assertEquals(100, alien.getY());
    }

    @Test @DisplayName("initAlien: Y excede límite -> clamp a BOARD_HEIGHT")
    void testInitAlien_InvalidCoordinatesY() {
        Alien alien = new Alien(100, H + 25);
        assertEquals(100, alien.getX());
        assertEquals(H, alien.getY());
    }

    @Test @DisplayName("initAlien: bordes exactos (0,0) y (W,H) se mantienen")
    void testInitAlien_ExactBorders() {
        Alien a1 = new Alien(0, 0);
        assertEquals(0, a1.getX());
        assertEquals(0, a1.getY());

        Alien a2 = new Alien(W, H);
        assertEquals(W, a2.getX());
        assertEquals(H, a2.getY());
    }

    // ------------------------------ act(direction) ----------------------------
    @Test @DisplayName("act: direction=0 -> sin movimiento, Y invariante")
    void testAlienMovementZero() {
        Alien alien = new Alien(100, 200);
        alien.act(0);
        assertEquals(100, alien.getX());
        assertEquals(200, alien.getY());
    }

    @Test @DisplayName("act: derecha (+20) -> X = x0 + 20, Y constante")
    void testAlienMovementRight() {
        Alien alien = new Alien(100, 200);
        alien.act(20);
        assertEquals(120, alien.getX());
        assertEquals(200, alien.getY());
    }

    @Test @DisplayName("act: izquierda (-10) -> X = x0 - 10, Y constante")
    void testAlienMovementLeft() {
        Alien alien = new Alien(100, 200);
        alien.act(-10);
        assertEquals(90, alien.getX());
        assertEquals(200, alien.getY());
    }

    @Test @DisplayName("act: secuencia (+3, -5) -> X = x0 - 2, Y invariante")
    void testAlienMovementSequence() {
        Alien alien = new Alien(70, 120);
        alien.act(3);
        alien.act(-5);
        assertEquals(68, alien.getX());
        assertEquals(120, alien.getY());
    }
}