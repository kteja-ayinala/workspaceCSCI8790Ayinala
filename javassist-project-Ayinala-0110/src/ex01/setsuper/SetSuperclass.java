package ex01.setsuper;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
/*import javassist.ClassClassPath;*/
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
/*import target.Rectangle;*/

public class SetSuperclass {
	static String _S = File.separator;
	static String workDir = System.getProperty("user.dir");
	static String outputDir = workDir + _S + "output";

	public static void main(String[] args) {
		int argswithcommon = 0;
		String superclass = null;
		String subclass = null;
		String arg0 = null;
		String arg1 = null;
		if (args.length < 2) { // condition to verify #.of arguments
			System.out.println("Not enough arguments");
			return;
		}
		try {
			ClassPool pool = ClassPool.getDefault();
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
				System.out.println(argswithcommon);
			}

			if (argswithcommon == 1) {
				insertClassPath(pool);
				CtClass cc = pool.get(subclass);
//				setSuperclass(cc, superclass, pool);
				setSuperClass(subclass,superclass);
				cc.writeFile(outputDir);
				System.out.println("[DBG] write output to: " + outputDir);
			} else if (argswithcommon == 2) {
				for (int i = 0; i < args.length; i++) {
					if (arg0.length() > arg1.length()) {
						superclass = "target." + arg0;
						subclass = "target." + arg1;
						insertClassPath(pool);
						CtClass cc = pool.get(subclass);
//						setSuperclass(cc, superclass, pool);
						setSuperClass(subclass,superclass);
						cc.writeFile(outputDir);
						System.out.println("[DBG] write output to: " + outputDir);
					} else {
						superclass = "target." + arg0;
						subclass = "target." + arg1;
						insertClassPath(pool);
						CtClass cc = pool.get(subclass);
//						setSuperclass(cc, superclass, pool);
						setSuperClass(subclass,superclass);
						cc.writeFile(outputDir);
						System.out.println("[DBG] write output to: " + outputDir);
					}
				}
			} else {
				insertClassPath(pool);
				superclass = "target." + arg0;
				subclass = "target." + arg1;
				CtClass cc = pool.get(subclass);
//				setSuperclass(cc, superclass, pool);
				setSuperClass(subclass,superclass);
				cc.writeFile(outputDir);
				System.out.println("[DBG] write output to: " + outputDir);
			}

			System.out.println("[DBG] write output to: " + outputDir);
		} catch (NotFoundException | CannotCompileException | IOException e) {
			e.printStackTrace();
		}
	}


	static void insertClassPath(ClassPool pool) throws NotFoundException {
		String strClassPath = workDir + _S + "classfiles";
		pool.insertClassPath(strClassPath);
		System.out.println("[DBG] insert classpath: " + strClassPath);
	}

//	static void setSuperclass(CtClass curClass, String superClass, ClassPool pool)
//			throws NotFoundException, CannotCompileException {
//		curClass.setSuperclass(pool.get(superClass));
//		System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
//				", subclass: " + curClass.getName());
//	}
	
	static void setSuperClass(String clazSub, String clazSuper) {
	      try {
	         ClassPool pool = ClassPool.getDefault();
	         insertClassPath(pool);

	         CtClass ctClazSub = pool.get("target." + clazSub);
	         CtClass ctClazSuper = pool.get("target." + clazSuper);
	         ctClazSub.setSuperclass(ctClazSuper);
	         System.out.println("[DBG] set superclass: " //
	               + ctClazSub.getSuperclass().getName() //
	               + ", subclass: " + ctClazSub.getName());

	         ctClazSub.writeFile(outputDir);
	         System.out.println("[DBG] write output to: " + outputDir);
	      } catch (NotFoundException | CannotCompileException | IOException e) {
	         e.printStackTrace();
	      }
	   }


}
