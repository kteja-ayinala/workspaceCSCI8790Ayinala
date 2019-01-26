package javassistloader;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.Loader;
import util.UtilMenu;

public class JavassistLoaderExample {
	private static final String WORK_DIR = System.getProperty("user.dir");
	private static final String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
	private static final String TARGET_POINT = "target.Point";
	private static final String TARGET_RECTANGLE = "target.Rectangle";
	static String _S = File.separator;
	static String OUTPUT_DIR = WORK_DIR + _S + "output";
	static String verifyMethod ;

	public static void main(String[] args) {
		try {

			while (true) {
				UtilMenu.showMenuOptions();
				switch (UtilMenu.getOption()) {
				case 1:
					System.out.println("Enter three method names:");
					String[] mthdNames = UtilMenu.getArguments();
					if (mthdNames.length != 3) {
						System.out.println("[WRN]: Invalid Input");
						continue;
					} else {
						ClassPool pool = ClassPool.getDefault();
						pool.insertClassPath(INPUT_DIR);
						System.out.println("[DBG] class path: " + INPUT_DIR);

						CtClass cc = pool.get(TARGET_RECTANGLE);
						 cc.defrost();
						cc.setSuperclass(pool.get(TARGET_POINT));
						System.out.println(verifyMethod+" "+ mthdNames[0].toString());
//						if(!verifyMethod.equals(mthdNames[0].toString())){
						CtMethod m1 = cc.getDeclaredMethod(mthdNames[0]);
						
						 
						m1.insertBefore("{ " //
								+ mthdNames[1].toString() + "();" //
								+ "System.out.println(\"[TR] getX result : \"+" + mthdNames[2] + "());}");
						Loader cl = new Loader(pool);
						Class<?> c = cl.loadClass(TARGET_RECTANGLE);
						Object rect = c.newInstance();
						System.out.println("[DBG] Created a Rectangle object.");

						Class<?> rectClass = rect.getClass();
						Method m = rectClass.getDeclaredMethod(mthdNames[0], new Class[] {});
						System.out.println("[DBG] Called getDeclaredMethod.");
						Object invoker = m.invoke(rect, new Object[] {});
						System.out.println("[DBG] getVal result: " + invoker);
						cc.writeFile(OUTPUT_DIR);
//						}
//						else{
//							continue;
//						}
					}
					
					break;
				default:
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
