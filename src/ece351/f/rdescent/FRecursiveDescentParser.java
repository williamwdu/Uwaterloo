/* *********************************************************************
 * ECE351 
 * Department of Electrical and Computer Engineering 
 * University of Waterloo 
 * Term: Summer 2016 (1165)
 *
 * The base version of this file is the intellectual property of the
 * University of Waterloo. Redistribution is prohibited.
 *
 * By pushing changes to this file I affirm that I am the author of
 * all changes. I affirm that I have complied with the course
 * collaboration policy and have not plagiarized my work. 
 *
 * I understand that redistributing this file might expose me to
 * disciplinary action under UW Policy 71. I understand that Policy 71
 * allows for retroactive modification of my final grade in a course.
 * For example, if I post my solutions to these labs on GitHub after I
 * finish ECE351, and a future student plagiarizes them, then I too
 * could be found guilty of plagiarism. Consequently, my final grade
 * in ECE351 could be retroactively lowered. This might require that I
 * repeat ECE351, which in turn might delay my graduation.
 *
 * https://uwaterloo.ca/secretariat-general-counsel/policies-procedures-guidelines/policy-71
 * 
 * ********************************************************************/

package ece351.f.rdescent;

import ece351.common.ast.AndExpr;
import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.ConstantExpr;
import ece351.common.ast.Constants;
import ece351.common.ast.Expr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.OrExpr;
import ece351.common.ast.VarExpr;
import ece351.f.ast.FProgram;
import ece351.util.CommandLine;
import ece351.util.Lexer;



public final class FRecursiveDescentParser implements Constants {
   
	// instance variables
	private final Lexer lexer;

    public FRecursiveDescentParser(String... args) {
    	final CommandLine c = new CommandLine(args);
        lexer = new Lexer(c.readInputSpec());
    }
    
    public FRecursiveDescentParser(final Lexer lexer) {
        this.lexer = lexer;
    }

    public static void main(final String arg) {
    	main(new String[]{arg});
    }
    
    public static void main(final String[] args) {
    	parse(args);
    }

    public static FProgram parse(final String... args) {
        final FRecursiveDescentParser p = new FRecursiveDescentParser(args);
        return p.parse();
    }
    
    public FProgram parse() {
        return program();
    }

    FProgram program() {
    	FProgram fp = new FProgram();
    	do {
        	fp = fp.append(formula());
        } while (!lexer.inspectEOF());
        lexer.consumeEOF();
        assert fp.repOk();
        return fp;
    }

    AssignmentStatement formula() {
        final VarExpr var = var();
        lexer.consume("<=");
        final Expr expr = expr();
        lexer.consume(";");
        System.out.print(var+" <= "); //test
        System.out.println(expr);
        return new AssignmentStatement(var, expr);
    }
    
    Expr expr() {
    	Expr returned;
    	returned = term();
    	while (lexer.inspect(OR)){
    		lexer.consume(OR);
    		Expr right = term();
    		returned = new OrExpr(returned,right);
    	}
    	return returned;
    } // TODO // TODO: replace this stub
    Expr term() {
    	Expr returned;
    	returned = factor();
    	while (lexer.inspect(AND)){
    		lexer.consume(AND);
    		Expr right = factor();
    		returned = new AndExpr(returned,right);
    	}
    	return returned;
    } // TODO // TODO: replace this stub
    Expr factor() {
    	Expr returned;
    	if(lexer.inspect(NOT)){
    		lexer.consume(NOT);
    		Expr newfactor =factor();
    		returned = new NotExpr(newfactor);
    		return returned;
    	}
		else{
			if(lexer.inspect("(")){
				lexer.consume("(");
				returned = expr();
				lexer.consume(")");
				return returned;
			}
			else{
				if(lexer.inspect("'")){
					returned = constant();
					return returned;
				}
				else{
				returned = var();
				return returned;
				}
			}
		}
    } // TODO // TODO: replace this stub
    VarExpr var() {
    	String input = "";
    	while (lexer.inspectID()){
    	input = input + lexer.consumeID();
    	}
    	return new VarExpr(input);
    } // TODO // TODO: replace this stub
    ConstantExpr constant() { 
    	String input = "";
    	if(lexer.inspect("'")){
    }
		lexer.consume("'");
		if(lexer.inspect("0","1")){
    		input = lexer.consume("1","0");
    		lexer.consume("'");
    		
    	}
		if (input == "1"){
			return ConstantExpr.TrueExpr;
		}
		else{
			return ConstantExpr.FalseExpr;
		}
    } // TODO // TODO: replace this stub

    // helper functions
    private boolean peekConstant() {
        return lexer.inspect("'");
    }

}

