// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc;

import jacc.grammar.Grammar;
import jacc.grammar.LookaheadMachine;
import jacc.grammar.LR0Machine;
import jacc.grammar.SLRMachine;
import jacc.grammar.LALRMachine;

public class Settings {

    //- Type of generated machine ---------------------------------------------
    private int machineType = LALR1;

    public static final int LR0   = 0;

    public static final int SLR1  = 1;

    public static final int LALR1 = 2;
    
    public boolean timestamp = true;

    public void setMachineType(int machineType) {
        this.machineType = machineType;
    }

    public int getMachineType() {
        return machineType;
    }

    public LookaheadMachine makeMachine(Grammar grammar) {
        if (machineType == LR0) {
            return new LR0Machine(grammar);
        } else if (machineType == SLR1) {
            return new SLRMachine(grammar);
        } else {
            return new LALRMachine(grammar);
        }
    }


    //- Name of the package for generated classes -----------------------------
    private String packageName;

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }


    //- Name of the parser class ----------------------------------------------
    private String className;

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }


    //- Name of the tokens interface ------------------------------------------
    private String interfaceName;

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }


    //- Name of the parser's base class ---------------------------------------
    private String extendsName;

    public void setExtendsName(String extendsName) {
        this.extendsName = extendsName;
    }

    public String getExtendsName() {
        return extendsName;
    }


    //- The list of interfaces implemented by the parser ----------------------
    private String implementsNames;

    public void setImplementsNames(String implementsNames) {
        this.implementsNames = implementsNames;
    }

    public void addImplementsNames(String implementsNames) {
        if (this.implementsNames!=null) {
            this.implementsNames += ", " + implementsNames;
        } else {
            this.implementsNames = implementsNames;
        }
    }

    public String getImplementsNames() {
        return implementsNames;
    }


    //- Name of the type used for semantic values -----------------------------
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    //- The text that is used to retrieve the current token -------------------
    private String getToken;

    public String getGetToken() {
        return getToken;
    }

    public void setGetToken(String getToken) {
        this.getToken = getToken;
    }


    //- The text that is used to retrieve the next token ----------------------
    private String nextToken;

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public String getNextToken() {
        return nextToken;
    }


    //- The text that is used to retrieve the value of the current token ------
    private String getSemantic;

    public void setGetSemantic(String getSemantic) {
        this.getSemantic = getSemantic;
    }

    public String getGetSemantic() {
        return getSemantic;
    }


    //- The text that precedes the parser class declaration -------------------
    private StringBuffer preTextBuffer = new StringBuffer();

    public void addPreText(String preText) {
        preTextBuffer.append(preText);
    }

    public String getPreText() {
        return preTextBuffer.toString();
    }


    //- The text that appears at the end of the parser class ------------------
    private StringBuffer postTextBuffer = new StringBuffer();

    public void addPostText(String postText) {
        postTextBuffer.append(postText);
    }

    public String getPostText() {
        return postTextBuffer.toString();
    }


    //- Defaults for unspecified settings -------------------------------------

    public void fillBlanks(String name) {
        if (getClassName()==null) {
            setClassName(name + "Parser");
        }
        if (getInterfaceName()==null) {
            setInterfaceName(name + "Tokens");
        }
        if (getTypeName()==null) {
            setTypeName("Object");
        }
        if (getInterfaceName()!=null) {
            addImplementsNames(getInterfaceName());
        }
        if (getGetSemantic()==null) {
            setGetSemantic("lexer.getSemantic()");
        }
        if (getGetToken()==null) {
            setGetToken("lexer.getToken()");
        }
        if (getNextToken()==null) {
            setNextToken("lexer.nextToken()");
        }
    }
}
