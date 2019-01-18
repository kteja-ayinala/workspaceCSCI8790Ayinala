package ex02.setsuper;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class SetSuperclass {
	static String _S = File.separator;
	static String WORK_DIR = System.getProperty("user.dir");
	static String OUTPUT_DIR = WORK_DIR + _S + "output";

	public static void main(String[] args) {
		int argswithcommon = 0;
		String superclass = null;
		String subclass = null;
		String arg0 = null;
		String arg1 = null;
		if (args.length != 2) { // condition to verify #.of arguments
			System.out.println("Not enough arguments");
			return;
		}
		for (int i = 0; i < args.length; i++) {
			System.out.println("[DBG] Arg[" + i + "]=" + args[i]);
			arg0 = args[0];
			arg1 = args[1];
			if (args[i].startsWith("Common")) {
				argswithcommon = argswithcommon + 1;
				superclass = "target." + args[i];
				System.out.println(superclass);
			} else {
				subclass = "target." + args[i];
				System.out.println(subclass);
			}
		}
		System.out.println("# of arguments with Common:" + argswithcommon);
		try {
			ClassPool pool = ClassPool.getDefault();
			if (argswithcommon == 1) {
				insertClassPath(pool);
				CtClass cc = pool.get(subclass);
				setSuperclass(cc, superclass, pool);
				cc.writeFile(OUTPUT_DIR);
				System.out.println("[DBG] write output to: " + OUTPUT_DIR);
			} else if (argswithcommon == 2) {
				if (arg0.length() > arg1.length()) {
					insertClassPath(pool);
					superclass = "target." + arg0;
					subclass = "target." + arg1;
					CtClass cc = pool.get(subclass);
					setSuperclass(cc, superclass, pool);
					cc.writeFile(OUTPUT_DIR);
					System.out.println("[DBG] write output to: " + OUTPUT_DIR);
				} else {
					insertClassPath(pool);
					superclass = "target." + arg1;
					subclass = "target." + arg0;
					CtClass cc = pool.get(subclass);
					setSuperclass(cc, superclass, pool);
					cc.writeFile(OUTPUT_DIR);
					System.out.println("[DBG] write output to: " + OUTPUT_DIR);
				}
			} else {
				insertClassPath(pool);
				superclass = "target." + arg0;
				subclass = "target." + arg1;
				CtClass cc = pool.get(subclass);
				setSuperclass(cc, superclass, pool);
				cc.writeFile(OUTPUT_DIR);
				System.out.println("[DBG] write output to: " + OUTPUT_DIR);
			}

			System.out.println("[DBG] write output to: " + OUTPUT_DIR);
		} catch (NotFoundException | CannotCompileException | IOException e) {
			e.printStackTrace();
		}
	}

	static void setSuperclass(CtClass curClass, String superClass, ClassPool pool)
			throws NotFoundException, CannotCompileException {
		curClass.setSuperclass(pool.get(superClass));
		System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
				", subclass: " + curClass.getName());
	}

	static void insertClassPath(ClassPool pool) throws NotFoundException {
		String strClassPath = WORK_DIR + _S + "target";
		pool.insertClassPath(strClassPath);
		System.out.println("[DBG] insert classpath: " + strClassPath);
	}
}
