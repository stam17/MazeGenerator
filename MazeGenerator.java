import java.lang.Math;
import java.io.PrintWriter;

public class MazeGenerator {

    public void run(int n) {

        // creates all cells
        Cell[][] mazeMap = new Cell[n][n];
        initializeCells(mazeMap);

        // create a list of all internal walls, and links the cells and walls
        Wall[] walls = getWalls(mazeMap);

        createMaze(walls, mazeMap);

        printMaze(mazeMap);

        printToFile(n, mazeMap);

    }

    public void createMaze(Wall[] walls, Cell[][] mazeMap) {
        // FILL IN THIS METHOD
        UnionFind uf = new UnionFind();

        // create headers for every cell in mazeMap and store them in uf
        for (int i = 0; i < mazeMap.length; i++) {
            for (int j= 0; j < mazeMap.length; j++) {
                LLAddOnly toAdd = uf.makeSet(mazeMap[i][j]);
                uf.headers.add(toAdd);
                mazeMap[i][j].head = toAdd;
            }
        }

        // set the entrance and exit walls to be !visible
        mazeMap[0][0].left.visible = false;
        mazeMap[mazeMap.length-1][mazeMap.length-1].right.visible = false;

        // randomly remove walls until all cells are in the same set
        while (uf.headers.size() > 1) {

            //choose a random wall
            int toCheck = (int) (Math.random() * walls.length);

            //check if outer wall
            if (walls[toCheck].first != null && walls[toCheck].second != null) {
                //check if the two adjacent walls are in the same set
                if (uf.find(walls[toCheck].first) != uf.find(walls[toCheck].second)) {
                    uf.union(walls[toCheck].first, walls[toCheck].second);
                    walls[toCheck].visible = false;
                }
            }
        }
    }


    // print out the maze in a specific format
    public void printMaze(Cell[][] maze) {
        for(int i = 0; i < maze.length; i++) {
            // print the up walls for row i
            for(int j = 0; j < maze.length; j++) {
                Wall up = maze[i][j].up;
                if(up != null && up.visible) System.out.print("+--");
                else System.out.print("+  ");
            }
            System.out.println("+");

            // print the left walls and the cells in row i
            for(int j = 0; j < maze.length; j++) {
                Wall left = maze[i][j].left;
                if(left != null && left.visible) System.out.print("|  ");
                else System.out.print("   ");
            }

            //print the last wall on the far right of row i
            Wall lastRight = maze[i][maze.length-1].right;
            if(lastRight != null && lastRight.visible) System.out.println("|");
            else System.out.println(" ");
        }

        // print the last row's down walls
        for(int i = 0; i < maze.length; i++) {
            Wall down = maze[maze.length-1][i].down;
            if(down != null && down.visible) System.out.print("+--");
            else System.out.print("+  ");
        }
        System.out.println("+");
    }

    // create a new Cell for each position of the maze
    public void initializeCells(Cell[][] maze) {
        for(int i = 0; i < maze.length; i++) {
            for(int j = 0; j < maze[0].length; j++) {
                maze[i][j] = new Cell();
            }
        }
    }

    // create all walls and link walls and cells
    public Wall[] getWalls(Cell[][] mazeMap) {

        int n = mazeMap.length;

        Wall[] walls = new Wall[2*n*(n+1)];
        int wallCtr = 0;

        // each "inner" cell adds its right and down walls
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                // add down wall
                if(i < n-1) {
                    walls[wallCtr] = new Wall(mazeMap[i][j], mazeMap[i+1][j]);
                    mazeMap[i][j].down = walls[wallCtr];
                    mazeMap[i+1][j].up = walls[wallCtr];
                    wallCtr++;
                }

                // add right wall
                if(j < n-1) {
                    walls[wallCtr] = new Wall(mazeMap[i][j], mazeMap[i][j+1]);
                    mazeMap[i][j].right = walls[wallCtr];
                    mazeMap[i][j+1].left = walls[wallCtr];
                    wallCtr++;
                }
            }
        }

        // "outer" cells add their outer walls
        for(int i = 0; i < n; i++) {
            // add left walls for the first column
            walls[wallCtr] = new Wall(null, mazeMap[i][0]);
            mazeMap[i][0].left = walls[wallCtr];
            wallCtr++;

            // add up walls for the top row
            walls[wallCtr] = new Wall(null, mazeMap[0][i]);
            mazeMap[0][i].up = walls[wallCtr];
            wallCtr++;

            // add down walls for the bottom row
            walls[wallCtr] = new Wall(null, mazeMap[n-1][i]);
            mazeMap[n-1][i].down = walls[wallCtr];
            wallCtr++;

            // add right walls for the last column
            walls[wallCtr] = new Wall(null, mazeMap[i][n-1]);
            mazeMap[i][n-1].right = walls[wallCtr];
            wallCtr++;
        }


        return walls;
    }

    // added a method to print the maze and its size in a text file (modified from printMaze)
    public void printToFile(int n, Cell[][] maze) {
        try {
            PrintWriter writer = new PrintWriter("maze.txt", "UTF-8");
            writer.println(n);
            for(int i = 0; i < maze.length; i++) {
                // print the up walls for row i
                for(int j = 0; j < maze.length; j++) {
                    Wall up = maze[i][j].up;
                    if(up != null && up.visible) writer.print("+--");
                    else writer.print("+  ");
                }
                writer.println("+");

                // print the left walls and the cells in row i
                for(int j = 0; j < maze.length; j++) {
                    Wall left = maze[i][j].left;
                    if(left != null && left.visible) writer.print("|  ");
                    else writer.print("   ");
                }

                //print the last wall on the far right of row i
                Wall lastRight = maze[i][maze.length-1].right;
                if(lastRight != null && lastRight.visible) writer.println("|");
                else writer.println(" ");
            }

            // print the last row's down walls
            for(int i = 0; i < maze.length; i++) {
                Wall down = maze[maze.length-1][i].down;
                if(down != null && down.visible) writer.print("+--");
                else writer.print("+  ");
            }
            writer.println("+");
            writer.close();
        } catch (Exception ex) {}
    }

    public static void main(String [] args) {
        if(args.length > 0) {
            int n = Integer.parseInt(args[0]);
            new MazeGenerator().run(n);
        }
        else new MazeGenerator().run(5);
    }

}