# ProjectSHD: desarrollo y documentación
ProjectSHD es un proyecto de Programación III de la Universidad de Deusto.

## Manejo de la GUI
### **Ventana Login**
Al iniciar la aplicación, se mostrará una pantalla de login. Dado que no habrá usuarios registrados inicialmente, se podrá registrar uno nuevo llevando a cabo 
el formulario de registro.

Los datos de Username y Password son obligatorios, y de ofrecer una dirección de email ésta debe ser válida (estructura 'a@a.aa'). No se puede registrar un usuario 
con Username ya existente.

### **Ventana de gestión de usuarios**
Un usuario administrador, o el usuario 'admin' (con contraseña 'admin') puede acceder al sistema de gestión de usuarios. En esta ventana se pueden realizar varias tareas:
* **Make editable**: si está activado, se pueden modificar manualmente los datos de la tabla.
* **Save changes**: actualiza los datos guardados con los datos actuales de la tabla.
* **Delete user**: borra el usuario seleccionado.
* **Create new user**: crea una instancia de la ventana de registro de usuario.
* **Refresh table**: refresca la tabla.
* **Exit**: cierra la ventana de gestión.

### **Ventana de ajustes iniciales**
Tras iniciar sesión con un usuario _válido_ ('admin' no cuenta como usuario), se muestra la ventana de ajustes iniciales. En ésta podemos ver:
* La puntuación máxima que ha obtenido el usuario
* Barra ajustable de dificultad
* Casillas de música (música activada) y sonido (sonidos de objetos, efectos, etc activados)
* Casilla de tutorial (al activar, el juego se inicia con un tutorial en vez de acceder al menú). Recomendable utilizarlo la primera vez que se juega.
* Botones de salida, confirmación, y gestión
El botón de gestión abre la ventana de gestión de usuarios, pero sólo es accesible si el usuario es administrador.

## Juego
### **Menú principal**
Al entrar por primera vez al juego (si el tutorial no se realiza) nos encontramos con una pantalla de juego con 4 opciones: 
1. **PLAY**: inicia el modo por rondas. Existen 5 rondas, y se deben superar para poder pasar a la siguiente. Para facilitar el testeo y evaluación, la tecla 'F' 
avanza a la siguiente ronda. Al perder, se empieza desde la primera ronda.
2. **INFINITE**: modo infinito. Los enemigos atacan continuamente desde lugares aleatorios, hasta que el jugador pierde.
3. **TESTING**: modo práctica/pacífico, sin enemigos y oro infinito, para probar estrategias de defensa.
4. **QUIT**: sale del juego.

### **Gameplay**
El objetivo del jugador es defender su 'beacon' de los enemigos, eliminandolos para que éstos no destruyan su objetivo. Para esto se dispone de varios objetos 
clasificados en 3 categorías: ataque, defensa y trampas.

### Objetos
En orden:
1. **Ataque**: para atacar a los enemigos.
    - Electricidad, motosierra: realizan daño a los enemigos que tocan, cada uno a un ratio distinto.
    - Ballesta: dispara a los enemigos en su rango. _Se pude tomar control de ella_, haciendo click encima, y disparando con el ratón/tecla espacio.
    - Waves: dispara en las 4 direcciones cardinales proyectiles indestructibles que atraviesan a los enemigos.
2. **Defensa**: para defenderse de los enemigos, y ofrecer apoyo a los objetos de ataque.
    - Mercy: cura a los objetos cercanos, transfiriéndoles su propia vida.
    - Muro, valla: bloquean el avance de los enemigos. El primero es más resistente que el segundo, pero más caro.
    - Martillo: _al ser seleccionado_, se puede elegir un objeto con poca vida para restaurarlo al completo. Se autodestruye tras un uso.
3. **Trampas**: de un sólo uso, con efectos variados.
    - Hielo: congela a los enemigos durante unos segundos.
    - Fuego: prende fuego a los enemigos durante unos segundos.
    - Cuchillo: daña instantáneamente a los enemigos.
    - Confusión: cambia temporalmente el objetivo del enemigo.
    - Teletransporte: transporta al enemigo a un lugar aleatorio.
    
Los objetos tienen un coste, y se colocan en las celdas de la cuadrícula. Si una casilla está ocupada, no se puede colocar el objeto en ella.
Los objetos se pueden filtrar por categoría haciendo click en su icono en lo alto del inventario.

En la parte inferior del inventario podemos ver 2 botones:
- **Pala**: al ser seleccionada, deja eliminar un objeto de su posición, recuperando un 80% de su coste inicial.
- **Menú**: abre el menú de pausa (también accesible con el botón Escape), con las opciones:
    1. RESUME: volver al juego
    2. MENU: volver al menú
    3. QUIT: salir del juego
    
### Enemigos
Existen dos tipos de enemigos: los que simplemente atacan la baliza, y los que tienen un comportamiento especial.

Los comportamientos especiales son, por ejemplo:
- Evitar obstáculos y trampas
- Capacidad de eliminar trampas
- Disparar en vez de atacar cuerpo a cuerpo

La fase final cuenta con un enemigo especial con un comportamiento especial extra.

## Controles (por teclas)
- **E**: salir de modo de control/borrado de objetos
- **O/flecha arriba**: subir volumen de música y sonidos
- **L/flecha abajo**: bajar volumen de música y sonidos
- **Escape**: abrir menú de pausa
- **Espacio**: realizar acción de objeto (disparar, curar)
- **Click del ratón**: realizar acciones pertinentes
- **F**: saltar ronda
