package jacc;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import java.io.*;
import java.util.ArrayList;

//takes all jacc files in a given directory and converts them to token and parser files.
@Mojo (name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class JaccMojo
    extends AbstractMojo
{
	
	@Parameter (defaultValue = "${basedir}/resources/", required = false)
	private File sourceDirectory;
	
	@Parameter (defaultValue = "true", required = false)
	private boolean timestamp;
	
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;
	
	private static final String FILE_EXTENSION= ".jacc";


    public void execute()
        throws MojoExecutionException
    {
        File f = sourceDirectory;
        String [] files = f.list();
        ArrayList<String> validJacc = new ArrayList<String>();
        if(files == null) System.out.println("Error, no files contained in source directory.");
        else {
	        for(String fname : files)
	        	if(fname.contains(FILE_EXTENSION))
	        		validJacc.add(f.getPath() +"/"+ fname);
	        	if(validJacc.isEmpty()) System.out.println("Error, no valid JACC files.");
	        	else {
	        		for(String e : validJacc) {
	        			System.out.println("Converting " + e + "...");
	        	
	        			String [] arr = {e};
	        			if(!timestamp) arr = new String [] {e, "-z"};
		        	
	        			jacc.CommandLine.main(arr);
	        	}
	        }
        }
       
    }
}
