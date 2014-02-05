package NPuzzle;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class AStarSearch extends Search {
	
	class Comp implements Comparator<Node> {
		@Override
		public int compare(Node o1, Node o2) {
			return o1.costs < o2.costs ? -1 : o1.costs == o2.costs ? 0 : 1;
		}
     };  
	
	public PriorityQueue<Node> openList = new PriorityQueue<Node>(100, new Comp());  
	public PriorityQueue<Node> closedList = new PriorityQueue<Node>(100, new Comp());
	
	public AStarSearch(String input) throws IOException {
		super(input);
		openList.add(startState);
	}
	
	/**
	 * Search a Queue for a node according its puzzle
	 * @param node
	 * @param list
	 * @return
	 */
	protected Node searchQueue(Node node, PriorityQueue<Node> list){
		Iterator<Node> itr = list.iterator();
		while(itr.hasNext()){
			Node next = itr.next();
			if(comparePuzzle(next.puzzle, node.puzzle)){
				return next;
			}
		}
		return null;
	}
		
	
	/**
	 * Manhattan Heuristics
	 * 
	 * @param currentNode
	 * @return integer
	 */
	private int heuristics(Node currentNode){
	    return Math.abs(currentNode.xX - goalState.xX) + Math.abs(currentNode.yX - goalState.yX);
	}
	
	/**
	 * A-star algorithm 
	 * @return Node | null
	 */
	public Node search(){
		while(!openList.isEmpty()){
			
			Node currentNode = openList.poll();
			
			if (comparePuzzle(currentNode.puzzle, goalState.puzzle)){
				return currentNode;
			}
			
			for(Node successor : currentNode.getSuccessors()){
				successor.heuristicCost = heuristics(successor);
				successor.costs = currentNode.costs + successor.pathCost + successor.heuristicCost;
				
				Node findOpen = searchQueue(successor, openList);
				Node findClosed = searchQueue(successor, closedList);
				
				if (findOpen != null && findOpen.compareTo(successor) <= 0) continue;
				if (findClosed != null && findClosed.compareTo(successor) <= 0) continue;
				
				openList.remove(findOpen);
				closedList.remove(findClosed);
				
				openList.add(successor);
			}
			closedList.add(currentNode);
		}
		
		return null;
	}
	
	public static void main(String[] args){
		
		try {
			AStarSearch astar = new AStarSearch("/Users/Marc/Dropbox/School/UCSB/CS165A-MP1/testPuzzle.txt");
			Node result = astar.search();
			System.out.println(result.path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
   
}