package target;

@Table(name = "SERVICE_APP", id = 201)
@Author(name = "SA", year = 2001)
public class ServiceApp {

   @Row(name = "SERVICE_NAME", id = 202)
   @Author(name = "SB", year = 2002)
   String serviceName = "202";
   
   @Author(name = "SD", year = 2003)
   String serviceLocation;

   @Row(name = "SERVICE_PROVIDER", id = 204)
   @Author(name = "SC", year = 2004)
   String serviceProvider = "204";

   public void getService(int p1, int p2, int p3) {
      System.out.println("[DBG] getService called");
   }

   public static void main(String[] args) {
      ServiceApp app = new ServiceApp();
      app.getService(10, 20, 30);
   }
}
