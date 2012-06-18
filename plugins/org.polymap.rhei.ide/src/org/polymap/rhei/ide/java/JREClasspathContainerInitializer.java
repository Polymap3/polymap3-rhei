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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * 
 *
 * @author <a href="http://www.polymap.de">Falko Br�utigam</a>
 */
public class JREClasspathContainerInitializer
        extends ClasspathContainerInitializer {

    private static Log log = LogFactory.getLog( JREClasspathContainerInitializer.class );

    public static final String      ID = "org.polymap.rhei.ide.JRE_CONTAINER";
    

    public JREClasspathContainerInitializer() {
    }


    public void initialize( IPath path, IJavaProject project )
    throws CoreException {
        log.info( "<JRE_CONTAINER> initialize()");
        log.info( "\tPath: " + path.toString());
        log.info( "\tProj: " + project.getProject().getName());

        if (path.segmentCount() > 0) {
            if (path.segment(0).equals( ID )) {
                JREClasspathContainer container = new JREClasspathContainer();
                JavaCore.setClasspathContainer( path, new IJavaProject[] {project}, 
                        new IClasspathContainer[] {container}, null);
            }
        }
    }
    
}
