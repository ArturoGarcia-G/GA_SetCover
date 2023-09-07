/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga_setcover;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

/**
 *
 * @author Arturo
 */
public class GA_SetCover {

    /**
     * @param args the command line arguments
     */
    public int[][] matriz;
    public void llenarMatriz(String nombreArchivo)
    {
         try {
            // Abrimos el archivo
            File archivo = new File("rail507.txt");
            Scanner sc = new Scanner(archivo);

            // Leemos el número de filas de la matriz
            int filas = 63009;

            // Creamos la matriz de objetos
            matriz = new int[filas][];

            // Leemos los valores del archivo y los almacenamos en la matriz
            for (int i = 0; i < filas; i++) {
                String[] linea = sc.nextLine().substring(1).split(" ");
                matriz[i] = new int[linea.length];
                for (int j = 0; j < linea.length; j++) {
                    try {
                        matriz[i][j] = Integer.parseInt(linea[j]);
                    } catch (NumberFormatException e) {
                        System.out.println("El archivo contiene un valor no válido en la fila " + i + " columna " + j);
                        matriz[i][j] = -1; // O un valor predeterminado que indique que hay un problema con el valor
                    }
                }
            }
            
            //MOSTRAR LA MATRIZ
            /*
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    System.out.print(matriz[i][j]+" ");
                }
                System.out.println();
            }
            */

            // Cerramos el archivo
            sc.close();

        } catch (FileNotFoundException e) {
            System.out.println("El archivo no fue encontrado");
        }
    }
    public static int numeroAleatorio(int min, int max) {
        Random rand = new Random();        
        int numero = rand.nextInt((max - min) + 1) + min; // Generar primer número aleatorio
        return numero;
    }
    public int calificar(int[] solucion )
    {
        int [] resultados = new int[508];
        int calificacion =0, faltan=0,necesarios=0;
        for(int x=0; x<63009; x++)
        {
            if(solucion[x]==1)
            {
                for (int j = 1; j < matriz[x].length; j++) {
                    resultados[matriz[x][j]]=1;
                }
                necesarios++;
            }

        }
        for(int y=1; y<508; y++)
        {
            if(resultados[y]!=1)
            {
                faltan++;
                
            }
            
        }
        if(faltan>0)
        {
            calificacion=faltan*-1;
        }
        else
        {
            calificacion=63009-(necesarios);
        }
        
        return calificacion;
    }
    public static int[] generarSolucion()
    {
        int[] numeros = new int[63009];
        Random random = new Random();

        // Llenar el arreglo con los números del 1 al 40
        for (int i = 0; i < numeros.length; i++) {
            if (random.nextInt(10) < 7) {
                numeros[i] = 1; // Probabilidad del 70% de que el número sea 1
            } else {
                numeros[i] = 0; // Probabilidad del 30% de que el número sea 0
            }
           
        }
     
        return numeros;

    }
    public static int[] seleccionarPadres()
    {
        int[] numeros = new int[800];

        // Llenar el arreglo con los números del 1 al 40
        for (int i = 0; i < numeros.length; i++) {
            numeros[i] = i;
        }

        // Ordenar aleatoriamente el arreglo
        Random random = new Random();
        for (int i = numeros.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = numeros[i];
            numeros[i] = numeros[j];
            numeros[j] = temp;
        }

        
        
        return numeros;
        
    
    }
    public static int[] crossover(int[] padre1, int[] padre2, int corte)
    {
        int n1, n2, bandera=0, m=0, temp=0;
        int [] hijo;
        hijo = new int[63009];
        
        /*
        n1=numeroAleatorio(1,38);
        do {
            n2=numeroAleatorio(1,38); 
        } while (n2==(n1+1)|| n2==(n1-1)|| n2==n1);
        */
      
        // Copiar una parte aleatoria del padre1 al hijo 
        for(int x=0;x!=(corte+1);x++)
        {
            hijo[x]=padre1[x];
            
        }
        // Copiar la otra parte del padre2 al hijo 
        for(int x=corte+1;x<padre1.length;x++)
        {
            hijo[x]=padre2[x];
            
        }
        
        
        
        return hijo;
    }
    static void swap(int[] arreglo, int[][] padres, int i, int j) {
        
        int [] padretemp;
        padretemp = new int[63009];
        
        int temp = arreglo[i];
        padretemp = padres[i];
        arreglo[i] = arreglo[j];
        padres[i] = padres[j];
        arreglo[j] = temp;
        padres[j] = padretemp;
    }
    static int partition(int[] arreglo,int[][] padres, int low, int high) {
        int pivot = arreglo[high]; // pivote
        int i = (low - 1); // índice del elemento más pequeño

        for (int j = low; j <= high - 1; j++) {
            // Si el elemento actual es menor o igual al pivote
            if (arreglo[j] <= pivot) {
                i++; // Incrementa el índice del elemento más pequeño
                swap(arreglo,padres, i, j);
            }
        }
        swap(arreglo,padres, i + 1, high);
        return (i + 1);
    }
    static void quickSort(int[] arreglo, int[][] padres,int low, int high) {
        if (low < high) {
            /* pi es el índice de partición, arreglo[pi] está en su posición correcta */
            int pi = partition(arreglo,padres, low, high);

            /* Ordena los elementos antes de la partición y después de la partición */
            quickSort(arreglo, padres, low, pi - 1);
            quickSort(arreglo, padres, pi + 1, high);
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        GA_SetCover programa = new GA_SetCover();
        programa.llenarMatriz("datos.txt");
        
        int[][] poblacionInicial;
        poblacionInicial = new int[1000][63009];
        
        int[][] padres;
        padres = new int[800][63009];
        int[][] hijos;
        hijos = new int[800][63009];
        int [] padresSelec;        
        padresSelec = new int[800];
        int [] solucion;        
        solucion = new int[63009];
        int [] costos;
        costos=new int[1000];
        int [] hijo1;
        int [] hijo2;
        hijo1 = new int[63009];
        hijo2 = new int[63009];
        int cali;
        int p1, p2, n1, n2, n3, probabilidad, r;
        
        // Crear una poblacion inicial de 1000 elementos
        
        int costo;
        for(int i=0; i<1000; i++){
            solucion = generarSolucion();
            poblacionInicial[i]= solucion;
            costo=programa.calificar(solucion);
            costos[i]=costo;
        }
        
        //CICLO DE 100 GENERACIONES
        for(int m=0; m<100;m++){
            //System.out.println("generacion "+m+" Mejor: " +(63009-costos[999]));
            
            for(int i=0; i<1000; i++){
                
                costo=programa.calificar(poblacionInicial[i]);
                costos[i]=costo;
            }
            
        
        
        //Seleccion de padres 
        for(int x=0;x<800; x++ )
        {
            
            p1=numeroAleatorio(0,999);
            p2=numeroAleatorio(0,999);
            if(costos[p1]<=costos[p2])
            {
                padres[x]=poblacionInicial[p2];
            }
            else
            {
                padres[x]=poblacionInicial[p1];
            }
                
        }
        
        // Crossover (Creacion de hijos)
        padresSelec=seleccionarPadres(); //Se seleccionan aleatoriamente el orden de padres 
        for(int z=0;z<800;z=z+2)
        {
            probabilidad=numeroAleatorio(1,10);
            if(probabilidad>7)
            {
                n3=numeroAleatorio(0,63007);
                
               
                hijo1=crossover(padres[padresSelec[z]], padres[padresSelec[z+1]], n3);
                hijo2=crossover(padres[padresSelec[z+1]], padres[padresSelec[z]], n3);
                //MUTACION
                int bandera1=0, bandera2=0;
                n1=numeroAleatorio(0,62008);
                
                for(int p=n1; p<(n1+500); p++)
                {
                    hijo1[p]=0;
                    hijo2[p]=0;
                }
                /*
                do{
                    n1=numeroAleatorio(0,63008);
                    if(hijo1[n1]==0)
                    {
                        hijo1[n1]=1;
                        bandera1=1;
                    }
                    
                }while(bandera1!=1);
                
                do{
                    n2=numeroAleatorio(0,63008);
                    if(hijo2[n2]==0)
                    {
                        hijo2[n2]=1;
                        bandera2=1;
                    }
                    
                }while(bandera2!=1);
                */
                
                hijos[z]=hijo1;
                hijos[z+1]=hijo2;
                
            }
            else
            {
                n3=numeroAleatorio(0,63007);
               hijos[z]=crossover(padres[padresSelec[z]], padres[padresSelec[z+1]], n3);
                hijos[z+1]=crossover(padres[padresSelec[z+1]], padres[padresSelec[z]], n3);
            }
            
        }
        //PARA MOSTRAR LAS TABLAS
        /*
        for(int i = 0; i < poblacionInicial.length; i++) {
            for (int j = 0; j < poblacionInicial[i].length; j++) {
                System.out.print(poblacionInicial[i][j] + " ");
            }
            System.out.println(); // Agregar un salto de línea después de cada fila
            System.out.println("-----"+i);
        }
        
        System.out.println("Generacion principal");
        System.out.println(Arrays.toString(costos));
        */
        quickSort(costos, poblacionInicial, 0, 999);
        
        // Sustituyen
        for(int p=0; p<800;p++)
        {
            poblacionInicial[p]=hijos[p];
            
        }
        
        
        
        }
        
  
        
        System.out.println("Resultado:");
        System.out.println(Arrays.toString(poblacionInicial[999])); 
        System.out.println("Mejor arreglo");
        System.out.println(63009-programa.calificar(poblacionInicial[999])); 
        
    }
    
}
