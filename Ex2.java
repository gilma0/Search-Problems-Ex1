import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.TimeUnit;



public class Ex2 {
	
	static Hashtable<Integer, String> colors = new Hashtable<Integer, String>();
	
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
		
		//temp default for check
		/*public Matrix() {
			this.state = new int[2][2];
			int a = 1;
			for (int i = 0; i < state.length; i++) {
				for (int j = 0; j < state[0].length; j++) {
					this.state[i][j] = a++;
				}
			}
			this.emptyi = 0;
			this.emptyj = 0;
			this.cost = 0;
			this.state[0][0] = -1;
			this.state[1][1] = 5;
		}*/
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
		System.out.println("-------------------");
		for (int i = 0; i < mat.state.length; i++) {
			for (int j = 0; j < mat.state[0].length; j++) {
				System.out.print(mat.state[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("-------------------");
		if(mat.father != null) {
			return mat.father.state[mat.emptyi][mat.emptyj] + direction(mat) + "-" + path(mat.father);
		}else {
			return "";
		}
	}
	public static String path2(Matrix mat) {
		System.out.println("-------------------");
		for (int i = 0; i < mat.state.length; i++) {
			for (int j = 0; j < mat.state[0].length; j++) {
				System.out.print(mat.state[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("-------------------");
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
	//need the check num!!!!!!!~~~~~~~~~~~~~~~~~~~~~~~
	public static String BFS(Matrix start) {
		Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
		Queue<Matrix> q = new LinkedList<Matrix>();
		q.add(finished_mat(start));
		//q.add(start);
		int i = 0;
		int num = 1;
		while(!q.isEmpty()) {
			Matrix temp = q.poll();
			table.put(i++, temp);
			for (int j = 0; j < 4; j++) {
				Matrix temp2 = null;
				if(check_ok(new Matrix(temp), j)){
					if(j == 0) {
						temp2 = (empty_up(new Matrix(temp)));
					}
					if(j == 1) {
						//temp2 = new Matrix(empty_down(temp));
						temp2 = (empty_down(new Matrix(temp)));
					}
					if(j == 2) {
						//temp2 = new Matrix(empty_left(temp));
						temp2 = (empty_left(new Matrix(temp)));
					}
					if(j == 3) {
						//temp2 = new Matrix(empty_right(temp));
						temp2 = (empty_right(new Matrix(temp)));
					}
					
					if(!q.contains(temp2) && !table.contains(temp2)) {
						//if(goal(temp2)) {
						if(goal(temp2, start)) {
							System.out.println("cost: " + temp2.cost);
							System.out.println("num: " + num);
							String ans = path(temp2);
							return ans.substring(0, ans.length()-1); //removes the final "-"
						}else {
							q.add(temp2);
							num++;
						}
					}
				}
			}
		}
		System.out.println("num: " + num);
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
	////////// PROBLEM THAT CAN OCCUR AND I NEED TO CHECK IS IF THERE IS A NEED NOT TO DUMP ALL THE NODES!!!!~~~~ (choose the highest floor if duplicate)
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
					if(j == 0) {
						temp2 = (empty_up(new Matrix(temp)));
					}
					if(j == 1) {
						//temp2 = new Matrix(empty_down(temp));
						temp2 = (empty_down(new Matrix(temp)));
					}
					if(j == 2) {
						//temp2 = new Matrix(empty_left(temp));
						temp2 = (empty_left(new Matrix(temp)));
					}
					if(j == 3) {
						//temp2 = new Matrix(empty_right(temp));
						temp2 = (empty_right(new Matrix(temp)));
					}
					num++;
					if(!q.contains(temp2) && !table.contains(temp2)) {
						//if(goal(temp2)) {
						if(goal(temp2, finished_mat(start))) {
							System.out.println("cost: " + temp2.cost);
							System.out.println("num: " + num);
							String ans = path2(temp2);
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
					if(j == 0) {
						temp2 = (empty_up(new Matrix(n)));
					}
					if(j == 1) {
						//temp2 = new Matrix(empty_down(temp));
						temp2 = (empty_down(new Matrix(n)));
					}
					if(j == 2) {
						//temp2 = new Matrix(empty_left(temp));
						temp2 = (empty_left(new Matrix(n)));
					}
					if(j == 3) {
						//temp2 = new Matrix(empty_right(temp));
						temp2 = (empty_right(new Matrix(n)));
					}
					//num++;
					if(!table.contains(temp2)) {
						String result = Limited_DFS(temp2, limit - 1, table);
						if(result == "cutoff") {
							isCutoff = true;
						}else if(result != "fail") {//change fail!~!~!~!~!~!~
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
		//Hashtable<Matrix, Integer> table = new Hashtable<Matrix, Integer>();
		//Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
		//Hashtable<Matrix, Matrix> table = new Hashtable<Matrix, Matrix>();
		Hashtable<Matrix, String> table = new Hashtable<Matrix, String>();
		Stack<Matrix> s = new Stack<Matrix>();
		//table.put(1, start);
		int n = 1;
		int t = h(start);
		while(t != Integer.MAX_VALUE) { //should be t != infinity
			int minF = Integer.MAX_VALUE;
			s.add(start);
			//table.put(start, n++);
			//table.put(n++, start);
			//table.put(start, start);
			table.put(start, "");
			while(!s.isEmpty()) {
				Matrix temp = s.pop();
				/*if(temp.marked.equals("out")) {
					table.remove(temp);
				}else {*/
				if(table.get(temp).equals("out")) {
					table.remove(temp);
				}else {
					//temp.marked = "out";
					table.remove(temp);
					table.put(temp, "out");
					s.add(temp);
					for (int j = 0; j < 4; j++) {
						Matrix temp2 = null; //temp2 == g
						if(check_ok(new Matrix(temp), j)){
							if(j == 0) {
								temp2 = (empty_up(new Matrix(temp)));
							}
							if(j == 1) {
								//temp2 = new Matrix(empty_down(temp));
								temp2 = (empty_down(new Matrix(temp)));
							}
							if(j == 2) {
								//temp2 = new Matrix(empty_left(temp));
								temp2 = (empty_left(new Matrix(temp)));
							}
							if(j == 3) {
								//temp2 = new Matrix(empty_right(temp));
								temp2 = (empty_right(new Matrix(temp)));
							}
							//n++;
							//System.out.println("aba");
							if(f(temp2) > t) {
								minF = Math.min(minF, f(temp2));
								n++;
								//System.out.println("if1");
								continue;
							}
							//n++;
							//table.
							//if(table.contains(temp2) && table.get(temp2).marked.equals("out")) {
							if(table.contains(temp2) && table.get(temp2).equals("out")) {
								n++;
								//System.out.println("if2");
								continue;
							}
							//if(table.contains(temp2) && !table.get(temp2).marked.equals("out")) {
							if(table.contains(temp2) && !table.get(temp2).equals("out")) {//addition
								n++;
								Matrix temp3 = null; //g'
								for (Matrix key : table.keySet()) {
									if(key.equals(temp2)) {
										temp3 = key;
									}
								}//end addition
								//if(f(table.get(temp2)) > f(temp2)) { //first temp2 is g, second is g' needs checking!!!
								if(f(temp3) > f(temp2)) {
									//s.remove(table.get(temp2));
									s.remove(temp3);//should remove g'
									table.remove(temp3); //should remove g'
								}else {
									continue;
								}
							}
							if(goal(temp2, finished_mat(start))) { //this is g not g'
								System.out.println("cost: " + temp2.cost);
								System.out.println("num: " + n);
								String ans = path2(temp2);
								return ans.substring(1);
								//return path(temp2); //path of g, not g'
							}
							s.add(temp2);    //this is g, not g'
							//n++;
							//table.put(temp2, temp2); //this is g, not g'
							//table.put(temp2, "out");
							table.put(temp2, "");
						}
					}
				}
			}
			t = minF;
		}
		return "no path";
	}
	
	public static void main(String[] args) {
		Matrix aba = new Matrix();
		//System.out.println(A(aba));
		System.out.println(DFID(aba));
		//System.out.println(h(aba));
		//System.out.println(h(empty_left(new Matrix(aba))));
		//System.out.println("empty right: " + f(empty_right(empty_up(empty_left(new Matrix(aba))))));
		//System.out.println("empty left: " + f(empty_left(empty_up(empty_left(new Matrix(aba))))));
		//System.out.println("empty up: " + f(empty_up(empty_up(empty_left(new Matrix(aba))))));
		//System.out.println("empty down: " + f(empty_down(empty_up(empty_left(new Matrix(aba))))));
		////System.out.println("empty down: " + h(empty_down(empty_right(empty_up(empty_left(new Matrix(aba)))))));
		////System.out.println("empty up: " + h(empty_up(empty_right(empty_up(empty_left(new Matrix(aba)))))));
		//System.out.println("empty right: " + f(empty_right(empty_right(empty_up(empty_left(new Matrix(aba)))))));
		////System.out.println("empty left: " + h(empty_left(empty_right(empty_up(empty_left(new Matrix(aba)))))));
		//System.out.println(h(empty_up(new Matrix(aba))));
		//System.out.println(h(aba));
		//System.out.println(f(empty_left(new Matrix(aba))));
		//System.out.println(f(empty_left(empty_up(new Matrix(aba)))));
		//System.out.println(f(empty_up(new Matrix(aba))));
		//System.out.println(h(empty_left(empty_left(new Matrix(aba)))));

	}
}
