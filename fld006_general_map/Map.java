package fld006_general_map;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Map{

	private int CUTOFF;
	private ForkJoinPool pool;
	private int OUTSIZE = 0;
	
	public Map(ForkJoinPool pool, int cutoff){
		this.pool = pool;
		this.CUTOFF = cutoff;
	}	
	
	public int[] map(int[] input, int output_size, IMapFunction func){
		OUTSIZE = output_size;
		return map(input, func);
	}
	public int[] map(int[] input, IMapFunction func){
		if(OUTSIZE == 0) OUTSIZE = input.length;
		int[] output =  new int[OUTSIZE];
		MapThread start = new MapThread(input, output, 0, input.length, CUTOFF, func);
		pool.invoke(start);
		return output;
	}
}

class MapThread extends RecursiveAction{

	private static final long serialVersionUID = 1L;
	private int CUTOFF;
	private int[] input;
	private int[] output;
	private int hi;
	private int lo;
	private IMapFunction func;
	
	public MapThread(int[] input, int[] output, int lo, int hi, int cutoff,  IMapFunction func){
		this.input = input;
		this.output = output;
		this.lo = lo;
		this.hi = hi;
		this.CUTOFF = cutoff;
		this.func = func;
	}
	
	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		if(hi - lo > CUTOFF){
			//Do recursive calls!
			int m = (hi + lo)/2;
			MapThread left = new MapThread(input, output, lo, m, CUTOFF, func);
			MapThread right = new MapThread(input, output, m, hi, CUTOFF, func);
			left.fork();
			right.compute();
			left.join()
		}else{ //sequential code
			func.calculateValue(input, output, lo, hi);
		}
	}
	
}


