package ex11.newexpr;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

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
	static String TARGET_CLASS = null;
	static int PARAMETERS_COUNT = 0;
	static String _L_ = System.lineSeparator();
	static Class a;
	static Class b;
	int LAST_FIELD = 0;

	public static void main(String[] args) throws Throwable {
		while (true) {
			UtilMenu.showMenuOptions();
			switch (UtilMenu.getOption()) {
			case 1:
				System.out.println("Enter 1)Class name 2)# of parameters");
				String[] input = UtilMenu.getArguments();
				// check input parametrs
				if (input.length != 2) {
					System.out.println("[WRN]: Invalid Input");
					continue;
				} else {
					TARGET_CLASS = "target." + input[0];
					PARAMETERS_COUNT = Integer.parseInt(input[1]);
					ClassPool pool = ClassPool.getDefault();
					pool.insertClassPath(CLASS_PATH);
					a = Class.forName("target.Column");
					b = Class.forName("target.Row");
					
					NewExprAccess s = new NewExprAccess();
					Class<?> c = s.loadClass(TARGET_CLASS);
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
					try {
						CtField fields[] = newExpr.getEnclosingClass().getDeclaredFields();

//						 for (CtField m : fields) {
//						 process(m.getAnnotations());
//						 }
						if (fields.length < PARAMETERS_COUNT) {
							int totalfields = fields.length;
							LAST_FIELD = totalfields - 1;
						}
						

						String log = String.format(
								"[Edited by ClassLoader] new expr: %s, " //
										+ "line: %d, signature: %s",
								newExpr.getEnclosingClass().getName(), //
								newExpr.getLineNumber(), newExpr.getSignature());
						System.out.println(log);
						String block1 = "{ " + _L_ //
								+ "  $_ = $proceed($$);" + _L_;
						if (LAST_FIELD == 0) {
							for (int i = 0; i < PARAMETERS_COUNT; i++) {
								String fieldName = fields[i].getName();
								String fieldType = fields[i].getType().getName();
								// Check if the field is annotated
								boolean an = process(fields[i].getAnnotations());
								if (an) {
									block1 += ( //
									"{ " + _L_ //
											+ "  String cName = $_.getClass().getName();" + _L_ //
											+ "  String fName = $_.getClass().getDeclaredFields()[" + i + "].getName();"
											+ _L_ //
											+ "  String fieldFullName = cName + \".\" + fName;" + _L_ //
											+ fieldType + " fieldValue = $_." + fieldName + ";" + _L_ //
											+ "  System.out.println( \"[Instrument] \" +  fieldFullName + \" :\" + fieldValue);"
											+ _L_ //
											+ "}" + _L_ //
									); //
								} 
									
							}
						}
						// if entered number exceeds count display last field
						else {

							String fieldName = fields[LAST_FIELD].getName();
							String fieldType = fields[LAST_FIELD].getType().getName();
							// Check if the field is annotated
							boolean an = process(fields[LAST_FIELD].getAnnotations());
							if (an) {
								block1 += ( //
								"{ " + _L_ //
										+ "  String cName = $_.getClass().getName();" + _L_ //
										+ "  String fName = $_.getClass().getDeclaredFields()[" + LAST_FIELD
										+ "].getName();" + _L_ //
										+ "  String fieldFullName = cName + \".\" + fName;" + _L_ //
										+ fieldType + " fieldValue = $_." + fieldName + ";" + _L_ //
										+ "  System.out.println( \"[Instrument] \" +  fieldFullName + \" :\" + fieldValue);"
										+ _L_ //
										+ "}" + _L_ //
								); //
							}
						}
						block1 += "}";
						System.out.println(block1);
						newExpr.replace(block1);
						// }
					} catch (NotFoundException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// Check if annotated
				boolean process(Object[] annoList) {
					boolean exist = false;
					for (int i = 0; i < annoList.length; i++) {
						if (a.isInstance(annoList[i]) || b.isInstance(annoList[i])) {
							exist = true;
							return true;
						}
					}
					return exist;

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
