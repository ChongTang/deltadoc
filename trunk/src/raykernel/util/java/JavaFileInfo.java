package raykernel.util.java;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import raykernel.apps.docinf.ast.MethodExtractor;
import raykernel.apps.docinf.ast.MethodReceiver;

public class JavaFileInfo implements MethodReceiver
{
	static ASTParser parser = ASTParser.newParser(AST.JLS3);

	List<MethodDeclaration> methods = new LinkedList<MethodDeclaration>();

	public JavaFileInfo(String file)
	{
		//System.out.println("getting info for: " + file);

		parser.setSource(file.toCharArray());
		CompilationUnit comp = (CompilationUnit) parser.createAST(null);

		MethodExtractor me = new MethodExtractor(this);

		comp.accept(me);
	}

	public String getName()
	{
		String className = "unknown";
		MethodDeclaration meth = methods.get(0);

		//type
		ASTNode parent = meth.getParent();
		className = "anonymous";

		if (parent instanceof TypeDeclaration)
		{
			TypeDeclaration typedec = (TypeDeclaration) parent;

			while (!(parent instanceof CompilationUnit))
				parent = parent.getParent();

			CompilationUnit n = (CompilationUnit) parent;

			try
			{
				className = n.getPackage().getName().toString() + "." + typedec.getName().toString();
			}
			catch (Exception e)
			{

			}
		}

		return className;
	}
	public void receiveMethod(MethodDeclaration md)
	{
		methods.add(md);
	}
}
