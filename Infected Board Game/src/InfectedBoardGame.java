/*alonso pascual, sergio*/
/*gil becares, alonso*/
import java.io.*;
import java.util.Scanner;
import java.util.Random;

public class InfectedBoardGame {
	final static int NUMNFILAS = 4;
	final static int NUMNCOLUMNAS = 4;
	final static int NUMRANGOS = 5;
	final static int NUMNCONTINUO = NUMNFILAS * NUMNCOLUMNAS * NUMRANGOS;
	final static int NUMNPROGRESIVO = 4;
	final static int NUMNIVELES = NUMNCONTINUO + NUMNPROGRESIVO;
	final static String NOMBREFICHERO = "puntuaciones.txt";

	public static void main(String[] args) {

		int[] puntuaciones = new int[NUMNIVELES];
		int nfilas = 0, rango = 0, nivel = 0, pasos = 0, modo, ncolumnas = 0;
		Scanner in = new Scanner(System.in);
		iniciarPuntuaciones(puntuaciones);
		while (true) {
			System.out.println("MODO DE JUEGO");
			System.out.println("1. Modo continuo");
			System.out.println("2. Modo progresivo");
			System.out.println("3. Iniciar estadisticas");
			System.out.println("0. Salir");
			byte seleccion = in.nextByte();
			while (seleccion < 0 || 3 < seleccion) {
				System.out.println("El numero introducido no corresponde con ninguna opcion intentelo de nuevo:");
			}
			switch (seleccion) {
			case 1:// modo continuo
				modo = 0;
				if (puntuaciones[0] == -1) {
					nfilas = 6;
					ncolumnas = 6;
					rango = 2;
					System.out.println("Comencemos con un tutorial: ");
					System.out.println("Al introducir un numero cambiara el valor de la casilla superior izquierda y de todas aquellas");
					System.out.println("que esten en contacto con ella y tengan el mismo numero");
					System.out.println("El juego termina cuando todas las casillas de la matriz tienen el mismo numero");
					pasos = jugar(generaTableroContinuo(nfilas, ncolumnas, rango), in);
					actualizar(puntuaciones, pasos, modo, nfilas, ncolumnas,rango, nivel);
					finalPartidaContinuo(nfilas, ncolumnas, rango, pasos,puntuaciones, in);
				} else {
					nfilas = selectorNumFilas(in);
					ncolumnas = selectorNumColumnas(in);
					rango = selectorRango(in);
					pasos = jugar(generaTableroContinuo(nfilas, ncolumnas, rango), in);
					actualizar(puntuaciones, pasos, modo, nfilas, ncolumnas,rango, nivel);
					finalPartidaContinuo(nfilas, ncolumnas, rango, pasos,puntuaciones, in);
				}
				break;
			case 2:// modo progresivo
				modo = 1;
				if (puntuaciones[NUMNCONTINUO - 1] == -1) {
					nivel = 1;
					pasos = jugar(generaTableroProgresivo(nivel), in);
					actualizar(puntuaciones, pasos, modo, nfilas, ncolumnas,rango, nivel);
					finalPartidaProgresivo(nivel, pasos, puntuaciones, in);
				} else {
					nivel = selectorNivelProgresivo(puntuaciones, in);
					if (nivel != 0) {
						pasos = jugar(generaTableroProgresivo(nivel), in);
						actualizar(puntuaciones, pasos, modo, nfilas,ncolumnas, rango, nivel);
						finalPartidaProgresivo(nivel, pasos, puntuaciones, in);
					}
				}
				break;
			case 3:// reiniciar estadisticas
				System.out.println("¿Estas seguro de deseas reiniciar todas las estadisticas?");
				System.out.println("               0.NO               1.SI");
				int s = in.nextInt();
				if (s == 1)
					reiniciarEstadisticas(puntuaciones);
				break;
			case 0:// salir
				guardarPuntuaciones(puntuaciones);
				return;
			}
		}
	}

	public static int selectorNumFilas(Scanner in) {
		System.out.println("Seleccione el numero de filas:");
		System.out.println("1. PEQUENO 6");
		System.out.println("2. MEDIANO 9");
		System.out.println("3. GRANDE 12");
		System.out.println("4. GIGANTE 15");
		byte seleccion = in.nextByte();
		while (seleccion < 1 || NUMNFILAS < seleccion) {
			System.out.println("El numero introducido no corresponde con ninguna opcion intentelo de nuevo:");
			seleccion = in.nextByte();
		}
		return (seleccion + 1) * 3;
	}

	public static int selectorNumColumnas(Scanner in) {
		System.out.println("Seleccione el numero de columnas:");
		System.out.println("1. PEQUENO 6");
		System.out.println("2. MEDIANO 9");
		System.out.println("3. GRANDE 12");
		System.out.println("4. GIGANTE 15");
		byte seleccion = in.nextByte();
		while (seleccion < 1 || NUMNCOLUMNAS < seleccion) {
			System.out.println("El numero introducido no corresponde con ninguna opcion intentelo de nuevo:");
			seleccion = in.nextByte();
		}
		return (seleccion + 1) * 3;
	}

	public static int selectorRango(Scanner in) {
		System.out.println("Seleccione un rango entre 2 y " + (NUMRANGOS + 1)+ ": ");
		byte seleccion = in.nextByte();
		while (seleccion < 2 || (NUMRANGOS + 1) < seleccion) {
			System.out.println("Intentalo de nuevo. El rango ha de estar entre 2 y "+ (NUMRANGOS + 1) + ":");
			seleccion = in.nextByte();
		}
		return seleccion;
	}

	public static int[][] generaTableroContinuo(int nfila, int ncolumna,int rango) {
		int[][] tablero = new int[nfila][ncolumna];
		Random rnd = new Random();
		// llenamos el tablero con numeros aleatorios en un rango adecuado
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero[i].length; j++) {
				tablero[i][j] = rnd.nextInt(rango) + 1;
			}
		}
		return tablero;
	}

	public static void finalPartidaContinuo(int nfilas, int ncolumnas,int rango, int pasos, int[] puntuaciones, Scanner in) {
		// Muestra los mensajes y menus correspondientes tras finalizar una partida
		while (true) {
			int nivelfilas = (nfilas / 3) - 2;
			int nivelcol = (ncolumnas / 3) - 2;
			int nivelrango = rango - 2;
			System.out.println("MODO CONTINUO");
			System.out.println("Tamano de tablero : " + nfilas + "x"+ ncolumnas);
			System.out.println("Rango : " + rango);
			System.out.println("Pasos empleados : " + pasos);
			System.out.println("Menor numero de pasos empleados : "+ puntuaciones[(nivelfilas * NUMNCOLUMNAS + nivelcol)* NUMRANGOS + nivelrango]);
			System.out.println();
			System.out.println();
			System.out.println("¿NUEVO TABLERO?");
			System.out.println("0. Volver a menu de MODO DE JUEGO");
			System.out.println("1. Nuevo juego con este rango y tamano");
			System.out.println("2. Cambiar tamano");
			System.out.println("3. Cambiar rango");
			byte seleccion = in.nextByte();
			while (seleccion < 0 || 3 < seleccion) {
				System.out.println("El numero introducido no corresponde con ninguna opcion intentelo de nuevo:");
				seleccion = in.nextByte();
			}
			switch (seleccion) {
			case 0:// volver a modo de juego
				return;
			case 1:// Nuevo juego con este rango y tamano
				pasos = jugar(generaTableroContinuo(nfilas, ncolumnas, rango),in);
				actualizar(puntuaciones, pasos, 0, nfilas, ncolumnas, rango, 0);
				break;
			case 2:// Nuevo juego con otro tamano
				nfilas = selectorNumFilas(in);
				ncolumnas = selectorNumColumnas(in);
				pasos = jugar(generaTableroContinuo(nfilas, ncolumnas, rango),in);
				actualizar(puntuaciones, pasos, 0, nfilas, ncolumnas, rango, 0);
				break;
			case 3:// Nuevo juego con otro rango
				rango = selectorRango(in);
				pasos = jugar(generaTableroContinuo(nfilas, ncolumnas, rango),in);
				actualizar(puntuaciones, pasos, 0, nfilas, ncolumnas, rango, 0);
				break;
			}
		}
	}

	public static int jugar(int tablero[][], Scanner in) {
		// Ejecuta la mecanica del juego
		int pasos, movimiento;
		for (pasos = 0; !compruebaTablero(tablero); pasos++) {
			imprimeTablero(tablero);
			System.out.println("Introduce un numero en la esquina superior izquierda");
			movimiento = in.nextInt();
			while (movimiento < 1 || NUMRANGOS + 1 < movimiento) {
				System.out.println("Inténtalo de nuevo. El rango ha de estar entre 2 y "+ (NUMRANGOS + 1) + ":");
				movimiento = in.nextByte();
			}
			modificaTablero(tablero, movimiento, 0, 0);
		}
		return pasos;
	}

	public static boolean compruebaTablero(int[][] tablero) {
		// comprueba si el tablero esta resuelto
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero[i].length; j++)
				if (tablero[0][0] != tablero[i][j])
					return false;
		}
		return true;
	}

	public static void imprimeTablero(int[][] tablero) {
		// muestra el tablero con un cuadro construido con caracteres unicode
		System.out.print("┌");
		for (int lineasuperior = 0; lineasuperior < tablero[0].length - 1; lineasuperior++)
			System.out.print("─┬");
		System.out.println("─┐");
		for (int i = 0; i < tablero.length; i++) {
			if (i != 0) {
				System.out.print("├");
				for (int lineasintermedias = 0; lineasintermedias < tablero[0].length - 1; lineasintermedias++)
					System.out.print("─┼");
				System.out.println("─┤");
			}
			for (int j = 0; j < tablero[i].length; j++) {
				System.out.print("│" + tablero[i][j]);
				if (j == tablero[i].length - 1)
					System.out.println("│");
			}
		}
		System.out.print("└");
		for (int lineainferior = 0; lineainferior < tablero[0].length - 1; lineainferior++)
			System.out.print("─┴");
		System.out.println("─┘");
	}

	public static void modificaTablero(int[][] tablero, int movimiento, int i,
			int j) {
		// ejecuta la infeccion de numeros en el tablero
		if (tablero[i][j] != movimiento) {
			boolean arriba = false;
			boolean abajo = false;
			boolean derecha = false;
			boolean izquierda = false;
			// determinamos hacia que lugares tiene que desplazarse
			if (i > 0)
				arriba = tablero[i][j] == tablero[i - 1][j];
			if (i < tablero.length - 1)
				abajo = tablero[i][j] == tablero[i + 1][j];
			if (j < tablero[i].length - 1)
				derecha = tablero[i][j] == tablero[i][j + 1];
			if (j > 0)
				izquierda = tablero[i][j] == tablero[i][j - 1];
			tablero[i][j] = movimiento;
			// llamamos de forma recursiva a los lugares adecuados
			if (arriba)
				modificaTablero(tablero, movimiento, i - 1, j);
			if (abajo)
				modificaTablero(tablero, movimiento, i + 1, j);
			if (derecha)
				modificaTablero(tablero, movimiento, i, j + 1);
			if (izquierda)
				modificaTablero(tablero, movimiento, i, j - 1);
		}
	}

	public static int[][] generaTableroProgresivo(int nivel) {
		switch (nivel) {
		case 1:
			int[][] nivel1 = { { 2, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 } };
			return nivel1;
		case 2:
			int[][] nivel2 = { { 3, 2, 1, 1, 1, 1, 1, 1, 1 },
					{ 2, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1 } };
			return nivel2;
		case 3:
			int[][] nivel3 = { { 1, 1, 1, 1, 1, 2, 3, 1, 1 },
					{ 1, 1, 1, 1, 2, 3, 1, 1, 1 },
					{ 1, 1, 1, 2, 3, 1, 1, 1, 1 },
					{ 1, 1, 2, 3, 1, 1, 1, 1, 1 },
					{ 1, 2, 3, 1, 2, 2, 2, 2, 2 },
					{ 2, 3, 1, 1, 2, 3, 3, 3, 2 },
					{ 3, 1, 1, 1, 2, 3, 1, 3, 2 },
					{ 1, 1, 1, 1, 2, 3, 3, 3, 2 },
					{ 1, 1, 1, 1, 2, 2, 2, 2, 2 } };
			return nivel3;
		case 4:
			int[][] nivel4 = { { 1, 2, 2, 2, 2, 2, 2, 2, 2 },
					{ 1, 2, 2, 2, 2, 2, 2, 2, 2 },
					{ 3, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 3, 2, 2, 2, 2, 2, 2, 2, 2 },
					{ 3, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 3, 2, 2, 2, 2, 2, 2, 2, 2 },
					{ 3, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 3, 2, 2, 2, 2, 2, 2, 2, 2 },
					{ 3, 1, 1, 1, 1, 1, 1, 1, 1 } };
			return nivel4;
		default:
			/*
			 * si el nivel introducido en este metodo es incorrecto se devuelve
			 * un tablero vacio
			 */
			int[][] error = {};
			return error;
		}
	}

	public static int selectorNivelProgresivo(int[] puntuaciones, Scanner in) {
		// muestra el menu de niveles
		byte selecion;
		System.out.println("MODO PROGRESIVO");
		System.out.println("0. Volver a menu de MODO DE JUEGO");
		for (int nivel = 1; nivel <= NUMNPROGRESIVO; nivel++) {
			if (puntuaciones[NUMNCONTINUO + (nivel - 1)] == -1)
				System.out.println(nivel + ".");
			else
				System.out.println(nivel + ". Resuelto en "+ puntuaciones[NUMNCONTINUO + (nivel - 1)] + " pasos");
		}
		while (true) {
			selecion = in.nextByte();
			while (selecion < 0 || NUMNPROGRESIVO < selecion) {
				System.out.println("El numero introducido no corresponde con ninguna opcion intentelo de nuevo:");
				selecion = in.nextByte();
			}
			// comprobamos que no esta bloqueado
			if (selecion < 2 || puntuaciones[NUMNCONTINUO + (selecion - 2)] != -1)
				return selecion;
			System.out.println("El numero introducido corresponde con un nivel bloqueado intentelo de nuevo:");
		}
	}

	public static void finalPartidaProgresivo(int nivel, int pasos,int[] puntuaciones, Scanner in) {
		// Muestra los mensajes y menus correspondientes tras finalizar una
		// partida
		while (nivel != 0) {
			System.out.println("MODO PROGRESIVO");
			System.out.println("Nivel : " + nivel);
			System.out.println("Pasos empleados : " + pasos);
			System.out.println("Menor numero de pasos empleados : "+ puntuaciones[NUMNCONTINUO - 1 + nivel]);
			System.out.println();
			System.out.println();
			if (nivel == NUMNPROGRESIVO) {
				nivel = selectorNivelProgresivo(puntuaciones, in);
				if (nivel != 0) {
					pasos = jugar(generaTableroProgresivo(nivel), in);
					actualizar(puntuaciones, pasos, 1, 0, 0, 0, nivel);
				}
			} else {
				System.out.println("¿NUEVO TABLERO?");
				System.out.println("0. Volver a lista de niveles");
				System.out.println("1. Siguiente nivel");
				byte seleccion = in.nextByte();
				while (seleccion < 0 || 1 < seleccion) {
					System.out.println("El numero introducido no corresponde con ninguna opcion intentelo de nuevo:");
					seleccion = in.nextByte();
				}
				switch (seleccion) {
				case 0:// mandamos al usuario a la lista de niveles
					nivel = selectorNivelProgresivo(puntuaciones, in);
					if (nivel != 0) {
						pasos = jugar(generaTableroProgresivo(nivel), in);
						actualizar(puntuaciones, pasos, 1, 0, 0, 0, nivel);
					}
					break;
				case 1:// se ejecuta el siguiente nivel
					nivel++;
					pasos = jugar(generaTableroProgresivo(nivel), in);
					actualizar(puntuaciones, pasos, 1, 0, 0, 0, nivel);
					break;
				}
			}
		}
	}

	public static void actualizar(int[] puntuaciones, int pasos, int modo,int tamano, int ncolumnas, int rango, int nivel) {
		// actualiza la puntuacion del nivel correspondiente en el array de puntuaciones
		int nivelcol = (ncolumnas / 3) - 2;
		int nivelfilas = (tamano / 3) - 2;
		int nivelrango = rango - 2;
		if (modo == 0) {
			if (puntuaciones[(nivelfilas * NUMNCOLUMNAS + nivelcol) * NUMRANGOS+ nivelrango] == -1)
				puntuaciones[(nivelfilas * NUMNCOLUMNAS + nivelcol) * NUMRANGOS+ nivelrango] = pasos;
			else
				puntuaciones[(nivelfilas * NUMNCOLUMNAS + nivelcol) * NUMRANGOS+ nivelrango] = Math.min(puntuaciones[(nivelfilas * NUMNCOLUMNAS + nivelcol)* NUMRANGOS + nivelrango], pasos);
		} else {
			if (puntuaciones[NUMNCONTINUO + nivel - 1] == -1)
				puntuaciones[NUMNCONTINUO + nivel - 1] = pasos;
			else
				puntuaciones[NUMNCONTINUO + nivel - 1] = Math.min(puntuaciones[NUMNCONTINUO + nivel - 1], pasos);
		}
	}

	public static void reiniciarEstadisticas(int[] puntuaciones) {
		// cambia todos los valores del array de puntuaciones por -1
		for (int i = 0; i < puntuaciones.length; i++)
			puntuaciones[i] = -1;
	}

	public static void iniciarPuntuaciones(int[] puntuaciones) {
		// Objetivo: leer las puntuaciones del fichero si existe; poner todas las puntuaciones a -1 si no existe
		// PRE: si el fichero existe es correcto, es decir, tiene al menos NUMNIVELES líneas, cada una con un int

		Scanner f = null;
		try {
			f = new Scanner(new FileReader(NOMBREFICHERO));
			for (int i = 1; i <= puntuaciones.length; i++) {
				puntuaciones[i - 1] = f.nextInt();
			}
			f.close();
		} catch (FileNotFoundException e) {
			reiniciarEstadisticas(puntuaciones);
		}
	}

	public static void guardarPuntuaciones(int[] puntuaciones) {
		// Guarda en el fichero los valores del vector de puntuaciones

		PrintWriter f = null;
		try {
			f = new PrintWriter(new FileWriter(NOMBREFICHERO));
			for (int i = 1; i <= NUMNIVELES; i++) {
				f.println(puntuaciones[i - 1]);
			}
			f.close();
		} catch (IOException e) {
			System.out.println("No se ha podido crear el fichero de puntuaciones");
		}
	}

}
