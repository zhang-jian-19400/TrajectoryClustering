package cluster.trajectory;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;


public class Cluster {

	protected int clusterID;
	protected String clusterName;
	protected Trajectory representativeTrajectory;
	protected int clusterSize;
	protected ArrayList<cluster.trajectory.Clusterable> elements;
	protected int cardinality;
	
	//For K-Medoids
	protected Trajectory centroidElement;
	protected double minInternalVariation;
	
	protected HashSet<Integer> parentTrajectories;
	
	public Cluster(int clusterID, String clusterName) {
		this.clusterID = clusterID;
		parentTrajectories = new HashSet<Integer>();
		elements = new ArrayList<>();
		clusterSize = 0;
		this.clusterName = clusterName;
	}	
	
	@Override
	protected Cluster clone()
	{
		// TODO Auto-generated method stub
		Cluster cloned = new Cluster(this.clusterID, this.clusterName);
		
		cloned.setRepresentativeTrajectory(this.representativeTrajectory);
		cloned.clusterSize = this.clusterSize;

		cloned.elements = new ArrayList<Clusterable>();

		for(int i=0; i<this.elements.size(); i++)
		{
			Clusterable singleElement;
			try {
				singleElement = (Clusterable) this.elements.get(i).clone();
				cloned.elements.add(singleElement);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		cloned.cardinality = this.cardinality;

		cloned.centroidElement = this.centroidElement;
		cloned.minInternalVariation = this.minInternalVariation;
		
		cloned.parentTrajectories = this.parentTrajectories;
		
		return cloned;
	}
	
	public Cluster cloneStructureWithNoElements()
	{
		// TODO Auto-generated method stub
		Cluster cloned = new Cluster(this.clusterID, this.clusterName);
		
		//cloned.setRepresentativeTrajectory(this.representativeTrajectory);
		//cloned.clusterSize = this.clusterSize;

		cloned.elements = new ArrayList<Clusterable>();
		//cloned.elements = (ArrayList) this.elements.clone();
		//cloned.cardinality = this.cardinality;

		cloned.centroidElement = this.centroidElement;
		cloned.minInternalVariation = this.minInternalVariation;
		
		//cloned.parentTrajectories = this.parentTrajectories;
		
		return cloned;
	}
	
	
	public void addElement(Clusterable c)
	{
		elements.add(c);
		clusterSize = elements.size();
	}
	
	//Refactor this
	public Trajectory calculateRepresentativeTrajectory(int minLines, double smoothingParameter)
	{
		//Should be overwritten
		return null;
	}
	
	//This method is not handling possible Errors.
	//I need the data in trajectory way to compute DBA DTW a
	public ArrayList<Trajectory> getElementsAsTrajectoryObjects()
	{
		ArrayList<Trajectory> trajectories = new ArrayList<Trajectory>();
		
		for(Clusterable cl:elements)
		{
			trajectories.add((Trajectory) cl);
		}
		
		return trajectories;
	}
	
	public boolean hasSameElements(Cluster o)
	{
		boolean sameElements = false;
		
		HashSet<Integer> clusterElements = new HashSet<Integer>();
		for(Clusterable t: elements)
		{
			clusterElements.add(t.id);
		}
		
		HashSet<Integer> otherClusterElements = new HashSet<Integer>();
		for(Clusterable e: o.getElements())
		{
			otherClusterElements.add(e.id);
		}
		
		if(clusterElements.containsAll(otherClusterElements))
		{
			sameElements = true;
		}
		return sameElements;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cluster other = (Cluster) obj;
		if (centroidElement == null) {
			if (other.centroidElement != null)
				return false;
		} else if (!centroidElement.equals(other.centroidElement))
			return false;
		if (clusterID != other.clusterID)
			return false;
		if (clusterName == null) {
			if (other.clusterName != null)
				return false;
		} else if (!clusterName.equals(other.clusterName))
			return false;
		if (clusterSize != other.clusterSize)
			return false;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}

	//This method is not handling possible Errors.
	//I need the data in trajectory way to compute DBA DTW a
	public Trajectory[] getElementsAsTrajectoryObjectsArray()
	{
		Trajectory[] trajectories = null;
		try{
			
			trajectories = new Trajectory[elements.size()];
			
			for(int i = 0; i<elements.size();i++)
			{
				trajectories[i] = (Trajectory) elements.get(i);
			}
		}catch(Exception ex){
			System.err.println("Index overflow: " + ex.getMessage());
		}
		return trajectories;
	}

	public int getClusterID() {
		return clusterID;
	}

	public void setClusterID(int clusterID) {
		this.clusterID = clusterID;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public Trajectory getRepresentativeTrajectory() {
		return representativeTrajectory;
	}

	public void setRepresentativeTrajectory(Trajectory representativeTrajectory) {
		this.representativeTrajectory = representativeTrajectory;
	}

	public ArrayList<Clusterable> getElements() {
		return elements;
	}
	
	public void setElements(ArrayList<Clusterable> elements) {
		this.elements = elements;
	}

	public Object getSingleElement(int i) {
		return elements.get(i);
	}

	
	public HashSet<Integer> getParentTrajectories() {
		return parentTrajectories;
	}

	public void setParentTrajectories(HashSet<Integer> parentTrajectories) {
		this.parentTrajectories = parentTrajectories;
	}

	public Trajectory getCentroidElement() {
		return centroidElement;
	}

	public void setCentroidElement(Trajectory centroidElement) {
		this.centroidElement = centroidElement;
	}

	public double getMinInternalVariation() {
		return minInternalVariation;
	}

	public void setMinInternalVariation(double minInternalVariation) {
		this.minInternalVariation = minInternalVariation;
	}

	public int calculateCardinality() {
		this.cardinality = 0;
		for(Object o:elements)
		{
			if(o.getClass().equals(Segment.class))
			{
				
			Segment tempSegment = (Segment) o;
			parentTrajectories.add(tempSegment.getParentTrajectory());
			}else if(o.getClass().equals(Trajectory.class)){
				Trajectory tempTrajectory = (Trajectory) o;
				parentTrajectories.add(tempTrajectory.getTrajectoryId());
			}else{
				//Handle error here
				System.out.println("Wrong casting in cluster class, not trajectory or segment");
			}
		}
		
		cardinality = parentTrajectories.size();
		return cardinality;
	}
	
	public void calculateCentroid() throws Exception 
	{
		

		//Find the point that minimizes the overall square sum 
		//of distances to all other points and make it the new centroid
			
		//This next method could fail
		if(getCentroidElement()==null)
		{
			if(elements.size()>0)
			{
				Trajectory t = Clusterable.convertToTrajectory(elements.get(0));
				setCentroidElement(t);
			}else{
				throw new Exception("Fatal Error, no Trajectories Available");
			}
		}
		
		Trajectory centroidTrajectory = DBA.DBATrajectories(getCentroidElement(), getElementsAsTrajectoryObjectsArray());
		setCentroidElement(centroidTrajectory);

		
	}
	
	/**
	 * Only to return clusters with at least a minimum number of Elements minElems
	 * @param originalClusters
	 * @param minElems
	 * @return
	 */
	public static ArrayList<Cluster> keepClustersWithMinElements(ArrayList<Cluster> originalClusters, int minElems)
	{
		ArrayList<Cluster> purgedClusters = new ArrayList<Cluster>();
		
		for(Cluster c: originalClusters)
		{
			if(c.getElements().size()>=minElems)
			{
				purgedClusters.add(c);
			}
		}
		return purgedClusters;
	}

	@Override
	public String toString() {
		return "Cluster [clusterID=" + clusterID + ", size="
						+ clusterSize + ", elements=" + elements
				+ ", cardinality=" + cardinality + ", parentTrajectories="
				+ parentTrajectories + "]";
	}
	
	
	public String toStringComplete() {
		return "Cluster [clusterID=" + clusterID + ", clusterName="
				+ clusterName + ", representativeTrajectory="
				+ representativeTrajectory  + ", size="
						+ clusterSize + ", elements=" + elements
				+ ", cardinality=" + cardinality + ", parentTrajectories="
				+ parentTrajectories + "]";
	}
}
