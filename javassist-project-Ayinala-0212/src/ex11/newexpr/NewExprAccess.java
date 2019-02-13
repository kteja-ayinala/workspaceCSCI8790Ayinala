package ex11.newexpr;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import util.UtilMenu;

public class NewExprAccess extends ClassLoader {
	static final String WORK_DIR = System.getProperty("user.dir");
	static final String CLASS_PATH = WORK_DIR + File.separator + "classfiles";
	static String OUTPUT_DIR = WORK_DIR + File.separator + "output";
	static String TARGET_MY_APP2 = null;
	static int PARAMETERS_COUNT = 0;
	static String _L_ = System.lineSeparator();

	public static void main(String[] args) throws Throwable {
		while (true) {
			UtilMenu.showMenuOptions();
			switch (UtilMenu.getOption()) {
			case 1:
				System.out.println("Enter 1)class name 2)# of parameters");
				String[] input = UtilMenu.getArguments();
				if (input.length != 2) {
					System.out.println("[WRN]: Invalid Input");
					continue;
				} else {
					TARGET_MY_APP2 = "target." + input[0];
					PARAMETERS_COUNT = Integer.parseInt(input[1]);
					NewExprAccess s = new NewExprAccess();
					Class<?> c = s.loadClass(TARGET_MY_APP2);
					Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
					mainMethod.invoke(null, new Object[] { args });
				}
				break;
			default:
				break;
			}
		}

	}

	private ClassPool pool;

	public NewExprAccess() throws NotFoundException {
		pool = new ClassPool();
		pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
		pool.insertClassPath(CLASS_PATH); // TARGET must be there.
	}

	/*
	 * Finds a specified class. The bytecode for that class can be modified.
	 */
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CtClass cc = null;
		try {
			cc = pool.get(name);
			cc.instrument(new ExprEditor() {
				public void edit(NewExpr newExpr) throws CannotCompileException {
					String fieldName = null;
					String fieldType = null;
					String fieldName1 = null;
					String fieldType1 = null;
					try {
						CtField fields[] = newExpr.getEnclosingClass().getDeclaredFields();
						if (fields.length < PARAMETERS_COUNT) {
							PARAMETERS_COUNT = fields.length;
						}

						String log = String.format(
								"[Edited by ClassLoader] new expr: %s, " //
										+ "line: %d, signature: %s",
								newExpr.getEnclosingClass().getName(), //
								newExpr.getLineNumber(), newExpr.getSignature());
						System.out.println(log);
						if (PARAMETERS_COUNT == 1) {
							fieldName = fields[0].getName();
							fieldType = fields[0].getType().getName();

							String block1 = "{ " + _L_ //
									+ "  $_ = $proceed($$);" + _L_ //
									+ "  String cName = $_.getClass().getName();" + _L_ //
									+ "  String fName = $_.getClass().getDeclaredFields()[0].getName();" + _L_ //
									+ "  String fieldFullName = cName + \".\" + fName;" + _L_ //
									+ fieldType + " fieldValue = $_." + fieldName + ";" + _L_ //
									+ "  System.out.println( \"[Instrument] \" +  fieldFullName + \" :\" + fieldValue);"
									+ _L_ //
									+ "}"; //

							System.out.println(block1);
							newExpr.replace(block1);
						}
						if (PARAMETERS_COUNT == 2) {
							for (int i = 0; i < PARAMETERS_COUNT; i++) {
								if (i == 0) {
									fieldName = fields[i].getName();
									fieldType = fields[i].getType().getName();
								} else {
									fieldName1 = fields[i].getName();
									fieldType1 = fields[i].getType().getName();
								}
							}

							String block1 = "{ " + _L_ //
									+ "  $_ = $proceed($$);" + _L_ //
									+ "  String cName = $_.getClass().getName();" + _L_ //
									+ "  String fName = $_.getClass().getDeclaredFields()[0].getName();" + _L_ //
									+ "  String fieldFullName = cName + \".\" + fName;" + _L_ //
									+ "  String fName1 = $_.getClass().getDeclaredFields()[1].getName();" + _L_ //
									+ fieldType + " fieldValue = $_." + fieldName + ";" + _L_ //
									+ fieldType1 + " fieldValue1 = $_." + fieldName1 + ";" + _L_ //
									+ "  System.out.print( \"[Instrument] \" +  fieldFullName + \" :\" + fieldValue+ "
									+ "\", \" );" + "  System.out.println(  fName1 + \" :\" + fieldValue1);" + _L_ //
									+ "}"; //

							System.out.println(block1);
							newExpr.replace(block1);
						}

					} catch (NotFoundException e) {
						e.printStackTrace();
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