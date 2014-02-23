package csp;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class ConstraintSatisfactionProblem {
	
	// used to store performance information about search runs.
	//  these should be updated during the process of searches

	// see methods later in this class to update these values
	protected int nodesExplored;
	protected int maxMemory;

	protected List<Integer> assignment;

	protected abstract List<Integer> assignmentInit();
	protected abstract List<Integer> orderDomainValues(int var);
	protected abstract boolean assignmentComplete();
	protected abstract boolean valueConsistent(int value);


	public List<Integer> basicBacktrackingSearch() {
		resetStats(); 

		return backtrack(assignment);
	}

	// recursive memoizing dfs. Private, because it has the extra
	// parameters needed for recursion.  
	private List<Integer> backtrack(List<Integer> assignment) {
		List<Integer> result = null;

		if (this.assignmentComplete()) {// assignment is complete
			result = assignment;
		} else {
			int var = assignment.size(); // next variable, or generated by the problem
			for (int value: this.orderDomainValues(var)) {
				if (valueConsistent(value)) {// value is consistent with assignment
					assignment.add(value);

					// inference goes here

					result = backtrack(assignment);
					if (result != null) {
						break;
					}

					assignment.remove(assignment.size() - 1);
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

}
