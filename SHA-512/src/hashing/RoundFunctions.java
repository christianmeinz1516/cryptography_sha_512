package hashing;


/**
 * 
 * @author meinzecp
 * 
 * Use the bitwise functions to calculate the round functions of
 * Ch, Maj, Sum0, Sum1, Sig0, Sig1, rotation, and shifts. Also included are methods that
 * are used for both compression and word scheduling.
 * 
 * Function math is taken from the M14-SHA512-Reference.pdf sheet on Moodle.
 * 
 */
public class RoundFunctions {
	
	protected static long ch(long e, long f, long g) 
		{return (e & f)^(~e & g);}
	
	protected static long maj(long a, long b, long c) 
		{return (a & b)^(a & c)^(b & c);}
	
	protected static long sum0(long a)
		{return (rotate(a,28))^(rotate(a,34))^(rotate(a,39));}
	
	protected static long sum1(long e)
		{return (rotate(e,14))^(rotate(e,18))^(rotate(e,41));}

	protected static long rotate(long x, int n)
		{return (x << (Long.SIZE-n)) | (x >>> n);}
	
	protected static long sig0(long x)
		{return (rotate(x,1))^(rotate(x,8))^(shr(x,7));}
	
	protected static long sig1(long x)
		{return (rotate(x,19))^(rotate(x,61))^(shr(x,6));}

	protected static long shr(long x, int n)
		{return (x >>> n);}
}
