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
	
	//checks that the child isn't the same as the father or a black block and that its within the bounds of the board
	public static boolean check_ok(Matrix m, int position) { //0=right 1=down 2=left 3=up
		//updating the empty position
		if(position == 0) {
			m.emptyj++;
		}
		if(position == 1) {
			m.emptyi++;
		}
		if(position == 2) {
			m.emptyj--;
		}
		if(position == 3) {
			m.emptyi--;
		}
		if(m.father.father != null && m.emptyi == m.father.father.emptyi && m.emptyj == m.father.father.emptyj) { //checking difference from father of father
			return false;
		}
		if (m.emptyi < 0 || m.emptyi >= m.state.length || m.emptyj < 0 || m.emptyj >= m.state[0].length) { //check for out of bounds
			return false;
		}
		if(colors.get(m.state[m.emptyi][m.emptyj]).equals("Black")) { //black block, can't move
			return false;
		}
		return true;
	}
	
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
		if(mat.father != null) {
			return mat.father.state[mat.emptyi][mat.emptyj] + direction(mat) + "-" + path(mat.father);
		}else {
			return "";
		}
	}
	
	//returns the path from start to finish
	public static String path2(Matrix mat) {
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
		
	//BFS, uses hashtable for the openlist
	public static String BFS(Matrix start, boolean openClosed) {
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
			if(openClosed == true) { //print to screen the matrixes in the openlist
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
	
	public static String DFBnB(Matrix mat, boolean openClosed) {
		Hashtable<Matrix, String> table = new Hashtable<Matrix, String>();
		Stack<Matrix> stack = new Stack<Matrix>();
		String result = null;
		int t = Integer.MAX_VALUE; //upper bound
		ansNum = 1;
		stack.add(mat);
		table.put(mat, ""); //inserted to hashtable with no "out" as value at first
		while(!stack.isEmpty()) {
			if(openClosed == true) { //print the openlist condition
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
	
	public static String A(Matrix start, boolean openClosed){
		Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
		Hashtable<Matrix, Matrix> openlist = new Hashtable<Matrix, Matrix>();
		PriorityQueue<Matrix> q = new PriorityQueue<Matrix>(new Comparator<Matrix>() {public int compare(Matrix m1, Matrix m2) {return f(m1)-f(m2);}});
		q.add(start); 
		openlist.put(start, start);
		int i = 0;
		int num = 1;
		while(!q.isEmpty()) {
			if(openClosed == true) { //printing the open list condition
				System.out.println(openlist.values().toString());
			}
			Matrix temp = q.poll();
			openlist.remove(temp);
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
		
	public static String DFID(Matrix start, boolean openClosed) {
		int depth = 1; //starting depth, less than that won't do anything at the first iteration
		Hashtable<Matrix, Matrix> openlist = new Hashtable<Matrix, Matrix>();
		openlist.put(start, start); //at first the given state is the only one in the open list
		if(openClosed == true) { //first iteration open list condition
			System.out.println(openlist.values().toString());
		}
		ansNum = 1;
		while(depth != Integer.MAX_VALUE) { 
			Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
			openlist = new Hashtable<Matrix, Matrix>();
			String result = Limited_DFS(start, depth, table, openlist);
			if(openClosed == true) { //print open list condition
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
	
	
	public static String IDA(Matrix start, boolean openClosed) {
		Hashtable<Matrix, String> table = new Hashtable<Matrix, String>();
		Stack<Matrix> s = new Stack<Matrix>();
		int n = 1; //generated states counter
		int t = h(start);
		while(t != Integer.MAX_VALUE) { //upper bound for cost
			int minF = Integer.MAX_VALUE;
			s.add(start);
			table.put(start, "");
			while(!s.isEmpty()) {
				if(openClosed == true) { //open list print condition
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
		boolean openClosed = false;
		long startTime = System.nanoTime();
		int sizeI = 0;
		int sizeJ = 0;
		String[] lineOfMat = null;
		File file = new File(path); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		String st; 
		int line = 1;
		int countmatrix=0;
		while ((st = br.readLine()) != null) {
			if(line==1) {
				algo=st;
			}
			else if(line == 2) {
				if( st.equalsIgnoreCase("no time")) {
					time=false;
					}else {
						time=true;
					}
			}
			else if(line == 3) {
				if(st.equalsIgnoreCase("no open")) {
					openClosed=false;
					}else {
						openClosed=true;
					}
			}
			else if(line == 4) {
				int x=st.indexOf('x');
				sizeI=Integer.parseInt(st.substring(0, x));
				sizeJ=Integer.parseInt(st.substring(x+1));
				lineOfMat=new String[sizeJ];
			} else if(line == 5) {
				st=st.substring(6); //getting numbers beyond Black:
				if(st.length() != 0 && !st.equalsIgnoreCase(" ") && !st.equalsIgnoreCase(null)) {
				   	st=st.substring(1);	
				   	String[] temp=st.split(",");				    	
				   	for (int i = 0; i < temp.length; i++) {
			 		colors.put(Integer.parseInt(temp[i]), "Black");   			
				   	}
				}
		   } else if(line == 6) {
			   st=st.substring(4); //getting numbers beyond Red:
			   if(st.length() != 0 && !st.equalsIgnoreCase(" ") && !st.equalsIgnoreCase(null)) {
				   st=st.substring(1);	    	
				   String[] temp=st.split(",");				    	
				   //Red=new int[temp.length];
				   for (int i = 0; i < temp.length; i++) {
					   colors.put(Integer.parseInt(temp[i]), "Red");  
				   }
			   }
		   }else { //reached the state of the board
			   lineOfMat[countmatrix]=st;
			   countmatrix++;
		   }
			line++;
		}
		int[][] temp=new int[sizeI][sizeJ]; //filling the matrix according to the one given in the input
		for (int i = 0; i < temp.length; i++) {
			String[] row = lineOfMat[i].split(",");
			for (int j = 0; j < temp[0].length; j++) {
				if(row[j].equalsIgnoreCase("_")) {
					temp[i][j]=-1;
				} else{
					temp[i][j]=Integer.parseInt(row[j]);
				}
			}
		}
		for (int i = 0; i < temp.length; i++) { //adding remaining green blocks to the table of colors
			for (int j = 0; j < temp[0].length; j++) {
				if(!colors.containsKey(temp[i][j])) {
					colors.put(temp[i][j], "Green");
				}
			}
		}
		start = new Matrix(temp);
		String ans = null;
		if(algo.equals("BFS")) {
			ans = BFS(start, openClosed);
		} else if(algo.equals("A*")) {
			ans = A(start, openClosed);
		} else if(algo.equals("DFID")) {
			ans = DFID(start, openClosed);
		} else if(algo.equals("IDA*")) {
			ans = IDA(start, openClosed);
		} else if(algo.equals("DFBnB")) {
			ans = DFBnB(start, openClosed);
		}
		long endTime = System.nanoTime();
		DecimalFormat format = new DecimalFormat("#.###");
		String T = format.format((double)(endTime - startTime) / 1000000000);
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
		//Game("C:\\Users\\Gil-PC\\Desktop\\solvingProblemInputs\\input.txt");
		Game("input.txt");
	}
}
