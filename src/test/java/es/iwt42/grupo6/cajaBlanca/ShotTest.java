package es.iwt42.grupo6.cajaBlanca;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Shot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class ShotTest {


    @Test
    @DisplayName("CB_INITSHOT_01: El constructor calcula la posición inicial (x, y)")
    void testInitShotViaConstructor() {

        int inputX = 100;
        int inputY = 100;


        Shot shot = new Shot(inputX, inputY);


        final int H_SPACE = 6;
        final int V_SPACE = 1;

        int expectedX = inputX + H_SPACE; // 100 + 6 = 106
        int expectedY = inputY - V_SPACE; // 100 - 1 = 99

        assertEquals(expectedX, shot.getX(), "La X inicial del disparo no se calculó correctamente (x + H_SPACE)");
        assertEquals(expectedY, shot.getY(), "La Y inicial del disparo no se calculó correctamente (y - V_SPACE)");
        assertNotNull(shot.getImage(), "La imagen del disparo no se cargó (debería ser 'shot.png')");
    }
}