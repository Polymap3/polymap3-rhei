/* 
 * polymap.org
 * Copyright 2012, Polymap GmbH. All rights reserved.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.polymap.rhei.ide.java;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

import org.polymap.rhei.ide.RheiIdePlugin;

/**
 * Provides static helpers to create Scripts project and its JavaNature.
 *
 * @author <a href="http://www.polymap.de">Falko Br�utigam</a>
 */
public class JavaProjectInitializer {

    private static Log log = LogFactory.getLog( JavaProjectInitializer.class );


    /**
     * 
     * <p/>
     * In order to delete the project delete also
     * ./.metadata/.plugins/org.eclipse.core.resources/.projects/Scripts/
     * 
     * @throws CoreException 
     * @throws IOException 
     */
    public static void initScriptsProject( IProject project ) 
    throws CoreException, IOException {
        //http://sdqweb.ipd.kit.edu/wiki/JDT_Tutorial:_Creating_Eclipse_Java_Projects_Programmatically
        IProjectDescription description = project.getDescription();
        
        String[] natureIds = description.getNatureIds();
        
        if (!ArrayUtils.contains( natureIds, JavaCore.NATURE_ID )) {
            description.setNatureIds( (String[])ArrayUtils.add( natureIds, JavaCore.NATURE_ID ) );
            project.setDescription( description, null );

            // java project
            IJavaProject javaProject = JavaCore.create( project ); 

            // src folder
            IFolder srcFolder = project.getFolder( "src" );
            IPackageFragmentRoot srcRoot = javaProject.getPackageFragmentRoot( srcFolder );

            // output folder
            IFolder binFolder = project.getFolder( RheiIdePlugin.BUILD_FOLDER_NAME );
            binFolder.create( false, true, null );
            javaProject.setOutputLocation( binFolder.getFullPath(), null );

            // classpath: JRE container + BUNDLES container + src
            List<IClasspathEntry> entries = new ArrayList();
            entries.add( JavaCore.newContainerEntry( new Path( 
                    JREClasspathContainerInitializer.ID ) ) );
            entries.add( JavaCore.newContainerEntry( new Path( 
                    BundlesClasspathContainerInitializer.ID ) ) );
            entries.add( JavaCore.newSourceEntry( srcRoot.getPath() ) );
            javaProject.setRawClasspath(
                    entries.toArray( new IClasspathEntry[entries.size()] ), null );

            srcRoot.createPackageFragment( "procs", true, null );
        }
    }
    
}
