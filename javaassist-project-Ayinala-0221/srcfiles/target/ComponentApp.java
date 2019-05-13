package target;

public class ComponentApp {
   public void foo(int x, int y, int z) {
      System.out.println("[DBG] foo called");
   }

   public static void main(String[] args) {
      ComponentApp f = new ComponentApp();
      f.foo(1, 2, 3);
   }
}
