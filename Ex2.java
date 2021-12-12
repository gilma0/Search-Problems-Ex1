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
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.TimeUnit;



public class Ex2 {
	
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
		String marked;

		@Override
	    public boolean equals(Object o) { 
			if (o == this) { 
	            return true; 
	        } 
			if (!(o instanceof Matrix)) { 
	            return false; 
	        } 
			Matrix temp = (Matrix) o;
			for (int i = 0; i < this.state.length; i++) {
				for (int j = 0; j < this.state[0].length; j++) {
					if(temp.state[i][j] != this.state[i][j]) {
						return false;
					}
				}
			}
			return true;
		} 
		
		//this matrix() is for the input.txt (the first) and for input3.txt
		public Matrix() {
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
			this.marked = ""; //for IDA*
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
		
		
		public Matrix(Matrix m) {
			this.cost = m.cost;
			this.emptyi = m.emptyi;
			this.emptyj = m.emptyj;
			this.father = m;
			this.marked = m.marked; //for IDA*
			this.state = new int[m.state.length][m.state[0].length];
			for (int i = 0; i < m.state.length; i++) {
				for (int j = 0; j < m.state[0].length; j++) {
					this.state[i][j] = m.state[i][j];
				}
			}
		}
	}
	
	public static boolean check_ok(Matrix m, int position) { //0=up 1=down 2=left 3=right
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
			return false;
		}
		if(colors.get(m.state[m.emptyi][m.emptyj]).equals("Black")) { //black block, can't move
			return false;
		}
		return true;
	}
	public static Matrix getMove(Matrix mat, int n) {
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
	}
	
	//this function move the empty space up
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
	
	//this function move the empty space down
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
	
	//this function moves the empty space right
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
	
	//this function moves the empty space left
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
	
	public static String direction2(Matrix mat) { //for IDA* AND A*
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
	
	//maybe needs to do with the hashtable table of BFS, this path is for BFS
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
	public static String path2(Matrix mat) {
		if(mat.father != null) {
			return path2(mat.father) + "-" + mat.father.state[mat.emptyi][mat.emptyj] + direction2(mat);
		}else {
			return "";
		}
	}
	
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
	
	//starts from answer puzzle to lower the number of nodes created
	public static String BFS(Matrix start) {
		Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
		Queue<Matrix> q = new LinkedList<Matrix>();
		q.add(finished_mat(start));
		//q.add(start);
		int i = 0;
		int num = 0;
		while(!q.isEmpty()) {
			Matrix temp = q.poll();
			table.put(i++, temp);
			for (int j = 0; j < 4; j++) {
				Matrix temp2 = null;
				if(check_ok(new Matrix(temp), j)){
					temp2 = getMove(temp, j);
					
					if(!q.contains(temp2) && !table.contains(temp2)) {
						num++;
						if(goal(temp2, start)) {
							System.out.println("cost: " + temp2.cost);
							System.out.println("num: " + num);
							String ans = path(temp2);
							ansNum = num;
							ansCost = temp2.cost;
							return ans.substring(0, ans.length()-1); //removes the final "-"
						}else {
							q.add(temp2);
						}
					}
				}
			}
		}
		System.out.println("num: " + num);
		return "no path";
	}
	
	public static String DFBnB(Matrix mat) {
		Hashtable<Matrix, String> table = new Hashtable<Matrix, String>();
		Stack<Matrix> stack = new Stack<Matrix>();
		String result = null;
		int t = Integer.MAX_VALUE;
		//int num =0;
		stack.add(mat);
		table.put(mat, "");
		while(!stack.isEmpty()) {
			Matrix temp = stack.pop();
			if(table.get(temp).equals("out")) {
				table.remove(temp);
			}else {
				table.remove(temp);
				table.put(temp, "out");
				ArrayList<Matrix> array = new ArrayList<Matrix>();
				for (int j = 0; j < 4; j++) {
					if(check_ok(new Matrix(temp), j)){
						array.add(getMove(temp, j));
						array.sort(new Comparator<Matrix>() {public int compare(Matrix m1, Matrix m2) {return f(m1)-f(m2);}});	
						for (int i = 0; i < array.size(); i++) {////check this for
							int eCost = f(array.get(i));
							if(eCost >= t) {
								for (int k = i; k < array.size(); k++) {
									array.remove(k);
								}
							}
							else if(table.contains(array.get(i)) && table.get(array.get(i)).equals("out")) {
								array.remove(i);
							}
							else if(table.contains(array.get(i)) && !table.get(array.get(i)).equals("out")) {
								for (Matrix key : table.keySet()) {
									if(key.equals(array.get(i)) && f(key) <= eCost) {								
										array.remove(i);
									}else {
										stack.remove(key);
										table.remove(key);
									}
								}
							}
							else if(goal(array.get(i), finished_mat(mat))) {
								t = f(array.get(i));
								result = path2(array.get(i));
								ansNum = 0; //needs replacing
								ansCost = array.get(i).cost;
								return result.substring(1);
						}
						for (int k = array.size() - 1; k >= 0; k--) {
								stack.add(array.get(k));
								table.put(array.get(k), "");
						}
					}
				}
			}
		}
		return "no path";
	}
		
	public static int h(Matrix mat) {
		int Edistance = 0;
		Matrix temp = finished_mat(mat);
		//int num = 1;
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
					//Edistance += (Math.abs(i-x1) + Math.abs(j-y1)) * ;
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
		//return mat.cost + calculateManhattanDistance(mat);
	}
	
	
	//A* algorithm
	//extremely similar to BFS, only difference is that we choose the best child to continue, the one with the lowest estimated cost to goal. 
	//need to check if start from start or from the end
	public static String A(Matrix start){
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
			for (int j = 0; j < 4; j++) {
				Matrix temp2 = null;
				if(check_ok(new Matrix(temp), j)){
					temp2 = getMove(temp, j);
					num++;
					if(!q.contains(temp2) && !table.contains(temp2)) {
						//if(goal(temp2)) {
						if(goal(temp2, finished_mat(start))) {
							System.out.println("cost: " + temp2.cost);
							System.out.println("num: " + num);
							String ans = path2(temp2);
							ansNum = num;
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
		return "no path";
	}
	//not finished, need alot of checking with the badkan
	public static String DFID(Matrix start) {
		int depth = 1;
		while(true) { //not while true

			Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
			String result = Limited_DFS(start, depth, table);
			if(result != null && !result.equals("cutoff") && !result.equals("fail")) {
				ansNum = 0; //this is temporary for now, needs checking
				ansCost = 0;
				return result.substring(1);
			}
			depth++;
		}
		//should return no path
	}
	
	public static String Limited_DFS(Matrix n, int limit, Hashtable<Integer, Matrix> table) {
		if(goal(n, finished_mat(n))) {
			return path2(n);
		}else if(limit == 0) {
			return "cutoff";
		}else {
			table.put(limit, n); //needs change
			boolean isCutoff = false;
			for (int j = 0; j < 4; j++) {
				Matrix temp2 = null;
				if(check_ok(new Matrix(n), j)){
					temp2 = getMove(n, j); //instead of if's
					//num++;
					if(!table.contains(temp2)) {
						String result = Limited_DFS(temp2, limit - 1, table);
						if(result == "cutoff") {
							isCutoff = true;
						}else if(result != "fail") {
							ansCost = temp2.cost;
							return result;
						}
					}
				}
			}
			table.remove(limit); //needs change
			if(isCutoff == true) {
				return "cutoff";
			}else {
				return "fail";
			}
		}
	}
	
	
	public static String IDA(Matrix start) {
		Hashtable<Matrix, String> table = new Hashtable<Matrix, String>();
		Stack<Matrix> s = new Stack<Matrix>();
		//table.put(1, start);
		int n = 1;
		int t = h(start);
		while(t != Integer.MAX_VALUE) { //should be t != infinity
			int minF = Integer.MAX_VALUE;
			s.add(start);
			table.put(start, "");
			while(!s.isEmpty()) {
				Matrix temp = s.pop();
				if(table.get(temp).equals("out")) {
					table.remove(temp);
				}else {
					table.remove(temp);
					table.put(temp, "out");
					s.add(temp);
					for (int j = 0; j < 4; j++) {
						Matrix temp2 = null; //temp2 == g
						if(check_ok(new Matrix(temp), j)){
							temp2 = getMove(temp, j);
							if(f(temp2) > t) {
								minF = Math.min(minF, f(temp2));
								n++;
								//System.out.println("if1");
								continue;
							}
							if(table.contains(temp2) && table.get(temp2).equals("out")) {
								n++;
								continue;
							}
							if(table.contains(temp2) && !table.get(temp2).equals("out")) {//addition
								n++;
								Matrix temp3 = null; //g'
								for (Matrix key : table.keySet()) {
									if(key.equals(temp2)) {
										temp3 = key;
									}
								}//end addition
								if(f(temp3) > f(temp2)) {
									s.remove(temp3);
									table.remove(temp3); 
								}else {
									continue;
								}
							}
							if(goal(temp2, finished_mat(start))) {
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
		return "no path";
	}
	
	public static void Game(String path) throws NumberFormatException, IOException{
		Matrix start;
		String algo = null;
		boolean time = false;
		boolean openclosed;
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
			  String ans;
			  if(algo.equals("BFS")) {
				  ans = BFS(start);
			  }
			  else if(algo.equals("A*")) {
				  ans = A(start);
			  }
			  else if(algo.equals("DFID")) {
				  ans = DFID(start);
			  }
			  else if(algo.equals("IDA*")) {
				  ans = IDA(start);
			  }
			  else {
				  ans = DFBnB(start);
			  }
			  System.out.println("cost: " + start.cost);
			  long stopTime3 = System.nanoTime();
			  DecimalFormat df = new DecimalFormat("#.###");
			  String T = df.format((double)(stopTime3 - startTime3) / 1000000000);
		      output(ans, time, T);
		}
	
	public static void output(String path, boolean ifTime, String time) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		writer.println(path);
		writer.println("Num: " + ansNum);
		if(!path.equals("no path")) {
			writer.println("Cost: " + ansCost);
		}	
		if(ifTime) {
			writer.println(time); //change to actual time
		}
		writer.close();
	}
	
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		Game("");

	}
}
