class OutofBound{
    public static void main(String[] a){
	System.out.println(new LS().Start(10));
    }
}


// This class contains an array of integers and
// methods to initialize, print and search the array
// using Linear Search
class LS {
    int[] number ;
    int size ;
    int j ;
    int k ;
    int nt ;
    boolean ls01 ;
    int aux01 ;
    int aux02 ;
    int ifound ;
    
    // Invoke methods to initialize, print and search
    // for elements on the array
    public int Start(int sz){
	aux01 = this.Init(sz);
	aux02 = this.Print();
	return 55 ;
    }


    // Print array of integers
    public int Print(){
	j = 0 ;
	while (j < (size+1)) {
	    System.out.println(number[j]);
	    j = j + 1 ;
	}
	return 0 ;
    }

    
    // initialize array of integers with some
    // some sequence
    public int Init(int sz){
	size = sz ;
	number = new int[sz] ;
	
	j = 0 ;
	k = size + 1 ;
	while (j < (size)) {
	    aux01 = 2 * j ;
	    aux02 = k - 3 ;
	    number[j] = aux01 + aux02 ;
	    j = j + 1 ;
	    k = k - 1 ;
	}
	return 0 ;	
    }

}
