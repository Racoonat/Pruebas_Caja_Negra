package es.iwt42.grupo6.cajaNegra;

import main.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
        // Evitamos que corra en segundo plano en los tests
        board.getTimer().stop();
        // Estado conocido
        board.setMessage("Game Over");
        board.setInGame(true);
        board.setDirection(-1);
        board.setDeaths(0);
    }

    @Test
    void initBoardTest() {

        assertNotNull(board, "El objeto Board no debe ser nulo");

        // Validar que los componentes principales estén inicializados
        assertNotNull(board.getAliens(), "La lista de aliens debe estar inicializada");
        assertNotNull(board.getPlayer(), "El jugador debe estar inicializado");
        assertNotNull(board.getShot(), "El disparo debe estar inicializado");

        // Verificar cantidad inicial de aliens
        assertEquals(24, board.getAliens().size(), "Debe haber 24 aliens iniciales (4x6)");

        // Verificar tipo de objetos
        for (Object obj : board.getAliens()) {
            assertInstanceOf(Alien.class, obj, "Cada elemento debe ser un Alien");
        }

        // El jugador y disparo deben existir
        assertInstanceOf(Player.class, board.getPlayer(), "Debe haber un objeto Player");
        assertInstanceOf(Shot.class, board.getShot(), "Debe haber un objeto Shot");
    }

    private void tickOnce() {
        Timer t = board.getTimer();
        assertNotNull(t, "El Timer de Board no debería ser null");
        ActionListener[] listeners = t.getActionListeners();
        assertTrue(listeners.length > 0, "El Timer debería tener al menos un ActionListener");
        listeners[0].actionPerformed(new ActionEvent(t, ActionEvent.ACTION_PERFORMED, "test"));
    }

    // ------------------- PRUEBAS DE update() (caja negra por ciclo) -------------------

    @Test
    @DisplayName("CP-B-UPDATE-01: Si deaths == 24 -> se gana, inGame=false, timer se para, mensaje 'Game won!'")
    void testUpdate_GameWon() {
        board.getTimer().start();
        assertTrue(board.getTimer().isRunning(), "El timer debería arrancar");

        board.setDeaths(24); // condición de victoria
        tickOnce();

        assertFalse(board.isInGame(), "Debería quedar fuera de juego al ganar");
        assertEquals("Game won!", board.getMessage(), "Mensaje de victoria incorrecto");
        assertFalse(board.getTimer().isRunning(), "El timer debe detenerse tras la victoria");
    }

    @Test
    @DisplayName("CP-B-UPDATE-02: Si deaths != 24 -> el timer sigue, inGame sigue true y NO se muestra 'Game won!'")
    void testUpdate_NoWin_KeepsRunningAndInGame() {
        board.getTimer().start();
        board.setDeaths(0);

        tickOnce();

        assertTrue(board.getTimer().isRunning(), "El timer debería seguir corriendo");
        assertTrue(board.isInGame(), "El juego debería seguir en marcha");
        assertNotEquals("Game won!", board.getMessage(), "No debería mostrar 'Game won!'");
    }

    // ------------------- PRUEBAS DE update_aliens() (vía update()) -------------------

    @Test
    @DisplayName("CP-B-ALIENS-01: Borde derecho (x>=328) con direction=-1 -> todos bajan 15px y direction se mantiene en -1")
    void testUpdateAliens_RightEdge_DropAndKeepMinusOne() {
        List<Alien> custom = new ArrayList<>();
        custom.add(new Alien(328, 10));  // fuerza borde derecho
        custom.add(new Alien(200, 20));
        custom.add(new Alien(250, 30));
        board.setAliens(custom);
        board.setDirection(-1);

        List<Integer> yBefore = custom.stream().map(Alien::getY).toList();

        tickOnce();

        for (int i = 0; i < custom.size(); i++) {
            assertEquals(yBefore.get(i) + 15, custom.get(i).getY(), "Todos los aliens deben bajar 15px");
        }
        assertEquals(-1, board.getDirection(), "direction debería seguir en -1 según la implementación actual");
    }

    @Test
    @DisplayName("CP-B-ALIENS-02: Borde izquierdo (x<=5) con direction != 1 -> todos bajan 15px y direction pasa a 1")
    void testUpdateAliens_LeftEdge_DropAndSetToOne() {
        List<Alien> custom = new ArrayList<>();
        custom.add(new Alien(5, 50));    // fuerza borde izquierdo
        custom.add(new Alien(100, 60));
        custom.add(new Alien(180, 70));
        board.setAliens(custom);
        board.setDirection(-1); // != 1

        List<Integer> yBefore = custom.stream().map(Alien::getY).toList();

        tickOnce();

        for (int i = 0; i < custom.size(); i++) {
            assertEquals(yBefore.get(i) + 15, custom.get(i).getY(), "Todos los aliens deben bajar 15px");
        }
        assertEquals(1, board.getDirection(), "direction debería cambiar a 1");
    }

    @Test
    @DisplayName("CP-B-ALIENS-03: Sin tocar bordes -> no hay descenso masivo ni cambio de dirección")
    void testUpdateAliens_NoEdges_NoDrop_NoDirectionChange() {
        List<Alien> custom = new ArrayList<>();
        custom.add(new Alien(150, 40));
        custom.add(new Alien(200, 60));
        custom.add(new Alien(250, 80));
        board.setAliens(custom);
        board.setDirection(-1);

        List<Integer> yBefore = custom.stream().map(Alien::getY).toList();

        tickOnce();

        // No debería haberse aplicado el +15 ni +30 (descenso masivo)
        for (int i = 0; i < custom.size(); i++) {
            int yAfter = custom.get(i).getY();
            assertNotEquals(yBefore.get(i) + 15, yAfter, "No debería haber descenso masivo (+15) sin borde");
            assertNotEquals(yBefore.get(i) + 30, yAfter, "No debería haber doble descenso (+30) sin borde");
        }
        assertEquals(-1, board.getDirection(), "direction debería mantenerse si no hay borde");
    }

    @Test
    @DisplayName("CP-B-ALIENS-04: Borde derecho y borde izquierdo presentes (con direction inicial -1) -> doble descenso (30px) y direction final=1")
    void testUpdateAliens_BothEdges_DoubleDrop_FinalDirOne() {
        List<Alien> custom = new ArrayList<>();
        custom.add(new Alien(328, 10)); // derecha
        custom.add(new Alien(5, 20));   // izquierda
        board.setAliens(custom);
        board.setDirection(-1);

        List<Integer> yBefore = custom.stream().map(Alien::getY).toList();

        tickOnce();

        for (int i = 0; i < custom.size(); i++) {
            assertEquals(yBefore.get(i) + 30, custom.get(i).getY(), "Debe aplicarse doble descenso (+30) al cumplirse ambos bordes");
        }
        assertEquals(1, board.getDirection(), "direction debería acabar en 1 tras tocar el borde izquierdo");
    }

    @Test
    @DisplayName("CP-B-ALIENS-05: Invasión (algún alien visible con y>302) -> message='Invasion!' y inGame=true (según código)")
    void testUpdateAliens_Invasion_Visible() {
        List<Alien> custom = new ArrayList<>();
        custom.add(new Alien(100, 303)); // fuerza invasión
        board.setAliens(custom);

        tickOnce();

        assertEquals("Invasion!", board.getMessage(), "Mensaje esperado 'Invasion!'");
        assertTrue(board.isInGame(), "Según la implementación, inGame se fija en true en invasión");
    }

    // CP-B-ALIENS-06: Alien NO visible con y>302 no dispara invasión (check solo para visibles)
    @Test
    @DisplayName("CP-B-ALIENS-06: Alien NO visible con y>302 no dispara invasión")
    void testUpdateAliens_Invasion_InvisibleNotTriggered() {
        List<Alien> custom = new ArrayList<>();
        Alien hidden = new Alien(120, 310);
        // Caja negra: usamos la API pública para volverlo no visible
        hidden.die(); // => isVisible() debe pasar a false
        custom.add(hidden);

        // Añadimos otro alien visible en zona segura para evitar bordes/invasión
        custom.add(new Alien(150, 40));

        board.setAliens(custom);
        board.setMessage("Game Over"); // valor control

        tickOnce(); // update -> update_aliens

        assertFalse(custom.get(0).isVisible(), "El alien marcado como die() debería no ser visible");
        assertNotEquals("Invasion!", board.getMessage(),
                "Un alien no visible con y>302 NO debe disparar 'Invasion!'");
    }

    // CP-B-ALIENS-07: Descenso por borde afecta también a aliens no visibles
    @Test
    @DisplayName("CP-B-ALIENS-07: El descenso por borde también afecta a aliens no visibles")
    void testUpdateAliens_DropAffectsInvisibleToo() {
        List<Alien> custom = new ArrayList<>();
        Alien invisibleRight = new Alien(328, 40);
        invisibleRight.die(); // no visible
        custom.add(invisibleRight);

        Alien visibleMid = new Alien(200, 50);
        custom.add(visibleMid);

        board.setAliens(custom);
        board.setDirection(-1);

        int yInvisibleBefore = invisibleRight.getY();
        int yVisibleBefore = visibleMid.getY();

        tickOnce(); // update -> update_aliens

        // El código del drop por borde no filtra visibilidad: todos bajan 15px
        assertEquals(yInvisibleBefore + 15, invisibleRight.getY(),
                "El alien no visible también debe bajar 15px por borde");
        assertEquals(yVisibleBefore + 15, visibleMid.getY(),
                "El alien visible debe bajar 15px por borde");
    }
}
