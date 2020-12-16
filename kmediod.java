package DWM;
import java.util.*;
import java.io.*;
import java.lang.*;


/*
 * k=no of clusters
 * size=size of data set
 * dataset[][]=the dataset
 * c1 is the data object of class Clustering
 */

class Clustering {
    public int i = 0, j = 0, l = 0, k = 0, m = 0, noofmedoids = 2, size = 0;
    int c_id = 1, tempx = 0, tempy = 0, medoid_id_index = 0;
    int totalcost, prevcost, presentcost;
    int x = 0, y = 0, z = 0;
    int medoid_id[] = new int[100];
    int[][] dataset = new int[10][2];
    int[][] medoidArray = null;
    int[][] nonMedoidArray = null;

    public void read(String str) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(str));
        noofmedoids = sc.nextInt();
        while (sc.hasNextInt()) {
            dataset[size][0] = sc.nextInt();
            dataset[size++][1] = sc.nextInt();
        }
        medoidArray = new int[size][3];
        nonMedoidArray = new int[size - k][3];
    }

    // Do:
    public void selectRandomCentroid() {
        int temprand, flag;
        while (true) {
            temprand = (int) Math.floor(100 * Math.random());
            if (temprand >= size) {
                continue;
            } else {
                medoid_id[medoid_id_index++] = temprand;
                break;
            }
        }

        for (i = 1; i < noofmedoids; i++) {
            while (true) {
                flag = 0;
                temprand = (int) Math.floor(100 * Math.random());
                if (temprand < size) {
                    for (j = 0; j < i; j++) {
                        if (temprand == medoid_id[j]) {
                            break;
                        } else {
                            flag++;
                        }
                    }
                }
                if (flag == i) {
                    medoid_id[medoid_id_index++] = temprand;
                    break;
                }
            }
        }
        for (l = 0, j = 0; l < noofmedoids; l++, j++) {
            medoidArray[l][0] = c_id++;
            medoidArray[l][1] = dataset[medoid_id[j]][0];
            medoidArray[l][2] = dataset[medoid_id[j]][1];
        }
    }

    public void cluster() {
        m = 0;
        c_id = 0;
        for (i = 0; i < size; i++) {
            int flag1 = 0;
            int dist = 0;
            int prev = 999999;

            // ID: 
            for (j = 0; j < noofmedoids; j++) {
                if (medoidArray[j][1] == dataset[i][0] && medoidArray[j][2] == dataset[i][1]) {
                    flag1 = 1;
                    break;
                }
            }
            if (flag1 == 1) {
                continue;
            } else {
                for (j = 0; j < noofmedoids; j++) {
                    dist = (Math.abs(medoidArray[j][1] - dataset[i][0]) + Math.abs(medoidArray[j][2] - dataset[i][1]));
                    if (prev > dist) {
                        prev = dist;
                        c_id = j + 1;
                    }
                }
                nonMedoidArray[m][0] = c_id;
                nonMedoidArray[m][1] = dataset[i][0];
                nonMedoidArray[m++][2] = dataset[i][1];
            }

        }
    }

    public int cost() {
        totalcost = 0;
        for (i = 0; i < noofmedoids; i++) {
            for (j = 0; j < (size - noofmedoids); j++) {
                if (medoidArray[i][0] == nonMedoidArray[j][0]) {
                    totalcost += (Math.abs(nonMedoidArray[j][1] - medoidArray[i][1]) + Math.abs(nonMedoidArray[j][2] - medoidArray[i][2]));
                }
            }
        }
        return totalcost;
    }

    public void checkandswap() {
        tempx = tempy = 0;
        for (x = 0; x < noofmedoids; x++) {
            for (y = 0; y < size; y++) {
                for (z = 0; z < size - noofmedoids; z++)
                    if (dataset[y][0] == nonMedoidArray[z][1] && dataset[y][1] == nonMedoidArray[z][2]) {
                        tempx = medoidArray[x][1];
                        tempy = medoidArray[x][2];
                        prevcost = cost();
                        System.out.print("cost:" + prevcost);
                        medoidArray[x][1] = nonMedoidArray[z][1];
                        medoidArray[x][2] = nonMedoidArray[z][2];
                        cluster();
                        presentcost = cost();
                        System.out.print("        New cost:" + presentcost);
                        if (presentcost >= prevcost) {
                            medoidArray[x][1] = tempx;
                            medoidArray[x][2] = tempy;
                            cluster();
                            //System.out.println("Old is better");
                        } else {
                            z = 0;
                            System.out.print(" (cost updated)");
                        }
                        System.out.println();
                    }
            }
        }
        System.out.println();
    }

    public void display() {
        displayMedoidsArray();
        displayNonMedoidsArray();
        displayclusters();
        System.out.println("--------------------------------------------------------");
        System.out.println();
    }

    public void displayMedoidsArray() {
        System.out.println("The medoids are:");
        System.out.println("ID: \tX\t\t Y");
        System.out.println("-------------------");
        for (i = 0; i < noofmedoids; i++) {
            System.out.print(medoidArray[i][0] + "\t\t" + medoidArray[i][1] + "\t\t" + medoidArray[i][2] + "\n");
        }
        System.out.println();

    }

    public void displayNonMedoidsArray() {
        System.out.println("The Non medoids are:");
        System.out.println("ID: \tX\t\t Y");
        System.out.println("-------------------");
        for (i = 0; i < (size - noofmedoids); i++)
            System.out.print(nonMedoidArray[i][0] + "\t\t" + nonMedoidArray[i][1] + "\t\t" + nonMedoidArray[i][2] + "\n");
        System.out.println();
    }

    public void displayclusters() {
        System.out.println("The clusters are:");
        System.out.println("ID: \tX\t\t Y");
        System.out.println("-------------------");
        for (i = 1; i <= noofmedoids; i++) {
            for (j = 0; j < noofmedoids; j++)
                if (i == medoidArray[j][0])
                    System.out.print(medoidArray[j][0] + "\t\t" + medoidArray[j][1] + "\t\t" + medoidArray[j][2] + "\n");
            for (j = 0; j < (size - noofmedoids); j++)
                if (i == nonMedoidArray[j][0])
                    System.out.print(nonMedoidArray[j][0] + "\t\t" + nonMedoidArray[j][1] + "\t\t" + nonMedoidArray[j][2] + "\n");

        }
        System.out.println();
    }
}

public class KMediod {
    public static void main(String[] args) throws FileNotFoundException, IOException {

        int noofmedoids = 2, i = 0, j = 0, size = 0;

        String file = "F:\\dwdmdataset.txt";

        Clustering c1 = new Clustering();
        c1.read(file);
        c1.selectRandomCentroid();
        c1.cluster();
        System.out.println("Init cost:" + c1.cost());
        c1.display();
        c1.checkandswap();
        c1.display();
        System.out.println("After swap cost:" + c1.cost());
    }
}


/*
2 6
3 4
3 8
4 7
6 2
6 4
7 3
7 4
8 5
7 6
 */