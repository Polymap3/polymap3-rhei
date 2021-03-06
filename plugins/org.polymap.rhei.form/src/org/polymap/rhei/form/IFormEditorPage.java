/* 
 * polymap.org
 * Copyright 2010, Falko Br�utigam, and other contributors as indicated
 * by the @authors tag.
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
 *
 * $Id: $
 */
package org.polymap.rhei.form;

import org.eclipse.jface.action.Action;

/**
 * Provides a page for a {@link FormEditor}. The form content consists of form fields
 * that are created via
 * {@link IFormEditorPageSite#newFormField(org.eclipse.swt.widgets.Composite, org.opengis.feature.Property, org.polymap.rhei.field.IFormField, org.polymap.rhei.field.IFormFieldValidator)
 * IFormEditorPageSite.newFormField()}.
 * <p/>
 * If you need more control over UI elements and/or submit/load then consider the
 * {@link IFormEditorPage2} interface.
 * <p/>
 * The abstract class {@link DefaultFormEditorPage} provides ready-to-use
 * implementations of some methods and some useful tools.
 * 
 * @author <a href="http://www.polymap.de">Falko Br�utigam</a>
 */
public interface IFormEditorPage {

    String getTitle();


    /**
     * An array of actions that are contributed to the toolbar of the editor of
     * this page.
     * 
     * @return The actions to be added to the toolbar of the editor of this
     *         page, or null if no actions are to be contributed.
     */
    Action[] getEditorActions();
    
    String getId();


    /**
     * The priority of this page within the entiry editor of a feature.
     * 
     * @return A value of 0 specifies that this page has no special priority;
     *         {@link Byte#MAX_VALUE} that this page is shown "on top" of the stack
     *         of all pages and it is shown by default.
     */
    byte getPriority();
    
    /**
     * Creates the user interface of this form page.
     * <p>
     * <b>Example code:</b>
     * <pre>
     *    site.setFormTitle( "Title" );
     *    site.getPageBody().setLayout( new FormLayout() );
     *
     *    Composite field = site.newFormFiel( null, "", new TextFormField(), null );
     *    field.setLayoutData( layoutData );
     * </pre>
     *
     * @param site The API to create fields and interact with the framework.
     */
    void createFormContent( IFormEditorPageSite site );

}
