package es.iwt42.grupo6;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Shot;
import main.Commons;

class ShotTest implements Commons {

    private static final int H_SPACE = 6;
    private static final int V_SPACE = 1;

    @ParameterizedTest(name = "Caso {index} [{0},{1}]: {2}")
    @CsvSource({
            // Casos de prueba para el eje X
            "4, 150,   'SI1: X menor que el límite izquierdo (4 < 5)'",
            "5, 150,   'SI2: X en el valor mínimo válido'",
            "6, 150,   'SI3: X justo por encima del mínimo'",
            "321, 150, 'SI4: X justo por debajo del máximo (322-1)'",
            "322, 150, 'SI5: X en el valor máximo permitido (322+6=328)'",
            "323, 150, 'SI6: X fuera del rango derecho (posición final 323+6=329 es inválida)'",

            // Casos de prueba para el eje Y
            "150, 0,   'SI7: Y fuera del rango superior (posición final 0-1=-1 es inválida)'",
            "150, 1,   'SI8: Y en el valor mínimo permitido (posición final 1-1=0)'",
            "150, 2,   'SI9: Y ligeramente por encima del mínimo'",
            "150, 289, 'SI11: Y justo por debajo del máximo'",
            "150, 290, 'SI12: Y en el valor máximo permitido (GROUND)'",
            "150, 291, 'SI13: Y fuera del rango inferior (291 > 290)'",

            // Caso de prueba para valores normales
            "150, 150, 'SI10: Valores normales dentro del tablero'"
    })
    void testInitShotRobustBoundary(int x, int y) {

        // Posición final del disparo tras aplicar el desplazamiento
        int adjustedX = x + H_SPACE;
        int adjustedY = y - V_SPACE;

        // Límites
        int minX = BORDER_LEFT;                 // 5
        int maxX = BOARD_WIDTH - BORDER_RIGHT;  // 328
        int minY = 0;
        int maxY = GROUND;                      // 290


        // La entrada es válida si las coordenadas iniciales están dentro de los límites del tablero.
        boolean isInputValid = (x >= minX && x <= maxX) && (y >= minY && y <= maxY);

        // La posición final es válida si las coordenadas ajustadas también están dentro de los límites.
        boolean isFinalPositionValid = (adjustedX >= minX && adjustedX <= maxX) && (adjustedY >= minY && adjustedY <= maxY);


        if (isInputValid && isFinalPositionValid) {
            // Si tanto la entrada como la posición final son válidas, el objeto Shot debe crearse correctamente.
            Shot shot = new Shot(x, y);
            assertEquals(adjustedX, shot.getX(), "La coordenada X ajustada es incorrecta.");
            assertEquals(adjustedY, shot.getY(), "La coordenada Y ajustada es incorrecta.");
        } else {
            // Si la entrada o la posición final están fuera de los límites, se lanza una excepción.
            assertThrows(IllegalArgumentException.class, () -> {
                new Shot(x, y);
            }, "Se esperaba una excepción para la entrada (" + x + "," + y + ") que resulta en una posición inválida.");
        }
    }
}