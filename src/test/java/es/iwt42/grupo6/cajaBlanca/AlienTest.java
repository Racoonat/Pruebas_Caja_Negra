
package es.iwt42.grupo6.cajaBlanca;


import main.Commons;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Alien;

import static org.junit.jupiter.api.Assertions.*;

public class AlienTest {

        @Test
        @DisplayName("CB_INITALIEN_01: El constructor inicializa (x, y), la imagen y la Bomb")
        void testInitAlienViaConstructor() {
            int inputX = 80;
            int inputY = 120;

            Alien alien = new Alien(inputX, inputY);

            // (1) Posición inicial
            assertEquals(inputX, alien.getX(), "La X inicial del alien no se estableció correctamente");
            assertEquals(inputY, alien.getY(), "La Y inicial del alien no se estableció correctamente");

            // (2) Imagen cargada
            assertNotNull(alien.getImage(), "La imagen del alien no se cargó (debería ser 'alien.png')");

            // (3) Bomb creada y en estado esperado
            Alien.Bomb bomb = alien.getBomb();
            assertNotNull(bomb, "La bomba no fue inicializada en el constructor del alien");
            assertEquals(inputX, bomb.getX(), "La X inicial de la bomba no coincide con la del alien");
            assertEquals(inputY, bomb.getY(), "La Y inicial de la bomba no coincide con la del alien");
            assertNotNull(bomb.getImage(), "La imagen de la bomba no se cargó (debería ser 'bomb.png')");
            assertTrue(bomb.isDestroyed(), "La bomba debería inicializarse como 'destroyed = true'");
        }

        @Test
        @DisplayName("CB_ALIEN_ACT_01: act() desplaza al alien según 'direction' (positivo, negativo, cero)")
        void testActMovesAlienHorizontally() {
            Alien alien = new Alien(50, 200);

            // Movimiento positivo
            alien.act(5);
            assertEquals(55, alien.getX(), "act(5) no desplazó correctamente al alien hacia la derecha");
            assertEquals(200, alien.getY(), "act() no debería modificar la coordenada Y");

            // Movimiento negativo
            alien.act(-3);
            assertEquals(52, alien.getX(), "act(-3) no desplazó correctamente al alien hacia la izquierda");
            assertEquals(200, alien.getY(), "act() no debería modificar la coordenada Y");

            // Movimiento nulo
            alien.act(0);
            assertEquals(52, alien.getX(), "act(0) no debería modificar la coordenada X");
            assertEquals(200, alien.getY(), "act() no debería modificar la coordenada Y");
        }

        @Test
        @DisplayName("CB_GETBOMB_01: getBomb() devuelve la misma instancia y permite cambiar 'destroyed'")
        void testGetBombAndDestroyedFlag() {
            Alien alien = new Alien(10, 10);
            Alien.Bomb bomb = alien.getBomb();

            // Debe empezar como destroyed = true (ya comprobado arriba, lo reafirmamos)
            assertTrue(bomb.isDestroyed(), "Estado inicial esperado: destroyed = true");

            // Cambiamos el flag y verificamos
            bomb.setDestroyed(false);
            assertFalse(bomb.isDestroyed(), "setDestroyed(false) no actualizó el estado de la bomba");

            bomb.setDestroyed(true);
            assertTrue(bomb.isDestroyed(), "setDestroyed(true) no actualizó el estado de la bomba");

            // La referencia devuelta debe ser estable (misma instancia)
            assertSame(bomb, alien.getBomb(), "getBomb() debería devolver siempre la misma instancia de Bomb");
        }

    @ParameterizedTest
    @CsvSource(value = {
        "179,175", "360,175", "179,351"
    })
    void bombTest(int x, int y) {
        Alien.Bomb bomb = new Alien(Commons.BOARD_WIDTH/2,Commons.BOARD_HEIGHT/2).new Bomb(x,y);
        boolean insideWidthRange = bomb.getX() >= 0 && bomb.getX() <= Commons.BOARD_WIDTH;
        boolean insideHeightRange = bomb.getY() >= 0 && bomb.getY() <= Commons.BOARD_HEIGHT;
        assertTrue(insideWidthRange && insideHeightRange);
    }
}

