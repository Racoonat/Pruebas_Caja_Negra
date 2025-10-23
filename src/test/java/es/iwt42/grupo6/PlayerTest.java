package es.iwt42.grupo6;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Player;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    // Componente falso y final que se usa para poder crear un objeto KeyEvent.
    private final Component dummyComponent = new Component() {};

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
    @Test
    void testPlayerInitialization() {
        Player player = new Player();
        assertNotNull(player, "El objeto Player no debería ser nulo después de la creación");
    }


    // --- Pruebas del método keyPressed() ---

    @Test
    @DisplayName("CP-P-: Pulsar flecha izquierda causa movimiento a la izquierda")
    void testKeyPressed_CausesLeftMovement() {
        Player player = new Player();
        KeyEvent pressLeft = new KeyEvent(dummyComponent, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_LEFT, ' ');
        int initialX = player.getX();
        player.keyPressed(pressLeft);
        player.act();
        int finalX = player.getX();
        assertTrue(finalX < initialX, "El jugador debería haberse movido a la izquierda.");
    }

    @Test
    @DisplayName("CP-P-: Pulsar flecha derecha establece x a 2")
    void testKeyPressed_Right() {
        Player player = new Player();
        KeyEvent pressRight = new KeyEvent(dummyComponent, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_RIGHT, ' ');
        int initialX = player.getX();
        player.keyPressed(pressRight);
        player.act();
        int finalX = player.getX();
        assertTrue(finalX > initialX, "El jugador debería haberse movido a la derecha");
    }

    @Test
    @DisplayName("CP-P-: Pulsar otra tecla (espacio) no debe cambiar dx")
    void testKeyPressed_OtherKey() {
        Player player = new Player();
        KeyEvent pressSpace = new KeyEvent(dummyComponent, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_SPACE, ' ');
        int initialX = player.getX();
        player.keyPressed(pressSpace);
        player.act();
        int finalX = player.getX();
        assertEquals(initialX, finalX, "El jugador no debería moverse al presionar una tecla sin acción");
    }

    // --- Pruebas del método keyReleased() ---

    @Test
    @DisplayName("CP-P-: Soltar flecha izquierda detiene el movimiento")
    void testKeyReleased_Left() {
        Player player = new Player();
        KeyEvent pressLeft = new KeyEvent(dummyComponent, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_LEFT, ' ');
        KeyEvent releaseLeft = new KeyEvent(dummyComponent, KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_LEFT, ' ');

        player.keyPressed(pressLeft);
        player.act();

        player.keyReleased(releaseLeft);
        int stoppedX = player.getX();

        player.act();
        int finalX = player.getX();

        assertEquals(stoppedX, finalX, "El jugador debería haberse detenido después de soltar la tecla izquierda");
    }

    @Test
    @DisplayName("CP-P-: Soltar flecha derecha detiene el movimiento")
    void testKeyReleased_Right() {
        Player player = new Player();
        KeyEvent pressRight = new KeyEvent(dummyComponent, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_RIGHT, ' ');
        KeyEvent releaseRight = new KeyEvent(dummyComponent, KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_RIGHT, ' ');

        player.keyPressed(pressRight);
        player.act();

        player.keyReleased(releaseRight);
        int stoppedX = player.getX();

        player.act();
        int finalX = player.getX();

        assertEquals(stoppedX, finalX, "El jugador debería haberse detenido después de soltar la tecla derecha");
    }

    @Test
    @DisplayName("CP-P-: Soltar otra tecla (espacio) no debe detener el movimiento")
    void testKeyReleased_OtherKey() {
        Player player = new Player();
        KeyEvent pressRight = new KeyEvent(dummyComponent, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_RIGHT, ' ');
        KeyEvent releaseRight = new KeyEvent(dummyComponent, KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_SPACE, ' ');

        player.keyPressed(pressRight);
        player.act();

        player.keyReleased(releaseRight);
        int stoppedX = player.getX();

        player.act();
        int finalX = player.getX();

        assertTrue(stoppedX < finalX, "El jugador debería seguir moviendose");
    }
}
