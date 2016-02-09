public class test {
    public int item;
    
    test next;
    
    public test() {
        item = 1;
        next = this;
    }
    
    public static void main(String[] args) {
        test t = new test();
        System.out.println(t.toString() + t.next.toString());
	}

}
