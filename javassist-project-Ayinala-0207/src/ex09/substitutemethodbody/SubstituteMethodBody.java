package ex09.substitutemethodbody;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import util.UtilMenu;

public class SubstituteMethodBody extends ClassLoader {
	static final String WORK_DIR = System.getProperty("user.dir");
	static final String INPUT_PATH = WORK_DIR + File.separator + "classfiles";
	static String OUTPUT_DIR = WORK_DIR + File.separator + "output";

	static String TARGET_MY_APP = null;
	static String TARGET_METHOD = null;
	static int INDEX = 0;
	static String VALUE = null;
	static final String DRAW_METHOD = "draw";
	static ArrayList<String> MODIFIED_METHODS =  new ArrayList<String>();

	static String _L_ = System.lineSeparator();

	public static void main(String[] args) throws Throwable {
		try {
			while (true) {
				UtilMenu.showMenuOptions();
				switch (UtilMenu.getOption()) {
				case 1:
					System.out.println("Enter 1)class name 2)method name 3)method parameter index and 4)value");
					String[] input = UtilMenu.getArguments();
					if (input.length != 4) {
						System.out.println("[WRN]: Invalid Input");
						continue;
					} else {
						TARGET_MY_APP = "target." + input[0];
						TARGET_METHOD = input[1];
						INDEX = Integer.parseInt(input[2]);
						VALUE = input[3];
						if (MODIFIED_METHODS != null && (MODIFIED_METHODS.contains(TARGET_METHOD))) {
							System.out.println("[WRN]: This method" + TARGET_METHOD + "has been modified");							
							continue;
						} else {
							SubstituteMethodBody s = new SubstituteMethodBody();
							Class<?> c = s.loadClass(TARGET_MY_APP);
							Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
							mainMethod.invoke(null, new Object[] { args });
						}
					}
					break;
				default:
					break;
				}
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	private ClassPool pool;

	public SubstituteMethodBody() throws NotFoundException {
		pool = new ClassPool();
		pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
		pool.insertClassPath(INPUT_PATH); // "target" must be there.
	}

	/*
	 * Finds a specified class. The bytecode for that class can be modified.
	 */
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CtClass cc = null;
		try {
			cc = pool.get(name);
			cc.instrument(new ExprEditor() {
				public void edit(MethodCall m) throws CannotCompileException {
					String className = m.getClassName();
					String methodName = m.getMethodName();

//					if (className.equals(TARGET_MY_APP) && methodName.equals(DRAW_METHOD)) {
//						System.out.println(
//								"[Edited by ClassLoader] method name: " + methodName + ", line: " + m.getLineNumber());
//						String block1 = "{" + _L_ //
//								+ "System.out.println(\"Before a call to " + methodName + ".\"); " + _L_ //
//								+ "$proceed($$); " + _L_ //
//								+ "System.out.println(\"After a call to " + methodName + ".\"); " + _L_ //
//								+ "}";
//						System.out.println("[DBG] BLOCK1: " + block1);
//						System.out.println("------------------------");
//						m.replace(block1);
//					} else 
						if (className.equals(TARGET_MY_APP) && methodName.equals(TARGET_METHOD)) {
						MODIFIED_METHODS.add(TARGET_METHOD);
						System.out.println(
								"[Edited by ClassLoader] method name: " + methodName + ", line: " + m.getLineNumber());
						String block2 = "{" + _L_ //
								+ "System.out.println(\"\tReset param to zero.\"); " + _L_ //
								+ "$" + INDEX + "=" + VALUE + ";" + _L_ //
								+ "$proceed($$); " + _L_ //
								+ "}";
						System.out.println("[DBG] BLOCK2: " + block2);
						System.out.println("------------------------");
						m.replace(block2);
					}
				}
			});
			cc.writeFile(OUTPUT_DIR);
			byte[] b = cc.toBytecode();
			return defineClass(name, b, 0, b.length);
		} catch (NotFoundException e) {
			throw new ClassNotFoundException();
		} catch (IOException e) {
			throw new ClassNotFoundException();
		} catch (CannotCompileException e) {
			e.printStackTrace();
			throw new ClassNotFoundException();
		}
	}
}
