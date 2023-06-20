class Test {
    public static int fnB(int n) {
        int sum = 0;
        for (int i = 1; i < n; i = 2 * i) {
            for (int j = 1; j < n; j = 2 * j) {
                for (int k = 0; k < j; k++) {
                    sum += i + j + k;
                }
            }
        }
        return sum;
    }
    
    public static void main(String[] args) {
        System.out.println(fnB(5));
    }
}