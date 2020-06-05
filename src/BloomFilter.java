

/**
 * Stores an approximation of a set of strings using a bloom filter.
 *
 * Creates a Bloom filter that can store a set S of cardinality setSize. 
 * The size of the filter should approximately be setSize * bitsPerElement.
 * The number of hash functions should be the optimal choice which is ln 2¡¿filterSize/setSize.
 */
public interface BloomFilter {

    /**
     * Adds the string {@code s} to the filter. Type of this method is void. This method
	 * should be case-insensitive
     */
    void add(String s);

    /**
     * Returns {@code true} iff {@code s} appears in the filter.
     */
    boolean appears(String s);

    /**
     * Returns the size of the filter (the size of the table).
     */
    int filterSize();

    /**
     * Returns the number of elements added to the filter.
     */
    int dataSize();

    /**
     * Returns the number of hash function used.
     */
    int numHashes();
    
    /**
     * Returns {@code true} if the j-th bit of the filter is 1
     */
    boolean getBit(int j);
	
}