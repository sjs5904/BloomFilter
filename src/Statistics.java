

import java.security.SecureRandom;
import java.util.BitSet;
import java.util.concurrent.ThreadLocalRandom;

public class Statistics {
	
	public static void main(String[] args) {
		Statistics stat = new Statistics();
		int setSize = 10;
		int bitsPerElement = 4;
		BloomFilterFNV fnv1 = new BloomFilterFNV(setSize, 4);
		BloomFilterFNV fnv2 = new BloomFilterFNV(setSize, 4);
		int length = ThreadLocalRandom.current().nextInt(4, bitsPerElement + 1);
		String random;
		int count = 0;
		for(int i = 0; i < setSize; i++) {
			random = generateRandomString(length);
			fnv1.add(random);
			count ++;
		}	
		
		for(int i = 0; i < setSize; i++) {
			random = generateRandomString(length);
			fnv2.add(random);
		}
		
		System.out.println("answer 		:  " + count);
		System.out.println("result 		:  " + stat.estimateSetSize(fnv1));
		System.out.println("intersection	:  " + stat.estimateIntersectSize(fnv1, fnv2));
	}
	
	public static String generateRandomString(int len) {
        if (len < 1) throw new IllegalArgumentException();
       
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(len);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(chars.length());
            char ranChar = chars.charAt(index);

            sb.append(ranChar);
        }

        return sb.toString();

    }
	
	/**
	 * returns an estimate for the number of elements stored in the filter
	 * http://en.wikipedia.org/wiki/Bloom_filter#Approximating_the_number_of_items_in_a_Bloom_filter
	 * @param f BloomFilterFNV
	 * @return double
	 */
	public static double estimateSetSize(BloomFilterFNV f) {
		double n;
		double m = f.filterSize();
		double k = f.numHashes();
		double X = f.filter.cardinality();
		
		n = -1*(m/k)*Math.log(1-X/m);
		
		return n;
	}
	
	/**
	 * Returns an estimate for the size of S1 ¡û S2 when S1 is stored in f1 and S2 is stored in f2.	
	 * https://en.wikipedia.org/wiki/Bloom_filter#The_union_and_intersection_of_sets
	 * @param f1 BloomFilterFNV
	 * @param f2 BloomFilterFNV
	 * @return double
	 */
	public static double estimateIntersectSize(BloomFilterFNV f1, BloomFilterFNV f2) {
		double nA;
		double mA = f1.filterSize();
		double kA = f1.numHashes();
		double XA = f1.filter.cardinality();
		double nB;
		double mB = f2.filterSize();
		double kB = f2.numHashes();
		double XB = f2.filter.cardinality();
		double Z;
		double t;
		double N;
		BitSet temp = (BitSet) f2.filter.clone();

//		from assignment pdf
//		temp.and(f1.filter);		
//		Z = (f1.count0()+f2.count0()-(temp.length()-temp.cardinality()))/((double) f1.count0()*f2.count0());
//		t = -(Math.log(mA*Z))/(kA*Math.log(1-1/mA));
		
//		from wiki page
		temp.or(f1.filter);
		t = -(mA/kA)*Math.log(1-temp.cardinality()/mA);
//		System.out.println(f1.filter);
//		System.out.println(f2.filter);
//		System.out.println(temp);
		
		nA = -(mA/kA)*Math.log(1-XA/mA);
		nB = -(mB/kB)*Math.log(1-XB/mB);
//		System.out.println(XA + " " + XB + " " + temp.cardinality());
//		System.out.println(nA + " " + nB + " " + t);
		N = nA+nB-t;
		if (N<0) N = 0;
		
		return N;
	}
}
