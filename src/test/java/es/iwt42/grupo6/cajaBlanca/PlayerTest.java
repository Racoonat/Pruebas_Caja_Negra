package es.iwt42.grupo6.cajaBlanca;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Player;
import java.awt.*;
import java.awt.event.KeyEvent;


import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private final Component dummyComponent = new Component() {};
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
    }

    @Test
    @DisplayName("CB_INITPLAYER_01: El constructor inicializa la posición y la imagen")
    void testInitPlayerViaConstructor() {


        final int START_X = 270;
        final int START_Y = 280;

        assertEquals(START_X, player.getX(), "La X inicial del jugador no es la esperada (270)");
        assertEquals(START_Y, player.getY(), "La Y inicial del jugador no es la esperada (280)");
        assertNotNull(player.getImage(), "La imagen del jugador no se cargó (debería ser 'player.png')");
    }


// Pruebas del método keyPressed(KeyEvent e) 
    @Test
    @DisplayName("KP-1: Flecha Izquierda -> dx = -2")
    void keyPressed_VK_LEFT_SetsDxToMinus2() {
       
        player.setDx(0);
        KeyEvent event = new KeyEvent(dummyComponent, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_LEFT, ' ');
        
       
        player.keyPressed(event);
        
        assertEquals(-2, player.getDx(), "dx debe ser -2 al presionar izquierda");
    }

    @Test
    @DisplayName("KP-2: Flecha Derecha -> dx = 2")
    void keyPressed_VK_RIGHT_SetsDxToPlus2() {
        
        player.setDx(0);
        KeyEvent event = new KeyEvent(dummyComponent, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_RIGHT, ' ');
       
        player.keyPressed(event);
        
        assertEquals(2, player.getDx(), "dx debe ser 2 al presionar derecha");
    }

    @Test
    @DisplayName("KP-3: Otra Tecla (UP) -> dx sin cambios (Cubre rama FALSE)")
    void keyPressed_OtherKey_DxRemainsUnchanged() {
        
        player.setDx(5); // Valor inicial para verificar que no cambia
        KeyEvent event = new KeyEvent(dummyComponent, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_UP, ' ');
       
        player.keyPressed(event);
        
        assertEquals(5, player.getDx(), "dx no debe cambiar para teclas sin acción");
    }

// Pruebas del método keyReleased(KeyEvent e) 
    @Test
    @DisplayName("KR-1: Soltar Izquierda -> dx = 0")
    void keyReleased_VK_LEFT_SetsDxTo0() {
       
        player.setDx(-2); 
        KeyEvent event = new KeyEvent(dummyComponent, KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_LEFT, ' ');
        

        player.keyReleased(event);

        assertEquals(0, player.getDx(), "dx debe ser 0 al soltar izquierda");
    }

    @Test
    @DisplayName("KR-2: Soltar Derecha -> dx = 0")
    void keyReleased_VK_RIGHT_SetsDxTo0() {
        
        player.setDx(2);
        KeyEvent event = new KeyEvent(dummyComponent, KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_RIGHT, ' ');

        player.keyReleased(event);
        
        assertEquals(0, player.getDx(), "dx debe ser 0 al soltar derecha");
    }

    @Test
    @DisplayName("KR-3: Soltar Otra Tecla (SPACE) -> dx sin cambios (Cubre rama FALSE)")
    void keyReleased_OtherKey_DxRemainsUnchanged() {

        player.setDx(2); 
        KeyEvent event = new KeyEvent(dummyComponent, KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_SPACE, ' ');

        player.keyReleased(event);
        
        assertEquals(2, player.getDx(), "dx no debe cambiar para teclas sin acción");
    }

    @ParameterizedTest
    @CsvSource(value = {
            "179,-2", "-1,-2", "359,2",
    })
    void actTest(int x, int dx) {
        player.setX(x);
        player.setDx(dx);
        player.act();
        assertTrue(player.getX() > 0 && player.getX() < 358);
    }
}
