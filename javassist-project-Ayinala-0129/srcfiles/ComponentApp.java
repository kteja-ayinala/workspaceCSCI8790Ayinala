import java.lang.reflect.Field;

public class ComponentApp {
   
   public static void main(String[] args) throws Exception {
      System.out.println("Run...");
      ComponentApp localMyApp = new ComponentApp();
      localMyApp.runComponent();
      /*System.out.println("1) getClass() is used to show the name: " + localMyApp.getClass().getField(args[0]).getName());
      System.out.println("2) .class is used to show the name: " + MyApp.class.getField(args[0]).getName());*/
      System.out.println("Class.forName() is used to show the name: " + Class.forName(args[0]).getField(args[1]).getName());
      System.out.println("Done.");
   }

   public void runComponent() {
      System.out.println("Called runComponent.");
   }
}
