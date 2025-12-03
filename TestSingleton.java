import core.ResourceManager;

public class TestSingleton {
    public static void main(String[] args) {
        ResourceManager rm1 = ResourceManager.getInstance();
        ResourceManager rm2 = ResourceManager.getInstance();

        System.out.println(rm1 == rm2); // should print true
    }
}
