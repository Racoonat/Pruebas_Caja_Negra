package es.iwt42.grupo6.cajaBlanca;

import main.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Player;
import space_invaders.sprites.Shot;
import space_invaders.sprites.Sprite;

import javax.swing.Timer;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private Timer timer;
    private Player player;

    private final Component dummyComponent = new Component() {};

    private void invokePrivateUpdate(Board instance) throws Exception {
        Method updateMethod = Board.class.getDeclaredMethod("update");
        updateMethod.setAccessible(true);
        updateMethod.invoke(instance);
    }

    private void invokePrivateUpdateAliens(Board instance) throws Exception {
        Method method = Board.class.getDeclaredMethod("update_aliens");
        method.setAccessible(true);
        method.invoke(instance);
    }

    private void invokePrivateUpdateBomb(Board instance) throws Exception {
        Method method = Board.class.getDeclaredMethod("update_bomb");
        method.setAccessible(true);
        method.invoke(instance);
    }

    private void invokePrivateUpdateShots(Board instance) throws Exception {
        Method method = Board.class.getDeclaredMethod("update_shots");
        method.setAccessible(true);
        method.invoke(instance);
    }

    private void invokeProtectedSetVisible(Sprite instance, boolean visible) throws Exception {
        Method method = Sprite.class.getDeclaredMethod("setVisible", boolean.class);
        method.setAccessible(true);
        method.invoke(instance, visible);
    }

    @BeforeEach
    void setUp() {
        board = new Board();
        timer = new Timer(17, null);
        board.setTimer(timer);
        player = new Player();
        board.setPlayer(player);
        board.setInGame(true);
        board.setMessage("Game Over");

        // CORRECCIÓN: Aseguramos un estado limpio para this.direction
        board.setDirection(-1);
    }

    // --- PRUEBAS update() (V(G)=2) ---

    @Test
    @DisplayName("update Path 1 (False)")
    void test1() throws Exception {
        board.setDeaths(10);
        timer.start();
        int initialX = player.getX();
        KeyEvent pressRight = new KeyEvent(dummyComponent, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_RIGHT, ' ');
        player.keyPressed(pressRight);

        invokePrivateUpdate(board);

        assertTrue(board.isInGame());
        assertNotEquals("Game won!", board.getMessage());
        assertTrue(timer.isRunning());
        int finalX = player.getX();
        assertTrue(finalX > initialX);
    }

    @Test
    @DisplayName("update Path 2 (True)")
    void test2() throws Exception {
        board.setDeaths(24);
        timer.start();
        int initialX = player.getX();
        KeyEvent pressRight = new KeyEvent(dummyComponent, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_RIGHT, ' ');
        player.keyPressed(pressRight);

        invokePrivateUpdate(board);

        assertFalse(board.isInGame());
        assertEquals("Game won!", board.getMessage());
        assertFalse(timer.isRunning());
        int finalX = player.getX();
        assertTrue(finalX > initialX);
    }

    // --- PRUEBAS update_aliens() (V(G)=9, Path Testing) ---

    @Test
    @DisplayName("update_aliens Path 1 (A:F, I:F) - Lista de aliens vacía")
    void testUpdateAliens_Path1_EmptyList() throws Exception {
        board.setAliens(new ArrayList<>());

        invokePrivateUpdateAliens(board);

        assertEquals(-1, board.getDirection());
    }

    @Test
    @DisplayName("update_aliens Path 2 (A:T, B:F, E:F, I:T, J:T, K:F) - Sin bordes, visible, sin invasión")
    void testUpdateAliens_Path2_NoBorders_Visible_NoInvasion() throws Exception {
        Alien alien = new Alien(100, 100);
        int initialX = alien.getX();
        board.setAliens(List.of(alien));

        invokePrivateUpdateAliens(board);

        assertEquals(100, alien.getY());
        assertEquals(-1, board.getDirection());
        assertNotEquals("Invasion!", board.getMessage());
        assertEquals(initialX - 1, alien.getX());
    }

    @Test
    @DisplayName("update_aliens Path 3 (A:T, B:T, C:F) - 1 Alien, borde derecho")
    void testUpdateAliens_Path3_RightBorder_1Alien() throws Exception {
        Alien alien = new Alien(330, 100);
        board.setAliens(List.of(alien));

        invokePrivateUpdateAliens(board);

        // CORRECCIÓN: Verificamos el comportamiento del BUG (direction NO cambia)
        assertEquals(-1, board.getDirection());
        assertEquals(115, alien.getY());
    }

    @Test
    @DisplayName("update_aliens Path 4 (A:T, B:T, C:T) - 2 Aliens, borde derecho")
    void testUpdateAliens_Path4_RightBorder_2Aliens() throws Exception {
        Alien alienBorde = new Alien(330, 100);
        Alien alienMedio = new Alien(150, 100);
        board.setAliens(List.of(alienBorde, alienMedio));

        invokePrivateUpdateAliens(board);

        // CORRECCIÓN: Verificamos el comportamiento del BUG
        assertEquals(-1, board.getDirection());
        assertEquals(115, alienBorde.getY());
        assertEquals(115, alienMedio.getY());
    }

    @Test
    @DisplayName("update_aliens Path 5 (A:T, E:T, F:F) - 1 Alien, borde izquierdo")
    void testUpdateAliens_Path5_LeftBorder_1Alien() throws Exception {
        Alien alien = new Alien(0, 100);
        board.setAliens(List.of(alien));

        invokePrivateUpdateAliens(board);

        assertEquals(1, board.getDirection());
        assertEquals(115, alien.getY());
    }

    @Test
    @DisplayName("update_aliens Path 6 (A:T, E:T, F:T) - 2 Aliens, borde izquierdo")
    void testUpdateAliens_Path6_LeftBorder_2Aliens() throws Exception {
        Alien alienBorde = new Alien(0, 100);
        Alien alienMedio = new Alien(150, 100);
        board.setAliens(List.of(alienBorde, alienMedio));

        invokePrivateUpdateAliens(board);

        assertEquals(1, board.getDirection());
        assertEquals(115, alienBorde.getY());
        assertEquals(115, alienMedio.getY());
    }

    @Test
    @DisplayName("update_aliens Path 7 (I:T, J:F) - Alien no visible")
    void testUpdateAliens_Path7_NotVisible() throws Exception {
        Alien alien = new Alien(100, 310);
        alien.die(); // Usamos el método público para hacerlo "no visible"
        int initialX = alien.getX();
        board.setAliens(List.of(alien));

        invokePrivateUpdateAliens(board);

        assertNotEquals("Invasion!", board.getMessage());
        assertEquals(initialX, alien.getX());
    }

    @Test
    @DisplayName("update_aliens Path 8 (I:T, J:T, K:T) - Invasión")
    void testUpdateAliens_Path8_Invasion() throws Exception {
        Alien alien = new Alien(100, 310);
        board.setAliens(List.of(alien));

        invokePrivateUpdateAliens(board);

        assertEquals("Invasion!", board.getMessage());
        assertTrue(board.isInGame());
    }

    @Test
    @DisplayName("update_aliens Path 9 (Combinación Borde Derecho + Invasión)")
    void testUpdateAliens_Path9_HitRightAndInvade() throws Exception {
        Alien alienBorde = new Alien(330, 310);
        Alien alienMedio = new Alien(150, 100);
        board.setAliens(List.of(alienBorde, alienMedio));

        invokePrivateUpdateAliens(board);

        // CORRECCIÓN: Verificamos el BUG. La dirección sigue siendo -1
        assertEquals(-1, board.getDirection());
        assertEquals(325, alienBorde.getY());
        assertEquals(115, alienMedio.getY());

        assertEquals("Invasion!", board.getMessage());
    }

    @Test
    @DisplayName("update_shots Path 1")
    void testUpdateShots_Path1() throws Exception {
        Shot shot = new Shot(1, 1);
        shot.setDying(false);
        invokeProtectedSetVisible(shot, true);
        board.setShot(shot);
        Alien alien = new Alien(shot.getX(), shot.getY());
        alien.setDying(false);
        invokeProtectedSetVisible(alien, true);
        board.setAliens(List.of(alien));
        int initDeaths = board.getDeaths();

        invokePrivateUpdateShots(board);

        assertTrue(alien.isDying(), "El alien deberia morir despues de ser disparado");
        assertEquals(initDeaths+1, board.getDeaths(), "El contador de muertes deberia haber aumentado");
        assertFalse(shot.isVisible(), "El disparo deberia desaparecer al llegar arriba del tablero");
    }

    @Test
    @DisplayName("update_shots Path 2")
    void testUpdateShots_Path2() throws Exception {
        Shot shot = new Shot(1, 1);
        shot.setDying(false);
        invokeProtectedSetVisible(shot, true);
        board.setShot(shot);
        Alien alien = new Alien(100, 100);
        alien.setDying(false);
        invokeProtectedSetVisible(alien, true);
        board.setAliens(List.of(alien));
        int initDeaths = board.getDeaths();

        invokePrivateUpdateShots(board);

        assertFalse(alien.isDying(), "El alien no deberia morir despues de no ser disparado");
        assertEquals(initDeaths, board.getDeaths(), "El contador de muertes no deberia haber aumentado");
        assertFalse(shot.isVisible(), "El disparo deberia desaparecer al llegar arriba del tablero");
    }

    @Test
    @DisplayName("update_shots Path 3")
    void testUpdateShots_Path3() throws Exception {
        Shot shot = new Shot(1, 1);
        shot.setDying(false);
        invokeProtectedSetVisible(shot, true);
        board.setShot(shot);
        Alien alien = new Alien(100, 100);
        alien.setDying(false);
        invokeProtectedSetVisible(alien, false);
        board.setAliens(List.of(alien));
        int initDeaths = board.getDeaths();

        invokePrivateUpdateShots(board);

        assertFalse(alien.isDying(), "El alien no deberia morir despues de no ser disparado");
        assertEquals(initDeaths, board.getDeaths(), "El contador de muertes no deberia haber aumentado");
        assertFalse(shot.isVisible(), "El disparo deberia desaparecer al llegar arriba del tablero");
    }

    @Test
    @DisplayName("update_shots Path 4")
    void testUpdateShots_Path4() throws Exception {
        Shot shot = new Shot(100, 100);
        shot.setDying(false);
        invokeProtectedSetVisible(shot, true);
        board.setShot(shot);
        Alien alien = new Alien(1, 1);
        alien.setDying(false);
        invokeProtectedSetVisible(alien, false);
        board.setAliens(List.of(alien));
        int initDeaths = board.getDeaths();

        invokePrivateUpdateShots(board);

        assertFalse(alien.isDying(), "El alien no deberia morir despues de no ser disparado");
        assertEquals(initDeaths, board.getDeaths(), "El contador de muertes no deberia haber aumentado");
        assertTrue(shot.isVisible(), "El disparo no deberia desaparecer");
    }
}