package es.iwt42.grupo6.cajaBlanca;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Player;



import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {


    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("CB_INITPLAYER_01: El constructor inicializa la posición y la imagen")
    void testInitPlayerViaConstructor() {

        Player player = new Player();

        final int START_X = 270;
        final int START_Y = 280;

        assertEquals(START_X, player.getX(), "La X inicial del jugador no es la esperada (270)");
        assertEquals(START_Y, player.getY(), "La Y inicial del jugador no es la esperada (280)");
        assertNotNull(player.getImage(), "La imagen del jugador no se cargó (debería ser 'player.png')");
    }












}
