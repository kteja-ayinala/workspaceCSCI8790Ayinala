package target;

@Table(name = "COMPONENT_APP", id = 101)
@Author(name = "CA", year = 1991)
public class ComponentApp {

   @Column(name = "COMPONENT_NAME", id = 102)
   @Author(name = "CB", year = 1992)
   String componentName = "102";

   @Author(name = "CD", year = 1993)
   String componentLocation;

   @Column(name = "COMPONENT_PROVIDER", id = 104)
   @Author(name = "CC", year = 1994)
   String componentProvider = "104";

   public void getComponent(int x, int y, int z) {
      System.out.println("[DBG] getComponent called");
   }

   public static void main(String[] args) {
      ComponentApp app = new ComponentApp();
      app.getComponent(1, 2, 3);
   }
}
