import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.TimeUnit;



public class Ex1 {
	
	static Hashtable<Integer, String> colors = new Hashtable<Integer, String>();
	static int ansNum;
	static int ansCost;
	
	public static class Matrix {
		
		int cost;
		String path;
		int [][] state;
		Matrix father;
		int emptyi;
		int emptyj;
		//String marked; //no need for this
		
		public String toString() {
			String ans = "\n";
			for (int i = 0; i < state.length; i++) {
				for (int j = 0; j < state[0].length; j++) {
					if(j == state[0].length -1) {
						if(this.state[i][j] == -1) { //end on line
							ans += "_";
						}else {
							ans += this.state[i][j];
						}
					}else {
						if(this.state[i][j] == -1) {
							ans += "_,";
						}else {
							ans += this.state[i][j] + ",";
						}
					}
				}
				ans += "\n";
			}
			return ans;
		}

		@Override
	    public boolean equals(Object o) { 
			if (o == this) { //check for same object
	            return true; 
	        } 
			if (!(o instanceof Matrix)) { //check if the object is a matrix
	            return false; 
	        } 
			Matrix temp = (Matrix) o;
			for (int i = 0; i < this.state.length; i++) { //checking each block of the matrixes for equality
				for (int j = 0; j < this.state[0].length; j++) {
					if(temp.state[i][j] != this.state[i][j]) {
						return false;
					}
				}
			}
			return true;
		} 
		
		//this matrix() is for the input.txt (the first) and for input3.txt
		public Matrix() { //no need for this one anymore
			this.state = new int[3][4];
			this.state[0][0] = 1;
			this.state[0][1] = 2;
			this.state[0][2] = 3;
			this.state[0][3] = 4;
			this.state[1][0] = 5;
			this.state[1][1] = 6;
			this.state[1][2] = 11;
			this.state[1][3] = 7;
			this.state[2][0] = 9;
			this.state[2][1] = 10;
			this.state[2][2] = 8;
			this.state[2][3] = -1;
			this.emptyi = 2;
			this.emptyj = 3;
			this.father = null;
			for (int i = 0; i < 10; i++) {
			colors.put(i+1, "Green");
			}
			colors.put(11, "Red");
			//this.marked = ""; //for IDA*
		}
		
		public Matrix(int[][] mat) {
			this.state = new int [mat.length][mat[0].length];
			for (int i = 0; i < mat.length; i++) {
				for (int j = 0; j < mat[0].length; j++) {
					this.state[i][j] = mat[i][j];
					if(mat[i][j] == -1) {
						this.emptyi = i;
						this.emptyj = j;
					}
				}
			}
			this.father = null;		
		}
			
		
		
			
		
		//this matrix() is for input2.txt
		/*public Matrix() {
			this.state = new int[3][4];
			this.state[0][0] = 1;
			this.state[0][1] = 2;
			this.state[0][2] = 3;
			this.state[0][3] = 4;
			this.state[1][0] = 5;
			this.state[1][1] = 6;
			this.state[1][2] = 11;
			this.state[1][3] = 7;
			this.state[2][0] = 9;
			this.state[2][1] = 10;
			this.state[2][2] = 8;
			this.state[2][3] = -1;
			this.emptyi = 2;
			this.emptyj = 3;
			this.father = null;
			colors.put(1, "Red");
			colors.put(2, "Red");
			colors.put(3, "Green");
			colors.put(4, "Black");
			colors.put(5, "Green");
			colors.put(6, "Green");
			colors.put(7, "Black");
			colors.put(8, "Black");
			colors.put(9, "Green");
			colors.put(10, "Green");
			colors.put(11, "Black");
		}*/
		
		//copy constructor for children, father is the given matrix.
		public Matrix(Matrix m) {
			this.cost = m.cost;
			this.emptyi = m.emptyi;
			this.emptyj = m.emptyj;
			this.father = m;
			//this.marked = m.marked; //for IDA*
			this.state = new int[m.state.length][m.state[0].length];
			for (int i = 0; i < m.state.length; i++) {
				for (int j = 0; j < m.state[0].length; j++) {
					this.state[i][j] = m.state[i][j];
				}
			}
		}
	}
	
	/*public static boolean check_ok(Matrix m, int position) { //0=up 1=down 2=left 3=right
		//updating the empty position
		if(position == 0) {
			m.emptyi--;
		}
		if(position == 1) {
			m.emptyi++;
		}
		if(position == 2) {
			m.emptyj--;
		}
		if(position == 3) {
			m.emptyj++;
		}
		if(m.father.father != null && m.emptyi == m.father.father.emptyi && m.emptyj == m.father.father.emptyj) { //checking difference from father of father
			return false;
		}
		if (m.emptyi < 0 || m.emptyi >= m.state.length || m.emptyj < 0 || m.emptyj >= m.state[0].length) { //check for out of bounds
			/*if(m.emptyi < 0) {
				System.out.println("too high");
			}
			if(m.emptyi >= m.mat.length) {
				System.out.println("too low");
			}
			if(m.emptyj < 0) {
				System.out.println("too left");
			}
			if(m.emptyj >= m.mat[0].length) {
				System.out.println("too right");
			}
			//System.out.println("check_ok2: out of bounds");
			return false;
		}
		if(colors.get(m.state[m.emptyi][m.emptyj]).equals("Black")) { //black block, can't move
			return false;
		}
		return true;
	}*/
	
	//checks that the child isn't the same as the father or a black block and that its within the bounds of the board
	public static boolean check_ok(Matrix m, int position) { //0=right 1=down 2=left 3=up
		//updating the empty position
		if(position == 0) {
			//m.emptyi--;
			m.emptyj++;
		}
		if(position == 1) {
			//m.emptyi++;
			m.emptyi++;
		}
		if(position == 2) {
			//m.emptyj--;
			m.emptyj--;
		}
		if(position == 3) {
			//m.emptyj++;
			m.emptyi--;
		}
		if(m.father.father != null && m.emptyi == m.father.father.emptyi && m.emptyj == m.father.father.emptyj) { //checking difference from father of father
			return false;
		}
		if (m.emptyi < 0 || m.emptyi >= m.state.length || m.emptyj < 0 || m.emptyj >= m.state[0].length) { //check for out of bounds
			/*if(m.emptyi < 0) {
				System.out.println("too high");
			}
			if(m.emptyi >= m.mat.length) {
				System.out.println("too low");
			}
			if(m.emptyj < 0) {
				System.out.println("too left");
			}
			if(m.emptyj >= m.mat[0].length) {
				System.out.println("too right");
			}*/
			//System.out.println("check_ok2: out of bounds");
			return false;
		}
		if(colors.get(m.state[m.emptyi][m.emptyj]).equals("Black")) { //black block, can't move
			return false;
		}
		return true;
	}

	/*public static Matrix getMove(Matrix mat, int n) { //old
		if(n == 0) {
			return (empty_up(new Matrix(mat)));
		}
		else if(n == 1) {
			//temp2 = new Matrix(empty_down(temp));
			return (empty_down(new Matrix(mat)));
		}
		else if(n == 2) {
			//temp2 = new Matrix(empty_left(temp));
			return (empty_left(new Matrix(mat)));
		}
		else	{
			//temp2 = new Matrix(empty_right(temp));
			return (empty_right(new Matrix(mat)));
		}
	}*/
	/*public static Matrix getMove(Matrix mat, int n) { //newer old
		if(n == 0) {
			return (empty_left(new Matrix(mat)));
		}
		else if(n == 1) {
			//temp2 = new Matrix(empty_down(temp));
			return (empty_up(new Matrix(mat)));
		}
		else if(n == 2) {
			//temp2 = new Matrix(empty_left(temp));
			return (empty_right(new Matrix(mat)));
		}
		else	{
			//temp2 = new Matrix(empty_right(temp));
			return (empty_down(new Matrix(mat)));
		}
	}*/
	
	public static Matrix getMove(Matrix mat, int n) { //creating the child matrix, each n represent a different move.
		if(n == 0) {
			return (empty_right(new Matrix(mat)));
		}
		else if(n == 1) {
			return (empty_down(new Matrix(mat)));
		}
		else if(n == 2) {
			return (empty_left(new Matrix(mat)));
		}
		else{
			return (empty_up(new Matrix(mat)));
		}
	}
	
	
	/*public static Matrix empty_up(Matrix m) {
		Matrix ans = new Matrix(m);
		int temp = ans.state[ans.emptyi - 1][ans.emptyj];
		if(colors.get(temp).equals("Green")) {
			ans.cost += 1;
		}
		if(colors.get(temp).equals("Red")) {
			ans.cost += 30;
		}
		ans.state[ans.emptyi - 1][ans.emptyj] = -1;
		ans.state[ans.emptyi][ans.emptyj] = temp;
		ans.emptyi--;
		return ans;
	}*/
	
	//this function move the empty space up and updates the cost of the matrix according to the color of the block moved
	public static Matrix empty_up(Matrix mat) {
		int temp = mat.state[mat.emptyi - 1][mat.emptyj];
		if(colors.get(temp).equals("Green")) {
			mat.cost += 1;
		}
		if(colors.get(temp).equals("Red")) {
			mat.cost += 30;
		}
		mat.state[mat.emptyi - 1][mat.emptyj] = -1;
		mat.state[mat.emptyi][mat.emptyj] = temp;
		mat.emptyi--;
		return mat;
	}
	
	//this function move the empty space down and updates the cost of the matrix according to the color of the block moved
	public static Matrix empty_down(Matrix mat) {
		int temp = mat.state[mat.emptyi + 1][mat.emptyj];
		if(colors.get(temp).equals("Green")) {
			mat.cost += 1;
		}
		if(colors.get(temp).equals("Red")) {
			mat.cost += 30;
		}
		mat.state[mat.emptyi + 1][mat.emptyj] = -1;
		mat.state[mat.emptyi][mat.emptyj] = temp;
		mat.emptyi++;
		return mat;
	}
	
	//this function moves the empty space right and updates the cost of the matrix according to the color of the block moved
	public static Matrix empty_right(Matrix mat) {
		int temp = mat.state[mat.emptyi][mat.emptyj + 1];
		if(colors.get(temp).equals("Green")) {
			mat.cost += 1;
		}
		if(colors.get(temp).equals("Red")) {
			mat.cost += 30;
		}
		mat.state[mat.emptyi][mat.emptyj + 1] = -1;
		mat.state[mat.emptyi][mat.emptyj] = temp;
		mat.emptyj++;
		return mat;
	}
	
	//this function moves the empty space left and updates the cost of the matrix according to the color of the block moved
	public static Matrix empty_left(Matrix mat) {
		int temp = mat.state[mat.emptyi][mat.emptyj - 1];
		if(colors.get(temp).equals("Green")) {
			mat.cost += 1;
		}
		if(colors.get(temp).equals("Red")) {
			mat.cost += 30;
		}
		mat.state[mat.emptyi][mat.emptyj - 1] = -1;
		mat.state[mat.emptyi][mat.emptyj] = temp;
		mat.emptyj--;
		return mat;
	}
	
	//this goal is for starting with given matrix.
	/*public static boolean goal(Matrix mat) {
		int num = 1;
		for (int i = 0; i < mat.state.length; i++) {
			for (int j = 0; j < mat.state[0].length; j++) {
				if(i == mat.state.length - 1 && j == mat.state[0].length - 1) { //reached the end, this is the right answer
					return true;
				}else if(mat.state[i][j] == num) {
					num++;
				}else {
					return false;
				}
			}
		}
		return true;
	}*/
	
	//sent with the answer board state and a given state of the board and check's for equality, if equality is present than the algorithm found the answer.
	public static boolean goal(Matrix mat, Matrix finished) {
		for (int i = 0; i < mat.state.length; i++) {
			for (int j = 0; j < mat.state[0].length; j++) {
				if(mat.state[i][j] != finished.state[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	//this path is returning the height of the ans
	/*public static int path(Matrix mat) {
		//print the path
		System.out.println("-------------------");
		for (int i = 0; i < mat.state.length; i++) {
			for (int j = 0; j < mat.state[0].length; j++) {
				System.out.print(mat.state[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("-------------------");
		if(mat.father == null) {
			return 0;
		}else {
		 return 1 + path(mat.father);
		}
	}*/
	
	//used in path (reverse), returns the letter of the direction changed between father and son states
	public static String direction(Matrix mat) { //for BFS
		if(mat.father.emptyi == mat.emptyi + 1) {
			return "U";
		}
		else if(mat.father.emptyi == mat.emptyi - 1) {
			return "D";
		}
		else if(mat.father.emptyj == mat.emptyj + 1) {
			return "L";
		}
		return "R";
	}
	
	//used in path2, returns the letter of the direction changed between father and son states
	public static String direction2(Matrix mat) {
		if(mat.father.emptyi == mat.emptyi + 1) {
			return "D";
		}
		else if(mat.father.emptyi == mat.emptyi - 1) {
			return "U";
		}
		else if(mat.father.emptyj == mat.emptyj + 1) {
			return "R";
		}
		return "L";
	}
	
	//returns the path from start to finish
	public static String path(Matrix mat) {
		/*System.out.println("-------------------");
		for (int i = 0; i < mat.state.length; i++) {
			for (int j = 0; j < mat.state[0].length; j++) {
				System.out.print(mat.state[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("-------------------");*/
		if(mat.father != null) {
			return mat.father.state[mat.emptyi][mat.emptyj] + direction(mat) + "-" + path(mat.father);
		}else {
			return "";
		}
	}
	
	//returns the path from start to finish
	public static String path2(Matrix mat) {
		/*System.out.println("-------------------");
		for (int i = 0; i < mat.state.length; i++) {
			for (int j = 0; j < mat.state[0].length; j++) {
				System.out.print(mat.state[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("-------------------");*/
		if(mat.father != null) {
			return path2(mat.father) + "-" + mat.father.state[mat.emptyi][mat.emptyj] + direction2(mat);
		}else {
			return "";
		}
	}
	
	//returns the answer matrix given the dimensions of the board
	public static Matrix finished_mat(Matrix mat) {
		Matrix ans = new Matrix(mat);
		ans.father = null;
		int num = 1;
		for (int i = 0; i < ans.state.length; i++) {
			for (int j = 0; j < ans.state[0].length; j++) {
				if(i == ans.state.length - 1 && j == ans.state[0].length - 1) {
					ans.state[i][j] = -1;
				}else {
					ans.state[i][j] = num++;
				}
			}
		}
		return ans;		
	}
	
	/*public static String BFS(Matrix start, boolean openclosed) {
		Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
		Queue<Matrix> q = new LinkedList<Matrix>();
		q.add(start);
		int i = 0;
		int num = 1;
		while(!q.isEmpty()) {
			if(openclosed == true) {
				System.out.println(q.toString()); //print the matrix in the queue
			}
			Matrix temp = q.poll();
			table.put(i++, temp);
			for (int j = 0; j < 4; j++) {
				Matrix temp2 = null;
				if(check_ok(new Matrix(temp), j)){
					temp2 = getMove(temp, j);
					num++;
					if(!q.contains(temp2) && !table.contains(temp2)) {
						if(goal(temp2, finished_mat(start))) {
							System.out.println("cost: " + temp2.cost);
							System.out.println("num: " + num);
							String ans = path2(temp2);
							ansNum = num;
							ansCost = temp2.cost;
							return ans.substring(1);
						}else {
							q.add(temp2);
						}
					}
				}
			}
		}
		ansNum = num;
		return "no path";
	}*/
	
	/*public static String BFS(Matrix start, boolean openclosed) {
		Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
		Hashtable<Integer, Matrix> q = new Hashtable<Integer, Matrix>();
		int hash = 0;
		int n = 0;
		q.put(hash++, start);
		int i = 0;
		int num = 1;
		while(!q.isEmpty()) {
			//if(openclosed == true) {
				System.out.println(q.toString()); //print the matrix in the queue
		//	}
			Matrix temp = q.get(n);
			q.remove(n++);
			table.put(i++, temp);
			for (int j = 0; j < 4; j++) {
				Matrix temp2 = null;
				if(check_ok(new Matrix(temp), j)){
					temp2 = getMove(temp, j);
					num++;
					if(!q.contains(temp2) && !table.contains(temp2)) {
						if(goal(temp2, finished_mat(start))) {
							System.out.println("cost: " + temp2.cost);
							System.out.println("num: " + num);
							String ans = path2(temp2);
							ansNum = num;
							ansCost = temp2.cost;
							return ans.substring(1);
						}else {
							q.put(hash++,temp2);
						}
					}
				}
			}
		}
		ansNum = num;
		return "no path";
	}*/
	
	
	//BFS, uses hashtable for the openlist
	public static String BFS(Matrix start, boolean openclosed) {
		Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
		Hashtable<Integer, Matrix> openlist = new Hashtable<Integer, Matrix>();
		Queue<Matrix> q = new LinkedList<Matrix>();
		q.add(start);
		int put = 0; //put counter for open list
		int remove = 0; //remove counter for open list
		openlist.put(put++, start);
		int i = 0; //put counter for table
		int num = 1; //counter for created states of the board
		while(!q.isEmpty()) {
			if(openclosed == true) { //print to screen the matrixes in the openlist
				//System.out.println("openlist:\n" + openlist.toString() +"\nsize openlist: " + openlist.size()); //print the matrix in the queue
				//System.out.println("q:\n" + q.toString() +"\nsize q: " +  q.size());
			//System.out.println("openlist size: " + openlist.size());
			//System.out.println("q size: " + q.size());
			System.out.println(openlist.values().toString());
			}
			openlist.remove(remove++);
			Matrix temp = q.poll();
			table.put(i++, temp);
			for (int j = 0; j < 4; j++) { //each child option, in reality only 3 children for each state can exist, in edge of board empty slot we get less than 3.
				Matrix temp2 = null;
				if(check_ok(new Matrix(temp), j)){ //if true the child is valid, not a duplicate of the father and not out of bounds.
					temp2 = getMove(temp, j); //creating the child
					num++; //counting the states created along the way of the algorithm
					if(!q.contains(temp2) && !table.contains(temp2)) { //deeper search for duplicates
						if(goal(temp2, finished_mat(start))) {
							String ans = path2(temp2);
							ansNum = num;
							ansCost = temp2.cost;
							return ans.substring(1);
						}else {
							q.add(temp2);
							openlist.put(put++, temp2); //adding generated child to the open list
						}
					}
				}
			}
		}
		ansNum = num;
		return "no path";
	}
	
	public static String DFBnB(Matrix mat, boolean openclosed) {
		Hashtable<Matrix, String> table = new Hashtable<Matrix, String>();
		Stack<Matrix> stack = new Stack<Matrix>();
		String result = null;
		int t = Integer.MAX_VALUE; //upper bound
		ansNum = 1;
		stack.add(mat);
		table.put(mat, ""); //inserted to hashtable with no "out" as value at first
		while(!stack.isEmpty()) {
			if(openclosed == true) { //print the openlist condition
				System.out.println(stack.toString());
			}
			Matrix temp = stack.pop();
			if(table.get(temp).equals("out")) { //if already checked this state remove it
				table.remove(temp);
			}else {
				table.remove(temp); //else check this state,remove it and insert it as checked to the table
				table.put(temp, "out");
				ArrayList<Matrix> array = new ArrayList<Matrix>();
				for (int j = 0; j < 4; j++) {
					if(check_ok(new Matrix(temp), j)){
						ansNum++; //generated nodes counter
						array.add(getMove(temp, j));
						array.sort(new Comparator<Matrix>() {public int compare(Matrix m1, Matrix m2) {return f(m1)-f(m2);}});	
						for (int i = 0; i < array.size(); i++) { //checking if any of the nodes goes above the upper bound, if so remove all of those that do.
							int eCost = f(array.get(i));
							if(eCost >= t) {
								for (int k = i; k < array.size(); k++) {
									array.remove(k);
								}
							}
							else if(table.contains(array.get(i)) && table.get(array.get(i)).equals("out")) { //child already created and checked
								array.remove(i);
							}
							else if(table.contains(array.get(i)) && !table.get(array.get(i)).equals("out")) { //child already created but not checked yet
								for (Matrix key : table.keySet()) { //if f(child) is less than f(child) that already exist's -> update
									if(key.equals(array.get(i)) && f(key) <= eCost) {							
										array.remove(i);
									}else {
										stack.remove(key);
										table.remove(key);
									}
								}
							}
							else if(goal(array.get(i), finished_mat(mat))) {
								t = f(array.get(i)); //bound, there isn't a need for this actually
								result = path2(array.get(i));
								ansCost = array.get(i).cost;
								return result.substring(1);
							}
						}
						for (int k = array.size() - 1; k >= 0; k--) { //insert new children to be checked
								stack.add(array.get(k));
								table.put(array.get(k), "");
						}
					}
				}
			}
		}
		return "no path";
	}
	
	//old h function
	/*public static int h(Matrix mat) {
		int Edistance = 0;
		Matrix temp = finished_mat(mat);
		for (int i = 0; i < mat.state.length; i++) {
			for (int j = 0; j < mat.state[0].length; j++) {
				if(temp.state[i][j] != mat.state[i][j]) {
					//Edistance += Math.abs(arg0)
					int x1 = 0;
					int y1 = 0;
					int num = mat.state[i][j];//number that should be here
					for (int k = 0; k < mat.state.length; k++) {//find the number position in the given mat
						for (int k2 = 0; k2 < mat.state[0].length; k2++) {
							//if(mat.state[k][k2] == temp.state[i][j] && mat.state[k][k2] == num) {
							if(mat.state[k][k2] == num) {
								x1 = k + 1;
								y1 = k2 + 1;
								//x1 = k;
								//y1 = k2;
								System.out.println("x1: " + x1 + "\ny1: " + y1);
							}
						}
					}
					//Edistance += (Math.abs(x1-(i+1)) + Math.abs(y1-(j+1)));
					//System.out.println(num);
					if(num == -1) {
						continue;
					}
					if(colors.get(num).equals("Red")) {
						Edistance += (Math.abs(x1-(i+1)) + Math.abs(y1-(j+1))) * 30;
					}else {
						Edistance += (Math.abs(x1-(i+1)) + Math.abs(y1-(j+1)));
					}
					System.out.println("current state of Edistance: " + Edistance);
				}
			}
		}
		return Edistance;
	}*/
	
	//this function estimates the cost between this given state to the goal state, based on Manhattan distance.
	//in short, this function sums the distance from each misplaced block to its proper position while taking the color of it into the calculation.
	public static int h(Matrix mat) {
		int Edistance = 0;
		Matrix temp = finished_mat(mat);
		for (int i = 0; i < mat.state.length; i++) {
			for (int j = 0; j < mat.state[0].length; j++) {
				if(mat.state[i][j] == -1) {
					continue;
				}
				if(mat.state[i][j] != temp.state[i][j]) {
					int x1 = 0;
					int y1 = 0;
					for (int i2 = 0; i2 < mat.state.length; i2++) {
						for (int j2 = 0; j2 < mat.state[0].length; j2++) {
							if(mat.state[i2][j2] == temp.state[i][j]) {
								x1 = i2+1;
								y1 = j2+1;
							}
						}
					}
					if(colors.get(mat.state[i][j]).equals("Red")) {
						Edistance += (Math.abs(x1-(i+1)) + Math.abs(y1-(j+1))) * 30;
					}else {
						Edistance += (Math.abs(x1-(i+1)) + Math.abs(y1-(j+1)));
					}
				}
			}
		}
		return Edistance;
	}
	
	//f(n) = g(n) + h(n) //cost until now + estimated cost
	public static int f(Matrix mat) {
		return mat.cost + h(mat);		
	}
	
	
	//A* algorithm
	//extremely similar to BFS, only difference is that we choose the best child to continue, the one with the lowest estimated cost to goal. 
	//need to check if start from start or from the end
	////////// PROBLEM THAT CAN OCCUR AND I NEED TO CHECK IS IF THERE IS A NEED NOT TO DUMP ALL THE NODES!!!!~~~~ (choose the highest floor if duplicate)
	/*public static String A(Matrix start, boolean openclosed){
		Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
		Queue<Matrix> q = new LinkedList<Matrix>();
		q.add(start); //this one has to start with the starting q
		//q.add(start);
		int i = 0;
		int num = 1;
		while(!q.isEmpty()) {
			Matrix temp = q.poll();
			int min = f(temp);
			while(!q.isEmpty()) { //find the best option to continue
				int fSibling = f(q.peek());
				if(fSibling < min) {
					temp = q.poll();
					min = fSibling;
				}else {
					q.poll(); //to counter the problem maybe i should insert to the end after i pop it
				}
			} //continue like BFS
			table.put(i++, temp);
			for (int j = 0; j < 4; j++) { //first create all children!~!~!~!~!~!~!~!~!~
				Matrix temp2 = null;
				if(check_ok(new Matrix(temp), j)){
					temp2 = getMove(temp, j);
					num++; //the correct son is entered before the other is created, i need to create them both so i choose the correct with the right amount of nodes
					if(!q.contains(temp2) && !table.contains(temp2)) {
						//if(goal(temp2)) {
						if(goal(temp2, finished_mat(start))) {
							j++;
							while(j < 4) { //counting brothers (finishes before creating them)
								if(check_ok(new Matrix(temp), j)) {
									num++;
								}
								j++;
							}
							System.out.println("cost: " + temp2.cost);
							System.out.println("num: " + num);
							String ans = path2(temp2);
							ansNum = num;
							//ansNum = table.size();
							ansCost = temp2.cost;
							return ans.substring(1); //removes the final "-"
							//return ans;
						}else {
							q.add(temp2);
							//num++;
						}
					}
				}
			}
		}
		System.out.println("num: " + num);
		ansNum = num;
		return "no path";
	}*/
	
	public static String A(Matrix start, boolean openclosed){
		Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
		Hashtable<Matrix, Matrix> openlist = new Hashtable<Matrix, Matrix>();
		PriorityQueue<Matrix> q = new PriorityQueue<Matrix>(new Comparator<Matrix>() {public int compare(Matrix m1, Matrix m2) {return f(m1)-f(m2);}});
		q.add(start); 
		openlist.put(start, start);
		int i = 0;
		int num = 1;
		while(!q.isEmpty()) {
			if(openclosed == true) { //printing the open list condition
				System.out.println(openlist.values().toString());
				//System.out.println("openlist size: " + openlist.size());
				//System.out.println("q size: " + q.size());
			}
			Matrix temp = q.poll();
			openlist.remove(temp);
			//int min = f(temp);
			/*while(!q.isEmpty()) { //find the best option to continue
				int fSibling = f(q.peek());
				if(fSibling < min) {
					temp = q.poll();
					min = fSibling;
				}else {
					q.poll(); //to counter the problem maybe i should insert to the end after i pop it
				}
			} //continue like BFS*/
			table.put(i++, temp);
			for (int j = 0; j < 4; j++) {
				Matrix temp2 = null;
				if(check_ok(new Matrix(temp), j)){
					temp2 = getMove(temp, j);
					num++; //generated nodes counter
					if(!q.contains(temp2) && !table.contains(temp2)) {
						if(goal(temp2, finished_mat(start))) {
							j++;
							while(j < 4) { //counting remaining brothers
								if(check_ok(new Matrix(temp), j)) {
									num++;
								}
								j++;
							}
							String ans = path2(temp2);
							ansNum = num;
							ansCost = temp2.cost;
							return ans.substring(1);
						}else {
							q.add(temp2);
							openlist.put(temp2, temp2);
						}
					}
				}
			}
		}
		System.out.println("num: " + num);
		ansNum = num;
		return "no path";
	}
	
	/*public static String A(Matrix start, boolean openclosed){
		Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
		Hashtable<Integer, Matrix> q = new Hashtable<Integer, Matrix>();
		int hash = 0;
		int n = 0;
		q.put(hash++, start); //this one has to start with the starting q
		//q.add(start);
		int i = 0;
		int num = 1;
		while(!q.isEmpty()) {
			Matrix temp = q.values().iterator().next();
			q.remove(q.keySet().iterator().next());
			int min = f(temp);
			/*while(!q.isEmpty()) { //find the best option to continue
				int fSibling = f(q.peek());
				if(fSibling < min) {
					temp = q.poll();
					min = fSibling;
				}else {
					q.poll(); //to counter the problem maybe i should insert to the end after i pop it
				}
			} //continue like BFS
			for (Map.Entry<Integer, Matrix> entry : q.entrySet()) {
				int fSibling = f(entry.getValue());
				if(fSibling < min) {
					temp = entry.getValue();
					q.remove(entry.getKey());
					min = fSibling;
				}else {
					q.remove(entry.getKey());
				}
			}
			q.forEach((key, value) ->{
				int fSibling = f(value);
				if(fSibling < min) {
					temp = value;
					q.remove(key);
					min = fSibling;
				}else {
					q.remove(key);
				}
			});
			table.put(i++, temp);
			for (int j = 0; j < 4; j++) { //first create all children!~!~!~!~!~!~!~!~!~
				Matrix temp2 = null;
				if(check_ok(new Matrix(temp), j)){
					temp2 = getMove(temp, j);
					num++; //the correct son is entered before the other is created, i need to create them both so i choose the correct with the right amount of nodes
					if(!q.contains(temp2) && !table.contains(temp2)) {
						//if(goal(temp2)) {
						if(goal(temp2, finished_mat(start))) {
							j++;
							while(j < 4) { //counting brothers (finishes before creating them)
								if(check_ok(new Matrix(temp), j)) {
									num++;
								}
								j++;
							}
							System.out.println("cost: " + temp2.cost);
							System.out.println("num: " + num);
							String ans = path2(temp2);
							ansNum = num;
							//ansNum = table.size();
							ansCost = temp2.cost;
							return ans.substring(1); //removes the final "-"
							//return ans;
						}else {
							q.put(hash++,temp2);
							//num++;
						}
					}
				}
			}
		}
		System.out.println("num: " + num);
		ansNum = num;
		return "no path";
	}*/
	
	
	
	/*public static String A(Matrix start, boolean openclosed){
		Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
		//Queue<Matrix> q = new LinkedList<Matrix>();
		Hashtable<Integer, Matrix> q = new Hashtable<Integer, Matrix>();
		int hash = 0;
		q.put(hash++, start); //this one has to start with the starting q
		//q.add(start);
		int n = 0;
		int i = 0;
		int num = 1;
		while(!q.isEmpty()) {
			Matrix temp = q.get(n);
			int min = f(temp);
			while(!q.isEmpty()) { //find the best option to continue
				int fSibling = f(q.peek());
				if(fSibling < min) {
					temp = q.poll();
					min = fSibling;
				}else {
					q.poll(); //to counter the problem maybe i should insert to the end after i pop it
				}
			} //continue like BFS
			table.put(i++, temp);
			for (int j = 0; j < 4; j++) { //first create all children!~!~!~!~!~!~!~!~!~
				Matrix temp2 = null;
				if(check_ok(new Matrix(temp), j)){
					temp2 = getMove(temp, j);
					num++; //the correct son is entered before the other is created, i need to create them both so i choose the correct with the right amount of nodes
					if(!q.contains(temp2) && !table.contains(temp2)) {
						//if(goal(temp2)) {
						if(goal(temp2, finished_mat(start))) {
							j++;
							while(j < 4) { //counting brothers (finishes before creating them)
								if(check_ok(new Matrix(temp), j)) {
									num++;
								}
								j++;
							}
							System.out.println("cost: " + temp2.cost);
							System.out.println("num: " + num);
							String ans = path2(temp2);
							ansNum = num;
							//ansNum = table.size();
							ansCost = temp2.cost;
							return ans.substring(1); //removes the final "-"
							//return ans;
						}else {
							q.add(temp2);
							//num++;
						}
					}
				}
			}
		}
		System.out.println("num: " + num);
		ansNum = num;
		return "no path";
	}*/
	
	
	public static String DFID(Matrix start, boolean openclosed) {
		int depth = 1; //starting depth, less than that won't do anything at the first iteration
		Hashtable<Matrix, Matrix> openlist = new Hashtable<Matrix, Matrix>();
		openlist.put(start, start); //at first the given state is the only one in the open list
		if(openclosed == true) { //first iteration open list condition
			System.out.println(openlist.values().toString());
		}
		ansNum = 1;
		while(depth != Integer.MAX_VALUE) { 
			Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
			openlist = new Hashtable<Matrix, Matrix>();
			String result = Limited_DFS(start, depth, table, openlist);
			if(openclosed == true) { //print open list condition
				System.out.println(openlist.values().toString());
			}
			if(result != null && !result.equals("cutoff") && !result.equals("fail")) { //a path to goal was returned
				return result.substring(1);
			}
			else if(result=="fail") {
				return "no path";
			}
			depth++; //go deeper in next iteration
		}
		return "no path";
	}
	
	public static String Limited_DFS(Matrix n, int limit, Hashtable<Integer, Matrix> table, Hashtable<Matrix, Matrix> openlist) {
		if(goal(n, finished_mat(n))) { //stop condition, goal reached
			ansCost = n.cost;
			return path2(n);
		}else if(limit == 0) { //stop condition, depth has reached its end in this iteration
			openlist.put(n, n);
			return "cutoff";
		}else {
			table.put(limit, n); 
			boolean isCutoff = false;
			for (int j = 0; j < 4; j++) {
				Matrix temp2 = null;
				if(check_ok(new Matrix(n), j)){
					temp2 = getMove(n, j);
					if(!table.contains(temp2)) {
						ansNum++;
						String result = Limited_DFS(temp2, limit - 1, table, openlist); //keep going with the next level
						if(result == "cutoff") {
							isCutoff = true;
						}else if(result != "fail") {
							return result;
						}
					}
				}
			}
			table.remove(limit);
			if(isCutoff == true) {
				return "cutoff";
			}else {
				return "fail";
			}
		}
	}
	
	
	public static String IDA(Matrix start, boolean openclosed) {
		Hashtable<Matrix, String> table = new Hashtable<Matrix, String>();
		Stack<Matrix> s = new Stack<Matrix>();
		int n = 1; //generated states counter
		int t = h(start);
		while(t != Integer.MAX_VALUE) { //upper bound for cost
			int minF = Integer.MAX_VALUE;
			s.add(start);
			table.put(start, "");
			while(!s.isEmpty()) {
				if(openclosed == true) { //open list print condition
					s.toString();
				}
				Matrix temp = s.pop();
				if(table.get(temp).equals("out")) {
					table.remove(temp);
				}else {
					table.remove(temp);
					table.put(temp, "out");
					s.add(temp);
					for (int j = 0; j < 4; j++) {
						Matrix temp2 = null;
						if(check_ok(new Matrix(temp), j)){
							temp2 = getMove(temp, j);
							if(f(temp2) > t) { //upper bound
								minF = Math.min(minF, f(temp2));
								n++;
								continue;
							}
							if(table.contains(temp2) && table.get(temp2).equals("out")) { //already checked
								n++;
								continue;
							}
							if(table.contains(temp2) && !table.get(temp2).equals("out")) { //already exist but not checked
								n++;
								Matrix temp3 = null; 
								for (Matrix key : table.keySet()) {
									if(key.equals(temp2)) {
										temp3 = key;
									}
								}
								if(f(temp3) > f(temp2)) { //if f is less then update the state cost by removing the previous (added later in the algorithm)
									s.remove(temp3);
									table.remove(temp3);
								}else {
									continue;
								}
							}
							if(goal(temp2, finished_mat(start))) {
								n++;
								System.out.println("cost: " + temp2.cost);
								System.out.println("num: " + n);
								String ans = path2(temp2);
								ansNum = n;
								ansCost = temp2.cost;
								return ans.substring(1);
							}
							s.add(temp2);
							table.put(temp2, "");
						}
					}
				}
			}
			t = minF;
		}
		ansNum = n;
		return "no path";
	}
	
	public static void Game(String path) throws NumberFormatException, IOException{
		Matrix start;
		String algo = null;
		boolean time = false;
		boolean openclosed = false;
		long startTime3 = System.nanoTime();
		int sizei=0, sizej = 0;
		int[] Black = null;
		int[] Red= null;
		String[] m = null;
		File file = new File(path); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		 String st; 
		 int counter=1;
		 int countmatrix=0;
		  while ((st = br.readLine()) != null) {
		    if(counter==1) {algo=st;}
		    else if(counter==2) {if( st.equalsIgnoreCase("no time")) {time=false;}else {time=true;}}
		    else if(counter==3 ) {if( st.equalsIgnoreCase("no open")) {openclosed=false;}else {openclosed=true;}}
		    else if(counter==4) {
		    	int x=st.indexOf('x');
		    	sizei=Integer.parseInt(st.substring(0, x));
			   	sizej=Integer.parseInt(st.substring(x+1));
			   	m=new String[sizej];
		   } else if(counter==5) {
			   	st=st.substring(6);
			   	if(st.length()==0||st.equalsIgnoreCase(" ") || st.equalsIgnoreCase(null)) {
			   		Black =new int[1];
			   		Black[0]=0;
			   	} else{
			    	st=st.substring(1);	
			    	String[] temp=st.split(",");				    	
			    	Black=new int[temp.length];
			    	for (int i = 0; i < temp.length; i++) {
			    		Black[i]=Integer.parseInt(temp[i]);
			    		colors.put(Integer.parseInt(temp[i]), "Black");   			
					}
			   	}
			   }
			   else if(counter==6) {
				   st=st.substring(4);
				   if(st.length()==0||st.equalsIgnoreCase(" ") || st.equalsIgnoreCase(null)) {
		    			Red =new int[1];
		    			Red[0]=0; 
				   } else{
					   st=st.substring(1);	    	
					   String[] temp=st.split(",");				    	
					   Red=new int[temp.length];
					   for (int i = 0; i < temp.length; i++) {
						   Red[i]=Integer.parseInt(temp[i]);
						   colors.put(Integer.parseInt(temp[i]), "Red");  
					   }
				   }
			   }else {
			    	m[countmatrix]=st;
			    	countmatrix++;
			   }
		    		counter++;
		  }
			  int[][] temp=new int[sizei][sizej];
			  for (int i = 0; i < temp.length; i++) {
				String[] s=m[i].split(",");
				for (int j = 0; j < temp[0].length; j++) {
					if(s[j].equalsIgnoreCase("_")) {temp[i][j]=-1;}
					else{temp[i][j]=Integer.parseInt(s[j]);}
				}
			  }
			  for (int i = 0; i < temp.length; i++) {
				  for (int j = 0; j < temp[0].length; j++) {
					  if(!colors.containsKey(temp[i][j])) {
						  colors.put(temp[i][j], "Green");
					  }
				  }
			  }
			  start = new Matrix(temp);
			  String ans = null;
			  if(algo.equals("BFS")) {
				  ans = BFS(start, openclosed);
			  }
			  else if(algo.equals("A*")) {
				  ans = A(start, openclosed);
			  }
			  else if(algo.equals("DFID")) {
				  ans = DFID(start, openclosed);
			  }
			  else if(algo.equals("IDA*")) {
				  ans = IDA(start, openclosed);
			  }
			  else if(algo.equals("DFBnB")) {
				  ans = DFBnB(start, openclosed);
			  }
			  long stopTime3 = System.nanoTime();
			  DecimalFormat format = new DecimalFormat("#.###");
			  String T = format.format((double)(stopTime3 - startTime3) / 1000000000);
		      output(ans, time, T, path);
		}
	
	public static void output(String path, boolean ifTime, String time, String place) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("output.txt");
		writer.println(path);
		System.out.println(path);
		writer.println("Num: " + ansNum);
		System.out.println("Num: " + ansNum);
		if(!path.equals("no path")) {
			writer.println("Cost: " + ansCost);
			System.out.println("Cost: " + ansCost);
		}	
		if(ifTime) {
			writer.println(time); //change to actual time
		}
		writer.close();
	}
	
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		Game("C:\\Users\\Gil-PC\\Desktop\\inputoutput\\input4.txt");
		//Game("input.txt");
	}
}
