package csp;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class ConstraintSatisfactionProblem {
	
	// used to store performance information about search runs.
	//  these should be updated during the process of searches

	// see methods later in this class to update these values
	protected int nodesExplored;
	protected int maxMemory;

	// protected List<Integer> assignment;
	protected Assignment assignment;

	protected abstract void assignmentInit();
	protected abstract List<Integer> orderDomainValues(int var);
	protected abstract int unassignedVar();
	protected abstract boolean assignmentComplete();
	protected abstract boolean valueConsistent(int var, int value);


	public Assignment basicBacktrackingSearch() {
		resetStats(); 

		return backtrack(assignment);
	}

	// recursive memoizing dfs. Private, because it has the extra
	// parameters needed for recursion.  
	private Assignment backtrack(Assignment assignment) {
		Assignment result = null;

		if (this.assignmentComplete()) {// assignment is complete
			// System.out.println("test");
			result = assignment;
		} else {
			int var = this.unassignedVar(); // next variable, or generated by the problem
			for (Integer valueObj: this.orderDomainValues(var)) {
				int value = valueObj.intValue();
				if (valueConsistent(var, value)) {// value is consistent with assignment
					// assignment.add(value);
					assignment.assign(var, value);

					// inference goes here
					result = backtrack(assignment);
					if (result != null) {
						break;
					}

					assignment.unassign(var);
					// assignment.remove(assignment.size() - 1);
				}
			}
		}

		return result;
	}


	protected void resetStats() {
		nodesExplored = 0;
		maxMemory = 0;
	}
	
	protected void printStats() {
		System.out.println("Nodes explored during last search:  " + nodesExplored);
		System.out.println("Maximum memory usage during last search " + maxMemory);
	}
	
	protected void updateMemory(int currentMemory) {
		maxMemory = Math.max(currentMemory, maxMemory);
	}
	
	protected void incrementNodeCount() {
		nodesExplored++;
	}

	public class Assignment{
		// protected List<Integer> assignment_li;

		protected int[] assignment;
		protected int varCount;
		protected int assigned;
		public Assignment(int count){
			this.varCount = count;
			assignment = new int[varCount];
			this.assigned = 0;
			for (int i = 0; i < varCount; ++i) {
				assignment[i] = -1;
			}
		}

		public Assignment(Assignment ass){
			this.varCount = ass.varCount;
			this.assigned = ass.assigned;
			System.arraycopy(ass.assignment, 0, this.assignment, 0, varCount);
		}

		public void assign(int var, int val){
			assignment[var] = val;
			assigned++;
		}

		public void unassign(int var){
			assignment[var] = -1;
			assigned--;
		}

		public boolean isAssigned(int var){
			return assignment[var] != -1;
		}

		public boolean allAssigned(){
			return assigned == varCount;
		}

		public int assignmentAt(int var){
			return assignment[var];
		}

		public void print(){
			for (int val: assignment) {
				System.out.print(val + " ");
			}
			System.out.print("\n");
		}

	}
}
