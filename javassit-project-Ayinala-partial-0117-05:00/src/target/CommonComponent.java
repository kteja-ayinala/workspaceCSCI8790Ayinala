package target;

public class CommonComponent {
   int x, y;

   public void copy(Integer dx, Integer dy) {
      x += dx;
      y += dy;
   }

   public void copy(int dx, int dy) {
      x += dx;
      y += dy;
   }
}
