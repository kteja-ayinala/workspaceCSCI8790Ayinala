package ex07.insertmethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import util.UtilFile;
import util.UtilMenu;

public class InsertMethodBody extends ClassLoader {
	static String WORK_DIR = System.getProperty("user.dir");
	static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
	static String OUTPUT_DIR = WORK_DIR + File.separator + "output";
	static int PARAMETERS_COUNT = 0;
	static String TARGET_METHOD = null;
	static String TARGET_MY_APP = null;
	static ArrayList<String> MODIFIED_METHODS = new ArrayList<String>();


	static String _L_ = System.lineSeparator();

	public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		try {
			while (true) {
				UtilMenu.showMenuOptions();
				switch (UtilMenu.getOption()) {
				case 1:
					System.out.println("Enter  1)class name, 2)method name and 3)# of method parameters");
					String[] input = UtilMenu.getArguments();
					if (input.length != 3) {
						System.out.println("[WRN]: Invalid Input size");
						continue;
					} else {
						TARGET_METHOD = input[1];
						if (MODIFIED_METHODS != null && (MODIFIED_METHODS.contains(TARGET_METHOD))) {
							System.out.println("[WRN]: This method " + TARGET_METHOD + " has been modified");
							continue;
						} else {
							ClassPool pool = ClassPool.getDefault();
							pool.insertClassPath(INPUT_DIR);
							TARGET_MY_APP = "target." + input[0];
							CtClass cc = pool.get(TARGET_MY_APP);
							CtMethod m = null;
							try {
								m = cc.getDeclaredMethod(TARGET_METHOD);
							String k = m.getReturnType().getName();
						String j=	m.getParameterTypes().toString();
//							Parameter[] j = m.getClass().getDeclaredMethod(TARGET_METHOD).getParameters();
							
							
							System.out.println(k + " " +j);
							} catch (Exception e) {
								System.out.println("[WRN]Invalid input class!!");
								continue;
							}

							PARAMETERS_COUNT = Integer.parseInt(input[2]);
							// To get # of parameters
							CtClass[] fields = m.getParameterTypes();
							
							MODIFIED_METHODS.add(TARGET_METHOD);
							if (fields.length < PARAMETERS_COUNT) {
								PARAMETERS_COUNT = fields.length;

								int index = PARAMETERS_COUNT;
								String block1 = "{ " + _L_ //
										+ "System.out.println(\"[Inserted] param " + index + ":\"+$" + index + "); "
										+ _L_ //
										+ "}";
								System.out.println(block1);
								m.insertBefore(block1);
							} else {

								String block1 = "{ " + _L_; //

								for (int i = 1; i <= PARAMETERS_COUNT; i++) {
									int index = i;
									block1 += ( //
									"System.out.println(\"[Inserted] param " + index + ":\"+$" + index + "); " + _L_ //
									); //
								}
								block1 += "}";

								System.out.println(block1);
								m.insertBefore(block1);

							}
							cc.writeFile(OUTPUT_DIR);

//							 Loader cl = new Loader(pool);
//							 Class<?> c = cl.loadClass(TARGET_MY_APP);
//							 Object rect = c.newInstance();
//					
//							 Class<?> rectClass = rect.getClass();
						
//							 rectClass.getDeclaredMethod(TARGET_METHOD, new
//							 Class[] {});

							 InsertMethodBody s = new InsertMethodBody();
							 Class<?> t = s.loadClass(TARGET_MY_APP);
							 Method mainMethod = t.getDeclaredMethod("main",
							 new Class[] { String[].class });
							 mainMethod.invoke(null, new Object[] { args });
							// System.out.println("[DBG] write output to: " +
							// OUTPUT_DIR);
							// System.out.println("[DBG] \t" +
							// UtilFile.getShortFileName(OUTPUT_DIR));
						}
					}

					break;
				default:
					break;
				}
			}
		} catch (NotFoundException | CannotCompileException | IOException | SecurityException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

}
