						  // Charanpreet Kaur cs610 8357 prp
import static java.lang.System.*;
import java.util.*;
import java.io.*;
import java.text.DecimalFormat;                 
class hits_8357{
  public static void main(String[] args) throws Exception {
	int vertices=0, edges = 0; 
    int count_iter = 0; 
    DecimalFormat a = new DecimalFormat("0.0000000"); 
    int firstval = 0; 
    int iter= 0;   
    double rateoferr = 0.0;
    String graphtext = ""; 
    System.out.println(" \n \t \t \t HITS for  8357 \n");  
    if (args.length != 3){
      System.out.println("Less number of arguments: Format is : hits_8357 interations firstval graphtext'");
      return;
    }
    for (int i=0;i<args.length;i++) {
      iter = Integer.parseInt(args[0]); //parsing the iteration value
      firstval = Integer.parseInt(args[1]); //parsing the initial value
      graphtext = args[2]; // parsing the sample graph text file name
    }
    if (!(firstval >= -2 && firstval <= 1)){
      System.out.println("First values not between -2, -1, 0 or 1");
      return;
    }
    Scanner scanner = new Scanner(new File(graphtext));  // reading sample graph 
    vertices = scanner.nextInt();
    edges = scanner.nextInt();
    double graph[][] = new double[vertices][vertices];// initializing and representing the graph as an adjacency matrix
    for(int i = 0; i < vertices; i++){
      for(int j = 0; j < vertices; j++){
            graph[i][j] = 0.0;
      }
    }
    while(scanner.hasNextInt()){
      graph[scanner.nextInt()][scanner.nextInt()] = 1.0;
    }
    if (iter < 0){  //if number of iteration is negative it sets the rate of error
      rateoferr = Math.pow(10, iter);
    }
	double initial_authority[][] = new double[vertices][1];
	double authority[][]= new double[vertices][1];
	double authority_previous[][] = new double [vertices][1];
    double authority_sum = 0.0;
	double scaler_for_authority = 0;
    double initial_hub[][] = new double[vertices][1];
    double hub[][] = new double[vertices][1];
    double hub_previous [][] = new double [vertices][1];
	double scaler_for_hub = 0;
    double hub_sum = 0.0;
    double transpose_graph[][] = new double[vertices][vertices];
  
    for(int i=0;i<vertices;i++){ //to calculate Transpose of Graph.
      for(int j=0; j<vertices; j++){
        transpose_graph[i][j] = graph[j][i];
      }
    }
    if (vertices < 10){ //using switch case to initialize value of Authority and Hub if Vertices less than 10
      switch(firstval){
        case 0:
          for(int i=0; i<vertices; i++){
            for(int j=0; j<1; j++){
              initial_authority[i][j] = 0.0;
              initial_hub[i][j] = 0.0;
            }
          }
          break;
          case 1:
          for(int i=0; i<vertices; i++){
            for(int j=0; j<1; j++){
              initial_authority[i][j] = 1.0;
              initial_hub[i][j] = 1.0;
            }
          }
          break;
          case -1:
          for(int i=0; i<vertices; i++){
            for(int j=0; j<1; j++){
              initial_authority[i][j] = 1.0/vertices;
              initial_hub[i][j] = 1.0/vertices;
            }
          }
          break;
          case -2:
          for(int i=0; i<vertices; i++){
            for(int j=0; j<1; j++){
              initial_authority[i][j] = 1.0/Math.sqrt(vertices);
              initial_hub[i][j] = 1.0/Math.sqrt(vertices);
            }
          }
          break;
      }
    }
    else {    //else if vertices is greater than 10
      iter = 0;
      firstval = -1;
      rateoferr = 0.00001;
      for(int i=0; i<vertices; i++){
        for(int j=0; j<1; j++){
          initial_authority[i][j] = 1.0/vertices;
          initial_hub[i][j] = 1.0/vertices;
        }
      }
    }
    for (int i=0; i<vertices; i++){     //it will Calculate Base Case of Hub.
      for (int j=0; j<1; j++){
        for (int k=0; k<vertices; k++){
          authority_sum = authority_sum + transpose_graph[i][k]*initial_hub[k][j];
        }
        authority[i][j] = authority_sum;
        authority_sum = 0;
      }
    }
    for (int i=0; i<vertices; i++){
      for (int j=0; j<1; j++){
        for (int k=0; k<vertices; k++){
          hub_sum = hub_sum + graph[i][k]*authority[k][j];     //it will Calculate for the Base Case of Authority
        }
        hub[i][j] = hub_sum;
        hub_sum = 0;
      }
    }
    for (int i=0; i<vertices; i++ ){
      for (int j=0; j<1; j++){
        authority_sum = authority_sum+ (authority[i][j]*authority[i][j]); //it will calculate Authority Sum Sqaure
      }
    }
    for (int i=0; i<vertices; i++ ){
      for (int j=0; j<1; j++){
        hub_sum = hub_sum + (hub[i][j]*hub[i][j]); //it will calculate Hub Sum Square
      }
    }
    System.out.print("Base: " +count_iter + " : ");
    if (vertices > 5){
      System.out.println();
    }
    for(int i=0; i<vertices; i++){
      for(int j=0; j<1; j++){
        System.out.print("A/H [" + i + "] = " + a.format(initial_authority[i][j]) + "/" + a.format(initial_hub[i][j]) + " ");
        if (vertices > 5){
          System.out.println();
        }
      }
    }
    System.out.println();
    if(iter > 0){  // till iterations is 0 it will calculate authority and hub values
      while (iter != 0){
        System.out.print("Iter: " + (count_iter+1) + " :  ");
        if (vertices > 5){
          System.out.println();
        }
        for (int i=0; i<vertices; i++){
          for (int j=0; j<1; j++){
            scaler_for_authority = Math.sqrt(authority_sum);
            scaler_for_hub = Math.sqrt(hub_sum);
            authority[i][j] = authority[i][j]/scaler_for_authority;
            hub[i][j] = hub[i][j]/scaler_for_hub;
            System.out.print(" A/H [" + i + "] = " + a.format(authority[i][j]) + "/" + a.format(hub[i][j]) + " ");
            if (vertices > 5){
              System.out.println();
            }
          }
        }
        authority_sum = 0;
        hub_sum = 0;           //it will Calculate authority and hub value till iteration = 0
        for (int i=0; i<vertices; i++){
          for (int j=0; j<1; j++){
            for (int k=0; k<vertices; k++){
              authority_sum = authority_sum + transpose_graph[i][k]*hub[k][j];
            }
            authority[i][j] = authority_sum;
            authority_sum = 0;
          }
        }
        for (int p=0; p<vertices; p++){
          for (int q=0; q<1; q++){
            for (int r=0; r<vertices; r++){
              hub_sum = hub_sum + graph[p][r]*authority[r][q];
            }
            hub[p][q] = hub_sum;
            hub_sum = 0;
          }
        }
        for (int s=0; s<vertices; s++ ){
          for (int t=0; t<1; t++){
            authority_sum = authority_sum+ (authority[s][t]*authority[s][t]);
          }
        }
        for (int x=0; x<vertices; x++ ){
          for (int y=0; y<1; y++){
            hub_sum = hub_sum + (hub[x][y]*hub[x][y]);
          }
        }
        System.out.println();
        iter = iter - 1;
        count_iter++;
      }
    }
    else{
      do {
        for (int i = 0; i<vertices; i++){
          for (int j = 0; j<1; j++){
            authority_previous[i][j] = authority[i][j];
            hub_previous[i][j] = hub[i][j];
          }
        }
        System.out.print("Iter: " + (count_iter+1) + " : ");
        if (vertices > 5){
          System.out.println();
        }
        for (int i=0; i<vertices; i++){
          for (int j=0; j<1; j++){
            scaler_for_authority = Math.sqrt(authority_sum);
            scaler_for_hub = Math.sqrt(hub_sum);
            authority[i][j] = authority[i][j]/scaler_for_authority;
            hub[i][j] = hub[i][j]/scaler_for_hub;
            System.out.print("A/H [" + i + "] = " + a.format(authority[i][j]) + "/" + a.format(hub[i][j]) + " ");
            if (vertices > 5){
              System.out.println();
            }
          }
        }
        authority_sum = 0; // It will calculate authority and hub value until convergence is achieved
        hub_sum = 0;
        for (int i=0; i<vertices; i++){
          for (int j=0; j<1; j++){
            for (int k=0; k<vertices; k++){
              authority_sum = authority_sum + transpose_graph[i][k]*hub[k][j];
            }
            authority[i][j] = authority_sum;
            authority_sum = 0;
          }
        }
        for (int p=0; p<vertices; p++){
          for (int q=0; q<1; q++){
            for (int r=0; r<vertices; r++){
              hub_sum = hub_sum + graph[p][r]*authority[r][q];
            }
            hub[p][q] = hub_sum;
            hub_sum = 0;
          }
        }
        for (int s=0; s<vertices; s++ ){
          for (int t=0; t<1; t++){
            authority_sum = authority_sum+ (authority[s][t]*authority[s][t]);
          }
        }
        for (int x=0; x<vertices; x++ ){
          for (int y=0; y<1; y++){
            hub_sum = hub_sum + (hub[x][y]*hub[x][y]);
          }
        }
        System.out.println();
        count_iter++;
      } while (false == CheckConverge8357(authority, authority_previous, vertices, rateoferr) || false == CheckConverge8357(hub, hub_previous, vertices,rateoferr));
    }
  }
  public static boolean CheckConverge8357(double initial[][], double previous[][], int n, double rateoferr){ // convergence function
     for(int i = 0 ; i < n; i++){
       for (int j = 0; j < 1; j++){
         if ( Math.abs(initial[i][j] - previous[i][j]) > rateoferr )
           return false;
       }
     }
     return true;
  }
}