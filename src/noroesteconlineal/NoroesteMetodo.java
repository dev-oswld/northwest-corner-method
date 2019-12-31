package noroesteconlineal;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NoroesteMetodo {

    private double Tiempo_I, Tiempo_F, Tiempo;
    private int costo_final, fila, columna; //Iterados para X(FILA) Y(COLUMNA)
    private final int oferta[] = {120, 130, 150};
    private int demanda[], matriz_costos[][], matriz_resultado[][]; //Son llenados dinamicamente

    public void Noroeste() {
        Tiempo_I = System.nanoTime(); //Inicio de TIEMPO
        System.out.println("Tengo un tiempo inicial de: " + Tiempo_I);

        try { //Añadimos los valores
            
            URL url_demanda = getClass().getResource("demanda.txt");
            File demanda_txt = new File(url_demanda.getPath());
            
            Scanner archivo_D = new Scanner(demanda_txt);
            List<Integer> Enteros = new ArrayList<>();
            while (archivo_D.hasNext()) {
                if (archivo_D.hasNextInt()) {
                    Enteros.add(archivo_D.nextInt());
                } else {
                    archivo_D.next();
                }
            }
            demanda = Enteros.stream().mapToInt(Integer::intValue).toArray(); //De una lista a un array
            archivo_D.close(); //Cerramos el archivo para una buena practica    
        } catch (FileNotFoundException e) {
            System.out.println("Archivo Demanda => " + e);
        }

        matriz_costos = new int[oferta.length][demanda.length]; //Creamos dinamicamente el espacio
        try { //Leemos el archivo para tomar los valores
            
            URL url_costos = getClass().getResource("costos.txt");
            File costos_txt = new File(url_costos.getPath());
            
            Scanner archivo_C = new Scanner(costos_txt);
            for (int i = 0; i < oferta.length; i++) {
                for (int j = 0; j < demanda.length; j++) {
                    matriz_costos[i][j] = archivo_C.nextInt();
                }
                archivo_C.nextLine(); //Hacemos un salto de linea para seguir
            }
            archivo_C.close(); //Cerramos el archivo para una buena practica

        } catch (FileNotFoundException e) {
            System.out.println("Archivo Costos => " + e);
        } //Termina lectura de los archivo

        //Muestro mi matriz con costos
        System.out.println("\nMatriz inicial con costos sin seleccionar");
        for (int i = 0; i < oferta.length; i++) {
            for (int j = 0; j < demanda.length; j++) {
                System.out.print("[" + matriz_costos[i][j] + "]");
            }
            System.out.println("");
        }

        matriz_resultado = new int[oferta.length][demanda.length];
        System.out.println("");
        System.out.println("Matriz resultado vacia con valor cero");
        for (int i = 0; i < oferta.length; i++) {
            for (int j = 0; j < demanda.length; j++) {
                matriz_resultado[i][j] = 0; //Relleno mi matriz con ceros
            }
        }

        //Muestro mi matriz con ceros
        for (int i = 0; i < oferta.length; i++) {
            for (int j = 0; j < demanda.length; j++) {
                System.out.print("[" + matriz_resultado[i][j] + "]");
            }
            System.out.println("");
        }

        System.out.println("");
        do {
            //Aqui tenemos la heuristica (ALGORITMO)
            // Another example => https://computersolution2016.blogspot.com/2014/01/north-west-corner-method.html
            if (demanda[columna] != 0) {
                if (demanda[columna] < oferta[fila]) {
                    matriz_resultado[fila][columna] = demanda[columna];
                    oferta[fila] -= demanda[columna];
                    demanda[columna] = 0;

                    if (columna == demanda.length - 1) {
                        columna = 0;
                    } else {
                        columna++;
                    }
                } else {
                    if (demanda[columna] == oferta[fila]) {
                        matriz_resultado[fila][columna] = oferta[fila];
                        demanda[columna] = 0;
                        oferta[fila] = 0;

                        if (columna == demanda.length - 1) {
                            columna = 0;
                        } else {
                            columna++;
                        }
                        if (fila == oferta.length - 1) {
                            fila = 0;
                        } else {
                            fila++;
                        }
                    } else {
                        matriz_resultado[fila][columna] = oferta[fila];
                        demanda[columna] -= oferta[fila];
                        if (fila == oferta.length - 1) {
                            fila = 0;
                        } else {
                            fila++;
                        }
                    }
                }
            } else {
                if (columna == demanda.length - 1) {
                    columna = 0;
                } else {
                    columna++;
                }
            }
        } while (demanda[demanda.length - 1] != 0);

        //Aqui mostramos los resultados
        System.out.println("Matriz resultante con los valores optimos");
        for (fila = 0; fila < oferta.length; fila++) {
            for (columna = 0; columna < demanda.length; columna++) {
                System.out.print(" " + matriz_resultado[fila][columna] + " ");
            }
            System.out.println("");
        }

        System.out.println("");
        for (fila = 0; fila < oferta.length; fila++) {
            for (columna = 0; columna < demanda.length; columna++) {
                costo_final += matriz_costos[fila][columna] * matriz_resultado[fila][columna];
            }
        }

        //Aqui mostramos el resultado de los costos
        System.out.println("Resultado final es " + costo_final + " como solución basica");
        Tiempo_F = System.nanoTime(); //Final de TIEMPO
        System.out.println("\nTengo un tiempo final de: " + Tiempo_F);
        Tiempo = (Tiempo_F - Tiempo_I) * 1.0e-9; //Multiplicamos por esa cantidad para hacerlo en segundos.
        System.out.println("Use un tiempo total de: " + Tiempo + " segundos");
    }
}