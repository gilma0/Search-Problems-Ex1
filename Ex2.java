import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;



public class Ex2 {
	
	static Hashtable<Integer, String> colors = new Hashtable<Integer, String>();
	
	public static class Matrix {
		
		int cost;
		String path;
		int [][] state;
		Matrix father;
		int emptyi;
		int emptyj;

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
		}
		
		public Matrix(Matrix m) {
			this.cost = m.cost;
			this.emptyi = m.emptyi;
			this.emptyj = m.emptyj;
			this.father = m;
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
		if(colors.get(m.state[m.emptyi][m.emptyj]).equals("black")) { //black block, can't move
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
	
	public static boolean goal(Matrix mat) {
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
	}
	
	public static int path(Matrix mat) {
		//print the path
		/*System.out.println("-------------------");
		for (int i = 0; i < mat.state.length; i++) {
			for (int j = 0; j < mat.state[0].length; j++) {
				System.out.print(mat.state[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("-------------------");*/
		if(mat.father == null) {
			return 0;
		}else {
		 return 1 + path(mat.father);
		}
	}
	
	public static int BFS(Matrix start) {
		Hashtable<Integer, Matrix> table = new Hashtable<Integer, Matrix>();
		Queue<Matrix> q = new LinkedList<Matrix>();
		q.add(start);
		int i = 0;
		int num = 0;
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
					num++;
					if(!q.contains(temp2) && !table.contains(temp2)) {
						if(goal(temp2)) {
							System.out.println("cost: " + temp2.cost);
							System.out.println("num: " + num);
							return path(temp2);
						}else {
							q.add(temp2);
						}
					}
				}
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		Matrix aba = new Matrix();
		/*colors.put(1, "Green");
		colors.put(2, "Green");
		colors.put(3, "Green");
		colors.put(4, "Green");
		colors.put(5, "Green");
		colors.put(3, "Green");
		colors.put(3, "Green");
		colors.put(3, "Green");
		colors.put(3, "Green");
		colors.put(3, "Green");*/
		for (int i = 0; i < 10; i++) {
			colors.put(i+1, "Green");
		}
		colors.put(11, "Red");
		//colors.put(5, "Red");
		/*System.out.println("before:");
		for (int i = 0; i < aba.state.length; i++) {
			for (int j = 0; j < aba.state[0].length; j++) {
				System.out.print(aba.state[i][j] + " ");
			}
			System.out.println();
		}
		Matrix temp = empty_left(new Matrix(aba));
		System.out.println("after:");
		for (int i = 0; i < temp.state.length; i++) {
			for (int j = 0; j < temp.state[0].length; j++) {
				System.out.print(temp.state[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("cost: " + temp.cost);*/
		System.out.println(BFS(aba));
	}
}
